package com.example.pandia.luxury.activities;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.pandia.luxury.R;
import com.example.pandia.luxury.util.QRCodeUtil;

public class QRCodeEncodeActivity extends AppCompatActivity {

    private Button mEncodeButton;
    private EditText mPlainText;
    private ImageView mPreviewImage;
    private EncodingTask mEncodeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_encode);

        mEncodeButton = findViewById(R.id.QRCodeEncodeActivity_encodeButton);
        mPlainText = findViewById(R.id.QRCodeEncodeActivity_plainText);
        mPreviewImage = findViewById(R.id.QRCodeEncodeActivity_previewImage);

        mEncodeButton.setOnClickListener((View v) -> {
            if (mEncodeTask != null) {
                return;
            }

            String text = mPlainText.getText().toString();
            mEncodeTask = new EncodingTask(text, mPreviewImage);
            mEncodeTask.execute((Void) null);
        });
        //mPreviewImage.setImageBitmap(bitmap);
    }

    public class EncodingTask extends AsyncTask<Void, Void, Boolean> {

        private String mText;
        private ImageView mImageView;
        private Bitmap mBitmap;

        public EncodingTask(String text, ImageView view) {
            mText = text;
            mImageView = view;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (mText != null) {
                Log.i("pandia", "doInBG: " + mText);
                mBitmap = QRCodeUtil.GenerateQRCodeImage(mText);
                if (mBitmap != null) {
                    return true;
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success && mImageView != null) {
                Log.i("pandia", "Setbitmap");
                mImageView.setImageBitmap(mBitmap);
            }
            mEncodeTask = null;
        }

        @Override
        protected void onCancelled() {
            mEncodeTask = null;
        }
    }
}
