package juhlamokka.database;

import java.net.InetAddress;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.security.SecureRandom;

/**
 *
 */
public class Login extends DBObject {
    /**
     * Shows which fields of object are updateable
     */
	protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
		"ipAddress",
		"isSuccessful",
		"user",
		"authenticationKey"
	));
	
    /**
     * Get user login data
     * @param id
     * @param db
     */
    public Login(Integer id, Connection db) {
    	preInit();
    	
    	this.id = id;
    	this.db = db;
    	
    	super.read();
    	
    	postInit();
    }
    
    public Login(InetAddress ipAddress, User user, boolean isSuccessful, Connection db) throws IllegalArgumentException {
    	preInit();
    	
    	if (isSuccessful && user == null) {
    		throw(new IllegalArgumentException("Successfull login when user == null? Possible hacking attempt :)"));
    	}
    	
    	this.authenticationKey = createAuthenticationKey();
    	this.ipAddress = ipAddress;
    	this.user = user;
    	this.isSuccessful = isSuccessful;
    	    	
    	super.create();
    	
    	postInit();
    }
    
    public Login(Integer id, InetAddress ipAddress, User user, boolean isSuccessful, char[] authenticationKey, Connection db) {
    	preInit();
    	
    	if (isSuccessful && user == null) {
    		throw(new IllegalArgumentException("Successfull login when user == null? Possible hacking attempt :)"));
    	}
    	
    	this.authenticationKey = authenticationKey;
    	this.ipAddress = ipAddress;
    	this.user = user;
    	this.isSuccessful = isSuccessful;
    	
        postInit();
    }
    
    /**
     * Do some common initialization for object at the start of construction
     */
    private void preInit() {
        this.tableName = "logins";
        
        addFields(this.ownFields);
    }
    
    /**
     * Do some common initialization for object at the end of construction
     */
    private void postInit() {
        ObjectManager.LOGINS.put(this.id, this);
    }
    
	/**
	 * The authentication key for user, length must be 32 characters
	 */
	private char[] authenticationKey;
	
    private InetAddress ipAddress;
    
    private User user;
    
    private boolean isSuccessful;
    
    private char[] createAuthenticationKey() {
    	char[] authenticationKey = new char[32];
    	
    	byte[] ba = new byte[192];
    	new SecureRandom().nextBytes(ba);
    	
    	byte[] ba64 = Base64.getEncoder().encode(ba);
    	
    	for(int i = 0; i < 32; i++) {
    		authenticationKey[i] = (char)ba64[i];
    	}
    	
    	return authenticationKey;
    }
    
    /**
     * Get user involving login event, maybe null
     * @return user
     */
    public User getUser() {
    	return user;
    }
    
    /**
     * Get 32 character BASE64 random authentication key
     * @return authenticationKey
     */
    public char[] getAuthenticationKey() {
    	return authenticationKey;
    }
    
    /**
     * Get IP Address involving login event
     * @return ipAddress
     */
    public InetAddress getIPAddress() {
    	return ipAddress;
    }
}
