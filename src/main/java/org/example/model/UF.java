package org.example.model;

import org.example.metrics.PerformanceTracker;

public class UF {
    private final int[] parent;
    private final int[] rank;
    private final int n;
    private final PerformanceTracker tracker;

    public UF(int n, PerformanceTracker tracker) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of elements must be positive");
        }
        this.n = n;
        this.tracker = tracker;
        this.parent = new int[n];
        this.rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
            if (tracker != null) tracker.incArrayAccesses(2);
        }
    }

    public UF(int n) {
        this(n, null);
    }

    public int find(int x) {
        if (x < 0 || x >= n) {
            throw new IllegalArgumentException("Index out of bounds: " + x);
        }
        if (tracker != null) tracker.incArrayAccesses();
        if (parent[x] != x) {
            if (tracker != null) tracker.incComparisons();
            int root = find(parent[x]);
            if (tracker != null) tracker.incArrayAccesses();
            parent[x] = root;
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (tracker != null) tracker.incComparisons();
        if (rootX == rootY) return;

        if (tracker != null) tracker.incUnions();

        if (tracker != null) tracker.incArrayAccesses(2);
        if (tracker != null) tracker.incComparisons();

        if (rank[rootX] < rank[rootY]) {
            if (tracker != null) tracker.incArrayAccesses();
            parent[rootX] = rootY;

        } else if (rank[rootX] > rank[rootY]) {
            if (tracker != null) tracker.incArrayAccesses();
            parent[rootY] = rootX;

        } else {
            if (tracker != null) tracker.incArrayAccesses();
            parent[rootY] = rootX;
            if (tracker != null) tracker.incArrayAccesses();
            rank[rootX]++;
        }
    }


    public boolean connected(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (tracker != null) tracker.incComparisons();
        return rootX == rootY;
    }

    public int size() {
        return n;
    }
}
