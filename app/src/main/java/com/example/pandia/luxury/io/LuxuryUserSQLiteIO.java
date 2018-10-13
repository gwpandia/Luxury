package com.example.pandia.luxury.io;

import com.example.pandia.luxury.data.LuxuryUser;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;

public class LuxuryUserSQLiteIO implements IWritable<LuxuryUser>, IReadable<LuxuryUser> {

    @Override
    public void initializeReader() {

    }

    @Override
    public long entrySize() {
        return 0;
    }

    @Override
    public boolean isInBound(long i) {
        return i >= 0 && i < entrySize();
    }

    @Override
    public LuxuryUser readEntry(long i) {
        return null;
    }

    @Override
    public void finishReader() {

    }

    @Override
    public void initializeWriter() {

    }

    @Override
    public void writeEntry(LuxuryUser entry) {

    }

    @Override
    public long writtenEntries() {
        return 0;
    }

    @Override
    public void finishWriter() {

    }
}
