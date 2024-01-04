import java.util.Scanner;

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

    
    
    public String resumeString(){
        return getDescription() + " " + this.mortaged;
    }

    public void doOwnerOperation(Player player, Terminal terminal) {
        Scanner scanner = new Scanner(System.in);
        int num;
        String response;
        terminal.show("You are in your property what do you want to do?");
        terminal.show("1. Mortgage property");
        terminal.show("2. Sell property");
        int option = scanner.nextInt();

        switch (option) {
            case 1:

                terminal.show("You are going to mortgaged your property for: " + getMortgageValue() + "euros");
                terminal.show("Do you want to continue (yes/no)");
                response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    player.setBalance(player.getBalance() + getMortgageValue());
                    setMortaged(true);
                } else {
                    terminal.show("Operation Canceled");
                }

                break;
            case 2:
                terminal.show("You are going to sell your property for: " + getPrice() / 2 + "euros");
                terminal.show("Do you want to continue (yes/no)");
                response = scanner.nextLine();
                if (response.equalsIgnoreCase("yes")) {
                    sellProperty(player, getPrice(), terminal);
                    terminal.show("Sold Property");
                } else {
                    terminal.show("Operation Canceled");
                }
                break;
        }
    }

    public void sellProperty(Player player, int price, Terminal terminal) {
        player.setBalance(player.getBalance() + price / 2);
        setOwner(null);
        player.getProperties().remove(this);
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
