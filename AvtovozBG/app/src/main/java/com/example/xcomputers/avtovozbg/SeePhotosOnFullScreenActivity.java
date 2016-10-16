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

        firstImage = new ImageView(SeePhotosOnFullScreenActivity.this);
        firstImage.setImageBitmap((Bitmap) getIntent().getParcelableExtra("carImage"));
        hsvLL.addView(firstImage);
        firstImage.requestLayout();
        firstImage.getLayoutParams().height = (int) (height/1.1);
        firstImage.getLayoutParams().width = width;
        firstImage.setScaleType(ImageView.ScaleType.FIT_XY);


        try {
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra("car"));

            for (int i = 1; i < jsonArray.length(); i++) {
                String temp = "http://avtovoz.hopto.org/";
                String address = temp + jsonArray.getString(i);
                new ImageDownloaderTask().execute(address);
            }

            Log.e("URLSARRAY", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            Log.e("IMAGESSVALQMSNIMKA", params[0]);
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
                //hsvLL.removeView(firstImage);
                hsvLL.addView(newImageView);

                newImageView.requestLayout();
                newImageView.getLayoutParams().height = (int) (height/1.1);
                newImageView.getLayoutParams().width = width;
                newImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                Log.e("IMAGESADD", bitmap.toString());
                //Log.e("IMAGES1", selectedCar.getImages().size() + "");
            } else {
                // Toast.makeText(SelectedPlaceActivity.this, "NO PHOTOS", Toast.LENGTH_SHORT).show();


                Log.e("IMAGESADD", bitmap.toString());
                // Log.e("IMAGES1", selectedCar.getImages().size() + "");
                //if (counter == 1) {
                //  hsvLL.removeViewAt(0);

                //  } else {
                // Toast.makeText(SelectedPlaceActivity.this, "NO PHOTOS", Toast.LENGTH_SHORT).show();

                // }
            }


        }


        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                Log.e("IMAGESTATUS", statusCode + "");
                if (statusCode != 200) {
                    Log.e("IMAGESTATUS!=200", statusCode + "");
                    return null;
                }
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.e("IMAGESBITMAP", bitmap + "");
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("IMAGESDownloader", "Error downloading image from " + url);
            } finally {
                Log.e("IMAGESFINALY", urlConnection + "");
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
            return null;
        }


    }
}
