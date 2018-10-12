package com.example.pandia.luxury.io.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LuxItemImageSQLiteHelper extends SQLiteOpenHelper {

    public static final String LUX_ITEM_IMAGE_DATABASE_NAME = "myluxuryimg.db";
    public static final int LUX_ITEM_IMAGE_DATABASE_VERSION = 1;
    private static SQLiteDatabase mLuxItemImageDatabase;

    public LuxItemImageSQLiteHelper(Context context, String name, CursorFactory factory,
                                    int version) {
        super(context, name, factory, version);
    }

    public static SQLiteDatabase getLuxItemImageDatabase(Context context) {
        if (mLuxItemImageDatabase == null || !mLuxItemImageDatabase.isOpen()) {
            mLuxItemImageDatabase = new LuxItemImageSQLiteHelper(context, LUX_ITEM_IMAGE_DATABASE_NAME,
                    null, LUX_ITEM_IMAGE_DATABASE_VERSION).getWritableDatabase();
        }

        return mLuxItemImageDatabase;
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
