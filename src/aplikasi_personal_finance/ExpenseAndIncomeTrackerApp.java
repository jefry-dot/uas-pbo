package aplikasi_personal_finance;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;


import java.util.ArrayList;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class ExpenseAndIncomeTrackerApp {
    
    // Variables for the main frame and UI components
    private JFrame frame;
    private JPanel titleBar;
    private JLabel titleLabel;
    private JLabel closeLabel;
    private JLabel minimizeLabel;
    private JPanel dashboardPanel;
    private JPanel buttonsPanel;
    private JButton addTransactionButton;
    private JButton removeTransactionButton;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    // Variable to store the total amount
    private double totalAmount = 0.0;
    
    // ArrayList to store data panel values
    private ArrayList<String> dataPanelValues = new ArrayList<>();
    
    // variables for form dragging
    private boolean isDragging = false;
    private Point mouseOffset;
    
    // Constructor
    public ExpenseAndIncomeTrackerApp(){
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,500);
        frame.setLocationRelativeTo(null);
        // Remove form border and default close and minimize buttons
        frame.setUndecorated(true);
        // Set Custom border to the frame
        frame.getRootPane().setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, new Color(52, 73, 94)));
        
        // Create and set up the title bar
        titleBar = new JPanel();
        titleBar.setLayout(null);
        titleBar.setBackground(new Color(52,73,94));
        titleBar.setPreferredSize(new Dimension(frame.getWidth(), 30));
        frame.add(titleBar, BorderLayout.NORTH);
        
        // Create and set up the title label
        titleLabel = new JLabel("Personal Finance Tracker");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 17));
        titleLabel.setBounds(10,0,250,30);
        titleBar.add(titleLabel);
        
        // Create and set up the close label
        closeLabel = new JLabel("x");
        closeLabel.setForeground(Color.WHITE);
        closeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        closeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        closeLabel.setBounds(frame.getWidth() - 50, 0, 30, 30);
        closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse listeners for close label interactions
        
        closeLabel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                System.exit(0);
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                closeLabel.setForeground(Color.red);
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                closeLabel.setForeground(Color.white);
            }
            
        });
        
        titleBar.add(closeLabel);
        
        // Create and set up the minimize label
        minimizeLabel = new JLabel("-");
        minimizeLabel.setForeground(Color.WHITE);
        minimizeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        minimizeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        minimizeLabel.setBounds(frame.getWidth() - 80, 0, 30, 30);
        minimizeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add mouse listeners for minimize label interactions
        minimizeLabel.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e){
                frame.setState(JFrame.ICONIFIED);
            }
            
            @Override
            public void mouseEntered(MouseEvent e){
                minimizeLabel.setForeground(Color.red);
            }
            
            @Override
            public void mouseExited(MouseEvent e){
                minimizeLabel.setForeground(Color.white);
            }
            
        });
        
        titleBar.add(minimizeLabel);
        
        // Set up form dragging functionality
        // Mouse listener for window dragging
        titleBar.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mousePressed(MouseEvent e){
                isDragging = true;
                mouseOffset = e.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent e){
                isDragging = false;
            }
            
        });
        
        // Mouse motion listener for window dragging
        titleBar.addMouseMotionListener(new MouseAdapter() {
            
            @Override
            public void mouseDragged(MouseEvent e){
                if(isDragging){
                    // When the mouse is dragged, this event is triggered
                    // Get the current location of the mouse on the screen
                    Point newLocation = e.getLocationOnScreen();
                    // Calculate the new window location by adjusting for the initial mouse offset
                    newLocation.translate(-mouseOffset.x, -mouseOffset.y);
                    // Set the new location of the main window to achieve dragging effect
                    frame.setLocation(newLocation);
                }
            }
            
        });
        
        // Create and set up the dashboard panel
        dashboardPanel = new JPanel();
        dashboardPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
        dashboardPanel.setBackground(new Color(236,240,241));
        frame.add(dashboardPanel,BorderLayout.CENTER);
        
        // Calculate total amount and populate data panel values
        totalAmount = TransactionValuesCalculation.getTotalValue(TransactionDAO.getAllTransaction());
        dataPanelValues.add(String.format("-%,.2f", TransactionValuesCalculation.getTotalExpenses(TransactionDAO.getAllTransaction())));
        dataPanelValues.add(String.format("%,.2f", TransactionValuesCalculation.getTotalIncomes(TransactionDAO.getAllTransaction())));
        dataPanelValues.add(""+totalAmount);

        
        // Add data panels for Expense, Income, and Total
        addDataPanel("Expense", 0);
        addDataPanel("Income", 1);
        addDataPanel("Total", 2);
        
        
        
        // Create and set up buttons panel
 // Tombol "Add Transaction"
