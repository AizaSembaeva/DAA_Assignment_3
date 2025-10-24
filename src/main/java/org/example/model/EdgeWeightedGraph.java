package org.example.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EdgeWeightedGraph {
    private final int V;
    private int E;
    private final List<List<Edge>> adj;

    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
        this.V = V;
        this.E = 0;
        this.adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        adj.get(v).add(e);
        adj.get(w).add(e);
        E++;
    }

    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return Collections.unmodifiableList(adj.get(v));
    }

    public Iterable<Edge> edges() {
        List<Edge> list = new ArrayList<>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj.get(v)) {
                if (e.other(v) > v) list.add(e);
            }
        }
        return list;
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public boolean isConnected() {
        if (V == 0) return true;

        boolean[] visited = new boolean[V];
        dfs(0, visited);
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(int v, boolean[] visited) {
        visited[v] = true;
        for (Edge e : adj(v)) {
            int w = e.other(v);
            if (!visited[w]) dfs(w, visited);
        }
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= V) throw new IllegalArgumentException("Vertex " + v + " is not between 0 and " + (V - 1));
    }
}

