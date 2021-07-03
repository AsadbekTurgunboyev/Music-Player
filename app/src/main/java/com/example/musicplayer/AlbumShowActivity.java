package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.musicplayer.adapter.AlbumSongsAdapter;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;

public class AlbumShowActivity extends AppCompatActivity {
AlbumSongsAdapter adapter;

RecyclerView recyclerView;
ArrayList<SongsModel> songAlbum= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_show);
        initViews();
        int asd = 0;
        String albumName = getIntent().getStringExtra("albumName");
        for (int i = 0 ; i<arrayList.size(); i++){
            if(albumName.equals(arrayList.get(i).getAlbum())){
                songAlbum.add(asd,arrayList.get(i));
                asd++;
            }
        }

    }

    private void initViews() {
        recyclerView = findViewById(R.id.rec1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!(songAlbum.size() <1)){
           adapter = new AlbumSongsAdapter(this, songAlbum);
           recyclerView.setAdapter(adapter);
        }
    }
}