addTransactionButton = new JButton("Add Transaction");
addTransactionButton.setBackground(new Color(41, 128, 185));
addTransactionButton.setForeground(Color.WHITE);
addTransactionButton.setFocusPainted(false);
addTransactionButton.setBorderPainted(false);
addTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
addTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
addTransactionButton.addActionListener((e) -> {
    showAddTransactionDialog();
});

// Tombol "Remove Transaction"
removeTransactionButton = new JButton("Remove Transaction");
removeTransactionButton.setBackground(new Color(231, 76, 60));
removeTransactionButton.setForeground(Color.WHITE);
removeTransactionButton.setFocusPainted(false);
removeTransactionButton.setBorderPainted(false);
removeTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
removeTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
removeTransactionButton.addActionListener((e) -> {
    removeSelectedTransaction();
});

// Tombol "Edit Transaction"
JButton editTransactionButton = new JButton("Edit Transaction");
editTransactionButton.setBackground(new Color(243, 156, 18)); // Warna berbeda untuk membedakan
editTransactionButton.setForeground(Color.WHITE);
editTransactionButton.setFocusPainted(false);
editTransactionButton.setBorderPainted(false);
editTransactionButton.setFont(new Font("Arial", Font.BOLD, 14));
editTransactionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
editTransactionButton.addActionListener((e) -> {
    showEditTransactionDialog(); // Tambahkan metode ini
});


JButton refreshButton = new JButton("Refresh");
refreshButton.setBackground(new Color(60, 179, 113)); // Warna biru untuk tombol refresh
refreshButton.setForeground(Color.WHITE);
refreshButton.setFocusPainted(false);
refreshButton.setBorderPainted(false);
refreshButton.setFont(new Font("Arial", Font.BOLD, 14));
refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Tambahkan ActionListener ke tombol Refresh
refreshButton.addActionListener((e) -> {
    refreshData();
});



JComboBox<String> filterComboBox = new JComboBox<>(new String[]{"ALL", "EXPENSE", "INCOME"});
filterComboBox.setBackground(Color.WHITE);
filterComboBox.setForeground(Color.BLACK);
filterComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
filterComboBox.setCursor(new Cursor(Cursor.HAND_CURSOR));

// Tambahkan ActionListener untuk memproses perubahan filter
filterComboBox.addActionListener((e) -> {
    String selectedFilter = (String) filterComboBox.getSelectedItem();
    filterTableData(selectedFilter, ""); // pass an empty string as the second argument
});



JButton exportCSVButton = new JButton("Export to CSV");
exportCSVButton.setBackground(new Color(46, 204, 113)); // Warna hijau
exportCSVButton.setForeground(Color.WHITE);
exportCSVButton.setFocusPainted(false);
exportCSVButton.setBorderPainted(false);
exportCSVButton.setFont(new Font("Arial", Font.BOLD, 14));
exportCSVButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
exportCSVButton.addActionListener((e) -> exportToCSV());


// Panel untuk tombol
buttonsPanel = new JPanel();
buttonsPanel.setLayout(new GridLayout(2, 3, 10, 10)); // Tata letak grid dengan 3 baris
buttonsPanel.add(addTransactionButton);
buttonsPanel.add(editTransactionButton);
buttonsPanel.add(removeTransactionButton);
buttonsPanel.add(refreshButton);
buttonsPanel.add(filterComboBox);
buttonsPanel.add(exportCSVButton);

