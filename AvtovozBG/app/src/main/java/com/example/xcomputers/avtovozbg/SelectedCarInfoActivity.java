package com.example.xcomputers.avtovozbg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xcomputers.avtovozbg.model.Car;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SelectedCarInfoActivity extends AppCompatActivity {
    private Car selectedCar;
    private TextView modelAndBrand;
    private TextView horsePower;
    private TextView kilometers;
    private TextView producedIn;
    private TextView color;
    private TextView price;
    private TextView description;
    private HorizontalScrollView hsv;
    private ImageButton call;
    private LinearLayout hsvLL;
    int height;
    int width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SelectedCarInfoActivity.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_car_info);
        modelAndBrand = (TextView) findViewById(R.id.model_and_brand);
        horsePower = (TextView) findViewById(R.id.horse_power);
        kilometers = (TextView) findViewById(R.id.kilometers);
        producedIn = (TextView) findViewById(R.id.producedYear);
        color = (TextView) findViewById(R.id.color);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);
        hsv = (HorizontalScrollView) findViewById(R.id.scrollView);
        call = (ImageButton) findViewById(R.id.call_us);
        hsvLL = (LinearLayout) findViewById(R.id.scrollViewLL);

        selectedCar = getIntent().getParcelableExtra("selectedCar");
        modelAndBrand.setText(selectedCar.getModel() + "," + selectedCar.getBrand());
        horsePower.setText(selectedCar.getHorsePower() + "HP");
        kilometers.setText(selectedCar.getKilometers() + "");
        producedIn.setText(selectedCar.getYearOfManufacture() + "");
        price.setText(" costs -> " + selectedCar.getPrice() + "$");
        color.setText(selectedCar.getColor());
        description.setText(selectedCar.getDescription());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        String urls = selectedCar.getImageUrls();


        try {
//            JSONObject obj = new JSONObject(urls);
            JSONArray jsonArray = new JSONArray(urls);
            for(int i = 0; i<jsonArray.length();i++){
                String address = jsonArray.getString(i);
                new ImageDownloaderTask().execute(address);
            }


            Log.e("URLSARRAY",jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Log.e("URLS",selectedCar.getImageUrls());


        //TODO  da napylnq scrollviewto sys snimkite


//        ImageView img = new ImageView(SelectedCarInfoActivity.this);
//        img.setImageResource(R.drawable.login_background);
//        img.setScaleType(ImageView.ScaleType.FIT_XY);
//        hsv.addView(img);
        //TODO clicklistener na call butona
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + getIntent().getStringExtra("phoneNumber")));
                startActivity(callIntent);
            }
        });


//
    }

    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            Log.e("IMAGESSVALQMSNIMKA", params[0]);
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPreExecute() {
//            if(selectedCar.getImages().size() == 0){
//                ImageView img = new ImageView(SelectedCarInfoActivity.this);
//                img.setImageBitmap(selectedCar.getImages().get(0));
//                img.setScaleType(ImageView.ScaleType.FIT_XY);
//                hsvLL.addView(img);
//            }
//
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
                        if (bitmap != null) {
                            selectedCar.getImages().add(bitmap);
                            ImageView newImageView = new ImageView(SelectedCarInfoActivity.this);
                            newImageView.setImageBitmap(bitmap);
                            hsvLL.addView(newImageView);

                            newImageView.requestLayout();
                            newImageView.getLayoutParams().height = height/3;
                            newImageView.getLayoutParams().width = width;
                            newImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                            Log.e("IMAGESADD",bitmap.toString());
                            Log.e("IMAGES1",selectedCar.getImages().size()+ "");
                    } else {
            // Toast.makeText(SelectedPlaceActivity.this, "NO PHOTOS", Toast.LENGTH_SHORT).show();

                    }
              }


    }



    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            Log.e("IMAGESTATUS",statusCode +  "");
            if (statusCode != 200) {
                Log.e("IMAGESTATUS!=200",statusCode +  "");
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Log.e("IMAGESBITMAP",bitmap +  "");
                return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("IMAGESDownloader", "Error downloading image from " + url);
        } finally {
            Log.e("IMAGESFINALY",urlConnection +  "");
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
        return null;
    }
}
