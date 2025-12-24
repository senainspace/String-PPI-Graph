package algorithms;

import graph.*;
import java.util.*;

public class ProteinBFS {

    public void traverse(Graph graph, String startProteinId) {
        Protein startNode = graph.searchProtein(startProteinId);

        if (startNode == null) {
            System.out.println("DEBUG: Start protein (" + startProteinId + ") not found!");
            return;
        }

        System.out.println("DEBUG: Starting BFS from " + startNode.getId());

        // Optimization: Pre-process edges
        Map<Protein, List<Protein>> adjMap = new HashMap<>();
        for (Edge e : graph.getEdges()) {
            adjMap.putIfAbsent(e.getSource(), new ArrayList<>());
            adjMap.get(e.getSource()).add(e.getDestination());
        }

        Set<Protein> visited = new HashSet<>();
        Queue<Protein> queue = new LinkedList<>();

        visited.add(startNode);
        queue.add(startNode);

        System.out.print("BFS Output: ");
        int count = 0;

        while (!queue.isEmpty()) {
            Protein current = queue.poll();
            System.out.print(current.getId() + " -> ");
            count++;

            if (adjMap.containsKey(current)) {
                for (Protein neighbor : adjMap.get(current)) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
        System.out.println("END");
        System.out.println("DEBUG: BFS visited " + count + " proteins.");
    }
}