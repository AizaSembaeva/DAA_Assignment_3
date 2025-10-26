package org.example.algorithms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.io.GraphReader;
import org.example.io.GraphWriter;
import org.example.metrics.PerformanceTracker;
import org.example.model.EdgeWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OutputFormatTest {

    @Test
    void testGraphWriterJSONFormat() throws IOException {
        GraphReader reader = new GraphReader();
        List<EdgeWeightedGraph> graphs = reader.fromJson("input/small_graph.json");

        EdgeWeightedGraph g = graphs.get(0);
        PerformanceTracker tracker = new PerformanceTracker();

        Prim prim = new Prim(g, tracker);
        String outputFile = "input/output_test.json";

        new GraphWriter().writeResult(
                outputFile, g, "Prim", prim.edges(), prim.weight(), tracker
        );

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(outputFile));

        String datasetKey = "graph_" + g.getId();
        assertTrue(root.has(datasetKey));

        JsonNode algoNode = root.get(datasetKey).get("Prim");
        assertNotNull(algoNode);

        assertTrue(algoNode.has("mst_edges"));
        assertTrue(algoNode.has("total_cost"));
        assertTrue(algoNode.has("execution_time_ms"));
        assertTrue(algoNode.has("comparisons"));

        assertTrue(algoNode.get("total_cost").asDouble() >= 0);
        assertTrue(algoNode.get("execution_time_ms").asLong() >= 0);
    }
}
