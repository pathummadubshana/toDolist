package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    private static  dbConnection dbconnection;
    private Connection connection;


   private dbConnection(){
       try {
           Class.forName("com.mysql.jdbc.Driver");

           connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/todolist","root","12345");
       } catch (ClassNotFoundException | SQLException e) {
           throw new RuntimeException(e);
       }


   }
    public static dbConnection getInstance(){
       return (dbconnection == null)? dbconnection = new dbConnection():dbconnection;
    }
    public Connection getConnection(){
       return connection;

    }
}
