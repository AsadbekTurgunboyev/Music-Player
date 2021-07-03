 package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.model.SongsModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
        ViewPager viewPager;
        TabLayout tabLayout;
        ViewPagerAdapter adapter;
        int REQUEST_CODE = 1;
        public  static ArrayList <SongsModel> arrayList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        permission();

    }

    private void initViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void permission() {
    if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
      ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
    } else {
        arrayList = getAudio(this);
        initViewPager();

    }
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
    }


    public static ArrayList<SongsModel> getAudio (Context context){
        ArrayList<SongsModel> audioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] project = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
        };
        Cursor cursor = context.getContentResolver().query(uri,project,null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                String album = cursor.getString(0);
                String artist = cursor.getString(1);
                String title = cursor.getString(2);
                String duration = cursor.getString(3);
                String data = cursor.getString(4);
                Log.e("Path",data);
                SongsModel model = new SongsModel(title,artist,duration,data,album);
                audioList.add(model);
            }
            cursor.close();

        }
        return audioList;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                arrayList = getAudio(this);
                initViewPager();

            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
    }
}