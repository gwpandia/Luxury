package com.example.pandia.luxury.interfaces;

import android.graphics.Bitmap;
import java.util.TreeMap;

public interface ILuxuryItemDetailPresenter {
    public void setModel(ILuxuryItemDetailModel model);
    public void setView(ILuxuryItemDetailView view);
    public void updateDetailView();

    public Bitmap getTopImage();
    public String getLayoutTitle();
    public String getItemName();
    public int getPrice();
    public String getUniqueID();
    public String getPurchasedDate();
    public String getItemType();
    public boolean isItemHasSubType();
    public String getItemSubType();
    public TreeMap<String, String> getExtraData();
}
