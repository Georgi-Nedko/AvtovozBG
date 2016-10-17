package com.example.xcomputers.avtovozbg;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SeePhotosOnFullScreenActivity extends AppCompatActivity {
    private HorizontalScrollView hsv;
    private LinearLayout hsvLL;
    private int height;
    private int width;
    private ImageView firstImage;
    int counter = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_photos_on_full_screen);
        hsv = (HorizontalScrollView) findViewById(R.id.hsv);
        hsvLL = (LinearLayout) findViewById(R.id.hsvLL);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        try {
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra("car"));

            for (int i = 0; i < jsonArray.length(); i++) {
                String temp = "http://avtovoz.hopto.org/";
                String address = temp + jsonArray.getString(i);
                new ImageDownloaderTask().execute(address);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            counter++;
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            if (bitmap != null) {

                ImageView newImageView = new ImageView(SeePhotosOnFullScreenActivity.this);
                newImageView.setImageBitmap(bitmap);
                hsvLL.addView(newImageView);

                newImageView.requestLayout();
                newImageView.getLayoutParams().height = (int) (height / 1.05);
                newImageView.getLayoutParams().width = width;
                newImageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }


        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != 200) {
                    return null;
                }
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
