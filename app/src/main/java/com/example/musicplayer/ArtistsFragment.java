package com.example.musicplayer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.adapter.ArtistAdapter;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;
import static com.example.musicplayer.MainActivity.artistList;

public class ArtistsFragment extends Fragment {
    RecyclerView recyclerView;
    ArrayList <SongsModel> ArtistList = new ArrayList<>();
    ArtistAdapter adapter;

    public ArtistsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_artists,container,false);
       initViews(view);



        if(!(ArtistList.size()<1)){
            ArtistList.clear();
            ArtistList = artistList;

            adapter = new ArtistAdapter(ArtistList,getContext());
        }else {
            ArtistList = artistList;
            adapter = new ArtistAdapter(ArtistList,getContext());
        }

        recyclerView.setAdapter(adapter);



       return view;
    }

    private void initViews(View view) {
    recyclerView = view.findViewById(R.id.recArtist);
    }

}