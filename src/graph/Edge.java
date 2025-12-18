package graph;

/**
 * Bu sınıf, Graph yapısındaki iki Vertex arasındaki "Interaction"ı  temsil eder.
 * PPI Network olduğu için bu yapı "Directed Edge" (Yönlü Kenar) olarak modellenmiştir.
 */
public class Edge {
    private Protein source;      // Kenarın başladığı düğüm (Source Vertex)
    private Protein destination; // Kenarın bittiği düğüm (Target/Destination Vertex)
    private double weight;       // Etkileşimin güven skoru (Confidence Score / Weight)

    public Edge(Protein source, Protein destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    // Getter methods for accessing private fields
    public Protein getSource() { return source; }
    public Protein getDestination() { return destination; }
    public double getWeight() { return weight; }

    @Override
    public String toString() {
        return source.getId() + " -> " + destination.getId() + " [Score: " + weight + "]";
    }
}