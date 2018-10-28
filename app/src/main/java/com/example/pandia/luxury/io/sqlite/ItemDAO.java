package com.example.pandia.luxury.io.sqlite;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static com.example.pandia.luxury.constants.LuxuryItemConstants.*;

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

    public void clearAllDBContent() {
        mDB.execSQL("DROP TABLE IF EXISTS " + ItemDAO.LUXURYITEM_TABLE_NAME);
        mDB.execSQL("DROP TABLE IF EXISTS " + ItemDAO.BORROWITEM_TABLE_NAME);
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
        contentValues.put(PURCHASED_DATE, Util.convertDateToString(item.getPurchasedDate()));
        contentValues.put(ITEM_TYPE, item.getItemType().getValue());
        contentValues.put(EXTRA_DATA, Util.convertMapToJSonString(item.getAllExtraData()));
        contentValues.put(CREATE_DATE, Util.convertLocalDateTimeToString(item.getCreateDate()));

        long id = mDB.insert(LUXURYITEM_TABLE_NAME, null, contentValues);
        item.setDataBaseID(id);

        return item;
    }

    public boolean updateLuxuryItem(LuxuryItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(ITEM_NAME, item.getItemName());
        contentValues.put(ITEM_PRICE, item.getPrice());
        contentValues.put(PURCHASED_DATE, Util.convertDateToString(item.getPurchasedDate()));
        contentValues.put(ITEM_TYPE, item.getItemType().getValue());
        contentValues.put(EXTRA_DATA, Util.convertMapToJSonString(item.getAllExtraData()));
        contentValues.put(CREATE_DATE, Util.convertLocalDateTimeToString(item.getCreateDate()));
        String where = KEY_ID + "=" + item.getDataBaseID();

        //TODO: Update Image DB if UniqueID is changed.
        // if (item.isUniqueIDUpdated()) {//Update Image DB}

        return mDB.update(LUXURYITEM_TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteLuxuryItem(long id){
        String where = KEY_ID + "=" + id;
        return mDB.delete(LUXURYITEM_TABLE_NAME, where , null) > 0;
    }

    public boolean deleteLuxuryItem(String uniqueID){
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
        return mDB.delete(LUXURYITEM_TABLE_NAME, where , null) > 0;
    }

    public List<LuxuryItem> getAllLuxuryItem() {
        List<LuxuryItem> result = new ArrayList<>();
        Cursor cursor = mDB.query(
                LUXURYITEM_TABLE_NAME, null, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(getLuxuryItemRecord(cursor));
            } while (cursor.moveToNext());
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

        if (result.moveToFirst()) {
            do {
                rangeItems.add(getLuxuryItemRecord(result));
            } while (result.moveToNext());
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
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
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
        result.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
        result.setItemName(cursor.getString(cursor.getColumnIndex(ITEM_NAME)));
        result.setPrice(cursor.getInt(cursor.getColumnIndex(ITEM_PRICE)));
        result.setPurchasedDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(PURCHASED_DATE))));
        result.setItemType(LuxuryItemConstants.LuxuryType.valueOf(cursor.getInt(cursor.getColumnIndex(ITEM_TYPE))));
        result.setAllExtraData(Util.convertJSonStringToMap(cursor.getString(cursor.getColumnIndex(EXTRA_DATA))));
        result.setUniqueID(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));

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
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
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
        contentValues.put(BORROW_DATE, Util.convertDateToString(item.getBorrowDate()));
        contentValues.put(RETURN_DATE, Util.convertDateToString(item.getReturnDate()));
        contentValues.put(CREATE_DATE, Util.convertLocalDateTimeToString(item.getCreateDate()));

        long id = mDB.insert(BORROWITEM_TABLE_NAME, null, contentValues);
        item.setDataBaseID(id);

        return item;
    }

    public boolean updateBorrowItem(BorrowItem item) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(UNIQUE_ID, item.getUniqueID());
        contentValues.put(BORROWER_NAME, item.getBorrower());
        contentValues.put(BORROW_ITEM_ID, item.getBorrowedItemID());
        contentValues.put(BORROW_DATE, Util.convertDateToString(item.getBorrowDate()));
        contentValues.put(RETURN_DATE, Util.convertDateToString(item.getReturnDate()));
        contentValues.put(CREATE_DATE, Util.convertLocalDateTimeToString(item.getCreateDate()));
        String where = KEY_ID + "=" + item.getDataBaseID();

        return mDB.update(BORROWITEM_TABLE_NAME, contentValues, where, null) > 0;
    }

    public boolean deleteBorrowItem(long id){
        String where = KEY_ID + "=" + id;
        return mDB.delete(BORROWITEM_TABLE_NAME, where , null) > 0;
    }

    public boolean deleteBorrowItem(String uniqueID){
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
        return mDB.delete(BORROWITEM_TABLE_NAME, where , null) > 0;
    }

    public List<BorrowItem> getAllBorrowItem() {
        List<BorrowItem> result = new ArrayList<>();
        Cursor cursor = mDB.query(
                BORROWITEM_TABLE_NAME, null, null, null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(getBorrowItemRecord(cursor));
            } while (cursor.moveToNext());
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
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
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
        result.setCreateDate(cursor.getString(cursor.getColumnIndex(CREATE_DATE)));
        result.setBorrower(cursor.getString(cursor.getColumnIndex(BORROWER_NAME)));
        result.setBorrowedItemID(cursor.getString(cursor.getColumnIndex(BORROW_ITEM_ID)));
        result.setBorrowDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(BORROW_DATE))));
        result.setReturnDate(Util.convertStringToDate(cursor.getString(cursor.getColumnIndex(RETURN_DATE))));
        result.setUniqueID(cursor.getString(cursor.getColumnIndex(UNIQUE_ID)));

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
        String where = UNIQUE_ID + "=\"" + uniqueID + "\"";
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
        item1.setPurchasedDate(Util.convertStringToDate("2018-03-01,00:00:00"));
        item1.setItemType(LuxuryItemConstants.LuxuryType.values()[1]);
        item1.setCreateDate("2018-03-01,00:00:00");
        item1.updateUniqueID();
        TreeMap<String, String> extraData1 = new TreeMap<String, String>();
        extraData1.put(MODEL_SUBCATEGORY_KEY, SUBCATEGORY_MODEL_GUNDAM);
        extraData1.put(BOOK_SUBCATEGORY_KEY, "Should not appear");
        extraData1.put("Status", "Unopened");
        extraData1.put("Foo", "Bar");
        item1.setAllExtraData(extraData1);

        LuxuryItem item2 = new LuxuryItem("UNINIT_ITEM");
        item2.setItemName("Item 2");
        item2.setPrice(200);
        item2.setPurchasedDate(Util.convertStringToDate("2018-04-01,00:00:00"));
        item2.setItemType(LuxuryItemConstants.LuxuryType.values()[2]);
        item2.setCreateDate("2018-04-01,00:00:00");
        item2.updateUniqueID();
        TreeMap<String, String> extraData2 = new TreeMap<String, String>();
        extraData2.put(LEGO_SUBCATEGORY_KEY, SUBCATEGORY_LEGO_SYSTEM);
        item2.setAllExtraData(extraData2);

        LuxuryItem item3 = new LuxuryItem("UNINIT_ITEM");
        item3.setItemName("Item 3");
        item3.setPrice(300);
        item3.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item3.setItemType(LuxuryItemConstants.LuxuryType.values()[3]);
        item3.setCreateDate("2018-05-01,00:00:00");
        item3.updateUniqueID();
        TreeMap<String, String> extraData3 = new TreeMap<String, String>();
        extraData3.put(BOOK_SUBCATEGORY_KEY, SUBCATEGORY_LEGO_SYSTEM);
        item3.setAllExtraData(extraData3);

        LuxuryItem item4 = new LuxuryItem("UNINIT_ITEM");
        item4.setItemName("Item 4");
        item4.setPrice(400);
        item4.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item4.setItemType(LuxuryItemConstants.LuxuryType.values()[4]);
        item4.setCreateDate("2018-05-01,00:00:00");
        item4.updateUniqueID();

        LuxuryItem item5 = new LuxuryItem("UNINIT_ITEM");
        item5.setItemName("Item 5");
        item5.setPrice(500);
        item5.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item5.setItemType(LuxuryItemConstants.LuxuryType.values()[5]);
        item5.setCreateDate("2018-05-01,00:00:00");
        item5.updateUniqueID();

        LuxuryItem item6 = new LuxuryItem("UNINIT_ITEM");
        item6.setItemName("Item 6");
        item6.setPrice(400);
        item6.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item6.setItemType(LuxuryItemConstants.LuxuryType.values()[6]);
        item6.setCreateDate("2018-05-01,00:00:00");
        item6.updateUniqueID();

        LuxuryItem item7 = new LuxuryItem("UNINIT_ITEM");
        item7.setItemName("Item 7");
        item7.setPrice(700);
        item7.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item7.setItemType(LuxuryItemConstants.LuxuryType.values()[7]);
        item7.setCreateDate("2018-05-01,00:00:00");
        item7.updateUniqueID();

        LuxuryItem item8 = new LuxuryItem("UNINIT_ITEM");
        item8.setItemName("Item 8");
        item8.setPrice(800);
        item8.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item8.setItemType(LuxuryItemConstants.LuxuryType.values()[8]);
        item8.setCreateDate("2018-05-01,00:00:00");
        item8.updateUniqueID();

        LuxuryItem item9 = new LuxuryItem("UNINIT_ITEM");
        item9.setItemName("Item 9");
        item9.setPrice(900);
        item9.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item9.setItemType(LuxuryItemConstants.LuxuryType.values()[9]);
        item9.setCreateDate("2018-05-01,00:00:00");
        item9.updateUniqueID();

        LuxuryItem item10 = new LuxuryItem("UNINIT_ITEM");
        item10.setItemName("Item 10");
        item10.setPrice(1000);
        item10.setPurchasedDate(Util.convertStringToDate("2018-05-01,00:00:00"));
        item10.setItemType(LuxuryItemConstants.LuxuryType.values()[10]);
        item10.setCreateDate("2018-05-01,00:00:00");
        item10.updateUniqueID();

        insertLuxuryItem(item1);
        insertLuxuryItem(item2);
        insertLuxuryItem(item3);
        insertLuxuryItem(item4);
        insertLuxuryItem(item5);
        insertLuxuryItem(item6);
        insertLuxuryItem(item7);
        insertLuxuryItem(item8);
        insertLuxuryItem(item9);
        insertLuxuryItem(item10);
    }

    public void genFakeBorrowItemData() {
        BorrowItem item1 = new BorrowItem("BORROW1", "lux1");
        item1.setBorrowDate(Util.convertStringToDate("2018-01-01,00:00:00"));
        item1.setReturnDate(Util.convertStringToDate("2018-02-01,00:00:00"));
        item1.setCreateDate("2018-04-01,00:00:00");

        BorrowItem item2 = new BorrowItem("BORROW2", "lux2");
        item2.setBorrowDate(Util.convertStringToDate("2018-02-01,00:00:00"));
        item2.setReturnDate(Util.convertStringToDate("2018-03-01,00:00:00"));
        item2.setCreateDate("2018-05-01,00:00:00");

        BorrowItem item3 = new BorrowItem("BORROW3", "lux3");
        item3.setBorrowDate(Util.convertStringToDate("2018-03-01,00:00:00"));
        item3.setReturnDate(Util.convertStringToDate("2018-04-01,00:00:00"));
        item3.setCreateDate("2018-06-01,00:00:00");

        insertBorrowItem(item1);
        insertBorrowItem(item2);
        insertBorrowItem(item3);
    }

}
