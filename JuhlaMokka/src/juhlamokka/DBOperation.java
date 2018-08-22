/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author s1800571
 */
public class DBOperation {
    
    public ArrayList getProducts() {
        return null;
    }
    
    public static String[] getKeysAndValues(ArrayList fields) {
        String keys = String.join(", ", fields);
        String values = "";
        
        // Get a string with as many question marks that there are fields
        if (fields.size() >= 1) {
            values = "?";
            
            for (int i = 1; i < fields.size(); i++) {
                values += ", ?";
            }
        }
        
        return new String[] {keys, values};
    }
    
    public static PreparedStatement prepareSQLMarkup(
            Object obj, PreparedStatement q, ArrayList fields) 
            throws NoSuchFieldException, IllegalAccessException, SQLException {
        
        for (int i = 0; i < fields.size(); i++) {
            String key = (String) fields.get(i);
            q.setObject(i, obj.getClass().getDeclaredField(key).get(obj));
        }
        
        return q;
    }
}