package org.example.algorithms;

import org.example.io.GraphReader;
import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PrimMSTTest {

    @Test
    void testPrimMST_CorrectnessAndStructure() throws IOException {
        GraphReader reader = new GraphReader();
        List<EdgeWeightedGraph> graphs = reader.fromJson("input/small_graph.json");

        for (EdgeWeightedGraph g : graphs) {
            PerformanceTracker tracker = new PerformanceTracker();
            Prim prim = new Prim(g, tracker);

            double totalWeight = prim.weight();
            Iterable<Edge> edges = prim.edges();

            int edgeCount = 0;
            Set<Integer> visited = new HashSet<>();

            for (Edge e : edges) {
                edgeCount++;
                visited.add(e.either());
                visited.add(e.other(e.either()));
            }

            assertTrue(edgeCount <= g.V() - 1, "MST edge count should not exceed V-1");
            assertTrue(totalWeight >= 0, "MST total weight must be non-negative");

            if (edgeCount == g.V() - 1)
                assertEquals(g.V(), visited.size(), "All vertices must be connected in MST");

            assertTrue(tracker.getExecutionTimeMs() >= 0);
            assertTrue(tracker.getArrayAccesses() >= 0);
            assertTrue(tracker.getComparisons() >= 0);
        }
    }
}
