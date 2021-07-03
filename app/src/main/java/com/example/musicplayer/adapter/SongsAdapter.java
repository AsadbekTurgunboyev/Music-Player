package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.ListenActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.MyViewHolder> {
    Context context;
    ArrayList<SongsModel> list;

    public SongsAdapter(Context context, ArrayList<SongsModel> arrayList) {
        this.context = context;
        this.list = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.music_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.songTitle.setText(list.get(position).getTitle());
        holder.songArtist.setText(list.get(position).getArtist());
       byte image[] = getAlbum(list.get(position).getPath());
        if(image !=null){
            Glide.with(context).asBitmap().load(image).into(holder.songImage);
        }else {
            Glide.with(context).asBitmap().load(R.drawable.shape).into(holder.songImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListenActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView songImage;
        TextView songTitle, songArtist;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songImage = itemView.findViewById(R.id.songImage);
            songTitle = itemView.findViewById(R.id.textView);
            songArtist = itemView.findViewById(R.id.textView2);
        }
    }
    private byte[] getAlbum(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte [] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }
}
