package com.example.lijie.csci571hw9.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Adapter.EventResultListAdapter;
import com.example.lijie.csci571hw9.Dao.FavoriteListDataManager;
import com.example.lijie.csci571hw9.Entity.Details;
import com.example.lijie.csci571hw9.Entity.DividerItemDecoration;
import com.example.lijie.csci571hw9.Entity.Event;
import com.example.lijie.csci571hw9.Listeners.myOnItemClickListener;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsListActivity extends AppCompatActivity {
    private ArrayList<Event> myevents;
    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private EventResultListAdapter eventResultListAdapter;
    private FavoriteListDataManager favoriteListDataManager;
    private Intent intent;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_results_list);
        progressBar = findViewById(R.id.zero_result_progressBar);
        recyclerView = findViewById(R.id.result_list_recyclerView);
        favoriteListDataManager = new FavoriteListDataManager(ResultsListActivity.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }, 2000);

        ReceiveData();
        toolbar = findViewById(R.id.result_list_toolbar);
        toolbar.setTitle("Search Results");
        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        RecyclerViewInitialization();
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < myevents.size(); i++) {
            Cursor mycursor = favoriteListDataManager.findEventById(myevents.get(i).getId());
            if (0 == mycursor.getCount()) {
                myevents.get(i).setFavor(false);
            } else {
                myevents.get(i).setFavor(true);
            }
        }
        Log.i("resume", "onResume: onResume");
        eventResultListAdapter.reSetEvents(myevents);
        eventResultListAdapter.notifyDataSetChanged();
    }

    private void ReceiveData() {
        myevents = new ArrayList<>();
        myevents.clear();
        myevents = (ArrayList<Event>) this.getIntent().getSerializableExtra("events");

        for (int i = 0; i < myevents.size(); i++) {
            Cursor mycursor = favoriteListDataManager.findEventById(myevents.get(i).getId());
            if (0 == mycursor.getCount()) {
                myevents.get(i).setFavor(false);
            } else {
                myevents.get(i).setFavor(true);
            }
        }
        Log.i("receiveevents", "ReceiveData: " + myevents.toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void RecyclerViewInitialization() {
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.result_list_recyclerView);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, R.drawable.divider));

        eventResultListAdapter = new EventResultListAdapter(ResultsListActivity.this, myevents);
        recyclerView.setAdapter(eventResultListAdapter);

        eventResultListAdapter.setMyOnItemClickListener(new myOnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                if (R.id.event_item == view.getId()) {
                    getDetailofEvent(myevents.get(index));
                } else if (view.getId() == R.id.favorite_item) {
                    if (myevents.get(index).isFavor()) {
                        myevents.get(index).setFavor(false);
                        favoriteListDataManager.deleteFromDB(myevents.get(index).getId());
                        Toast.makeText(ResultsListActivity.this, myevents.get(index).getName()+" was removed from favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        myevents.get(index).setFavor(true);
                        favoriteListDataManager.insertIntoDB(myevents.get(index).getId(), myevents.get(index).getName(), myevents.get(index).getCategory(), myevents.get(index).getVenueName(), myevents.get(index).getDate());
                        Toast.makeText(ResultsListActivity.this, myevents.get(index).getName()+" was added to favorites", Toast.LENGTH_SHORT).show();
                    }
                    Log.i("favor", "onItemClick: Click on Favorite");
                    eventResultListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private int getDetailofEvent(final Event event) {
        String params;
        params = "eventId=" + event.getId();
        String url = NetworkUtility.GET_DETAIL + params;
        url = url.replaceAll("\\s+", "+");
        RequestQueue queue = Volley.newRequestQueue(ResultsListActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Details detail = new Details();
                    ArrayList<String> artistList = detail.getArtists();
                    ArrayList<String> categoryList = detail.getCategory();
                    artistList.clear();
                    categoryList.clear();

                    detail.setName(jsonObject.optString("name"));
                    detail.setId(jsonObject.optString("id"));

                    try {
                        String venueName = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getString("name");
                        detail.setVenueName(venueName);
                        Log.i("detailvenue", "onResponse: " + detail.getVenueName());
                    } catch (JSONException e) {
                        detail.setVenueName("none");
                    }

                    try {
                        JSONArray attractions = jsonObject.getJSONObject("_embedded").getJSONArray("attractions");
                        Log.i("attactionslength", "onResponse: " + attractions.length());
                        for (int i = 0; i < attractions.length(); i++) {
                            if (null != attractions.getJSONObject(i).optString("name") && !attractions.getJSONObject(i).optString("name").equals("null") && !attractions.getJSONObject(i).optString("name").equals("")) {
                                artistList.add(attractions.getJSONObject(i).optString("name"));
                            }
                            Log.i("attactionslength", "onResponse: " + attractions.getJSONObject(i).optString("name"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        String localDate = jsonObject.getJSONObject("dates").getJSONObject("start").optString("localDate");
                        String localTime = jsonObject.getJSONObject("dates").getJSONObject("start").optString("localTime");
                        detail.setTime(localDate + " " + localTime);
                    } catch (JSONException e) {
                        detail.setTime("none");
                    }

                    try {
                        String max = jsonObject.getJSONArray("priceRanges").getJSONObject(0).optString("max");
                        String min = jsonObject.getJSONArray("priceRanges").getJSONObject(0).optString("min");
                        detail.setMaxPrice(max);
                        detail.setMinPrice(min);
                    } catch (JSONException e) {
                        detail.setMaxPrice("none");
                        detail.setMinPrice("none");
                    }

                    try {
                        String status = jsonObject.getJSONObject("dates").getJSONObject("status").optString("code");
                        detail.setStatus(status);
                    } catch (JSONException e) {
                        detail.setStatus("none");
                    }

                    String url = jsonObject.optString("url");
                    detail.setUrl(url);

                    try {
                        String seatUrl = jsonObject.getJSONObject("seatmap").optString("staticUrl");
                        detail.setSeatMapUrl(seatUrl);
                        Log.i("attactions", "onResponse: " + detail.getSeatMapUrl());
                    } catch (JSONException e) {
                        detail.setSeatMapUrl("none");
                    }

                    try {
                        JSONArray classifications = jsonObject.getJSONArray("classifications");
                        for (int i = 0; i < classifications.length(); i++) {
                            String segment = classifications.getJSONObject(i).getJSONObject("segment").optString("name");
                            String subGenre = classifications.getJSONObject(i).getJSONObject("subGenre").optString("name");
                            String genre = classifications.getJSONObject(i).getJSONObject("genre").optString("name");
                            String subType = classifications.getJSONObject(i).getJSONObject("subType").optString("name");
                            String type = classifications.getJSONObject(i).getJSONObject("type").optString("name");

                            categoryList.add(subGenre);
                            categoryList.add(genre);
                            categoryList.add(segment);
                            categoryList.add(subType);
                            categoryList.add(type);
                        }
                    } catch (JSONException e) {
                        categoryList.clear();
                    }

                    try {
                        String lat = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").optString("latitude");
                        String lon = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("location").optString("longitude");
                        detail.setLat(Double.valueOf(lat));
                        detail.setLon(Double.valueOf(lon));

                        Log.i("attractions", "onResponse: " + detail.getLat() + detail.getLon());
                    } catch (JSONException e) {
                        detail.setLat(0.0);
                        detail.setLon(0.0);
                    }
                    detail.setSegment(event.getCategory());

                    Intent intent = new Intent(ResultsListActivity.this, MainDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("details", detail);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } catch (JSONException e) {
                    intent = new Intent(ResultsListActivity.this, ZeroResultsActivity.class);
                    startActivity(intent);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("detail_response", "onErrorResponse: " + error);
            }
        });
        queue.add(stringRequest);
        return 1;
    }
}

//.addItemDecoration
