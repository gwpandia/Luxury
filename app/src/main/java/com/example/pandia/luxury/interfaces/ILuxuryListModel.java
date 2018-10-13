package com.example.pandia.luxury.interfaces;

import com.example.pandia.luxury.data.LuxuryItem;

import java.util.ArrayList;

public interface ILuxuryListModel {
    public int luxuryItemsSize();
    public LuxuryItem getLuxuryItem(int index);
    public ArrayList<LuxuryItem> getLuxuryItems();
    public boolean loadLuxuryItems(long start, long end);
    public boolean loadAllLuxuryItems();
    public boolean addLuxuryItem(LuxuryItem luxuryItem);
    public boolean updateLuxuryItem(LuxuryItem luxuryItem);
    public boolean removeLuxuryItem(String uniqueID);
    public boolean removeLuxuryItem(long id);
}
