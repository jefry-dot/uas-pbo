/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasi_personal_finance;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.logging.*;

/**
 *
 * @author pakpa
 */
public class TransactionDAO {
    
    // Method to retrieve all transactions from the database
    public static List<Transaction> getAllTransaction() {
    // Create a list to store Transaction objects
    List<Transaction> transactions = new ArrayList<>();
    
    Connection connection = DatabaseConnection.getConnection();
    PreparedStatement ps;
    ResultSet rs;
    
    try {
        // Query untuk mendapatkan semua transaksi
        ps = connection.prepareStatement("SELECT * FROM `transaction_table`");
        rs = ps.executeQuery();
        
        // Iterasi melalui hasil query
        while (rs.next()) {
            // Ambil data dari tabel
            int id = rs.getInt("id");
            String type = rs.getString("transaction_type");
            String description = rs.getString("description");
            double amount = rs.getDouble("amount");
            Date date = rs.getDate("date"); // Ambil tanggal
            String category = rs.getString("category"); // Ambil kategori
            
            // Buat objek Transaction
            Transaction transaction = new Transaction(id, type, description, amount, date, category);
            // Tambahkan ke daftar transaksi
            transactions.add(transaction);
        }
    } catch (SQLException ex) {
        Logger.getLogger(TransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    // Kembalikan daftar transaksi
    return transactions;
}


}
