public abstract class Terminal implements Serializable{

    private TranslatorManager translatorManager;
    // public Terminal() {
        
    // }

    public Terminal(){
        this.translatorManager = new TranslatorManager();
    }

    public abstract void show(String s, Object...args);

    public abstract int read();

    public TranslatorManager getTranslatorManager() {
        return translatorManager;
    }

    public void setTranslatorManager(TranslatorManager translatorManager) {
        this.translatorManager = translatorManager;
    }

    
}
