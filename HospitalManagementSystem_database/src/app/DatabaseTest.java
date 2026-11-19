
package app;

import database.DatabaseConnection;
import java.sql.Connection;

public class DatabaseTest {

    public static void main(String[] args) {

        Connection connection = DatabaseConnection.getConnection();

        if (connection != null) {
            System.out.println("Hospital Management System connected!");
            
            try {
                connection.close();
                System.out.println("Connection closed successfully!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Connection unsuccessful!");
        }
    }
}
