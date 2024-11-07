import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader; // Import this for JTableHeader
import java.awt.*;
import java.sql.*;

public class DeliveriesPanel extends JFrame {
    private DefaultTableModel deliveriesTableModel;
    private JTable deliveriesTable;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String DB_USER = "root"; // Replace with your database username
    private static final String DB_PASSWORD = "Seafoam@2024"; // Replace with your database password

    public DeliveriesPanel() {
        setTitle("Upcoming Deliveries");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dark theme colors
        Color backgroundColor = new Color(45, 45, 45); // Dark background for JFrame and table
        Color textColor = new Color(220, 220, 220); // Light text color for contrast
        Color selectionColor = new Color(80, 80, 80); // Dark selection background color
        Color tableHeaderColor = new Color(50, 50, 50); // Dark background for table header
        Color headerTextColor = new Color(255, 255, 255); // White text for table headers

        // JFrame background
        getContentPane().setBackground(backgroundColor);

        // Table setup for deliveries
        deliveriesTableModel = new DefaultTableModel(new Object[]{"Delivery ID", "Order ID", "Product ID", "Supplier ID", "Delivery Date", "Quantity"}, 0);
        deliveriesTable = new JTable(deliveriesTableModel);
        deliveriesTable.setSelectionBackground(selectionColor);
        deliveriesTable.setSelectionForeground(textColor); // Light text when selected
        deliveriesTable.setBackground(backgroundColor); // Dark background for table
        deliveriesTable.setForeground(textColor); // Light text for table rows
        deliveriesTable.setFont(new Font("Arial", Font.PLAIN, 14)); // Change font to match dark theme

        // Set table header style
        JTableHeader header = deliveriesTable.getTableHeader();
        header.setBackground(tableHeaderColor);
        header.setForeground(headerTextColor);
        header.setFont(new Font("Arial", Font.BOLD, 16)); // Change header font style

        // Add table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(deliveriesTable);
        add(scrollPane, BorderLayout.CENTER);

        // Load deliveries from the database
        loadDeliveriesFromDatabase();
    }

    private void loadDeliveriesFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT delivery_id, order_id, product_id, supplier_id, expected_delivery_date, quantity FROM deliveries WHERE expected_delivery_date >= CURDATE()")) {

            // Clear any existing data
            deliveriesTableModel.setRowCount(0);

            // Populate the table with data from the `deliveries` table
            while (rs.next()) {
                int deliveryId = rs.getInt("delivery_id");
                int orderId = rs.getInt("order_id");
                int productId = rs.getInt("product_id");
                int supplierId = rs.getInt("supplier_id");
                Date deliveryDate = rs.getDate("expected_delivery_date");
                int quantity = rs.getInt("quantity");

                deliveriesTableModel.addRow(new Object[]{deliveryId, orderId, productId, supplierId, deliveryDate, quantity});
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading deliveries: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DeliveriesPanel panel = new DeliveriesPanel();
            panel.setVisible(true);
        });
    }
}
