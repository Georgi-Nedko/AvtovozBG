package com.example.xcomputers.avtovozbg;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.xcomputers.avtovozbg.model.Car;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        recyclerView = (RecyclerView) findViewById(R.id.cars_recycler);
        carList = new ArrayList<>();


        String json = getIntent().getStringExtra("json");
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject contacts = jsonObject.getJSONObject("contacts");
            phoneNumber = contacts.getString("phone");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ImageDownloader().execute(json);
        showProgressDialog();
        /*carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));*/


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CarsRecyclerViewAdapter(CarsActivity.this, carList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).color(Color.WHITE).build());
        adapter.setOnResultClickListener(createClickListener());

    }

    protected CarsRecyclerViewAdapter.onResultClickListener createClickListener() {
        return new CarsRecyclerViewAdapter.onResultClickListener() {
            @Override
            public void onResultClicked(View view, int position) {
                Car selectedCar = carList.get(position);
                Intent intent = new Intent(CarsActivity.this,SelectedCarInfoActivity.class);
                intent.putExtra("selectedCar",selectedCar);
                intent.putExtra("phoneNumber",phoneNumber);

                startActivity(intent);

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


    class ImageDownloader extends AsyncTask<String, Void, ArrayList<Bitmap>>{

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
                    JSONArray urls = post.getJSONArray("urls");
                    String address = urls.getString(0);
                    tepmoraryBitmapList = downloadBitmap(address);
                    carList.add(new Car(model,brand,hp, 5000, productionYear,color, km, description, tepmoraryBitmapList, urls.toString()));
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
        //for(int i = 0; i < urls.length(); i++) {
            try {
                URL url = new URL(address);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();
                Log.e("IMAGESTATUS", statusCode + "");
                if (statusCode != 200) {
                    Log.e("IMAGESTATUS!=200", statusCode + "");
                    return null;
                }
                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Bitmap resizedBitmap = getResizedBitmap(bitmap, 200, 200);
                    Log.e("IMAGESBITMAP", bitmap + "");
                    map.add(resizedBitmap);
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                //Log.w("IMAGESDownloader", "Error downloading image from " + url);
            } finally {
                Log.e("IMAGESFINALY", urlConnection + "");
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

            }
        //}
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
