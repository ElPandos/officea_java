import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
        
public class Database {

    // JDBC driver name and database URL
    static final private String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    private String USER = "username";
    private String PASS = "password";

    Connection conn = null;
    Statement stmt = null;

     private void Setup(String sDatabaseUrl, String sUser, String sPassword) {
         DB_URL = sDatabaseUrl;
         USER = sUser;
         PASS = sPassword;      
     }
     
    private void Init() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean Connect() {
        try {

            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            return true;

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return false;
    }

    private void Disconnect() {
        try {
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
    }

    private boolean Query(String sQuery) {
        try {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sQuery);
            return true;

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return false;
    }
}
