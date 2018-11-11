package com.example.pandia.luxury.interfaces;

import android.graphics.Bitmap;

import com.example.pandia.luxury.constants.LuxuryItemConstants;

import java.util.Date;
import java.util.TreeMap;

public interface ILuxuryItemEditPresenter {
    public void setModel(ILuxuryItemEditModel model);
    public void setView(ILuxuryItemEditView view);

    public void updateEditView();
    public void notifyItemSaved();

    public void setItemImage(Bitmap bitmap);
    public void setItemName(String itemName);
    public void setPrice(int price);
    public void setPurchasedDate(Date date);
    public void setItemType(LuxuryItemConstants.LuxuryType type);
    public void setItemSubType(String subType);
    public void clearExtraData();
    public void addExtraData(String key, String value);
    public void removeExtraData(String key);
    public void saveLuxuryItem(boolean updateImage, String contentRootPath);

    public Bitmap getItemImage();
    public String getItemName();
    public int getPrice();
    public String getUniqueID();
    public String getPurchasedDate();
    public String getItemType();
    public boolean isItemHasSubType();
    public String getItemSubType();
    public TreeMap<String, String> getExtraData();
}
