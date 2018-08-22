package juhlamokka;

/**
 * The main class of program
 */
public class JuhlaMokka {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        DatabaseManager db = new DatabaseManager(
            "juhlamokkacomputing.postgres.database.azure.com",
            5432,
            "juhlamokka@juhlamokkacomputing",
            "computing4523!",
            "juhlamokka"
        );
        
        //CLIFrontend fe = new CLIFrontend();
        //fe.start();
    }
}
