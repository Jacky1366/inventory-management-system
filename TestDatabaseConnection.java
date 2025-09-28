import java.sql.*;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        // UPDATE THESE WITH YOUR ACTUAL DATABASE INFO
        String DB_URL = "jdbc:mysql://localhost:3306/giraffe";
        String DB_USER = "root";
        String DB_PASSWORD = "password";  // The password hidden by dots in PopSQL

        System.out.println("Testing MySQL Connection...");

        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL Driver loaded successfully");

            // Try to connect
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✓ Connected to database successfully!");

            // Test a simple query
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM Product");

            if (rs.next()) {
                int productCount = rs.getInt("count");
                System.out.println("✓ Found " + productCount + " products in database");
            }

            // List all tables
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet tables = metadata.getTables(null, null, "%", new String[]{"TABLE"});

            System.out.println("\nTables in your database:");
            while (tables.next()) {
                System.out.println("  - " + tables.getString("TABLE_NAME"));
            }

            conn.close();
            System.out.println("\n✓ Connection test SUCCESSFUL! You're ready to run the main GUI.");

        } catch (ClassNotFoundException e) {
            System.out.println("✗ MySQL Driver not found!");
            System.out.println("  Make sure mysql-connector-j-x.x.x.jar is in your classpath");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("✗ Database connection failed!");
            System.out.println("  Check your database URL, username, and password");
            System.out.println("  Error: " + e.getMessage());
        }
    }
}