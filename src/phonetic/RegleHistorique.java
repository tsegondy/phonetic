package phonetic;



public abstract class RegleHistorique {
    String description="";
    Integer epoque=0;
    public record WordEvolution(Boolean hasEvolved, Word word){}
    
    public RegleHistorique(String description) {
        super();
        this.description=description;
    }
    public RegleHistorique(String description, Integer epoque) {
        super();
        this.description=description;
        this.epoque = epoque;
    }

    public abstract WordEvolution evolue(Word initialWord);
    public abstract String regle();
}
