package com.gainwise.transactional.RoomFiles;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;

import com.gainwise.transactional.POJO.Transaction;

@Database(entities = {Transaction.class}, version = 3)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract TransactionDAO TransactionDAO();

   public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE _table_transactions"
                    + " ADD COLUMN _timeInMillis INTEGER NOT NULL DEFAULT 0");
        }
    };

}
