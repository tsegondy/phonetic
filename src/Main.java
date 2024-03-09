
import java.util.List;

import phonetic.*;

import java.util.regex.Pattern;

//chcp 65001
//java --enable-preview Main

public class Main {
    public static void main(String[] args) {

        for (String argument : args) {
            System.out.println("Argument n°  : " + argument);
        }
        /*
         * System.out.println(
         * "\n##################################### fiches modèles #####################################"
         * );
         */
        List<WordDataSet.WordRequest> wordRequestList = WordDataSet.readDataSet(
            args[0]
                //"C:\\Users\\tangu\\OneDrive\\Documents\\Mon java\\Phonetic\\ressources\\fiches modèles.txt"
                );
        wordRequestList.forEach(wr -> EvolutionHistorique.evolutionHistorique(new Word(wr.IPAformat()), wr.commentaire()));

    }
}