
package universitysystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Database {
    
     
     public Statement StartDatabase(String DatabaseName,String Username,String Password){
            try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+DatabaseName+"?autoReconnect=true&useSSL=false",Username,Password);
            Statement statement = connection.createStatement();
            return statement;
        } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("Error in Database class - ClassNotFoundException or SQLException");
            Logger.getLogger(UniversitySystem.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        return null;     
    }
}
