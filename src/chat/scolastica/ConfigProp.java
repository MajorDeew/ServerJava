package chat.scolastica;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class ConfigProp {
    private static ConfigProp istanza;
    
    static InputStream inputStream;
    
    static String propFileName = "config.properties";
    static Properties confFile = new Properties();
    
    static int NET_DATA = 1;
    static int DB_DATA = 2;
    
    private ConfigProp() {
        try {
            inputStream = getClass().getClassLoader().getResourceAsStream("resources/config/" + propFileName);
            if (inputStream != null) {
                confFile.load(inputStream);
                printConfigFile();
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
                System.err.println("Errore nella chiusura del file config: " + ex);
            }
        }
    }
    
    public static ConfigProp getIstance() throws IOException {
        if (istanza == null) {
            istanza = new ConfigProp();
        }
        
        return istanza;
    }
    
    public static ArrayList<Object> getPropertyData(int ciao) {
        ArrayList<Object> result = new ArrayList<>();
        
        switch(ciao) {
            case 1:
                result.add(confFile.getProperty("INDIRIZZO_IP"));
                result.add(Integer.parseInt(confFile.getProperty("PORTA")));
                
                break;
            case 2:
                result.add(confFile.getProperty("TIPO_DATABASE_USATO"));
                result.add(confFile.getProperty("DB_HOST"));
                result.add(confFile.getProperty("DB_USERNAME"));
                result.add(confFile.getProperty("DB_PASSWORD"));
                
                break;
        }
        
        return result;
    }
    
    public static String getStringValue(String key) {
        String result = confFile.getProperty(key);
        
        return result;
    }
    
    public static void printConfigFile() {
        Set<Object> keys = confFile.keySet();
        
        System.out.println("File di configurazione: ");
        
        for(Object k:keys){
            String key = (String) k;
            System.out.println(" " + key + ": " + confFile.getProperty(key));
        }
        
        System.out.println();
    }
}
