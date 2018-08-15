/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.math.BigDecimal;

/**
 *
 * @author s1800580
 */
public class Transaction {
    /**
     * The user object representing the handler of transaction
     */
    private User user;
    
    /**
     * The client who is doing business with user
     */
    private Client client;
    
    /**
     * The product that is being in center of transaction
     */
    private Product product;
    
    /**
     * How much of that product is changing owner: positive to increment the
     * inventory amount, negative to decrement it
     */
    private Integer amount;
    
    /**
     * The final price that is agreed upon the deal
     */
    private BigDecimal price;
}
