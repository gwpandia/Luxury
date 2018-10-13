package com.example.pandia.luxury.io;

import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.io.interfaces.IRangeReadable;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;

import java.util.ArrayList;
import java.util.Collection;

public class LuxuryItemIO {
    public static void writeLuxuryData(Collection<LuxuryItem> allData, IWritable<LuxuryItem> writer) {
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
    public static ArrayList<LuxuryItem> readAllLuxuryData(IRangeReadable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }
        return readRangeLuxuryData(0, reader.entrySize(), reader);
    }

    public static ArrayList<LuxuryItem> readRangeLuxuryData(long start, long end, IRangeReadable<LuxuryItem> reader) {
        if (reader == null || end < start) {
            return null;
        }

        ArrayList<LuxuryItem> allData = new ArrayList<LuxuryItem>();
        reader.initializeReader();
        allData.addAll(reader.readRangeEntries(start, end));
        reader.finishReader();

        return allData;
    }

    public static LuxuryItem readOneLuxuryData(long i, IReadable<LuxuryItem> reader) {
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
