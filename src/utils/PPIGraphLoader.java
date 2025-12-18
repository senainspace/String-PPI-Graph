package utils;

import graph.EdgeListGraph;
import graph.Graph;
import graph.Protein;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Bu sınıf, dosya sisteminden Raw Datayı okuyup
 * Graph veri yapısına dönüştürmekten sorumlu "Utility" sınıfıdır.
 * * Class İçeriği:
 * 1. Dosya Okuma (File I/O)
 * 2. Veri Temizleme (Data Parsing)
 * 3. Threshold Filtering
 * 4. Graph Construction
 */
public class PPIGraphLoader {

    /**
     * Verilen dosya yollarından graph nesnesini oluşturur.
     * @param infoFilePath Protein bilgilerinin (ID, Name) olduğu dosya yolu.
     * @param linksFilePath Etkileşimlerin (Links) olduğu dosya yolu.
     * @param threshold Kullanıcının girdiği güven eşiği (Confidence Threshold). (0.0 - 1.0 arası)
     * @return Doldurulmuş Graph nesnesi.
     */
    public static Graph loadGraph(String infoFilePath, String linksFilePath, double threshold) {
        // Concrete class (EdgeListGraph) oluşturuyoruz ama interface (Graph) referansıyla döndürüyoruz.
        Graph graph = new EdgeListGraph();

        // Proteinleri geçici olarak hafızada tutmak için bir Map kullanıyoruz.
        // Böylece linkleri okurken "Bu ID hangi proteine ait?" diye hızlıca buluyoruz.
        Map<String, Protein> tempProteinMap = new HashMap<>();

        System.out.println("Loading proteins from: " + infoFilePath);

        // Protein Tanımları (Vertices]
        try (BufferedReader br = new BufferedReader(new FileReader(infoFilePath))) {
            String line;
            boolean isHeader = true; // İlk satır başlık mı kontrolü

            while ((line = br.readLine()) != null) {
                // Header (başlık) satırını atla (Skip Header)
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Parse line
                // STRING formatında genellikle: string_protein_id <tab> preferred_name ...
                String[] parts = line.split("\t"); // Tab ile ayırıyoruz

                if (parts.length >= 2) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();

                    // Yeni protein nesnesi (Vertex) oluştur
                    Protein p = new Protein(id, name);

                    // Hem Grafa hem de geçici Map'e ekle
                    graph.addVertex(p);
                    tempProteinMap.put(id, p);
                }
            }
        } catch (IOException e) {
            // Exception Handling (Hata Yönetimi) - Ödevin 8. maddesi gereği [cite: 82]
            System.err.println("Error reading protein info file: " + e.getMessage());
        }

        System.out.println("Loading interactions from: " + linksFilePath);

        // Edges ve Threshold Filtering
        try (BufferedReader br = new BufferedReader(new FileReader(linksFilePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                // Format: protein1 <space> protein2 <space> combined_score
                // Dosya formatına göre split karakteri " " (boşluk) olabilir.
                String[] parts = line.trim().split("\\s+"); // Birden fazla boşluğu tek seperatör sayar (Regex)

                if (parts.length >= 3) {
                    String id1 = parts[0];
                    String id2 = parts[1];
                    int rawScore = Integer.parseInt(parts[2]);

                    // Normalization: Skoru 0.0 - 1.0 aralığına çekiyoruz.
                    double normalizedScore = rawScore > 1 ? rawScore / 1000.0 : rawScore;

                    // Threshold Filtering: Kullanıcının girdiği eşik değerinden düşükse bu kenarı EKLEME.
                    if (normalizedScore < threshold) {
                        continue;
                    }

                    // Kaynak ve Hedef proteinleri bul
                    Protein p1 = tempProteinMap.get(id1);
                    Protein p2 = tempProteinMap.get(id2);

                    if (p1 != null && p2 != null) {
                        // Graph'a Directed Edge ekle
                        graph.addEdge(p1, p2, normalizedScore);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading links file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Data parsing error (Score is not a number): " + e.getMessage());
        }

        return graph;
    }
}