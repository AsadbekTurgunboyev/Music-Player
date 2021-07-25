package com.example.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.example.musicplayer.inter.ActionPlay;
import com.example.musicplayer.model.SongsModel;
import com.example.musicplayer.service.SerViceMusic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static com.example.musicplayer.AlbumShowActivity.albumArraylist;
import static com.example.musicplayer.adapter.SongsAdapter.list;
import static com.example.musicplayer.service.ApplicationClass.ACTION_NEXT;
import static com.example.musicplayer.service.ApplicationClass.ACTION_PLAY;
import static com.example.musicplayer.service.ApplicationClass.ACTION_PREVIOUS;
import static com.example.musicplayer.service.ApplicationClass.CHANNEL_2;

public class ListenActivity extends AppCompatActivity implements ActionPlay, ServiceConnection {
    public static ArrayList<SongsModel> songList;
    TextView songTitle, songArtist, nowTime, lengtTime;
    int position = -1;
    Uri uri;
    ImageView listenImage, backIV, nextIV;
    //    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    FloatingActionButton fab;
    ImageView btnBack;
    Handler handler = new Handler();
    Thread backThread, nextThread, playThread;
    Bitmap bitmap;
    SerViceMusic serViceMusic;
    MediaSessionCompat mediaSessionCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        initViews();
        initVars();
        mediaSessionCompat = new MediaSessionCompat(getBaseContext(),"Audio");
        getIntenData();


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (serViceMusic != null && fromUser) {
                    serViceMusic.seekTo(progress * 1000);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ListenActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serViceMusic != null) {
                    int mPosition = serViceMusic.getDuration() / 1000;
                    seekBar.setProgress(mPosition);
                    nowTime.setText(timeFormat(mPosition));

                }
                handler.postDelayed(this, 1000);
            }
        });

    }

    private String timeFormat(int mPosition) {
        String showTime = "";
        String seconds = String.valueOf(mPosition % 60);
        String minute = String.valueOf(mPosition / 60);
        if (seconds.length() == 1) {
            showTime = minute + ":" + "0" + seconds;
        } else {
            showTime = minute + ":" + seconds;
        }
        return showTime;
    }

    private void initVars() {
        songList = new ArrayList<>();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListenActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void getIntenData() {
        position = getIntent().getExtras().getInt("position", -1);
        String key = getIntent().getExtras().getString("key");
        if (key != null && key.equals("Kalit")) {
            songList = albumArraylist;
        } else {

            songList = list;
        }
        if (songList != null) {
            getNotification(R.drawable.ic_baseline_pause_24);

            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            uri = Uri.parse(songList.get(position).getPath());


        }
        Intent intent = new Intent(this, SerViceMusic.class);
        intent.putExtra("servicePosition", position);
        startService(intent);


    }

    private void initViews() {
        songTitle = findViewById(R.id.textView4);
        songArtist = findViewById(R.id.textView5);
        listenImage = findViewById(R.id.listenImage);
        seekBar = findViewById(R.id.seekBar);
        nowTime = findViewById(R.id.nowTime);
        lengtTime = findViewById(R.id.lengtTime);
        backIV = findViewById(R.id.backIV);
        nextIV = findViewById(R.id.nextIv);
        fab = findViewById(R.id.floatingActionButton2);
        btnBack = findViewById(R.id.backActivity);
    }

    private void getPathData(Uri mUri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(mUri.toString());
        int timeLEngth = Integer.parseInt(songList.get(position).getDuration()) / 1000;
        lengtTime.setText(timeFormat(timeLEngth));
        byte[] img = retriever.getEmbeddedPicture();
        if (img != null) {
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            AnimationImage(this, bitmap, listenImage);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@Nullable Palette palette) {
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch != null) {
                        ConstraintLayout bg = findViewById(R.id.cons);
                        Toolbar toolbar = findViewById(R.id.toolbar2);
                        bg.setBackgroundResource(R.drawable.listen_bg);

                        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, new int[]{swatch.getRgb(), 0x00000000});
                        bg.setBackground(drawable);
                        GradientDrawable drawableTool = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{swatch.getRgb(), 0x000000000});
                        toolbar.setBackground(drawableTool);
                        songTitle.setTextColor(swatch.getTitleTextColor());
                        songArtist.setTextColor(swatch.getBodyTextColor());
                    }
                }
            });

        } else {
            Glide.with(this).load(R.drawable.shape).into(listenImage);
        }
    }

    @Override
    protected void onResume() {
        Intent intent = new Intent(this, SerViceMusic.class);

        bindService(intent, this, BIND_AUTO_CREATE);
        playBtn();
        prevBtn();
        nextBtn();
        super.onResume();

    }

    private void prevBtn() {
        backThread = new Thread() {
            @Override
            public void run() {
                super.run();
                prevTreadBtn();
            }
        };
        backThread.start();
    }

    private void prevTreadBtn() {
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backClicking();

            }
        });

    }

    public void backClicking() {
        if (serViceMusic.isPlaying()) {
            serViceMusic.stop();
            serViceMusic.release();
            if (position == 0) {
                position = songList.size() - 1;
            } else {
                position = position - 1;
            }
            uri = Uri.parse(songList.get(position).getPath());
            serViceMusic.createMediaPlayer(position);

            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            getNotification(R.drawable.ic_baseline_pause_24);

            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            serViceMusic.start();

        } else {
            if (position == 0) {
                position = songList.size() - 1;
            } else {
                position = position - 1;
            }
            uri = Uri.parse(songList.get(position).getPath());
            serViceMusic.createMediaPlayer(position);

            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            getNotification(R.drawable.ic_baseline_pause_24);

            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            serViceMusic.start();
        }

    }

    private void nextBtn() {
        nextThread = new Thread() {
            @Override
            public void run() {
                super.run();
                nextTreadBtn();
            }
        };
        nextThread.start();
    }

    private void nextTreadBtn() {
        nextIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtnCLicking();


            }
        });
    }

    public void nextBtnCLicking() {
        if (serViceMusic.isPlaying()) {
            serViceMusic.stop();
            serViceMusic.release();
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            serViceMusic.createMediaPlayer(position);

            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            serViceMusic.OnCompleated();
            getNotification(R.drawable.ic_baseline_pause_24);

            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            serViceMusic.start();

        } else {
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            serViceMusic.createMediaPlayer(position);

            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            serViceMusic.OnCompleated();
            getNotification(R.drawable.ic_baseline_pause_24);

            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            serViceMusic.start();

        }
    }

    private void playBtn() {
        playThread = new Thread() {
            @Override
            public void run() {
                super.run();
                clickPlayBtn();
            }
        };
        playThread.start();
    }

    private void clickPlayBtn() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPlaying();

            }
        });
    }

    public void clickPlaying() {
        if (serViceMusic.isPlaying()) {
            getNotification(R.drawable.ic_baseline_play_arrow_24);

            fab.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            serViceMusic.pause();
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);


                    }
                    handler.postDelayed(this, 1000);
                }
            });
        } else {
            getNotification(R.drawable.ic_baseline_pause_24);
            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            serViceMusic.start();
            seekBar.setMax(serViceMusic.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serViceMusic != null) {
                        int mPosition = serViceMusic.getDuration() / 1000;
                        seekBar.setProgress(mPosition);


                    }
                    handler.postDelayed(this, 1000);
                }
            });

        }
    }

    public void AnimationImage(Context context, Bitmap bitmap, ImageView imageView) {
        Animation showOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation showIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        showOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                showIn.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(showIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(showOut);
    }


//    @Override
//    public void onCompletion(MediaPlayer mp) {
//        nextBtnCLicking();
//        if (serViceMusic != null) {
//            serViceMusic.stop();
//            serViceMusic.release();
//            serViceMusic.createMediaPlayer();
//
//            serViceMusic.start();
//            serViceMusic.OnCompleated();
//
//        }
//    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        SerViceMusic.MyBinder binder = (SerViceMusic.MyBinder) service;
        serViceMusic = binder.getService();
        serViceMusic.getActionInterface(this);
        seekBar.setMax(serViceMusic.getDuration() / 1000);
        getPathData(uri);
        songTitle.setText(songList.get(position).getTitle());
        songArtist.setText(songList.get(position).getArtist());
        serViceMusic.OnCompleated();

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public void getNotification(int PlayNextBtn) {
        Intent intent = new Intent(this, ListenActivity.class);
        PendingIntent contentInt = PendingIntent.getActivity(this, 0, intent, 0);
        Intent prevInt = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent prevPending = PendingIntent.getBroadcast(this, 0, prevInt, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent pauseInt = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent pausePending = PendingIntent.getBroadcast(this, 0, pauseInt, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent nextInt = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPending = PendingIntent.getBroadcast(this, 0, nextInt, PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] img = null;
        Bitmap thumb = null;
        img = getAlbum(songList.get(position).getPath());
        if (img != null) {
            thumb = BitmapFactory.decodeByteArray(img, 0, img.length);
        } else {
            thumb = BitmapFactory.decodeResource(getResources(), R.drawable.shape);
        }


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2)
                .setSmallIcon(PlayNextBtn)
                .setLargeIcon(thumb)
                .setContentTitle(songList.get(position).getTitle())
                .setContentText(songList.get(position).getArtist())
                .addAction(R.drawable.ic_baseline_skip_previous_24, "previous", prevPending)
                .addAction(PlayNextBtn, "pause", pausePending)
                .addAction(R.drawable.ic_baseline_skip_next_24, "next", nextPending)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mediaSessionCompat.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOnlyAlertOnce(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0,notification);

    }

    private byte[] getAlbum(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] rasm = retriever.getEmbeddedPicture();
        retriever.release();
        return rasm;
    }
}