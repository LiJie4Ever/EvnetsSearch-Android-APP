package com.example.lijie.csci571hw9.Fragment;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.lijie.csci571hw9.Adapter.ArtistsPhotoAdapter;
import com.example.lijie.csci571hw9.Entity.ArtistsAndPhoto;
import com.example.lijie.csci571hw9.R;
import com.example.lijie.csci571hw9.Utils.NetworkUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistsPhotoFragment extends Fragment {

    private ArrayList<String> artistsList;
    private ArrayList<ArtistsAndPhoto> artistsAndPhotosLists;
    private String segment;
    private boolean flag;

    private RecyclerView recyclerView;
    private TextView tv_noRecord;
    private ArtistsPhotoAdapter artistsPhotoAdapter;

    public ArtistsPhotoFragment() {
        artistsAndPhotosLists = new ArrayList<>();
    }

    public static ArtistsPhotoFragment getArtistsPhotoFragment(ArrayList<String> artistsList, String segment) {
        ArtistsPhotoFragment artistsPhotoFragment = new ArtistsPhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("artistsList", artistsList);
        bundle.putString("segment", segment);
        artistsPhotoFragment.setArguments(bundle);
        return artistsPhotoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        artistsList = getArguments().getStringArrayList("artistsList");
        for (int i = 0; i <artistsList.size(); i++) {
            Log.i("artistsName", "onCreate: " + artistsList.get(i));
        }
        segment = getArguments().getString("segment");
        if (null != segment && segment.equals("Music")) {
            flag = true;
        } else {
            flag = false;
        }

        for (int i = 0; i < artistsList.size(); i++) {
            if (flag) {
                getArtistsInfo(artistsList.get(i));
            } else {
                ArtistsAndPhoto artistsAndPhoto = new ArtistsAndPhoto();
                artistsAndPhoto.setName(artistsList.get(i));
                artistsAndPhotosLists.add(artistsAndPhoto);
                getArtistPhoto(artistsAndPhoto);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists_photo, container, false);
        recyclerView = view.findViewById(R.id.detail_photo_recyclerview);
        tv_noRecord = view.findViewById(R.id.detail_photo_noRecord);

        if (null == artistsAndPhotosLists || artistsAndPhotosLists.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            tv_noRecord.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tv_noRecord.setVisibility(View.GONE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        artistsPhotoAdapter = new ArtistsPhotoAdapter(artistsAndPhotosLists, flag, getContext());
        recyclerView.setAdapter(artistsPhotoAdapter);

        return view;

    }

    private int getArtistsInfo(final String artistName) {
        //music
        String params;
        params = "artistName=" + artistName;
        String url = NetworkUtility.GET_ARTISTINFO + params;
        url = url.replaceAll("\\s+", "+");
        Log.i("artisturl", "getArtistsInfo: " + url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("artistinfo", "onResponse: " + response);
                ArtistsAndPhoto artistsAndPhoto = new ArtistsAndPhoto();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray artistsList = jsonObject.getJSONObject("artists").getJSONArray("items");
                    JSONObject artist = null;
                    String uri = null;
                    if (null != artistsList && artistsList.length() > 0) {
                        for (int i = 0; i < artistsList.length(); i++) {
                            if (null != artistsList.getJSONObject(i).optString("name") && artistsList.getJSONObject(i).optString("name").equals(artistName) && !artistsList.getJSONObject(i).optString("name").equals("null")){
                                artist = artistsList.getJSONObject(i);
                                Log.i("artisturl", "getArtistsInfo: " + artist);
                                break;
                            }
                        }
                    }
                    if (null != artist) {
                        artistsAndPhoto.setName(artist.optString("name"));
                        Log.i("artisturl", "getArtistsInfo: " + artist.optString("name"));
                        artistsAndPhoto.setFollowersNum(artist.getJSONObject("followers").optString("total"));
                        artistsAndPhoto.setPopularityNum(artist.optString("popularity"));
                        uri = artist.optString("uri");
                    }
                    if (null != uri) {
                        String[] words = uri.split(":");
                        uri = "https://open." + words[0] + ".com/" + words[1] + "/" + words[2];
                        artistsAndPhoto.setUrl(uri);
                    }
                    Log.i("artistobj", "onResponse: " + artistsAndPhoto.getName());
                    Log.i("artistobj", "onResponse: " + artistsAndPhoto.getFollowersNum());
                    Log.i("artistobj", "onResponse: " + artistsAndPhoto.getPopularityNum());
                    Log.i("artistobj", "onResponse: " + artistsAndPhoto.getUrl());
                    artistsAndPhotosLists.add(artistsAndPhoto);
                    getArtistPhoto(artistsAndPhoto);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("artist_info", "onErrorResponse: " + error);
            }
        });

        queue.add(stringRequest);
        return 1;
    }

    private int getArtistPhoto(final ArtistsAndPhoto artistsAndPhoto) {
        String params;
        params = "keyword=" + artistsAndPhoto.getName();
        String url = NetworkUtility.GET_PHOTOS + params;
        url = url.replaceAll("\\s+", "+");
        Log.i("artistobj", "onResponse: " + url);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> photoList = artistsAndPhoto.getPhotosList();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray items = jsonObject.getJSONArray("items");
                    int num = 0;
                    if (items.length() >= 8) {
                        num = 8;
                    } else {
                        num = items.length();
                    }
                    for (int i = 0; i < num; i++) {
                        String temp = items.getJSONObject(i).optString("link");
                        Log.i("photolink2", "onResponse: " + temp);
                        photoList.add(temp);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("artist_photo", "onErrorResponse: " + error);
            }
        });

        queue.add(stringRequest);
        return 1;
    }
}
