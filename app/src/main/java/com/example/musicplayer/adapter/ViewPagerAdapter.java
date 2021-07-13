package com.example.musicplayer.adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayer.AlbumFragment;
import com.example.musicplayer.ArtistsFragment;
import com.example.musicplayer.PlaylistFragment;
import com.example.musicplayer.SongsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    int numberTab;
    Context context;

    public ViewPagerAdapter(FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);
        this.numberTab = behavior;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new SongsFragment();
            case 1:
                return new ArtistsFragment();
            case 2:
                return new AlbumFragment();
            case 3:
                return new PlaylistFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {    //bu yerda fragment sonini sanaydi
        return numberTab;
    }

    @Override
    public CharSequence getPageTitle(int position) {       //tabLayoutni viewpagerga bog'laganda yuzaga keladigan noqulayliklar TabItem title yoqolib qolishini tuzatish uchun ishlatiladi
        switch (position) {
            case 0:
                return "Songs";
            case 1:
                return "Artist";
            case 2:
                return "Album";
            case 3:
                return "Playlist";
            default:
                return null;
        }
    }

}
