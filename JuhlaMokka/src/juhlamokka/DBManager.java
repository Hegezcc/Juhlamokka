package juhlamokka;

import java.sql.*;
import java.util.Properties;

/**
 * A database manager responsible for giving the connection to DB
 */
public class DBManager {
    /**
     * Holds the Connection for database
     */
    public Connection connection = null;
    
    private String host, username, password, database;
    private Integer port;
    
    DBManager(String host, int port, String username, String password, String database) throws ClassNotFoundException, SQLException {
       this.host = host;
       this.port = port;
       this.username = username;
       this.password = password;
       this.database = database;
       
       try
       {
           Class.forName("org.postgresql.Driver");
       }
       catch (ClassNotFoundException e)
       {
           throw new ClassNotFoundException("PostgreSQL JDBC driver NOT detected in library path.", e);
       }
       
       connect();
    }
    
    private void connect() throws SQLException {
        try {
            String url = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
            
            Properties properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            properties.setProperty("ssl", "true");
            
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new SQLException("Failed to create connection to database.", e);
        }
        
        System.out.println("Successfully created connection to database.");
    }
    
    /**
     * Return the instantiated connection
     * @return
     */
    public Connection getConnection() {
        return connection;
    }
}
