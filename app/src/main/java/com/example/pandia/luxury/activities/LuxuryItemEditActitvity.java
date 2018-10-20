package com.example.pandia.luxury.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.interfaces.ILuxuryItemEditView;

import java.util.ArrayList;

public class LuxuryItemEditActitvity extends AppCompatActivity implements ILuxuryItemEditView {

    private TextView mMetaHeader;
    private EditText mNameEditText;
    private EditText mPriceEditText;
    private EditText mTypeEditText;
    private EditText mPurchasedEditText;
    private Button mAddMetaButton;
    private ImageView mItemImage;
    private LinearLayout mRootLayout;

    private ArrayList<LinearLayout> mMetaDataLayouts;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mMetaHeader = findViewById(R.id.EditContent_MetaHeader);
        mNameEditText = findViewById(R.id.EditContent_NameText);
        mPriceEditText = findViewById(R.id.EditContent_PriceText);
        mTypeEditText = findViewById(R.id.EditContent_TypeText);
        mPurchasedEditText = findViewById(R.id.EditContent_PurchasedDateText);
        mAddMetaButton = findViewById(R.id.EditContent_AddMetaButton);
        mItemImage = findViewById(R.id.EditContent_itemImage);
        mRootLayout = findViewById(R.id.EditContent_root_layout);

        mMetaDataLayouts = new ArrayList<LinearLayout>();
    }

    @Override
    public void updateEditView() {

    }
}
