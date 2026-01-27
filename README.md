# STRING Protein-Protein Interaction Graph

This project implements a graph-based representation of the STRING Protein–Protein Interaction (PPI) Network using Java. Proteins are modeled as vertices, while interactions between proteins are represented as directed edges with associated confidence scores.

The main goal of this project is to analyze large-scale biological interaction networks by applying graph data structures, traversal algorithms, path analysis, and network-level metrics.

---

## Overview

Protein–Protein Interaction (PPI) networks provide critical insights into biological processes, disease mechanisms, and functional relationships between proteins. The STRING database integrates known and predicted protein interactions from multiple biological sources and assigns a confidence score to each interaction.

In this project:
- Proteins are treated as graph vertices
- Interactions are treated as directed edges
- Low-confidence interactions are filtered using a user-defined threshold
- Graph algorithms are applied to explore and analyze the network structure

---

## Dataset

The dataset is obtained from the **STRING database** for *Homo sapiens* and consists of two main files:

- `9606.protein.info.v12.0.txt`  
  Contains protein identifiers and related metadata.

- `9606.protein.links.v12.0.txt`  
  Contains protein–protein interaction pairs with confidence scores ranging from 0 to 1.

Only interactions with confidence scores greater than or equal to a specified threshold are included in the graph.

---

## Graph Representation

The network is implemented using an **Edge List–based Graph Abstract Data Type (ADT)**.

- **Vertices:** Proteins  
- **Edges:** Directed protein–protein interactions  
- **Weights:** Interaction confidence scores  

The implementation follows object-oriented design principles and separates graph structure, algorithms, metrics, and utilities into distinct modules.

---

## Features

The application provides the following functionalities through a menu-driven interface:

- Load the protein interaction graph using a confidence score threshold
- Search for a protein by its identifier
- Check whether two proteins have a direct interaction
- Find the most confident path between two proteins
- Compute basic graph metrics:
  - Number of vertices
  - Number of edges
  - Average degree
  - Graph diameter
  - Reciprocity
- Perform Breadth-First Search (BFS)
- Perform Depth-First Search (DFS)

---
## Technologies Used

- Java
- Object-Oriented Programming (OOP)
- Graph Data Structures
- Breadth-First Search (BFS)
- Depth-First Search (DFS)

---

## How to Run

1. Place the dataset files inside the `data/` directory.
2. Compile the Java source files.
3. Run the `Main` class.
4. Follow the menu instructions to load and analyze the graph.

### 📂 Project Structure

```text
string-ppi-graph
│
├── src/
│   ├── app/
│   │   └── Main.java
│   ├── graph/
│   │   ├── Graph.java
│   │   ├── Edge.java
│   │   └── Vertex.java
│   ├── algorithms/
│   │   ├── BFS.java
│   │   ├── DFS.java
│   │   └── MostConfidentPath.java
│   ├── metrics/
│   │   └── GraphMetrics.java
│   └── utils/
│       ├── DataLoader.java
│       └── Menu.java
│
└── data/
    ├── 9606.protein.info.v12.0.txt
    └── 9606.protein.links.v12.0.txt



