package com.example.pandia.luxury.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.pandia.luxury.constants.Constants;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailPresenter;
import com.example.pandia.luxury.io.LuxuryItemIO;
import com.example.pandia.luxury.io.LuxuryItemSQLiteIO;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.util.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import static com.example.pandia.luxury.constants.LuxuryItemConstants.ITEM_DEFAULT_EXTRA_VALUE;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_STRING_KEY;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_KEY_IDENTIFIER;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_VALUE_IDENTIFIER;

public class LuxuryItemDetailModel implements ILuxuryItemDetailModel {

    private enum TaskType {
        LOAD_LUXURY_ITEM
    }

    private static final String TASK_PARAM_NAME_UNIQUEID = "uniqueID";

    //TODO: add IO type parameter ?
    public static LuxuryItemDetailModel createLuxuryItemDetailModel(Context context, Constants.DataIO ioType, ILuxuryItemDetailPresenter presenter, String uniqueID) {
        LuxuryItemDetailModel retModel = new LuxuryItemDetailModel(presenter, uniqueID);
        presenter.setModel(retModel);

        switch (ioType) {
            case SQLITE:
                LuxuryItemSQLiteIO sqliteIO = new LuxuryItemSQLiteIO(context);
                retModel.setLuxuryReader(sqliteIO);
                break;
            case FILE:
            default:
                assert(false);
                break;
        }

        return retModel;
    }


    private IReadable<LuxuryItem> mLuxuryReader;
    private ILuxuryItemDetailPresenter mPresenter;
    private LuxuryItemDetailModel.LuxuryItemDetailModelTask mDBAsyncTask;
    private LuxuryItem mLuxuryItem;
    private boolean mItemUpdated;
    private boolean mInited;
    private String mUniqueID;

    private LuxuryItemDetailModel(ILuxuryItemDetailPresenter presenter, String uniqueID) {
        mPresenter = presenter;
        mLuxuryItem = null;
        mLuxuryReader = null;
        mItemUpdated = false;
        mInited = false;
        mUniqueID = uniqueID;
    }

    public void setLuxuryReader(IReadable<LuxuryItem> luxuryReader) {
        this.mLuxuryReader = luxuryReader;
    }

    public void setItemUpdated(boolean itemUpdated) {
        mItemUpdated = itemUpdated;
        mInited = true;
        if (mPresenter != null && mItemUpdated) {
            mPresenter.updateDetailView();
            mItemUpdated = false;
        }
    }

    public void initialize() {
        if (mDBAsyncTask != null) {
            return;
        }
        mDBAsyncTask = new LuxuryItemDetailModelTask();
        TaskParameters<TaskType> parameters = new TaskParameters<TaskType>(TaskType.LOAD_LUXURY_ITEM);
        parameters.addParameter(TASK_PARAM_NAME_UNIQUEID, mUniqueID);
        mDBAsyncTask.execute(parameters);
    }

    private boolean isDataAvailable() {
        return mInited && mLuxuryItem != null;
    }

    @Override
    public Bitmap getTopImage() {
        if (!isDataAvailable()) {
            return null;
        }
        return mLuxuryItem.getItemImage();
    }

    @Override
    public String getLayoutTitle() {
        if (!isDataAvailable()) {
            return null;
        }
        return mLuxuryItem.getItemName();
    }

    @Override
    public String getItemName() {
        if (!isDataAvailable()) {
            return null;
        }
        return mLuxuryItem.getItemName();
    }

    @Override
    public int getPrice() {
        if (!isDataAvailable()) {
            return 0;
        }
        return mLuxuryItem.getPrice();
    }

    @Override
    public String getUniqueID() {
        if (!isDataAvailable()) {
            return null;
        }
        return mLuxuryItem.getUniqueID();
    }

    @Override
    public String getPurchasedDate() {
        if (!isDataAvailable()) {
            return null;
        }
        return Util.convertDateToString(mLuxuryItem.getPurchasedDate());
    }

    @Override
    public String getItemType() {
        if (!isDataAvailable()) {
            return null;
        }
        return LUXURY_TYPE_DISPLAY_NAME.get(mLuxuryItem.getItemType());
    }

    @Override
    public boolean isItemHasSubType() {
        if (!isDataAvailable()) {
            return false;
        }
        return LUXURY_SUBTYPE_STRING_KEY.containsKey(mLuxuryItem.getItemType());
    }

    @Override
    public String getItemSubType() {
        if (!isDataAvailable()) {
            return null;
        }
        String subCatKey = LUXURY_SUBTYPE_STRING_KEY.getOrDefault(mLuxuryItem.getItemType(), "");
        String subCatValue = mLuxuryItem.getExtraData(subCatKey);

        if (ITEM_DEFAULT_EXTRA_VALUE.equals(subCatValue) ||
                !LUXURY_SUBTYPE_DISPLAY_NAME.getOrDefault(subCatKey, new HashSet<String>()).contains(subCatValue)) {
            return null;
        }

        String retVal = "";
        if (subCatValue.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
            retVal = subCatValue.substring(SUBCATEGORY_VALUE_IDENTIFIER.length());
        }
        retVal = retVal.replaceAll("_", " ");
        return retVal;
    }

    @Override
    public TreeSet<String> getExtraDataKey() {
        if (!isDataAvailable()) {
            return null;
        }

        TreeSet<String> retValue = new TreeSet<String>();

        for (String key: mLuxuryItem.getAllExtraData().keySet()) {
            if (!key.startsWith(SUBCATEGORY_KEY_IDENTIFIER)) {
                retValue.add(key);
            }
        }

        return retValue;
    }

    @Override
    public String getExtraDataValue(String key) {
        if (!isDataAvailable()) {
            return null;
        }

        String value = mLuxuryItem.getExtraData(key);

        if (ITEM_DEFAULT_EXTRA_VALUE.equals(value)
            || value.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
            return null;
        }
        return value;
    }

    private class LuxuryItemDetailModelTask extends AsyncTask<TaskParameters<LuxuryItemDetailModel.TaskType>, Void, Boolean> {

        @Override
        protected Boolean doInBackground(TaskParameters<LuxuryItemDetailModel.TaskType>... taskTypes) {
            if (taskTypes.length != 1) {
                return false;
            }

            boolean ret = false;

            switch (taskTypes[0].getTaskType()) {
                case LOAD_LUXURY_ITEM:
                    String key = taskTypes[0].getParamter(TASK_PARAM_NAME_UNIQUEID);
                    LuxuryItem tmp = LuxuryItemIO.readOneLuxuryData(key, mLuxuryReader);
                    if (tmp != null) {
                        mLuxuryItem = tmp;
                        ret = true;
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
