package com.example.pandia.luxury.io.sqlite;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.data.LuxuryItemConstants;
import com.example.pandia.luxury.util.Util;

import java.util.ArrayList;
import java.util.List;


public class ItemDAO {
    public static final String LUXURYITEM_TABLE_NAME = "luxury_item";
    public static final String BORROWITEM_TABLE_NAME = "borrow_item";

    // Common column
    public static final String KEY_ID = "_id";
    public static final String UNIQUE_ID = "unique_id";
    public static final String CREATE_DATE = "create_date";

    // Luxury Item column
    public static final String ITEM_NAME = "item_name";
    public static final String ITEM_PRICE = "item_price";
    public static final String PURCHASED_DATE = "purchased_date";
    //TODO: image ?
    public static final String ITEM_TYPE = "item_type";
    public static final String EXTRA_DATA = "extra_data";


    // Borrow Management Item
    public static final String BORROWER_NAME = "borrower";
    public static final String BORROW_ITEM_ID = "borrow_item_id";
    public static final String BORROW_DATE = "borrow_date";
    public static final String RETURN_DATE = "return_date";

    public static final String CREATE_LUXURYITEM_TABLE =
            "CREATE TABLE " + LUXURYITEM_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UNIQUE_ID + " TEXT NOT NULL, " +
                    ITEM_NAME + " TEXT NOT NULL, " +
                    ITEM_PRICE + " INTEGER NOT NULL, " +
                    PURCHASED_DATE + " TEXT NOT NULL, " +
                    ITEM_TYPE + " INTEGER NOT NULL, " +
                    EXTRA_DATA + " TEXT, " +
                    CREATE_DATE + " TEXT NOT NULL)";

    public static final String CREATE_BORROWITEM_TABLE =
            "CREATE TABLE " + BORROWITEM_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UNIQUE_ID + " TEXT NOT NULL, " +
                    BORROWER_NAME + " TEXT NOT NULL, " +
                    BORROW_ITEM_ID + " TEXT NOT NULL, " +
                    BORROW_DATE + " TEXT NOT NULL, " +
                    RETURN_DATE + " TEXT NOT NULL, " +
                    CREATE_DATE + " TEXT NOT NULL)";;


    private SQLiteDatabase mDB;

    public ItemDAO(Context context) {
        mDB = LuxItemSQLiteHelper.getLuxItemDatabase(context);
    }

    public void closeDB() {
        if (mDB.isOpen()) {
            mDB.close();
        }
    }

    public LuxuryItem insertLuxuryItem(LuxuryItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(ITEM_NAME, item.getItemName());
        contentValues.put(ITEM_PRICE, item.getPrice());
        contentValues.put(PURCHASED_DATE, Util.convertToDateString(item.getPurchasedDate()));
        contentValues.put(ITEM_TYPE, item.getItemType().getValue());
        contentValues.put(EXTRA_DATA, Util.convertMapToJSonString(item.getAllExtraData()));
        contentValues.put(CREATE_DATE, Util.convertToLocalDateTimeString(item.getCreateDate()));

        long id = mDB.insert(LUXURYITEM_TABLE_NAME, null, contentValues);
        item.setDataBaseID(id);

        return item;
    }

