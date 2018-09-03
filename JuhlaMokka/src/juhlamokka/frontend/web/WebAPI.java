package juhlamokka.frontend.web;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import juhlamokka.database.DBOperation;
import juhlamokka.database.User;

/**
 * A RESTful API for this program
 */
@ApplicationPath("/api/v1")
public class WebAPI extends Application {
    /**
     * Can be used from controllers to gain access to db
     */
    public static Connection db;
    
    /**
     * Simple hashmap for authentication bearers: get a user with auth token
     */
    private static final LinkedHashMap<String, User> AUTHENTICATED = new LinkedHashMap<>();
    
    public static User getUserByToken(String authToken) 
            throws NoSuchElementException {
        if (AUTHENTICATED.containsKey(authToken)) {
            return AUTHENTICATED.get(authToken);
        } else {
            throw new NoSuchElementException("No access token found!");
        }
    }
    
    /**
     * Give username and password, get an auth token for user
     * @param username
     * @param password
     * @return
     */
    public static String authenticateUser(String username, char[] password) {
        DBOperation dbo = new DBOperation(db);
        
        //User user = dbo.getUserByCredentials(username, password);
        
        return "";
    }
}
