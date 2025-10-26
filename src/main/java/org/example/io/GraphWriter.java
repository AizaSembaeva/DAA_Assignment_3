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
        ArrayNode resultsArray;

        if (file.exists() && file.length() > 0) {
            root = (ObjectNode) mapper.readTree(file);
            resultsArray = (ArrayNode) root.get("results");
            if (resultsArray == null) {
                resultsArray = mapper.createArrayNode();
                root.set("results", resultsArray);
            }
        } else {
            root = mapper.createObjectNode();
            resultsArray = mapper.createArrayNode();
            root.set("results", resultsArray);
        }

        ObjectNode graphNode = null;
        for (int i = 0; i < resultsArray.size(); i++) {
            ObjectNode node = (ObjectNode) resultsArray.get(i);
            if (node.get("graph_id").asInt() == graph.getId()) {
                graphNode = node;
                break;
            }
        }

        if (graphNode == null) {
            graphNode = mapper.createObjectNode();
            graphNode.put("graph_id", graph.getId());

            ObjectNode inputStats = mapper.createObjectNode();
            inputStats.put("vertices", graph.V());
            inputStats.put("edges", graph.E());
            graphNode.set("input_stats", inputStats);

            resultsArray.add(graphNode);
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
        algoNode.put("operations_count",
                tracker.getArrayAccesses() +
                        tracker.getComparisons() +
                        tracker.getAllocations() +
                        tracker.getUnions()
        );
        algoNode.put("execution_time_ms", tracker.getExecutionTimeMs());

        graphNode.set(algorithmName, algoNode);

        mapper.writerWithDefaultPrettyPrinter().writeValue(file, root);
    }
}
