package com.gainwise.transactional;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "_table_transactions")
public class Transaction {

    @ColumnInfo (name = "_id")
    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo (name = "_amount")
    private String amount;

    @ColumnInfo (name = "_label")
    private String label;

    @ColumnInfo (name = "_date")
    private String date;

    @ColumnInfo (name = "_current_balance")
    private String currentBalance;

    @ColumnInfo (name = "_additional_info")
    private String additionalInfo;

    public Transaction(String amount, String label, String date, String currentBalance, String additionalInfo) {
        this.amount = amount;
        this.label = label;
        this.date = date;
        this.currentBalance = currentBalance;
        this.additionalInfo = additionalInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
