/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.sql.*;
import java.util.Properties;

/**
 *
 * @author s1800571
 */
public class DatabaseManager {
    public Connection connection = null;
    
    private String host, username, password, database;
    private int port;
    
    DatabaseManager(String host, int port, String username, String password, String database) throws ClassNotFoundException {
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
    }
    
    public void Connect() throws SQLException {
        try {
            String url = String.format("jdbc:postgresql://%s/%s", host, database);
            
            Properties properties = new Properties();
            properties.setProperty("user", username);
            properties.setProperty("password", password);
            properties.setProperty("ssl", "true");
            
            connection = DriverManager.getConnection(url, properties);
        } catch (SQLException e) {
            throw new SQLException("Failed to create connection to database.", e);
        }
        
        if (connection != null) {
            System.out.println("Successfully created connection to database.");
        }
    }
    
    public int Test() {
        return 1;
    }
}