package com.example.lijie.csci571hw9.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lijie.csci571hw9.Entity.ArtistsAndPhoto;
import com.example.lijie.csci571hw9.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ArtistsPhotoAdapter extends RecyclerView.Adapter<ArtistsPhotoAdapter.MyViewHolder>{

    private ArrayList<ArtistsAndPhoto> artistsAndPhotoArrayList;
    private Context context;
    private boolean flag;

    public ArtistsPhotoAdapter(ArrayList<ArtistsAndPhoto> artistsAndPhotoArrayList, boolean flag, Context context) {
        this.artistsAndPhotoArrayList = artistsAndPhotoArrayList;
        this.flag = flag;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        if (flag) {
            holder.tv_title.setText(artistsAndPhotoArrayList.get(position).getName());
            holder.tv_followers.setText(artistsAndPhotoArrayList.get(position).getFollowersNum());
            holder.tv_popularity.setText(artistsAndPhotoArrayList.get(position).getPopularityNum());
            String text = holder.tv_url.getText().toString();
            SpannableString ss = new SpannableString(text);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Uri uri = Uri.parse(artistsAndPhotoArrayList.get(position).getUrl());
                    Intent myintent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(myintent);
                }
            };
            ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_url.setText(ss);
            holder.tv_url.setMovementMethod(LinkMovementMethod.getInstance());
            ArrayList<String> photoList = artistsAndPhotoArrayList.get(position).getPhotosList();
            if (photoList.size() == 1) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
            }
            if (photoList.size() == 2) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
            }
            if (photoList.size() == 3) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);

            }
            if (photoList.size() == 4) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
            }
            if (photoList.size() == 5) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
            }
            if (photoList.size() == 6) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
            }
            if (photoList.size() == 7) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
                Picasso.get().load(photoList.get(6)).into(holder.iv_photo7);
            }
            if (photoList.size() == 8) {
                Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
                Picasso.get().load(photoList.get(6)).into(holder.iv_photo7);
                Picasso.get().load(photoList.get(7)).into(holder.iv_photo8);
            }
        } else {
            holder.ll_table.setVisibility(View.GONE);
            holder.tv_title.setText(artistsAndPhotoArrayList.get(position).getName());
            ArrayList<String> photoList = artistsAndPhotoArrayList.get(position).getPhotosList();
            if (photoList.size() > 0) {
                if (photoList.size() == 1) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                }
                if (photoList.size() == 2) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                }
                if (photoList.size() == 3) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);

                }
                if (photoList.size() == 4) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                    Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                }
                if (photoList.size() == 5) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                    Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                    Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                }
                if (photoList.size() == 6) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                    Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                    Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                    Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
                }
                if (photoList.size() == 7) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                    Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                    Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                    Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
                    Picasso.get().load(photoList.get(6)).into(holder.iv_photo7);
                }
                if (photoList.size() == 8) {
                    Picasso.get().load(photoList.get(0)).into(holder.iv_photo1);
                    Picasso.get().load(photoList.get(1)).into(holder.iv_photo2);
                    Picasso.get().load(photoList.get(2)).into(holder.iv_photo3);
                    Picasso.get().load(photoList.get(3)).into(holder.iv_photo4);
                    Picasso.get().load(photoList.get(4)).into(holder.iv_photo5);
                    Picasso.get().load(photoList.get(5)).into(holder.iv_photo6);
                    Picasso.get().load(photoList.get(6)).into(holder.iv_photo7);
                    Picasso.get().load(photoList.get(7)).into(holder.iv_photo8);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if (null == artistsAndPhotoArrayList || artistsAndPhotoArrayList.size() == 0) {
            return 0;
        } else {
            return artistsAndPhotoArrayList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_title;
        private LinearLayout ll_table;
        private TextView tv_name;
        private TextView tv_followers;
        private TextView tv_popularity;
        private TextView tv_url;

        private ImageView iv_photo1;
        private ImageView iv_photo2;
        private ImageView iv_photo3;
        private ImageView iv_photo4;
        private ImageView iv_photo5;
        private ImageView iv_photo6;
        private ImageView iv_photo7;
        private ImageView iv_photo8;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.item_artist_title);
            ll_table = itemView.findViewById(R.id.item_artist_table);
            tv_name = itemView.findViewById(R.id.item_artist_name);
            tv_followers = itemView.findViewById(R.id.item_artist_followers);
            tv_popularity = itemView.findViewById(R.id.item_artist_popularity);
            tv_url = itemView.findViewById(R.id.item_artist_url);
            iv_photo1 = itemView.findViewById(R.id.item_artist_1);
            iv_photo2 = itemView.findViewById(R.id.item_artist_2);
            iv_photo3 = itemView.findViewById(R.id.item_artist_3);
            iv_photo4 = itemView.findViewById(R.id.item_artist_4);
            iv_photo5 = itemView.findViewById(R.id.item_artist_5);
            iv_photo6 = itemView.findViewById(R.id.item_artist_6);
            iv_photo7 = itemView.findViewById(R.id.item_artist_7);
            iv_photo8 = itemView.findViewById(R.id.item_artist_8);
        }
    }
}
