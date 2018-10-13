package com.example.pandia.luxury.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.constants.LuxuryItemConstants.LuxuryType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ItemUtil {
    public static String generateItemUniqueID(String itemName, LuxuryType type, String createDate){
        String typeName = "UNKNOWN";
        if (LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.containsKey(type)) {
            typeName = LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.get(type);
        }

        //TODO: regenerate by time, may cause code inconsist, or this value shall be updated everytime
        return Util.hashText(itemName + typeName + createDate);
    }

    public static String generateBorrowUniqueID(String borrowerName, String itemID, String createDate){
        return Util.hashText(borrowerName + itemID + createDate);
    }

    public static byte [] convertBitmapToByteArray(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] x = stream.toByteArray();
            stream.close();
            return x;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap convertByteArrayToBitmap(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
