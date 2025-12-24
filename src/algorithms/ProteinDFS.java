package algorithms;

import graph.*;
import java.util.*;

public class ProteinDFS {

    public void traverse(Graph graph, String startProteinId) {
        Protein startNode = graph.searchProtein(startProteinId);

        if (startNode == null) {
            System.out.println("DEBUG: Start protein (" + startProteinId + ") not found!");
            return;
        }

        System.out.println("DEBUG: Starting DFS from " + startNode.getId());

        // Optimization: Pre-process edges
        Map<Protein, List<Protein>> adjMap = new HashMap<>();
        for (Edge e : graph.getEdges()) {
            adjMap.putIfAbsent(e.getSource(), new ArrayList<>());
            adjMap.get(e.getSource()).add(e.getDestination());
        }

        Set<Protein> visited = new HashSet<>();
        Stack<Protein> stack = new Stack<>();

        stack.push(startNode);

        System.out.print("DFS Output: ");
        int count = 0;

        while (!stack.isEmpty()) {
            Protein current = stack.pop();

            if (!visited.contains(current)) {
                visited.add(current);
                System.out.print(current.getId() + " -> ");
                count++;

                if (adjMap.containsKey(current)) {
                    List<Protein> neighbors = adjMap.get(current);
                    // Push neighbors to stack (reverse order)
                    if (neighbors != null) {
                        for (int i = neighbors.size() - 1; i >= 0; i--) {
                            Protein v = neighbors.get(i);
                            if (!visited.contains(v)) {
                                stack.push(v);
                            }
                        }
                    }
                }
            }
        }
        System.out.println("END");
        System.out.println("DEBUG: DFS visited " + count + " proteins.");
    }
}