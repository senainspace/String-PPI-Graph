package metrics;

import graph.*;
import java.util.*;

public class PPIGraphMetrics {

    public void calculateMetrics(Graph graph) {
        System.out.println("DEBUG: Starting metric calculations...");

        List<Protein> vertices = graph.getVertices();
        List<Edge> edges = graph.getEdges();

        // 1. Vertex Count
        int vertexCount = vertices.size();
        System.out.println("1. Vertex Count: " + vertexCount);

        // 2. Edge Count
        int edgeCount = edges.size();
        System.out.println("2. Edge Count: " + edgeCount);

        // 3. Average Degree
        double avgDegree = (vertexCount == 0) ? 0 : (double) edgeCount / vertexCount;
        System.out.println("3. Average Degree: " + String.format("%.2f", avgDegree));

        // 4. Reciprocity
        System.out.println("DEBUG: Calculating Reciprocity...");
        double reciprocity = calculateReciprocity(edges);
        System.out.println("4. Reciprocity: " + String.format("%.4f", reciprocity));

        // 5. Diameter
        System.out.println("DEBUG: Calculating Diameter (Iterating all nodes)...");
        int diameter = calculateDiameter(graph);
        System.out.println("5. Diameter: " + diameter);
    }

    private double calculateReciprocity(List<Edge> edges) {
        if (edges.isEmpty()) return 0.0;

        Set<String> edgeSet = new HashSet<>();
        for (Edge e : edges) {
            edgeSet.add(e.getSource().getId() + "|" + e.getDestination().getId());
        }

        int mutualEdges = 0;
        for (Edge e : edges) {
            String reverseKey = e.getDestination().getId() + "|" + e.getSource().getId();
            if (edgeSet.contains(reverseKey)) {
                mutualEdges++;
            }
        }
        return (double) mutualEdges / edges.size();
    }

    private int calculateDiameter(Graph graph) {
        Map<Protein, List<Protein>> adjMap = new HashMap<>();
        for (Edge e : graph.getEdges()) {
            adjMap.putIfAbsent(e.getSource(), new ArrayList<>());
            adjMap.get(e.getSource()).add(e.getDestination());
        }

        int maxDistance = 0;
        int processed = 0;

        for (Protein startNode : graph.getVertices()) {
            int maxFromNode = bfsMaxDist(startNode, adjMap);
            if (maxFromNode > maxDistance) {
                maxDistance = maxFromNode;
            }

            processed++;
            if (processed % 100 == 0) System.out.print(".");
        }
        System.out.println();
        return maxDistance;
    }

    private int bfsMaxDist(Protein start, Map<Protein, List<Protein>> adjMap) {
        Map<Protein, Integer> distances = new HashMap<>();
        Queue<Protein> queue = new LinkedList<>();

        queue.add(start);
        distances.put(start, 0);
        int localMax = 0;

        while (!queue.isEmpty()) {
            Protein u = queue.poll();
            int d = distances.get(u);
            if (d > localMax) localMax = d;

            if (adjMap.containsKey(u)) {
                for (Protein v : adjMap.get(u)) {
                    if (!distances.containsKey(v)) {
                        distances.put(v, d + 1);
                        queue.add(v);
                    }
                }
            }
        }
        return localMax;
    }
}