package juhlamokka;

import defuse.passwordhashing.PasswordStorage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A user object
 */
public class User extends DBObject {
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
            "password", "admin", "locked"
    ));
    
    /**
     * Get a product by its identifier
     * @param id
     * @param db
     */
    public User(Integer id, Connection db) {
        init();
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        read();
    }
    
    /**
     * Create a new product
     * @param name
     * @param description
     * @param password
     * @param admin
     * @param locked
     * @param db
     */
    public User(String name, String description, String password, Boolean admin,
            Boolean locked, Connection db) {
        init();
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
        this.password = password;
        this.admin = admin;
        this.locked = locked;
        
        // Add the object to database
        create();
    }
    
    /**
     * Instantiate a user with known info
     * @param id
     * @param name
     * @param description
     * @param password
     * @param admin
     * @param locked
     * @param db
     */
    public User (Integer id, String name, String description, String password, 
            Boolean admin, Boolean locked, Connection db) {
        init();
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.password = password;
        this.admin = admin;
        this.locked = locked;
        this.db = db;
    }
    
    /**
     * Do some common initialization for object
     */
    private void init() {
        this.tableName = "users";
        
        addFields(this.ownFields);
    }
    
    /**
     * Password hash of the users password, created with a library
     */
    private String password;
    
    /**
     * The true answer of a privilege question: is the account locked or not?
     */
    private Boolean locked;
    
    /**
     * Determines if user is an administrator, i.e. can edit other users
     */
    private Boolean admin;
    
    /**
     * Securely check and verify a password against the password hash.
     * Returns true if those match
     * @param pwd
     * @return
     * @throws IllegalArgumentException
     * @throws PasswordStorage.CannotPerformOperationException
     * @throws PasswordStorage.InvalidHashException
     */
    public Boolean checkPassword(char[] pwd) 
            throws IllegalArgumentException, 
            PasswordStorage.CannotPerformOperationException, 
            PasswordStorage.InvalidHashException {
        if (pwd.length <= 0) {
            throw new IllegalArgumentException("A password must be specified!");
        }
        
        return PasswordStorage.verifyPassword(pwd, this.password);
    }
    
    /**
     * Set the password
     * May log an CannotPerformOperationException if system is unsafe for crypto
     * @param pwd
     * @throws IllegalArgumentException
     */
    public void setPassword(char[] pwd) throws IllegalArgumentException {
        if (pwd.length <= 0) {
            throw new IllegalArgumentException("A password must be specified!");
        }

        // Password checking guidelines
        // TODO: implement configuration variables and haveibeenpwned api
        
        try {
            // This may throw an exception
            this.password = PasswordStorage.createHash(pwd);
            
            // Add this after possible exception has been thrown
            this.changedFields.add("password");
            
        } catch (PasswordStorage.CannotPerformOperationException ex) {
            // This is run if something in the system is not secure for crypto
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, 
                    "Cannot save password! " + 
                    "Something is wrong within this system.", ex);
        }
    }
    
    /**
     * Check if user account is locked
     * @return
     */
    public Boolean isLocked() {
        return locked;
    }
    
    /**
     * Set user account locked status
     * @param status
     * @throws IllegalArgumentException
     */
    public void setLocked(Boolean status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("You must specify a " + 
                    "truth value!");
        }
        
        this.changedFields.add("locked");
        
        this.locked = status;
    }
    
    /**
     * Check if user is an admin
     * @return
     */
    public Boolean isAdmin() {
        return admin;
    }
    
    /**
     * Set user account administrator status
     * @param status
     * @throws IllegalArgumentException
     */
    public void setAdmin(Boolean status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("You must specify a " + 
                    "truth value!");
        }
        
        this.changedFields.add("admin");
        
        this.admin = status;
    }
}
