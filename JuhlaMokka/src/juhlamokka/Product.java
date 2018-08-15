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
public class Product extends DBObject {
    
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
