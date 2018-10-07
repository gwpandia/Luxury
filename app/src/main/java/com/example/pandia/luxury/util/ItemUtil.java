package com.example.pandia.luxury.util;

import com.example.pandia.luxury.data.LuxuryItemConstants;
import com.example.pandia.luxury.data.LuxuryItemConstants.LuxuryType;

public class ItemUtil {
    public static String GenerateItemUniqueID(String itemName, LuxuryType type){
        String typeName = "UNKNOWN";
        if (LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.containsKey(type)) {
            typeName = LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME.get(type);
        }

        String currentDate = Util.getCurrentDateString();
        return Util.hashText(itemName + typeName + currentDate);
    }

    public static String GenerateBorrowUniqueID(String borrowerName, String itemID){
        String currentDate = Util.getCurrentDateString();
        return Util.hashText(borrowerName + itemID + currentDate);
    }
}
