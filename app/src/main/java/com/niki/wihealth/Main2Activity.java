package com.niki.wihealth;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TableLayout;

public class Main2Activity extends AppCompatActivity {

    private TabLayout tablayout ;
    private AppBarLayout appBarLayout ;
    private ViewPager viewPager;

    public Main2Activity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tablayout = (TabLayout) findViewById(R.id.tablayout_id);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbarid);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        ViewPagerAdapter1 adapter = new ViewPagerAdapter1(getSupportFragmentManager());

        adapter.AddFragment(new FragmentReward() , "Reward");
        adapter.AddFragment(new FragmentMyReward() , "My Reward");

        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);

    }
}
