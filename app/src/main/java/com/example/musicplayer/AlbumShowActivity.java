package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicplayer.adapter.AlbumSongsAdapter;
import com.example.musicplayer.model.SongsModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.musicplayer.MainActivity.arrayList;

public class AlbumShowActivity extends AppCompatActivity {
    AlbumSongsAdapter adapter;
    RecyclerView recyclerView;
    CollapsingToolbarLayout collapsingToolbarLayout;
    ImageView albumImage;
    String albumName;
    String albumImg;
    public static ArrayList<SongsModel> albumArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_show);
        initViews();
        String key = getIntent().getExtras().getString("kalit");
        if( key != null && key.equals("key")){
            albumName = getIntent().getExtras().getString("artist");
            albumImg = getIntent().getExtras().getString("artisImage");
            getArtistList();
        }else {
            albumName = getIntent().getExtras().getString("albumName");
            albumImg =  getIntent().getExtras().getString("albumImage");
            getList();
        }


        collapsingToolbarLayout.setTitle(albumName);

        byte[] img = getAlbum(albumImg);
        Glide.with(this).asBitmap().load(img).into(albumImage);



    }

    private void getArtistList() {
        albumArraylist.clear();
        int index = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getArtist().equals(albumName)) {
                albumArraylist.add(index, arrayList.get(i));
                index++;
            }
        }
    }

    private void getList() {
        albumArraylist.clear();
        int index = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getAlbum().equals(albumName)) {
                albumArraylist.add(index, arrayList.get(i));
                index++;
            }
        }
    }

    private void initViews() {
        recyclerView = findViewById(R.id.rec1);
        albumImage = findViewById(R.id.imageView2);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new AlbumSongsAdapter(this, albumArraylist);
        recyclerView.setAdapter(adapter);

    }

    private byte[] getAlbum(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }
}