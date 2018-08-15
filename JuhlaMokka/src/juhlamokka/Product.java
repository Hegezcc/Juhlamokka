/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.math.BigDecimal;
import java.sql.Connection;

/**
 *
 * @author s1800580
 */
public class Product extends DBObject {
    
    /**
     * Get a product by its identifier
     * @param id
     * @param manager
     */
    public Product(Integer id, Connection db) {
        
    }
    
    /**
     * Create a new product
     * @param name
     * @param description
     * @param basePrice
     * @param amount
     * @param unit
     * @param manager
     */
    public Product(String name, String description, BigDecimal basePrice, 
            Integer amount, String unit, Connection db) {
        
    }
    
    /**
     * The base price of product, mustn't be undercut when negotiating
     * prices with clients
     */
    private BigDecimal basePrice;
    
    /**
     * The amount of product in inventory
     */
    private Integer amount;
    
    /**
     * The unit which is used to count the amount of product
     */
    private String unit;
}
