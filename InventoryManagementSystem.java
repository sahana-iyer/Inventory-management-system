import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InventoryManagementSystem extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, quantityField, priceField, searchField;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String DB_USER = "root"; // Change to your username
    private static final String DB_PASSWORD = "Seafoam@2024"; // Change to your password

    public InventoryManagementSystem() {
        setTitle("Somaiya Inventory Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dark theme colors
        Color backgroundColor = new Color(45, 45, 45);
        Color textColor = new Color(230, 230, 230);
        Color buttonColor = new Color(70, 70, 70);
        Color inputColor = new Color(60, 60, 60);

        // Table setup with dark theme colors
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Quantity", "Price"}, 0);
        table = new JTable(tableModel);
        table.setBackground(backgroundColor);
        table.setForeground(textColor);
        table.setGridColor(new Color(80, 80, 80));
        table.setSelectionBackground(new Color(80, 80, 80));
        table.getTableHeader().setBackground(new Color(60, 60, 60));
        table.getTableHeader().setForeground(textColor);
        loadProductsFromDatabase();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input panel with aligned labels and text fields
// Input panel with aligned labels and text fields
JPanel inputPanel = new JPanel(new GridBagLayout());
inputPanel.setBackground(backgroundColor);
GridBagConstraints gbc = new GridBagConstraints();
gbc.insets = new Insets(5, 5, 5, 5);
gbc.fill = GridBagConstraints.HORIZONTAL;

JLabel idLabel = new JLabel("ID:");
idLabel.setForeground(textColor);
gbc.gridx = 0;
gbc.gridy = 0;
inputPanel.add(idLabel, gbc);

idField = new JTextField(10);
idField.setBackground(inputColor);
idField.setForeground(textColor);
gbc.gridx = 1;
inputPanel.add(idField, gbc);

JLabel nameLabel = new JLabel("Name:");
nameLabel.setForeground(textColor);
gbc.gridx = 0;
gbc.gridy = 1;
inputPanel.add(nameLabel, gbc);

nameField = new JTextField(10);
nameField.setBackground(inputColor);
nameField.setForeground(textColor);
gbc.gridx = 1;
inputPanel.add(nameField, gbc);

JLabel quantityLabel = new JLabel("Quantity:");
quantityLabel.setForeground(textColor);
gbc.gridx = 0;
gbc.gridy = 2;
inputPanel.add(quantityLabel, gbc);

quantityField = new JTextField(10);
quantityField.setBackground(inputColor);
quantityField.setForeground(textColor);
gbc.gridx = 1;
inputPanel.add(quantityField, gbc);

JLabel priceLabel = new JLabel("Price:");
priceLabel.setForeground(textColor);
gbc.gridx = 0;
gbc.gridy = 3;
inputPanel.add(priceLabel, gbc);

priceField = new JTextField(10);
priceField.setBackground(inputColor);
priceField.setForeground(textColor);
gbc.gridx = 1;
inputPanel.add(priceField, gbc);

// Adjust button positioning
gbc.gridheight = 1; // reset grid height

// Add button
JButton addButton = new JButton("Add Product");
addButton.setBackground(buttonColor);
addButton.setForeground(textColor);
addButton.addActionListener(e -> addProduct());
gbc.gridx = 2;
gbc.gridy = 0;
inputPanel.add(addButton, gbc);

// Delete button
JButton deleteButton = new JButton("Delete Product");
deleteButton.setBackground(buttonColor);
deleteButton.setForeground(textColor);
deleteButton.addActionListener(e -> deleteProduct());
gbc.gridy = 1;
inputPanel.add(deleteButton, gbc);

// Update button
JButton updateButton = new JButton("Update Product");
updateButton.setBackground(buttonColor);
updateButton.setForeground(textColor);
updateButton.addActionListener(e -> updateProduct());
gbc.gridy = 2;
inputPanel.add(updateButton, gbc);

// Manage suppliers button
JButton supplierButton = new JButton("Manage Suppliers");
supplierButton.setBackground(buttonColor);
supplierButton.setForeground(textColor);
supplierButton.addActionListener(e -> openSupplierManagementPanel());
gbc.gridy = 3;
inputPanel.add(supplierButton, gbc);

// Manage pending orders button
JButton pendingOrdersButton = new JButton("Manage Pending Orders");
pendingOrdersButton.setBackground(buttonColor);
pendingOrdersButton.setForeground(textColor);
pendingOrdersButton.addActionListener(e -> openPendingOrdersPanel());
gbc.gridy = 4;
inputPanel.add(pendingOrdersButton, gbc);

// Find product by ID field and button
JLabel searchLabel = new JLabel("Find Product by ID:");
searchLabel.setForeground(textColor);
gbc.gridx = 0;
gbc.gridy = 4;
inputPanel.add(searchLabel, gbc);

searchField = new JTextField(10);
searchField.setBackground(inputColor);
searchField.setForeground(textColor);
gbc.gridx = 1;
inputPanel.add(searchField, gbc);

JButton findButton = new JButton("Find Product");
findButton.setBackground(buttonColor);
findButton.setForeground(textColor);
findButton.addActionListener(e -> findProduct());
gbc.gridx = 2;
gbc.gridy = 5;
inputPanel.add(findButton, gbc);

add(inputPanel, BorderLayout.SOUTH);


    }
    private void openSupplierManagementPanel() {
        SupplierManagementPanel supplierPanel = new SupplierManagementPanel();
        supplierPanel.setVisible(true);
    }


    private void loadProductsFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM products")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading products from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (id, name, quantity, price) VALUES (?, ?, ?, ?)")) {

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, price);

            pstmt.executeUpdate();
            tableModel.addRow(new Object[]{id, name, quantity, price});
            clearFields();

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET name = ?, quantity = ?, price = ? WHERE id = ?")) {
    
                // Get the ID of the selected product
                int id = (int) tableModel.getValueAt(selectedRow, 0);
    
                String name = nameField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                double price = Double.parseDouble(priceField.getText());
    
                // Set the parameters for the update statement
                pstmt.setString(1, name);
                pstmt.setInt(2, quantity);
                pstmt.setDouble(3, price);
                pstmt.setInt(4, id); // Update the product with this ID
    
                pstmt.executeUpdate();
                tableModel.setValueAt(name, selectedRow, 1); // Update name in the table model
                tableModel.setValueAt(quantity, selectedRow, 2); // Update quantity in the table model
                tableModel.setValueAt(price, selectedRow, 3); // Update price in the table model
                clearFields(); // Clear the input fields after updating
    
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error updating product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a product to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    

    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id = ?")) {

                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                tableModel.removeRow(selectedRow);

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting product: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a product to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void findProduct() {
        String idText = searchField.getText();
        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a product ID to search for.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((int) tableModel.getValueAt(i, 0) == id) {
                    table.setRowSelectionInterval(i, i);
                    return;
                }
            }

            JOptionPane.showMessageDialog(this, "Product with ID " + id + " not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openPendingOrdersPanel() {
        PendingOrdersPanel pendingOrdersPanel = new PendingOrdersPanel();
        pendingOrdersPanel.setVisible(true);
    }
    

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        quantityField.setText("");
        priceField.setText("");
    }

    public static void main(String[] args) {
        AdminLogin login = new AdminLogin();
        login.setVisible(true);
    }
}

class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(usernameLabel, gbc);

        usernameField = new JTextField(15);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel, BorderLayout.CENTER);
    }

    private void authenticateUser() {
        // Dummy credentials for demonstration purposes
        String correctUsername = "admin"; // Change as necessary
        String correctPassword = "password"; // Change as necessary

        String inputUsername = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        if (inputUsername.equals(correctUsername) && inputPassword.equals(correctPassword)) {
            // Successful login, open the inventory management system
            InventoryManagementSystem inventoryManagementSystem = new InventoryManagementSystem();
            inventoryManagementSystem.setVisible(true);
            this.dispose(); // Close the login window
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminLogin login = new AdminLogin();
            login.setVisible(true);
        });
    }
}
