package com.example.pandia.luxury.io;

import com.example.pandia.luxury.data.BorrowItem;
import com.example.pandia.luxury.interfaces.Readable;
import com.example.pandia.luxury.interfaces.Writable;

public class BorrowItemSQLiteIO  implements Writable<BorrowItem>, Readable<BorrowItem> {
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
    public BorrowItem readEntry(int i) {
        return null;
    }

    @Override
    public void finishReader() {

    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(BorrowItem entry) {

    }

    @Override
    public int writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {

    }
}
