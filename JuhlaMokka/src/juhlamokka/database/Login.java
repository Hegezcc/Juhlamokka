package juhlamokka.database;

import java.net.InetAddress;
import java.sql.Connection;

/**
 *
 */
public class Login extends DBObject {
    /**
     * Get user login data
     * @param id
     * @param db
     */
    public Login(Integer id, Connection db) {
    }
    
    public Login(Integer id, InetAddress ipAddress, Connection db) {
        this.ipAddress = ipAddress;
    }
    
    private InetAddress ipAddress;
    
    private User user;
    
}
