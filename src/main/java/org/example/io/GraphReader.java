package org.example.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Edge;
import org.example.model.EdgeWeightedGraph;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GraphReader {

    private final ObjectMapper mapper = new ObjectMapper();

    public List<EdgeWeightedGraph> fromJson(String filename) throws IOException {
        JsonNode root = mapper.readTree(new File(filename));
        List<EdgeWeightedGraph> graphs = new ArrayList<>();

        JsonNode graphsArray = root.get("graphs");
        if (graphsArray == null || !graphsArray.isArray()) {
            throw new IllegalArgumentException("Invalid input: missing 'graphs' array");
        }

        for (JsonNode gNode : graphsArray) {
            int id = gNode.get("id").asInt();
            JsonNode nodes = gNode.get("nodes");
            JsonNode edges = gNode.get("edges");

            if (nodes == null || nodes.size() == 0) throw new IllegalArgumentException("Graph " + id + " has no vertices");

            int verticesCount = nodes.size();
            EdgeWeightedGraph graph = new EdgeWeightedGraph(verticesCount);
            graph.setId(id);

            Map<String, Integer> nameToIndex = new HashMap<>();
            Map<Integer, String> indexToName = new HashMap<>();
            for (int i = 0; i < verticesCount; i++) {
                String name = nodes.get(i).asText();
                nameToIndex.put(name, i);
                indexToName.put(i, name);
            }

            for (JsonNode e : edges) {
                String from = e.get("from").asText();
                String to = e.get("to").asText();
                double weight = e.get("weight").asDouble();
                if (!nameToIndex.containsKey(from) || !nameToIndex.containsKey(to))
                    throw new IllegalArgumentException("Edge refers to non-existent vertex");
                if (weight < 0) throw new IllegalArgumentException("Edge weight cannot be negative");

                int v = nameToIndex.get(from);
                int w = nameToIndex.get(to);
                graph.addEdge(new Edge(v, w, weight));
            }

            graph.setNameToIndex(nameToIndex);
            graph.setIndexToName(indexToName);

            graphs.add(graph);
        }

        return graphs;
    }
}
