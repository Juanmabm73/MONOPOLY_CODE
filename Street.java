import java.util.ArrayList;


public class Street extends Property{
    private int builtHouses;
    private int housePrice;
    private int [] costStayingWithHouses = new int [6];
    
    // 16;STREET;CALLE FELIPE;14;70;200;550;750;950;100;100;90
    public Street(String [] segment, Terminal terminal) {
        super(Integer.parseInt(segment[0]), segment[2], terminal, Integer.parseInt(segment[3]), false, Integer.parseInt(segment[11]));
        this.builtHouses = 0;
        this.housePrice = Integer.parseInt(segment[9]);
        for (int i = 0; i < 6; i++) { //si en vez de 6 es 5 el 950 me mete un 0
            this.costStayingWithHouses[i] = Integer.parseInt(segment[i+3]);
        }
    }

    
    public String toDetailedString(){ // se usa para mostrar el resumen general
        return super.toString() + " houses: " + getBuiltHouses() + ". Mortgaged: " + isMortaged() ;
    }


    @Override
    public void doOperation(Player player, Terminal terminal) {
        
        int response;

        if (getOwner() == null) {
            
            terminal.show(player.toString() + " you are going to pay " + getPrice() + " euros for " + getDescription() + " your balance will be " + (player.getBalance() - getPrice()) + " euros");
            terminal.show("For accept enter 1 for cancel enter 0");
            do {
                response = terminal.read();
            } while ((response != 0) && (response != 1));

            if (response == 1)  {
                player.pay(getPrice(), false, terminal); //no es obligatorio

                if (player.getProperties() == null) {
                    player.setProperties(new ArrayList<>());
                }   

                player.getProperties().add(this);
                setOwner(player);
            
            } else if (response == 0){
                terminal.show("Operation canceled");
            }

            terminal.show(player.toString() + " has paid " + getPrice() + " euros for buy "+ getDescription() + " .Updated balance: " + player.getBalance());
            terminal.show(getDescription() + " has been added to " + player.toString() + " properties");
            terminal.show("Properties of " + player.toString() + ": " + player.getProperties());
            
        } else if (getOwner() != player) {
            if (isMortaged()) {
                terminal.show("You are in " + getOwner() + "property but is Mortgaged you don't pay rent");
            } else {
                terminal.show("You are in " + getOwner() +  " property you will pay: " + getCostStayingWithHouses()[getBuiltHouses()]);
                terminal.show("NUMERO DE CASAS: "+getBuiltHouses() + "PRECIO: " + getCostStayingWithHouses()[getBuiltHouses()]);
                player.pay(getCostStayingWithHouses()[getBuiltHouses()], true, terminal); //lo mandas a pago obligatorio
                getOwner().setBalance(getOwner().getBalance() + getCostStayingWithHouses()[getBuiltHouses()]); //dueño recibe
                if (player.isBankrupt()){
                    player.traspaseProperties(getOwner());
                    terminal.show(player.toString() + " transferred his properties to " + getOwner().toString());
                }
                showPaymentSummary(getCostStayingWithHouses()[getBuiltHouses()], player, terminal);
            }
            
        } else if (getOwner() == player) {
            doOwnerOperation(player, terminal);
        }

        
    }

