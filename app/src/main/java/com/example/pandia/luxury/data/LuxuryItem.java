package com.example.pandia.luxury.data;

import android.graphics.Bitmap;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.Date;
import java.util.TreeMap;

import androidx.annotation.NonNull;


public class LuxuryItem extends BaseItem {

    private String mItemName;
    private int mPrice;
    private Date mPurchasedDate;
    private Bitmap mItemImage;
    private LuxuryItemConstants.LuxuryType mItemType;
    private TreeMap<String, String> mExtraData;

    public LuxuryItem(String itemName) {
        super();
        setItemName(itemName);
        setItemType(LuxuryItemConstants.LuxuryType.OTHER_TYPE);

        setPrice(LuxuryItemConstants.ITEM_DEFAULT_PRICE);
        setPurchasedDate(new Date());
        setItemImage(null);

        updateUniqueID();
        mExtraData = new TreeMap<String, String>();
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        if (Util.isValidString(itemName)) {
            this.mItemName = itemName;
            //TODO: should update only when store to db
            updateUniqueID();
        }
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        if (price >= 0) {
            this.mPrice = price;
        }
    }

    public Date getPurchasedDate() {
        return mPurchasedDate;
    }

    public void setPurchasedDate(Date purchasedDate) {
        this.mPurchasedDate = purchasedDate;
    }

    public Bitmap getItemImage() {
        return mItemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.mItemImage = itemImage;
    }

    public LuxuryItemConstants.LuxuryType getItemType() {
        return mItemType;
    }

    public void setItemType(LuxuryItemConstants.LuxuryType itemType) {
        this.mItemType = itemType;
        //TODO: should update only when store to db
        updateUniqueID();
    }

    public void addExtraData(String key, String value) {
        if (Util.isValidString(key)) {
            return;
        }

        if (Util.isValidString(value)){
            value = LuxuryItemConstants.ITEM_DEFAULT_EXTRA_VALUE;
        }
        mExtraData.put(key, value);
    }

    public void removeExtraData(String key) {
        if (mExtraData.containsKey(key)) {
            mExtraData.remove(key);
        }
    }

    public String getExtraData(String key) {
        return mExtraData.getOrDefault(key, LuxuryItemConstants.ITEM_DEFAULT_EXTRA_VALUE);
    }

    public void setAllExtraData(TreeMap<String, String> extraData) {
        if (extraData != null) {
            mExtraData = extraData;
        }
    }

    public TreeMap<String, String> getAllExtraData() {
        return mExtraData;
    }

    @NonNull
    @Override
    public String toString() {
        String ret =
                "[LuxuryItem] dbID: " + mDataBaseID + ", UniqueID: " + mUniqueID +
                ", ItemName: " + mItemName + ", Price: " + mPrice +
                ", PurchaseDate: " + Util.convertDateToString(mPurchasedDate) +
                ", ItemType: " + mItemType + ", HasImage:" + ((mItemImage != null) ? "true": "false");

        String extraData = ", ExtraData: [";
        for (String key: mExtraData.keySet()) {
            extraData += "(" + key + ", " + mExtraData.get(key) + "), ";
        }

        if (!mExtraData.isEmpty()) {
            extraData = extraData.substring(0, extraData.length() - 2);
        }

        extraData += "]";

        return ret + extraData;
    }

    @Override
    public void updateUniqueID() {
        mUniqueID = ItemUtil.generateItemUniqueID(mItemName, mItemType,
                Util.convertLocalDateTimeToString(mCreateDate));
    }
}
