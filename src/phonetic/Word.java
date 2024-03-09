package phonetic;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class Word {
    public String IPAformat;

    public Word(String mot) {
        super();
        // syllabes = new ArrayList<Syllable>();
        IPAformat = mot;
    }

    @Override
    public String toString() {
        String outputString = "Word [IPAformat=" + IPAformat + "]";

        return outputString;
    }
}
