# Inventory Management System

A comprehensive Java-based inventory management system with MySQL database integration, featuring real-time stock tracking, supplier management, and sales analytics.

## 🚀 Features

- **Product Management**: Full CRUD operations for inventory items
- **Supplier Tracking**: Manage supplier information and relationships
- **Customer Database**: Maintain customer records and order history
- **Order Processing**: Create and track sales orders with automatic stock updates
- **Reporting System**: Generate business insights including:
  - Top selling products
  - Customer purchase analytics
  - Revenue tracking by product/supplier
  - Inventory status reports

## 🛠️ Tech Stack

- **Backend**: Java 22
- **Database**: MySQL 8.0
- **GUI Framework**: Java Swing
- **Build Tool**: Maven
- **JDBC**: MySQL Connector/J 8.0.33



2. **Set up the MySQL database**
   ```sql
   CREATE DATABASE inventory_db;
   USE inventory_db;
   ```
   Then run the SQL script located in `/database/schema.sql`

3. **Configure database connection**
   
   Update the connection parameters in `src/main/java/InventoryManagementGUI.java`:
   ```java
   private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
   private static final String DB_USER = "your_username";
   private static final String DB_PASSWORD = "your_password";
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn exec:java -Dexec.mainClass="InventoryManagementGUI"
   ```

## 📊 Database Schema

The system uses 5 interconnected tables:

- **Supplier**: Manages supplier information
- **Product**: Stores product details and pricing
- **Customer**: Maintains customer records
- **Order**: Tracks sales transactions
- **Order_Item**: Junction table for order-product relationships

![Database ERD](docs/database-erd.png) *(Optional: Add your ERD diagram)*


## 🔑 Key Functionalities

### Product Management
- Add, edit, and delete products
- Track stock levels
- Set pricing and supplier relationships

### Order Processing
- Create new orders
- Add multiple products per order
- Automatic total calculation
- Order history tracking

### Reporting
- Revenue analysis by product
- Top customers by spending
- Supplier product listings
- Sales trends and analytics

## 📝 Project Structure

```
inventory-management-system/
├── src/
│   └── main/
│       └── java/
│           ├── InventoryManagementGUI.java
│           └── TestDatabaseConnection.java
├── database/
│   └── schema.sql
├── pom.xml
└── README.md
```



