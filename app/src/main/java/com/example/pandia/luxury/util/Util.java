package com.example.pandia.luxury.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Util {
    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    public static Date convertStringToDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertToDateString(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDate = sdf.format(date);
        return currentDate;
    }

    public static String getCurrentDateString(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd,HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String currentDate = dtf.format(now);
        return currentDate;
    }

    public static String hashText(String plainText) {
        byte [] secretbytes = {0x23, 0x40, 0x6c, 0x75, 0x78, 0x75, 0x72, 0x79,
                               0x61, 0x69, 0x64, 0x6e, 0x61, 0x70, 0x33, 0x31};

        return generateHashValue(plainText, new String(secretbytes), "SHA-256");
    }

    public static String hashCredential(String plainText, String salt) {
        byte [] secretbytes = {0x23, 0x40, 0x6c, 0x75, 0x78, 0x75, 0x72, 0x79,
                               0x61, 0x69, 0x64, 0x6e, 0x61, 0x70, 0x33, 0x31};

        return generateHashValue(plainText, salt + new String(secretbytes), "SHA-512");
    }

    private static String generateHashValue(String plainText, String salt, String encodeMethod){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance(encodeMethod);
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public static String convertMapToJSonString(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        return new JSONObject(map).toString();
    }

    public static TreeMap<String, String> convertJSonStringToMap(String json) {
        TreeMap<String, String> theMap = null;
        if (json == null) {
            return null;
        }
        try {
            theMap = jsonToMap(new JSONObject(json));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return theMap;
    }

    private static TreeMap<String, String> jsonToMap(JSONObject json) throws JSONException {
        TreeMap<String, String> retMap = new TreeMap<String, String>();

        if(json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    private static TreeMap<String, String> toMap(JSONObject object) throws JSONException {
        TreeMap<String, String> map = new TreeMap<String, String>();

        Iterator<String> keysItr = object.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }

            if (value instanceof String) {
                map.put(key, (String) value);
            }
            else {
                assert(false);
            }
        }
        return map;
    }

    private static List<String> toList(JSONArray array) throws JSONException {
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if(value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if(value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }

            if (value instanceof String) {
                list.add((String) value);
            }
            else {
                assert(false);
            }
        }
        return list;
    }
}
