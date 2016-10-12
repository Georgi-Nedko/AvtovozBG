package com.example.xcomputers.avtovozbg;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.xcomputers.avtovozbg.model.Car;

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

        selectedCar = (Car) getIntent().getParcelableExtra("selectedCar");
        modelAndBrand.setText(selectedCar.getModel() + "," + selectedCar.getBrand());
        horsePower.setText(selectedCar.getHorsePower() + "HP");
        kilometers.setText(selectedCar.getKilometers() + "");
        producedIn.setText(selectedCar.getYearOfManufacture()+ "");
        price.setText(" cost -> " + selectedCar.getPrice() + "$");
        color.setText(selectedCar.getColor());
        description.setText(selectedCar.getDescription());


        //TODO  da na pylnq scrollviewto sys snimkite
//        ImageView img = new ImageView(SelectedCarInfoActivity.this);
//        img.setImageResource(R.drawable.login_background);
//        img.setScaleType(ImageView.ScaleType.FIT_XY);
//        hsv.addView(img);
        //TODO clicklistener na call butona
        /**
         *         call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:" + phoneTV));
                    startActivity(callIntent);
                    }
                    });
         *
         */
//
    }
}
