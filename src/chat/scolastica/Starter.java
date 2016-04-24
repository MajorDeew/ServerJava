package chat.scolastica;

import chat.scolastica.server.*;
import chat.scolastica.http.*;
import java.io.IOException;
import java.util.ArrayList;


public class Starter {
    static ServerWeb webServer;
    static Server2 server;
    static ConfigProp prop;
    
    
    public static void main(String args[]) throws IOException {
        ArrayList<Object> netData= ConfigProp.getIstance().getPropertyData(ConfigProp.NET_DATA);
        String ip = (String) netData.get(0);
        int port = (int) netData.get(1);
        
        startServerWeb();
        
        server = new Server2(ip, port);
        
        try {
            server.bindServer();
            server.startFetch();
        } catch (IOException ex) {
            System.out.println("Errore nel bind del server: " + ex);
        }
        
        
    }
    
    public static void startServerWeb() {
        webServer = new ServerWeb();
        
        webServer.setName("Server Web");
        webServer.start();
    }
}
