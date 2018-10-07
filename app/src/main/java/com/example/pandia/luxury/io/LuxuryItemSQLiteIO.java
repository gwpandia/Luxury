package com.example.pandia.luxury.io;

import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.Writable;
import com.example.pandia.luxury.interfaces.Readable;

public class LuxuryItemSQLiteIO implements Writable<LuxuryItem>, Readable<LuxuryItem> {

    private SQLiteHandler sqLiteHandler;

    @Override
    public void initializeReader() {

    }

    @Override
    public int entrySize() {
        return 0;
    }

    @Override
    public boolean isInBound(int i) {
        return i >= 0 && i < entrySize();
    }

    @Override
    public LuxuryItem readEntry(int i) {
        return null;
    }

    @Override
    public void finishReader() {

    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(LuxuryItem entry) {

    }

    @Override
    public int writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {

    }
}
