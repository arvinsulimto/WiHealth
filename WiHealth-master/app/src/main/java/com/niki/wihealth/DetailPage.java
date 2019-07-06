package com.niki.wihealth;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.niki.wihealth.adapter.ViewPagerAdapter;

public class DetailPage extends AppCompatActivity {
    ViewPager viewPager;
    TextView tvName, tvDescription, tvPrice, tvRating, tvCount, tvAddress;
    DataPassing dataPassing = DataPassing.getInstance();
    Utility util = new Utility();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        viewPager = findViewById(R.id.viewpager);
        viewPager = findViewById(R.id.viewpager);
        tvName = findViewById(R.id.tvNamePager);
        tvDescription = findViewById(R.id.tvDescriptionPager);
        tvCount = findViewById(R.id.tvPersonPager);
        tvPrice = findViewById(R.id.tvPricePager);
        tvRating = findViewById(R.id.tvRatePager);
        tvAddress = findViewById(R.id.tvAddressPager);

        tvName.setText(dataPassing.getSportCenter().getName());
        tvDescription.setText(dataPassing.getSportCenter().getDescription());
        tvCount.setText(String.valueOf(dataPassing.getSportCenter().getCount())+" person here");
        tvPrice.setText(util.toIDR(dataPassing.getSportCenter().getPrice()));
        tvRating.setText(String.valueOf(dataPassing.getSportCenter().getRating()));
        tvAddress.setText(dataPassing.getSportCenter().getLocation());

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

    }
}
