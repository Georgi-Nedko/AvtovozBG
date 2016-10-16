package com.example.xcomputers.avtovozbg;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.xcomputers.avtovozbg.model.Car;

import java.util.ArrayList;


public class SeePhotosOnFullScreenActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_photos_on_full_screen);
        Bitmap map = getIntent().getParcelableExtra("carImage");
        Log.e("TAG", map.toString());

    }
}
