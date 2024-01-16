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

    public Street(){
        
    }

    
    


    @Override
    public void doOperation(Player player, Terminal terminal) {
        
        int response;

        if (getOwner() == null) {
            if(player.pay(getPrice(), false, terminal)){
            
                terminal.show("Player %d you are going to pay %d euros for %s your balance will be %d euros",player.getPlayerId(),getPrice(), getDescription(), (player.getBalance() - getPrice()));
                terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                
                do {
                    response = terminal.read();
                } while ((response != 0) && (response != 1));

                if (response == 1)  {
                    
                    player.setBalance(player.getBalance() - getPrice());

                    if (player.getProperties() == null) {
                        player.setProperties(new ArrayList<>());
                    }   

                    player.getProperties().add(this);
                    setOwner(player);

                    terminal.show("Player %d has paid %d euros for buy %s. Updated balance: %d",player.getPlayerId(), getPrice(), getDescription(),player.getBalance());
                    terminal.show("%s has been added to player %d properties", getDescription(), player.getPlayerId());
                    
                
                } else if (response == 0){
                    terminal.show("Operation cancelled");
                }
            } else {
                terminal.show("Your balance is not enough");
            }
            
            
        } else if (getOwner() != player) {
        
            if (isMortaged()) {
                terminal.show("You are in %s property. But is mortgaged you don't pay rent", getOwner());
            } else {
                if (player.pay(getCostStayingWithHouses()[getBuiltHouses()], true, terminal)){
                    terminal.show("You are in %s property you will pay: %d", getOwner(), getCostStayingWithHouses()[getBuiltHouses()]);
                    getOwner().setBalance(getOwner().getBalance() + getCostStayingWithHouses()[getBuiltHouses()]); //dueño recibe
                    player.setBalance(player.getBalance() - getCostStayingWithHouses()[getBuiltHouses()]);
                    showPaymentSummary(getCostStayingWithHouses()[getBuiltHouses()], player, terminal);
                } else {
                    player.traspaseProperties(getOwner());
                    terminal.show("Player %d transfered his properties to player %d",player.getPlayerId(),getOwner().getPlayerId());
                }
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
        if (isMortaged()){
            terminal.show("4. Unmortgaged property");
        }
        
        
        switch (option) {
            case 1:
                if (getBuiltHouses() == 0) {
                    terminal.show("%d euros", getMortgageValue());
                    terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0) && (response != 1));

                    if (response == 1){
                        player.setBalance(player.getBalance() + getMortgageValue());
                        setMortaged(true);

                    } else if (response  == 0) {
                        terminal.show("Operation Cancelled");
                    }
                } else {
                     terminal.show("You can't Mortgage this property, you must sell all houses before");
                }
                break;
            case 2: 
                terminal.show("At this moment you have %d houses",getBuiltHouses());
                if (getBuiltHouses() == 4) {
                    terminal.show("You can only buy a Hotel for: %d euros", getHousePrice());
                    if (player.pay(getHousePrice(), false, terminal)){
                        terminal.show("Do you want to continue (1 = yes/ 0= no)");
                        do {
                            response = terminal.read();
                        } while ((response != 0)  && (response != 1));

                        if (response == 1){
                            player.setBalance(player.getBalance() - getHousePrice());
                            setBuiltHouses(5);
                        } else if (response == 0){
                            terminal.show("Operation cancelled");
                        }
                    }
                    
                } else if (getBuiltHouses() < 4) {
                    do{
                        terminal.show("You can buy %d houses", (4-getBuiltHouses()));
                        terminal.show("How many houses do you want to buy");
                        num = terminal.read();
                        
                    } while ((num<=1) && (num >= 4-getBuiltHouses()));
                    
                    if ( player.pay(num*getHousePrice(), false, terminal)){
                        terminal.show("You are going to buy %d houses for %d", num, num*getHousePrice());
                        terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                        do {
                            response = terminal.read();
                        } while ((response != 0) && (response != 1));
                        if (response == 1){
                            player.setBalance(player.getBalance() - num*getHousePrice());
                            setBuiltHouses(getBuiltHouses() + num);
                            showPurchaseSummary(num, player, terminal);
                        } else {    
                            terminal.show("Operation cancelled");
                        }
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
                        terminal.show("You have %d houses to sell", getBuiltHouses());
                        terminal.show("How many houses do you want to sell");
                        num = terminal.read();
                    } while(num<= 1 && num >= getBuiltHouses());
                
                    terminal.show("You are going to sell %d houses for %d", num, (getHousePrice()/2 * num )+ " your balance will be" +(player.getBalance()+(getHousePrice()/2 * num)));
                    terminal.show("Do you want to continue (1 = yes/0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response != 0) && (response != 1));

                    if (response == 1){
                        player.setBalance(player.getBalance() + (getHousePrice()/2 * num));
                        setBuiltHouses(getBuiltHouses() - num);
                    } else {
                        terminal.show("Operation cancelled");
                    }
                    
                }
                break;
            case 4: 
                
                terminal.show("You are going to unmortgaged your property for %d", (getMortgageValue() + (int) 0.1*getMortgageValue()));
                if (player.pay(getMortgageValue() + (int) 0.1*getMortgageValue(), false, terminal)){
                    terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                    do {
                        response = terminal.read();
                    } while ((response == 0) && (response == 1));
                    if ( response == 1){
                        player.setBalance(getMortgageValue() - (int) 0.1*getMortgageValue());
                        setMortaged(false);
                    } else {
                        terminal.show("Operation cancelled");
                    }
                }
                
            
            break;

            default:
                
                break;
        }   
       

    }

    private void showPaymentSummary(int amount, Player player, Terminal terminal){
    
        terminal.show("Player %d has paid %d euros to player %d", player.getPlayerId(), amount, getOwner().getPlayerId());
        
        
    }

    private void showPurchaseSummary(int houses, Player player, Terminal terminal) {
        if (getBuiltHouses() <= 4) {
            terminal.show("Player %d you have bought %d on the property %s", player.getPlayerId(),houses, getDescription());
            terminal.show("At this moment you have %d houses on %s",getBuiltHouses(),getDescription());
        } else if (getBuiltHouses() == 5) {
            terminal.show("You have bought an hotel, your property %s is fully developed", getDescription());
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
