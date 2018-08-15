/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.util.Date;

/**
 *
 * @author s1800580
 */
public abstract class DBObject {

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
            name = null;
            return;
        }
        
        // We better throw out an exception to ensure database will not do that
        if (newName.length() > maxLength) {
            throw new IllegalArgumentException();
        }
        
        name = newName;
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
            description = null;
            return;
        }
        
        // We better throw out an exception to ensure database will not do that
        if (newDescription.length() > maxLength) {
            throw new IllegalArgumentException();
        }
        
        description = newDescription;
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
     * The constructor of class
     * @param id
     */
    public DBObject(Integer id) {
        //DatabaseManager manager = new DatabaseManager().getInstance();
        
    }
}
