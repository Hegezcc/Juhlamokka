package juhlamokka;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * An abstract database object. Other objects are inherited of this
 */
public abstract class DBObject {
    
    /**
     * Holds the Connection for database
     */
    protected Connection db;
    
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> fields = new ArrayList<>(Arrays.asList(
            "name", "description"
    ));

    protected ArrayList<String> changedFields = new ArrayList<>();

    /**
     * Object id, used for database purposes (created by database)
     */
    protected Integer id;
    
    /**
     * A name for object, that can be used for example to search the object
     */
    protected String name;
    
    /**
     * A description text for that object
     */
    protected String description;

    /**
     * Timestamp of the very moment when that object was created in database
     * (is set by database)
     */
    protected Date addedOn;

    /**
     * Timestamp of the moment when this object was modified in database
     * (is set by database)
     */
    protected Date modifiedOn;
    
    /**
     * Get the database identifier of property
     * @return Integer
     */
    public Integer getId() {
        return id;
    }
    
    /**
     * Get the name of property
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Set the name of property
     * @param newName
     * @throws IllegalArgumentException
     */
    public void setName(String newName) throws IllegalArgumentException {
        // Change this to suit database configuration
        Integer maxLength = 255;
        
        // Handle the possible null value: user may want to clear the name
        if (newName == null) {
            this.name = null;
            return;
        }
        
        // We better throw out an exception to ensure database will not do that
        if (newName.length() > maxLength) {
            throw new IllegalArgumentException("New name is too long!");
        }
        
        // Add for updation
        changedFields.add("name");

        this.name = newName;
    }
    
    /**
     * Get the description of property
     * @return String
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Set the description of property
     * @param newDescription
     * @throws IllegalArgumentException
     */
    public void setDescription(String newDescription) 
            throws IllegalArgumentException {
        // Change this to suit database configuration
        Integer maxLength = 65535;
        
        // Handle the possible null value: user may want to clear
        // the description
        if (newDescription == null) {
            this.description = null;
            return;
        }
        
        // We better throw out an exception to ensure database will not do that
        if (newDescription.length() > maxLength) {
            throw new IllegalArgumentException("New description is too long!");
        }
        // Add for updation
        changedFields.add("description");
        
        this.description = newDescription;
    }
    
    /**
     * Get the time of addition of property
     * @return Date
     */
    public Date getAddedOn() {
        return addedOn;
    }
    
    /**
     * Get the time of modification of property
     * @return Date
     */
    public Date getModifiedOn() {
        return modifiedOn;
    }
    
    /**
     * Add new parameters to be searched in database for returning object
     * @param adds
     */
    protected void addFields(ArrayList<String> adds) {
        adds.forEach((String x) -> {
           if (!this.fields.contains(x)) {
               this.fields.add(x);
           }
        });
    }
    
    /**
     * Insert new object to database
     */
    protected void create() {
        
    }
    
    /**
     * Read the object from database and initialize its properties
     */
    protected void read() {
        
    }
    
    /**
     * Save changes of object to database
     */
    protected void update() {
        
    }
    
    /**
     * Delete the object from database
     */
    protected void delete() {
        
    }
}
