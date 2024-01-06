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

    @Override
    public void doOperation(Player player, Terminal terminal) {
        int response;
        
        if (getOwner() == null) {
            terminal.show(player.toString() + " you are going to pay " + getPrice() + " euros your balance will be " + (player.getBalance() - getPrice()) + " euros to buy the property" + getDescription());
            terminal.show("For accept enter 1 for cancel enter 0.");
            do{
                response = terminal.read();
            } while ((response == 0) && (response == 1));
            
            if (response == 1) {
                player.pay(getPrice(),false, terminal);
                setOwner(player);

                if (player.getProperties() == null) {
                    player.setProperties(new ArrayList<>());
                }

                player.getProperties().add(this);
                setOwner(player);
            
            } else if (response == 0){
                terminal.show("Operation canceled");
            }
            showPurchaseSummary(getPrice(), player, terminal);
            // terminal.show( "Propiedades: " + player.getProperties());

        } else if (getOwner() != player) {
            
            int dice;
            boolean doubleService = false; // comprobar si tiene dos cartas de tipo service
            for (Property property : getOwner().getProperties()) {
                if (property instanceof Service && !property.equals(this)){
                    doubleService = true;
                }
            }
            terminal.show("You are in " + getOwner() +  " property");
            do{
                terminal.show("What number did you get on the dice");
                dice = terminal.read();
            
            } while(dice<1 && dice>6);
            
            if(doubleService == false){ 
                player.pay(dice*getCostStaying()[0], true, terminal);
                getOwner().setBalance(getOwner().getBalance() + dice*getCostStaying()[0]);
                showPaymentSummary(dice*getCostStaying()[0], player, terminal);
            } else{
                player.pay(dice*getCostStaying()[1], true, terminal);
                getOwner().setBalance(getOwner().getBalance() + dice*getCostStaying()[0]);
                showPaymentSummary(dice*getCostStaying()[1], player, terminal);
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

    public void setCostStaying(int[] costStaying) {
        this.costStaying = costStaying;
    }

    
}










