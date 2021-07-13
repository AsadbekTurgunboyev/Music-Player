package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.AlbumShowActivity;
import com.example.musicplayer.ListenActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;

public class AlbumSongsAdapter extends RecyclerView.Adapter<AlbumSongsAdapter.myViewHolder> {
    ArrayList<SongsModel> songsAlbum;
    int i ;
    Context context;

    public AlbumSongsAdapter(Context albumShowActivity, ArrayList<SongsModel> songAlbum) {
        this.context = albumShowActivity;
        this.songsAlbum = songAlbum;
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.album_item,parent,false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.songTitleAlbum.setText(songsAlbum.get(position).getTitle());
        holder.songArtistAlbum.setText(songsAlbum.get(position).getAlbum());
        byte[] img = getAlbum(songsAlbum.get(position).getPath());
        if(img != null){

            Glide.with(context).asBitmap().load(img).into(holder.songImageAlbum);
        }else {

            Glide.with(context).asBitmap().load(R.drawable.shape).into(holder.songImageAlbum);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListenActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("key","Kalit");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsAlbum.size();
    }

    public class myViewHolder extends  RecyclerView.ViewHolder {
        ImageView songImageAlbum;
        TextView songTitleAlbum, songArtistAlbum;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            songImageAlbum = itemView.findViewById(R.id.songImageAlbum);
            songTitleAlbum = itemView.findViewById(R.id.textViewAlbum);
            songArtistAlbum = itemView.findViewById(R.id.textView2Album);
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
