package phonetic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordDataSet {

    public record WordRequest(String IPAformat, String commentaire) {
    }

    public static List<WordRequest> readDataSet(String filePath) {

        List<WordRequest> values = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String IPAformat = "";
                String commentaire = "";
                String[] parts = line.split(";");
                if (parts.length == 1) {
                    IPAformat = parts[0].trim();
                } else if (parts.length == 2) {
                    IPAformat = parts[0].trim();
                    commentaire = parts[1].trim();
                }
                values.add(new WordRequest(IPAformat, commentaire));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }
}
