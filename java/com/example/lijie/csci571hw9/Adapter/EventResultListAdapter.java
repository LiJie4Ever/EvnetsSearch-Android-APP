package com.example.lijie.csci571hw9.Adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lijie.csci571hw9.Entity.Event;
import com.example.lijie.csci571hw9.Listeners.myOnItemClickListener;
import com.example.lijie.csci571hw9.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventResultListAdapter extends RecyclerView.Adapter<EventResultListAdapter.MyViewHolder> implements View.OnClickListener {

    private ArrayList<Event> myevents;
    private LayoutInflater inflater;
    private Context context;
    private myOnItemClickListener myOnItemClickListener;

    public EventResultListAdapter(Context context, ArrayList<Event> events) {
        this.myevents = events;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.myOnItemClickListener = null;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_event, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        viewHolder.favorClickListener = myOnItemClickListener;
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (myevents.get(position).getCategory().equals("Sports")) {
            Picasso.get().load("http://csci571.com/hw/hw9/images/android/sport_icon.png").into(holder.iv_segment);
        } else if (myevents.get(position).getCategory().equals("Music")) {
            Picasso.get().load("http://csci571.com/hw/hw9/images/android/music_icon.png").into(holder.iv_segment);
        } else if (myevents.get(position).getCategory().equals("Arts & Theatre")) {
            Picasso.get().load("http://csci571.com/hw/hw9/images/android/art_icon.png").into(holder.iv_segment);
        } else if (myevents.get(position).getCategory().equals("Miscellaneous")) {
            Picasso.get().load("http://csci571.com/hw/hw9/images/android/miscellaneous_icon.png").into(holder.iv_segment);
        } else if (myevents.get(position).getCategory().equals("Film")) {
            Picasso.get().load("http://csci571.com/hw/hw9/images/android/film_icon.png").into(holder.iv_segment);
        }
        holder.tv_name.setText(myevents.get(position).getName());
        holder.tv_venueName.setText(myevents.get(position).getVenueName());
        holder.tv_date.setText(myevents.get(position).getDate());
        holder.itemView.setTag(position);

        if (myevents.get(position).isFavor()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_favorite.setImageDrawable(context.getDrawable(R.drawable.heart_fill_red));
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.iv_favorite.setImageDrawable(context.getDrawable(R.drawable.heart_outline_black));
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == myevents) {
            return 0;
        } else {
            return myevents.size();
        }
    }

    @Override
    public void onClick(View view) {
        if (null != myOnItemClickListener) {
            myOnItemClickListener.onItemClick(view, (int)(view.getTag()));
        }
    }

    public void reSetEvents(ArrayList<Event> myevents) {
        this.myevents = myevents;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public myOnItemClickListener favorClickListener;

        public ImageView iv_segment;
        public TextView tv_name;
        public TextView tv_venueName;
        public TextView tv_date;
        public ImageView iv_favorite;

        public MyViewHolder(View itemView) {
            super(itemView);
            iv_segment = itemView.findViewById(R.id.event_segment);
            tv_name = itemView.findViewById(R.id.event_name);
            tv_venueName = itemView.findViewById(R.id.event_venueName);
            tv_date = itemView.findViewById(R.id.event_date);
            iv_favorite = itemView.findViewById(R.id.favorite_item);
            iv_favorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (null != favorClickListener) {
                favorClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setMyOnItemClickListener(myOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }
}
