package com.example.pandia.luxury.models;

import android.content.Context;
import android.os.AsyncTask;
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

    private enum TaskType {
        LOAD_RANGE_LUXURY_ITEMS,
        LOAD_ALL_LUXURY_ITEMS,
        UNIQUE_KEY_DELETE_LUXURY_ITEM,
        DBID_DELETE_LUXURY_ITEM
    }

    private static final String TASK_PARAM_NAME_UNIQUEID = "uniqueID";
    private static final String TASK_PARAM_NAME_START = "start";
    private static final String TASK_PARAM_NAME_END = "end";

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
    private boolean mIsWritingData;
    private ILuxuryListPresenter mPresenter;
    private LuxuryListModelTask mDBAsyncTask;

    private LuxuryListModel(ILuxuryListPresenter presenter) {
        mAllLuxuryItems = new ArrayList<LuxuryItem>();
        mItemUpdated = false;
        mIsWritingData = false;
        mPresenter = presenter;
        mDBAsyncTask = null;
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
        if (mDBAsyncTask != null) {
            return false;
        }

        mDBAsyncTask = new LuxuryListModelTask();
        TaskParameters<TaskType> parameters = new TaskParameters<TaskType>(TaskType.LOAD_RANGE_LUXURY_ITEMS);
        parameters.addParameter(TASK_PARAM_NAME_START, Long.toString(start));
        parameters.addParameter(TASK_PARAM_NAME_START, Long.toString(end));
        mDBAsyncTask.execute(parameters);

        return true;
    }

    @Override
    public boolean loadAllLuxuryItems() {
        if (mDBAsyncTask != null) {
            return false;
        }

        mDBAsyncTask = new LuxuryListModelTask();
        TaskParameters<TaskType> parameters = new TaskParameters<TaskType>(TaskType.LOAD_ALL_LUXURY_ITEMS);
        mDBAsyncTask.execute(parameters);

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
        if (mDBAsyncTask != null) {
            return false;
        }

        mDBAsyncTask = new LuxuryListModelTask();
        TaskParameters<TaskType> parameters = new TaskParameters<TaskType>(TaskType.UNIQUE_KEY_DELETE_LUXURY_ITEM);
        parameters.addParameter(TASK_PARAM_NAME_UNIQUEID, uniqueID);
        mDBAsyncTask.execute(parameters);

        return true;
    }

    @Override
    public boolean removeLuxuryItem(long id) {
        if (mDBAsyncTask != null) {
            return false;
        }

        return true;
    }

    private class LuxuryListModelTask extends AsyncTask<TaskParameters<TaskType>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(TaskParameters<TaskType>... taskTypes) {
            if (taskTypes.length != 1) {
                return false;
            }

            boolean ret = false;

            switch (taskTypes[0].getTaskType()) {
                case LOAD_RANGE_LUXURY_ITEMS: {
                    int start = Integer.parseInt(taskTypes[0].getParamter(TASK_PARAM_NAME_START));
                    int end = Integer.parseInt(taskTypes[0].getParamter(TASK_PARAM_NAME_END));

                    ArrayList<LuxuryItem> tmp = LuxuryItemIO.readRangeLuxuryData(start, end, mLuxuryRangeReader, false);
                    if (tmp != null && !tmp.isEmpty()) {
                        mAllLuxuryItems.addAll(tmp);
                        ret = true;
                    }
                }
                    break;
                case LOAD_ALL_LUXURY_ITEMS: {
                    ArrayList<LuxuryItem> tmp = LuxuryItemIO.readAllLuxuryData(mLuxuryRangeReader);
                    if (tmp != null && !tmp.isEmpty()) {
                        mAllLuxuryItems.clear();
                        mAllLuxuryItems = tmp;
                        ret = true;
                    }
                }
                    break;
                case UNIQUE_KEY_DELETE_LUXURY_ITEM:
                    String uniqueID = taskTypes[0].getParamter(TASK_PARAM_NAME_UNIQUEID);
                    if (!uniqueID.isEmpty()) {
                        ret = LuxuryItemIO.deleteLuxuryItemData(uniqueID, mLuxuryWriter);
                        ArrayList<LuxuryItem> tmp = LuxuryItemIO.readAllLuxuryData(mLuxuryRangeReader);
                        if (ret && tmp != null && !tmp.isEmpty()) {
                            mAllLuxuryItems.clear();
                            mAllLuxuryItems = tmp;
                            ret = true;
                        }
                        else {
                            ret = false;
                        }
                    }
                    break;
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mDBAsyncTask = null;
            if (aBoolean) {
                setItemUpdated(true);
            }
        }
    }
}
