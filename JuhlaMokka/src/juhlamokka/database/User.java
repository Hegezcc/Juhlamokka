package juhlamokka.database;

import defuse.passwordhashing.PasswordStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import juhlamokka.ConfigManager;

/**
 * A user object
 */
public class User extends DBObject {
    /**
     * Shows which fields of object are updateable
     */
    protected ArrayList<String> ownFields = new ArrayList<>(Arrays.asList(
            "password", "isAdmin", "isLocked"
    ));
    
    /**
     * Get a product by its identifier
     * @param id
     * @param db
     */
    public User(Integer id, Connection db) {
        preInit();
        
        this.db = db;
        this.id = id;
        
        // Read the rest of data from db
        super.read();
        
        postInit();
    }
    
    /**
     * Create a new product
     * @param name
     * @param description
     * @param password
     * @param admin
     * @param locked
     * @param db
     */
    public User(String name, String description, String password, Boolean admin,
            Boolean locked, Connection db) {
        preInit();
        
        // We are creating a new object, set properties
        this.db = db;
        this.name = name;
        this.description = description;
        this.password = password;
        this.isAdmin = admin;
        this.isLocked = locked;
        
        // Add the object to database
        super.create();
        
        postInit();
    }
    
    /**
     * Instantiate a user with known info
     * @param id
     * @param name
     * @param description
     * @param password
     * @param admin
     * @param locked
     * @param db
     */
    public User (Integer id, String name, String description, String password, 
            Boolean admin, Boolean locked, Connection db) {
        preInit();
        
        this.id = id;
        this.name = name;
        this.description = description;
        this.password = password;
        this.isAdmin = admin;
        this.isLocked = locked;
        this.db = db;
        
        postInit();
    }
    
    /**
     * Do some common initialization for object at the start of construction
     */
    private void preInit() {
        this.tableName = "users";
        
        addFields(this.ownFields);
    }
    
    /**
     * Do some common initialization for object at the end of construction
     */
    private void postInit() {
        ObjectManager.USERS.put(this.id, this);
    }
    
    /**
     * Password hash of the users password, created with a library
     */
    protected String password;
    
    /**
     * The true answer of a privilege question: is the account locked or not?
     */
    protected Boolean isLocked;
    
    /**
     * Determines if user is an administrator, i.e. can edit other users
     */
    protected Boolean isAdmin;
    
    /**
     * Securely check and verify a password against the password hash.
     * Returns true if those match
     * @param pwd
     * @return
     * @throws IllegalArgumentException
     * @throws PasswordStorage.CannotPerformOperationException
     * @throws PasswordStorage.InvalidHashException
     */
    public Boolean checkPassword(char[] pwd) 
            throws IllegalArgumentException, 
            PasswordStorage.CannotPerformOperationException, 
            PasswordStorage.InvalidHashException {
        if (pwd.length <= 0) {
            throw new IllegalArgumentException("A password must be specified!");
        }
        
        return PasswordStorage.verifyPassword(pwd, this.password);
    }
    
    /**
     * Set the password
     * May log an CannotPerformOperationException if system is unsafe for crypto
     * @param pwd
     * @throws IllegalArgumentException
     */
    public void setPassword(char[] pwd) throws IllegalArgumentException {
        Integer minimumPasswordLength = Integer.parseInt(
                ConfigManager.DATA.getProperty("password-minimum-length", "10")
        );
        
        if (pwd.length <= minimumPasswordLength) {
            throw new IllegalArgumentException(
                    "A password with at least %d characters must be specified!"
            );
        } else if (
                "true".equals(ConfigManager.DATA.getProperty("check-password-pwnage")) && 
                checkPasswordPwnage(pwd)) {
            throw new IllegalArgumentException(
                    "Your password has appeared in a data breach before! " +
                    "Please give a new password. " +
                    "Check https://haveibeenpwned.com/ for more details."
            );
        }

        // Password checking guidelines
        // TODO: implement configuration variables and haveibeenpwned api
        
        try {
            // This may throw an exception
            this.password = PasswordStorage.createHash(pwd);
            
            // Add this after possible exception has been thrown
            this.changedFields.add("password");
            
        } catch (PasswordStorage.CannotPerformOperationException ex) {
            // This is run if something in the system is not secure for crypto
            String msg = "Cannot save password! " + 
                         "Something is wrong within this system.";
            
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, msg, ex);
        }
    }
    
    /**
     * Check if password has been seen in a data breach using the
     * haveibeenpwned api
     * @param password
     * @return
     */
    public static Boolean checkPasswordPwnage(char[] password) {
        Boolean pwned = false;
        
        HttpURLConnection c = null;
        
        try {
            String hash = sha1Hex(password);
            String hashStart = hash.substring(0, 5);
            String hashEnd = hash.substring(5);
            
            URL url = new URL(String.format(
                    ConfigManager.DATA.getProperty("pwnage-check-api"),
                    hashStart
            ));
            c = (HttpURLConnection) url.openConnection();
            
            c.setRequestProperty("User-Agent", 
                    String.format(
                            "%s/%s",
                            ConfigManager.DATA.getProperty("program-name"),
                            ConfigManager.DATA.getProperty("version")
                    )
            );
            
            c.setRequestMethod("GET");
            c.setRequestProperty("Accept", "application/json");

            if (c.getResponseCode() != 200)
            {
                throw new RuntimeException("Failed : HTTP error code : "
                        + c.getResponseCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            c.getInputStream()
                    )
            );

            String line;
            
            while (true) {
                line = br.readLine();
                if (line == null) {
                    break;
                } else if (line.equals(hashEnd)) {
                    pwned = true;
                    break;
                }
            }
        } catch (IOException | RuntimeException | NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        
        return pwned;
    }
    
    /**
     * Get a sha1 hex hash from char array
     * @param password
     * @return
     * @throws NoSuchAlgorithmException 
     */
    private static String sha1Hex(char[] password) throws NoSuchAlgorithmException {
        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
        crypt.reset();
        crypt.update(charToBytes(password));
        return byteToHex(crypt.digest());
    }

    /**
     * Get hex string from byte[]
     * @param hash
     * @return 
     */
    private static String byteToHex(final byte[] hash) {
        String result;
        try (Formatter formatter = new Formatter()) {
            for (byte b : hash) {
                formatter.format("%02x", b);
            }   result = formatter.toString();
        }
        return result;
    }
    
    /**
     * Get byte[] from char[]
     * @param chars
     * @return 
     */
    private static byte[] charToBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
                byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }
    
    /**
     * Check if user account is locked
     * @return
     */
    public Boolean isLocked() {
        return isLocked;
    }
    
    /**
     * Set user account locked status
     * @param status
     * @throws IllegalArgumentException
     */
    public void setLocked(Boolean status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("You must specify a " + 
                    "truth value!");
        }
        
        this.changedFields.add("isLocked");
        
        this.isLocked = status;
    }
    
    /**
     * Check if user is an admin
     * @return
     */
    public Boolean isAdmin() {
        return isAdmin;
    }
    
    /**
     * Set user account administrator status
     * @param status
     * @throws IllegalArgumentException
     */
    public void setAdmin(Boolean status) throws IllegalArgumentException {
        if (status == null) {
            throw new IllegalArgumentException("You must specify a " + 
                    "truth value!");
        }
        
        this.changedFields.add("isAdmin");
        
        this.isAdmin = status;
    }
}
