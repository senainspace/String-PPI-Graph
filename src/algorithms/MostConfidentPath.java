package algorithms;

import graph.*;
import java.util.*;

public class MostConfidentPath {

    // Finds the path with the highest cumulative confidence score
    public void findMostConfidentPath(Graph graph, String startId, String endId) {
        System.out.println("DEBUG: Finding most confident path from " + startId + " to " + endId);

        Protein startNode = graph.searchProtein(startId);
        Protein endNode = graph.searchProtein(endId);

        if (startNode == null || endNode == null) {
            System.out.println("DEBUG: One of the proteins does not exist in the graph.");
            return;
        }

        // Optimization: Build an adjacency map locally
        Map<Protein, List<Edge>> adjMap = buildAdjacencyMap(graph);

        Map<Protein, Double> maxConfidence = new HashMap<>();
        Map<Protein, Protein> previous = new HashMap<>();
        Set<Protein> visited = new HashSet<>();

        // Priority Queue (Max-Heap based on confidence score)
        PriorityQueue<Protein> pq = new PriorityQueue<>((p1, p2) ->
                Double.compare(maxConfidence.getOrDefault(p2, 0.0), maxConfidence.getOrDefault(p1, 0.0))
        );

        // Initialize scores
        for (Protein p : graph.getVertices()) {
            maxConfidence.put(p, 0.0);
        }
        maxConfidence.put(startNode, 1.0); // 100% confidence
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Protein current = pq.poll();

            if (current.equals(endNode)) {
                System.out.println("DEBUG: Destination " + endNode.getId() + " reached.");
                break;
            }

            if (visited.contains(current)) continue;
            visited.add(current);

            if (adjMap.containsKey(current)) {
                for (Edge edge : adjMap.get(current)) {
                    Protein neighbor = edge.getDestination();
                    double weight = edge.getWeight();

                    double newScore = maxConfidence.get(current) * weight;

                    if (newScore > maxConfidence.getOrDefault(neighbor, 0.0)) {
                        maxConfidence.put(neighbor, newScore);
                        previous.put(neighbor, current);
                        pq.add(neighbor);
                    }
                }
            }
        }

        printPath(startNode, endNode, previous, maxConfidence);
    }

    private void printPath(Protein start, Protein end, Map<Protein, Protein> previous, Map<Protein, Double> maxConfidence) {
        if (!maxConfidence.containsKey(end) || maxConfidence.get(end) == 0.0) {
            System.out.println("DEBUG: No path found between " + start.getId() + " and " + end.getId());
            return;
        }

        LinkedList<String> path = new LinkedList<>();
        Protein curr = end;
        while (curr != null) {
            path.addFirst(curr.toString());
            curr = previous.get(curr);
        }

        System.out.println("\n--- Most Confident Path Result ---");
        System.out.println("Path: " + String.join(" -> ", path));
        System.out.println("Total Confidence Score: " + String.format("%.4f", maxConfidence.get(end)));
    }

    // Helper to speed up neighbor lookup
    private Map<Protein, List<Edge>> buildAdjacencyMap(Graph graph) {
        Map<Protein, List<Edge>> map = new HashMap<>();
        for (Edge edge : graph.getEdges()) {
            map.putIfAbsent(edge.getSource(), new ArrayList<>());
            map.get(edge.getSource()).add(edge);
        }
        return map;
    }
}