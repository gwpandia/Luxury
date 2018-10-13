package com.example.pandia.luxury.models;

import android.content.Context;
import android.util.Log;

import com.example.pandia.luxury.constants.Constants;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.ILuxuryListModel;
import com.example.pandia.luxury.interfaces.ILuxuryListPresenter;
import com.example.pandia.luxury.io.LuxuryItemIO;
import com.example.pandia.luxury.io.LuxuryItemSQLiteIO;
import com.example.pandia.luxury.io.interfaces.IRangeReadable;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;

import java.util.ArrayList;

public class LuxuryListModel implements ILuxuryListModel {

    //TODO: add IO type parameter ?
    public static LuxuryListModel createLuxuryListModel(Context context, Constants.DataIO ioType, ILuxuryListPresenter presenter) {
        LuxuryListModel retModel = new LuxuryListModel(presenter);
        presenter.setModel(retModel);

        switch (ioType) {
            case SQLITE:
                LuxuryItemSQLiteIO sqliteIO = new LuxuryItemSQLiteIO(context);
                retModel.setLuxuryRangeReader(sqliteIO);
                retModel.setLuxuryReader(sqliteIO);
                retModel.setLuxuryWriter(sqliteIO);
                break;
            case FILE:
            default:
                assert(false);
                break;
        }

        return retModel;
    }


    private ArrayList<LuxuryItem> mAllLuxuryItems;
    private IRangeReadable<LuxuryItem> mLuxuryRangeReader;
    private IReadable<LuxuryItem> mLuxuryReader;
    private IWritable<LuxuryItem> mLuxuryWriter;

    private boolean mItemUpdated;

    private boolean mIsFetchingData;
    private boolean mIsWritingData;
    private ILuxuryListPresenter mPresenter;

    private LuxuryListModel(ILuxuryListPresenter presenter) {
        mAllLuxuryItems = new ArrayList<LuxuryItem>();
        mItemUpdated = false;
        mIsFetchingData = false;
        mIsWritingData = false;
        mPresenter = presenter;
    }

    public void setLuxuryRangeReader(IRangeReadable<LuxuryItem> luxuryRangeReader) {
        this.mLuxuryRangeReader = luxuryRangeReader;
    }

    public void setLuxuryReader(IReadable<LuxuryItem> luxuryReader) {
        this.mLuxuryReader = luxuryReader;
    }

    public void setLuxuryWriter(IWritable<LuxuryItem> luxuryWriter) {
        this.mLuxuryWriter = luxuryWriter;
    }

    public void setItemUpdated(boolean itemUpdated) {
        mItemUpdated = itemUpdated;
        // TODO: Notify
        if (mPresenter != null && mItemUpdated) {
            mPresenter.onListUpdated();
            mItemUpdated = false;
        }
    }

    @Override
    public int luxuryItemsSize() {
        if (mAllLuxuryItems == null) {
            return 0;
        }
        return mAllLuxuryItems.size();
    }

    @Override
    public LuxuryItem getLuxuryItem(int index) {
        if (mAllLuxuryItems == null) {
            return null;
        }

        if (index < 0 || index >= mAllLuxuryItems.size()) {
            return null;
        }

        return mAllLuxuryItems.get(index);
    }

    // TODO: expose whole model ?
    @Override
    public ArrayList<LuxuryItem> getLuxuryItems() {
        return mAllLuxuryItems;
    }

    @Override
    public boolean loadLuxuryItems(long start, long end) {
        if (mIsFetchingData) {
            return false;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    mIsFetchingData = true;
                    ArrayList<LuxuryItem> tmp = LuxuryItemIO.readRangeLuxuryData(start, end, mLuxuryRangeReader, false);

                    if (tmp != null && !tmp.isEmpty()) {
                        mAllLuxuryItems.addAll(tmp);
                        setItemUpdated(true);
                    }
                    mIsFetchingData = false;
                }
            }
        }).start();

        return true;
    }

    @Override
    public boolean loadAllLuxuryItems() {
        if (mIsFetchingData) {
            return false;
        }
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    mIsFetchingData = true;
                    ArrayList<LuxuryItem> tmp = LuxuryItemIO.readAllLuxuryData(mLuxuryRangeReader);
                    Log.i("pandia", "loadAll result size: " + tmp.size());
                    if (tmp != null && !tmp.isEmpty()) {
                        mAllLuxuryItems.clear();
                        mAllLuxuryItems = tmp;
                        setItemUpdated(true);
                    }
                    mIsFetchingData = false;
                }
            }
        }).start();
*/

        ArrayList<LuxuryItem> tmp = LuxuryItemIO.readAllLuxuryData(mLuxuryRangeReader);
        Log.i("pandia", "loadAll result size: " + tmp.size());
        if (tmp != null && !tmp.isEmpty()) {
            mAllLuxuryItems.clear();
            mAllLuxuryItems = tmp;
            setItemUpdated(true);
        }

        return true;
    }

    @Override
    public boolean addLuxuryItem(LuxuryItem luxuryItem) {
        return false;
    }

    @Override
    public boolean updateLuxuryItem(LuxuryItem luxuryItem) {
        return false;
    }

    @Override
    public boolean removeLuxuryItem(String uniqueID) {
        boolean ret = LuxuryItemIO.deleteLuxuryItemData(uniqueID, mLuxuryWriter);
        ret = ret && loadAllLuxuryItems();
        return ret;
    }

    @Override
    public boolean removeLuxuryItem(long id) {
        boolean ret = LuxuryItemIO.deleteLuxuryItemData(id, mLuxuryWriter);
        ret = ret && loadAllLuxuryItems();
        return ret;
    }
}
