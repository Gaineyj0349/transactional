package com.gainwise.transactional.RoomFiles;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.gainwise.transactional.POJO.Transaction;

@Database(entities = {Transaction.class}, version = 2)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract TransactionDAO TransactionDAO();


}
