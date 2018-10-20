package com.example.pandia.luxury.interfaces;

import android.graphics.Bitmap;

import com.example.pandia.luxury.constants.LuxuryItemConstants;

import java.util.TreeSet;

public interface ILuxuryItemDetailModel {
    public void initialize();
    public Bitmap getTopImage();
    public String getLayoutTitle();
    public String getItemName();
    public int getPrice();
    public String getUniqueID();
    public String getPurchasedDate();
    public String getItemType();
    public boolean isItemHasSubType();
    public String getItemSubType();
    public TreeSet<String> getExtraDataKey();
    public String getExtraDataValue(String key);
}
