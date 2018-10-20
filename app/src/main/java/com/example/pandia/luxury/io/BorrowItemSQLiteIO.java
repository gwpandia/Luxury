package com.example.pandia.luxury.io;

import android.content.Context;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;
import com.example.pandia.luxury.io.sqlite.ItemDAO;

public class BorrowItemSQLiteIO implements IWritable<BorrowItem>, IReadable<BorrowItem> {

    private Context mContext;
    private ItemDAO mItemDAO;

    public BorrowItemSQLiteIO(Context context) {
        mContext = context;
        mItemDAO = new ItemDAO(mContext);
    }
    @Override
    public void initializeReader() {

    }

    @Override
    public long entrySize() {
        return mItemDAO.getBorrowItemCount();
    }

    @Override
    public boolean isInBound(long i) {
        return i >= 0 && i < entrySize();
    }

    @Override
    public BorrowItem readEntry(long i) {
        //TODO: Query too much for boundary
        if (isInBound(i)) {
            //TODO: i is not ID, this is bug here
            return mItemDAO.getBorrowItem(i);
        }
        return null;
    }

    @Override
    public BorrowItem readEntry(String key) {
        return mItemDAO.getBorrowItemByUniqueID(key);
    }

    @Override
    public void finishReader() {
        mItemDAO.closeDB();
    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(BorrowItem entry) {
        if (mItemDAO.isBorrowItemExists(entry.getUniqueID())) {
            mItemDAO.updateBorrowItem(entry);
        }
        else {
            mItemDAO.insertBorrowItem(entry);
        }
    }

    @Override
    public void removeEntry(String uniqueID) {
        if (mItemDAO.isBorrowItemExists(uniqueID)) {
            mItemDAO.deleteBorrowItem(uniqueID);
        }
    }

    @Override
    public void removeEntry(long id) {
        mItemDAO.deleteBorrowItem(id);
    }

    @Override
    public long writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {
        mItemDAO.closeDB();
    }
}
