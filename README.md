# Inventory Management System

## Overview
The Inventory Management System is a desktop application developed in Java using Swing for the user interface and MySQL for the database. This system is designed to assist small businesses in managing their inventory effectively. It includes functionality for tracking product details, pending orders, and supplier information. The system provides a simple, user-friendly interface that allows users to add, update, and delete inventory records with ease.

## Features
Product Management: Allows users to add, update, and delete products. Each product record includes fields like ID, name, price per item, quantity, and total price.

Pending Orders Management: Enables users to manage customer orders that are pending. Users can add, delete, and view orders. Each order includes fields for order ID, product ID, customer name, quantity, order date, and status.

Supplier Management: Provides a section for managing supplier information, including supplier ID, name, contact information, and products supplied.

User-Friendly Interface: Designed with a dark theme for improved readability and usability in low-light environments.

Database Integration: Stores all inventory, orders, and supplier data in a MySQL database for persistent storage.

## Tech Stack
Java: Core programming language used for application logic and GUI development.

Swing: Java's GUI toolkit used to create a responsive and intuitive interface.

MySQL: Relational database used to store and manage data for products, orders, and suppliers.

## Installation and Setup
Prerequisites
Java Development Kit (JDK): Ensure you have JDK 8 or later installed.

MySQL: Install MySQL and set up a database.

Database Setup
Create a new database in MySQL (e.g., inventory_db).

### Run the following SQL commands to create the necessary tables:

sql
Copy code
CREATE DATABASE inventory_db;

USE inventory_db;

CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price_per_item DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    total_price DECIMAL(10, 2) AS (price_per_item * quantity) STORED
);

CREATE TABLE pending_orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    customer_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    order_date DATE,
    status VARCHAR(50),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255),
    products_supplied VARCHAR(255)
);

## Application Configuration
Clone the repository to your local machine.

bash
Copy code
cd inventory-management-system
Update database credentials in the Java code (PendingOrdersPanel.java or other relevant files):

java
Copy code
private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_db";

private static final String DB_USER = "your-username"; // Change to your MySQL username

private static final String DB_PASSWORD = "your-password"; // Change to your MySQL password

Compile and run the application:

bash
Copy code
javac PendingOrdersPanel.java
java PendingOrdersPanel

## Usage
Launch the Application: Start the Inventory Management System by running the main class.

Manage Products: Use the product management section to add, update, or delete products in your inventory.

Handle Orders: Navigate to the pending orders panel to add new customer orders, update existing ones, or delete completed orders.

Manage Suppliers: Add and update supplier information to keep track of your supply chain.

## Future Enhancements
Reports Generation: Add functionality to generate sales and inventory reports.

Enhanced Order Tracking: Include order history and tracking features for better order management.

Export Data: Provide options to export data in CSV or Excel formats for record-keeping and analysis.

User Authentication: Add user authentication and access control for secure data management.

## Screenshots
![WhatsApp Image 2024-11-05 at 21 49 53_4a348d5f](https://github.com/user-attachments/assets/8b307e2c-d5d7-4fee-b845-e72c6d237a61)

![WhatsApp Image 2024-11-05 at 21 50 19_ab51db9b](https://github.com/user-attachments/assets/2b211cce-e755-4d4d-88e8-0eef20799817)

![WhatsApp Image 2024-11-05 at 21 50 41_bed5472d](https://github.com/user-attachments/assets/ef6052c8-4c31-4650-ab51-1a26efcd0548)

![WhatsApp Image 2024-11-05 at 21 52 36_cd7c8255](https://github.com/user-attachments/assets/80077e66-05bf-48e8-aea3-3fa3e35d22b6)

## Conclusion

Conclusion
The Somaiya Inventory Management System is a Java-based application designed to streamline inventory management tasks for small to medium-sized businesses. This system provides a user-friendly interface with a dark theme for improved usability and accessibility.

## Key Features:
Product Management: Users can easily add, update, and delete products in the inventory.

Supplier Management: Manage suppliers to ensure a seamless supply chain.

Pending Orders: A dedicated section for managing and tracking pending orders to enhance order fulfillment.

Search Functionality: Quickly find products by their ID, improving operational efficiency.

Database Integration: The application connects to a MySQL database for persistent data storage, ensuring data integrity and accessibility.

## Technologies Used:
Java: The primary programming language for the application.

Swing: For creating the graphical user interface.

MySQL: For database management and data storage.



