import  java.util.ArrayList;
import java.util.Scanner;
public class Player implements SerializableI {
    private int playerId;
    private Color color;
    private String name;
    private int balance;
    private boolean bankrupt = false;
    private ArrayList<Property> properties;
    
    public Player(int playerId, Color color, String name, int balance, boolean bankrupt) {
        this.playerId = playerId;
        this.color = color;
        this.name = name;
        this.balance = balance;
        this.bankrupt = bankrupt;
        properties = new ArrayList<>();
    }

    public void resume(Terminal terminal, Player player){
        
            for (Property properties : player.getProperties()) {
                if (properties instanceof Street){
                    Street street = (Street) properties;
                    terminal.show(street.toDetailedString()); //llamamos al "toString" de street
                } else{
                    terminal.show(properties.resumeString());
                }
        }
    }


    public void pay(int price, boolean mandatory, Terminal terminal){
        Scanner scanner =  new Scanner(System.in);
        if (mandatory == false) {
            if ((getBalance() - price) > 0) {
                setBalance(getBalance() - price);
            }
            else if ((getBalance() - price) < 0) {
                terminal.show("Insuficcient money. Do you want to sell? (yes/no)");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")){
                    sellActives(price, false, terminal);
                } else {
                    terminal.show("Operation cancelled"); 
                }                    
            }
        } else if (mandatory == true) {
            if ((getBalance() - price) > 0) { 
                setBalance(getBalance() - price);
            } else if ((getBalance() - price) < 0){
                sellActives(price, true, terminal);
            }
        }

    }

    
    private void sellOrMortgaged(Terminal terminal) {
        Scanner scanner = new Scanner(System.in);
        int sales = 0;
        int propertyValue = 0;
        int number = 0;
        
        showList(terminal);
        terminal.show("Choose your Property id from list");
        int num = scanner.nextInt();
        scanner.nextLine();
        if (canSell(num)) { //esto decide entre vender e hipotecar 
            Property property = getProperties().get(num);
            if (property instanceof Street) {
                Street street = (Street) property;
                    do{
                        terminal.show("Built Houses " + street.getBuiltHouses());
                        terminal.show("Choose a number of houses to sell");
                        number = scanner.nextInt();
                        scanner.nextLine();
                    } while(number>=1 && number<= street.getBuiltHouses());
                    
                    propertyValue = (street.getHousePrice()/ 2) * number;
                    
                    sales += propertyValue;
                    setBalance(getBalance() + propertyValue);
                    street.setBuiltHouses(street.getBuiltHouses() - number);
            }
        } else { 
            terminal.show("We are going to Mortgage");
            Property property = getProperties().get(num);
            propertyValue = property.getMortgageValue();
            sales += property.getMortgageValue();
            setBalance(getBalance() + propertyValue);
            property.setMortaged(true);
            
        }
    }

    private void sellActives(int target, boolean mandatory, Terminal terminal) {
        Scanner scanner = new Scanner(System.in);
        
        if (mandatory){
        while (getBalance() < target) {
            if (thereAreThingsToSell() == false) {
                    setBankrupt(true);
                    break;
            } else{
                    terminal.show("You need to mortgage");
                    sellOrMortgaged(terminal);
                
            }
        }
        } else {
            do{
                sellOrMortgaged(terminal);
                terminal.show("Do you want to continue selling (yes/no)");
                String response = scanner.nextLine();
                if (response.equalsIgnoreCase("no")) {
                    break;
                }
            } while (getBalance() < target);
        }
    }

    private boolean canSell(int num){ //comprueba si hay cosas para vender en la propiedad
        System.out.println(num);
        Property property = getProperties().get(num); //coge la propiedad con el num que es el id que hemos creado
            if (property instanceof Street ) {
                Street street = (Street) property;
                return street.getBuiltHouses() > 0;  // devuelve true si 
            } else {
                return false;
            }    
    }

    public boolean thereAreThingsToSell(){
        for (Property properties : getProperties()) {
            if (properties instanceof Street) {
                Street street = (Street) properties;
            
                if(!properties.isMortaged() && street.getBuiltHouses() > 0 ){
                    return true;
                }

            } else {
                if(!properties.isMortaged()){
                    return true;
                }
            }}
        return false;
    }

    private void showList(Terminal terminal) {
        int index = 0;
        
        terminal.show("List of properties with posible sales");
        for (Property property : this.properties) {
            if (property instanceof Street){
                Street street = (Street) property;
                if (street.getBuiltHouses()>0){
                    terminal.show("Id: " + index + " " + street.toString() + "Built Houses: " + street.getBuiltHouses() + "Sell price: " + street.getCostStayingWithHouses()[4]/2);
                }
            }
            index++;
        }
        index = 0;
        
        terminal.show("List of properties with posible mortgage");
        for (Property property : this.properties) {
            if (property instanceof Street){
                Street street =  (Street) property;
                if (street.getBuiltHouses() == 0) {
                    terminal.show("Id: " + index + " " + street.toString() + "Mortgage Value: " + street.getMortgageValue());
                }
            } else if (property instanceof Service || property instanceof Transport) {
                terminal.show("Id: " + index + " " + property.toString() + "Mortgage Value: " + property.getMortgageValue());
            }
        index++;
        }
    }

    public void traspaseProperties(Player newOwner){
        for(Property properties : getProperties()) {
            newOwner.getProperties().add(properties);
            properties.setOwner(newOwner);
            // traspaso de las propiedades pero todas hipotecadas revisar

        }

    }
    
    @Override
    public String toString() {
        return "Player" + " " + this.playerId;
    }
    public Color getColor() {
        return color;
    }


    public void setColor(Color color) {
        this.color = color;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getBalance() {
        return balance;
    }


    public void setBalance(int balance) {
        this.balance = balance;
    }


    public boolean isBankrupt() {
        return bankrupt;
    }


    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public int getPlayerId() {
        return playerId;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

   

    
    
    
    


}



