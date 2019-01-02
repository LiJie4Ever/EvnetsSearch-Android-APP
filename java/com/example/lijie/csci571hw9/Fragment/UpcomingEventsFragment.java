package com.example.lijie.csci571hw9.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Adapter.UpcomingListRecyclerAdapter;
import com.example.lijie.csci571hw9.Entity.UpcomingEvents;
import com.example.lijie.csci571hw9.Listeners.myOnItemClickListener;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpcomingEventsFragment extends Fragment {

    private String venueName;
    private ArrayList<UpcomingEvents> upcomingEventsList;
    private AppCompatSpinner acs_type;
    private AppCompatSpinner acs_order;
    private RecyclerView recyclerView;
    private TextView tv_noRecords;
    private UpcomingListRecyclerAdapter upcomingListRecyclerAdapter;
    private String type;
    private String order;

    public UpcomingEventsFragment() {

    }

    public static UpcomingEventsFragment getUpcomingEventsFragment(String venueName) {
        UpcomingEventsFragment upcomingEventsFragment = new UpcomingEventsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("venueName", venueName);
        upcomingEventsFragment.setArguments(bundle);
        return upcomingEventsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            venueName = getArguments().getString("venueName");
        }
        getUpcomingEventsList(venueName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int i = 0;
//            }
//        }, 2000);
        View view = inflater.inflate(R.layout.fragment_upcoming_lists, container, false);
        acs_type = view.findViewById(R.id.events_orderBy_type);
        acs_order = view.findViewById(R.id.events_orderBy_order);
        recyclerView = view.findViewById(R.id.upcoming_events_recycler);
        tv_noRecords = view.findViewById(R.id.upcoming_events_noInfo);
        acs_order.setEnabled(false);
        if (0 == upcomingEventsList.size()) {
            tv_noRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tv_noRecords.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        upcomingListRecyclerAdapter = new UpcomingListRecyclerAdapter(upcomingEventsList, getContext());
        recyclerView.setAdapter(upcomingListRecyclerAdapter);

        upcomingListRecyclerAdapter.reSetMyOnItemClickListener(new myOnItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                if (null != upcomingEventsList.get(index).getUrl()) {
                    Uri uri = Uri.parse(upcomingEventsList.get(index).getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            }
        });

        acs_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = adapterView.getItemAtPosition(i).toString();
                Log.i("ordertype", "onItemSelected: " + type);
                SortUpcomingEvntsList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        acs_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                order = adapterView.getItemAtPosition(i).toString();
                Log.i("ordertype", "onItemSelected: " + order);
                SortUpcomingEvntsList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }


    private void getUpcomingEventsList(String venueName) {
        upcomingEventsList = new ArrayList<>();
        upcomingEventsList.clear();
        String params = "venueName=" + venueName;
        String url = NetworkUtility.GET_UPCOMING_EVETNS + params;
        url = url.replaceAll("\\s+", "+");
        Log.i("upcomingurl", "getUpcomingEventsList: " + url);
        RequestQueue queue = Volley.newRequestQueue(getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("upcomingurl", "getUpcomingEventsList: " + response);
                try {
                    int number;
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray data = jsonObject.getJSONObject("resultsPage").getJSONObject("results").getJSONArray("event");
                    if (null == data || 0 == data.length()) {
                        return;
                    }
                    if (data.length() <= 5){
                        number = data.length();
                    } else {
                        number = 5;
                    }
                    for (int i = 0; i < number; i++) {
                        UpcomingEvents upcomingEvents = new UpcomingEvents();
                        String date;
                        String time;
                        upcomingEvents.setName(data.getJSONObject(i).optString("displayName"));
                        upcomingEvents.setArtist(data.getJSONObject(i).getJSONArray("performance").getJSONObject(0).getJSONObject("artist").optString("displayName"));
                        upcomingEvents.setType(data.getJSONObject(i).optString("type"));
                        date = data.getJSONObject(i).getJSONObject("start").optString("date");
                        if (null != date) {
                            upcomingEvents.setDateForOrder(date);
                            String[] temp = date.split("-");
                            String monthName = convertMonth(temp[1]);
                            date = monthName + " " + temp[2] + ", " + temp[0] + " ";
                        }
                        Log.i("upcomingname", "onResponse: ");
                        if ("null".equals(data.getJSONObject(i).getJSONObject("start").optString("time"))){
                            upcomingEvents.setDate(date);
                            Log.i("testtime", "onResponse: get into" + data.getJSONObject(i).getJSONObject("start").optString("time"));
                        } else {
                            upcomingEvents.setDate(date + data.getJSONObject(i).getJSONObject("start").optString("time"));
                            Log.i("testtime", "onResponse: not get into " + data.getJSONObject(i).getJSONObject("start").optString("time"));
                        }
                        upcomingEvents.setUrl(data.getJSONObject(i).optString("uri"));
                        upcomingEventsList.add(upcomingEvents);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("upcomingevents_response", "onErrorResponse: " + error);
            }
        });
        queue.add(stringRequest);
    }

    private void SortUpcomingEvntsList() {
        if (type.equals("Default")) {
            acs_order.setEnabled(false);
        } else {
            acs_order.setEnabled(true);
        }
        if (type.equals("Event Name")) {
            Comparator<UpcomingEvents> comparator = new Comparator<UpcomingEvents>() {
                @Override
                public int compare(UpcomingEvents t1, UpcomingEvents t2) {
                    if (order.equals("Ascending")) {
                        return (t1.getName().compareTo(t2.getName()));
                    } else {
                        return (t2.getName().compareTo(t1.getName()));
                    }
                }
            };
            Collections.sort(upcomingEventsList, comparator);
        }

        if (type.equals("Time")) {
            Comparator<UpcomingEvents> comparator = new Comparator<UpcomingEvents>() {
                @Override
                public int compare(UpcomingEvents t1, UpcomingEvents t2) {
                    if (order.equals("Ascending")) {
                        return (t1.getDateForOrder().compareTo(t2.getDateForOrder()));
                    } else {
                        return (t2.getDateForOrder().compareTo(t1.getDateForOrder()));
                    }
                }
            };
            Collections.sort(upcomingEventsList, comparator);
        }

        if (type.equals("Artist")) {
            Comparator<UpcomingEvents> comparator = new Comparator<UpcomingEvents>() {
                @Override
                public int compare(UpcomingEvents t1, UpcomingEvents t2) {
                    if (order.equals("Ascending")) {
                        return (t1.getArtist().compareTo(t2.getArtist()));
                    } else {
                        return (t2.getArtist().compareTo(t1.getArtist()));
                    }
                }
            };
            Collections.sort(upcomingEventsList, comparator);
        }

        if (type.equals("Type")) {
            Comparator<UpcomingEvents> comparator = new Comparator<UpcomingEvents>() {
                @Override
                public int compare(UpcomingEvents t1, UpcomingEvents t2) {
                    if (order.equals("Ascending")) {
                        return (t1.getType().compareTo(t2.getType()));
                    } else {
                        return (t2.getType().compareTo(t1.getType()));
                    }
                }
            };
            Collections.sort(upcomingEventsList, comparator);
        }

        upcomingListRecyclerAdapter.reSetUpcomingEventsList(upcomingEventsList);
        upcomingListRecyclerAdapter.notifyDataSetChanged();
    }

    private String convertMonth(String num) {
        String monthName = "";
        if (num.equals("01")) {
            monthName = "Jan";
        } else if (num.equals("02")) {
            monthName = "Feb";
        } else if (num.equals("03")) {
            monthName = "Mar";
        } else if (num.equals("04")) {
            monthName = "Apr";
        } else if (num.equals("05")) {
            monthName = "May";
        } else if (num.equals("06")) {
            monthName = "Jun";
        } else if (num.equals("07")) {
            monthName = "Jul";
        } else if (num.equals("08")) {
            monthName = "Aug";
        } else if (num.equals("09")) {
            monthName = "Sep";
        } else if (num.equals("10")) {
            monthName = "Oct";
        } else if (num.equals("11")) {
            monthName = "Nov";
        } else if (num.equals("12")) {
            monthName = "Dec";
        }
        return monthName;
    }
}
