/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

import juhlamokka.DatabaseManager;


/**
 *
 * @author s1800571
 */
public class JuhlaMokka {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        System.out.println("Hello World");
        
        DatabaseManager db = new DatabaseManager(
                "juhlamokkacomputing.postgres.database.azure.com",
                5432,
                "juhlamokka@juhlamokkacomputing",
                "computing4523!",
                "juhlamokka"
        );
    }
}
