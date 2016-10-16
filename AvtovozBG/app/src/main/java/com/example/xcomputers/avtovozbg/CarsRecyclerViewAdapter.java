package com.example.xcomputers.avtovozbg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xcomputers.avtovozbg.model.Car;

import java.io.IOException;
import java.util.List;

/**
 * Created by svetlio on 12.10.2016 г..
 */

public class CarsRecyclerViewAdapter extends RecyclerView.Adapter<CarsRecyclerViewAdapter.MyRecyclerViewHolder> {

    private List<Car> cars;
    private Activity activity;
    onResultClickListener resultsItemClickListener;

    public CarsRecyclerViewAdapter(Activity activity, List<Car> places) {
        this.cars = places;
        Log.e("TAG", "places in adapter size: " + places.size());
        this.activity = activity;
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.cars_list_view, parent, false);
        //create vh
        MyRecyclerViewHolder vh = new MyRecyclerViewHolder(row);
        //return vh
        return vh;

    }

    @Override
    public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {

        Car car = cars.get(position);
        holder.brandAndModel.setText(car.getBrand() + " " + car.getModel());
        holder.firstImage.setImageBitmap(car.getImages().get(0));
        holder.price.setText("Price: " + car.getPrice() + "$");
        holder.yearOfManufacture.setText("Produced in: " + car.getYearOfManufacture());
        holder.horsePower.setText(car.getHorsePower() + "HP , " + car.getKilometers() + "KM , " + car.getColor());

    }

    @Override
    public int getItemCount() {
        Log.e("TAG", cars.size() + "");
        return cars.size();
    }

    class MyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView brandAndModel;
        TextView horsePower;
        TextView yearOfManufacture;
        TextView kilometers;
        TextView color;
        TextView price;
        ImageView firstImage;


        public MyRecyclerViewHolder(View itemView) {
            super(itemView);
            brandAndModel = (TextView) itemView.findViewById(R.id.model_and_brand);
            horsePower = (TextView) itemView.findViewById(R.id.horse_power_KM_color);
            yearOfManufacture = (TextView) itemView.findViewById(R.id.year_of_manufacture);
            price = (TextView) itemView.findViewById(R.id.price);
            firstImage = (ImageView) itemView.findViewById(R.id.first_img);
            firstImage.setScaleType(ImageView.ScaleType.FIT_XY);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            resultsItemClickListener.onResultClicked(v, getPosition());
        }
    }

    public interface onResultClickListener {
        void onResultClicked(View view, int position);
    }

    public void setOnResultClickListener(final onResultClickListener mItemClickListener) {
        this.resultsItemClickListener = mItemClickListener;
    }
}

