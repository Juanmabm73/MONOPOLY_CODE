public class TranslatorManager implements Serializable {
    private Translator currentIdiom;
    public Translator [] translators =  new Translator[3];

 
    public TranslatorManager (){
        this.translators[0] = new Translator("spanish");
        this.translators[1] = new Translator("euskera");
        this.translators[2] = new Translator("english");
        this.currentIdiom = this.translators[2];
    }

    public void changeIdiom(int idiomNumber){
        if (idiomNumber>= 0 && idiomNumber< translators.length){
            currentIdiom = translators[idiomNumber];
        }
    }

    public Translator getCurrentIdiom() {
        return this.currentIdiom;
    }

    public void setCurrentIdiom(Translator currentIdiom) {
        this.currentIdiom = currentIdiom;
    }

    public Translator[] getTranslators() {
        return translators;
    }

    public void setTranslators(Translator[] translators) {
        this.translators = translators;
    }

    
    
}

