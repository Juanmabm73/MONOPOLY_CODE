import java.util.ArrayList;


public class Transport extends Property{
    private int[] costStaying = new int [4] ;
   

    // 15;TRANSPORT;ESTACIÓN DE LAS DELICIAS;25;50;75;100;100
    public Transport(String [] segment, Terminal terminal) {
        super(Integer.parseInt(segment[0]), segment[2], terminal, Integer.parseInt(segment[3]), false, Integer.parseInt(segment[7]));
        for (int i = 0; i < 3; i++) {
             this.costStaying[i] = Integer.parseInt(segment[i+3]);        }
       
    }

    @Override
    public void doOperation(Player player, Terminal terminal) {
        int response;
       
        if (getOwner() == null) {
            terminal.show(player.toString() + " you are going to pay " + getPrice() + " euros your balance will be " + (player.getBalance() - getPrice()) + " euros to buy the property");
            if (player.pay(getPrice(), false, terminal)) {
                terminal.show("For accept enter 1 for cancel enter 0");
                do{
                    response = terminal.read();
                } while ((response != 0) && (response != 1));
                if (response == 1) {
                    player.setBalance(player.getBalance() - getPrice());
                    setOwner(player);

                    if (player.getProperties() == null) {
                        player.setProperties(new ArrayList<>());
                    }

                    player.getProperties().add(this);
                    
                    showPurchaseSummary(getPrice(), player, terminal);
                
                } else if (response == 0) {
                    terminal.show("Operation canceled");
                }

            }
            
        } else if (getOwner() != player) {
            int numberTransport = 1;

            for(Property property: getOwner().getProperties()){
                if(property instanceof Transport && !property.equals(this)){
                    numberTransport += 1;
                }
            }

            terminal.show("You are in " + getOwner() + " property you will pay" + getCostStaying()[numberTransport-1]);
            if (player.pay(getCostStaying()[numberTransport-1], true, terminal)){
                player.setBalance(player.getBalance() - getCostStaying()[numberTransport-1]);
                getOwner().setBalance(getOwner().getBalance() + getCostStaying()[numberTransport-1]);
                showPaymentSummary(getCostStaying()[numberTransport-1], player, terminal);
            } else {
                player.traspaseProperties(getOwner());
                terminal.show(player.toString() + " transferred his properties to " + getOwner().toString());

            }
            
        } else if (getOwner() == player) {
            doOwnerOperation(player, terminal);

        } 
    }


    private void showPaymentSummary(int amount, Player player, Terminal terminal){
    
        terminal.show(player.toString() + " has paid " + amount + " euros to " + getOwner().toString());
        terminal.show(player.toString() + " old balance were " + (player.getBalance()+amount) + " updated balance: " + player.getBalance());
        terminal.show(getOwner().toString() + " has recibed " + amount + ". Updated balance: " + getOwner().getBalance());
        
    }

    private void showPurchaseSummary(int amount, Player player, Terminal terminal) {
        terminal.show(player.toString() + " has paid " + amount + " euros for buy "+ getDescription());
        terminal.show(player.toString() +  " updated balance is " + player.getBalance());
        terminal.show(getDescription() + " has been added to " + player.toString() + " properties");
        terminal.show("Properties of " + player.toString() + ": " + player.getProperties());
    }


    public int[] getCostStaying() {
        return costStaying;
    }
    
}

