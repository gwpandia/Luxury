package com.example.pandia.luxury.data;

import android.media.Image;
import com.example.pandia.luxury.util.ItemUtil;
import com.example.pandia.luxury.util.Util;

import java.util.Date;
import java.util.TreeMap;


public class LuxuryItem {

    enum LuxuryType {
        OTHER_TYPE,
        MODEL,
        LEGO,
        BOOK,
        GAME_CONSOLE,
        GAME_ACCESSORY,
        GAME,
        EARPHONE,
        COMPUTER,
        PHONE,
        CAMERA,
        LENS
    }

    public String mUniqueID;
    public String mItemName;
    public int mPrice;
    public Date mPurchasedDate;
    public Image mItemImage;
    public LuxuryType mItemType;
    public TreeMap<String, String> mExtraData;

    public LuxuryItem(String itemName) {
        mItemName = itemName;
        mUniqueID = ItemUtil.GenerateItemUniqueID(itemName);
        mPrice = LuxuryItemConstants.ITEM_DEFAULT_PRICE;
        mPurchasedDate = new Date();
        mItemImage = null;
        mItemType = LuxuryType.OTHER_TYPE;

        mExtraData = new TreeMap<String, String>();
    }

    public String getUniqueID() {
        return mUniqueID;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        this.mItemName = itemName;
        mUniqueID = ItemUtil.GenerateItemUniqueID(mItemName);
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        if (price > 0) {
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

    public LuxuryType getmItemType() {
        return mItemType;
    }

    public void setmItemType(LuxuryType mItemType) {
        this.mItemType = mItemType;
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
}
