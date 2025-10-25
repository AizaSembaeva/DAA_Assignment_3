package org.example.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GraphWriter {

    private final ObjectMapper mapper = new ObjectMapper();

    public void writeResult(
            String outputFile,
            EdgeWeightedGraph graph,
            String algorithmName,
            Iterable<Edge> mstEdges,
            double totalCost,
            PerformanceTracker tracker
    ) throws IOException {
        File file = new File(outputFile);
        ObjectNode root;

        if (file.exists() && file.length() > 0) {
            root = (ObjectNode) mapper.readTree(file);
        } else {
            root = mapper.createObjectNode();
        }

        String datasetName = "graph_" + graph.getId();
        ObjectNode datasetNode = (ObjectNode) root.get(datasetName);
        if (datasetNode == null) {
            datasetNode = mapper.createObjectNode();
            root.set(datasetName, datasetNode);
        }

        ObjectNode algoNode = mapper.createObjectNode();

        ArrayNode edgesArray = mapper.createArrayNode();
        Map<Integer, String> indexToName = graph.getIndexToName();
        for (Edge e : mstEdges) {
            ObjectNode eNode = mapper.createObjectNode();
            eNode.put("from", indexToName.get(e.v()));
            eNode.put("to", indexToName.get(e.w()));
            eNode.put("weight", e.weight());
            edgesArray.add(eNode);
        }

        algoNode.set("mst_edges", edgesArray);
        algoNode.put("total_cost", totalCost);
        algoNode.put("vertices", graph.V());
        algoNode.put("edges", graph.E());
        algoNode.put("comparisons", tracker.getComparisons());
        algoNode.put("unions", tracker.getUnions());
        algoNode.put("array_accesses", tracker.getArrayAccesses());
        algoNode.put("allocations", tracker.getAllocations());
        algoNode.put("execution_time_ms", tracker.getExecutionTimeMs());

        datasetNode.set(algorithmName, algoNode);

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
    }
}
