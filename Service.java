import java.util.ArrayList;


public class Service extends Property {
    private int [] costStaying = new int[2];
    
    // 12;SERVICE;COMPAÑÍA DE ELECTRICIDAD;4;10;75
    public Service( String [] segment, Terminal terminal) {
        super(Integer.parseInt(segment[0]), segment[2],  terminal, Integer.parseInt(segment[3]), false, Integer.parseInt(segment[5]));
        for (int i = 0; i < 1; i++) {
            costStaying[i] = Integer.parseInt(segment[i+3]);
        }
    }
    public Service(){
        
    }

    @Override
    public void doOperation(Player player, Terminal terminal) {
        int response;
        
        if (getOwner() == null) {
            terminal.show("%s you are going to pay %d euros your balance will be %d euros to buy the property %s", player.toString(), getPrice(), (player.getBalance() - getPrice()),getDescription());
            if (player.pay(getPrice(),false, terminal)){
                terminal.show("Do you want to continue (1 = yes/ 0 = no)");
                do{
                    response = terminal.read();
                } while ((response != 0) && (response != 1));
                
                if (response == 1) {
                    
                    setOwner(player);

                    if (player.getProperties() == null) {
                        player.setProperties(new ArrayList<>());
                    }
                    
                    player.setBalance(player.getBalance() - getPrice());
                    player.getProperties().add(this);
                    setOwner(player);
                    showPurchaseSummary(getPrice(), player, terminal);

                
                } else if (response == 0){
                    terminal.show("Operation canceled");
                }
            }
            

        } else if (getOwner() != player) {
            
            int dice;
            boolean doubleService = false; // comprobar si tiene dos cartas de tipo service
            for (Property property : getOwner().getProperties()) {
                if (property instanceof Service && !property.equals(this)){
                    doubleService = true;
                }
            }
            terminal.show("You are in %s property", getOwner());
            do{
                terminal.show("What number did you get on the dice");
                dice = terminal.read();
            
            } while(dice<1 && dice>6);
            
            if(doubleService == false){ 
                if (player.pay(dice*getCostStaying()[0], true, terminal)){
                    player.setBalance(player.getBalance() - dice*getCostStaying()[0]);
                    getOwner().setBalance(getOwner().getBalance() + dice*getCostStaying()[0]);
                    showPaymentSummary(dice*getCostStaying()[0], player, terminal);
                } else {
                    player.traspaseProperties(getOwner());
                    terminal.show("%s transfered his properties to %s", player.toString(),getOwner().toString());
                }
                
            } else{
                if (player.pay(dice*getCostStaying()[1], true, terminal)){
                    player.setBalance(player.getBalance() - dice*getCostStaying()[1]);
                    getOwner().setBalance(getOwner().getBalance() + dice*getCostStaying()[1]);
                    showPaymentSummary(dice*getCostStaying()[1], player, terminal);
                } else {
                    player.traspaseProperties(getOwner());
                    terminal.show("%s transfered his properties to %s", player.toString(),getOwner().toString());
                }
            }
            
        } else if (getOwner() == player) {
            doOwnerOperation(player, terminal);
        }
     
    }

    private void showPaymentSummary(int amount, Player player, Terminal terminal){
    
        terminal.show("%s has paid %d euros to %s",player.toString(), amount, getOwner().toString());
        terminal.show("%s has recibed %d. Updated balance %d",getOwner().toString() , amount, getOwner().getBalance());
        
    }

    private void showPurchaseSummary(int amount, Player player, Terminal terminal) {
        terminal.show("%s has paid %d euros for buy %s", player.toString(), amount, getDescription());
        terminal.show("%s has been added to %s properties",getDescription(), player.toString());
    }

    public int[] getCostStaying() {
        return costStaying;
    }

    public void setCostStaying(int[] costStaying) {
        this.costStaying = costStaying;
    }

    
}










