package phonetic;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import phonetic.RegleHistorique.WordEvolution;
import phonetic.Phoneme;

public class EvolutionHistorique {

        public List<RegleHistorique> listeReglesEvolution;

        public EvolutionHistorique() {
                super();
                listeReglesEvolution = listeReglesLatinFrancais;
                // listeReglesEvolution = new ArrayList<RegleHistorique>();
        }

        public record ResultatEvolutionHistorique(String commentaire, Word initialWord,
                        List<WordEvolution> evolutionSteps) {
        }

        /* simplification de la méthode sans commentaire */
        public void EvolutionHistoriqueOutput(Word word) {
                EvolutionHistoriqueOutput(word, "");
        }

        public void EvolutionHistoriqueOutput(Word word, String commentaire) {
                PrintResultatEvolutionHistorique(this.EvolutionHistoriqueBySteps(word, commentaire));
        }

        public ResultatEvolutionHistorique EvolutionHistoriqueBySteps(Word word, String commentaire) {
                List<WordEvolution> stepsList = new ArrayList<WordEvolution>();
                Word lastWord = word;
                for (RegleHistorique rule : listeReglesEvolution) {
                        WordEvolution evolutionMot = rule.evolue(lastWord);
                        stepsList.add(evolutionMot);
                        lastWord = evolutionMot.finalWord();
                }
                return new ResultatEvolutionHistorique(commentaire, word, stepsList);
        }

        public static void PrintResultatEvolutionHistorique(ResultatEvolutionHistorique resultatEvolutionHistorique) {
                try {
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out, "UTF-8"));

                        writer.println("");
                        writer.println("\t********");
                        writer.println("\t** Evolution du mot : "
                                        + resultatEvolutionHistorique.initialWord().IPAformat
                                        + "\t" + resultatEvolutionHistorique.commentaire());
                        writer.println("\t********\n");

