package utils;

import graph.Graph;
import graph.Protein;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private Graph graph; // The loaded graph is stored here

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.graph = null; // Initially, no graph is loaded
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayOptions();
            System.out.print("Your Choice (1-7): ");

            String input = scanner.next();
            // nextLine() to clean the buffer
            scanner.nextLine();

            switch (input) {
                case "1":
                    handleLoadGraph();
                    break;
                case "2":
                    handleSearchProtein();
                    break;
                case "3":
                    handleCheckInteraction();
                    break;
                case "4":
                    System.out.println(">> [TODO] Most Confident Path algorithm is not implemented yet.");
                    break;
                case "5":
                    System.out.println(">> [TODO] Graph Metrics (Item 5) are not calculated yet.");
                    break;
                case "6":
                    System.out.println(">> [TODO] BFS & DFS Traversals (Item 6) are not implemented yet.");
                    break;
                case "7":
                    System.out.println("Exiting program...");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            System.out.println("--------------------------------------------------");
        }
        scanner.close();
    }

    private void displayOptions() {
        System.out.println("\n=== STRING PPI GRAPH SYSTEM MENU ===");
        System.out.println("1. Load Graph");
        System.out.println("2. Search for Protein");
        System.out.println("3. Check Interaction");
        System.out.println("4. Find Most Confident Path - [COMING SOON]");
        System.out.println("5. Calculate Graph Metrics - [COMING SOON]");
        System.out.println("6. BFS & DFS Traversal - [COMING SOON]");
        System.out.println("7. Exit");
    }

    // Load Graph
    private void handleLoadGraph() {
        System.out.println("Use default file paths? (Y/N)");
        String choice = scanner.nextLine();

        String infoPath = "data/9606.protein.info.v12.0.txt";
        String linksPath = "data/9606.protein.links.v12.0.txt";

        // "N" means No, user wants to enter paths manually
        if (choice.equalsIgnoreCase("N")) {
            System.out.print("Protein Info File Path: ");
            infoPath = scanner.nextLine();
            System.out.print("Links File Path: ");
            linksPath = scanner.nextLine();
        }

        System.out.print("Confidence Score Threshold (0.0 - 1.0, e.g., 0.7): ");
        double threshold;
        try {
            threshold = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid number format! Using default 0.5.");
            threshold = 0.5;
        }

        System.out.println("Loading graph, please wait...");
        long start = System.currentTimeMillis();

        // Call the Loader utility
        this.graph = PPIGraphLoader.loadGraph(infoPath, linksPath, threshold);

        long end = System.currentTimeMillis();
        System.out.println("Loading Completed! (" + (end - start) + " ms)");
        System.out.println("Total Proteins (Vertices): " + graph.getVertices().size());
        System.out.println("Total Interactions (Edges): " + graph.getEdges().size());
    }

    // Search Protein
    private void handleSearchProtein() {
        if (graph == null) {
            System.out.println("ERROR: You must load the graph first (Option 1).");
            return;
        }

        System.out.print("Enter Protein ID to search (e.g., 9606.ENSP00000000233): ");
        String id = scanner.nextLine().trim();

        Protein p = graph.searchProtein(id);
        if (p != null) {
            System.out.println("------------------------------------------------");
            System.out.println("FOUND: " + p);

            // Get neighbors
            var neighbors = graph.getNeighbors(p);
            System.out.println("Connection Count (Out-Degree): " + neighbors.size());

            // Show first 10 neighbors as proof
            if (!neighbors.isEmpty()) {
                System.out.println("First 10 connected proteins:");
                int count = 0;
                for (Protein neighbor : neighbors) {
                    System.out.println("   -> " + neighbor.getId());
                    count++;
                    if (count >= 10) break;
                }
                if (neighbors.size() > 10) {
                    System.out.println("   ... and " + (neighbors.size() - 10) + " more.");
                }
            } else {
                System.out.println("This protein has no outgoing edges.");
            }
            System.out.println("------------------------------------------------");
        } else {
            System.out.println("Protein not found.");
        }
    }

    // Check Interaction
    private void handleCheckInteraction() {
        if (graph == null) {
            System.out.println("ERROR: You must load the graph first (Option 1).");
            return;
        }

        System.out.print("Source Protein ID: ");
        String id1 = scanner.nextLine().trim();
        System.out.print("Destination Protein ID: ");
        String id2 = scanner.nextLine().trim();

        Protein p1 = graph.searchProtein(id1);
        Protein p2 = graph.searchProtein(id2);

        if (p1 == null || p2 == null) {
            System.out.println("ERROR: One or both proteins do not exist in the graph.");
            return;
        }

        boolean interaction = graph.hasInteraction(p1, p2);
        if (interaction) {
            System.out.println("RESULT: YES, interaction EXISTS between " + p1.getId() + " and " + p2.getId() + ".");
        } else {
            System.out.println("RESULT: NO, there is NO direct interaction between these two proteins.");
        }
    }
}