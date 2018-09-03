package juhlamokka.database;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 */
public class Login extends DBObject {
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
            "ipAddress", "isSuccessful", "user"
    ));
    
    /**
     * Foreign object info for smart database usage
     */
    protected ArrayList<String> foreignObjects = new ArrayList<>(Arrays.asList(
            "user"
    ));
    
    /**
     * Get user login data
     * @param id
     * @param db
     */
    public Login(Integer id, Connection db) {
        preInit();
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        super.read();
        
        postInit();
    }
    
    /**
     * 
     * @param ipAddress
     * @param isSuccessful
     * @param user
     * @param db
     */
    public Login(InetAddress ipAddress, Boolean isSuccessful,
            User user, Connection db) {
        preInit();
        
        // We are creating a new object, set properties
        this.ipAddress = ipAddress;
        this.isSuccessful = isSuccessful;
        this.user = user;
        
        // Add the object to database
        super.create();
        
        postInit();
        
        
    }
    
    private InetAddress ipAddress;
    
    private Boolean isSuccessful;
    
    private User user;

    private void preInit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void postInit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