// Tambahkan panel tombol ke dashboard
dashboardPanel.add(buttonsPanel);

        
        // Set up the transaction table
        String[] columnNames = {"ID", "Type", "Description", "Amount", "Date", "Category"};
        tableModel = new DefaultTableModel(columnNames, 0)
        {
            @Override
            public boolean isCellEditable(int row, int column){
                // Make all cells non-editable
                return false;
            }
            
        };
        
        
        transactionTable = new JTable(tableModel);
        configureTransactionTable();
        
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        configureScrollPane(scrollPane);
        dashboardPanel.add(scrollPane);
        
        frame.setVisible(true);
    }
    
    
    // fix the negative value
    private String fixNegativeValueDisplay(double value){
        
   
        String newVal = String.format("%.2f", value);
        
        if(newVal.startsWith("-")){
            String numericPart = newVal.substring(2);
            // Format the result as "-$XXX"
            newVal = "-"+numericPart;
        }
        
        return newVal;
    }
    
    
    // Removes the selected transaction from the table and database
    private void removeSelectedTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        
        // Check if a row is selected
        if (selectedRow != -1) {
            // Obtain the transaction details from the selected row
            int transactionId = (int) transactionTable.getValueAt(selectedRow, 0);
            String type = transactionTable.getValueAt(selectedRow, 1).toString();
            String amountStr = transactionTable.getValueAt(selectedRow, 3).toString();
            
            // Remove "Rp" and commas, then parse to double
            double amount = Double.parseDouble(amountStr.replace("Rp", "").replace(",", "").trim());
            
            // Update totalAmount based on the type of transaction
            if (type.equals("Income")) {
                totalAmount -= amount;
            } else {
                totalAmount += amount;
            }
            
            // Repaint the total panel to reflect the updated total amount
            JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
            totalPanel.repaint();
            
            // Determine the index of the data panel to update (0 for Expense, 1 for Income)
            int indexToUpdate = type.equals("Income") ? 1 : 0;
            
            // Update the data panel value and repaint it
            String currentValue = dataPanelValues.get(indexToUpdate);
            double currentAmount = Double.parseDouble(currentValue.replace("", "").replace(" ", "").replace(",", "").replace("--", "-"));
            double updatedAmount = currentAmount + (type.equals("Income") ? -amount : amount);
            // Update the value in the data panel
            if (indexToUpdate == 1) { // income
                dataPanelValues.set(indexToUpdate, String.format("%,.2f", updatedAmount));
            } else { // expense
                dataPanelValues.set(indexToUpdate, fixNegativeValueDisplay(updatedAmount));
            }
    
            // Repaint the corresponding data panel
            JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
            dataPanel.repaint();
            
            // Remove the selected row from the table model
            tableModel.removeRow(selectedRow);
            
            // Remove the transaction from the database
            removeTransactionFromDatabase(transactionId);
        }
    }
    
    
    
    
    // Remove a transaction from the database
    private void removeTransactionFromDatabase(int transactionId){
        
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `transaction_table` WHERE `id` = ?");
            
            ps.setInt(1, transactionId);
            ps.executeLargeUpdate();
            System.out.println("Transaction Removed");
            
        } catch (SQLException ex) {
            Logger.getLogger(ExpenseAndIncomeTrackerApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    
    
    // Displays the dialog for adding a new transaction
    private void showAddTransactionDialog() {
        // Buat JDialog untuk menambahkan transaksi
        JDialog dialog = new JDialog(frame, "Add Transaction", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(frame);
    
        // Panel untuk komponen input
        JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dialogPanel.setBackground(Color.LIGHT_GRAY);
    
        // Input untuk tipe transaksi
        JLabel typeLabel = new JLabel("Type:");
        JComboBox<String> typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});
        typeCombobox.setBackground(Color.WHITE);
        typeCombobox.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
    
        // Input untuk deskripsi
        JLabel descriptionLabel = new JLabel("Description:");
        JTextField descriptionField = new JTextField();
        descriptionField.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
    
        // Input untuk jumlah
        JLabel amountLabel = new JLabel("Amount:");
        JTextField amountField = new JTextField();
        amountField.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
    
        // Input untuk tanggal
        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        JTextField dateField = new JTextField();
        dateField.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
    
        // Input untuk kategori
        JLabel categoryLabel = new JLabel("Category:");
        JTextField categoryField = new JTextField();
        categoryField.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
    
        // Tombol Add
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(41, 128, 185));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addButton.addActionListener((e) -> {
            // Panggil metode addTransaction dengan semua input
            addTransaction(typeCombobox, descriptionField, amountField, dateField, categoryField);
            dialog.dispose(); // Tutup dialog setelah transaksi ditambahkan
        });
    
        // Tambahkan komponen ke panel
        dialogPanel.add(typeLabel);
        dialogPanel.add(typeCombobox);
        dialogPanel.add(descriptionLabel);
        dialogPanel.add(descriptionField);
        dialogPanel.add(amountLabel);
        dialogPanel.add(amountField);
        dialogPanel.add(dateLabel);
        dialogPanel.add(dateField);
        dialogPanel.add(categoryLabel);
        dialogPanel.add(categoryField);
        dialogPanel.add(new JLabel()); // Spacer
        dialogPanel.add(addButton);
    
        // Tambahkan panel ke dialog
        dialog.add(dialogPanel);
        dialog.setVisible(true);
    }
    
    
    
    // Add a new transaction to the database
   private void addTransaction(JComboBox<String> typeCombobox, JTextField descriptionField, JTextField amountField, JTextField dateField, JTextField categoryField) {
    // Ambil data input dari pengguna
    String type = (String) typeCombobox.getSelectedItem();
    String description = descriptionField.getText();
    String amount = amountField.getText();
    String dateStr = dateField.getText();
    String category = categoryField.getText();

    // Validasi input tanggal
    java.sql.Date sqlDate;
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false); // Non-aktifkan leniency untuk validasi ketat
        java.util.Date parsedDate = sdf.parse(dateStr);
        sqlDate = new java.sql.Date(parsedDate.getTime());
    } catch (Exception e) {
        JOptionPane.showMessageDialog(frame, "Invalid date format. Please use yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
        return; // Keluar jika tanggal tidak valid
    }

    // Validasi dan parse nilai amount
    double newAmount;
    try {
        newAmount = Double.parseDouble(amount.replace(",", "").trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a numeric value.", "Error", JOptionPane.ERROR_MESSAGE);
        return; // Keluar jika amount tidak valid
    }

    // Perbarui total amount berdasarkan tipe transaksi
    if (type.equals("Income")) {
        totalAmount += newAmount;
    } else {
        totalAmount -= newAmount;
    }

    // Perbarui data panel dengan jumlah yang diperbarui
    int indexToUpdate = type.equals("Income") ? 1 : 0;
    String currentValue = dataPanelValues.get(indexToUpdate);
    double currentAmount = Double.parseDouble(currentValue.replace(",", "").trim());
    double updatedAmount = currentAmount + (type.equals("Income") ? newAmount : -newAmount);

    if (indexToUpdate == 1) { // Income
        dataPanelValues.set(indexToUpdate, String.format("%,.2f", updatedAmount));
    } else { // Expense
        dataPanelValues.set(indexToUpdate, fixNegativeValueDisplay(updatedAmount));
    }

    // Perbarui tampilan panel
    JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
    totalPanel.repaint();
    JPanel dataPanel = (JPanel) dashboardPanel.getComponent(indexToUpdate);
    dataPanel.repaint();

    // Simpan data ke database
    try (Connection connection = DatabaseConnection.getConnection();
    PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO transaction_table (transaction_type, description, amount, temp_date, date, category) VALUES (?, ?, ?, ?, ?, ?)"
    )) {
   ps.setString(1, type);
   ps.setString(2, description);
   ps.setDouble(3, newAmount);
   ps.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Isi temp_date dengan tanggal sekarang
   ps.setDate(5, sqlDate); // Nilai date yang dimasukkan pengguna
   ps.setString(6, category);

   ps.executeUpdate();



        JOptionPane.showMessageDialog(frame, "Transaction added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Refresh tabel transaksi
        tableModel.setRowCount(0); // Kosongkan tabel
        populateTableTransactions(); // Muat ulang data
    } catch (SQLException ex) {
        Logger.getLogger(ExpenseAndIncomeTrackerApp.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(frame, "Failed to add transaction.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    refreshData();
}

    

private void updateTransaction(int transactionId, JComboBox<String> typeCombobox, JTextField descriptionField, JTextField amountField, JTextField categoryField, int selectedRow, String oldType, double oldAmount) {
    try {
        // Retrieve new transaction data from the UI components
        String newType = (String) typeCombobox.getSelectedItem();
        String newDescription = descriptionField.getText();
        java.sql.Date sqlDate = new java.sql.Date(System.currentTimeMillis());
        String newCategory = categoryField.getText();
        
        // Input validation for amount
        double newAmount = 0.0;
        try {
            newAmount = Double.parseDouble(amountField.getText().replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter a valid amount.");
            return;
        }

        // Update totalAmount based on the difference in transaction amounts
        updateTotalAmount(oldType, newType, oldAmount, newAmount);

        // Update data panel amounts
        updateDataPanelAmounts(oldType, newType, oldAmount, newAmount);

        // Update the database
        updateDatabase(transactionId, newType, newDescription, newAmount, sqlDate, newCategory);

        // Update the table model with the new values
        tableModel.setValueAt(newType, selectedRow, 1);
        tableModel.setValueAt(newDescription, selectedRow, 2);
        tableModel.setValueAt(String.format("%,.2f", newAmount), selectedRow, 3);

        System.out.println("Transaction updated successfully.");
    } catch (SQLException ex) {
        Logger.getLogger(ExpenseAndIncomeTrackerApp.class.getName()).log(Level.SEVERE, null, ex);
    }
    refreshData();
}

// Helper method to update the total amount based on transaction type
private void updateTotalAmount(String oldType, String newType, double oldAmount, double newAmount) {
    // Update totalAmount based on the difference in transaction amounts
    if (oldType.equals("Income")) {
        totalAmount -= oldAmount;
    } else {
        totalAmount += oldAmount;
    }

    if (newType.equals("Income")) {
        totalAmount += newAmount;
    } else {
        totalAmount -= newAmount;
    }

    // Repaint total panel to reflect updated totalAmount
    JPanel totalPanel = (JPanel) dashboardPanel.getComponent(2);
    totalPanel.repaint();

    refreshData();
}

// Helper method to update the data panel amounts
private void updateDataPanelAmounts(String oldType, String newType, double oldAmount, double newAmount) {
    int oldIndex = oldType.equals("Income") ? 1 : 0;
    int newIndex = newType.equals("Income") ? 1 : 0;

    if (oldIndex != newIndex) {
        // Deduct from old type panel
        updatePanelAmount(oldIndex, -oldAmount);

        // Add to new type panel
        updatePanelAmount(newIndex, newAmount);
    } else {
        // Update the same panel
        updatePanelAmount(oldIndex, -oldAmount + newAmount);
    }
}

// Helper method to update the amount in a panel
private void updatePanelAmount(int index, double amount) {
    double panelAmount = Double.parseDouble(dataPanelValues.get(index).replaceAll("[^\\d.]", ""));
    dataPanelValues.set(index, String.format("%,.2f", panelAmount + amount));
    ((JPanel) dashboardPanel.getComponent(index)).repaint();
}

// Helper method to update the database with new transaction data
private void updateDatabase(int transactionId, String newType, String newDescription, double newAmount, 
                            java.sql.Date newTempDate, String newCategory) throws SQLException {
    // Establish connection to the database
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement ps = connection.prepareStatement(
             "UPDATE `transaction_table` SET `transaction_type` = ?, `description` = ?, `amount` = ?, " +
             "`temp_date` = ?, `category` = ? WHERE `id` = ?"
         )) {
        ps.setString(1, newType);
        ps.setString(2, newDescription);
        ps.setDouble(3, newAmount);
        ps.setDate(4, newTempDate); // Tambahkan kolom temp_date
        ps.setString(5, newCategory); // Tambahkan kolom category
        ps.setInt(6, transactionId); // ID tetap di urutan terakhir
        ps.executeUpdate();
    }
    refreshData();
}


    

    private void showEditTransactionDialog() {
        int selectedRow = transactionTable.getSelectedRow();
    
        // Check if a row is selected
        if (selectedRow != -1) {
            // Retrieve the existing data for the selected transaction
            int transactionId = (int) transactionTable.getValueAt(selectedRow, 0);
            String currentType = transactionTable.getValueAt(selectedRow, 1).toString();
            String currentDescription = transactionTable.getValueAt(selectedRow, 2).toString();
            String currentAmountStr = transactionTable.getValueAt(selectedRow, 3).toString();
            String currentDate = transactionTable.getValueAt(selectedRow, 4).toString(); // Ambil tanggal
            String currentCategory = transactionTable.getValueAt(selectedRow, 5).toString(); // Ambil kategori
            double currentAmount = Double.parseDouble(
    currentAmountStr.replace("Rp", "").replace(",", "").trim()
);
    
            // Create a new JDialog for editing the transaction
            JDialog dialog = new JDialog(frame, "Edit Transaction", true);
            dialog.setSize(400, 400);
            dialog.setLocationRelativeTo(frame);
    
            // Create and configure dialog panel
            JPanel dialogPanel = new JPanel(new GridLayout(6, 2, 10, 10));
            dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            dialogPanel.setBackground(Color.LIGHT_GRAY);
    
            // Input untuk tipe transaksi
            JLabel typeLabel = new JLabel("Type:");
            JComboBox<String> typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});
            typeCombobox.setSelectedItem(currentType);
    
            // Input untuk deskripsi
            JLabel descriptionLabel = new JLabel("Description:");
            JTextField descriptionField = new JTextField(currentDescription);
    
            // Input untuk jumlah
            JLabel amountLabel = new JLabel("Amount:");
            JTextField amountField = new JTextField(String.format("%,.2f", currentAmount));
    
            // Input untuk tanggal
            JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
            JTextField dateField = new JTextField(currentDate);
    
            // Input untuk kategori
            JLabel categoryLabel = new JLabel("Category:");
            JTextField categoryField = new JTextField(currentCategory);
    
            // Tombol Update
            JButton updateButton = new JButton("Update");
            updateButton.setBackground(new Color(39, 174, 96));
            updateButton.setForeground(Color.WHITE);
            updateButton.setFocusPainted(false);
            updateButton.setBorderPainted(false);
            updateButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            updateButton.addActionListener(e -> {
                // Panggil metode updateTransaction
                updateTransaction(transactionId, typeCombobox, descriptionField, amountField, categoryField, selectedRow, currentType, currentAmount);
                dialog.dispose();
            });
    
            // Tambahkan komponen ke panel dialog
            dialogPanel.add(typeLabel);
            dialogPanel.add(typeCombobox);
            dialogPanel.add(descriptionLabel);
            dialogPanel.add(descriptionField);
            dialogPanel.add(amountLabel);
            dialogPanel.add(amountField);
            dialogPanel.add(dateLabel);
            dialogPanel.add(dateField);
            dialogPanel.add(categoryLabel);
            dialogPanel.add(categoryField);
            dialogPanel.add(new JLabel()); // Spacer
            dialogPanel.add(updateButton);
    
            // Tampilkan dialog
            dialog.add(dialogPanel);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a transaction to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    
    

    private void refreshData() {
        try {
            // Kosongkan tabel transaksi
            tableModel.setRowCount(0);
    
            // Muat ulang data dari database dan perbarui tabel
            populateTableTransactions();
    
            // Hitung ulang total, income, dan expense
            totalAmount = TransactionValuesCalculation.getTotalValue(TransactionDAO.getAllTransaction());
            dataPanelValues.set(0, String.format("-%,.2f", TransactionValuesCalculation.getTotalExpenses(TransactionDAO.getAllTransaction())));
            dataPanelValues.set(1, String.format("%,.2f", TransactionValuesCalculation.getTotalIncomes(TransactionDAO.getAllTransaction())));
            dataPanelValues.set(2, "" + totalAmount);
    
            // Perbarui tampilan data panel
            for (int i = 0; i < 3; i++) {
                JPanel dataPanel = (JPanel) dashboardPanel.getComponent(i);
                dataPanel.repaint();
            }
    
            System.out.println("Data successfully refreshed!");
        } catch (Exception ex) {
            Logger.getLogger(ExpenseAndIncomeTrackerApp.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(frame, "Failed to refresh data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

private void exportToCSV() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save CSV File");
    fileChooser.setSelectedFile(new File("transactions.csv"));
    int userSelection = fileChooser.showSaveDialog(frame);

    if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();
        try (FileWriter writer = new FileWriter(fileToSave)) {
            // Header
            writer.append("ID,Type,Description,Amount\n");

            // Data dari tabel
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    writer.append(tableModel.getValueAt(i, j).toString());
                    if (j < tableModel.getColumnCount() - 1) {
                        writer.append(",");
                    }
                }
                writer.append("\n");
            }

            JOptionPane.showMessageDialog(frame, "Data exported to CSV successfully!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Failed to export data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


  
    


    
    // Populate Table Transactions
    private void populateTableTransactions() {
        // Kosongkan tabel sebelum menambahkan data baru
        tableModel.setRowCount(0);
    
        // Ambil semua transaksi dari DAO
        for (Transaction transaction : TransactionDAO.getAllTransaction()) {
            // Buat data baris untuk setiap transaksi
            Object[] rowData = {
                transaction.getId(),
                transaction.getType(),
                transaction.getDescription(),
                String.format("Rp%,.2f", transaction.getAmount()), // Format amount ke Rupiah
                transaction.getDate().toString(), // Konversi tanggal ke string
                transaction.getCategory() // Ambil kategori
            };
            // Tambahkan data baris ke tabel
            tableModel.addRow(rowData);
        }
    }
    



    private void filterTableData(String filterType, String filterCategory) {
        // Kosongkan tabel sebelum menambahkan data yang difilter
        tableModel.setRowCount(0);
        
        // Ambil semua transaksi dari DAO
        List<Transaction> allTransactions = TransactionDAO.getAllTransaction();
        
        for (Transaction transaction : allTransactions) {
            boolean matchesFilter = true;
            
            // Filter berdasarkan tipe transaksi (ALL, EXPENSE, INCOME)
            if (!filterType.equals("ALL") && !transaction.getType().equalsIgnoreCase(filterType)) {
                matchesFilter = false;
            }
    
            // Filter berdasarkan kategori transaksi
            if (!filterCategory.isEmpty() && !transaction.getCategory().equalsIgnoreCase(filterCategory)) {
                matchesFilter = false;
            }
    
            // Jika transaksi memenuhi semua filter, tambahkan ke tabel
            if (matchesFilter) {
                Object[] rowData = {
                    transaction.getId(),
                    transaction.getType(),
                    transaction.getDescription(),
                    String.format("%,.2f", transaction.getAmount()),
                    transaction.getDate().toString(),
                    transaction.getCategory()
                };
                tableModel.addRow(rowData);
            }
        }
    }
    
    
    
    // Configures the appearance and behavior of the transaction table
    private void configureTransactionTable(){
        transactionTable.setBackground(new Color(236,240,241));
        transactionTable.setRowHeight(30);
        transactionTable.setShowGrid(false);
        transactionTable.setBorder(null);
        transactionTable.setFont(new Font("Arial",Font.ITALIC,16));
        transactionTable.setDefaultRenderer(Object.class, new TransactionTableCellRenderer());
        transactionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        populateTableTransactions();
        
        JTableHeader tableHeader = transactionTable.getTableHeader();
        tableHeader.setForeground(Color.red);
        tableHeader.setFont(new Font("Arial", Font.BOLD, 18)); 
        transactionTable.setDefaultRenderer(Transaction.class, new TransactionTableCellRenderer());
    }
    
    
    // Configures the appearance of the scroll pane
    private void configureScrollPane(JScrollPane scrollPane){
        
        scrollPane.getVerticalScrollBar().setUI(new CustomScrollBarUI());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        
    }
    
    
    // Add a data panel to the dashboard panel
    private void addDataPanel(String title, int index){
        // Create a new JPanel for the data panel
        JPanel dataPanel = new JPanel(){
            // Override the paintComponent method to customize the appearance
            @Override
            protected void paintComponent(Graphics g){
                // Call the paintComponent method of the superclass
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                //make the drawing smooth
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Check if the title is "Total" to determine the content to display
                if(title.equals("Total")){
      // If the title is "Total," draw the data panel with the total amount
     //drawDataPanel(g2d, title, String.format("$%,.2f", totalAmount), getWidth(), getHeight());
                    drawDataPanel(g2d, title, fixNegativeValueDisplay(totalAmount), getWidth(), getHeight());
                }
                else{
           // If the title is not "Total," draw the data panel with the corresponding value from the list
                    drawDataPanel(g2d, title, dataPanelValues.get(index), getWidth(), getHeight());
                }
            }
            
        };
        
        // Set the layout, size, background color, and border for the data panel
        dataPanel.setLayout(new GridLayout(2, 1));
        dataPanel.setPreferredSize(new Dimension(170, 100));
        dataPanel.setBackground(new Color(255,255,255));
        dataPanel.setBorder(new LineBorder(new Color(149,165,166),2));
        dashboardPanel.add(dataPanel);
    }
    
    
    
    // Draws a data panel with specified title and value
    private void drawDataPanel(Graphics g, String title, String value, int width, int height){
        
        Graphics2D g2d = (Graphics2D)g;
        
        // draw the panel
        g2d.setColor(new Color(255,255,255));
        g2d.fillRoundRect(0, 0, width, height, 20, 20);
        g2d.setColor(new Color(236,240,241));
        g2d.fillRect(0, 0, width, 40);
        
        // draw title
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString(title, 20, 30);
        
        // draw value
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 16));
        g2d.drawString(value, 20, 75);
        
        
    }
    
    // main method
    public static void main(String[] args) {
        new ExpenseAndIncomeTrackerApp();
    }

}



// Custom table header renderer with gradient background
class GradientHeaderRenderer extends JLabel implements TableCellRenderer{
  
    private final Color startColor = new Color(192,192,192);
    private final Color endColor = new Color(50,50,50);

    public GradientHeaderRenderer(){
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD,22));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 1, Color.YELLOW),
                BorderFactory.createEmptyBorder(2, 5, 2, 5))
        );
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      
        setText(value.toString());
        return this;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        
        Graphics2D g2d = (Graphics2D) g;
        
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gradientPaint = new GradientPaint(
                0, 0, startColor,width, 0, endColor);
        
        g2d.setPaint(gradientPaint);
        g2d.fillRect(0, 0, width, height);
        
        super.paintComponent(g);
    }
    
}



