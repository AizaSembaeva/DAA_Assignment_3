package org.example.algorithms;

import org.example.io.GraphReader;
import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KruskalMSTTest {

    @Test
    void testKruskalMST_CorrectnessAndPerformance() throws IOException {
        GraphReader reader = new GraphReader();
        List<EdgeWeightedGraph> graphs = reader.fromJson("input/small_graph.json");

        for (EdgeWeightedGraph g : graphs) {
            PerformanceTracker tracker = new PerformanceTracker();
            Kruskal kruskal = new Kruskal(g, tracker);

            double totalWeight = kruskal.weight();
            Iterable<Edge> edges = kruskal.edges();

            int edgeCount = 0;
            Set<Integer> vertices = new HashSet<>();
            for (Edge e : edges) {
                edgeCount++;
                vertices.add(e.either());
                vertices.add(e.other(e.either()));
            }

            assertTrue(edgeCount <= g.V() - 1);
            if (edgeCount == g.V() - 1)
                assertEquals(g.V(), vertices.size());

            assertTrue(tracker.getExecutionTimeMs() >= 0);
            assertTrue(tracker.getArrayAccesses() >= 0);
            assertTrue(tracker.getComparisons() >= 0);
            assertTrue(tracker.getAllocations() >= 0);
        }
    }
}
