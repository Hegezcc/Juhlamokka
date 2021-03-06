package juhlamokka.database;

import defuse.passwordhashing.PasswordStorage;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;

/**
 * Do extensive database operations
 */
public class DBOperation {
    /**
     * Holds the Connection for database
     */
    private final Connection db;

    /**
     * Construct an interface for extensive database operations
     * @param db
     */
    public DBOperation(Connection db) {
        this.db = db;
    }
    
    /**
     * Mass getter for products. It must be observed that "condition" won't
     * get attack surface for possible SQL injection.
     * @param condition
     * @param limit
     * @return
     */
    public ArrayList<Product> getProducts(String condition, Integer limit) {
        ArrayList<Product> results = new ArrayList<>();
        
        try {
            CachedRowSet crs = read(new ArrayList<>(
                    Arrays.asList("id", "name", "description", "basePrice", 
                                  "amount", "unit")),
                    "products",
                    condition,
                    limit
            );
            
            if (crs != null) {
               while (crs.next()) {
                    results.add(new Product(
                            crs.getInt("id"), 
                            crs.getString("name"), 
                            crs.getString("description"), 
                            crs.getBigDecimal("basePrice"), 
                            crs.getInt("amount"), 
                            crs.getString("unit"), 
                            this.db
                    ));         
                }
                
                crs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * Mass getter for users. It must be observed that "condition" won't
     * get attack surface for possible SQL injection.
     * @param condition
     * @param limit
     * @return
     */
    public ArrayList<User> getUsers(String condition, Integer limit) {
        ArrayList<User> results = new ArrayList<>();
        
        try {
            CachedRowSet rs = read(new ArrayList<>(
                    Arrays.asList("id", "name", "description", "password", 
                                  "isAdmin", "isLocked")),
                    "users", 
                    condition,
                    limit
            );
            
            if (rs != null) {
                while (rs.next()) {
                    results.add(new User(
                            rs.getInt("id"), 
                            rs.getString("name"), 
                            rs.getString("description"), 
                            rs.getString("password"),
                            rs.getBoolean("isAdmin"),
                            rs.getBoolean("isLocked"),
                            this.db
                    ));
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * Mass getter for clients. It must be observed that "condition" won't
     * get attack surface for possible SQL injection.
     * @param condition
     * @param limit
     * @return
     */
    public ArrayList<Client> getClients(String condition, Integer limit) {
        ArrayList<Client> results = new ArrayList<>();
        
        try {
        	CachedRowSet rs = read(new ArrayList<>(
                    Arrays.asList("id", "name", "description")),
                    "clients", 
                    condition,
                    limit
            );
            
            if (rs != null) {
                while (rs.next()) {
                    results.add(new Client(
                            rs.getInt("id"), 
                            rs.getString("name"), 
                            rs.getString("description"), 
                            this.db
                    ));
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * Mass getter for transactions. It must be observed that "condition" won't
     * get attack surface for possible SQL injection.
     * @param condition
     * @param limit
     * @return
     */
    public ArrayList<Transaction> getTransactions(
            String condition, Integer limit) {
        ArrayList<Transaction> results = new ArrayList<>();
        
        try {
        	CachedRowSet rs = read(new ArrayList<>(
                    Arrays.asList("id", "name", "description", "productid",
                            "userid", "clientid", "amount", "price")),
                    "transactions", 
                    condition,
                    limit
            );
            
            if (rs != null) {
                // First round: ensure we have all the objects
                // to construct the transactions
                ArrayList<String> missingProductIds = new ArrayList<>();
                ArrayList<String> missingUserIds = new ArrayList<>();
                ArrayList<String> missingClientIds = new ArrayList<>();
                
                while (rs.next()) {
                    Integer productId = rs.getInt("productid");
                    Integer userId = rs.getInt("userid");
                    Integer clientId = rs.getInt("clientid");
                    
                    if (!(ObjectManager.PRODUCTS.containsKey(productId))) {
                        missingProductIds.add(productId.toString());
                    }
                    
                    if (!(ObjectManager.USERS.containsKey(userId))) {
                        missingUserIds.add(userId.toString());
                    }
                    
                    if (!(ObjectManager.CLIENTS.containsKey(clientId))) {
                        missingClientIds.add(clientId.toString());
                    }
                }
                
                // Get missing objects
                if (missingProductIds.size() > 0) {
                    // E.g. "id in(1, 2, 5, 8)"
                    String where = "id in(" + 
                                    String.join(", ", missingProductIds) + 
                                    ")";
                    
                    // Just run the retrieval code, it invokes ObjectManager
                    getProducts(where, missingProductIds.size());
                }
                
                if (missingUserIds.size() > 0) {
                    String where = "id in(" + 
                                    String.join(", ", missingUserIds) + 
                                    ")";
                    
                    getUsers(where, missingUserIds.size());
                }
                
                if (missingClientIds.size() > 0) {
                    String where = "id in(" + 
                                    String.join(", ", missingClientIds) + 
                                    ")";
                    
                    getClients(where, missingClientIds.size());
                }
                
                // Refresh ResultSet back to start
                rs.beforeFirst();
                
                // Second round: actually create the objects
                while (rs.next()) {
                    results.add(new Transaction(
                            rs.getInt("id"), 
                            rs.getString("name"), 
                            rs.getString("description"), 
                            ObjectManager.PRODUCTS.get(rs.getInt("productid")),
                            ObjectManager.USERS.get(rs.getInt("userid")),
                            ObjectManager.CLIENTS.get(rs.getInt("clientid")),
                            rs.getInt("amount"),
                            rs.getBigDecimal("price"),
                            this.db
                    ));
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        return results;
    }
    
    /**
     * Get a user by its username and password
     * @param username
     * @param password
     * @param ipAddress
     * @return
     */
    public Login getUserLoginByCredentials(String username, char[] password, InetAddress ipAddress) {
        User user = null;
        try {
            ResultSet rs = read(new ArrayList<>(
                    Arrays.asList("id", "name", "description", "password", 
                                  "isadmin", "islocked")),
                    "users",
                    String.format("name = %s", username),
                    1
            );
            
            if (rs != null) {
                while (rs.next()) {
                    user = new User(
                            rs.getInt("id"), 
                            rs.getString("name"), 
                            rs.getString("description"), 
                            rs.getString("password"), 
                            rs.getBoolean("isadmin"), 
                            rs.getBoolean("islocked"), 
                            this.db
                    );
                }
                
                rs.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        
        try {
            if (user == null || !user.checkPassword(password)) {
            	new Login(ipAddress, user, false, db);
            	
                throw new IllegalArgumentException(
                		String.format("User %s not found", username)
                );
            }
        } catch (IllegalArgumentException | 
                PasswordStorage.CannotPerformOperationException | 
                PasswordStorage.InvalidHashException ex) {
            Logger.getLogger(DBOperation.class.getName())
                    .log(Level.SEVERE, null, ex);
            
            throw new NoSuchElementException("User not found");
        }
        
        Login login = new Login(ipAddress, user, true, db);
        
		return login;
    }
    
    /**
     * Mass-get things from database. It must be observed that "condition" won't
     * get attack surface for possible SQL injection.
     */
    private CachedRowSet read(ArrayList<String> fields, String tableName, 
            String condition, Integer limit) {
        
        // Declare the q and rs here to allow proper .close() 
        // in case of exception
        PreparedStatement q = null;
        ResultSet rs = null;
        
        try {
            // Prepare SQL query
            // ex. SELECT fields FROM object.tableName WHERE condition
            String queryText = String.format("select %s from %s", String.join(", ", fields), tableName);
            
            // Use where clause only if it is specified
            if (condition == null || "".equals(condition)) {
                queryText += " where " + condition;
            }
            
            // Limit only if limit is positive
            if (limit > 0) {
                queryText += " limit " + limit.toString();
            }
            
            q = db.prepareStatement(queryText);
            rs = q.executeQuery();
            
            // DEBUG DATA! TODO: REMOVE FOR PRODUCTION IF IT WORKS
            /*while(rs.next()) {
                System.out.println(rs.getString("name") + ", " + rs.getString("description"));
            }*/
            
            CachedRowSet crs = RowSetProvider.newFactory().createCachedRowSet();
            crs.populate(rs);
            rs.close();
            
            // Debug crs
            /*while(crs.next()) {
                System.out.println(crs.getString("name") + ", " + crs.getString("description"));
            }*/
            
            return crs;
        } catch(SQLException e) {
            Logger.getLogger(
                    DBOperation.class.getName())
                    .log(Level.SEVERE, null, e);
        } finally {
            // Ensure we will close the rs in case of exception and have proper
            // logging in case there happens an exception while closing
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(
                        DBOperation.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
            try {
                if (q != null && !q.isClosed()) {
                    q.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(
                        DBOperation.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
        
        return null;
    }
    
    /**
     * Create keys and values for use in SQL syntax, e.g. insert
     * @param fields
     * @return
     */
    public static String[] getKeysAndValues(ArrayList<String> fields) {
        String keys = String.join(", ", fields);
        String values = "";
        
        // Get a string with as many question marks that there are fields
        if (fields.size() >= 1) {
            StringBuilder b = new StringBuilder("?");
            
            for (int i = 1; i < fields.size(); i++) {
                b.append(", ?");
            }
            
            values = b.toString();
        }
        
        return new String[] {keys, values};
    }
    
    /**
     * Create effective SQL fields from object properties for use in SQL code
     * @param fields
     * @param foreignObjects
     * @return
     */
    public static ArrayList<String> getSQLFields(ArrayList<String> fields, 
            ArrayList<String> foreignObjects) {
        ArrayList<String> sqlFields = new ArrayList<>();
        
        for (String field : fields) {
            if (foreignObjects.contains(field)) {
                sqlFields.add(field + "id");
            } else {
                sqlFields.add(field);
            }
        }
        
        return sqlFields;
    }
    
    /**
     * Create key-value pairs for SQL select syntax, e.g. key1 = ?, key2 = ?
     * @param fields
     * @return
     */
    public static String getKeyValuePairs(ArrayList<String> fields) {
        ArrayList<String> pairs = new ArrayList();
        
        fields.forEach((field) -> {
            pairs.add(field + " = ?");
        });
        
        return String.join(", ", pairs);
    }
    
    /**
     * Prepares given statement with fields from obj
     * @param obj
     * @param q
     * @param keyFields
     * @param sqlFields
     * @return
     * @throws SQLException
     */
    public static PreparedStatement prepareSQLMarkup(
            DBObject obj, PreparedStatement q, ArrayList<String> keyFields, 
            ArrayList<String> sqlFields) throws SQLException {
        
        for (int i = 0; i < keyFields.size(); i++) {
            String key = keyFields.get(i);
            
            // Test if we had this value on our object: if not, set it null
            Object value;
            try {
                value = obj.getClass().getDeclaredField(key).get(obj);
                
                // We may have an actual foreign object here, so prepare by id
                if (sqlFields.contains(key + "id")) {
                    value = ((DBObject) value).getId();
                }
            } catch (
                    NoSuchFieldException | 
                    SecurityException | 
                    IllegalArgumentException | 
                    IllegalAccessException ex) {
                Logger.getLogger(
                        DBOperation.class.getName())
                        .log(Level.WARNING, null, ex);

                value = null;
            }
            
            // JDBC indexes start at 1
            q.setObject(i + 1, value);
        }
        
        return q;
    }
}