package com.example.pandia.luxury.interfaces;

import android.graphics.Bitmap;

import com.example.pandia.luxury.constants.LuxuryItemConstants;

import java.util.Date;
import java.util.TreeSet;

public interface ILuxuryItemEditModel {
    public void initialize();

    public void setItemImage(Bitmap bitmap);
    public void setItemName(String itemName);
    public void setPrice(int price);
    public void setPurchasedDate(Date date);
    public void setItemType(LuxuryItemConstants.LuxuryType type);
    public void setItemSubType(String subType);
    public void addExtraData(String key, String value);
    public void removeExtraData(String key);
    public void saveLuxuryItem();
}
