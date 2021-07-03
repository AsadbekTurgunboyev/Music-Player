package com.example.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicplayer.adapter.SongsAdapter;
import com.example.musicplayer.model.SongsModel;

import java.util.ArrayList;

import static com.example.musicplayer.MainActivity.arrayList;


public class SongsFragment extends Fragment {
        RecyclerView recyclerView;
        SongsAdapter adapter ;

    public SongsFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_songs, container, false);
        initView(view);
        if(!(arrayList.size()<1)){
            adapter = new SongsAdapter(getContext(),arrayList);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));
        }




       return view;
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.rec);
    }




}