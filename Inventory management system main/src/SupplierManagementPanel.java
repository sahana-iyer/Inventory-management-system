import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SupplierManagementPanel extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField idField, nameField, contactField;

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String DB_USER = "root"; // Change to your username
    private static final String DB_PASSWORD = "Seafoam@2024"; // Change to your password

    public SupplierManagementPanel() {
        setTitle("Supplier Management");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Dark theme colors
        Color backgroundColor = new Color(45, 45, 45);
        Color textColor = new Color(230, 230, 230);
        Color buttonColor = new Color(70, 70, 70);
        Color inputColor = new Color(60, 60, 60);

        // Table setup with dark theme colors
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Contact Person"}, 0);
        table = new JTable(tableModel);
        table.setBackground(backgroundColor);
        table.setForeground(textColor);
        table.setGridColor(new Color(80, 80, 80));
        table.setSelectionBackground(new Color(80, 80, 80));
        table.getTableHeader().setBackground(new Color(60, 60, 60));
        table.getTableHeader().setForeground(textColor);
        loadSuppliersFromDatabase();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Input panel for supplier information
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

        JLabel contactLabel = new JLabel("Contact Person:");
        contactLabel.setForeground(textColor);
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(contactLabel, gbc);

        contactField = new JTextField(10);
        contactField.setBackground(inputColor);
        contactField.setForeground(textColor);
        gbc.gridx = 1;
        inputPanel.add(contactField, gbc);

        JButton addButton = new JButton("Add Supplier");
        addButton.setBackground(buttonColor);
        addButton.setForeground(textColor);
        addButton.addActionListener(e -> addSupplier());
        gbc.gridx = 2;
        gbc.gridy = 0;
        inputPanel.add(addButton, gbc);

        JButton updateButton = new JButton("Update Supplier");
        updateButton.setBackground(buttonColor);
        updateButton.setForeground(textColor);
        updateButton.addActionListener(e -> updateSupplier());
        gbc.gridy = 1;
        inputPanel.add(updateButton, gbc);

        JButton deleteButton = new JButton("Delete Supplier");
        deleteButton.setBackground(buttonColor);
        deleteButton.setForeground(textColor);
        deleteButton.addActionListener(e -> deleteSupplier());
        gbc.gridy = 2;
        inputPanel.add(deleteButton, gbc);

        add(inputPanel, BorderLayout.SOUTH);
    }

    private void loadSuppliersFromDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM suppliers")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("supplier_id"),
                        rs.getString("name"),
                        rs.getString("contact_person")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading suppliers from database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addSupplier() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO suppliers (supplier_id, name, contact_person) VALUES (?, ?, ?)")) {

            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            String contactInfo = contactField.getText();

            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, contactInfo);

            pstmt.executeUpdate();
            tableModel.addRow(new Object[]{id, name, contactInfo});
            clearFields();

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error adding supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSupplier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE suppliers SET name = ?, contact_person = ? WHERE supplier_id = ?")) {

                String name = nameField.getText();
                String contactInfo = contactField.getText();

                pstmt.setString(1, name);
                pstmt.setString(2, contactInfo);
                pstmt.setInt(3, id);

                pstmt.executeUpdate();
                tableModel.setValueAt(name, selectedRow, 1);
                tableModel.setValueAt(contactInfo, selectedRow, 2);
                clearFields();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a supplier to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSupplier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM suppliers WHERE supplier_id = ?")) {

                pstmt.setInt(1, id);
                pstmt.executeUpdate();
                tableModel.removeRow(selectedRow);
                clearFields();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting supplier: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a supplier to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        contactField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SupplierManagementPanel panel = new SupplierManagementPanel();
            panel.setVisible(true);
        });
    }
}
