package com.example.lijie.csci571hw9.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lijie.csci571hw9.Adapter.mainFragmentPagerAdapter;
import com.example.lijie.csci571hw9.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ViewPager viewPager;

    private ArrayList<String> tabTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.mainToolbar);
        toolbar.setTitle("Event Search");
        setSupportActionBar(toolbar);

        viewPagerInit();

        askRequestPermission();
    }

    private void viewPagerInit() {
        tabTitles = new ArrayList<>();

        tabTitles.add("SEARCH");
        tabTitles.add("FAVORITES");

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        mainFragmentPagerAdapter fragmentPagerAdapter = new mainFragmentPagerAdapter(getSupportFragmentManager(), tabTitles, this);
        Log.d("aa", "viewPagerInit: "+ fragmentPagerAdapter);

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabTitles.size(); i++) {
            tabLayout.getTabAt(i).setCustomView(getMyTabView(i));
        }
    }

    private View getMyTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.main_tablayout, null);
        TextView tab_name = view.findViewById(R.id.tab_name);
        tab_name.setText(tabTitles.get(index));

        return view;
    }

    private void askRequestPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}