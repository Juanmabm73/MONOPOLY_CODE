import java.io.File;


public class GameManager {
    // private int option;
    private File file;
    private Terminal terminal;

    public void start(){
        Game game = null;
        // TextTerminal tt = new TextTerminal();
        // TranslatorManager trans = new TranslatorManager();
        
       
        System.out.println("Welcome to Monopoly");
        System.out.println("1. Start Game");
        System.out.println("2. Load Game");
        
    }

    public void askForResumeGame(Terminal tt){
        tt.show("Dame el nombre de tu partida");
        tt.read();
    }
}
