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
import com.example.musicplayer.R;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.Viewholder> {
    ArrayList<SongsModel> mList;
    Context context;

    public ArtistAdapter(ArrayList<SongsModel> arrayList, Context context) {
        this.mList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false);

        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.artistName.setText(mList.get(position).getArtist());
        int l = 0;
        for(int i = 0; i<arrayList.size(); i++){
           if(arrayList.get(i).getArtist().equals(mList.get(position).getArtist())){
               l++;
           }
        }
        holder.countSongs.setText(l + " qo'shiq");
        byte [] art = getAlbum(mList.get(position).getPath());
        if( art != null){

            Glide.with(context).asBitmap().load(art).into(holder.artistImage);
        }else {
            Glide.with(context).asBitmap().load(R.drawable.shape).into(holder.artistImage);

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AlbumShowActivity.class);
                intent.putExtra("kalit","key");
                intent.putExtra("artist",mList.get(position).getArtist());
                intent.putExtra("artisImage",mList.get(position).getPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView artistName, countSongs;
        ImageView artistImage;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            artistName = itemView.findViewById(R.id.textViewArtist);
            countSongs = itemView.findViewById(R.id.textView2Artist);
            artistImage = itemView.findViewById(R.id.songImageArtist);
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
