package chat.scolastica.server;

import chat.scolastica.ConfigProp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

public class ManageDB {
    String dbDriver;            //"com.mysql.jdbc.Driver" = mysql; "org.sqlite.JDBC" = sqlite
    String dbName = "ChatScolastica";           
    String userDB = "serverChat";
    String passDB = "chatscolastica";                       // Da prenderli dal ConfigFile;
    String ip = "192.168.0.10";
    String port = "3306";
    String header;
    String dbURL;
    
    String typeDB;
    
    Connection conn;
    Statement statement;
    
    public ManageDB() {
        try {
            typeDB = ConfigProp.getIstance().getStringValue("TIPO_DATABASE_USATO");
        } catch (IOException ex) {
            System.err.println("Errore " + ex);
        }
        
        if (typeDB.equals("mysql")) {
            dbDriver = "com.mysql.jdbc.Driver";
            header = "jdbc:mysql:";
            dbURL = header + "//" + ip + ":" + port + "/" + dbName + "?user=" + userDB + "&password=" + passDB;
        } else {
            dbDriver = "org.sqlite.JDBC";
            header = "jdbc:sqlite:";
            dbURL = header + dbName + ".db";
        }
        
        try {
                //Carico i driver per MySql     ClassNotFoundException
            Class.forName(dbDriver);
                //Crea la connessione con il db ChatScolastica      SQLException
            Connection connect = DriverManager.getConnection(dbURL);
                //Creo l'struzione da mandare al db
            statement = connect.createStatement();
        } catch (ClassNotFoundException ex) {System.err.println("Errore nel caricamento dei driver: " + ex);this.closeConn();} 
        catch (SQLException ex) {System.err.println("Errore MySql: " + ex);this.closeConn();}
    }
    
    public void insertUser(String name, String pass, String mail) {
        try {
            //preparedStatement è il risultato del comando sql
            statement.execute(String.format("insert into Utenti(username, password, email) values('%s', '%s', '%s')", name, pass, mail));
            System.out.println("Inserimento dell'utente " + name + " eseguito!");
        } catch (SQLException ex) {System.err.println("Errore nell'esecuzione del comando MySql: " + ex);}
    }
    
    public final void closeConn() {
        try {
            if (statement != null) {statement.close();}
            if (conn != null) {conn.close();}
        } catch (Exception e) {System.err.println("Errore nella chiusura della connessione del database: " + e);}
    }
}
