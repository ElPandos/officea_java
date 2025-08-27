/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_server;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laptop
 */
public class Database {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "username";
    static final String PASS = "password";

    Connection conn = null;
    Statement stmt = null;

    private void Init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
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

    private boolean Query(String Query) {
        try {
            System.out.println("Creating statement...");
            stmt = conn.createStatement();
            String sql;
            sql = "SELECT id, first, last, age FROM Employees";
            ResultSet rs = stmt.executeQuery(sql);
            return true;

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        }
        return false;
    }

    /*
     public static void main(String[] args) {

     //STEP 5: Extract data from result set
     while(rs.next()){
     //Retrieve by column name
     int id  = rs.getInt("id");
     int age = rs.getInt("age");
     String first = rs.getString("first");
     String last = rs.getString("last");

     //Display values
     System.out.print("ID: " + id);
     System.out.print(", Age: " + age);
     System.out.print(", First: " + first);
     System.out.println(", Last: " + last);
     }
     //STEP 6: Clean-up environment
     rs.close();
     stmt.close();
     conn.close();
     }catch(SQLException se){
     //Handle errors for JDBC
     se.printStackTrace();
     }catch(Exception e){
     //Handle errors for Class.forName
     e.printStackTrace();
     }finally{
     //finally block used to close resources
     try{
     if(stmt!=null)
     stmt.close();
     }catch(SQLException se2){
     }// nothing we can do
     try{
     if(conn!=null)
     conn.close();
     }catch(SQLException se){
     se.printStackTrace();
     }//end finally try
     }//end try
     System.out.println("Goodbye!");
     }//end main
     }//end FirstExample
     */
}
