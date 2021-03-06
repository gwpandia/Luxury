package com.example.pandia.luxury.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.example.pandia.luxury.activities.LuxuryItemEditActivity;
import com.example.pandia.luxury.configs.Config;
import com.example.pandia.luxury.constants.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_VALUE_IDENTIFIER;

public class Util {
    private static final String DATE_FORMAT = "yyyy-MM-dd,HH:mm:ss";

    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    public static Date convertStringToDate(String string) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Date date = null;
        try {
            date = sdf.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String convertDateToString(Date date) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String currentDate = sdf.format(date);
        return currentDate;
    }

    public static LocalDateTime convertStringToLocalDateTime(String string) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDateTime.parse(string, dtf);
    }

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime){
        if (localDateTime == null) {
            return "";
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String currentDate = dtf.format(localDateTime);
        return currentDate;
    }

    public static String getCurrentDateString(){
        LocalDateTime now = LocalDateTime.now();
        return convertLocalDateTimeToString(now);
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

    public static String generateSubTypeItemDisplayName(String origin) {
        String ret = "";
        if (origin.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
            ret = origin.substring(SUBCATEGORY_VALUE_IDENTIFIER.length());
        }
        ret = ret.replace("_", " ");
        return ret;
    }

    public static String generateSubTypeModelString(String itemName) {
        String ret = "";
        ret = itemName.replace(" ", "_");
        if (!ret.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
            ret = SUBCATEGORY_VALUE_IDENTIFIER + ret;
        }
        return ret;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap downScaleBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
        if (true) return bitmap;
        if (reqWidth == 0 && reqHeight > 0) {
            reqWidth = Math.round((float)bitmap.getWidth() * ((float)reqHeight / (float)bitmap.getHeight()));
        }
        else if (reqHeight == 0 && reqWidth > 0) {
            reqHeight = Math.round((float)bitmap.getHeight() * ((float)reqWidth / (float)bitmap.getWidth()));
        }

        int inSample = calculateInSampleSize(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
        Bitmap convertedBitmap = Bitmap.createBitmap(bitmap.getWidth() / inSample,
                bitmap.getHeight() / inSample, bitmap.getConfig());
        Canvas canvas = new Canvas(convertedBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return convertedBitmap;
    }

    public static int calculateInSampleSize(int originWidth, int originHeight, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = originWidth;
        final int width = originHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        return calculateInSampleSize(options.outWidth, options.outHeight, reqWidth, reqHeight);
    }

    public static String getDirectory(Constants.DirectoryType type) {
        String path = "";
        path += Config.ROOT_DIR_NAME;
        path += File.separator;

        switch (type) {
            case LUXURY_IMAGE:
                path += Config.LUXURY_ITEM_IMAGE_DIR_NAME;
                break;
            case DATABASE:
                path += Config.DATABASE_DIR_NAME;
                break;
            default:
                return "";
        }
        return path;
    }

    public static void initExternalStorageStructure(Context context, String packageName) {
        String path = context.getExternalFilesDir(null) + File.separator;
        path += Config.ROOT_DIR_NAME;
        //Uri uri = FileProvider.getUriForFile(context, packageName, new File(path));
        //File rootDir = new File(uri.getPath());
        File rootDir = new File(path);

        if (!rootDir.exists()) {
            rootDir.mkdir();
        }

        for (String subDir : Config.ALL_SUB_DIRS) {
            File sub = new File(path, subDir);
            if (!sub.exists()) {
                sub.mkdir();
            }
        }
    }

    public static void requestPermission(Activity activity, String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity , permission)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
            else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        }
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    public static Bitmap getRotatedBitmap(Bitmap bitmap, String path) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        return rotateBitmap(bitmap, orientation);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