    public boolean updateLuxuryItem(LuxuryItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(ITEM_NAME, item.getItemName());
        contentValues.put(ITEM_PRICE, item.getPrice());
        contentValues.put(PURCHASED_DATE, Util.convertToDateString(item.getPurchasedDate()));
        contentValues.put(ITEM_TYPE, item.getItemType().getValue());
        contentValues.put(EXTRA_DATA, Util.convertMapToJSonString(item.getAllExtraData()));
        contentValues.put(CREATE_DATE, Util.convertToLocalDateTimeString(item.getCreateDate()));
        String where = KEY_ID + "=" + item.getDataBaseID();

        //TODO: Update Image DB if UniqueID is changed.
        // if (item.isUniqueIDUpdated()) {//Update Image DB}

        return mDB.update(LUXURYITEM_TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteLuxuryItem(long id){
        String where = KEY_ID + "=" + id;
        return mDB.delete(LUXURYITEM_TABLE_NAME, where , null) > 0;
    }

    public List<LuxuryItem> getAllLuxuryItem() {
        List<LuxuryItem> result = new ArrayList<>();
        Cursor cursor = mDB.query(
                LUXURYITEM_TABLE_NAME, null, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getLuxuryItemRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public LuxuryItem getNthLuxuryItem(long n) {
        ArrayList<LuxuryItem> ret = getRangeLuxuryItem(n, n + 1);
        if (ret == null || ret.isEmpty()) {
            return null;
        }
        return ret.get(0);
    }

    public ArrayList<LuxuryItem> getRangeLuxuryItem(long start, long end) {
        //TODO start and end > total row count ?
        if (start > end || start < 0 || end < 0) {
            return null;
        }
        ArrayList<LuxuryItem> rangeItems = new ArrayList<LuxuryItem>();
        LuxuryItem item = null;
        String limit = start + "," + (end - start);
        Cursor result = mDB.query(
                LUXURYITEM_TABLE_NAME, null, null, null,
                null, null, null, limit);
        while (result.moveToFirst()) {
            rangeItems.add(getLuxuryItemRecord(result));
        }

        result.close();
        return rangeItems;
    }

    public LuxuryItem getLuxuryItem(long id) {
        LuxuryItem item = null;
        String where = KEY_ID + "=" + id;
        Cursor result = mDB.query(
                LUXURYITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            item = getLuxuryItemRecord(result);
        }

        result.close();
        return item;
    }

    public LuxuryItem getLuxuryItemByUniqueID(String uniqueID) {
        LuxuryItem item = null;
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                LUXURYITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            item = getLuxuryItemRecord(result);
        }

        result.close();
        return item;
    }

    public LuxuryItem getLuxuryItemRecord(Cursor cursor) {
        LuxuryItem result = new LuxuryItem("UNINIT_ITEM");

        result.setDataBaseID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        result.setUniqueID(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
        result.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
        result.setPrice(cursor.getInt(cursor.getColumnIndex(ITEM_PRICE)));
        result.setPurchasedDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(PURCHASED_DATE))));
        result.setItemType(LuxuryItemConstants.LuxuryType.values()[cursor.getInt(cursor.getColumnIndex(ITEM_TYPE))]);
        result.setAllExtraData(Util.convertJSonStringToMap(cursor.getString(cursor.getColumnIndex(EXTRA_DATA))));
        result.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));

