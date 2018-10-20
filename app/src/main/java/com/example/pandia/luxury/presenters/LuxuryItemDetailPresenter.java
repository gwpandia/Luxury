package com.example.pandia.luxury.presenters;

import android.graphics.Bitmap;

import com.example.pandia.luxury.interfaces.ILuxuryItemDetailModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailView;

import java.util.TreeMap;
import java.util.TreeSet;

public class LuxuryItemDetailPresenter implements ILuxuryItemDetailPresenter {
    private ILuxuryItemDetailModel mModel;
    private ILuxuryItemDetailView mView;

    public LuxuryItemDetailPresenter() {
        mModel = null;
        mView = null;
    }

    @Override
    public void setModel(ILuxuryItemDetailModel model) {
        mModel = model;
    }

    @Override
    public void setView(ILuxuryItemDetailView view) {
        mView = view;
    }

    @Override
    public void updateDetailView() {
        if (mView != null) {
            mView.updateDetailView();
        }
    }

    @Override
    public Bitmap getTopImage() {
        if (mModel == null) {
            return null;
        }
        return mModel.getTopImage();
    }

    @Override
    public String getLayoutTitle() {
        if (mModel == null) {
            return null;
        }
        return mModel.getLayoutTitle();
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
