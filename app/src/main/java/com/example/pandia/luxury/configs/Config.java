package com.example.pandia.luxury.configs;

import com.example.pandia.luxury.constants.Constants;

public class Config {
    public static final Constants.DataIO DEFAULT_DATA_IO = Constants.DataIO.SQLITE;
    private static final int DEFAULT_ITEM_LOAD_PER_TIME = 20;

    public static final String DEFAULT_IMAGE_EXTENSION = "jpg";

    public static final String DEFAULT_IMAGE_NAME = "_DefLuxItem." + DEFAULT_IMAGE_EXTENSION;

    public static final String ROOT_DIR_NAME = "Luxury";
    public static final String LUXURY_ITEM_IMAGE_DIR_NAME = "item_images";
    public static final String DATABASE_DIR_NAME = "database";

    public static final String [] ALL_SUB_DIRS = {LUXURY_ITEM_IMAGE_DIR_NAME, DATABASE_DIR_NAME};
}
