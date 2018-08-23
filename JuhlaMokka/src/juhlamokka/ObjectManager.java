package juhlamokka;

import java.util.LinkedHashMap;

/**
 * Save and retrieve objects from database without using db
 */
public class ObjectManager {

    /**
     * LinkedHashMap of all products seen in the program, keys by id
     */
    public static final LinkedHashMap<Integer, Product> PRODUCTS = new LinkedHashMap<>();

    /**
     * LinkedHashMap of all users seen in the program, keys by id
     */
    public static final LinkedHashMap<Integer, User> USERS = new LinkedHashMap<>();

    /**
     * LinkedHashMap of all clients seen in the program, keys by id
     */
    public static final LinkedHashMap<Integer, Client> CLIENTS = new LinkedHashMap<>();
    
    /**
     * LinkedHashMap of all transactions seen in the program, keys by id
     */
    public static final LinkedHashMap<Integer, Transaction> TRANSACTIONS = new LinkedHashMap<>();
    
    /**
     * Add or replace a product in the manager
     * @param p
     */
    public void addProduct(Product p) {
        if (!PRODUCTS.containsKey(p.id)) {
            PRODUCTS.put(p.id, p);
        } else if (!(PRODUCTS.get(p.id) == p)) {
            PRODUCTS.replace(p.id, p);
        }
    }
    
    /**
     * Add or replace a user in the manager
     * @param u
     */
    public void addUser(User u) {
        if (!USERS.containsKey(u.id)) {
            USERS.put(u.id, u);
        } else if (!(USERS.get(u.id) == u)) {
            USERS.replace(u.id, u);
        }
    }
    
    /**
     * Add or replace a client in the manager
     * @param c
     */
    public void addClient(Client c) {
        if (!CLIENTS.containsKey(c.id)) {
            CLIENTS.put(c.id, c);
        } else if (!(CLIENTS.get(c.id) == c)) {
            CLIENTS.replace(c.id, c);
        }
    }
    
    /**
     * Add or replace a transaction in the manager
     * @param t
     */
    public void addTransaction(Transaction t) {
        if (!TRANSACTIONS.containsKey(t.id)) {
            TRANSACTIONS.put(t.id, t);
        } else if (!(TRANSACTIONS.get(t.id) == t)) {
            TRANSACTIONS.replace(t.id, t);
        }
    }
}
