import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Game implements Serializable {

    private static final String WELCOME_MESSAGE = "Welcome to Monopoly";
    private static final String CHOOSE_IDIOM_MESSAGE = "Choose your idiom";
    private static final String SPANISH_OPTION_MESSAGE = "1. Spanish";
    private static final String EUSKERA_OPTION_MESSAGE = "2. Euskera";
    private static final String ENGLISH_OPTION_MESSAGE = "3. English";
    private static final String ENTER_CARD_CODE_MESSAGE = "Enter card code";
    private static final String ENTER_PLAYER_ID_MESSAGE = "Enter player id";
    private static final String PLAYER_ELIMINATED_MESSAGE = "This player is eliminated";
    private static final String ERROR_MESSAGE_PREFIX = "ERROR " + "%s"; // Usado como prefijo
    private static final String PLAYERS_SUMMARY_MESSAGE = "# PLAYERS SUMMARY #"; // Usado como prefijo
    private static final String PLAYER_BANKRUPT_MESSAGE = "Player %d is bankrupt and has been removed from the game"; // Usado como sufijo
    private static final String PLAYER_INFO_FORMAT = "%s balance: %d. Properties: %s"; // Usado para formatear con String.format
    private static final String NAME_OF_PLAYER_MESSAGE = "Name of %s player:"; // Usado como prefijo
    


    private  MonopolyCode [] monopolyCodeArray = new MonopolyCode[81];
    private Terminal terminal; 
    private TranslatorManager translatorManager;
    private  Player[] playerArray = new Player [4];

   
  
    public Game(MonopolyCode[] monopolyCodeArray, Terminal terminal, TranslatorManager translatorManager, Player[] playerArray) {
        this.monopolyCodeArray = monopolyCodeArray;
        this.terminal = terminal;
        this.translatorManager = translatorManager;
        this.playerArray = playerArray;
    }
    public Game(){

    }

    private void loadMonopolyCodes()  {
        BufferedReader reader = null;
        try { 
            reader = new BufferedReader(new FileReader("./files/properties.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String [] segment = line.split(";");
                switch (segment[1]) { //property name 
                    case "STREET" :
                        monopolyCodeArray[Integer.parseInt(segment[0])] = (new Street(segment, terminal));
                        break;
                    case "SERVICE":
                        monopolyCodeArray[Integer.parseInt(segment[0])] = (new Service(segment, terminal));
                        break;
                    case "TRANSPORT":
                        monopolyCodeArray[Integer.parseInt(segment[0])] = (new Transport(segment, terminal)); 
                        break;
                    case "PAYMENT_CHARGE_CARD": 
                        monopolyCodeArray[Integer.parseInt(segment[0])] = (new PaymentCharge(segment, terminal));
                        break;
                    case "REPAIRS_CARD":
                        monopolyCodeArray[Integer.parseInt(segment[0])] = (new RepairsCard(segment, terminal));
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try{
                    reader.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        

    }

    private void createPlayers() {
        Scanner scanner = new Scanner(System.in);

            for (int i = 0; i < 4; i++) {
                Color color = Color.values()[i];
                terminal.show(Game.NAME_OF_PLAYER_MESSAGE, color.toString());
                String playerName = scanner.nextLine();
                playerArray[i] = (new Player(i+1, color, playerName, 1500, false));
            }
            
        }

    public void play() {
        int idiomNumber;
        
        do {
            terminal.show("Welcome to Monopoly");
            terminal.show("Choose your idiom");
            terminal.show(Game.SPANISH_OPTION_MESSAGE);
            terminal.show(Game.EUSKERA_OPTION_MESSAGE);
            terminal.show(Game.ENGLISH_OPTION_MESSAGE);
            idiomNumber= terminal.read();

        } while(idiomNumber<=1 && idiomNumber>3);
        
        switch (idiomNumber) {
            case 1:
                translatorManager.changeIdiom(0);
                break;
            case 2:
                translatorManager.changeIdiom(1);
                break; 
            case 3: 
            translatorManager.changeIdiom(2);
                break;
            
            default:
                break;
        }
        
        
         
        createPlayers();
        loadMonopolyCodes();
        Game game = new Game(monopolyCodeArray, terminal,translatorManager, playerArray);
        while (playerArray.length >= 2) { //cambiar para que cuente los null
            
            
            terminal.show(Game.ENTER_CARD_CODE_MESSAGE);
            int id = terminal.read();
            terminal.show(Game.ENTER_PLAYER_ID_MESSAGE);
            int playerId = terminal.read();

            for (int i = 0; i < getMonopolyCodeArray().length; i++) {
                if (getMonopolyCodeArray()[i] != null && getMonopolyCodeArray()[i].getId() == id)  {
                    terminal.show(getMonopolyCodeArray()[i].toString());
                    if(getPlayerArray()[playerId-1] != null){
                        getMonopolyCodeArray()[i].doOperation(getPlayerArray()[playerId - 1],terminal); //posicion del array no indice
                        break;
                    } else{
                        terminal.show(Game.PLAYER_ELIMINATED_MESSAGE);
                    }
                    
                }
            }

            removePlayer();

            try{
                XMLEncoder encoder = new XMLEncoder(
                    new BufferedOutputStream(
                    new FileOutputStream("./files/partida.xml"))
                );
                encoder.writeObject(game);
                encoder.close();
            }
            catch (Exception e){
                terminal.show(Game.ERROR_MESSAGE_PREFIX, e);
            }

            terminal.show(Game.PLAYERS_SUMMARY_MESSAGE);
            for (Player player : getPlayerArray()) {
                if (player != null){
                    terminal.show( Game.PLAYER_INFO_FORMAT,player.toString(), player.getBalance(), player.getProperties());
                    player.resume(terminal, player);
                }
            }

        } //fin del while
    } //fin del metodo

   //CAMBIAR METODO Y CONTROLAR JUGADORES NULOS
    private void removePlayer(){
        for (int i = 0; i < getPlayerArray().length; i++){
            if((getPlayerArray()[i] != null) && (getPlayerArray()[i].isBankrupt())){
                getPlayerArray()[i] = null;
                terminal.show(Game.PLAYER_BANKRUPT_MESSAGE, (i+1));
                break;
            }
        }
    }
        
   

        

    
    
    public MonopolyCode[] getMonopolyCodeArray() {
        return monopolyCodeArray;
    }

    public void setMonopolyCodeArray(MonopolyCode[] monopolyCodeArray) {
        this.monopolyCodeArray = monopolyCodeArray;
    }

    public Terminal getTerminal() {
        return terminal;
    }

    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    public Player[] getPlayerArray() {
        return playerArray;
    }

    public void setPlayerArray(Player[] playerArray) {
        this.playerArray = playerArray;
    }

    

    

    
    

    
}

