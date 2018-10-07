package com.example.pandia.luxury.io;

import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.interfaces.Readable;
import com.example.pandia.luxury.interfaces.Writable;

import java.util.ArrayList;
import java.util.Collection;

public class LuxuryItemIO {
    public static void writeLuxuryData(Collection<LuxuryItem> allData, Writable<LuxuryItem> writer) {
        if (writer == null) {
            return;
        }

        writer.initializeWriter();

        for (LuxuryItem item : allData) {
            writer.writeEntry(item);
        }

        writer.finishWriter();
    }

    // dynamically loading ?
    public static ArrayList<LuxuryItem> readAllLuxuryData(Readable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }
        return readRangeLuxuryData(0, reader.entrySize(), reader);
    }

    public static ArrayList<LuxuryItem> readRangeLuxuryData(long start, long end, Readable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }

        if (end < start) {
            return null;
        }

        ArrayList<LuxuryItem> allData = new ArrayList<LuxuryItem>();
        reader.initializeReader();

        for (long i = start; i < end; ++i) {
            if (reader.isInBound(i)) {
                allData.add(reader.readEntry(i));
            }
        }

        reader.finishReader();

        return allData;
    }

    public static LuxuryItem readOneLuxuryData(int i, Readable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }

        LuxuryItem data = null;
        reader.initializeReader();

        if (reader.isInBound(i)) {
            data = reader.readEntry(i);
        }

        reader.finishReader();
        return data;
    }
}
