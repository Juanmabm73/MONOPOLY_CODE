import java.util.ArrayList;


public class Transport extends Property{
    private int[] costStaying = new int [4] ;
   

    // 15;TRANSPORT;ESTACIÓN DE LAS DELICIAS;25;50;75;100;100
    public Transport(String [] segment, Terminal terminal) {
        super(Integer.parseInt(segment[0]), segment[2], terminal, Integer.parseInt(segment[3]), false, Integer.parseInt(segment[7]));
        for (int i = 0; i < 3; i++) {
             this.costStaying[i] = Integer.parseInt(segment[i+3]);        }
       
    }
    public Transport(){
        
    }

    @Override
    public void doOperation(Player player, Terminal terminal) {
        int response;
       
        if (getOwner() == null) {
            terminal.show("Player %d you are going to pay %d euros your balance will be %d euros to buy the property %s;",player.getPlayerId(),getPrice(), (player.getBalance() - getPrice()), getDescription());
            if (player.pay(getPrice(), false, terminal)) {
                terminal.show("Do you want to continue (1 = yes/ 0 = no)");
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
                    terminal.show("Operation cancelled");
                }

            }
            
        } else if (getOwner() != player) {
            int numberTransport = 1;

            for(Property property: getOwner().getProperties()){
                if(property instanceof Transport && !property.equals(this)){
                    numberTransport += 1;
                }
            }

            terminal.show("You are in %s property you will pay %d", getOwner(), getCostStaying()[numberTransport-1]);
            if (player.pay(getCostStaying()[numberTransport-1], true, terminal)){
                player.setBalance(player.getBalance() - getCostStaying()[numberTransport-1]);
                getOwner().setBalance(getOwner().getBalance() + getCostStaying()[numberTransport-1]);
                showPaymentSummary(getCostStaying()[numberTransport-1], player, terminal);
            } else {
                player.traspaseProperties(getOwner());
                terminal.show("Player %d transfered his properties to palyer %d", player.getPlayerId(),getOwner().getPlayerId());

            }
            
        } else if (getOwner() == player) {
            doOwnerOperation(player, terminal);

        } 
    }


    private void showPaymentSummary(int amount, Player player, Terminal terminal){
    
        terminal.show("Player %d has paid %d euros to player %d", player.getPlayerId(),amount,getOwner().getPlayerId());
        terminal.show("Player %d has recibed %d. Updated balance %d", getOwner().getPlayerId(), amount, getOwner().getBalance());
        
    }

    private void showPurchaseSummary(int amount, Player player, Terminal terminal) {
        terminal.show("Player %d has paid %d euros for buy %s", player.getPlayerId(), amount, getDescription());
        terminal.show("%s has been added to player %d properties", getDescription(), player.getPlayerId());
        
    }


    public int[] getCostStaying() {
        return costStaying;
    }
    
}

