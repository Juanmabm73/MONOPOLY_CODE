import java.io.BufferedReader;
import java.io.FileReader;

import java.util.HashMap;
import java.util.Map;



public class Translator {
    private Map<String, String> dictionary = new HashMap<>();
    private String language;
    

    public Translator(String language) {
        try{
            this.language = language;
            BufferedReader reader = new BufferedReader(new FileReader("./files/" + language + ".txt"));
            String line;
            while((line =  reader.readLine()) != null){
            String [] segment = line.split(";");
                dictionary.put(segment[0], segment[1]); //lo metemos al diccionario con clave y traduccion;
            }
        } catch(Exception e){
            
        }
       
    }

    public String Translate(String sentence){
        String translation = dictionary.get(sentence);
        if (translation == null){
            return "No se encontro la traduccion para" + sentence;
        } else {
            return translation;
        }
        
    }

    
    public Map<String, String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, String> dictionary) {
        this.dictionary = dictionary;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    





}
