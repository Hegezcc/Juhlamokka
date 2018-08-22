/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author s1800571
 */
public class DBOperation {
    /**
     * Holds the Connection for database
     */
    private Connection db;

    public DBOperation(Connection db) {
        this.db = db;
    }
    
    public ArrayList<Product> getProducts() {
        ArrayList al = new ArrayList<Product>();
        
        /*
        TODO:
        ResultSet -> Products ArrayList
        */
        
        ResultSet results = doOperation(new ArrayList<String>(Arrays.asList("name", "description")), "products", "id > 0");
        
        if (results != null) {
            
        }
        
        return al;
    }
    
    /**
     * 
    */
    private ResultSet doOperation(ArrayList<String> fields, String tableName, String condition) {
        try {
            // Prepare SQL query
            // ex. SELECT fields FROM object.tableName WHERE condition
            PreparedStatement q = db.prepareStatement(
                    "select " +
                    String.join(", ", fields) +
                    " from " +
                    tableName +
                    " WHERE " +
                    condition
            );
            
            ResultSet rs = q.executeQuery();
            while(rs.next()) {
                System.out.println(rs.getString("name") + ", " + rs.getString("description"));
            }
            
            return rs;
        } catch(SQLException e) {
            Logger.getLogger(DBOperation.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return null;
    }
}