package com.gainwise.transactional;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Query("SELECT * FROM _table_transactions")
    public List<Transaction> getAllTransactions();

    @Query("UPDATE _table_transactions SET _amount = :newAmount WHERE _id = :id")
    public void updateTransactionAmountWithID(int id, String newAmount);

    @Query("UPDATE _table_transactions SET _label = :newLabel WHERE _id = :id")
    public void updateTransactionLabelWithID(int id, String newLabel);

    @Query("UPDATE _table_transactions SET _additional_info = :newAdditionalLabel WHERE _id = :id")
    public void updateTransactionAdditionalInfoWithID(int id, String newAdditionalLabel);

    @Query("SELECT _amount FROM _table_transactions")
    public List<String> fetchAllTransactionAmountsOnly();

    @Query("SELECT _current_balance FROM _table_transactions")
    public List<String> fetchAllTransactionCurrentBalancesOnly();

    @Insert
    public void insertAll(Transaction... transactions);

    @Delete
    public void delete(Transaction transaction);

    @Query("DELETE FROM _table_transactions WHERE _id = :id")
    public void deleteTransactionWithID(int id);

}
