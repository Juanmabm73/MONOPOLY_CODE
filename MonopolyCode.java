public class MonopolyCode implements Serializable {
    private int id;
    private String description;
    private Terminal terminal;
    
    
    
    public MonopolyCode(int id, String description, Terminal terminal) {
        this.id = id;
        this.description = description;
        this.terminal = terminal;
    }

    public MonopolyCode(){

    }
    
    public void doOperation(Player player, Terminal terminal){
        
    };


    @Override
    public String toString() {
        return this.description;
    }

    

    public int getId() {
        return id;
    }



    public void setId(int id) {
        this.id = id;
    }



    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }



    public Terminal getTerminal() {
        return terminal;
    }



    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }






    



}
