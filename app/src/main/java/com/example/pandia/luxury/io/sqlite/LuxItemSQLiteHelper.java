package com.example.pandia.luxury.io.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LuxItemSQLiteHelper extends SQLiteOpenHelper {

    public static final String LUX_ITEM_DATABASE_NAME = "myluxury2.db";
    public static final int LUX_ITEM_DATABASE_VERSION = 2;
    private static SQLiteDatabase mLuxItemDatabase;

    public LuxItemSQLiteHelper(Context context, String name, CursorFactory factory,
                               int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getLuxItemDatabase(Context context) {
        if (mLuxItemDatabase == null || !mLuxItemDatabase.isOpen()) {
            mLuxItemDatabase = new LuxItemSQLiteHelper(context, LUX_ITEM_DATABASE_NAME,
                    null, LUX_ITEM_DATABASE_VERSION).getWritableDatabase();

            //mLuxItemDatabase.execSQL(ItemDAO.CREATE_LUXURYITEM_TABLE);
            //mLuxItemDatabase.execSQL(ItemDAO.CREATE_BORROWITEM_TABLE);
        }

        return mLuxItemDatabase;
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
