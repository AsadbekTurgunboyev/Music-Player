package com.example.musicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.example.musicplayer.service.SerViceMusic;

import static com.example.musicplayer.service.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.service.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.service.ApplicationClass.ACTION_PREVIOUS;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getAction();
        Intent sendIntent = new Intent(context, SerViceMusic.class);
        if(name != null){
            switch (name){
                case ACTION_PLAY:
                    sendIntent.putExtra("action","actionPlay");
                    context.startService(sendIntent);
                    break;
                case ACTION_NEXT:
                    sendIntent.putExtra("action","actionNext");
                    context.startService(sendIntent);
                    break;
                case ACTION_PREVIOUS:
                    sendIntent.putExtra("action","actionPrevious");
                    context.startService(sendIntent);
                    break;


            }
        }

    }
}
