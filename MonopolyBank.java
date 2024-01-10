public class MonopolyBank {
    public static void main(String[] args) {
        // GameManager gameManager = new GameManager();
        Terminal tt = new TextTerminal();
        MonopolyCode[] monopolyCodeArray = new MonopolyCode[81];
        Player[] playerArray = new Player[4];
        Game game =new Game(monopolyCodeArray, tt, playerArray);
        
        game.play();
    }
}