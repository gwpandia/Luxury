package com.example.pandia.luxury.io.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myluxury.db";
    public static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase mDatabase;

    public SQLiteHelper(Context context, String name, CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getDatabase(Context context) {
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = new SQLiteHelper(context, DATABASE_NAME,
                    null, DATABASE_VERSION).getWritableDatabase();
        }

        return mDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemDAO.CREATE_LUXURYITEM_TABLE);
        db.execSQL(ItemDAO.CREATE_BORROWITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: no patch here !?
        db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.LUXURYITEM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ItemDAO.BORROWITEM_TABLE_NAME);
        onCreate(db);
    }
}
