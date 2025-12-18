package graph;

import java.util.Objects;

/**
 * Bu sınıf, Graph veri yapısındaki "Vertex" (Düğüm) birimini temsil eder.
 * Her protein benzersiz bir ID'ye sahiptir.
 */
public class Protein {
    private String id;    // Protein ID (Örn: "9606.ENSP00000000233")
    private String name;  // Protein Name (info dosyasından parse edilecek)

    public Protein(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        // Eğer isim varsa ismiyle, yoksa sadece ID ile string representation döner.
        return name != null && !name.isEmpty() ? id + " (" + name + ")" : id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Reference equality kontrolü
        if (o == null || getClass() != o.getClass()) return false; // Type check
        Protein protein = (Protein) o;
        return Objects.equals(id, protein.id);
    }

    /**
     * HashMap içinde "Key" olarak kullanıldığında bucket bulmak için gereklidir.
     * ID üzerinden hash kodu üretilir.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}