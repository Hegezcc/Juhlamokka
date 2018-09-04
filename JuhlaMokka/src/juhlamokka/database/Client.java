package juhlamokka.database;

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
        preInit();
        
        
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        super.read();
        
        postInit();
    }
    
    /**
     * Create a new client
     * @param name
     * @param description
     * @param db
     */
    public Client(String name, String description, Connection db) {
        preInit();
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
        
        // Add the object to database
        super.create();
        
        postInit();
    }
    
    /**
     * Instantiate a client with known info
     * @param id
     * @param name
     * @param description
     * @param db
     */
    public Client(Integer id, String name, String description, Connection db) {
        preInit();
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.db = db;
        
        postInit();
    }
    
    /**
     * Do some common initialization for object at the start of construction
     */
    private void preInit() {
        this.tableName = "clients";
        
        addFields(this.ownFields);
    }
    
    /**
     * Do some common initialization for object at the end of construction
     */
    private void postInit() {
        ObjectManager.CLIENTS.put(this.id, this);
    }
}
