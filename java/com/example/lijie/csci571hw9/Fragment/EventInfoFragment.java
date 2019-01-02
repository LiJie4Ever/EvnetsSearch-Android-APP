package com.example.lijie.csci571hw9.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lijie.csci571hw9.R;

import java.util.ArrayList;

public class EventInfoFragment extends Fragment {

    private String name;
    private String venueName;
    private String date;
    private ArrayList<String> classifications;
    private String maxPrice;
    private String minPrice;
    private String status;
    private String url;
    private String seatMapUrl;

    private TextView tv_name;
    private TextView tv_venueName;
    private TextView tv_Time;
    private TextView tv_category;
    private TextView tv_price;
    private TextView tv_status;
    private TextView tv_url;
    private TextView tv_seatMap_url;

    private LinearLayout ll_name;
    private LinearLayout ll_venueName;
    private LinearLayout ll_time;
    private LinearLayout ll_category;
    private LinearLayout ll_price;
    private LinearLayout ll_status;
    private LinearLayout ll_url;
    private LinearLayout ll_seatMap_url;



    public EventInfoFragment() {

    }

    public static EventInfoFragment getEventInfoFragment(String name, String venueName, String date, ArrayList<String> classifications, String maxPrice, String minPrice, String status, String url, String seatMapUrl) {
        EventInfoFragment eventInfoFragment = new EventInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("venueName", venueName);
        bundle.putString("date", date);
        bundle.putStringArrayList("classifications", classifications);
        bundle.putString("maxPrice", maxPrice);
        bundle.putString("minPrice", minPrice);
        bundle.putString("status", status);
        bundle.putString("url", url);
        bundle.putString("seatMapUrl", seatMapUrl);

        eventInfoFragment.setArguments(bundle);
        return eventInfoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {
            name = getArguments().getString("name");
            venueName = getArguments().getString("venueName");
            date = getArguments().getString("date");
            classifications = getArguments().getStringArrayList("classifications");
            maxPrice = getArguments().getString("maxPrice");
            minPrice = getArguments().getString("minPrice");
            status = getArguments().getString("status");
            url = getArguments().getString("url");
            seatMapUrl = getArguments().getString("seatMapUrl");
            Log.i("fragemntDetail", "getEventInfoFragment: " + date);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_info, container, false);
        tv_name = view.findViewById(R.id.event_detail_name);
        tv_venueName = view.findViewById(R.id.event_detail_venue);
        tv_Time = view.findViewById(R.id.event_detail_time);
        tv_category = view.findViewById(R.id.event_detail_category);
        tv_price = view.findViewById(R.id.event_detail_price);
        tv_status = view.findViewById(R.id.event_detail_status);
        tv_url = view.findViewById(R.id.event_detail_buyTicket);
        tv_seatMap_url = view.findViewById(R.id.event_detail_seat);

        ll_name = view.findViewById(R.id.event_name_info);
        ll_venueName = view.findViewById(R.id.event_venue_info);
        ll_time = view.findViewById(R.id.event_time_info);
        ll_category = view.findViewById(R.id.event_category_info);
        ll_price = view.findViewById(R.id.event_price_info);
        ll_status = view.findViewById(R.id.event_status_info);
        ll_url = view.findViewById(R.id.event_ticket_info);
        ll_seatMap_url = view.findViewById(R.id.event_seat_info);

        if (null != name) {
            tv_name.setText(name);
        } else {
            ll_name.setVisibility(View.GONE);
        }

        if (!venueName.equals("")) {
            tv_venueName.setText(venueName);
        } else {
            ll_venueName.setVisibility(View.GONE);
        }

        if (null != date) {
            String[] str = date.split("\\s+");
            String[] datePart;
            String monthName;
            if (!str[0].equals("") && !str[1].equals("")) {
                datePart = str[0].split("-");
                monthName = convertMonth(datePart[1]);
                tv_Time.setText(monthName + " " + datePart[2] + ", " + datePart[0] + " " + str[1]);
            } else if (!str[0].equals("")) {
                datePart = str[0].split("-");
                monthName = convertMonth(datePart[1]);
                tv_Time.setText(monthName + " " + datePart[2] + ", " + datePart[0]);
            } else {
               tv_Time.setText(str[1]);
            }
        } else {
            ll_time.setVisibility(View.GONE);
        }

        String sets = "";
        if (classifications.size() > 0) {
            for (int i = 0; i < classifications.size() - 1; i++) {
                if (!classifications.get(i).equals("")) {
                    sets += classifications.get(i) + " | ";
                }
            }
            sets += classifications.get(classifications.size() - 1);
        }

        if (sets.trim().equals("")) {
            ll_category.setVisibility(View.GONE);
        } else {
            tv_category.setText(sets);
        }

        if (null != maxPrice && null != minPrice) {
            tv_price.setText("$" + minPrice + " ~ " + "$" + maxPrice);
        } else if (!maxPrice.equals("")) {
            tv_price.setText("$" + maxPrice);
        } else if (!minPrice.equals("")){
            tv_price.setText("$" + minPrice);
        } else {
            ll_price.setVisibility(View.GONE);
        }

        if (null != status) {
            tv_status.setText(status);
        } else {
            ll_status.setVisibility(View.GONE);
        }

        if (null != url) {
            String text = tv_url.getText().toString();
            SpannableString ss = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Uri uri = Uri.parse(url);
                    Intent myintent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(myintent);
                }
            };
            ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_url.setText(ss);
            tv_url.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            ll_url.setVisibility(View.GONE);
        }

        if (null != seatMapUrl) {
            String text = tv_seatMap_url.getText().toString();
            SpannableString ss = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Uri uri = Uri.parse(seatMapUrl);
                    Intent myintent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(myintent);
                }
            };
            ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv_seatMap_url.setText(ss);
            tv_seatMap_url.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            ll_seatMap_url.setVisibility(View.GONE);
        }

        return view;

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
