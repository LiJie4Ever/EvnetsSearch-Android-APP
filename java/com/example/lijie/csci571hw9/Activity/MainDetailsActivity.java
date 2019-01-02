package com.example.lijie.csci571hw9.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lijie.csci571hw9.Adapter.mainDetailFragmentPagerAdapter;
import com.example.lijie.csci571hw9.Dao.FavoriteListDataManager;
import com.example.lijie.csci571hw9.Entity.Details;
import com.example.lijie.csci571hw9.Entity.UpcomingEvents;
import com.example.lijie.csci571hw9.Fragment.ArtistsPhotoFragment;
import com.example.lijie.csci571hw9.Fragment.EventInfoFragment;
import com.example.lijie.csci571hw9.Fragment.UpcomingEventsFragment;
import com.example.lijie.csci571hw9.Fragment.VenueFragment;
import com.example.lijie.csci571hw9.R;

import java.util.ArrayList;

public class MainDetailsActivity extends AppCompatActivity {

    private String eventName;
    private boolean is_in_favorlist;
    private String eventId;
    private String venueName;
    private String segment;
    private String time;
    private String maxPrice;
    private String minPrice;
    private String status;
    private String url;
    private String seatMapUrl;

    private double lat;
    private double lon;

    private ArrayList<String> artistsList;
    private ArrayList<String> classifications;
    private ArrayList<UpcomingEvents> upcomingEvents;
    private ArrayList<Fragment> fragmentsList;
    private ArrayList<String> tabTitles;
    private ArrayList<Integer> icons = new ArrayList<>();
    private FavoriteListDataManager favoriteListDataManager;
    private Details detail;
    private ProgressBar progressBar;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_main);
        progressBar = findViewById(R.id.zero_detail_progressBar);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 2000);
        icons.add(0, R.drawable.info_outline);
        icons.add(1, R.drawable.artist);
        icons.add(2, R.drawable.venue);
        icons.add(3, R.drawable.upcoming);
        favoriteListDataManager = new FavoriteListDataManager(this);
        ReceiveData();
        ToolbarInitialization(eventName);
        ViewPagerInitialization();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (is_in_favorlist) {
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.heart_fill_red);
        } else {
            menu.findItem(R.id.menu_favorite).setIcon(R.drawable.heart_fill_white);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            finish();
        } else if (R.id.menu_twitter == item.getItemId()) {
            String twitterUrl = "https://twitter.com/intent/tweet?text=Check%20out%20" + eventName + "%20located%20at%20" + venueName + ".%20Website:%20" + url;
            Uri uri = Uri.parse(twitterUrl);
            Intent twitterintent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(twitterintent);
        } else if (R.id.menu_favorite == item.getItemId()) {
            if (is_in_favorlist) {
                favoriteListDataManager.deleteFromDB(eventId);
                is_in_favorlist = false;
                Toast.makeText(MainDetailsActivity.this, eventName + " was removed from favorites", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            } else {
                favoriteListDataManager.insertIntoDB(eventId, eventName, segment, venueName, time);
                is_in_favorlist = true;
                Toast.makeText(MainDetailsActivity.this, eventName + " was added to favorites", Toast.LENGTH_SHORT).show();
                invalidateOptionsMenu();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ReceiveData() {
        detail = new Details();
        detail = (Details) getIntent().getSerializableExtra("details");
        eventId = detail.getId();
        eventName = detail.getName();
        venueName = detail.getVenueName();
        time = detail.getTime();
        maxPrice = detail.getMaxPrice();
        minPrice = detail.getMinPrice();
        status = detail.getStatus();
        url = detail.getUrl();
        seatMapUrl = detail.getSeatMapUrl();
        artistsList = detail.getArtists();
        classifications = detail.getCategory();
        lat = detail.getLat();
        lon = detail.getLon();
        segment = detail.getSegment();

        Cursor mycursor = favoriteListDataManager.findEventById(eventId);
        if (0 == mycursor.getCount()) {
            is_in_favorlist = false;
        } else {
            is_in_favorlist = true;
        }
    }

    private void ToolbarInitialization(String name){
        toolbar = findViewById(R.id.details_toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);

        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void ViewPagerInitialization() {
        tabTitles = new ArrayList<>();
        fragmentsList = new ArrayList<>();
        tabTitles.add("EVENT");
        tabTitles.add("ARTIST(S)");
        tabTitles.add("VENUE");
        tabTitles.add("UPCOMING");

        UpcomingEventsFragment upcomingEventsFragment = UpcomingEventsFragment.getUpcomingEventsFragment(venueName);
        VenueFragment venueFragment = VenueFragment.getVenueFragment(venueName, lat, lon);
        EventInfoFragment eventInfoFragment = EventInfoFragment.getEventInfoFragment(eventName, venueName, time, classifications, maxPrice, minPrice, status, url, seatMapUrl);
        ArtistsPhotoFragment artistsPhotoFragment = ArtistsPhotoFragment.getArtistsPhotoFragment(artistsList, segment);

        fragmentsList.add(eventInfoFragment);
        fragmentsList.add(artistsPhotoFragment);
        fragmentsList.add(venueFragment);
        fragmentsList.add(upcomingEventsFragment);

        viewPager = findViewById(R.id.detail_viewpager);
        viewPager.setAdapter(new mainDetailFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList, tabTitles, this));
        tabLayout = findViewById(R.id.detail_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabTitles.size(); i++) {
            tabLayout.getTabAt(i).setCustomView(getViewForTab(i));
        }
    }

    private View getViewForTab(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.detail_tablayout, null);
        ImageView iv_icon = view.findViewById(R.id.detail_tabLayout_image);
        TextView tv_name = view.findViewById(R.id.detail_tabLayout_text);
        iv_icon.setImageResource(icons.get(index));
        tv_name.setText(tabTitles.get(index));
        return view;
    }

}
