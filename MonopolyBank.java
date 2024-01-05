public class MonopolyBank {
    public static void main(String[] args) {
        // GameManager gameManager = new GameManager();
        Terminal tt = new TextTerminal();
        // gameManager.start();
        // gameManager.askForResumeGame(tt);
        Game game =new Game(tt);
        
        game.play();

        
    }
}