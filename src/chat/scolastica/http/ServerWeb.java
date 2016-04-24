package chat.scolastica.http;

import java.io.File;
import java.io.IOException;
import org.jibble.simplewebserver.*;

public class ServerWeb extends Thread{
    File fileIndex;
    
    public ServerWeb() {
        fileIndex = new File("./src/resources/html");
    }
    
    @Override
    public void run() {
        try {
            SimpleWebServer server = new SimpleWebServer(fileIndex, 80);
            System.out.println("Server Web aperto sulla porta 80");
        } catch (IOException ex) {
            System.out.println("Errore server web: " + ex);
        }
    }
}
