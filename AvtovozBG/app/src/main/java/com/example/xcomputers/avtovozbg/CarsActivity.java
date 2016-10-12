package com.example.xcomputers.avtovozbg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.xcomputers.avtovozbg.model.Car;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;


public class CarsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CarsRecyclerViewAdapter adapter;
    private ArrayList<Car> carList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        recyclerView = (RecyclerView) findViewById(R.id.cars_recycler);
        carList = new ArrayList<>();
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
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));
        carList.add(new Car("Astra","Opel",101,18000,2000,"green metalic",222200,"it is an amazing car",new ArrayList<Bitmap>()));


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
                startActivity(intent);

            }
        };
    }


}
