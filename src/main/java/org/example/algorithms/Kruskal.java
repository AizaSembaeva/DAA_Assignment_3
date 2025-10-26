package org.example.algorithms;

import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;
import org.example.model.UF;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kruskal {
    private final List<Edge> mst;
    private double weight;
    private final PerformanceTracker tracker;
    private final boolean disconnected;

    public Kruskal(EdgeWeightedGraph G, PerformanceTracker tracker) {
        this.tracker = tracker;
        tracker.start();

        List<Edge> edges = new ArrayList<>();
        for (Edge e : G.edges()) edges.add(e);
        tracker.incAllocations();

        Collections.sort(edges);
        tracker.incComparisons(edges.size());

        UF uf = new UF(G.V(), tracker);
        tracker.incAllocations();

        mst = new ArrayList<>();
        tracker.incAllocations();

        for (Edge e : edges) {
            int v = e.either();
            int w = e.other(v);
            tracker.incArrayAccesses(2);
            tracker.incComparisons();

            if (!uf.connected(v, w)) {
                uf.union(v, w);
                mst.add(e);
                tracker.incAllocations();
                weight += e.weight();
                tracker.incArrayAccesses();

                if (mst.size() == G.V() - 1) break;
            }
        }

        disconnected = mst.size() != G.V() - 1;
        if (disconnected) weight = 0;

        tracker.stop();
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }

    public boolean isDisconnected() {
        return disconnected;
    }
}
