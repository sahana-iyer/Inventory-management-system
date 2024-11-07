import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db"; // Modify the DB URL if necessary
    private static final String DB_USER = "root";  // Your database username
    private static final String DB_PASSWORD = "Seafoam@2024";  // Your database password

    // This method will return a connection to the database
    public static Connection connect() {
        try {
            // Load MySQL JDBC driver (this is needed for older versions of Java, newer versions do it automatically)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish connection and return it
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
            return null;
        }
    }
}
