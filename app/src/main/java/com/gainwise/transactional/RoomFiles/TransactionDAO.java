package com.gainwise.transactional.RoomFiles;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.gainwise.transactional.POJO.Transaction;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM _table_transactions WHERE _id = :ID")
    public Transaction getTransactionWithID(int ID);

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'reg' ORDER BY _id DESC")
    public List<Transaction> getAllTransactions();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'reg' ORDER BY _id DESC LIMIT 50")
    public List<Transaction> getRecentHistoryTransactions();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'info'")
    public List<Transaction> getAllTransactionsInfo();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry NOT LIKE 'info' AND _main_label = :main ORDER BY _id DESC")
    public List<Transaction> fetchAllTransactionsWithMain(String main);


    @Query("SELECT * FROM _table_transactions WHERE _type_entry LIKE 'info' GROUP BY _main_label")
    public List<Transaction> getAllMainDistinctTransactionsInfo();

    @Query("UPDATE _table_transactions SET _amount = :newAmount WHERE _id = :id")
    public void updateTransactionAmountWithID(int id, String newAmount);

    @Query("UPDATE _table_transactions SET _main_label = :newLabel WHERE _id = :id")
    public void updateTransactionMainLabelWithID(int id, String newLabel);

    @Query("UPDATE _table_transactions SET _color_1 = :color WHERE _id = :id")
    public void updateTransactionColorWithID(int id, String color);


    @Query("UPDATE _table_transactions SET _cross_reference_id = :crossRef WHERE _id = :id")
    public void updateTransactionCrossRefIDWithID(int id, String crossRef);

    @Query("UPDATE _table_transactions SET _cross_reference_adjust_for_stats = :crossRefAdjust WHERE _id = :id")
    public void updateTransactionCrossRefAdjustForStatsWithID(int id, String crossRefAdjust);

    @Query("UPDATE _table_transactions SET _cross_reference_amount = :crossRefAmount WHERE _id = :id")
    public void updateTransactionCrossRefAmountWithID(int id, String crossRefAmount);

    @Query("UPDATE _table_transactions SET _account = :newAccount WHERE _id = :id")
    public void updateTransactionAccountWithID(int id, String newAccount);

    @Query("UPDATE _table_transactions SET _current_balance = :newBalance WHERE _id = :id")
    public void updateTransactionEndBalance(int id, String newBalance);

    @Query("UPDATE _table_transactions SET _timeInMillis = :timeInMillis WHERE _id = :id")
    public void updateTimeInMillis(int id, long timeInMillis);
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

    @Query("SELECT _id FROM _table_transactions")
    public List<Integer> fetchAllTransactionIds();

    @Query("SELECT _current_balance FROM _table_transactions")
    public List<String> fetchAllTransactionCurrentBalancesOnly();

    @Query("SELECT DISTINCT _main_label FROM _table_transactions WHERE _type_entry LIKE 'info' ORDER BY _main_label ASC")
    public List<String> fetchAllTransactionMainLabelsOnly();

    @Query("SELECT * FROM _table_transactions WHERE _type_entry NOT LIKE 'info' AND _main_label LIKE :main AND _sub_label LIKE :sub ORDER BY _id DESC")
    public List<Transaction> allTransactionsWithBoth(String main, String sub);


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
            "SET _timeInMillis = :timeInMillis \n" +
            "WHERE _id = :id;")
    public void updateTimeInMillisWithId(int id, long timeInMillis);

    @Query("UPDATE _table_transactions \n" +
            "SET _main_label = :newLabel \n" +
            "WHERE _main_label = :oldLabel;")
    public void updateAllMainLabels(String oldLabel, String newLabel);

    @Query("UPDATE _table_transactions \n" +
            "SET _cross_reference_id = :stat \n" +
            "WHERE _main_label = :mainLabel;")
    public void updateAllStatIncludeswithMain(String mainLabel, String stat);

    @Query("UPDATE _table_transactions \n" +
            "SET _sub_label = :newLabel \n" +
            "WHERE _sub_label = :oldLabel;")
    public void updateAllSubLabels(String oldLabel, String newLabel);

    @Query("UPDATE _table_transactions \n" +
            "SET _color_1 = :newColor \n" +
            "WHERE _main_label = :mainLabel;")
    public void updateAllColorsWithMain(String mainLabel, String newColor);

    @Query("SELECT * FROM _table_transactions WHERE _id > :id AND _type_entry NOT LIKE 'info'")
   public List<Transaction> fetchAllTransactionsWithAmountsOnlyAbove(int id);

    @Query("UPDATE _table_transactions \n" +
            "SET _is_purchase = :value " +
            "WHERE _id = :id;")
    public void updateIsPurchaseWithID(int id, boolean value);

    @Query("SELECT DISTINCT _main_label, COUNT(_main_label) FROM _table_transactions \n" +
            "WHERE _type_entry NOT LIKE 'info' AND _cross_reference_id LIKE '1' AND" +
            " _timeInMillis BETWEEN :milliIn AND :milliOut GROUP BY _main_label ORDER BY _main_label ASC")
    public Cursor STATgetCursorMainWithCounts(long milliIn, long milliOut);

    @Query("SELECT _amount,_is_purchase  FROM _table_transactions WHERE _main_label like :main  AND _timeInMillis BETWEEN :milliIn AND :milliOut  AND _type_entry NOT LIKE 'info'")
    public Cursor getCursorAmountsAndIsPurchaseWithMain(String main,long milliIn, long milliOut);

    @Query("SELECT _amount,_is_purchase  FROM _table_transactions WHERE _main_label like :main" +
            " AND _timeInMillis BETWEEN :milliIn AND :milliOut" +
            " AND _cross_reference_id LIKE '1' AND _type_entry NOT LIKE 'info'")
    public Cursor STATgetCursorAmountsAndIsPurchaseWithMain(String main,long milliIn, long milliOut);

    @Query("SELECT _amount,_is_purchase  FROM _table_transactions WHERE _type_entry NOT LIKE 'info' AND _timeInMillis BETWEEN :milliIn AND :milliOut AND _cross_reference_id LIKE '1'")
    public Cursor STATgetCursorAmountsAndIsPurchaseOfAll(long milliIn, long milliOut);

    @Query("SELECT MIN(_timeInMillis) AS First," +
            "    MAX(_timeInMillis) AS Last" +
            "    FROM _table_transactions WHERE _type_entry NOT LIKE 'info'" +
            " AND _cross_reference_id LIKE '1';")
    public Cursor STATFirstAndLast();



    @Query("UPDATE _table_transactions \n" +
            "SET _cross_reference_id = '1' WHERE _id = :id;")
    public void updateTypeIncludeForStatsWithMain(int id);

    @Query("SELECT _cross_reference_id FROM _table_transactions WHERE _main_label LIKE :main AND _type_entry LIKE 'info' ORDER BY _id" +
            " ASC LIMIT 1;")
    public String getIncludeStatStatus(String main);

    @Query("SELECT MIN(_timeInMillis) AS First," +
            "    MAX(_timeInMillis) AS Last" +
            "    FROM _table_transactions WHERE _type_entry NOT LIKE 'info'" +
            " AND _timeInMillis BETWEEN :milliIn AND :milliOut " +
            " " +
            " AND _cross_reference_id LIKE '1';")
    public Cursor STATcustomRangeFirstAndLast(long milliIn, long milliOut);

    @Query("SELECT * FROM _table_transactions;")
    public Cursor getCountOfAll();

}
