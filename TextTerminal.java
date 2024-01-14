import java.util.Scanner;


public class TextTerminal extends Terminal {
    private TranslatorManager translatorManager;
    public TextTerminal(){

    }
    public TextTerminal(TranslatorManager translatorManager) {
        super();
        this.translatorManager = translatorManager;
    }

    @Override 
    public void show(String s, Object...args) {
        
        Translator currentTranslator = translatorManager.getCurrentIdiom();
        if (currentTranslator == null) {
            System.out.println("No se ha establecido traductor");
        }else {
            String transalatedText = currentTranslator.Translate(s);
            String formatText = String.format(transalatedText, args);
            System.out.println(formatText);
        }
        
        
    } 

    @Override
    public int read() {
        
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();
        scanner.nextLine(); // para limpiar el scanner
        
        

        return option;
        
    }
   
    
}
   
