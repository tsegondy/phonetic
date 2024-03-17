package test.java;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import phonetic.EvolutionHistorique;

//import org.junit.Test;
//import static org.junit.Assert.*;

public class EvolutionHistoriqueTest {

    EvolutionHistorique evolutionLatinFrancais = new EvolutionHistorique();

    public void testEvolution(String input, String output) {
        EvolutionHistorique.ResultatEvolutionHistorique result = evolutionLatinFrancais.EvolutionHistoriqueBySteps(
                new phonetic.Word(input),
                "test" + input);

        assertEquals(output, result.evolutionSteps().getLast().finalWord().IPAformat);
    }

    @Test
    public void testBellos() {
        testEvolution("be'l.los", "bo'");
    }

    @Test
    public void testBenem() {
        testEvolution("be'.nem", "bjɛ̃'");
    }

    @Test
    public void testBowem() {
        testEvolution("bo'.wem", "bø'");
    }

    @Test
    public void testKameram() {
        testEvolution("ka'.me.ram", "ʃɑ̃'bR");
    }

    @Test
    public void testKantatul() {
        testEvolution("kan.ta-'.tum", "ʃɑ̃.te-'");
    }

    @Test
    public void testKapum() {
        testEvolution("ka'.pum", "ʃe'f");
    }

    @Test
    public void testKomitem() {
        testEvolution("ko'.mi.tem", "kɔ̃'t");
    }

    @Test
    public void testKomputat() {
        testEvolution("ko'm.pu.tat", "kɔ̃'t");
    }

    @Test
    public void testFaktum() {
        testEvolution("fa'k.tum", "fɛ'");
    }

    @Test
    public void testFide() {
        testEvolution("fi'.de", "fwa'");
    }

    @Test
    public void testFilius() {
        testEvolution("fi-'.li.us", "fi'");
    }

    @Test
    public void testFoliam() {
        testEvolution("fo'.li.am", "fœ'j");
    }

    @Test
    public void testMerkedem() {
        testEvolution("mer.ke-'.dem", "mɛR.si'");
    }

    @Test
    public void testNauseam() {
        testEvolution("na'u.se.am", "nwa'z");
    }

    @Test
    public void testTektum() {
        testEvolution("te-'k.tum", "twa'");
    }

    @Test
    public void testTenerum() {
        testEvolution("te'.ne.rum", "tɛ̃'dR");
    }

    @Test
    public void testWitam() {
        testEvolution("wi-'.tam", "vi-'");
    }

}
