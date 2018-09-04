package juhlamokka;

import java.net.InetAddress;
import java.util.ArrayList;
import juhlamokka.database.DBManager;
import juhlamokka.database.DBOperation;
import juhlamokka.database.Login;
import juhlamokka.database.User;

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
        ArrayList<User> users = dbo.getUsers("1=1", -1);
        
        for (User user : users) {
            System.out.println(user.getName());
        }
       /* for (Transaction transaction : transactions) {
        	transaction
			.getUser().setLocked(true);
        	transaction.getUser().update();
		}
        */
        
        /* Some initial password hashing code
        // Toni: tervehdys
        // Aleksi: jaahas
        // Heikki: blackhat
        
        for (User user : users) {
        	String pw = user.getpw(); // This was some legacy hacking
                                          // Do not long for it
                String hash = defuse.passwordhashing
                                    .PasswordStorage.createHash(pw);
                
                user.setpw(hash); // This was legacy hacking too
                
                System.out.println(user.getName() + " " + pw + " " + hash);
                
                user.update();
        }
        */
        
        char[] s = "blackhat".toCharArray();
        Login l = dbo.getUserLoginByCredentials("Heikki", s, InetAddress.getLocalHost());

        //System.out.println(String.join(",", ObjectManager.TRANSACTIONS.keySet().stream().map(e -> String.valueOf(e)).collect(Collectors.toSet())));
        
        //CLIFrontend fe = new CLIFrontend();
        //fe.start();
    }
}
