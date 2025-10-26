package org.example.algorithms;

import org.example.io.GraphReader;
import org.example.metrics.PerformanceTracker;
import org.example.model.EdgeWeightedGraph;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrimVsKruskalConsistencyTest {

    @Test
    void testPrimAndKruskalConsistency() throws IOException {
        GraphReader reader = new GraphReader();
        List<EdgeWeightedGraph> graphs = reader.fromJson("input/small_graph.json");

        for (EdgeWeightedGraph g : graphs) {
            PerformanceTracker t1 = new PerformanceTracker();
            PerformanceTracker t2 = new PerformanceTracker();

            Prim prim = new Prim(g, t1);
            Kruskal kruskal = new Kruskal(g, t2);

            double w1 = prim.weight();
            double w2 = kruskal.weight();

            assertEquals(w1, w2, 1e-6, "Prim and Kruskal should produce same MST weight");

            long primEdges = prim.edges().spliterator().getExactSizeIfKnown();
            long kruskalEdges = kruskal.edges().spliterator().getExactSizeIfKnown();

            assertTrue(primEdges <= g.V() - 1);
            assertTrue(kruskalEdges <= g.V() - 1);
        }
    }
}
