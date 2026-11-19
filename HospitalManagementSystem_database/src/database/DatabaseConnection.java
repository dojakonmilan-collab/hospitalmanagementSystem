
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
        "jdbc:mysql://localhost:3306/hospital_db";

    private static final String USER = "hospital_app";

    private static final String PASSWORD = "dojakonmilan5421";

    public static Connection getConnection() {

        try {
            Connection connection =
                DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Connected to MySQL successfully!");

            return connection;

        } catch (SQLException e) {

            System.out.println("Database connection failed!");
            e.printStackTrace();

            return null;
        }
    }
}