// Create a custom scroll bar UI class for the scrollPane
class CustomScrollBarUI extends BasicScrollBarUI{
        // Colors for the thumb and track of the scroll bar
        private Color thumbColor = new Color(189,195,199);
        private Color trackColor = new Color(236,240,241);
   
        // Override method to configure the scroll bar colors
        @Override
        protected void configureScrollBarColors(){
            // Call the superclass method to ensure default configuration
            super.configureScrollBarColors();
            
        }
        
        // Override method to create the decrease button of the scroll bar
        @Override
        protected JButton createDecreaseButton(int orientation){
            // Create an empty button for the decrease button
            return createEmptyButton();
        }
        
        // Override method to create the increase button of the scroll bar
        @Override
        protected JButton createIncreaseButton(int orientation){
            // Create an empty button for the increase button
            return createEmptyButton();
        }
        
        // Override method to paint the thumb of the scroll bar
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds){
            // Set the color and fill the thumb area with the specified color
            g.setColor(thumbColor);
            g.fillRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height);
        }
        
        // Override method to paint the track of the scroll bar
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds){
            // Set the color and fill the track area with the specified color
            g.setColor(trackColor);
            g.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
        
        // Private method to create an empty button with zero dimensions
        private JButton createEmptyButton(){
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            return button;
        }
        
}
    
// Custom cell renderer for the transaction table
class TransactionTableCellRenderer extends DefaultTableCellRenderer{
    
    // Override method to customize the rendering of table cells
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        
        // Call the superclass method to get the default rendering component
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        // Get the transaction type from the second column of the table
        String type = (String) table.getValueAt(row, 1);
        
        // Customize the appearance based on the selection and transaction type
        if(isSelected){
            c.setForeground(Color.BLACK);
            c.setBackground(Color.ORANGE);
        }
        else
        {
            if("Income".equals(type)){
                c.setBackground(new Color(144, 238, 144));
            }
            else{
                c.setBackground(new Color(255,99,71));
            }
        }
        
        return c;
    }
}
