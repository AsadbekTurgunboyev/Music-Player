package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.adapter.SongsAdapter;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;


public class AlbumFragment extends Fragment {

    RecyclerView recyclerView;
    AlbumAdapter albumAdapter;
    ArrayList<SongsModel> albumList;
    public AlbumFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        initView(view);

        if(!(arrayList.size()<1)){
            albumAdapter = new AlbumAdapter(getContext(),arrayList);
            recyclerView.setAdapter(albumAdapter);
        }
        return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recyAlbum);
    }
}