package chat.scolastica.server;

import java.io.*;
import java.net.*;
/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;*/

public class Server extends Thread {
//Settaggio delle variabili d'istanza
    ServerSocket serverSocket;
    Socket nuovoClient;
    
    String ip;
    int port;
    int nMaxUsers = 30;
    
    boolean isRunning = false;
    
//Costrutti vari
    public Server(int port) {
        this.port = port;
    }
    public Server(int port, int maxUsers) {
    	this.port = port;
    	this.nMaxUsers = maxUsers;
    }
    /*public Servers(String ip, int port, int maxUsers) {
    	this.ip = ip;
    	this.port = port;
    	this.nMaxUsers = maxUsers;
    }*/

//Metodo che avvia appunto il listening del server e la connessione dei client 
    @Override
    public void run() {
    	startServer();
    	
    	if(isRunning) 
            System.out.println("**ServerSocket " + getName() + " creato con porta " + port + "**");
    	
    	while(isRunning) {
            nuovoClient = connectClient();
            //arrayClient.addClient(nuovoClient);
            
            //console.write(nuovoClient.toString());
            
            nuovoClient = null;
    	}
	    
    	if(stopS())
	    	System.exit(0);
    }

//Metodo per avviare il server, con valore booleano di ritorno
    public boolean startServer() {
        try {
            serverSocket = new ServerSocket(port, nMaxUsers);
        } catch (IOException ex) {System.out.println(ex); return isRunning = false;}
        
        return isRunning = true;
    }

//Metodo per mattere in ascolto il server
    public Socket connectClient() {
        Socket clientSocket = null;
        
        while (clientSocket == null) {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException ex) {System.out.println(ex);}
        }
        
        return clientSocket;
    }
    
//Metodo per mandare un messaggio String come argomento al client, con valore booleano di ritorno
    /*public boolean sendMsg(Socket clientSocket, String msg) {
        try {
            inputByClient = new PrintStream(clientSocket.getOutputStream());
            inputByClient.println(msg);
            System.out.println("Server " + currentThread().getName() + "> " + msg);
        } catch (IOException ex) {System.out.println(ex);return false;}
        
        return true;
    }
    
//Metodo per ricevere messaggi dal client
    @SuppressWarnings("deprecation")
	public boolean receiveMsg(Socket clientSocket) {
        String line;
        
        try {
            outputFromServer = new DataInputStream(clientSocket.getInputStream());
            line = outputFromServer.readLine();
            System.out.println("Client> " + line);
        } catch (IOException ex) {System.out.println(ex);return false;}
        
        return true;
    }*/
    
//Metodo per stoppare il server
    public boolean stopS() {
        try {
            //outputFromServer.close();
            //inputByClient.close();
            isRunning = false;
            serverSocket.close();
            
            //console.dispose();
            
            System.out.println("**Server " + getName() + " chiuso con successo**");
        } catch (Exception ex) {System.out.println(ex);return false;}
        
        return true;
    }
}