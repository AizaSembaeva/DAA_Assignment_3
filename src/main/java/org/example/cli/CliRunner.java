package org.example.cli;

import org.example.algorithms.Kruskal;
import org.example.algorithms.Prim;
import org.example.io.GraphReader;
import org.example.io.GraphWriter;
import org.example.metrics.PerformanceTracker;
import org.example.model.EdgeWeightedGraph;
import java.io.IOException;
import java.util.List;


public class CliRunner {
    public static void main(String[] args) throws IOException {
        String inputFile = null;
        String outputFile = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--input" -> inputFile = args[++i];
                case "--output" -> outputFile = args[++i];
            }
        }

        if (inputFile == null || outputFile == null) {
            System.err.println("Usage: --input <input.json> --output <output.json>");
            return;
        }

        GraphReader reader = new GraphReader();
        GraphWriter writer = new GraphWriter();

        List<EdgeWeightedGraph> graphs = reader.fromJson(inputFile);

        for (EdgeWeightedGraph graph : graphs) {
            PerformanceTracker primTracker = new PerformanceTracker();
            Prim primMST = new Prim(graph, primTracker);

            writer.writeResult(
                    outputFile,
                    graph,
                    "prim",
                    primMST.edges(),
                    primMST.weight(),
                    primTracker
            );

            PerformanceTracker kruskalTracker = new PerformanceTracker();
            Kruskal kruskalMST = new Kruskal(graph, kruskalTracker);

            writer.writeResult(
                    outputFile,
                    graph,
                    "kruskal",
                    kruskalMST.edges(),
                    kruskalMST.weight(),
                    kruskalTracker
            );

            System.out.println("Processed graph " + graph.getId());
        }

        System.out.println("Benchmark completed. Output written to " + outputFile);
    }
}