                        for (WordEvolution wordEvolution : resultatEvolutionHistorique.evolutionSteps) {
                                if (wordEvolution.hasEvolved()) {
                                        String sortie = "";
                                        sortie = String.format("%-" + 7 + "s", "(" + wordEvolution.rule().epoque + ")");
                                        sortie = String.format("%-" + 80 + "s",
                                                        sortie + wordEvolution.rule().description);
                                        sortie = sortie + wordEvolution.initialWord().IPAformat + " --> "
                                                        + wordEvolution.finalWord().IPAformat;
                                        writer.println(sortie);
                                } else {
                                        // System.out.println(rule.description + ":\t\t\t\t\t" + "pas d'évolution");
                                }
                        }
                        writer.flush();
                        return;
                } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        System.exit(2);
                }
        }

        private static List<RegleHistorique> listeReglesLatinFrancais = List.of(
                        // voyelle non accentuée en hiatus
                        new RegleRegex("([" + Phoneme.C + "])(i)\\.([" + Phoneme.V + "])", "$1j$3",
                                        "voyelle non accentuée en hiatus", -50),
                        new RegleRegex("([" + Phoneme.C + "])(e)\\.([" + Phoneme.V + "])", "$1j$3",
                                        "voyelle non accentuée en hiatus", -50),
                        new RegleRegex("([" + Phoneme.C + "])(u)\\.([" + Phoneme.V + "])", "$1w$3",
                                        "voyelle non accentuée en hiatus", -50),
                        // amuissement du m final
                        new RegleRegex("(?<=\\..*)m$", "", "amuissement du m final", 0),
                        // w perd son articulation vélaire
                        new RegleRegex("w", "β", "w perd son articulation vélaire", 50),
                        // spirantisation de b en β, p. 63
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(b)([" + Phoneme.V + "])", "$1β$3",
                                        "spirantisation de b intervocalique dès Ier s.",
                                        050),
                        /*
                         * IIe siecle
                         */
                        // palatisation s+j p.85
                        new RegleRegex("\\.sj", "j\\.s", "palatisation s+j", 150),
                        // palatisation l+j p.89
                        new RegleRegex("lj", "ʎ", "palatisation l+j", 150),
                        // changement vocalique e bref devient ouvert
                        new RegleRegex("(e)([^-])", "ɛ$2", "e bref devient ouvert", 150),
                        // changement vocalique e long devient court
                        new RegleRegex("e-", "e", "e long devient court", 150),

                        /*
                         * IIIe siecle
                         */
                        // disparition des voyelles penultieme atone
                        /*
                         * On rajoute un accent final sur les pro paroxyton pour identifier que les
                         * voyelles finales dans ce cas ne doit pas disparaitre vers 600, mais
                         * être neutralisée uniquement
                         */
                        new RegleRegex(
                                        "(.*)\\.([" + Phoneme.C + "]*)([" + Phoneme.V + "])(?!')([" + Phoneme.C
                                                        + "]*)\\.([^.]+)",
                                        "$1$2.$4$5'",
                                        "disparition des voyelles penultieme atone", 250),
                        // la consonne centrale d'un groupe de trois disparait
                        new RegleRegex(
                                        "([" + Phoneme.C + "])(\\.?)([" + Phoneme.C + "])(\\.?)([" + Phoneme.C + "])",
                                        "$1$2$4$5", "la consonne centrale d'un groupe de trois disparait", 250),

                        // épenthèse par dénasalisation
                        new RegleRegex("m\\.r", "m\\.br", "épenthèse par dénasalisation", 250),
                        new RegleRegex("m\\.l", "m\\.bl", "épenthèse par dénasalisation", 250),
                        new RegleRegex("n\\.r", "n\\.dr", "épenthèse par dénasalisation", 250),
                        // changement des i bref non accentué p.19
                        new RegleRegex("i(?!-)", "e", "changement des i bref non accentué", 250),
                        // palatisation de k+e
                        new RegleRegex("ke", "tse", "palatisation de k+e", 250),
                        // β se renforce en v, p. 67
                        new RegleRegex("β", "v", "β se renforce en v", 250),
                        // spirantisation de k implosif devant t ou s et changement articulation, p. 86
                        new RegleRegex("k\\.(?=[ts])", "j\\.", "k devant t ou s", 250),

                        // diphtongaison romane spontanée de e accentué libre p. 27
                        new RegleRegex("ɛ'([\\.$])", "i'ɛ$1",
                                        "diphtongaison romane spontanée de e accentué libre", 250),
                        // dentalisation m devant t,s
                        new RegleRegex("m(\\.?[st])", "n$1", "dentalisation m devant t,s", 275),
                        /*
                         * IVe siecle
                         */
                        // changement des u bref à l'intérieur d'un mot p.19
                        new RegleRegex("(u)(?!\\b|-)\\b", "o$2", "changement des u bref à l'intérieur d'un mot",
                                        350),
                        // diphtongaison romane o ouvert accentué en syllabe ouverte p. 28
                        new RegleRegex("o'\\.", "uo'\\.",
                                        "diphtongaison romane o ouvert accentué en syllabe ouverte", 350),
                        // spirantisation en ɣ p. 65
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(g)([" + Phoneme.V + "])", "$1ɣ$3",
                                        "spirantisation en ɣ", 350),
                        // sonorisation des sourdes intervocaliques
                        new RegleRegex("([" + Phoneme.V + "]-?'?j?\\.)(s)([" + Phoneme.V + "])", "$1z$3",
                                        "sonorisation de s intervocalique",
                                        350),
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(t)([" + Phoneme.V + "])", "$1d$3",
                                        "sonorisation de t intervocalique",
                                        375),
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(p)([" + Phoneme.V + "])", "$1b$3",
                                        "sonorisation de p intervocalique",
                                        375),
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(k)([" + Phoneme.V + "])", "$1g$3",
                                        "sonorisation de g intervocalique",
                                        375),
                        // avancée point articulation ɣ, p. 85
                        new RegleRegex("ɣ", "j", "avancée point articulation ɣ", 350),
                        /*
                         * Ve siecle
                         */
                        // spirantisation en ɣ p. 65
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(g)([" + Phoneme.V + "])", "$1ɣ$3",
                                        "spirantisation en ɣ", 450),
                        // amuissement ɣ p. 65
                        new RegleRegex("ɣ", "",
                                        "amuissement ɣ", 450),
                        // changement des u brefs finaux p.19
                        new RegleRegex("(u)([" + Phoneme.C + "]*)$", "o$2", "changement des u brefs finaux",
                                        450),
                        // palatisation de k+a
                        new RegleRegex("ka", "tʃa", "palatisation de k+a", 450),
                        // spirantisation des occlusives intervocaliques des sourdes intervoliques, p.
                        // 62
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(b)([" + Phoneme.V + "])", "$1β$3",
                                        "spirantisation de b intervocalique",
                                        450),
                        // puis renforcement en v, p. 62
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(β)([" + Phoneme.V + "])", "$1v$3",
                                        "renforcement de β intervocalique en v",
                                        450),
                        // au latin se réduit à o fermé
                        new RegleRegex("a'u", "o'", "au latin se réduit à o fermé", 450),
                        // géminisation de y intervocalique p. 85
                        new RegleRegex("([" + Phoneme.V + "]-?'?)([\\.])(j)([" + Phoneme.V + "])", "$1j$2j$4",
                                        "géminisation de j intervocalique", 450),

                        /*
                         * VIe siecle
                         */
                        // effet de Bartsh a après consomme palatale libre
                        new RegleRegex("([" + Phoneme.Cpalatale + "])a'\\.", "$1i'e\\.",
                                        "effet de Bartsh a après consomme palatale", 500),

                        // spirantisation des occlusives intervocaliques des sourdes intervoliques
                        new RegleRegex("([" + Phoneme.V + "]-?'?\\.)(d)([" + Phoneme.V + "])", "$1ð$3",
                                        "spirantisation de d intervocalique",
                                        550),
                        // diphtongation spontané de a accentué libre, p. 30
                        new RegleRegex("(a)(-?)'\\.", "ae$2'\\.", "diphtongation spontané de a accentué libre",
                                        550),
                        // diphtongation spontané de o long accentué libre
                        new RegleRegex("o-'\\.", "ou'\\.", "diphtongation spontané de o long accentué libre",
                                        550),
                        // diphtongation spontané de e accentué libre, p. 28
                        new RegleRegex("e'\\.", "e'i\\.", "diphtongation spontané de e accentué libre",
                                        550),

                        /*
                         * VIIe siecle
                         */
                        // diphtongue de coalescence, p. 101
                        new RegleRegex("o'j\\.", "o'i\\.", "diphtongue de coalescence", 650),
                        // simplification des géminées j p. 85
                        new RegleRegex("j\\.j", "\\.j", "simplification des géminées j", 650),
                        // simplification des géminées p. 65
                        new RegleRegex("([" + Phoneme.C + "])\\.\\1", "\\.$1", "simplification des géminées",
                                        650),
                        // monophtongaison, p. 30
                        new RegleRegex("ae-'", "ɛ-'", "monophtongaison", 650),
                        // disparition des voyelles finales hors a hors ancien proparoxyton (plus bas)
                        new RegleRegex(
                                        "(\\.)([" + Phoneme.C + "]*)([" + Phoneme.Vhorsa + "]-?)([" + Phoneme.C
                                                        + "]*)$",
                                        "$2$4", "disparition des voyelles finales hors a", 650),
                        // neutralisatio des voyelles finales hors a des anciens proparoxytons (plus
                        // bas)
                        new RegleRegex(
                                        "(\\.)([" + Phoneme.C + "]*)([" + Phoneme.Vhorsa + "]-?)([" + Phoneme.C
                                                        + "]*)(['])$",
                                        "$1$2ə$4", "disparition des voyelles finales hors a", 650),

                        // assimilation d'aperture p. 27
                        new RegleRegex("i'ɛ", "i'e", "assimilation d'aperture",
                                        650),
                        // ʎs final devient ʎts, p. 77
                        new RegleRegex("ʎs$", "ʎts", "ʎs final devient ʎts",
                                        650),
                        // l devant consonne devient vélaire, p. 16 et 91
                        new RegleRegex("[lʎ]([" + Phoneme.C + "])", "ɫ$1", "l devant consonne devient vélaire",
                                        650),
                        /*
                         * VIIIe siecle
                         */
                        // disparition des voyelles finales hors a p. 40
                        /*
                         * new RegleSansContexte(
                         * "(\\.)([" + Phoneme.C + "]*)([" + Phoneme.Vhorsa + "]-?)([" + Phoneme.C
                         * + "]*)$",
                         * "$2$4",
                         * "disparition des voyelles finales hors a",
                         * 700),
                         */
                        // neutralisation du a final"
                        new RegleRegex("(\\.)([" + Phoneme.C + "]*)(a)(-?)([" + Phoneme.C + "]*)('?)$",
                                        "$1$2ə$5",
                                        "neutralisation du a final", 700),
                        // fin de la palatalisation de u
                        // new RegleSansContexte("u", "y", "fin de la palatalisation de u", 750),
                        // assourdissement des consonnes finales
                        new RegleRegex("b$", "p", "assourdissement de b final", 750),
                        new RegleRegex("v$", "f", "assourdissement de v final", 750),

                        new RegleRegex("ð$", "θ", "assourdissement de ð final", 750),
                        new RegleRegex("d$", "t", "assourdissement de d final", 750),
                        /*
                         * IXe siecle
                         */
                        // vocalisation de j après a, page 34
                        new RegleRegex("(a)(-?'?)(j)", "$1$2i", "vocalisation de j après a", 800),
                        // vocalisation de j
                        new RegleRegex("(e)(-?'?)(j)", "$1i$2", "vocalisation de j", 850),

                        /*
                         * Xe siecle
                         */
                        // nasalisation diphtongale de ie p.50
                        // new RegleSansContexte("ie", "$1i$2", "nasalisation diphtongale de ie", 1050),
                        /*
                         * XIe siecle
                         */

                        // nasalisation du a"
                        new RegleRegex("(a)('?)([nm])", "ɑ̃$2$3", "nasalisation du a", 1025),
                        // nasalisation du e"
                        new RegleRegex("(e)('?)([nm])", "ɛ̃$2$3", "nasalisation du e", 1050),
                        // evolution diphtongation ou p. 28
                        new RegleRegex("ou", "eu", "evolution diphtongation ou-1", 1050),
                        // evolution e non accentué libre initial
                        new RegleRegex("([" + Phoneme.C + "]*)(e)(-?\\.)", "$1ə$3",
                                        "evolution e non accentué libre initial", 1050),
                        // ð intervocalique disparait
                        new RegleRegex("([" + Phoneme.V + "]-?'?)(\\.ð)([" + Phoneme.V + "])", "$1\\.$3",
                                        "ð intervocalique disparait",
                                        1050),
                        // fermeture, p. 30
                        new RegleRegex("ɛ-'", "e-'", "fermeture ɛ", 1050),
                        // evolution diphtongation uo
                        new RegleRegex("uo'", "ue'", "evolution diphtongation ou-1", 1050),
                        new RegleRegex("ue'", "yø'", "evolution diphtongation ou-2", 1050),
                        // a initial non accentué en hiatus
                        new RegleRegex("(^[" + Phoneme.C + "]*)(a)(-?)(\\.[" + Phoneme.V + "])", "$1ə$3$4",
                                        "a initial non accentué en hiatus", 1025),
                        // l vélaire aprés voyelle se vocalise, p. 70 p. 54
                        new RegleRegex("([" + Phoneme.V + "]-?'?)ɫ", "$1u", "l vélaire après voyelle se vocalise",
                                        1050),
                        // u après i ou y disparait p. 56
                        new RegleRegex("([iy]-?'?)u", "$1", "u après i ou y disparait",
                                        1050),
                        // u diphtongal après ɛ ouvert, p. 70 p. 54
                        new RegleRegex("ɛ-?'?u", "ea'u",
                                        "u diphtongal après e ouvert", 1050),
                        // e s'affaiblit en ə, p. 70
                        new RegleRegex("ea'u", "əa'u", "e s'affaiblit en ə", 1050),
                        // disparition des t, θ finaux après voyelle, p. 59
                        new RegleRegex("([" + Phoneme.V + "]-?'?)[tθ]$", "$1",
                                        "disparition des t, θ finaux après voyelle", 1050),

                        /*
                         * XIIe siecle
                         */
                        // evolution diphtongation ai p. 34
                        new RegleRegex("(a)(-?'?)i", "ɛ$2", "evolution diphtongation ai", 1100),
                        // evolution e non accentué entravé initial
                        new RegleRegex("([" + Phoneme.C + "]*)(e)(-?[" + Phoneme.C + "]+\\.)", "$1ɛ$3",
                                        "evolution non accentué entravé initial", 1150),
                        // evolution diphtongation eu
                        new RegleRegex("eu", "øu", "evolution diphtongation eu", 1150),
                        // evolution diphtongation ei p. 28, 34
                        new RegleRegex("e(-?'?')i", "u$1e", "evolution diphtongation ei", 1150),
                        // assimilation réciproque d'aperture
                        new RegleRegex("(o'i)(-?)", "u'e$2", "assimilation réciproque d'aperture", 1175),
                        // nasalisation du o, page 47
                        new RegleRegex("(o)('?)([nm])", "ɔ̃$2$3", "nasalisation du o", 1250),
                        /*
                         * XIIIe siecle
                         */
                        // réduction de la diphtongue, p. 36
                        new RegleRegex("ie", "je", "réduction de la diphtongue", 1200),
                        // evolution diphtongation øu
                        new RegleRegex("øu", "ø", "evolution diphtongation øu", 1225),
                        // evolution diphtongation o ouvert p.28
                        new RegleRegex("yø'", "ɥø'", "evolution diphtongation o ouvert-1", 1225),
                        new RegleRegex("ɥø'", "ø'", "evolution diphtongation o ouvert-2", 1225),
                        // reduction, bascule d'accent p. 29
                        new RegleRegex("(u'e)(-?)", "we$2'", "reduction, bascule d'accent", 1225),
                        // bascule d'accent p. 50
                        new RegleRegex("i'ɛ̃", "jɛ̃'", "bascule d'accent", 1250),
                        // bascule d'accent p. 94
                        new RegleRegex("i'e", "je'", "bascule d'accent", 1250),

                        // nasalisation du i"
                        new RegleRegex("(i)('?)([nm])", "ɛ̃$2$3", "nasalisation du i", 1350),
                        new RegleRegex("en", "ɛ̃", "nasalisation du i", 1350),
                        // simplification des affiquées
                        new RegleRegex("t([ʃs])", "$1", "simplification des affiquées", 1250),
                        // new RegleSansContexte("a-", "ie"),
                        // new RegleSansContexte("ie", "je"),
                        // new RegleSansContexte("\\.ne$", "n"),
                        /*
                         * XIVe siecle
                         */
                        // disparition des consonnes finales hors mono syllabe
                        /*
                         * new RegleSansContexte("(.*\\..*)[" + Phoneme.C + "]$", "$1",
                         * "disparition des consonnes finales hors mono syllabe", 1300),
                         */
                        // disparition des consonnes finales même mono syllabe
                        new RegleRegex("[" + Phoneme.C + "]$", "",
                                        "disparition des consonnes finales même mono syllabe", 1300),

                        // réduction des hiatus p.39, p.100
                        new RegleRegex("(ə\\.)([" + Phoneme.V + "])", "$2", "réduction des hiatus", 1350),

                        // labialisation du schwa final
                        /*
                         * new RegleSansContexte("ə$", "œ",
                         * "labialisation du schwa final",
                         * 1450),
                         */
                        /*
                         * XVIIe siecle
                         */
                        // dénasalisation implosive
                        new RegleRegex("([" + Phoneme.Vnasale + "])('?)([nm])", "$1$2",
                                        "dénasalisation implosive", 1600),

                        // amuissement "e" final p. 98
                        new RegleRegex("([^\\.]*)(\\.)?([^\\.]*)(ə)$", "$1$3", "amuissement \"e\" final", 1650),
                        // fermeture voyelle en syllabe entravée p. 28
                        new RegleRegex("(ø)('?)([" + Phoneme.C + "])", "œ$2$3",
                                        "fermeture voyelle en syllabe entravée",
                                        1650),
                        /*
                         * XVIe siecle
                         */
                        // monophtongation de au
                        new RegleRegex("(a)('?)(u)", "o$2", "monophtongation de au", 1550),
                        /*
                         * XVIe siecle
                         */
                        // dénasalisation implosive, disparition de la consonne, p. 45
                        new RegleRegex("([" + Phoneme.Vnasale + "])([nm])(\\.)", "$1$3",
                                        "dénasalisation implosive, disparition de la consonne", 1600),
                        // amuissement de ə, p. 55
                        new RegleRegex("əo", "o", "amuissement de ə", 1620),
                        /*
                         * XVIIIe siecle
                         */
                        // changement articulation du r
                        new RegleRegex("r", "R", "changement articulation du r", 1750),
                        // relachement l mouillé p.84
                        new RegleRegex("ʎ", "j", "relachement l mouillé", 1750),
                        // wa l'emporte p. 29
                        new RegleRegex("we", "wa", "wa l'emporte", 1750));

}
