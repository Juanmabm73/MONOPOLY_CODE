import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RepairsCard extends MonopolyCode {
    private int amountForHouse;
    private int amountForHotel;

    public RepairsCard(String [] segment, Terminal terminal){

    super(Integer.parseInt(segment[0]), segment[2], terminal);
        Pattern pattern = Pattern.compile("(\\d+)(€)");
        Matcher matcher = pattern.matcher(segment[2]);

        
    
        if (matcher.find()) {
            String foundnumber = matcher.group(1);
            this.amountForHouse = Integer.parseInt(foundnumber);
        }
        
        if (matcher.find()) {
            String foundnumber = matcher.group(1);
            this.amountForHotel = Integer.parseInt(foundnumber);
        }

    }

    @Override
    public void doOperation(Player player, Terminal terminal) {
        int total = 0;
        if (player.getProperties() != null && !player.getProperties().isEmpty()) {
        for (Property property : player.getProperties()) {
            if (property instanceof Street) {
                Street street = (Street) property;
                
                if (street.getBuiltHouses() <= 4) {
                    total += street.getBuiltHouses() * this.amountForHouse;
                } else {
                    total += this.amountForHotel;
                }
            }
        }
    }
    player.pay(total, true, terminal);
    // total -= player.getBalance();
    // player.setBalance(total);
    System.out.println(player.getBalance());
    }

    public int getAmountForHouse() {
        return amountForHouse;
    }

    public void setAmountForHouse(int amountForHouse) {
        this.amountForHouse = amountForHouse;
    }

    public int getAmountForHotel() {
        return amountForHotel;
    }

    public void setAmountForHotel(int amountForHotel) {
        this.amountForHotel = amountForHotel;
    }

    
}
