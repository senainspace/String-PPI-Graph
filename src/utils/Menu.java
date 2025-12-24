package utils;

import graph.Graph;
import graph.Protein;
import algorithms.MostConfidentPath;
import algorithms.ProteinBFS;
import algorithms.ProteinDFS;
import metrics.PPIGraphMetrics;

import java.util.Scanner;

public class Menu {
    private Scanner scanner;
    private Graph graph;

    public Menu() {
        this.scanner = new Scanner(System.in);
        this.graph = null;
    }

    public void start() {
        boolean running = true;
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║       STRING Protein-Protein Interaction Network           ║");
        System.out.println("║                    System Started                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");

        while (running) {
            try {
                displayOptions();
                System.out.print("Your Choice (1-7): ");

                String input = scanner.next();
                scanner.nextLine(); // Buffer temizleme

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
                        handleMostConfidentPath();
                        break;
                    case "5":
                        handleGraphMetrics();
                        break;
                    case "6":
                        handleTraversals();
                        break;
                    case "7":
                        System.out.println("Exiting program... Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println(">> [WARNING] Invalid choice! Please enter 1-7.");
                }

                if (running) {
                    System.out.println("\nPress [ENTER] to continue...");
                    scanner.nextLine();
                }

            } catch (Exception e) {
                System.out.println("\n>> [CRITICAL ERROR] An unexpected error occurred in the main loop.");
                e.printStackTrace();
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void displayOptions() {
        System.out.println("\n=== STRING PPI GRAPH SYSTEM MENU ===");
        System.out.println("1. Load Graph (Start Here)");
        System.out.println("2. Search for Protein");
        System.out.println("3. Check Interaction Between Two Proteins");
        System.out.println("4. Find Most Confident Path");
        System.out.println("5. Calculate Graph Metrics");
        System.out.println("6. BFS & DFS Traversal");
        System.out.println("7. Exit");
        System.out.println("------------------------------------");
    }

    // --- 1. Load Graph ---
    private void handleLoadGraph() {
        System.out.println("\n--- LOAD GRAPH ---");
        try {
            System.out.println("Use default file paths? (Y/N)");
            String choice = scanner.nextLine();

            String infoPath = "data/9606.protein.info.v12.0.txt";
            String linksPath = "data/9606.protein.links.v12.0.txt";

            if (choice.equalsIgnoreCase("N")) {
                System.out.print("Enter Protein Info File Path: ");
                infoPath = scanner.nextLine().trim();
                System.out.print("Enter Links File Path: ");
                linksPath = scanner.nextLine().trim();
            }

            // Threshold Alma - Doğrulama
            double threshold = -1;
            boolean validInput = false;

            while (!validInput) {
                System.out.print("Enter Confidence Score Threshold (0.0 - 1.0): ");
                String input = scanner.nextLine().trim();

                try {
                    if (input.isEmpty()) {
                        System.out.println(">> [WARNING] Threshold cannot be empty. Please enter a value.");
                        continue;
                    }

                    double val = Double.parseDouble(input);
                    if (val < 0.0 || val > 1.0) {
                        System.out.println(">> [WARNING] Please enter a value between 0.0 and 1.0.");
                    } else {
                        threshold = val;
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println(">> [WARNING] Invalid number format. Please enter a decimal number (e.g., 0.5).");
                }
            }

            System.out.println("Loading graph with threshold " + threshold + ", please wait...");
            long start = System.currentTimeMillis();

            // Loaderı çağır ve grafiği yükle
            this.graph = PPIGraphLoader.loadGraph(infoPath, linksPath, threshold);

            long end = System.currentTimeMillis();
            System.out.println("✓ Loading Completed in " + (end - start) + " ms.");

            if (graph != null) {
                System.out.println("Total Proteins (Vertices): " + graph.getVertices().size());
                System.out.println("Total Interactions (Edges): " + graph.getEdges().size());
            } else {
                System.out.println(">> [ERROR] Graph could not be loaded (returned null).");
            }

        } catch (Exception e) {
            System.out.println(">> [ERROR] Failed to load graph.");
            e.printStackTrace();
        }
    }
    // --- 2. Search Protein ---
    private void handleSearchProtein() {
        if (!isGraphLoaded()) return;

        System.out.println("\n--- SEARCH PROTEIN ---");
        System.out.print("Enter Protein ID (e.g., 9606.ENSP00000000233): ");
        String id = scanner.nextLine().trim();

        Protein p = graph.searchProtein(id);
        if (p != null) {
            System.out.println("✓ FOUND: " + p);
            var neighbors = graph.getNeighbors(p);
            System.out.println("Connection Count (Out-Degree): " + neighbors.size());
        } else {
            System.out.println("✗ Protein NOT found with ID: " + id);
        }
    }

    // --- 3. Check Interaction ---
    private void handleCheckInteraction() {
        if (!isGraphLoaded()) return;

        System.out.println("\n--- CHECK INTERACTION ---");
        System.out.print("Source Protein ID: ");
        String id1 = scanner.nextLine().trim();
        System.out.print("Destination Protein ID: ");
        String id2 = scanner.nextLine().trim();

        Protein p1 = graph.searchProtein(id1);
        Protein p2 = graph.searchProtein(id2);

        if (p1 == null) { System.out.println("✗ Source protein not found."); return; }
        if (p2 == null) { System.out.println("✗ Destination protein not found."); return; }

        boolean interaction = graph.hasInteraction(p1, p2);
        if (interaction) {
            System.out.println("✓ RESULT: YES, interaction EXISTS.");
        } else {
            System.out.println("✗ RESULT: NO interaction found.");
        }
    }

    // --- 4. Most Confident Path ---
    private void handleMostConfidentPath() {
        if (!isGraphLoaded()) return;

        System.out.println("\n--- MOST CONFIDENT PATH ---");
        try {
            System.out.print("Enter Source Protein ID: ");
            String id1 = scanner.nextLine().trim();
            System.out.print("Enter Destination Protein ID: ");
            String id2 = scanner.nextLine().trim();

            System.out.println("Running Algorithm...");

            MostConfidentPath mcp = new MostConfidentPath();
            mcp.findMostConfidentPath(graph, id1, id2);

        } catch (Exception e) {
            System.out.println(">> [ERROR] Algorithm failed.");
            e.printStackTrace();
        }
    }

    // --- 5. Graph Metrics ---
    private void handleGraphMetrics() {
        if (!isGraphLoaded()) return;

        System.out.println("\n--- GRAPH METRICS ---");
        try {
            System.out.println("Calculating metrics (This might take a while for large graphs)...");

            PPIGraphMetrics metrics = new PPIGraphMetrics();
            metrics.calculateMetrics(graph);

        } catch (Exception e) {
            System.out.println(">> [ERROR] Metrics calculation failed.");
            e.printStackTrace();
        }
    }

    // --- 6. Traversals (BFS & DFS) ---
    private void handleTraversals() {
        if (!isGraphLoaded()) return;

        System.out.println("\n--- GRAPH TRAVERSALS ---");
        System.out.println("1. Breadth-First Search (BFS)");
        System.out.println("2. Depth-First Search (DFS)");
        System.out.print("Select Traversal Type (1 or 2): ");

        String type = scanner.nextLine();

        System.out.print("Enter Start Protein ID: ");
        String startId = scanner.nextLine().trim();

        if (graph.searchProtein(startId) == null) {
            System.out.println("✗ Protein not found in graph.");
            return;
        }

        try {
            if (type.equals("1")) {
                System.out.println("Running BFS...");
                ProteinBFS bfs = new ProteinBFS();
                bfs.traverse(graph, startId);
            } else if (type.equals("2")) {
                System.out.println("Running DFS...");
                ProteinDFS dfs = new ProteinDFS();
                dfs.traverse(graph, startId);
            } else {
                System.out.println(">> Invalid traversal type selected.");
            }
        } catch (Exception e) {
            System.out.println(">> [ERROR] Traversal failed.");
            e.printStackTrace();
        }
    }

    // Helper: Check if graph is loaded
    private boolean isGraphLoaded() {
        if (this.graph == null) {
            System.out.println(">> [WARNING] Graph is NOT loaded. Please select Option 1 first.");
            return false;
        }
        return true;
    }
}