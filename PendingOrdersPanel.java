import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PendingOrdersPanel extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField customerNameField, quantityField, productIdField;
    private JButton addButton, deleteButton, refreshButton;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String DB_USER = "root"; // Change to your username
    private static final String DB_PASSWORD = "Seafoam@2024"; // Change to your password

    public PendingOrdersPanel() {
        setTitle("Pending Orders Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Apply dark theme to components
        Color backgroundColor = new Color(45, 45, 45);
        Color textColor = new Color(220, 220, 220);
        Color buttonColor = new Color(70, 70, 70);
        Color tableBackgroundColor = new Color(50, 50, 50);
        Color tableGridColor = new Color(100, 100, 100);

        getContentPane().setBackground(backgroundColor);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Order ID", "Product ID", "Customer Name", "Quantity", "Order Date", "Status"}, 0);
        table = new JTable(tableModel);
        table.setBackground(tableBackgroundColor);
        table.setForeground(textColor);
        table.setGridColor(tableGridColor);
        table.setSelectionBackground(new Color(90, 90, 90));
        table.setSelectionForeground(textColor);
        loadPendingOrdersFromDatabase();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel productIdLabel = new JLabel("Product ID:");
        productIdLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(productIdLabel, gbc);

        productIdField = new JTextField(10);
        productIdField.setBackground(buttonColor);
        productIdField.setForeground(textColor);
        gbc.gridx = 1;
        inputPanel.add(productIdField, gbc);

        JLabel customerNameLabel = new JLabel("Customer Name:");
        customerNameLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(customerNameLabel, gbc);

        customerNameField = new JTextField(10);
        customerNameField.setBackground(buttonColor);
        customerNameField.setForeground(textColor);
        gbc.gridx = 1;
        inputPanel.add(customerNameField, gbc);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(quantityLabel, gbc);

        quantityField = new JTextField(10);
        quantityField.setBackground(buttonColor);
        quantityField.setForeground(textColor);
        gbc.gridx = 1;
        inputPanel.add(quantityField, gbc);

        JLabel orderDateLabel = new JLabel("Order Date (YYYY-MM-DD):");
        orderDateLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(orderDateLabel, gbc);

        JTextField orderDateField = new JTextField(10);
        orderDateField.setBackground(buttonColor);
        orderDateField.setForeground(textColor);
        gbc.gridx = 1;
        inputPanel.add(orderDateField, gbc);

        addButton = new JButton("Add Order");
        addButton.setBackground(buttonColor);
        addButton.setForeground(textColor);
        addButton.addActionListener(e -> addOrder(orderDateField.getText()));
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(addButton, gbc);

        deleteButton = new JButton("Delete Order");
        deleteButton.setBackground(buttonColor);
        deleteButton.setForeground(textColor);
        deleteButton.addActionListener(e -> deleteOrder());
        gbc.gridx = 1;
        inputPanel.add(deleteButton, gbc);

        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(buttonColor);
        refreshButton.setForeground(textColor);
        refreshButton.addActionListener(e -> loadPendingOrdersFromDatabase());
        gbc.gridx = 2;
        inputPanel.add(refreshButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadPendingOrdersFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM pending_orders")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("order_id"),
                        rs.getInt("product_id"),
                        rs.getString("customer_name"),
                        rs.getInt("quantity"),
                        rs.getDate("order_date"),
                        rs.getString("status")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading pending orders from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addOrder(String orderDate) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO pending_orders (product_id, customer_name, quantity, order_date, status) VALUES (?, ?, ?, ?, ?)")) {

            int productId = Integer.parseInt(productIdField.getText());
            String customerName = customerNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            String status = "Pending"; // Default status

            pstmt.setInt(1, productId);
            pstmt.setString(2, customerName);
            pstmt.setInt(3, quantity);
            pstmt.setDate(4, Date.valueOf(orderDate));
            pstmt.setString(5, status);

            pstmt.executeUpdate();
            loadPendingOrdersFromDatabase();
            clearFields();

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding order: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int orderId = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM pending_orders WHERE order_id = ?")) {

                pstmt.setInt(1, orderId);
                pstmt.executeUpdate();
                loadPendingOrdersFromDatabase();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting order: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select an order to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        productIdField.setText("");
        customerNameField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PendingOrdersPanel panel = new PendingOrdersPanel();
            panel.setVisible(true);
        });
    }
}
