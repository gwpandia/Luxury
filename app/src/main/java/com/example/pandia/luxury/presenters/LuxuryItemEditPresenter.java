package com.example.pandia.luxury.presenters;

import android.graphics.Bitmap;

import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditView;

import java.util.Date;

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
}
