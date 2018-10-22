package com.example.pandia.luxury.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.configs.Config;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailModel;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailPresenter;
import com.example.pandia.luxury.interfaces.ILuxuryItemDetailView;
import com.example.pandia.luxury.models.LuxuryItemDetailModel;
import com.example.pandia.luxury.presenters.LuxuryItemDetailPresenter;
import com.example.pandia.luxury.util.Util;

import java.util.ArrayList;
import java.util.TreeMap;

import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static com.example.pandia.luxury.constants.LuxuryItemConstants.BUNDLE_UNIQUEID_KEY;

public class LuxuryItemDetailViewActivity extends AppCompatActivity implements ILuxuryItemDetailView {

    private ImageView mTopImageView;
    private TextView mNameText;
    private TextView mPriceText;
    private TextView mTypeText;
    private TextView mPurchasedDateText;
    private CollapsingToolbarLayout mCollapsingLayout;
    private TextView mMetaHeader;
    private LinearLayout mContextRootLayout;

    private ILuxuryItemDetailModel mItemModel;
    private ILuxuryItemDetailPresenter mItemPresenter;

    private ArrayList<LinearLayout> mGeneratedExtraData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luxury_item_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LuxuryItemDetailViewActivity.this, LuxuryItemEditActitvity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_UNIQUEID_KEY, mItemPresenter.getUniqueID());
                intent.putExtras(bundle);
                LuxuryItemDetailViewActivity.this.startActivity(intent);
            }
        });

        mTopImageView = findViewById(R.id.LuxuryItemDetailViewActivity_top_image);
        mNameText = findViewById(R.id.Content_NameText);
        mPriceText = findViewById(R.id.Content_PriceText);
        mTypeText = findViewById(R.id.Content_TypeText);
        mPurchasedDateText = findViewById(R.id.Content_PurchasedDateText);
        mCollapsingLayout = findViewById(R.id.LuxuryItemDetailViewActivity_toolbar_layout);
        mMetaHeader = findViewById(R.id.Content_MetaHeader);
        mContextRootLayout = findViewById(R.id.Content_root_layout);
        mGeneratedExtraData = new ArrayList<LinearLayout>();

        String uniqueID = this.getIntent().getExtras().getString(BUNDLE_UNIQUEID_KEY);

        mItemPresenter = new LuxuryItemDetailPresenter();
        mItemModel = LuxuryItemDetailModel.createLuxuryItemDetailModel(getApplicationContext(), Config.DEFAULT_DATA_IO, mItemPresenter, uniqueID);
        mItemPresenter.setModel(mItemModel);
        mItemPresenter.setView(this);
        mItemModel.initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void updateDetailView() {
        mNameText.setText(mItemPresenter.getItemName());
        mPriceText.setText(String.valueOf(mItemPresenter.getPrice()));
        mPurchasedDateText.setText(mItemPresenter.getPurchasedDate());
        String typeText = mItemPresenter.getItemType();

        if (mItemPresenter.isItemHasSubType()) {
            typeText += " / " + mItemPresenter.getItemSubType();
        }
        mTypeText.setText(typeText);
        mCollapsingLayout.setTitle(mItemPresenter.getItemName());

        Bitmap bitmap = mItemPresenter.getTopImage();

        if (bitmap == null) {
            mTopImageView.setImageBitmap(
                    Util.decodeSampledBitmapFromResource(getResources(), R.drawable.b777, 0, 600));
        }

        TreeMap<String, String> extraData = mItemPresenter.getExtraData();

        if (!extraData.isEmpty()) {
            mMetaHeader.setVisibility(View.VISIBLE);

            for (String key : extraData.keySet()) {
                LinearLayout layout = generateMetaDataItem(key, extraData.get(key));
                mContextRootLayout.addView(layout);
            }

        }
        else {
            mMetaHeader.setVisibility(View.GONE);

            for (LinearLayout layout : mGeneratedExtraData) {
                mContextRootLayout.removeView(layout);
            }

            mGeneratedExtraData.clear();
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

        TextView valueView = new TextView(this);
        valueView.setLayoutParams(valueLayoutParams);
        valueView.setText(value);
        valueView.setTextSize(COMPLEX_UNIT_SP, 24);

        layout.addView(keyView);
        layout.addView(valueView);

        mGeneratedExtraData.add(layout);

        return layout;
    }
}
