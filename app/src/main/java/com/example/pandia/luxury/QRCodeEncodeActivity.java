package com.example.pandia.luxury;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeEncodeActivity extends AppCompatActivity {

    private Button mEncodeButton;
    private EditText mPlainText;
    private ImageView mPreviewImage;
    private EncodingTask mEncodeTask;

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

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
                mBitmap = encodeImage(mText);
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

    private static Bitmap encodeImage(String plainText) {
        Bitmap bitmap = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(plainText, BarcodeFormat.QR_CODE,200,200);
            bitmap = createBitmap(bitMatrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private static Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
