 package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import android.view.Menu;
import android.view.MenuItem;

import com.example.musicplayer.adapter.ViewPagerAdapter;
import com.example.musicplayer.model.SongsModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
        ViewPager viewPager;
        TabLayout tabLayout;
        ViewPagerAdapter adapter;
        int REQUEST_CODE = 1;
        public  static ArrayList <SongsModel> arrayList ;
        public  static ArrayList <SongsModel> artistList = new ArrayList<>();
        public static ArrayList <SongsModel> albums = new ArrayList<>();

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
        artistList.clear();
        albums.clear();
        ArrayList<SongsModel> audioList = new ArrayList<>();
        ArrayList<String> ikki = new ArrayList<>();
        ArrayList<String> artistL = new ArrayList<>();
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
                if(!ikki.contains(album)){
                    albums.add(model);
                    ikki.add(album);

                }
                if(!artistL.contains(artist)){
                    artistList.add(model);
                    artistL.add(artist);
                }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<SongsModel> searchList = new ArrayList<>();
        String kichkina = newText.toLowerCase();
        for(SongsModel music: arrayList){
            if(music.getTitle().toLowerCase().contains(kichkina)){
                searchList.add(music);
            }
        }
        SongsFragment.adapter.SearchList(searchList);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search,menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }
}