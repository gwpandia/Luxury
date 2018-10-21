package com.example.pandia.luxury.presenters;

import android.graphics.Bitmap;

import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditView;

import java.util.Date;
import java.util.TreeMap;
import java.util.TreeSet;

public class LuxuryItemEditPresenter implements ILuxuryItemEditPresenter {

    private ILuxuryItemEditModel mModel;
    private ILuxuryItemEditView mView;

    public LuxuryItemEditPresenter() {
        mModel = null;
        mView = null;
    }

    @Override
    public void setModel(ILuxuryItemEditModel model) {
        mModel = model;
    }

    @Override
    public void setView(ILuxuryItemEditView view) {
        mView = view;
    }

    @Override
    public void updateEditView() {
        mView.updateEditView();
    }

    @Override
    public void setItemImage(Bitmap bitmap) {
        mModel.setItemImage(bitmap);
    }

    @Override
    public void setItemName(String itemName) {
        mModel.setItemName(itemName);
    }

    @Override
    public void setPrice(int price) {
        mModel.setPrice(price);
    }

    @Override
    public void setPurchasedDate(Date date) {
        mModel.setPurchasedDate(date);
    }

    @Override
    public void setItemType(LuxuryItemConstants.LuxuryType type) {
        mModel.setItemType(type);
    }

    @Override
    public void setItemSubType(String subType) {
        mModel.setItemSubType(subType);
    }

    @Override
    public void addExtraData(String key, String value) {
        mModel.addExtraData(key, value);
    }

    @Override
    public void removeExtraData(String key) {
        mModel.removeExtraData(key);
    }

    @Override
    public void saveLuxuryItem() {
        mModel.saveLuxuryItem();
    }

    @Override
    public Bitmap getItemImage() {
        if (mModel == null) {
            return null;
        }
        return mModel.getItemImage();
    }

    @Override
    public String getItemName() {
        if (mModel == null) {
            return null;
        }
        return mModel.getItemName();
    }

    @Override
    public int getPrice() {
        if (mModel == null) {
            return 0;
        }
        return mModel.getPrice();
    }

    @Override
    public String getUniqueID() {
        if (mModel == null) {
            return null;
        }
        return mModel.getUniqueID();
    }

    @Override
    public String getPurchasedDate() {
        if (mModel == null) {
            return null;
        }
        return mModel.getPurchasedDate();
    }

    @Override
    public String getItemType() {
        if (mModel == null) {
            return null;
        }
        return mModel.getItemType();
    }

    @Override
    public boolean isItemHasSubType() {
        if (mModel == null) {
            return false;
        }
        return mModel.isItemHasSubType();
    }

    @Override
    public String getItemSubType() {
        if (mModel == null) {
            return null;
        }
        return mModel.getItemSubType();
    }

    @Override
    public TreeMap<String, String> getExtraData() {
        TreeSet<String> keys = mModel.getExtraDataKey();
        TreeMap<String, String> retMap = new TreeMap<String, String>();
        for (String key: keys) {
            String value = mModel.getExtraDataValue(key);
            if (value != null) {
                retMap.put(key, value);
            }
        }
        return retMap;
    }
}
