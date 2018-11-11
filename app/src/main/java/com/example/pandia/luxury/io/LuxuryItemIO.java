package com.example.pandia.luxury.io;

import com.example.pandia.luxury.configs.Config;
import com.example.pandia.luxury.constants.Constants;
import com.example.pandia.luxury.data.LuxuryItem;
import com.example.pandia.luxury.io.interfaces.IRangeReadable;
import com.example.pandia.luxury.io.interfaces.IReadable;
import com.example.pandia.luxury.io.interfaces.IWritable;
import com.example.pandia.luxury.util.Util;

import java.io.File;
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

    public static void writeLuxuryData(LuxuryItem data, String contentRootPath, boolean updateImage, IWritable<LuxuryItem> writer) {
        if (writer == null) {
            return;
        }

        writer.initializeWriter();
        writer.writeEntry(data);
        writer.finishWriter();

        if (updateImage) {
            updateLuxuryItemImage(data, contentRootPath);
        }
    }

    public static void updateLuxuryData(long id, LuxuryItem data, IWritable<LuxuryItem> writer) {
        if (writer == null) {
            return;
        }

        writer.initializeWriter();
        writer.writeEntry(data);
        writer.finishWriter();
    }

    // dynamically loading ?
    public static ArrayList<LuxuryItem> readAllLuxuryData(IRangeReadable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }
        return readRangeLuxuryData(0, reader.entrySize(), reader, true);
    }

    public static ArrayList<LuxuryItem> readRangeLuxuryData(long start, long end, IRangeReadable<LuxuryItem> reader, boolean clearOld) {
        if (reader == null || end < start) {
            return null;
        }

        ArrayList<LuxuryItem> allData = new ArrayList<LuxuryItem>();
        reader.initializeReader();
        if (clearOld) {
            allData.clear();
        }
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

    public static LuxuryItem readOneLuxuryData(String uniqueID, IReadable<LuxuryItem> reader) {
        if (reader == null) {
            return null;
        }

        LuxuryItem data = null;
        reader.initializeReader();
        data = reader.readEntry(uniqueID);
        reader.finishReader();
        return data;
    }

    public static boolean deleteLuxuryItemData(String uniqueID, IWritable<LuxuryItem> writer) {
        if (writer == null) {
            return false;
        }
        writer.initializeWriter();
        writer.removeEntry(uniqueID);
        writer.finishWriter();
        return true;
    }

    public static boolean deleteLuxuryItemData(long id, IWritable<LuxuryItem> writer) {
        if (writer == null) {
            return false;
        }
        writer.initializeWriter();
        writer.removeEntry(id);
        writer.finishWriter();
        return true;
    }

    private static boolean updateLuxuryItemImage(LuxuryItem data, String contentRootPath) {
        String itemImgPath = contentRootPath + File.separator + Util.getDirectory(Constants.DirectoryType.LUXURY_IMAGE)
                + File.separator + data.getUniqueID() + "." + Config.DEFAULT_IMAGE_EXTENSION;

        String newImgPath = contentRootPath + File.separator + Util.getDirectory(Constants.DirectoryType.LUXURY_IMAGE)
                + File.separator + Config.DEFAULT_IMAGE_NAME;

        File newImgFile = new File(newImgPath);
        File targetImgFile = new File(itemImgPath);

        if (!newImgFile.exists()) {
            return false;
        }

        boolean ret = true;

        if (targetImgFile.exists()) {
            ret = ret && targetImgFile.delete();
        }

        if (!ret) {
            return ret;
        }

        ret = ret && newImgFile.renameTo(targetImgFile);

        if (data.isUniqueIDUpdated()) {
            String oldImgPath = contentRootPath + File.separator + Util.getDirectory(Constants.DirectoryType.LUXURY_IMAGE)
                    + File.separator + data.getOldUniqueID() + "." + Config.DEFAULT_IMAGE_EXTENSION;
            File oldImgFile = new File(oldImgPath);

            if (oldImgFile.exists()) {
                ret = ret && oldImgFile.delete();
            }
        }

        return ret;
    }
}
