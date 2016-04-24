package chat.scolastica.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

public class Server2 {
    InetSocketAddress serverSockAddr;
    
    ServerSocketChannel serverSockCh;
    Selector selector;
    
    ByteBuffer bufferMem;
    
    ManageDB dataBase;
    
    boolean running = false;
    
    
    public Server2() {
        dataBase = new ManageDB();
    }
    public Server2(int port) {
        dataBase = new ManageDB();
        serverSockAddr = new java.net.InetSocketAddress(port);
        //this.port = port;
    }
    public Server2(String host, int port) {
        dataBase = new ManageDB();
        
        serverSockAddr = new java.net.InetSocketAddress(host, port);
        //this.host = host ;
        //this.port = port;
    }
    
//Associa host e porta al ServerSocketChannel
    public void bindServer() {
        try {
            serverSockCh = ServerSocketChannel.open();
            serverSockCh.configureBlocking(false);
            serverSockCh.socket().bind(serverSockAddr);
            System.out.println("Server Socket Channel aperto su " + serverSockAddr.getHostString() + " on port: " + serverSockAddr.getPort());
        } catch (IOException ex) {System.out.println("Errore nell'apertura del Server Socket Channel: " + ex);}
    }

//Inizia a smistare le richieste dei client
    public void startFetch() throws IOException {
        selector = Selector.open();        //provider().openSelector();  //Selector.open();
        serverSockCh.register(selector, SelectionKey.OP_ACCEPT);
        
        running = true;
        while(running) {
            selector.select();
            
            Set keys = selector.selectedKeys();
            Iterator iterator = keys.iterator();
            
            while(iterator.hasNext()) {
                SelectionKey key = (SelectionKey) iterator.next();
                iterator.remove();
                
                    if(!key.isValid()) {
                        continue;
                    }
                
                    if(key.isAcceptable()) {
                        accept(key);
                        //continue;
                    }

                    if(key.isReadable()) {
                        SocketChannel newClient = (SocketChannel) key.channel();
                        
                        InetSocketAddress socketAddress= (InetSocketAddress) newClient.getRemoteAddress();
                        InetAddress inetAddress = socketAddress.getAddress();
                        
                        System.out.print(inetAddress.getHostAddress() + "> ");
                        
                        bufferMem = ByteBuffer.allocate(1024);

                        try {
                           int numRead = newClient.read(bufferMem);
                           
                           if (numRead == -1) {
                                Socket socket = newClient.socket();
                                SocketAddress remoteAddr = socket.getRemoteSocketAddress();
                                System.out.println("Connection closed by client: " + remoteAddr);
                                newClient.close();
                                key.cancel();
                                return;
                            }
                        }
                        catch (Exception e) {
                            System.out.println("Client non più attivo: " + e);
                            key.channel().close();
                            continue;
                        }

                        bufferMem.flip();
                        Charset charset=Charset.forName("ISO-8859-1");
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer charBuffer = decoder.decode(bufferMem);

                        System.out.println(charBuffer.toString());
                        continue;
                    }
            }
        }
    }
    
    public void stopFetch() {
        running = false;
    }
    
    public void stopServer() {
        try {
            serverSockCh.close();
        } catch (IOException ex) {
            System.out.println("Errore nella chiusura del ServerSocketChannel: " + ex);
        }
    }

    
    public void accept(SelectionKey key) {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
            SocketChannel client = serverSocketChannel.accept();
                // Accetto la nuova connessione, la configuro in noBlocking e registro il canale
                //  come OP_READ
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
            
            System.out.println("Nuovo client connesso: " + client.getRemoteAddress());
        } catch (IOException ex) {
            System.err.println("Errore durante la connessione con l'host: " + ex);
        }
    }
}


