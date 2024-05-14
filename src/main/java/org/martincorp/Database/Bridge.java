package org.martincorp.Database;

import java.sql.*;

import org.martincorp.Interface.GUI;

public class Bridge {
    //Variables:
    private static String DBName;
    public static boolean connected;
    public static Connection conn;
    public static Statement querier;

      //Query variables:
    private static final String COMP = "CREATE TABLE company (com_id INTEGER PRIMARY KEY AUTO_INCREMENT, com_name VARCHAR(60) NOT NULL, com_pass BLOB NOT NULL, com_creation_date TIMESTAMP)";
    private static final String USER = "CREATE TABLE `user`(user_id INTEGER PRIMARY KEY AUTO_INCREMENT, user_alias VARCHAR(50) NOT NULL, user_comp INTEGER NOT NULL DEFAULT 1, user_pass BLOB NOT NULL, CONSTRAINT userCompany FOREIGN KEY (user_comp) REFERENCES company(com_id) ON UPDATE CASCADE ON DELETE CASCADE)";
    private static final String SAMPLE = "INSERT INTO company VALUES (1, 'MartinCORP', 'hola', '2023-04-15 12:40:00')";
    
    //Constructor:
    public Bridge(String db){
        DBName = db;
    }

    public Bridge(){
        
    }
    
    //Métodos:
    public void startConnection(){
        try{
            //TODO: pick up which DB to connect from settings file (aka add settings).
            //TODO: change corporativista password to 'cAntANlosQUEyanoESTAn' in all bridges that use this user.
            Class.forName("com.mysql.cj.jdbc.Driver");
            //conn = DriverManager.getConnection
              //  ("jdbc:mysql://neutronmailservice.duckdns.org:3306/" + DBName, "corporativista", "");
            //this.conn = DriverManager.getConnection
              //    ("jdbc:mysql://192.168.1.34/" + this.DBNAME, "corporativista", "");
            conn = DriverManager.getConnection
                  ("jdbc:mysql://localhost/", "corporativista", "");
            querier = conn.createStatement();
            querier.execute("USE companies");
            ResultSet result = querier.executeQuery("SELECT * FROM " + DBName + ".company");
            
            if(!result.next()){
                resetDB();
            }
            else{
                System.out.println("'"+ DBName + "' database successfully connected.");
            }
            
            connected = true;
            result.close();
        }
        catch (ClassNotFoundException e) {
            System.out.println("ERROR - "+e.getMessage());

            connected = false;
        }
        catch (SQLException e) {
            System.out.println("ERROR - "+e.getMessage());
            
            connected = false;
            if(querier != null){
                resetDB();
            }
        }
    }
    
    public void closeConnection(){
        try{
            querier.close();
            conn.close();
            connected = false;
        }
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
    }

    public void checkConnection(){
        try{
            if(!conn.isValid(3)){
                GUI.retryDatabase("");
            }
        }
        catch(SQLException sqle){
            //This catch is here for literally nothing, is only thrown if timeout is less than 0.
            sqle.printStackTrace();
        }
    }
    
    public void resetDB(){
        System.out.println("Creating of new database " + DBName + " for user 'corporativista'.");
        
        try{
            querier.executeUpdate(COMP);
            querier.executeUpdate(USER);
            querier.executeUpdate(SAMPLE);
        }
        catch (SQLException e) {
            GUI.launchMessage(2, "Error de base de datos", "Ha ocurrido un error ");
            e.printStackTrace();
        }
        System.out.println("New database " + DBName + " created.");
    }

    public String getDB(){
        return DBName;
    }

}