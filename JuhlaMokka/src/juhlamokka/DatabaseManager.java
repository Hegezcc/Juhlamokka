/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package juhlamokka;

/**
 *
 * @author s1800571
 */
public class DatabaseManager {
    private String host, username, password, database;
    private int port;
    
    DatabaseManager(String host, int port, String username, String password, String database) {
       this.host = host;
       this.port = port;
       this.username = username;
       this.password = password;
       this.database = database;
    }
    
    public synchronized int Test() {
        return 1;
    }
}
