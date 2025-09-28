import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class InventoryManagementGUI extends JFrame {
    // Database connection parameters
    String DB_URL = "jdbc:mysql://localhost:3306/giraffe";
    String DB_USER = "root";
    String DB_PASSWORD = "password";  // The password hidden by dots in PopSQL

    private Connection connection;
    private JTabbedPane tabbedPane;

    public InventoryManagementGUI() {
        super("Inventory & Sales Management System");
        initializeDatabase();
        initializeGUI();
    }

    private void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private void initializeGUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create tabbed pane for different sections
        tabbedPane = new JTabbedPane();

        // Add tabs for each major function
        tabbedPane.addTab("Products", createProductPanel());
        tabbedPane.addTab("Customers", createCustomerPanel());
        tabbedPane.addTab("Suppliers", createSupplierPanel());
        tabbedPane.addTab("Orders", createOrderPanel());
        tabbedPane.addTab("Reports", createReportsPanel());

        add(tabbedPane, BorderLayout.CENTER);

        setSize(1000, 700);
        setLocationRelativeTo(null);
    }

    // Product Management Panel
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table for displaying products
        String[] columns = {"Product ID", "Name", "Description", "Unit Price", "Supplier ID"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Load products into table
        loadProducts(model);

        // Control panel for CRUD operations
        JPanel controlPanel = new JPanel();
        JButton btnAdd = new JButton("Add Product");
        JButton btnEdit = new JButton("Edit Product");
        JButton btnDelete = new JButton("Delete Product");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> showAddProductDialog(model));
        btnEdit.addActionListener(e -> showEditProductDialog(table, model));
        btnDelete.addActionListener(e -> deleteProduct(table, model));
        btnRefresh.addActionListener(e -> loadProducts(model));

        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);
        controlPanel.add(btnRefresh);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Customer Management Panel
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Customer ID", "Name", "Email", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        loadCustomers(model);

        JPanel controlPanel = new JPanel();
        JButton btnAdd = new JButton("Add Customer");
        JButton btnEdit = new JButton("Edit Customer");
        JButton btnDelete = new JButton("Delete Customer");
        JButton btnRefresh = new JButton("Refresh");

        btnAdd.addActionListener(e -> showAddCustomerDialog(model));
        btnEdit.addActionListener(e -> showEditCustomerDialog(table, model));
        btnDelete.addActionListener(e -> deleteCustomer(table, model));
        btnRefresh.addActionListener(e -> loadCustomers(model));

        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);
        controlPanel.add(btnRefresh);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Supplier Management Panel
    private JPanel createSupplierPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Supplier ID", "Name", "Contact Name", "Phone", "Address"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        loadSuppliers(model);

        JPanel controlPanel = new JPanel();
        JButton btnAdd = new JButton("Add Supplier");
        JButton btnEdit = new JButton("Edit Supplier");
        JButton btnDelete = new JButton("Delete Supplier");
        JButton btnRefresh = new JButton("Refresh");

        controlPanel.add(btnAdd);
        controlPanel.add(btnEdit);
        controlPanel.add(btnDelete);
        controlPanel.add(btnRefresh);

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Order Management Panel
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Split panel - top for orders, bottom for order items
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        // Orders table
        String[] orderColumns = {"Order ID", "Order Date", "Customer ID", "Total Amount"};
        DefaultTableModel orderModel = new DefaultTableModel(orderColumns, 0);
        JTable orderTable = new JTable(orderModel);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);

        loadOrders(orderModel);

        // Order items table
        String[] itemColumns = {"Order ID", "Product ID", "Quantity", "Unit Price"};
        DefaultTableModel itemModel = new DefaultTableModel(itemColumns, 0);
        JTable itemTable = new JTable(itemModel);
        JScrollPane itemScrollPane = new JScrollPane(itemTable);

        // Load order items when an order is selected
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && orderTable.getSelectedRow() != -1) {
                int orderId = (int) orderTable.getValueAt(orderTable.getSelectedRow(), 0);
                loadOrderItems(itemModel, orderId);
            }
        });

        JPanel orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(new JLabel("Orders"), BorderLayout.NORTH);
        orderPanel.add(orderScrollPane, BorderLayout.CENTER);

        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.add(new JLabel("Order Items"), BorderLayout.NORTH);
        itemPanel.add(itemScrollPane, BorderLayout.CENTER);

        splitPane.setTopComponent(orderPanel);
        splitPane.setBottomComponent(itemPanel);
        splitPane.setDividerLocation(300);

        // Control panel
        JPanel controlPanel = new JPanel();
        JButton btnNewOrder = new JButton("New Order");
        JButton btnDeleteOrder = new JButton("Delete Order");
        JButton btnRefresh = new JButton("Refresh");

        btnNewOrder.addActionListener(e -> showNewOrderDialog(orderModel));
        btnDeleteOrder.addActionListener(e -> deleteOrder(orderTable, orderModel));
        btnRefresh.addActionListener(e -> loadOrders(orderModel));

        controlPanel.add(btnNewOrder);
        controlPanel.add(btnDeleteOrder);
        controlPanel.add(btnRefresh);

        panel.add(splitPane, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Reports Panel
    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Report buttons
        JButton btnTopProducts = new JButton("Top Selling Products");
        JButton btnTopCustomers = new JButton("Top Customers");
        JButton btnSupplierReport = new JButton("Products by Supplier");
        JButton btnOrderSummary = new JButton("Order Summary");

        btnTopProducts.addActionListener(e -> showTopProductsReport());
        btnTopCustomers.addActionListener(e -> showTopCustomersReport());
        btnSupplierReport.addActionListener(e -> showSupplierProductsReport());
        btnOrderSummary.addActionListener(e -> showOrderSummaryReport());

        panel.add(btnTopProducts);
        panel.add(btnTopCustomers);
        panel.add(btnSupplierReport);
        panel.add(btnOrderSummary);

        return panel;
    }

    // Database operations
    private void loadProducts(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getDouble("unit_price"),
                        rs.getInt("supplier_id")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products: " + e.getMessage());
        }
    }

    private void loadCustomers(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("customer_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage());
        }
    }

    private void loadSuppliers(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Supplier");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_name"),
                        rs.getString("phone"),
                        rs.getString("address")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers: " + e.getMessage());
        }
    }

    private void loadOrders(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM `Order` ORDER BY order_date DESC");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getDate("order_date"),
                        rs.getInt("customer_id"),
                        rs.getDouble("total_amount")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage());
        }
    }

    private void loadOrderItems(DefaultTableModel model, int orderId) {
        model.setRowCount(0);
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM Order_Item WHERE order_id = ?"
            );
            pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading order items: " + e.getMessage());
        }
    }

    // Dialog methods for adding/editing
    private void showAddProductDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Add Product", true);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField supplierField = new JTextField();

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Description:"));
        dialog.add(descField);
        dialog.add(new JLabel("Unit Price:"));
        dialog.add(priceField);
        dialog.add(new JLabel("Supplier ID:"));
        dialog.add(supplierField);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            try {
                PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO Product (name, description, unit_price, supplier_id) VALUES (?, ?, ?, ?)"
                );
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, descField.getText());
                pstmt.setDouble(3, Double.parseDouble(priceField.getText()));
                pstmt.setInt(4, Integer.parseInt(supplierField.getText()));
                pstmt.executeUpdate();
                loadProducts(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Product added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnSave);
        dialog.add(btnCancel);

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditProductDialog(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit");
            return;
        }

        // Similar implementation to showAddProductDialog but with pre-filled values
        // and UPDATE query instead of INSERT
    }

    private void showAddCustomerDialog(DefaultTableModel model) {
        JDialog dialog = new JDialog(this, "Add Customer", true);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));

        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();

        dialog.add(new JLabel("Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Email:"));
        dialog.add(emailField);
        dialog.add(new JLabel("Phone:"));
        dialog.add(phoneField);
        dialog.add(new JLabel("Address:"));
        dialog.add(addressField);

        JButton btnSave = new JButton("Save");
        JButton btnCancel = new JButton("Cancel");

        btnSave.addActionListener(e -> {
            try {
                PreparedStatement pstmt = connection.prepareStatement(
                        "INSERT INTO Customer (name, email, phone, address) VALUES (?, ?, ?, ?)"
                );
                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.setString(3, phoneField.getText());
                pstmt.setString(4, addressField.getText());
                pstmt.executeUpdate();
                loadCustomers(model);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Customer added successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        dialog.add(btnSave);
        dialog.add(btnCancel);

        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showEditCustomerDialog(JTable table, DefaultTableModel model) {
        // Similar to showEditProductDialog
    }

    private void showNewOrderDialog(DefaultTableModel model) {
        // Complex dialog for creating orders with line items
        // Would involve selecting customer, adding products, calculating total
    }

    private void deleteProduct(JTable table, DefaultTableModel model) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete");
            return;
        }

        int productId = (int) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this product?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Product WHERE product_id = ?");
                pstmt.setInt(1, productId);
                pstmt.executeUpdate();
                loadProducts(model);
                JOptionPane.showMessageDialog(this, "Product deleted successfully!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage());
            }
        }
    }

    private void deleteCustomer(JTable table, DefaultTableModel model) {
        // Similar to deleteProduct
    }

    private void deleteOrder(JTable table, DefaultTableModel model) {
        // Similar to deleteProduct, but cascades to Order_Item
    }

    // Report methods
    private void showTopProductsReport() {
        try {
            String query = "SELECT p.name, SUM(oi.quantity * oi.unit_price) as revenue " +
                    "FROM Order_Item oi JOIN Product p ON oi.product_id = p.product_id " +
                    "GROUP BY p.product_id ORDER BY revenue DESC LIMIT 10";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder report = new StringBuilder("TOP SELLING PRODUCTS\n\n");
            while (rs.next()) {
                report.append(String.format("%s: $%.2f\n",
                        rs.getString("name"),
                        rs.getDouble("revenue")));
            }

            JTextArea textArea = new JTextArea(report.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Top Products Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }

    private void showTopCustomersReport() {
        try {
            String query = "SELECT c.name, SUM(o.total_amount) as total_spent " +
                    "FROM `Order` o JOIN Customer c ON o.customer_id = c.customer_id " +
                    "GROUP BY c.customer_id ORDER BY total_spent DESC LIMIT 10";

            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            StringBuilder report = new StringBuilder("TOP CUSTOMERS\n\n");
            while (rs.next()) {
                report.append(String.format("%s: $%.2f\n",
                        rs.getString("name"),
                        rs.getDouble("total_spent")));
            }

            JTextArea textArea = new JTextArea(report.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(400, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Top Customers Report", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }

    private void showSupplierProductsReport() {
        // Show products grouped by supplier
    }

    private void showOrderSummaryReport() {
        // Show order statistics (average order value, orders per day, etc.)
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            InventoryManagementGUI gui = new InventoryManagementGUI();
            gui.setVisible(true);
        });
    }
}