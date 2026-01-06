package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//these are java's build-in JDBC API (dont talk to postgres themselves, .jar does that)

public class DatabasePersistenceTest {
    public static void main(String[] args) {
        //database connection details: 
        String url = "jdbc:postgresql://localhost:5432/calories_counter"; 
        //use postgresql, connected to my computer, on port 5432, to database: calories counter
        String user = "ningshu";
        String password = "password"; //if doest have anything then leave as """
        //depends on the setup password

        //try to connect: 
        try (Connection con = DriverManager.getConnection(url, user, password)) { //test
            //connection con: opens the connection and closes it safely when done 
            //if reach herel, then connection works 
            System.err.println("Connected to database successfully!");
        } catch (SQLException e) {
            //if something goes wrong, catch it here and print 
            System.err.println("Connection failed.");
            e.printStackTrace();
        }
    }
}
