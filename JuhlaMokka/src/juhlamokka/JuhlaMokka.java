package juhlamokka;

import java.util.ArrayList;

import juhlamokka.database.DBManager;
import juhlamokka.database.DBOperation;
import juhlamokka.database.Product;
import juhlamokka.database.Transaction;

/**
 * The main class of program
 */
public class JuhlaMokka {
    /**
     * Main class and entry point for the program run flow
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // Read the configuration statically, it's now stored
        // in ConfigManager.DATA
        ConfigManager.readDefaultConfig();
        
        // TODO: move to config and remove for production
        // These are now publicly at Github, hi for everyone connecting 
        // to our database
        ConfigManager.DATA.setProperty("db.hostname", "juhlamokkacomputing.postgres.database.azure.com");
        ConfigManager.DATA.setProperty("db.port", "5432");
        ConfigManager.DATA.setProperty("db.username", "juhlamokka@juhlamokkacomputing");
        ConfigManager.DATA.setProperty("db.password", "computing4523!");
        ConfigManager.DATA.setProperty("db.database", "juhlamokka");
        
        DBManager db = new DBManager(
                ConfigManager.DATA.getProperty("db.hostname"),
                Integer.parseInt(ConfigManager.DATA.getProperty("db.port")),
                ConfigManager.DATA.getProperty("db.username"),
                ConfigManager.DATA.getProperty("db.password"),
                ConfigManager.DATA.getProperty("db.database")
        );
        
        DBOperation dbo = new DBOperation(db.getConnection());
        ArrayList<Transaction> transactions = dbo.getTransactions("id > 0", -1);
        for (Transaction transaction : transactions) {
			System.out.println("Mahtavaa!: " + transaction.getProduct());
		}
        
        //CLIFrontend fe = new CLIFrontend();
        //fe.start();
    }
}
