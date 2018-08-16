/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import defuse.passwordhashing.PasswordStorage;

/**
 *
 * @author s1800580
 */
public class User extends DBObject {
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
    
    public void setPassword(char[] pwd) throws IllegalArgumentException {
        
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
