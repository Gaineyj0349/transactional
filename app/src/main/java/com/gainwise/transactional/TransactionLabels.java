package com.gainwise.transactional;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "_table_transaction_labels")
public class TransactionLabels {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "_id")
    private int id;

    @ColumnInfo (name = "_label")
    private String label;

}
