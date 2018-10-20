package com.example.pandia.luxury.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.pandia.luxury.R;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ImageButton mAddButton;
    private ImageButton mViewButton;
    private ImageButton mBorrowButton;
    private ImageButton mRestoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAddButton = findViewById(R.id.MainActivity_AddButton);
        mAddButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, QRCodeScannerActivity.class);
            MainActivity.this.startActivity(intent);
        });

        mViewButton = findViewById(R.id.MainActivity_ViewButton);
        mViewButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, LuxuryListViewActivity.class);
            MainActivity.this.startActivity(intent);
        });

        mBorrowButton = findViewById(R.id.MainActivity_BorrowButton);
        mBorrowButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, QRCodeEncodeActivity.class);
            MainActivity.this.startActivity(intent);
        });

        mRestoreButton = findViewById(R.id.MainActivity_RestoreButton);
        mRestoreButton.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, LuxuryItemDetailViewActivity.class);
            MainActivity.this.startActivity(intent);
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
