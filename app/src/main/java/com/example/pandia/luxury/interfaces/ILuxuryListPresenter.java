package com.example.pandia.luxury.interfaces;

public interface ILuxuryListPresenter {
    // init
    public void setModel(ILuxuryListModel model);
    public void setView(ILuxuryListView view);

    // view interaction
    public void onItemClicked(int row);
    public void onItemSwiped(int row);
    public void onListScrolledDown();
    public void onListScrolledUp();
    public void deleteLuxuryItem(String uniqueID);
    public void deleteLuxuryItem(long id);

    // for model callback
    public void onListUpdated();
    //public void onListContinueLoading();
}
