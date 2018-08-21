package juhlamokka;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A client object
 */
public class Client extends DBObject {
    
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList());
    
    /**
     * Get a client by its identifier
     * @param id
     * @param db
     */
    public Client(Integer id, Connection db) {
        addFields(this.ownFields);
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        read();
    }
    
    /**
     * Create a new client
     * @param name
     * @param description
     * @param db
     */
    public Client(String name, String description, Connection db) {
        addFields(this.ownFields);
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
    }
}
