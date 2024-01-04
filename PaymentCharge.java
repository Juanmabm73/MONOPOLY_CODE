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

    public void doOperation(Player player, Terminal terminal) {
        int amount = -this.amount; //paso a negativo para que funcione en pay
        player.pay(amount, true, terminal);
        // player.setBalance(player.getBalance() + this.amount);
        terminal.show( "Balance: " + player.getBalance());
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
        
}

