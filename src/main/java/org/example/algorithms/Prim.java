package org.example.algorithms;

import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;
import org.example.metrics.PerformanceTracker;

import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Prim {
    private final List<Edge> mst;
    private double weight;
    private final boolean[] marked;
    private final Queue<Edge> pq;
    private final PerformanceTracker tracker;

    public Prim(EdgeWeightedGraph G, PerformanceTracker tracker) {
        this.tracker = tracker;
        tracker.start();

        int V = G.V();

        marked = new boolean[V];
        mst = new ArrayList<>();
        pq = new PriorityQueue<>();
        tracker.incAllocations(3);

        if (V > 0) visit(G, 0);

        while (!pq.isEmpty() && mst.size() < V - 1) {
            Edge e = pq.poll();
            tracker.incArrayAccesses();

            int v = e.either(), w = e.other(v);
            tracker.incComparisons();

            if (marked[v] && marked[w]) continue;
            mst.add(e);
            tracker.incAllocations();

            weight += e.weight();
            tracker.incArrayAccesses();

            if (!marked[v]) visit(G, v);
            if (!marked[w]) visit(G, w);
        }

        tracker.stop();
    }

    private void visit(EdgeWeightedGraph G, int v) {
        marked[v] = true;
        tracker.incArrayAccesses();

        for (Edge e : G.adj(v)) {
            tracker.incArrayAccesses();
            int w = e.other(v);

            tracker.incComparisons();
            if (!marked[w]) {
                pq.add(e);
                tracker.incAllocations();
            }
        }
    }

    public Iterable<Edge> edges() {
        return mst;
    }

    public double weight() {
        return weight;
    }
}
