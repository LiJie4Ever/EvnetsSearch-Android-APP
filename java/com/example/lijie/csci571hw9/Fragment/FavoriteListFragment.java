package com.example.lijie.csci571hw9.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Activity.MainDetailsActivity;
import com.example.lijie.csci571hw9.Activity.ZeroResultsActivity;
import com.example.lijie.csci571hw9.Adapter.EventResultListAdapter;
import com.example.lijie.csci571hw9.Dao.FavoriteListDataManager;
import com.example.lijie.csci571hw9.Entity.Details;
import com.example.lijie.csci571hw9.Entity.Event;
import com.example.lijie.csci571hw9.Listeners.myOnItemClickListener;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteListFragment extends Fragment {
    private FavoriteListDataManager favoriteListDataManager;
    private ArrayList<Event> myevents;
    private TextView tv_noRecords;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private EventResultListAdapter eventResultListAdapter;
    private Intent intent;
    private int Flag = 0;

    public FavoriteListFragment() {

    }

    public static FavoriteListFragment getNewInstance() {
         return new FavoriteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Flag = getAllRecords();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorlist, container, false);
        recyclerView = view.findViewById(R.id.favor_list_recycler);
        tv_noRecords = view.findViewById(R.id.favor_list_norecourds);

        if (null == myevents || 0 == myevents.size()) {
            tv_noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tv_noRecords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        RecyclerViewInitialization();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        myevents.clear();
        Flag = getAllRecords();
        if (null == myevents || 0 == myevents.size()) {
            tv_noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tv_noRecords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        eventResultListAdapter.reSetEvents(myevents);
        eventResultListAdapter.notifyDataSetChanged();
    }

    private int getAllRecords() {
        myevents = new ArrayList<>();
        favoriteListDataManager = new FavoriteListDataManager(getContext());
        Cursor mycursor = favoriteListDataManager.findAllEvents();

        if (0 != mycursor.getCount()) {
            while (mycursor.moveToNext()) {
                Event event = new Event();
                event.setId(mycursor.getString(mycursor.getColumnIndex("event_id")));
                event.setName(mycursor.getString(mycursor.getColumnIndex("event_name")));
                event.setCategory(mycursor.getString(mycursor.getColumnIndex("event_segment")));
                event.setVenueName(mycursor.getString(mycursor.getColumnIndex("event_venue")));
                event.setDate(mycursor.getString(mycursor.getColumnIndex("event_date")));
                event.setFavor(true);
                myevents.add(event);
            }
            mycursor.close();
        } else {
            mycursor.close();
            return 0;
        }
        return 1;
    }

    private void RecyclerViewInitialization() {

        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        eventResultListAdapter = new EventResultListAdapter(getContext(), myevents);
        recyclerView.setAdapter(eventResultListAdapter);

        eventResultListAdapter.setMyOnItemClickListener(new myOnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                if (R.id.event_item == view.getId()) {
                    getDetailofEvent(myevents.get(index));
                } else {
                    Toast.makeText(getContext(), myevents.get(index).getName()+" was removed from favorites", Toast.LENGTH_SHORT).show();
                    favoriteListDataManager.deleteFromDB(myevents.get(index).getId());
                    myevents.remove(index);
                    if (null == myevents || 0 == myevents.size()) {
                        tv_noRecords.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        tv_noRecords.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
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
        RequestQueue queue = Volley.newRequestQueue(getContext());
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
                        for (int i = 0; i < attractions.length(); i++) {
                            artistList.add(attractions.getJSONObject(i).optString("name"));
                        }
                    } catch (JSONException e) {
                        artistList.clear();
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
                            String subGenre = classifications.getJSONObject(i).getJSONObject("subGenre").optString("name");
                            String genre = classifications.getJSONObject(i).getJSONObject("genre").optString("name");
                            String segment = classifications.getJSONObject(i).getJSONObject("segment").optString("name");
                            String subType = classifications.getJSONObject(i).getJSONObject("subType").optString("name");
                            String type = classifications.getJSONObject(i).getJSONObject("type").optString("name");

                            categoryList.add(subGenre);
                            categoryList.add(genre);
                            categoryList.add(segment);
                            categoryList.add(subType);
                            categoryList.add(type);

                            detail.setSegment(segment);

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

                    Intent intent = new Intent(getContext(), MainDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("details", detail);
                    intent.putExtras(bundle);
                    startActivity(intent);

                } catch (JSONException e) {
                    intent = new Intent(getContext(), ZeroResultsActivity.class);
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
