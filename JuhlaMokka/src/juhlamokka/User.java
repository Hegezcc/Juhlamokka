/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

/**
 *
 * @author s1800580
 */
public class User extends DBObject {
    /**
     * Password hash (SHA256) of the users password
     */
    private String password;
    
    /**
     * The true answer of a privilege question: is the account locked or not?
     */
    private Boolean locked;
}
