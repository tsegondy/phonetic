import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import java.util.Arrays;
import java.util.List;
import phonetic.EvolutionHistorique;
import phonetic.Phoneme;
import phonetic.WordDataSet;

//chcp 65001
//java --enable-preview -cp . Main "C:\Users\tangu\OneDrive\Documents\Mon java\Phonetic\ressources\fiches modèles.txt"

public class Main {
    public static void main(String[] args) throws IOException {

        // Vérifiez si l'option --help ou -h est spécifiée dans les arguments de la
        // ligne de commande
        if (Arrays.asList(args).contains("--help") || Arrays.asList(args).contains("-h")) {
            String helpMessage = """
                    Utilisation : java Main <fichier d'entrée>
                    Options :
                      --help, -h : affiche cette aide et quitte
                      --test-encodage : affiche les caractères spéciaux
                      --evolution, -e : définit le fichier des mots à faire évoluer
                    Les caractères utilisés sont
                        voyelles : %s
                        consonnes : %s
                    Exemple :
                        chcp 65001 //pour afficher les caractères spéciaux de l'alphabet phonétique international
                        java --enable-preview -cp . Main --evolution  \"C:\\Users\\tangu\\OneDrive\\Documents\\Mon java\\Phonetic\\ressources\\fiches modèles.txt\"
                    """;
            System.out.println(String.format(helpMessage, Phoneme.V, Phoneme.C));
            System.exit(0);
        }

        // option test-encodage
        if (Arrays.asList(args).contains("--test-encodage")) {

            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"))) {
                writer.println(String.format("Consonnes IPA : %s", Phoneme.C));
                writer.println(String.format("Voyelles IPA : %s", Phoneme.V));
                writer.flush();
                System.exit(0);
            } catch (UnsupportedEncodingException e) {
                // Traitez l'exception ici (par exemple, en affichant un message d'erreur)
                e.printStackTrace();
                System.exit(2);
            }
        }

        // Vérifiez que le fichier d'entrée est spécifié en tant qu'argument de la ligne
        // de commande
        // boolean evolution = false;
        int argIndex = 0;
        String inputFile = "";
        while (argIndex < args.length) {
            if ("--evolution".equals(args[argIndex]) || "-e".equals(args[argIndex])) {
                // evolution = true;
                argIndex++;
                if (argIndex >= args.length) {
                    System.err.println("Pas de fichier d'évolution défini");
                    System.exit(1);
                } else {
                    inputFile = args[argIndex];
                    break;
                }
            } else {
                argIndex++;
            }
        }
        if (inputFile == "") {
            System.err.println("Pas de fichier d'évolution défini");
            System.exit(1);
            System.out.flush();
        }

        // Lisez le contenu du fichier d'entrée et créez une liste de demandes de mots

        List<WordDataSet.WordRequest> wordRequests = WordDataSet.readDataSet(inputFile);

        // Traitez chaque demande de mot et affichez l'évolution historique
        // correspondante
        EvolutionHistorique evolutionLatinFrancais = new EvolutionHistorique();

        for (WordDataSet.WordRequest wordRequest : wordRequests) {

            evolutionLatinFrancais.evolutionHistorique(new phonetic.Word(wordRequest.IPAformat()),
                    wordRequest.commentaire());

        }

    }
}
