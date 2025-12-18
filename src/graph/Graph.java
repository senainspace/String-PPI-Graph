package graph;

import java.util.List;

/**
 * Graph ADT (Abstract Data Type) arayüzü.
 * Bu interface, grafın "nasıl" (implementation) değil "ne" (abstraction) yapacağını belirtir.
 */
public interface Graph {
    // Graf'a yeni bir Vertex (Protein) ekler.
    void addVertex(Protein p);

    // İki protein arasında Directed Weighted Edge (Yönlü Ağırlıklı Kenar) oluşturur.
    void addEdge(Protein source, Protein destination, double weight);

    // ID'ye göre graf içindeki proteini arar (Search Operation).
    Protein searchProtein(String id);

    // İki protein arasında kenar olup olmadığını kontrol eder.
    boolean hasInteraction(Protein p1, Protein p2);

    // Algoritmalar ve Metrikler için gerekli getter metodları.
    List<Edge> getEdges();
    List<Protein> getVertices();

    // Bir Vertex'in komşularını (adjacent vertices) döndürür.
    // BFS ve DFS traversalları için kritiktir.
    List<Protein> getNeighbors(Protein p);
}