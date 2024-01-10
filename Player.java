import  java.util.ArrayList;

public class Player implements Serializable {
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
    public Player(){
        
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


    // public void pay(int price, boolean mandatory, Terminal terminal){
    //     Scanner scanner =  new Scanner(System.in);
    //     if (mandatory == false) {
    //         if ((getBalance() - price) > 0) {
    //             setBalance(getBalance() - price);
    //         }
    //         else if ((getBalance() - price) < 0) {
    //             terminal.show("Insuficcient money. Do you want to sell? (yes/no)");
    //             String response = scanner.nextLine();
    //             if (response.equalsIgnoreCase("yes")){
    //                 sellActives(price, false, terminal);
    //             } else {
    //                 terminal.show("Operation cancelled"); 
    //             }                    
    //         }
    //     } else if (mandatory == true) {
    //         if ((getBalance() - price) > 0) { 
    //             setBalance(getBalance() - price);
    //         } else if ((getBalance() - price) < 0){
    //             sellActives(price, true, terminal);
    //         }
    //     }

    // }

    public boolean pay(int amount, boolean mandatory, Terminal terminal){
        if (mandatory == true) {
            if (patrimony() > amount) {
                if (getBalance() > amount){
                    return true; //gestionamos pagos en cada propiedad 
                } else {
                    while (getBalance() < amount){
                        terminal.show("Objetive: " + amount);
                        showList(terminal);
                        terminal.show("Choose a number");
                        int num =terminal.read();
                        sellActives(num, terminal);
                    }  
                    return true; //cuando salga del while deberia de tener el dinero suficiente
                }
            }else { //no tiene suficiente patrimonio bancarota traspasamos donde toque
                terminal.show("You don't have enough patrimony for pay you are in bankrupt");
                setBankrupt(true);
                //traspasar propiedades en player
                return false; //no se efectua el pago por lo que traspasar a quien sea
            }
        } else { //no oblogatorio
            if (getBalance() > amount) {
                return true; //pagos en player
            } else{
                terminal.show("You don't have enough money");
                return false;
            }
        }
    }
    

    private void sellActives(int num, Terminal terminal) {
        int number = 0;
        int propertyValue = 0;
        Property property = getProperties().get(num);
        if (canSell(num) == true){
            if (property instanceof Street) {
                Street street = (Street) property;
                    do{
                        terminal.show("Built Houses " + street.getBuiltHouses());
                        terminal.show("Choose a number of houses to sell");
                        number =terminal.read();
                    } while(number<=1 && number>= street.getBuiltHouses());
                    
                    propertyValue += (street.getHousePrice()/ 2) * number;
                    
                    setBalance(getBalance() + propertyValue);
                    street.setBuiltHouses(street.getBuiltHouses() - number);
            }

        } else {
            terminal.show("We are going to Mortgage");
            propertyValue += property.getMortgageValue();
            setBalance(getBalance() + propertyValue);
            property.setMortaged(true);

        }

        
    }

    private boolean canSell(int num){ //comprueba si hay cosas para vender en la propiedad
        
        Property property = getProperties().get(num); //coge la propiedad con el num que es el id que hemos creado
            if (property instanceof Street ) {
                Street street = (Street) property;
                return street.getBuiltHouses() > 0;  // devuelve true si 
            } else {
                return false;
            }    
    }


    private void showList(Terminal terminal) {
        int index = 0;
        
        terminal.show("List of properties with posible sales");
        for (Property property : this.properties) {
            if (property instanceof Street){
                Street street = (Street) property;
                if (street.getBuiltHouses()>0){
                    terminal.show("Id: " + index + " " + street.toString() + " Built Houses: " + street.getBuiltHouses() + " Sell price: " + street.getHousePrice()/2 + "per house");
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

    public void traspasePropertiesToBank(){
        for (Property properties: getProperties()){
            if (properties instanceof Street) {
                Street street = (Street) properties;
                street.setBuiltHouses(0);
                street.setMortaged(false);
                street.setOwner(null);
            } else{
                properties.setMortaged(false);
                properties.setOwner(null);
            }
        }
    }


    public int patrimony(){
        int totalValue = getBalance();
        for (Property properties : getProperties()){
            if (properties instanceof Street){
                Street street = (Street) properties;
                totalValue += street.getBuiltHouses() * street.getHousePrice() + street.getMortgageValue();
            } else if (properties instanceof Transport){
                Transport transport = (Transport) properties;
                totalValue += transport.getMortgageValue();
            } else if (properties instanceof Service){
                Service service = (Service) properties;
                totalValue += service.getMortgageValue();
            }
        }
        return totalValue;
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



