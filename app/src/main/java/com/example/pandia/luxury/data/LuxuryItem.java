package com.example.pandia.luxury.data;

import android.media.Image;
import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import org.json.JSONObject;

import java.util.Date;
import java.util.TreeMap;


public class LuxuryItem {

    private long mDataBaseID;
    private String mUniqueID;
    private String mItemName;
    private int mPrice;
    private Date mPurchasedDate;
    private Image mItemImage;
    private LuxuryItemConstants.LuxuryType mItemType;
    private TreeMap<String, String> mExtraData;

    public LuxuryItem(String itemName) {
        setItemName(itemName);
        setItemType(LuxuryItemConstants.LuxuryType.OTHER_TYPE);

        setPrice(LuxuryItemConstants.ITEM_DEFAULT_PRICE);
        setPurchasedDate(new Date());
        setItemImage(null);

        mDataBaseID = -1;
        mUniqueID = ItemUtil.GenerateItemUniqueID(mItemName, mItemType);
        mExtraData = new TreeMap<String, String>();
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public void setUniqueID(String uniqueID) {
        if (Util.isValidString(uniqueID)) {
            this.mUniqueID = mUniqueID;
        }
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        if (Util.isValidString(itemName)) {
            this.mItemName = itemName;
            mUniqueID = ItemUtil.GenerateItemUniqueID(mItemName, mItemType);
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

    public Image getItemImage() {
        return mItemImage;
    }

    public void setItemImage(Image itemImage) {
        this.mItemImage = itemImage;
    }

    public LuxuryItemConstants.LuxuryType getItemType() {
        return mItemType;
    }

    public void setItemType(LuxuryItemConstants.LuxuryType itemType) {
        this.mItemType = itemType;
        mUniqueID = ItemUtil.GenerateItemUniqueID(mItemName, mItemType);
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

    public long getDataBaseID() {
        return mDataBaseID;
    }

    public void setDataBaseID(long mDataBaseID) {
        this.mDataBaseID = mDataBaseID;
    }
}
