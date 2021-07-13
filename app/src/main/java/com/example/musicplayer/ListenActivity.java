package com.example.musicplayer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.palette.graphics.Palette;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.musicplayer.model.SongsModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


import static com.example.musicplayer.AlbumShowActivity.albumArraylist;
import static com.example.musicplayer.MainActivity.arrayList;
import static com.example.musicplayer.adapter.SongsAdapter.list;

public class ListenActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView songTitle, songArtist, nowTime, lengtTime;
    int position = -1;
    ArrayList<SongsModel> songList;
    Uri uri;
    ImageView listenImage, backIV, nextIV;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    FloatingActionButton fab;
    ImageView btnBack;
    Handler handler = new Handler();
    Thread backThread, nextThread, playThread;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listen);
        initViews();
        initVars();
        getIntenData();
        mediaPlayer.setOnCompletionListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer.seekTo(progress * 1000);
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
                if (mediaPlayer != null) {
                    int mPosition = mediaPlayer.getCurrentPosition() / 1000;
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
            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            uri = Uri.parse(songList.get(position).getPath());
            getPathData(uri);

        }
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                Log.e("musiqa", "bolyapti");
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

            } else {
                Log.e("musiqa", "ikki");
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();

            }

        } else {

            Log.e("musiqa", "uch");
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration() / 1000);
        songTitle.setText(songList.get(position).getTitle());
        songArtist.setText(songList.get(position).getArtist());

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
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (position == 0) {
                        position = songList.size() - 1;
                    } else {
                        position = position - 1;
                    }
                    uri = Uri.parse(songList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    getPathData(uri);
                    songTitle.setText(songList.get(position).getTitle());
                    songArtist.setText(songList.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mPosition);

                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    fab.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();

                } else {
                    if (position == 0) {
                        position = songList.size() - 1;
                    } else {
                        position = position - 1;
                    }
                    uri = Uri.parse(songList.get(position).getPath());
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    getPathData(uri);
                    songTitle.setText(songList.get(position).getTitle());
                    songArtist.setText(songList.get(position).getArtist());
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mPosition);

                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                    fab.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                }
            }
        });

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

    private void nextBtnCLicking() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();

        } else {
            position = (position + 1) % songList.size();
            uri = Uri.parse(songList.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            getPathData(uri);
            songTitle.setText(songList.get(position).getTitle());
            songArtist.setText(songList.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration() / 1000);
            ListenActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null) {
                        int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(mPosition);

                    }
                    handler.postDelayed(this, 1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            fab.setImageResource(R.drawable.ic_baseline_pause_24);
            mediaPlayer.start();

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
                if (mediaPlayer.isPlaying()) {
                    fab.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    mediaPlayer.pause();
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mPosition);


                            }
                            handler.postDelayed(this, 1000);
                        }
                    });
                } else {
                    fab.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();
                    seekBar.setMax(mediaPlayer.getDuration() / 1000);
                    ListenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mediaPlayer != null) {
                                int mPosition = mediaPlayer.getCurrentPosition() / 1000;
                                seekBar.setProgress(mPosition);


                            }
                            handler.postDelayed(this, 1000);
                        }
                    });

                }
            }
        });
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


    @Override
    public void onCompletion(MediaPlayer mp) {
        nextBtnCLicking();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }


}