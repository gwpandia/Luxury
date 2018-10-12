package com.example.pandia.luxury.data;

import com.example.pandia.luxury.util.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class BaseItem {
    protected long mDataBaseID;
    protected String mUniqueID;
    protected String mOldUniqueID;
    protected LocalDateTime mCreateDate;

    BaseItem() {
        mDataBaseID = -1;
        mCreateDate = LocalDateTime.now();
        mUniqueID = "";
        mOldUniqueID = "";
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public void setUniqueID(String uniqueID) {
        if (Util.isValidString(uniqueID)) {
            this.mUniqueID = uniqueID;
            this.mOldUniqueID = uniqueID;
        }
    }

    public long getDataBaseID() {
        return mDataBaseID;
    }

    public void setDataBaseID(long dataBaseID) {
        this.mDataBaseID = dataBaseID;
    }

    public LocalDateTime getCreateDate() {
        return mCreateDate;
    }

    public void setCreateDate(String createDate) {
        if (Util.isValidString(createDate)) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd,HH:mm:ss");
            mCreateDate = LocalDateTime.parse(createDate, dtf);
        }
    }

    public String getOldUniqueID() {
        return mOldUniqueID;
    }

    public boolean isUniqueIDUpdated() {
        return !mUniqueID.equals(mOldUniqueID);
    }

    protected abstract void updateUniqueID();
}
