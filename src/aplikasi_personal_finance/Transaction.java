/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package aplikasi_personal_finance;

import java.sql.Date;

/**
 *
 * @author pakpa
 */
public class Transaction {
    private int id;
    private String type; // "Income" atau "Expense"
    private String description;
    private double amount;
    private Date date; // Gunakan java.sql.Date untuk tanggal
    private String category;
    
    
    public Transaction(){}
    
    public Transaction(int id, String type, String description, double amount, Date date, String category) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
}
