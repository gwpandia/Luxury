package com.example.pandia.luxury.io;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.io.interfaces.IRangeReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.sqlite.ItemDAO;
import com.example.pandia.luxury.io.sqlite.ItemImageDAO;

import java.util.ArrayList;
import java.util.Collection;

public class LuxuryItemSQLiteIO implements IWritable<LuxuryItem>, IReadable<LuxuryItem>, IRangeReadable<LuxuryItem> {

    private Context mContext;
    private ItemDAO mItemDAO;
    private ItemImageDAO mItemImageDAO;

    public LuxuryItemSQLiteIO(Context context) {
        mContext = context;
        mItemDAO = new ItemDAO(mContext);
        mItemImageDAO = new ItemImageDAO(mContext);

        //TODO: Remove test code
        if (mItemDAO.getLuxuryItemCount() == 0) {
            mItemDAO.genFakeLuxuryItemData();
        }
    }

    @Override
    public void initializeReader() {

    }

    @Override
    public long entrySize() {
        return mItemDAO.getLuxuryItemCount();
    }

    @Override
    public boolean isInBound(long i) {
        return i >= 0 && i < entrySize();
    }

    @Override
    public LuxuryItem readEntry(long i) {
        //TODO: Query too much for boundary
        //if (isInBound(i)) {
            //TODO: i is not ID, this is bug here
            LuxuryItem item = mItemDAO.getNthLuxuryItem(i);
            Bitmap bitmap = mItemImageDAO.getLuxuryItemImageByUniqueID(item.getUniqueID());
            item.setItemImage(bitmap);
        //}
        return item;
    }

    @Override
    public LuxuryItem readEntry(String key) {
        LuxuryItem item = mItemDAO.getLuxuryItemByUniqueID(key);
        Bitmap bitmap = mItemImageDAO.getLuxuryItemImageByUniqueID(item.getUniqueID());
        item.setItemImage(bitmap);
        return item;
    }

    @Override
    public void finishReader() {
        //mItemDAO.closeDB();
        //mItemImageDAO.closeDB();
    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(LuxuryItem entry) {
        if (mItemDAO.isLuxuryItemExists(entry.getUniqueID())) {
            mItemDAO.updateLuxuryItem(entry);
        }
        else {
            mItemDAO.insertLuxuryItem(entry);
        }

        if (mItemImageDAO.isLuxuryItemImageExists(entry.getUniqueID())) {
            mItemImageDAO.updateLuxuryItemImage(entry.getUniqueID(), entry.getItemImage());
        }
        else {
            mItemImageDAO.insertLuxuryItemImage(entry.getUniqueID(), entry.getItemImage());
        }

        if (entry.isUniqueIDUpdated()) {
            mItemDAO.deleteLuxuryItem(entry.getOldUniqueID());
            mItemImageDAO.deleteLuxuryItemImage(entry.getOldUniqueID());
        }
    }

    @Override
    public void removeEntry(String uniqueID) {
        if (mItemDAO.isLuxuryItemExists(uniqueID)) {
            mItemDAO.deleteLuxuryItem(uniqueID);
        }

        if (mItemImageDAO.isLuxuryItemImageExists(uniqueID)) {
            mItemImageDAO.deleteLuxuryItemImage(uniqueID);
        }
    }

    @Override
    public void removeEntry(long id) {
        mItemDAO.deleteLuxuryItem(id);
        //TODO: Fix Image ID
        //mItemImageDAO.deleteLuxuryItemImage();
    }

    @Override
    public long writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {
        //mItemDAO.closeDB();
        //mItemImageDAO.closeDB();
    }

    @Override
    public Collection<LuxuryItem> readRangeEntries(long start, long end) {
        ArrayList<LuxuryItem> ret = mItemDAO.getRangeLuxuryItem(start, end);

        Log.i("pandia", "Image count:" + mItemImageDAO.getLuxuryItemImageCount());
        //TODO: maybe load in thread ?
        /*for (LuxuryItem item : ret) {
            item.setItemImage(mItemImageDAO.getLuxuryItemImageByUniqueID(item.getUniqueID()));
        }*/

        return ret;
    }
}