        return result;
    }

    public long getLuxuryItemCount() {
        long result = 0;
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + LUXURYITEM_TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getLong(0);
        }
        cursor.close();
        return result;
    }

    public boolean isLuxuryItemExists(String uniqueID) {
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                LUXURYITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        boolean exist = result.moveToFirst();
        result.close();
        return exist;
    }

    public BorrowItem insertBorrowItem(BorrowItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(BORROWER_NAME, item.getBorrower());
        contentValues.put(BORROW_ITEM_ID, item.getBorrowedItemID());
        contentValues.put(BORROW_DATE, Util.convertToDateString(item.getBorrowDate()));
        contentValues.put(RETURN_DATE, Util.convertToDateString(item.getReturnDate()));
        contentValues.put(CREATE_DATE, Util.convertToLocalDateTimeString(item.getCreateDate()));

        long id = mDB.insert(BORROWITEM_TABLE_NAME, null, contentValues);
        item.setDataBaseID(id);

        return item;
    }

    public boolean updateBorrowItem(BorrowItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(BORROWER_NAME, item.getBorrower());
        contentValues.put(BORROW_ITEM_ID, item.getBorrowedItemID());
        contentValues.put(BORROW_DATE, Util.convertToDateString(item.getBorrowDate()));
        contentValues.put(RETURN_DATE, Util.convertToDateString(item.getReturnDate()));
        contentValues.put(CREATE_DATE, Util.convertToLocalDateTimeString(item.getCreateDate()));
        String where = KEY_ID + "=" + item.getDataBaseID();

        return mDB.update(BORROWITEM_TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteBorrowItem(long id){
        String where = KEY_ID + "=" + id;
        return mDB.delete(BORROWITEM_TABLE_NAME, where , null) > 0;
    }

    public List<BorrowItem> getAllBorrowItem() {
        List<BorrowItem> result = new ArrayList<>();
        Cursor cursor = mDB.query(
                BORROWITEM_TABLE_NAME, null, null, null,
                null, null, null, null);

        while (cursor.moveToNext()) {
            result.add(getBorrowItemRecord(cursor));
        }

        cursor.close();
        return result;
    }

    public BorrowItem getBorrowItem(long id) {
        BorrowItem item = null;
        String where = KEY_ID + "=" + id;
        Cursor result = mDB.query(
                BORROWITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            item = getBorrowItemRecord(result);
        }

        result.close();
        return item;
    }

    public BorrowItem getBorrowItemByUniqueID(String uniqueID) {
        BorrowItem item = null;
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                BORROWITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        if (result.moveToFirst()) {
            item = getBorrowItemRecord(result);
        }

        result.close();
        return item;
    }

    public BorrowItem getBorrowItemRecord(Cursor cursor) {
        BorrowItem result = new BorrowItem("UNINIT_BORROWER", "UNINIT_ITEM");

        result.setDataBaseID(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        result.setUniqueID(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));
        result.setBorrower(cursor.getString(cursor.getColumnIndex(BORROWER_NAME)));
        result.setBorrowedItemID(cursor.getString(cursor.getColumnIndex(BORROW_ITEM_ID)));
        result.setBorrowDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(BORROW_DATE))));
        result.setReturnDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(RETURN_DATE))));
        result.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));

        return result;
    }

    public long getBorrowItemCount() {
        long result = 0;
        Cursor cursor = mDB.rawQuery("SELECT COUNT(*) FROM " + BORROWITEM_TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getLong(0);
        }
        cursor.close();
        return result;
    }

    public boolean isBorrowItemExists(String uniqueID) {
        String where = UNIQUE_ID + "=" + uniqueID;
        Cursor result = mDB.query(
                BORROWITEM_TABLE_NAME, null, where, null,
                null, null, null, null);

        boolean exist = result.moveToFirst();
        result.close();
        return exist;
    }

    public void genFakeLuxuryItemData() {
        LuxuryItem item1 = new LuxuryItem("UNINIT_ITEM");
        item1.setItemName("Item 1");
        item1.setPrice(100);
        item1.setPurchasedDate(Util.convertStringToDate("2018-03-01 00:00:00"));
        item1.setItemType(LuxuryItemConstants.LuxuryType.values()[10]);
        item1.setCreateDate("2018-03-01 00:00:00");

        LuxuryItem item2 = new LuxuryItem("UNINIT_ITEM");
        item2.setItemName("Item 2");
        item2.setPrice(200);
        item2.setPurchasedDate(Util.convertStringToDate("2018-04-01 00:00:00"));
        item2.setItemType(LuxuryItemConstants.LuxuryType.values()[20]);
        item2.setCreateDate("2018-04-01 00:00:00");

        LuxuryItem item3 = new LuxuryItem("UNINIT_ITEM");
        item3.setItemName("Item 3");
        item3.setPrice(300);
        item3.setPurchasedDate(Util.convertStringToDate("2018-05-01 00:00:00"));
        item3.setItemType(LuxuryItemConstants.LuxuryType.values()[30]);
        item2.setCreateDate("2018-05-01 00:00:00");

        insertLuxuryItem(item1);
        insertLuxuryItem(item2);
        insertLuxuryItem(item3);
    }

    public void genFakeBorrowItemData() {
        BorrowItem item1 = new BorrowItem("BORROW1", "lux1");
        item1.setBorrowDate(Util.convertStringToDate("2018-01-01 00:00:00"));
        item1.setReturnDate(Util.convertStringToDate("2018-02-01 00:00:00"));
        item1.setCreateDate("2018-04-01 00:00:00");

        BorrowItem item2 = new BorrowItem("BORROW2", "lux2");
        item2.setBorrowDate(Util.convertStringToDate("2018-02-01 00:00:00"));
        item2.setReturnDate(Util.convertStringToDate("2018-03-01 00:00:00"));
        item2.setCreateDate("2018-05-01 00:00:00");

        BorrowItem item3 = new BorrowItem("BORROW3", "lux3");
        item3.setBorrowDate(Util.convertStringToDate("2018-03-01 00:00:00"));
        item3.setReturnDate(Util.convertStringToDate("2018-04-01 00:00:00"));
        item3.setCreateDate("2018-06-01 00:00:00");

        insertBorrowItem(item1);
        insertBorrowItem(item2);
        insertBorrowItem(item3);
    }

}