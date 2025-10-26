package org.example.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonToCsv {
    public static void convertJsonToCsv(String jsonFilePath, String csvFilePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(jsonFilePath)).get("results");

        try (FileWriter writer = new FileWriter(csvFilePath)) {
            writer.append("graph_id,algorithm,vertices,edges,total_cost,operations_count,execution_time_ms\n");

            for (JsonNode graphNode : root) {
                int graphId = graphNode.get("graph_id").asInt();
                int vertices = graphNode.get("input_stats").get("vertices").asInt();
                int edges = graphNode.get("input_stats").get("edges").asInt();

                for (String algorithm : new String[]{"prim", "kruskal"}) {
                    JsonNode algoNode = graphNode.get(algorithm);
                    double totalCost = algoNode.get("total_cost").asDouble();
                    long operations = algoNode.get("operations_count").asLong();
                    double timeMs = algoNode.get("execution_time_ms").asDouble();

                    writer.append(String.join(",",
                            String.valueOf(graphId),
                            algorithm,
                            String.valueOf(vertices),
                            String.valueOf(edges),
                            String.valueOf(totalCost),
                            String.valueOf(operations),
                            String.valueOf(timeMs)
                    ));
                    writer.append("\n");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        convertJsonToCsv("output/small_graph_output.json", "plots/small_graph_output.csv");
        convertJsonToCsv("output/meduim_graph_output.json", "plots/medium_graph_output.csv");
        convertJsonToCsv("output/large_graph_output.json", "plots/large_graph_output.csv");
    }
}
