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
        }
    }

    public UF(int n) {
        this(n, null);
    }

    public int find(int x) {
        if (x < 0 || x >= n) {
            throw new IllegalArgumentException("Index out of bounds: " + x);
        }
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX == rootY) return;

        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
    }

    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    public int size() {
        return n;
    }
}
