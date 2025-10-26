package org.example.algorithms;

import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DisconnectedGraphTest {

    @Test
    void testDisconnectedGraphHandledGracefully() {
        EdgeWeightedGraph g = new EdgeWeightedGraph(4);
        g.addEdge(new Edge(0, 1, 1.0));
        g.addEdge(new Edge(2, 3, 2.0));

        PerformanceTracker t1 = new PerformanceTracker();
        PerformanceTracker t2 = new PerformanceTracker();

        Prim prim = new Prim(g, t1);
        Kruskal kruskal = new Kruskal(g, t2);

        assertTrue(prim.weight() >= 0);
        assertTrue(kruskal.weight() >= 0);

        int primEdgeCount = (int) prim.edges().spliterator().getExactSizeIfKnown();
        int kruskalEdgeCount = (int) kruskal.edges().spliterator().getExactSizeIfKnown();

        assertTrue(primEdgeCount < g.V() - 1);
        assertTrue(kruskalEdgeCount < g.V() - 1);
    }
}
