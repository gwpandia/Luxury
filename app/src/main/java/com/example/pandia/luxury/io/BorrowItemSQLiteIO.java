package com.example.pandia.luxury.io;

import android.content.Context;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.interfaces.Readable;
import com.example.pandia.luxury.interfaces.Writable;
import com.example.pandia.luxury.io.sqlite.ItemDAO;

public class BorrowItemSQLiteIO implements Writable<BorrowItem>, Readable<BorrowItem> {

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
            return mItemDAO.getBorrowItem(i);
        }
        return null;
    }

    @Override
    public void finishReader() {

    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(BorrowItem entry) {
        //TODO: how about update data
        mItemDAO.insertBorrowItem(entry);
    }

    @Override
    public long writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {

    }
}
