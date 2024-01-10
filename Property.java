

public class Property extends MonopolyCode {
    private int price;
    private boolean mortaged;
    private int mortgageValue;
    private Player owner = null;

    public Property(int id, String description, Terminal terminal, int price, boolean mortaged, int mortgageValue) {
        super(id, description, terminal);
        this.price = price;
        this.mortaged = mortaged;
        this.mortgageValue = mortgageValue;
    }

    public Property(){

    }
    
    
    public String resumeString(){
        return getDescription() + " " + this.mortaged;
    }

    public void doOwnerOperation(Player player, Terminal terminal) {
        
        
        int response;
        terminal.show("You are in your property what do you want to do?");
        terminal.show("1. Mortgage property");
        terminal.show("2. Nothing");
        if (isMortaged()) {
            terminal.show("3. Unmortgage");
        }

        int option = terminal.read();

        switch (option) {
            case 1:

                terminal.show("You are going to mortgaged your property for: " + getMortgageValue() + "euros");
                terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                do {
                    response = terminal.read();
                } while ((response == 0) && (response == 1));

                if (response == 1) {
                    player.setBalance(player.getBalance() + getMortgageValue());
                    setMortaged(true);
                } else if (response == 0){
                    terminal.show("Operation Canceled");
                }

                break;
            case 2:
                terminal.show("No opperation");
                
                break;
            case 3: 
                terminal.show("You are going to unmortgaged your property for" + (getMortgageValue() + (int) 0.1*getMortgageValue()));
                if (player.pay(getMortgageValue() + (int) 0.1*getMortgageValue(), false, terminal)){
                    terminal.show("Confirm opperation (0 = no/ 1 = yes)");
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
                
            }
    }   
    

    

    

    public int getPrice() {
        return price;
    }

    public boolean isMortaged() {
        return mortaged;
    }

    public void setMortaged(boolean mortaged) {
        this.mortaged = mortaged;
    }

    public int getMortgageValue() {
        return mortgageValue;
    }



    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

}
