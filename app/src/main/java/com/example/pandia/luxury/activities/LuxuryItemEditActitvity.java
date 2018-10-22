package com.example.pandia.luxury.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.configs.Config;
import com.example.pandia.luxury.constants.LuxuryItemConstants;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditView;
import com.example.pandia.luxury.models.LuxuryItemEditModel;
import com.example.pandia.luxury.presenters.LuxuryItemEditPresenter;
import com.example.pandia.luxury.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.BUNDLE_UNIQUEID_KEY;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_SUBTYPE_STRING_KEY;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.LUXURY_TYPE_DISPLAY_NAME;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_KEY_IDENTIFIER;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.SUBCATEGORY_VALUE_IDENTIFIER;

public class LuxuryItemEditActitvity extends AppCompatActivity implements ILuxuryItemEditView {

    private static final int GALLERY = 201;
    private static final int CAMERA = 202;
    private TextView mMetaHeader;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private Spinner mTypeSpinner;
    private LinearLayout mSubTypeHeaderLayout;
    private LinearLayout mSubTypeColumnLayout;
    private Spinner mSubTypeSpinner;
    private EditText mPurchasedEditText;
    private Button mAddMetaButton;
    private ImageView mItemImage;
    private LinearLayout mRootLayout;

    private ArrayList<LinearLayout> mMetaDataLayouts;

    private ILuxuryItemEditModel mLuxuryItemEditModel;
    private ILuxuryItemEditPresenter mLuxuryItemEditPresenter;

    private boolean mIsNewData;

    private ArrayAdapter<String> mTypeSpinnerAdapter;

    private List<String> mSubTypeItemList;
    private ArrayAdapter<String> mSubTypeSpinnerAdapter;

    private Bitmap mOriginSizeItemImage;

    private static final int REQUEST_CAMERA = 200;
    private static final int REQUEST_READEXT = 300;
    private static final int REQUEST_WRITEEXT = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_luxury_item_actitvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        mMetaHeader = findViewById(R.id.EditContent_MetaHeader);
        mNameEditText = findViewById(R.id.EditContent_NameText);
        mPriceEditText = findViewById(R.id.EditContent_PriceText);
        mTypeSpinner = findViewById(R.id.EditContent_TypeSpinner);
        mSubTypeSpinner = findViewById(R.id.EditContent_SubTypeSpinner);
        mSubTypeHeaderLayout = findViewById(R.id.EditContent_SubTypeHeader);
        mSubTypeColumnLayout = findViewById(R.id.EditContent_SubTypeLayout);
        mPurchasedEditText = findViewById(R.id.EditContent_PurchasedDateText);
        mAddMetaButton = findViewById(R.id.EditContent_AddMetaButton);
        mItemImage = findViewById(R.id.EditContent_itemImage);
        mRootLayout = findViewById(R.id.EditContent_root_layout);

        mMetaDataLayouts = new ArrayList<LinearLayout>();
        mIsNewData = false;

        mAddMetaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LuxuryItemEditActitvity.this);
                builder.setTitle("Title");

                LinearLayout layout = new LinearLayout(LuxuryItemEditActitvity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1);

                final EditText keyInput = new EditText(LuxuryItemEditActitvity.this);
                keyInput.setHint("Key");
                keyInput.setLayoutParams(layoutParams);

                final EditText valueInput = new EditText(LuxuryItemEditActitvity.this);
                valueInput.setHint("Value");
                valueInput.setLayoutParams(layoutParams);

                layout.addView(keyInput);
                layout.addView(valueInput);
                builder.setView(layout);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String key = keyInput.getText().toString();
                        String value = valueInput.getText().toString();

                        if (key == null || key.isEmpty() || value == null || value.isEmpty()) {
                            return;
                        }

                        if (key.startsWith(SUBCATEGORY_KEY_IDENTIFIER)) {
                            Toast.makeText(LuxuryItemEditActitvity.this, "Invalid key.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (value.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
                            Toast.makeText(LuxuryItemEditActitvity.this, "Invalid value.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        LinearLayout layout = generateMetaDataItem(key, value);
                        int index = mRootLayout.indexOfChild(mAddMetaButton);
                        mRootLayout.addView(layout, index);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        String uniqueID = "";
        if (this.getIntent() != null && this.getIntent().getExtras() != null) {
            uniqueID = this.getIntent().getExtras().getString(BUNDLE_UNIQUEID_KEY);
        }

        if (uniqueID == null || uniqueID.isEmpty()) {
            mIsNewData = true;
        }

        mItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                showPictureDialog();
            }
        });

        mLuxuryItemEditPresenter = new LuxuryItemEditPresenter();
        mLuxuryItemEditModel = LuxuryItemEditModel.createLuxuryItemDetailModel(getApplicationContext(),
                Config.DEFAULT_DATA_IO, mLuxuryItemEditPresenter, uniqueID);

        mLuxuryItemEditPresenter.setModel(mLuxuryItemEditModel);
        mLuxuryItemEditPresenter.setView(this);
        mSubTypeItemList = new ArrayList<String>();

        mOriginSizeItemImage = null;

        initTypeSpinner();
        initSubTypeSpinner();

        mLuxuryItemEditModel.initialize();
    }

    private void setSubTypeVisibility(boolean visible) {
        int visibility = (visible) ? View.VISIBLE : View.GONE;
        mSubTypeColumnLayout.setVisibility(visibility);
        mSubTypeHeaderLayout.setVisibility(visibility);
    }

    private void initTypeSpinner() {
        String [] typeArray = LUXURY_TYPE_DISPLAY_NAME.values().toArray(new String[LUXURY_TYPE_DISPLAY_NAME.values().size()]);
        mTypeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typeArray);
        mTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(mTypeSpinnerAdapter);
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (LuxuryItemConstants.LuxuryType type : LUXURY_TYPE_DISPLAY_NAME.keySet()) {
                    if (LUXURY_TYPE_DISPLAY_NAME.get(type).equals(mTypeSpinnerAdapter.getItem(position))) {
                        updateSubTypeSpinner(type);
                        //mLuxuryItemEditPresenter.setItemType(type);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTypeSpinnerAdapter.notifyDataSetChanged();
    }

    private void initSubTypeSpinner() {
        mSubTypeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mSubTypeItemList);
        mSubTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSubTypeSpinner.setAdapter(mSubTypeSpinnerAdapter);
        mSubTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSubTypeSpinnerAdapter.notifyDataSetChanged();
    }

    private void updateSubTypeSpinner(LuxuryItemConstants.LuxuryType itemType) {
        if (LUXURY_SUBTYPE_STRING_KEY.containsKey(itemType)) {
            HashSet<String> items = LUXURY_SUBTYPE_DISPLAY_NAME.get(LUXURY_SUBTYPE_STRING_KEY.get(itemType));
            mSubTypeSpinnerAdapter.clear();

            for (String item : items) {
                if (item.startsWith(SUBCATEGORY_VALUE_IDENTIFIER)) {
                    item = item.substring(SUBCATEGORY_VALUE_IDENTIFIER.length());
                }
                mSubTypeSpinnerAdapter.add(item);
            }

            setSubTypeVisibility(true);
            mSubTypeSpinnerAdapter.notifyDataSetChanged();
        }
        else {
            setSubTypeVisibility(false);
            mSubTypeSpinnerAdapter.clear();
            mSubTypeSpinnerAdapter.notifyDataSetChanged();
        }
    }

    private void saveData() {
        mLuxuryItemEditPresenter.setItemName(mNameEditText.getText().toString());
        mLuxuryItemEditPresenter.setItemImage(mOriginSizeItemImage);
        mLuxuryItemEditPresenter.setPrice(Integer.parseInt(mPriceEditText.getText().toString()));
        mLuxuryItemEditPresenter.setPurchasedDate(Util.convertStringToDate(mPurchasedEditText.getText().toString()));

        for (LuxuryItemConstants.LuxuryType type : LUXURY_TYPE_DISPLAY_NAME.keySet()) {
            String selectedText = mTypeSpinnerAdapter.getItem(mTypeSpinner.getSelectedItemPosition());
            if (LUXURY_TYPE_DISPLAY_NAME.get(type).equals(selectedText)) {
                mLuxuryItemEditPresenter.setItemType(type);

                if (LUXURY_SUBTYPE_STRING_KEY.containsKey(type)) {
                    String key = LUXURY_SUBTYPE_STRING_KEY.get(type);
                    String selectedSubText = mSubTypeSpinnerAdapter.getItem(mSubTypeSpinner.getSelectedItemPosition());
                    String value = Util.generateSubTypeModelString(selectedSubText);

                    if (LUXURY_SUBTYPE_DISPLAY_NAME.get(key).contains(value)) {
                        mLuxuryItemEditPresenter.addExtraData(key, value);
                    }
                }

                break;
            }
        }

        for (LinearLayout layout : mMetaDataLayouts) {
            if (layout.getChildCount() == 3) {
                TextView keyTextView = (TextView) layout.getChildAt(0);
                EditText valueEditText = (EditText) layout.getChildAt(1);
                String key = keyTextView.getText().toString();
                String value = valueEditText.getText().toString();
                mLuxuryItemEditPresenter.addExtraData(key, value);
            }
        }

        mLuxuryItemEditPresenter.saveLuxuryItem();
    }

    @Override
    public void updateEditView() {
        mNameEditText.setText(mLuxuryItemEditPresenter.getItemName());
        mPriceEditText.setText(String.valueOf(mLuxuryItemEditPresenter.getPrice()));
        mItemImage.setImageBitmap(Util.decodeSampledBitmapFromResource(getResources(), R.drawable.b777, 0, 600));
        //TODO: Enable here.
        //mItemImage.setImageBitmap(Util.downScaleBitmap(mLuxuryItemEditModel.getItemImage(), 0, 600));
        mTypeSpinner.setSelection(mTypeSpinnerAdapter.getPosition(mLuxuryItemEditPresenter.getItemType()));
        mPurchasedEditText.setText(mLuxuryItemEditPresenter.getPurchasedDate());

        if (mLuxuryItemEditPresenter.isItemHasSubType()) {
            //TODO: Do we need to regenerate sub item menu here? Set selection trigger callback?
            mSubTypeSpinner.setSelection(mSubTypeSpinnerAdapter.getPosition(mLuxuryItemEditPresenter.getItemSubType()));
        }

        TreeMap<String, String> extraData = mLuxuryItemEditPresenter.getExtraData();

        if (!extraData.isEmpty()) {
            mMetaHeader.setVisibility(View.VISIBLE);
            for (String key: extraData.keySet()) {
                LinearLayout layout = generateMetaDataItem(key, extraData.get(key));
                int index = mRootLayout.indexOfChild(mAddMetaButton);
                mRootLayout.addView(layout, index);
            }
        }
        else {
            mMetaHeader.setVisibility(View.GONE);

            for (LinearLayout layout : mMetaDataLayouts) {
                mRootLayout.removeView(layout);
            }
            mMetaDataLayouts.clear();
        }
    }

    private LinearLayout generateMetaDataItem(String key, String value) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams rootLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(rootLayoutParams);

        LinearLayout.LayoutParams keyLayoutParams = new LinearLayout.LayoutParams(50, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams valueLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 3);
        TextView keyView = new TextView(this);
        keyView.setLayoutParams(keyLayoutParams);
        keyView.setText("  " + key);
        keyView.setTextColor(ContextCompat.getColor(this, R.color.black));
        keyView.setTextSize(COMPLEX_UNIT_SP, 16);

        EditText valueView = new EditText(this);
        valueView.setLayoutParams(valueLayoutParams);
        valueView.setText(value);
        valueView.setTextSize(COMPLEX_UNIT_SP, 24);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1);

        Button deleteButton = new Button(this);
        deleteButton.setLayoutParams(buttonLayoutParams);
        deleteButton.setText("Delete");
        deleteButton.setTextSize(COMPLEX_UNIT_SP, 24);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootLayout.removeView(layout);
                mMetaDataLayouts.remove(layout);
            }
        });

        layout.addView(keyView);
        layout.addView(valueView);
        layout.addView(deleteButton);

        mMetaDataLayouts.add(layout);

        return layout;
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
        //TODO: This will not grab full size picture
        // https://stackoverflow.com/questions/3442462/how-to-capture-an-image-and-store-it-with-the-native-android-camera
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(LuxuryItemEditActitvity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LuxuryItemEditActitvity.this,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this, new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
            else {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this,
                        new String[]{Manifest.permission.CAMERA},
                        REQUEST_CAMERA);
            }
        }

        if (ContextCompat.checkSelfPermission(LuxuryItemEditActitvity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LuxuryItemEditActitvity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READEXT);
            }
            else {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READEXT);
            }
        }

        if (ContextCompat.checkSelfPermission(LuxuryItemEditActitvity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LuxuryItemEditActitvity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITEEXT);
            }
            else {
                ActivityCompat.requestPermissions(LuxuryItemEditActitvity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITEEXT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    mOriginSizeItemImage = bitmap;
                    mItemImage.setImageBitmap(Util.downScaleBitmap(mOriginSizeItemImage, 0, 600));

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(LuxuryItemEditActitvity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            mOriginSizeItemImage = thumbnail;
            mItemImage.setImageBitmap(Util.downScaleBitmap(mOriginSizeItemImage, 0, 600));
            Toast.makeText(LuxuryItemEditActitvity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }
/*
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/
}
