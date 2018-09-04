package juhlamokka.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Represents a transaction involving product, user and client objects
 */
public class Transaction extends DBObject {
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
            "product", "user", "client", "amount", "price"
    ));
    
    /**
     * Foreign object info for smart database usage
     */
    protected ArrayList<String> foreignObjects = new ArrayList<>(Arrays.asList(
            "product", "user", "client"
    ));
    
    /**
     * Get a product by its identifier
     * @param id
     * @param db
     */
    public Transaction(Integer id, Connection db) {
        preInit();
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        super.read();
        
        postInit();
    }
    
    /**
     * Create a new product
     * @param name
     * @param description
     * @param product
     * @param user
     * @param client
     * @param amount
     * @param price
     * @param db
     */
    public Transaction(String name, String description, Product product,
            User user, Client client, Integer amount, BigDecimal price, Connection db) {
        preInit();
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
        this.product = product;
        this.user = user;
        this.client = client;
        this.amount = amount;
        this.price = price;
        
        // Add the object to database
        super.create();
        
        postInit();
    }
    
    /**
     * Instantiate a transaction with known info
     * @param id
     * @param name
     * @param description
     * @param product
     * @param user
     * @param client
     * @param amount
     * @param price
     * @param db
     */
    public Transaction(Integer id, String name, String description, Product product,
            User user, Client client, Integer amount, BigDecimal price, Connection db) {
        preInit();
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.product = product;
        this.user = user;
        this.client = client;
        this.amount = amount;
        this.price = price;
        this.db = db;
        
        postInit();
    }
    
    /**
     * Do some common initialization for object at the start of construction
     */
    private void preInit() {
        this.tableName = "transactions";
        
        addFields(this.ownFields);
    }
    
    /**
     * Do some common initialization for object at the end of construction
     */
    private void postInit() {
        ObjectManager.TRANSACTIONS.put(this.id, this);
    }
    
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
    
    /**
     * Get the user who is making the deal in database
     * @return
     */
    public User getUser() {
        return this.user;
    }
    
    /**
     * Set the user who is making the deal in database
     * @param user 
     */
    public void setUser(User user) {
        this.changedFields.add("user");
        this.user = user;
    }
    
    /**
     * Get the client who is the deal being made with
     * @return
     */
    public Client getClient() {
        return this.client;
    }
    
    /**
     * Set the client who is the deal being made with
     * @param client 
     */
    public void setClient(Client client) {
        this.changedFields.add("client");
        this.client = client;
    }
    
    /**
     * Get the product in center of deal
     * @return 
     */
    public Product getProduct() {
        return this.product;
    }
    
    /**
     * Set the product in center of deal
     * @param product 
     */
    public void setProduct(Product product) {
        this.changedFields.add("product");
        this.product = product;
    }
    
    /**
     * Get the amount of product transaction handles with
     * @return 
     */
    public Integer getAmount() {
        return this.amount;
    }
    
    /**
     * Set the amount of product transaction handles with
     * @param amount 
     */
    public void setAmount(Integer amount) {
        this.changedFields.add("amount");
        this.amount = amount;
    }
    
    /**
     * Return the base price of product
     * @return
     */
    public BigDecimal getPrice() {
        return price;
    }
    
    /**
     * Set the base price of product (must be non-negative)
     * @param newPrice
     * @throws IllegalArgumentException
     */
    public void setPrice(BigDecimal newPrice) {
        this.changedFields.add("price");
        
        this.price = newPrice;
    }
}
