import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Game implements SerializableI {
    private  MonopolyCode [] monopolyCodeArray = new MonopolyCode[81];
    private Terminal terminal; 
    private  Player[] playerArray = new Player [4];

    public Game(Terminal terminal) {
        this.terminal = terminal;
    }

    
  
    private void loadMonopolyCodes()  {
        BufferedReader reader = null;
        try { reader = new BufferedReader(new FileReader("./files/properties.txt"));
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
                terminal.show("Name of " + color + " player: ");
                String playerName = scanner.nextLine();
                playerArray[i] = (new Player(i+1, color, playerName, 1500, false));
            }
            
        }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(terminal);
        createPlayers();
        loadMonopolyCodes();
        while (playerArray.length > 1) {
            
            
            terminal.show("Enter card code");
            int id = scanner.nextInt();
            scanner.nextLine();
            terminal.show("Enter player id");
            int playerId = scanner.nextInt();
            scanner.nextLine();


            for (int i = 0; i < getMonopolyCodeArray().length; i++) {
                if (getMonopolyCodeArray()[i] != null && getMonopolyCodeArray()[i].getId() == id)  {
                    terminal.show(getMonopolyCodeArray()[i].toString());
                    getMonopolyCodeArray()[i].doOperation(getPlayerArray()[playerId - 1],terminal); //posicion del array no indice
                    break;
                    
                }
            }

            removePlayer();
            
            terminal.show("### ### ### ### ###");
            terminal.show("# PLAYERS SUMMARY #");
            terminal.show("### ### ### ### ###");

            for (Player player : getPlayerArray()) {
                if (player != null){
                    terminal.show(player.toString()+ " balance: " + player.getBalance() + ". Properties: ");
                    player.resume(terminal, player);
                }
            }

            
            
          

        }


          
        
        
            

        //fin del while
    } //fin del metodo

   //CAMBIAR METODO Y CONTROLAR JUGADORES NULOS
    private void removePlayer(){
        for (int i = 0; i < getPlayerArray().length; i++){
            if((getPlayerArray()[i] != null) && (getPlayerArray()[i].isBankrupt())){
                getPlayerArray()[i] = null;
                terminal.show("Player " + (i+1) + " is bankrupt and has been removed from the game");
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

