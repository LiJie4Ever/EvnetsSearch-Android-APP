package com.example.lijie.csci571hw9.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lijie.csci571hw9.Entity.UpcomingEvents;
import com.example.lijie.csci571hw9.Listeners.myOnItemClickListener;
import com.example.lijie.csci571hw9.R;

import java.util.ArrayList;

public class UpcomingListRecyclerAdapter extends RecyclerView.Adapter<UpcomingListRecyclerAdapter.MyViewHolder> implements View.OnClickListener{

    private ArrayList<UpcomingEvents> upcomingEventsArrayList;
    private Context context;
    private myOnItemClickListener myOnItemClickListener;

    @NonNull
    @Override
    public UpcomingListRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_upcoming_event, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(this);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingListRecyclerAdapter.MyViewHolder holder, int position) {
        if (null != upcomingEventsArrayList.get(position).getName()) {
            holder.tv_name.setText(upcomingEventsArrayList.get(position).getName());
        } else {
            holder.tv_name.setText("");
        }
        if (null != upcomingEventsArrayList.get(position).getArtist()) {
            holder.tv_artist.setText(upcomingEventsArrayList.get(position).getArtist());
        } else {
            holder.tv_artist.setText("");
        }
        if (null != upcomingEventsArrayList.get(position).getDate()) {
            holder.tv_time.setText(upcomingEventsArrayList.get(position).getDate());
        } else {
            holder.tv_time.setText("");
        }
        if (null != upcomingEventsArrayList.get(position).getType()) {
            holder.tv_type.setText(upcomingEventsArrayList.get(position).getType());
        } else {
            holder.tv_type.setText("");
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        if (null == upcomingEventsArrayList) {
            return 0;
        } else {
            return upcomingEventsArrayList.size();
        }
    }

    @Override
    public void onClick(View view) {
        if (null != myOnItemClickListener) {
            myOnItemClickListener.onItemClick(view, (int)(view.getTag()));
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_artist;
        public TextView tv_time;
        public TextView tv_type;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.item_upcoming_event_name);
            tv_artist = itemView.findViewById(R.id.item_upcoming_event_artist);
            tv_time = itemView.findViewById(R.id.item_upcoming_event_date);
            tv_type = itemView.findViewById(R.id.item_upcoming_event_type);
        }
    }

    public UpcomingListRecyclerAdapter(ArrayList<UpcomingEvents> upcomingEventsArrayList, Context context) {
        this.upcomingEventsArrayList = upcomingEventsArrayList;
        this.context = context;
        myOnItemClickListener = null;
    }

    public void reSetUpcomingEventsList(ArrayList<UpcomingEvents> upcomingEventsArrayList) {
        this.upcomingEventsArrayList = upcomingEventsArrayList;
    }

    public void reSetMyOnItemClickListener(myOnItemClickListener listener) {
        this.myOnItemClickListener = listener;
    }
}
