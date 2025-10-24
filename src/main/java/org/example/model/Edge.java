package org.example.model;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    private final int v;
    private final int w;
    private final double weight;

    public Edge(int v, int w, double weight) {
        if (v < 0 || w < 0) throw new IllegalArgumentException("Vertex index must be non-negative");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int either() {
        return v;
    }

    public int other(int vertex) {
        if (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new IllegalArgumentException("Invalid endpoint: " + vertex);
    }

    public double weight() {
        return weight;
    }

    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    @Override
    public String toString() {
        return String.format("%d-%d %.2f", v, w, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge that = (Edge) o;
        return (v == that.v && w == that.w || v == that.w && w == that.v)
                && Double.compare(weight, that.weight) == 0;
    }

    @Override
    public int hashCode() {
        int min = Math.min(v, w);
        int max = Math.max(v, w);
        return Objects.hash(min, max, weight);
    }
}

