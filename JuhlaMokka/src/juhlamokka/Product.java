package juhlamokka;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A product object
 */
public class Product extends DBObject {
    
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
            "basePrice", "amount", "unit"
    ));
    
    /**
     * Get a product by its identifier
     * @param id
     * @param db
     */
    public Product(Integer id, Connection db) {
        addFields(this.ownFields);
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        read();
    }
    
    /**
     * Create a new product
     * @param name
     * @param description
     * @param basePrice
     * @param amount
     * @param unit
     * @param db
     */
    public Product(String name, String description, BigDecimal basePrice, 
            Integer amount, String unit, Connection db) {
        addFields(this.ownFields);
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
        this.amount = amount;
        this.unit = unit;
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
    
    /**
     * Return the base price of product
     * @return
     */
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    /**
     * Set the base price of product (must be non-negative)
     * @param newPrice
     * @throws IllegalArgumentException
     */
    public void setBasePrice(BigDecimal newPrice) 
            throws IllegalArgumentException {
        // This essentially means: basePrice < 0
        if (basePrice.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("You must specify a " +
                    "non-negative price!");
        }
        
        this.changedFields.add("basePrice");
        
        this.basePrice = newPrice;
    }
    
    /**
     * Get the amount of product in inventory
     * @return
     */
    public Integer getAmount() {
        return amount;
    }
    
    /**
     * Set the amount of product in inventory (can be also negative)
     * @param newAmount
     * @throws IllegalArgumentException
     */
    public void setAmount(Integer newAmount) throws IllegalArgumentException{
        if (newAmount == null) {
            throw new IllegalArgumentException("You must specify an amount");
        }
        
        this.changedFields.add("amount");
        
        this.amount = newAmount;
    }
    
    /**
     * Get the unit the product is specified in
     * @return
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * Set the unit the product is specified in
     * @param newUnit
     * @throws IllegalArgumentException
     */
    public void setUnit(String newUnit) throws IllegalArgumentException {
        if (newUnit == null || newUnit.length() <= 0) {
            throw new IllegalArgumentException("You must specify an unit");
        }
        
        this.changedFields.add("unit");
        
        this.unit = newUnit;
    }
}
