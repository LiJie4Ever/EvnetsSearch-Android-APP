package com.example.lijie.csci571hw9.Fragment;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Activity.ResultsListActivity;
import com.example.lijie.csci571hw9.Activity.ZeroResultsActivity;
import com.example.lijie.csci571hw9.Adapter.AutoSuggestAdapter;
import com.example.lijie.csci571hw9.Entity.AutoCompleteApiCall;
import com.example.lijie.csci571hw9.Entity.Event;
import com.example.lijie.csci571hw9.Entity.SearchParams;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.LocationUtility;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchEventFragment extends Fragment {
    private Intent intent;
    private ArrayList<Event> myevents = new ArrayList<>();
    private Location location;
    private SearchParams info;
    private String flag = "local";
    private AppCompatSpinner acs_category;
    private EditText et_distance;
    private AppCompatSpinner acs_unit;
    private RadioGroup rg_from;
    private RadioButton rb_currentLocation;
    private RadioButton rb_otherLocation;
    private EditText et_locationName;
    private Button btn_search;
    private Button btn_clear;
    private TextView tv_invalidKeyword;
    private TextView tv_invalidLocName;

    //*********************************************************
    private AppCompatAutoCompleteTextView autoCompleteTextView;
    private TextView selectedText;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    //**********************************************************

    public static SearchEventFragment getNewInstance() {
        return new SearchEventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        acs_category = view.findViewById(R.id.category);
        et_distance = view.findViewById(R.id.distance);
        acs_unit = view.findViewById(R.id.unit);
        rg_from = view.findViewById(R.id.from);
        rb_currentLocation = view.findViewById(R.id.current_location);
        rb_otherLocation = view.findViewById(R.id.other_location);
        et_locationName = view.findViewById(R.id.location_name);
        btn_search = view.findViewById(R.id.btn_search);
        btn_clear = view.findViewById(R.id.btn_clear);
        tv_invalidKeyword = view.findViewById(R.id.invalid_keyword);
        tv_invalidLocName = view.findViewById(R.id.invalid_locationName);


        //**************************************************************
        autoCompleteTextView =
                view.findViewById(R.id.auto_complete_edit_text);
        selectedText = view.findViewById(R.id.selected_item);
        autoSuggestAdapter = new AutoSuggestAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });
    //**************************************************************
        return view;
    }

    private void makeApiCall(String text) {
        AutoCompleteApiCall.make(getContext(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONObject("_embedded").getJSONArray("attractions");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        location = LocationUtility.getInstance(getActivity()).showLocation();

        info = new SearchParams();
        info.setLat(location.getLatitude());
        info.setLon(location.getLongitude());
        info.setLocationName("");

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_invalidKeyword.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        et_locationName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tv_invalidLocName.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        acs_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_category = adapterView.getItemAtPosition(i).toString();
                if (selected_category.equals("Arts & Theatre")) {
                    info.setCategory("ArtsAndTheatre");
                } else {
                    info.setCategory(selected_category);
                }
                Log.i("category", info.getCategory());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                info.setCategory("All");
            }
        });

        acs_unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_unit = adapterView.getItemAtPosition(i).toString().toLowerCase();
                info.setUnit(selected_unit);
                Log.i("unit", info.getUnit());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                info.setUnit("miles");
            }
        });

        rg_from.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == rb_currentLocation.getId()) {
                    flag = "local";
                    et_locationName.setEnabled(false);
                    info.setLat(location.getLatitude());
                    info.setLon(location.getLongitude());
                    info.setLocationName("");
                    tv_invalidLocName.setVisibility(View.GONE);
                    Log.i("currentLoc", Double.valueOf(info.getLat()).toString());
                } else if (i == rb_otherLocation.getId()) {
                    flag = "other";
                    et_locationName.setEnabled(true);
                    Log.i("otherLoc", flag);
                }
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.setKeyword(autoCompleteTextView.getText().toString());
                Log.i("autocompletetext", "onClick: " + autoCompleteTextView.getText());
                if (0 != et_distance.getText().toString().trim().length()) {
                    try {
                        int temp = Math.round(Integer.parseInt(et_distance.getText().toString()));
                        info.setDistance(temp);
                        Log.i("distance", Integer.valueOf(temp).toString());
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Distance Should be Number", Toast.LENGTH_LONG).show();
                    }
                } else {
                    info.setDistance(10);
                }

                if (flag.equals("other")) {
                    if (0 == autoCompleteTextView.getText().toString().trim().length()) {
                        tv_invalidKeyword.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                    }
                    if (0 == et_locationName.getText().toString().trim().length()) {
                        tv_invalidLocName.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                    }
                    if (0 == et_locationName.getText().toString().trim().length() || 0 == autoCompleteTextView.getText().toString().trim().length()) {
                        return;
                    }
                    info.setLocationName(et_locationName.getText().toString());
                    fetchGeoLocation();
                } else {
                    if (0 == autoCompleteTextView.getText().toString().trim().length()) {
                        tv_invalidKeyword.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), "Please fix all fields with errors", Toast.LENGTH_LONG).show();
                        return;
                    }
                    info.setLocationName("");
                    fetchEventsResult();
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.setText("");
                selectedText.setText("");
                acs_category.setSelection(0);
                et_distance.setText("");
                acs_unit.setSelection(0);
                rb_currentLocation.setChecked(true);
                et_locationName.setText("");
                tv_invalidLocName.setVisibility(View.GONE);
                tv_invalidKeyword.setVisibility(View.GONE);
                et_locationName.setEnabled(false);
                myevents.clear();
            }
        });
    }

    private int fetchGeoLocation() {
        String params;
        params = "addr=" + info.getLocationName();
        String url = NetworkUtility.GET_GEOLOCATION + params;
        url = url.replaceAll("\\s+", "+");
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String _response) {
                        try {
                            if (new JSONObject(_response).optString("status").equals("OK")) {
                                String mylat = new JSONObject(_response).getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").optString("lat");
                                String mylon = new JSONObject(_response).getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").optString("lng");
                                info.setLat(Double.valueOf(mylat));
                                info.setLon(Double.valueOf(mylon));
                                Log.i("string", _response);
                                fetchEventsResult();
                            } else if (new JSONObject(_response).optString("status").equals("ZERO_RESULTS")) {
                                intent = new Intent(getActivity(), ZeroResultsActivity.class);
                                startActivity(intent);
                            } else if (new JSONObject(_response).optString("status").equals("INVALID_REQUEST")) {
                                Toast.makeText(getActivity(), "Invalid GeoLocation Request", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Unknown Request Error", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                Toast.makeText(getActivity(), "Network Failed", Toast.LENGTH_LONG).show();
            }
        });
        //excecute your request
        queue.add(stringRequest);

        Log.i("params", url);
        return 1;
    }

    private int fetchEventsResult() {
        String params;
        params = "keyword=" + info.getKeyword() + "&category=" + info.getCategory() + "&distance=" + info.getDistance() + "&unit=" + info.getUnit() + "&lat=" + info.getLat() + "&lon=" + info.getLon();
        String url = NetworkUtility.GET_EVETNS + params;
        url = url.replaceAll("\\s+", "+");
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String _response) {
                        String temp_segment = "Sports";
                        try {
                            JSONObject jsonObject = new JSONObject(_response).getJSONObject("_embedded");
                            JSONArray events = jsonObject.getJSONArray("events");
                            Log.i("eventlength", "onResponse: " + events.length());
                            for (int i = 0; i < events.length(); i++) {
                                Log.i("eventlength", "onResponse: " + events.getJSONObject(i));
                                Event event = new Event();
                                JSONObject temp = events.getJSONObject(i);
                                event.setId(temp.optString("id"));
                                event.setName(temp.optString("name"));
                                try {
                                    String segment = temp.getJSONArray("classifications").getJSONObject(0).getJSONObject("segment").getString("name");
                                    event.setCategory(segment);
                                    temp_segment = segment;
                                } catch (JSONException e) {
                                    event.setCategory(temp_segment);
                                }
                                try {
                                    String date = temp.getJSONObject("dates").getJSONObject("start").optString("localDate");
                                    String time = temp.getJSONObject("dates").getJSONObject("start").optString("localTime");
                                    event.setDate(date + " " + time);
                                    Log.i("eventdate", "onResponse: " + event.getDate());
                                } catch (JSONException e) {
                                    event.setDate("Unknown");
                                } finally {
                                    if (event.getDate().equals("")) {
                                        event.setDate("Unknown");
                                    }
                                }
                                try{
                                    String venueName = temp.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).optString("name");
                                    event.setVenueName(venueName);
                                    Log.i("venueName", "onResponse: " + event.getVenueName());
                                } catch (JSONException e) {
                                    event.setVenueName("Unknown");
                                }
                                event.setFavor(false);
                                myevents.add(event);
                            }
                            Log.i("totalEvents", "onResponse: " + myevents.toString());
                            intent = new Intent(getActivity(), ResultsListActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("events", myevents);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (JSONException e) {
                            intent = new Intent(getActivity(), ZeroResultsActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Error handling
                Toast.makeText(getActivity(), "Network Failed", Toast.LENGTH_LONG).show();
            }
        });
        //http://csci571hw9.us-east-2.elasticbeanstalk.com/ticketMaster/getEvents?keyword=lakers&category=Music&distance=10&unit=miles&lat=34.0223519&lon=-118.285117
        //excecute your request
        queue.add(stringRequest);

        return 1;
    }

}
