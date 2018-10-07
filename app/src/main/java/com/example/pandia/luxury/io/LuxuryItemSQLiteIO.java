package com.example.pandia.luxury.io;

import android.content.Context;

import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.Writable;
import com.example.pandia.luxury.interfaces.Readable;
import com.example.pandia.luxury.io.sqlite.ItemDAO;

public class LuxuryItemSQLiteIO implements Writable<LuxuryItem>, Readable<LuxuryItem> {

    private Context mContext;
    private ItemDAO mItemDAO;

    public LuxuryItemSQLiteIO(Context context) {
        mContext = context;
        mItemDAO = new ItemDAO(mContext);
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
        if (isInBound(i)) {
            //TODO: i is not ID, this is bug here
            return mItemDAO.getLuxuryItem(i);
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
    public void writeEntry(LuxuryItem entry) {
        if (mItemDAO.isLuxuryItemExists(entry.getUniqueID())) {
            mItemDAO.updateLuxuryItem(entry);
        }
        else {
            mItemDAO.insertLuxuryItem(entry);
        }
    }

    @Override
    public long writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {

    }
}
