import java.util.Scanner;

public class TextTerminal extends Terminal {
    public TextTerminal() {
        super();
    }

    @Override 
    public void show(String s) {
        // Translator t = new Translator(s);
        System.out.println(s);
        
    } 

    @Override
    public int read() {
        
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine(); // para limpiar el scanner
        
        

        return option;
        
    }
   
    
}
   
