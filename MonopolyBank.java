public class MonopolyBank {
    public static void main(String[] args) {
        // GameManager gameManager = new GameManager();
        TranslatorManager translatorManager = new TranslatorManager();
        Terminal tt = new TextTerminal(translatorManager);
        MonopolyCode[] monopolyCodeArray = new MonopolyCode[81];
        Player[] playerArray = new Player[4];
        

        Game game =new Game(monopolyCodeArray, tt, translatorManager, playerArray);
        
        game.play();
    }
}