package com.example.pandia.luxury.io.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.data.LuxuryItemConstants;
import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.ArrayList;
import java.util.List;


public class ItemImageDAO {
    public static final String LUXURYITEMIMAGE_TABLE_NAME = "luxury_item_image";

    // Common column
    public static final String KEY_ID = "_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String IMAGE_ID = "img_raw";


    public static final String CREATE_LUXURYITEMIMAGE_TABLE =
            "CREATE TABLE " + LUXURYITEMIMAGE_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UNIQUE_ID + " TEXT NOT NULL, " +
                    IMAGE_ID + " BLOB NOT NULL)";

    private SQLiteDatabase mDB;

    public ItemImageDAO(Context context) {
        mDB = LuxItemImageSQLiteHelper.getLuxItemImageDatabase(context);
    }

    public boolean insertLuxuryItemImage(String uniqueID, Bitmap bitmap) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, uniqueID);
        contentValues.put(IMAGE_ID, ItemUtil.convertBitmapToByteArray(bitmap));

        long id = mDB.insert(LUXURYITEMIMAGE_TABLE_NAME, null, contentValues);

        return true;
    }

    public boolean updateLuxuryItemImage(String uniqueID, Bitmap bitmap) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, uniqueID);
        contentValues.put(IMAGE_ID, ItemUtil.convertBitmapToByteArray(bitmap));
        String where = UNIQUE_ID + "=" + uniqueID;

        return mDB.update(LUXURYITEMIMAGE_TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteLuxuryItemImage(String uniqueID){
        String where = UNIQUE_ID + "=" + uniqueID;
        return mDB.delete(LUXURYITEMIMAGE_TABLE_NAME, where , null) > 0;
    }

    public Bitmap getLuxuryItemImage(long id) {
        Bitmap bitmap = null;
        String where = KEY_ID + "=" + id;
        Cursor result = mDB.query(
                LUXURYITEMIMAGE_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            bitmap = getLuxuryItemImageRecord(result);
        }

        result.close();
        return bitmap;
    }

    public Bitmap getLuxuryItemImageByUniqueID(String uniqueID) {
        Bitmap bitmap = null;
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                LUXURYITEMIMAGE_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            bitmap = getLuxuryItemImageRecord(result);
        }

        result.close();
        return bitmap;
    }

    public Bitmap getLuxuryItemImageRecord(Cursor cursor) {
        return ItemUtil.convertByteArrayToBitmap(cursor.getBlob(cursor.getColumnIndex(IMAGE_ID)));
    }

    public long getLuxuryItemImageCount() {
        long result = 0;
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + LUXURYITEMIMAGE_TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getLong(0);
        }
        cursor.close();
        return result;
    }

    public boolean isLuxuryItemImageExists(String uniqueID) {
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                LUXURYITEMIMAGE_TABLE_NAME, null, where, null,
                null, null, null, null);

        boolean exist = result.moveToFirst();
        result.close();
        return exist;
    }
}
