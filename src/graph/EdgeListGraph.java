package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bu sınıf, Graph interface'ini "Edge List" veri yapısı kullanarak implemente eder.
 * Edge List: Tüm kenarların tek bir listede tutulduğu yapıdır.
 */
public class EdgeListGraph implements Graph {

    // Tüm kenarları (interactions) tutan ana liste.
    // Edge List Representation burada sağlanır.
    private List<Edge> edges;

    // Vertex'lere (Proteinlere) ID ile O(1) sürede erişmek için kullanılan yardımcı Map.
    private Map<String, Protein> verticesMap;

    public EdgeListGraph() {
        // Dinamik boyutlandırma için ArrayList tercih edildi.
        this.edges = new ArrayList<>();
        this.verticesMap = new HashMap<>();
    }

    @Override
    public void addVertex(Protein p) {
        // Duplicate vertex oluşumunu engellemek için kontrol ediyoruz.
        // putIfAbsent: Eğer key yoksa ekler, varsa dokunmaz.
        verticesMap.putIfAbsent(p.getId(), p);
    }

    @Override
    public void addEdge(Protein source, Protein destination, double weight) {
        // Kenar eklenmeden önce vertex'lerin grafta var olduğundan emin olunur (Validation).
        addVertex(source);
        addVertex(destination);

        // Yeni edge nesnesi oluşturulup edge list'e eklenir.
        Edge newEdge = new Edge(source, destination, weight);
        edges.add(newEdge);
    }

    @Override
    public Protein searchProtein(String id) {
        // HashMap sayesinde O(1) time complexity ile arama yapılır.
        return verticesMap.get(id);
    }

    @Override
    public boolean hasInteraction(Protein p1, Protein p2) {
        // Edge List yapısının dezavantajı: Bir kenarı bulmak için tüm listeyi gezmek gerekir.
        // Time Complexity: O(E) -> E: Edge sayısı (Worst-case).
        for (Edge edge : edges) {
            if (edge.getSource().equals(p1) && edge.getDestination().equals(p2)) {
                return true; // Etkileşim bulundu
            }
        }
        return false;
    }

    @Override
    public List<Edge> getEdges() {
        return edges;
    }

    @Override
    public List<Protein> getVertices() {
        // Map'teki değerleri (values) bir listeye çevirip döndürür.
        return new ArrayList<>(verticesMap.values());
    }

    @Override
    public List<Protein> getNeighbors(Protein p) {
        // Belirli bir düğümden çıkan (outgoing) kenarları bulmak için listeyi iterate ediyoruz.
        List<Protein> neighbors = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(p)) {
                neighbors.add(edge.getDestination());
            }
        }
        return neighbors;
    }
}