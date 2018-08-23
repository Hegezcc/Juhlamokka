package juhlamokka;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import static juhlamokka.DBOperation.getKeyValuePairs;
import static juhlamokka.DBOperation.getKeysAndValues;
import static juhlamokka.DBOperation.prepareSQLMarkup;

/**
 * An abstract database object. Other objects are inherited of this
 */
public abstract class DBObject {
    /**
     * Holds the Connection for database
     */
    protected Connection db;
    
    /**
     * The table name for what will be used for saving object, set on init()
     */
    protected String tableName;
    
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> fields = new ArrayList<>(Arrays.asList(
            "name", "description"
    ));

    /**
     * The fields that have been touched after construction. Used for taking
     * care of updating object in database.
     */
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
        return this.addedOn;
    }
    
    /**
     * Get the time of modification of property
     * @return Date
     */
    public Date getModifiedOn() {
        return this.modifiedOn;
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
     * Hacky way to set properties of object with knowing the property name
     * @param object
     * @param fieldName
     * @param fieldValue
     * @return 
     */
    public static boolean set(
            Object object, String fieldName, Object fieldValue) {
        Class<?> clazz = object.getClass();
        while (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                field.set(object, fieldValue);
                return true;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (
                    SecurityException | 
                    IllegalAccessException | 
                    IllegalArgumentException e) {
                throw new IllegalStateException(e);
            }
        }
        return false;
    }
    
    /**
     * Insert new object to database
     */
    protected void create() {
        // Keys and values as strings (used for SQL clause creation)
        String[] kvs = getKeysAndValues(this.fields);
        String keys = kvs[0];
        String values = kvs[1];
        
        // Only log errors, don't crash the program
        try {
            // Prepare the statement
            PreparedStatement q = db.prepareStatement(
                    "insert into " + this.tableName + " (" + keys +
                    ") values (" + values + ")"
            );
            
            // Prepare the attributes
            q = prepareSQLMarkup(this, q, this.fields);
            
            // Inject SQL code to DB
            q.executeUpdate();
        } catch (SQLException ex) {
            // Handle exceptions
            Logger.getLogger(
                    DBObject.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Read the object from database and initialize its properties
     */
    protected void read() {
        // Keys as string (used for SQL clause creation)
        String keys = getKeysAndValues(this.fields)[0];
        
        // Only log errors, don't crash the program
        try {
            // Prepare the statement
            PreparedStatement q = db.prepareStatement(
                    "select " + keys + " from " + this.tableName + 
                    " where id = " + this.id + " limit 1"
            );
            
            // Prepare the attributes
            q = prepareSQLMarkup(this, q, this.fields);
            
            // Inject SQL code to DB and get some payload from there
            try (ResultSet rs = q.executeQuery()) {
                for (String field : this.fields) {
                    set(this, field, rs.getObject(field));
                }
            }
        } catch (SQLException ex) {
            // Handle exceptions
            Logger.getLogger(
                    DBObject.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Save changes of object to database
     */
    protected void update() {
        // Keys and values as strings (used for SQL clause creation)
        String pairs = getKeyValuePairs(this.changedFields);
        
        // Only log errors, don't crash the program
        try {
            // Prepare the statement
            PreparedStatement q = db.prepareStatement(
                    "update " + this.tableName + " set " + pairs +
                    " where id = " + this.id + " limit 1"
            );
            
            // Prepare the attributes
            q = prepareSQLMarkup(this, q, this.changedFields);
            
            // Inject SQL code to DB
            q.executeUpdate();
        } catch (SQLException ex) {
            // Handle exceptions
            Logger.getLogger(
                    DBObject.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Delete the object from database
     */
    protected void delete() {
        // Only log errors, don't crash the program
        try {
            // Prepare the statement
            PreparedStatement q = db.prepareStatement(
                    "delete from " + this.tableName + 
                    " where id = " + this.id + " limit 1"
            );
            
            // Prepare the attributes
            q = prepareSQLMarkup(this, q, this.changedFields);
            
            // Inject SQL code to DB
            q.executeUpdate();
        } catch (SQLException ex) {
            // Handle exceptions
            Logger.getLogger(
                    DBObject.class.getName()
            ).log(Level.SEVERE, null, ex);
        }
    }
}
