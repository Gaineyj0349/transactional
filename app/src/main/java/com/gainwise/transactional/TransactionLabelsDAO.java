package com.gainwise.transactional;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TransactionLabelsDAO {

    @Query("SELECT * FROM _table_transaction_labels")
    public List<TransactionLabels> getAllTransactionLabels();

    @Insert
    public void insertAll(TransactionLabels... transactionLabels);

    @Delete
    public void delete(TransactionLabels transactionLabel);

    @Query("DELETE FROM _table_transaction_labels WHERE _id = :id")
    public void deleteWithID(int id);
}
