package com.gainwise.transactional.RoomFiles;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.gainwise.transactional.POJO.Transaction;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'reg'")
    public List<Transaction> getAllTransactions();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'reg' ORDER BY _id DESC LIMIT 50")
    public List<Transaction> getRecentHistoryTransactions();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'info'")
    public List<Transaction> getAllTransactionsInfo();

    @Query("UPDATE _table_transactions SET _amount = :newAmount WHERE _id = :id")
    public void updateTransactionAmountWithID(int id, String newAmount);

    @Query("UPDATE _table_transactions SET _main_label = :newLabel WHERE _id = :id")
    public void updateTransactionMainLabelWithID(int id, String newLabel);

    @Query("UPDATE _table_transactions SET _cross_reference_id = :crossRef WHERE _id = :id")
    public void updateTransactionCrossRefIDWithID(int id, String crossRef);

    @Query("UPDATE _table_transactions SET _cross_reference_adjust_for_stats = :crossRefAdjust WHERE _id = :id")
    public void updateTransactionCrossRefAdjustForStatsWithID(int id, String crossRefAdjust);

    @Query("UPDATE _table_transactions SET _cross_reference_amount = :crossRefAmount WHERE _id = :id")
    public void updateTransactionCrossRefAmountWithID(int id, String crossRefAmount);

    @Query("UPDATE _table_transactions SET _account = :newAccount WHERE _id = :id")
    public void updateTransactionAccountWithID(int id, String newAccount);

    @Query("UPDATE _table_transactions SET _sub_label = :newLabel WHERE _id = :id")
    public void updateTransactionSubLabelWithID(int id, String newLabel);

    @Query("SELECT _current_balance FROM _table_transactions WHERE _type_entry NOT LIKE 'info' ORDER BY _id DESC LIMIT 1")
    public String getLastBalance();

    @Query("SELECT _color_1 FROM _table_transactions WHERE _main_label = :main AND _type_entry LIKE 'info' ORDER BY _id ASC LIMIT 1")
    public String getColorOfMain(String main);

    @Query("UPDATE _table_transactions SET _additional_info = :newAdditionalInfo WHERE _id = :id")
    public void updateTransactionAdditionalInfoWithID(int id, String newAdditionalInfo);

    @Query("SELECT _amount FROM _table_transactions")
    public List<String> fetchAllTransactionAmountsOnly();

    @Query("SELECT _current_balance FROM _table_transactions")
    public List<String> fetchAllTransactionCurrentBalancesOnly();

    @Query("SELECT DISTINCT _main_label FROM _table_transactions WHERE _type_entry LIKE 'info' ORDER BY _main_label ASC")
    public List<String> fetchAllTransactionMainLabelsOnly();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'info' ORDER BY _main_label ASC")
    public List<Transaction> fetchAllTransactionsLabels();

    @Query("SELECT DISTINCT _sub_label FROM _table_transactions WHERE _main_label LIKE :mainLabel AND _type_entry LIKE 'info' ORDER BY _sub_label ASC")
    public List<String> fetchAllTransactionStringOnlySubLabelsOnlyWithMainLabel(String mainLabel);

    @Query("SELECT DISTINCT _sub_label FROM _table_transactions WHERE _main_label LIKE :mainLabel AND _type_entry LIKE 'info' ORDER BY _sub_label DESC LIMIT 1")
    public String fetchLatestSubWithMain(String mainLabel);

    @Query("SELECT * FROM _table_transactions WHERE _main_label LIKE :mainLabel AND _type_entry LIKE 'info' ORDER BY _sub_label ASC")
    public List<Transaction> fetchAllTransactionSubLabelsOnlyWithMainLabel(String mainLabel);

    @Insert
    public void insertAll(Transaction... transactions);

    @Delete
    public void delete(Transaction transaction);

    @Query("DELETE FROM _table_transactions WHERE _id = :id")
    public void deleteTransactionWithID(int id);

    @Query("DELETE FROM _table_transactions WHERE _main_label = :label")
    public void deleteTransactionWithMainLabel(String label);

    @Query("DELETE FROM _table_transactions WHERE _sub_label = :label")
    public void deleteTransactionWithSubLabel(String label);

    @Query("UPDATE _table_transactions \n" +
            "SET _main_label = :newLabel \n" +
            "WHERE _main_label = :oldLabel;")
    public void updateAllMainLabels(String oldLabel, String newLabel);

    @Query("UPDATE _table_transactions \n" +
            "SET _sub_label = :newLabel \n" +
            "WHERE _sub_label = :oldLabel;")
    public void updateAllSubLabels(String oldLabel, String newLabel);

    @Query("UPDATE _table_transactions \n" +
            "SET _color_1 = :newColor \n" +
            "WHERE _main_label = :mainLabel;")
    public void updateAllColorsWithMain(String mainLabel, String newColor);

}
