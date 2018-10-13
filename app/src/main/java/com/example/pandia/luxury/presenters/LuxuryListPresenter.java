package com.example.pandia.luxury.presenters;

import com.example.pandia.luxury.interfaces.ILuxuryListModel;
import com.example.pandia.luxury.interfaces.ILuxuryListPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryListView;
import com.example.pandia.luxury.models.LuxuryListModel;

public class LuxuryListPresenter implements ILuxuryListPresenter {
    private ILuxuryListModel mModel;
    private ILuxuryListView mView;

    public LuxuryListPresenter() {
        mModel = null;
        mView = null;
    }

    @Override
    public void setModel(ILuxuryListModel model) {
        mModel = model;
    }

    @Override
    public void setView(ILuxuryListView view) {
        mView = view;
    }

    @Override
    public void onItemClicked(int row) {

    }

    @Override
    public void onItemSwiped(int row) {

    }

    @Override
    public void onListScrolledDown() {
        mModel.loadAllLuxuryItems();
    }

    @Override
    public void onListScrolledUp() {
        mModel.loadAllLuxuryItems();
    }

    @Override
    public void deleteLuxuryItem(String uniqueID) {
        mModel.removeLuxuryItem(uniqueID);
    }

    @Override
    public void deleteLuxuryItem(long id) {
        mModel.removeLuxuryItem(id);
    }

    @Override
    public void onListUpdated() {
        mView.updateListViewItems(mModel.getLuxuryItems());
    }
}
