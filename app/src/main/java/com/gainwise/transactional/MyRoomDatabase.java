package com.gainwise.transactional;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Transaction.class, TransactionLabels.class}, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract Dao TransactionDAO();
    public abstract Dao TransactionLabelsDAO();
}
