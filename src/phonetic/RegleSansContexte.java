package phonetic;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegleSansContexte extends RegleHistorique {
    String regex;
    String remplacement;

    public RegleSansContexte(String aRegex, String aRemplacement, String descriptionHistorique, Integer epoque) {
        super(descriptionHistorique, epoque);
        this.regex = aRegex;
        this.remplacement = aRemplacement;
    }

    public RegleSansContexte(String aRegex, String aRemplacement, String descriptionHistorique) {
        super(descriptionHistorique);
        this.regex = aRegex;
        this.remplacement = aRemplacement;
    }

    public RegleSansContexte(String aRegex, String aRemplacement) {
        super(aRegex + "\t→\t" + aRemplacement);
        this.regex = aRegex;
        this.remplacement = aRemplacement;
    }

    @Override
    public String regle() {
        return this.regex + "\t→\t" + this.remplacement;
    }

    @Override
    public WordEvolution evolue(Word initialWord) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(initialWord.IPAformat);

        Boolean isMatching = m.find();
        String result = initialWord.IPAformat.replaceAll(this.regex, this.remplacement);

        return new WordEvolution(isMatching, new Word(result));
    }

}