    @Override
    public void doOwnerOperation(Player player, Terminal terminal) {
        int response;
        int num;// para el numero de casas
        
        terminal.show("You are in your property what do you want to do?");
        terminal.show("1. Mortgage property");
        terminal.show("2. Buy houses or Hotels");
        if (getBuiltHouses() > 0){
            terminal.show("3. Sell Houses of the Property");
        }
        int option = terminal.read();
        
        
        switch (option) {
            case 1:
                if (getBuiltHouses() == 0) {
                    terminal.show("You are going to mortgaged your property for: " + getMortgageValue() + "euros");
                    terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0) && (response != 1));

                    if (response == 1){
                        player.setBalance(player.getBalance() + getMortgageValue());
                        setMortaged(true);

                    } else if (response  == 0) {
                        terminal.show("Operation Canceled");
                    }
                } else {
                     terminal.show("You can't Mortgage this property, you must sell all houses before");
                }
                break;
            case 2: 
                terminal.show("At this moment you have " + getBuiltHouses() + " houses");
                if (getBuiltHouses() == 4) {
                    terminal.show("You can only buy a Hotel for: " + getHousePrice() + "euros" );
                    terminal.show("Do you want to continue (1 = yes/ 0= no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0)  && (response != 1));

                    if (response == 1){
                        player.pay(getHousePrice(), false, terminal);
                        setBuiltHouses(5);
                    } else if (response == 0){
                        terminal.show("Operation cancelled");
                    }
                } else if (getBuiltHouses() < 4) {
                    do{
                        terminal.show("You can buy " + (4-getBuiltHouses()) + " houses");
                        terminal.show("How many houses do you want to buy");
                        num = terminal.read();
                        
                    } while ((num<=1) && (num >= 4-getBuiltHouses()));
                    terminal.show("You are going to buy " + num + " houses for " + num*getHousePrice());
                    terminal.show("Do you accept operation ( 1 = yes / 0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0) && (response != 1));
                    if (response == 1){
                        player.pay(num*getHousePrice(), false, terminal);
                        setBuiltHouses(getBuiltHouses() + num);
                        showPurchaseSummary(num, player, terminal);
                    } else {    
                        terminal.show("Operation canceled");
                    }
                } else {
                    terminal.show("At this moment your property is fully developed you can't buy anything more");
                }
                break;
            case 3: 
                if (getBuiltHouses() == 0){
                    terminal.show("You don't have any house to sell");
                } else {
                    do{
                        terminal.show("You have " + getBuiltHouses() + " houses to sell");
                        terminal.show("How many houses do you want to sell");
                        num = terminal.read();
                    } while(num<= 1 && num >= getBuiltHouses());

                    terminal.show("You are going to sell" + num + " houses for " + "your balance will be" + player.getBalance()+(getHousePrice()/2 * num));
                    terminal.show("Do you want to continue (1 = yes/0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0) && (response != 1));

                    if (response == 1){
                        player.pay(-(getHousePrice()/2 * num), false, terminal);
                        setBuiltHouses(getBuiltHouses() - num);
                    } else {
                        terminal.show("Operation cancelled");
                    }
                }
                break;

            default:
                terminal.show("Introduzca correctamente el numero");
                break;
        }   
       

    }

    private void showPaymentSummary(int amount, Player player, Terminal terminal){
    
        terminal.show(player.toString() + " has paid " + amount + " euros to " + getOwner().toString());
        // for (int i = 0; i < costStayingWithHouses.length; i++) {
        //     terminal.show("PRECIO " + i + " " + getCostStayingWithHouses()[i]);
        // }
        //comprobaciones del array
        
    }

    private void showPurchaseSummary(int houses, Player player, Terminal terminal) {
        if (getBuiltHouses() <= 4) {
            terminal.show(player.toString() + " you have bought " + houses  + " on the property " + getDescription());
            terminal.show("At this moment you have " + getBuiltHouses() + " houses on " + getDescription());
        } else if (getBuiltHouses() == 5) {
            terminal.show("You have bought an hotel, your property " + getDescription() + " is fully developed congratulations");
        }
            
    }
    public int getBuiltHouses() {
        return builtHouses;
    }


    public void setBuiltHouses(int builtHouses) {
        this.builtHouses = builtHouses;
    }


    public int getHousePrice() {
        return housePrice;
    }


    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }


    public int[] getCostStayingWithHouses() {
        return costStayingWithHouses;
    }


    public void setCostStayingWithHouses(int[] costStayingWithHouses) {
        this.costStayingWithHouses = costStayingWithHouses;
    }

    //getters y setters 
   

   

    


    
}
