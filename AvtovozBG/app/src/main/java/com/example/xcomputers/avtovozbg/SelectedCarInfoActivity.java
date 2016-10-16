package com.example.xcomputers.avtovozbg;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xcomputers.avtovozbg.model.Car;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private int height;
    private int width;
    private int counter = 0;
    private ImageView firstImage;
    private AlertDialog alertDialog;

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

        hsvLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnectingToInternet()) {
                    Intent intent = new Intent(SelectedCarInfoActivity.this, SeePhotosOnFullScreenActivity.class);
                    intent.putExtra("car", selectedCar.getImageUrls());
                    intent.putExtra("carImage", selectedCar.getImages().get(0));
                    startActivity(intent);
                }
                else{
                    promptUserToTurnOnWifi();
                }
            }
        });
        selectedCar = getIntent().getParcelableExtra("selectedCar");
        modelAndBrand.setText(selectedCar.getBrand() + "  " + selectedCar.getModel());
        horsePower.setText(selectedCar.getHorsePower()+"");
        kilometers.setText(selectedCar.getKilometers()+"");
        producedIn.setText(selectedCar.getYearOfManufacture()+"");
        price.setText(selectedCar.getPrice());
        color.setText(selectedCar.getColor());
        description.setText(selectedCar.getDescription());

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        String urls = selectedCar.getImageUrls();

        firstImage = new ImageView(SelectedCarInfoActivity.this);
        firstImage.setImageResource(R.drawable.image_coming_soon);
        hsvLL.addView(firstImage);
        firstImage.requestLayout();
        firstImage.getLayoutParams().height = height / 3;
        firstImage.getLayoutParams().width = width;
        firstImage.setScaleType(ImageView.ScaleType.FIT_XY);


        try {
            JSONArray jsonArray = new JSONArray(urls);

            for(int i = 0; i<jsonArray.length();i++){
                String temp = "http://avtovoz.hopto.org/";
                String address = temp + jsonArray.getString(i);
                new ImageDownloaderTask().execute(address);
            }

            Log.e("URLSARRAY", jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }



       // Log.e("URLS", selectedCar.getImageUrls());


        //TODO  da napylnq scrollviewto sys snimkite


        /*ImageView img = new ImageView(SelectedCarInfoActivity.this);
        img.setImageResource(R.drawable.login_background);
        img.setScaleType(ImageView.ScaleType.FIT_XY);
        hsv.addView(img);*/
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
            counter++;
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPreExecute() {
            if(selectedCar.getImages().size() == 0){
                ImageView img = new ImageView(SelectedCarInfoActivity.this);
                img.setImageResource(R.drawable.image_coming_soon);
                img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                hsvLL.addView(img);
            }
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
                hsvLL.removeView(firstImage);
                hsvLL.addView(newImageView);

                newImageView.requestLayout();
                newImageView.getLayoutParams().height = height / 3;
                newImageView.getLayoutParams().width = width;
                newImageView.setScaleType(ImageView.ScaleType.FIT_XY);

                Log.e("IMAGESADD", bitmap.toString());
                //Log.e("IMAGES1", selectedCar.getImages().size() + "");
            } else {
                // Toast.makeText(SelectedPlaceActivity.this, "NO PHOTOS", Toast.LENGTH_SHORT).show();


                Log.e("IMAGESADD", bitmap.toString());
                Log.e("IMAGES1", selectedCar.getImages().size() + "");
                if (counter == 1) {
                    hsvLL.removeViewAt(0);

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
    private void promptUserToTurnOnWifi() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!isConnectingToInternet()) {
            builder.setTitle("Internet Services Not Active");
            builder.setMessage("Please enable Internet Services");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
    }

    private boolean isConnectingToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) SelectedCarInfoActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    return true;
                }
            }
        } else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
