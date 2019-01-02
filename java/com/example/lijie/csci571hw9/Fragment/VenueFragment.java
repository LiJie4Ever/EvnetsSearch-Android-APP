package com.example.lijie.csci571hw9.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;


public class VenueFragment extends Fragment {

    private double lat;
    private double lon;
    private String venueName;
    private String address;
    private String city;
    private String openHours;
    private String generalRule;
    private String childRule;
    private String phoneNumber;
    private GoogleMap map;

    private TextView tv_name;
    private TextView tv_addr;
    private TextView tv_city;
    private TextView tv_openHours;
    private TextView tv_generalRule;
    private TextView tv_childRule;
    private TextView tv_phoneNumber;
    private MapView mapView;

    private LinearLayout ll_name;
    private LinearLayout ll_addr;
    private LinearLayout ll_city;
    private LinearLayout ll_openHours;
    private LinearLayout ll_generalRule;
    private LinearLayout ll_childRule;
    private LinearLayout ll_phoneNumber;

    public VenueFragment() {

    }

    public static VenueFragment getVenueFragment(String venueName, double lat, double lon) {
        VenueFragment venueFragment = new VenueFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lon", lon);
        bundle.putString("venueName", venueName);
        venueFragment.setArguments(bundle);
        return venueFragment;
}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (null != getArguments()) {
            venueName = getArguments().getString("venueName");
            lat = getArguments().getDouble("lat");
            lon = getArguments().getDouble("lon");
        }
        GetVenueInfo(venueName);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int i = 0;
            }
        }, 5000);
        View view = inflater.inflate(R.layout.fragment_venue, container, false);
        tv_name = view.findViewById(R.id.event_venue_name);
        tv_addr = view.findViewById(R.id.event_venue_addr);
        tv_city = view.findViewById(R.id.event_venue_city);
        tv_openHours = view.findViewById(R.id.event_venue_openhours);
        tv_phoneNumber = view.findViewById(R.id.event_venue_phone);
        tv_generalRule = view.findViewById(R.id.event_venue_rule);
        tv_childRule = view.findViewById(R.id.event_venue_childRule);
        mapView = view.findViewById(R.id.mapView);

        ll_name = view.findViewById(R.id.venue_name_info);
        ll_addr = view.findViewById(R.id.venue_addr_info);
        ll_city = view.findViewById(R.id.venue_city_info);
        ll_openHours = view.findViewById(R.id.venue_openhour_info);
        ll_phoneNumber = view.findViewById(R.id.venue_phone_info);
        ll_generalRule = view.findViewById(R.id.venue_rule_info);
        ll_childRule = view.findViewById(R.id.venue_child_info);

        if (null != venueName) {
            tv_name.setText(venueName);
        } else {
            ll_name.setVisibility(View.GONE);
        }

        if (null != address && !address.equals("")) {
            tv_addr.setText(address);
        } else {
            ll_addr.setVisibility(View.GONE);
        }

        if (null != city && !city.equals("")) {
            tv_city.setText(city);
        } else {
            ll_city.setVisibility(View.GONE);
        }

        if (null != openHours && !openHours.equals("")) {
            tv_openHours.setText(openHours);
        } else {
            ll_openHours.setVisibility(View.GONE);
        }

        if (null != phoneNumber && !phoneNumber.equals("")) {
            tv_phoneNumber.setText(phoneNumber);
            Log.i("tv_phoneNumber", "onCreateView: " + phoneNumber);
        } else {
            ll_phoneNumber.setVisibility(View.GONE);
        }

        if (null != generalRule && !generalRule.equals("")) {
            tv_generalRule.setText(generalRule);
        } else {
            ll_generalRule.setVisibility(View.GONE);
        }

        if (null != childRule && !childRule.equals("")) {
            tv_childRule.setText(childRule);
        } else {
            ll_childRule.setVisibility(View.GONE);
        }

        setMapReadyListener(savedInstanceState);

        return view;
    }

    private void setMapReadyListener(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity().getApplicationContext());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                LatLng latLng = new LatLng(lat, lon);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("check", "onMapReady: check");
                    return;
                }
                map.setMyLocationEnabled(true);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                map.addMarker(new MarkerOptions().position(latLng).title(venueName));
            }
        });
    }

    private void GetVenueInfo(final String venueName){
    String params = "veneuName=" + venueName;
    String url = NetworkUtility.GET_VENUE + params;
    url = url.replaceAll("\\s+", "+");
    RequestQueue queue = Volley.newRequestQueue(getContext());

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                String city;
                JSONObject jsonObject = new JSONObject(response);
                JSONObject venueData = jsonObject.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);

                address = venueData.getJSONObject("address").optString("line1");
                String cityName = venueData.getJSONObject("city").optString("name");
                String stateName = venueData.getJSONObject("state").optString("name");
                if (!cityName.equals("") && !stateName.equals("")) {
                    city = cityName + ", " + stateName;
                } else if (!cityName.equals("")) {
                    city = cityName;
                } else {
                    city = stateName;
                }
                phoneNumber = venueData.getJSONObject("boxOfficeInfo").optString("phoneNumberDetail");
                openHours = venueData.getJSONObject("boxOfficeInfo").optString("openHoursDetail");
                generalRule = venueData.getJSONObject("generalInfo").optString("generalRule");
                childRule = venueData.getJSONObject("generalInfo").optString("childRule");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.i("venue_response", "onErrorResponse: " + error);
        }
    });
    queue.add(stringRequest);
}

}
