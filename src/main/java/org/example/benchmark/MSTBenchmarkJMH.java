package org.example.benchmark;

import org.example.algorithms.Kruskal;
import org.example.algorithms.Prim;
import org.example.io.GraphReader;
import org.example.metrics.PerformanceTracker;
import org.example.model.EdgeWeightedGraph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class MSTBenchmarkJMH {

    @Param({"input/small_graph.json", "input/meduim_graph.json", "input/large_graph.json"})
    private String inputFile;

    private List<EdgeWeightedGraph> graphs;

    @Setup(Level.Trial)
    public void setup() throws IOException {
        GraphReader reader = new GraphReader();
        graphs = reader.fromJson(inputFile);
    }

    @Benchmark
    public void primBenchmark(Blackhole bh) {
        for (EdgeWeightedGraph graph : graphs) {
            Prim prim = new Prim(graph, new PerformanceTracker(false));
            bh.consume(prim.edges());
        }
    }

    @Benchmark
    public void kruskalBenchmark(Blackhole bh) {
        for (EdgeWeightedGraph graph : graphs) {
            Kruskal kruskal = new Kruskal(graph, new PerformanceTracker(false));
            bh.consume(kruskal.edges());
        }
    }
}
