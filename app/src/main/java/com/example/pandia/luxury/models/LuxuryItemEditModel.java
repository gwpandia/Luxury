package com.example.pandia.luxury.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.example.pandia.luxury.constants.Constants;
import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditPresenter;
import com.example.pandia.luxury.io.LuxuryItemIO;
import com.example.pandia.luxury.io.LuxuryItemSQLiteIO;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;
import com.example.pandia.luxury.util.Util;

import java.util.Date;
import java.util.HashSet;
import java.util.TreeSet;

import static com.example.pandia.luxury.constants.LuxuryItemConstants.ITEM_DEFAULT_EXTRA_VALUE;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_STRING_KEY;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_KEY_IDENTIFIER;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_VALUE_IDENTIFIER;

public class LuxuryItemEditModel implements ILuxuryItemEditModel {
    private enum TaskType {
        LOAD_LUXURY_ITEM,
        SAVE_LUXURY_ITEM
    }

    private static final String TASK_PARAM_NAME_UNIQUEID = "uniqueID";

    //TODO: add IO type parameter ?
    public static LuxuryItemEditModel createLuxuryItemDetailModel(Context context, Constants.DataIO ioType, ILuxuryItemEditPresenter presenter, String uniqueID) {
        LuxuryItemEditModel retModel = new LuxuryItemEditModel(presenter, uniqueID);
        presenter.setModel(retModel);

        switch (ioType) {
            case SQLITE:
                LuxuryItemSQLiteIO sqliteIO = new LuxuryItemSQLiteIO(context);
                retModel.setLuxuryWriter(sqliteIO);
                retModel.setLuxuryReader(sqliteIO);
                break;
            case FILE:
            default:
                assert(false);
                break;
        }

        return retModel;
    }


    private IWritable<LuxuryItem> mLuxuryWriter;
    private IReadable<LuxuryItem> mLuxuryReader;
    private ILuxuryItemEditPresenter mPresenter;
    private LuxuryItemEditModel.LuxuryItemEditModelTask mDBAsyncTask;
    private LuxuryItem mLuxuryItem;
    private boolean mItemUpdated;
    private boolean mInited;
    private String mUniqueID;

    private LuxuryItemEditModel(ILuxuryItemEditPresenter presenter, String uniqueID) {
        mPresenter = presenter;
        mLuxuryItem = null;
        mLuxuryWriter = null;
        mLuxuryReader = null;
        mItemUpdated = false;
        mInited = false;
        mUniqueID = uniqueID;
    }

    public void setLuxuryWriter(IWritable<LuxuryItem> luxuryWriter) {
        this.mLuxuryWriter = luxuryWriter;
    }

    public void setLuxuryReader(IReadable<LuxuryItem> luxuryReader) {
        this.mLuxuryReader = luxuryReader;
    }

    private boolean isDataAvailable() {
        return mInited && mLuxuryItem != null;
    }

    public void setItemUpdated(boolean itemUpdated) {
        mItemUpdated = itemUpdated;
        mInited = true;
        if (mPresenter != null && mItemUpdated) {
            mUniqueID = mPresenter.getUniqueID();
            mPresenter.updateEditView();
            mItemUpdated = false;
        }
    }

    @Override
    public void initialize() {
        if (mUniqueID == null || mUniqueID.isEmpty()) {
            mLuxuryItem = new LuxuryItem("");
            mInited = true;
        }
        else {
            loadLuxuryItem();
        }
    }

    @Override
    public void setItemImage(Bitmap bitmap) {
        if (!isDataAvailable()) {
            return;
        }
        mLuxuryItem.setItemImage(bitmap);
    }

    @Override
    public void setItemName(String itemName) {
        if (!isDataAvailable()) {
            return;
        }
        mLuxuryItem.setItemName(itemName);
    }

    @Override
    public void setPrice(int price) {
        if (!isDataAvailable()) {
            return;
        }
        mLuxuryItem.setPrice(price);
    }

    @Override
    public void setPurchasedDate(Date date) {
        if (!isDataAvailable()) {
            return;
        }
        mLuxuryItem.setPurchasedDate(date);
    }

    @Override
    public void setItemType(LuxuryItemConstants.LuxuryType type) {
        if (!isDataAvailable()) {
            return;
        }
        mLuxuryItem.setItemType(type);
    }

    @Override
    public void setItemSubType(String subType) {
        if (!isDataAvailable()) {
            return;
        }
        //TODO: Check if itemType is set first.
        //TODO: subType mapping ?
        String key = LUXURY_SUBTYPE_STRING_KEY.getOrDefault(mLuxuryItem.getItemType(), "");

        if (!key.isEmpty() && LUXURY_SUBTYPE_DISPLAY_NAME.get(key).contains(subType)){
            mLuxuryItem.addExtraData(key, subType);
        }
    }

    @Override
    public void addExtraData(String key, String value) {
        mLuxuryItem.addExtraData(key, value);
    }

    @Override
    public void removeExtraData(String key) {
        mLuxuryItem.removeExtraData(key);
    }

    @Override
    public void saveLuxuryItem() {
        if (mDBAsyncTask != null) {
            return;
        }

        //TODO: Change name old unique id update ?
        mLuxuryItem.updateUniqueID();
        mDBAsyncTask = new LuxuryItemEditModel.LuxuryItemEditModelTask();
        TaskParameters<LuxuryItemEditModel.TaskType> parameters = new TaskParameters<LuxuryItemEditModel.TaskType>(TaskType.SAVE_LUXURY_ITEM);
        mDBAsyncTask.execute(parameters);
    }

    @Override
    public Bitmap getItemImage() {
        if (!isDataAvailable()) {
            return null;
        }
        return mLuxuryItem.getItemImage();
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

        return Util.generateSubTypeItemDisplayName(subCatValue);
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

    private void loadLuxuryItem() {
        if (mDBAsyncTask != null) {
            return;
        }
        mDBAsyncTask = new LuxuryItemEditModel.LuxuryItemEditModelTask();
        TaskParameters<LuxuryItemEditModel.TaskType> parameters = new TaskParameters<LuxuryItemEditModel.TaskType>(LuxuryItemEditModel.TaskType.LOAD_LUXURY_ITEM);
        parameters.addParameter(TASK_PARAM_NAME_UNIQUEID, mUniqueID);
        mDBAsyncTask.execute(parameters);
    }

    private class LuxuryItemEditModelTask extends AsyncTask<TaskParameters<LuxuryItemEditModel.TaskType>, Void, Boolean> {
        boolean mNotifyCaller;
        @Override
        protected Boolean doInBackground(TaskParameters<LuxuryItemEditModel.TaskType>... taskTypes) {
            if (taskTypes.length != 1) {
                return false;
            }

            boolean ret = false;
            mNotifyCaller = false;

            switch (taskTypes[0].getTaskType()) {
                case LOAD_LUXURY_ITEM:
                    String key = taskTypes[0].getParamter(TASK_PARAM_NAME_UNIQUEID);
                    LuxuryItem tmp = LuxuryItemIO.readOneLuxuryData(key, mLuxuryReader);
                    if (tmp != null) {
                        mLuxuryItem = tmp;
                        ret = true;
                        mNotifyCaller = true;
                    }
                    break;
                case SAVE_LUXURY_ITEM:
                    LuxuryItemIO.writeLuxuryData(mLuxuryItem, mLuxuryWriter);
                    ret = true;
                    break;
                default:
                    assert(false);
                    break;
            }

            return ret;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mDBAsyncTask = null;
            if (aBoolean && mNotifyCaller) {
                setItemUpdated(true);
            }
        }
    }
}
