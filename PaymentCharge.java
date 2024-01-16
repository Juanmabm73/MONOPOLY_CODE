import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PaymentCharge extends MonopolyCode{

    private int amount;   
    
    
    public PaymentCharge(String [] segment, Terminal terminal) {
        super(Integer.parseInt(segment[0]), segment[2], terminal);
        
        Pattern patternNum = Pattern.compile("(-?\\d+)(€)");
        Matcher matcherNum = patternNum.matcher(segment[2]);

        if (matcherNum.find()){
            String foundNumber = matcherNum.group(1);
            this.amount = Integer.parseInt(foundNumber);
        } else {
            this.amount = 0;
        }
    }
    public PaymentCharge(){
        
    }

    public void doOperation(Player player, Terminal terminal) {
        if (getAmount() < 0 ){
            if (player.pay(-getAmount(), true, terminal)){
            player.setBalance(player.getBalance() + getAmount());
            terminal.show("Player %d have paid %d", player.getPlayerId(),-getAmount());
            } else {
                player.traspasePropertiesToBank();
                terminal.show("The properties were traspased to the bank");
            }
        } else {
            player.setBalance(player.getBalance()+getAmount());
        }
       
        
    
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
        
}

