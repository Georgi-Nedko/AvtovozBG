package com.example.xcomputers.avtovozbg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.xcomputers.avtovozbg.model.Car;
import com.example.xcomputers.avtovozbg.model.WifiManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class CarsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CarsRecyclerViewAdapter adapter;
    private ArrayList<Car> carList;
    private ArrayList<Bitmap> tepmoraryBitmapList;
    private String brand;
    private String model;
    private int hp;
    private int productionYear;
    private int km;
    private String color;
    private String description;
    private ProgressDialog mProgressDialog;
    private String phoneNumber;
    private static String displayName;
    private static String eMail;
    private String token;
    private String price;
    private AlertDialog alertDialog;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        recyclerView = (RecyclerView) findViewById(R.id.cars_recycler);
        carList = new ArrayList<>();
        wifiManager = WifiManager.getInstance(this);
        displayName = getIntent().getStringExtra("displayName");
        eMail = getIntent().getStringExtra("eMail");
        token = getIntent().getStringExtra("token");
        Log.e("TOKEN" , token);

        JSONObject regJson = new JSONObject();
        try {
            regJson.put("device", token);
            regJson.put("email", eMail);
            regJson.put("name", displayName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("CAR REQUEST", regJson.toString());

        new TokenRegistrationTask().execute("http://avtovoz.hopto.org/registerDevice.php");
        new NewCarsRequestTask().execute("http://avtovoz.hopto.org/getPostsNotification.php?device=" + token);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarsRecyclerViewAdapter(CarsActivity.this, carList);
        recyclerView.setAdapter(adapter);
        adapter.setOnResultClickListener(createClickListener());
    }

    protected CarsRecyclerViewAdapter.onResultClickListener createClickListener() {
        return new CarsRecyclerViewAdapter.onResultClickListener() {
            @Override
            public void onResultClicked(View view, int position) {
                if (wifiManager.isConnectingToInternet()) {
                    Car selectedCar = carList.get(position);
                    Intent intent = new Intent(CarsActivity.this, SelectedCarInfoActivity.class);
                    intent.putExtra("selectedCar", selectedCar);
                    intent.putExtra("phoneNumber", phoneNumber);
                    startActivity(intent);
                } else {
                    alertDialog = wifiManager.promptUserToTurnOnWifi();
                }
            }
        };
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    class TokenRegistrationTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            JSONObject regJson = new JSONObject();
            try {
                regJson.put("device", token);
                regJson.put("email", eMail);
                regJson.put("name", displayName);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String address = params[0];
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();
                OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                wr.write(regJson.toString());
                wr.flush();

                StringBuilder sb = new StringBuilder();
                int HttpResult = connection.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(connection.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println("HTTP_OK_TAG" + sb.toString());
                } else {
                    System.out.println("HTTP_FAIL_TAG" + connection.getResponseMessage());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    class NewCarsRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String address = params[0];
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                Scanner sc = new Scanner(connection.getInputStream());
                while (sc.hasNextLine()) {
                    response.append(sc.nextLine());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String jsonResponce) {
            try {
                JSONObject jsonObject = new JSONObject(jsonResponce);
                JSONObject contacts = jsonObject.getJSONObject("contacts");
                phoneNumber = contacts.getString("phone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new ImageDownloader().execute(jsonResponce);
            showProgressDialog();
        }
    }

    class ImageDownloader extends AsyncTask<String, Void, ArrayList<Bitmap>> {

        @Override
        protected ArrayList<Bitmap> doInBackground(String... params) {
            try {
                JSONObject responce = new JSONObject(params[0]);
                JSONArray posts = responce.getJSONArray("posts");
                for (int i = 0; i < posts.length(); i++) {
                    JSONObject post = posts.getJSONObject(i);
                    brand = post.getString("brand");
                    model = post.getString("model");
                    hp = post.getInt("hp");
                    productionYear = post.getInt("year");
                    km = post.getInt("km");
                    color = post.getString("color");
                    description = post.getString("description");
                    price = post.getString("price");
                    JSONArray urls = post.getJSONArray("urls");

                    String temp = "http://avtovoz.hopto.org/";
                    String address = temp + urls.getString(0);
                    tepmoraryBitmapList = downloadBitmap(address);
                    carList.add(new Car(model, brand, hp, price, productionYear, color, km, description, tepmoraryBitmapList, urls.toString()));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            hideProgressDialog();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tepmoraryBitmapList;
        }

        @Override
        protected void onPostExecute(ArrayList<Bitmap> bitmap) {
            tepmoraryBitmapList = new ArrayList<Bitmap>();
        }
    }

    private ArrayList<Bitmap> downloadBitmap(String address) {
        HttpURLConnection urlConnection = null;
        ArrayList<Bitmap> map = new ArrayList<>();
        try {
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != 200) {
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Bitmap resizedBitmap = getResizedBitmap(bitmap, 200, 200);
                map.add(resizedBitmap);
            }
        } catch (Exception e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

        }
        return map;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}
