package com.gainwise.transactional.POJO;


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

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    @ColumnInfo (name = "_timeInMillis")
    private long timeInMillis;

    @ColumnInfo (name = "_main_label")
    private String mainLabel;

    @ColumnInfo (name = "_sub_label")
    private String subLabel;

    @ColumnInfo(name = "_is_purchase")
    private boolean isPurchase;

    @ColumnInfo (name = "_date")
    private String date;

    @ColumnInfo (name = "_day_of_week")
    private String dayOfWeek;

    @ColumnInfo (name = "_day")
    private String day;

    @ColumnInfo (name = "_month")
    private String month;

    @ColumnInfo (name = "_year")
    private String year;

    @ColumnInfo (name = "_current_balance")
    private String currentBalance;

    @ColumnInfo (name = "_color_1")
    private String color1;


    @ColumnInfo (name = "_additional_info")
    private String additionalInfo;

    @ColumnInfo (name = "_account")
    private String account;

    @ColumnInfo (name = "_from_cash")
    private String fromCash;

    //using this column name/info for the includeForStatistics variable to be added later
    @ColumnInfo (name = "_cross_reference_id")
    private String crossReferenceID;

    @ColumnInfo (name = "_cross_reference_amount")
    private String crossReferenceAmount;

    @ColumnInfo (name = "_cross_reference_adjust_for_stats")
    private String crossReferenceAdjustForStats;

    //if type entry is info, this is not a transaction but rather information like main label creation for rv in selections
    @ColumnInfo (name = "_type_entry")
    private String typeEntry;

    public Transaction(String amount, String mainLabel, String subLabel, String date, String currentBalance, String additionalInfo,boolean isPurchase, String account, String crossReference,
                      String typeEntry ) {
        this.amount = amount;
        this.mainLabel = mainLabel;
        this.subLabel = subLabel;
        this.date = date;
        this.currentBalance = currentBalance;
        this.additionalInfo = additionalInfo;
        this.isPurchase = isPurchase;
        this.account = account;

        this.typeEntry = typeEntry;
    }

    public Transaction(){}

    public boolean isPurchase() {
        return isPurchase;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCrossReferenceID() {
        return crossReferenceID;
    }

    public void setCrossReferenceID(String crossReferenceID) {
        this.crossReferenceID = crossReferenceID;
    }

    public String getCrossReferenceAmount() {
        return crossReferenceAmount;
    }

    public void setCrossReferenceAmount(String crossReferenceAmount) {
        this.crossReferenceAmount = crossReferenceAmount;
    }

    public String getCrossReferenceAdjustForStats() {
        return crossReferenceAdjustForStats;
    }

    public void setCrossReferenceAdjustForStats(String crossReferenceAdjustForStats) {
        this.crossReferenceAdjustForStats = crossReferenceAdjustForStats;
    }

    public String getTypeEntry() {
        return typeEntry;
    }

    public void setTypeEntry(String typeEntry) {
        this.typeEntry = typeEntry;
    }

    public void setPurchase(boolean purchase) {
        isPurchase = purchase;
    }

    public int getId() {
        return id;
    }

    public String getFromCash() {
        return fromCash;
    }

    public void setFromCash(String fromCash) {
        this.fromCash = fromCash;
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

    public String getMainLabel() {
        return mainLabel;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public void setMainLabel(String mainLabel) {
        this.mainLabel = mainLabel;
    }

    public String getSubLabel() {
        return subLabel;
    }

    public void setSubLabel(String subLabel) {
        this.subLabel = subLabel;
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

    public String getDay() {
        return day;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String printMyInfo(){
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n");
        sb.append(getId());
        sb.append("\n");
        sb.append(getAccount());
        sb.append("\n");
        sb.append(getAmount());
        sb.append("\n");
        sb.append(getMainLabel());
        sb.append("\n");
        sb.append(getSubLabel());
        sb.append("\n");
        sb.append(getDate());
        sb.append("\n");
        sb.append(getAdditionalInfo());
        sb.append("\n");
        sb.append(getCurrentBalance());
        sb.append("\n");
        sb.append(getCrossReferenceID());
        sb.append("\n");
        sb.append(getCrossReferenceAdjustForStats());
        sb.append("\n");
        sb.append(getCrossReferenceAmount());
        sb.append("\n");
        sb.append(getTypeEntry());
        return sb.toString();
    }
}
