package com.game.myAoiMusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.game.myAoiMusic.MainActivity.music;

public class PlayerActivity extends AppCompatActivity {

    // VARIÁVEIS VIEW
     private TextView textSong;
     private Button btnNext,btnPrev;
     private ImageButton btnMenu;
     private SeekBar progressSong;
     private TextView texTime,texTimer2;
     private ConstraintLayout constraintLayout;
     private LottieAnimationView lottieAnimationView;
     private LottieAnimationView lottieAnimationView2;
    // VARIÁVEIS VIEW

    // VARIÁVEIS PLAYER
    private String yName;
    private int position = 0;
    private int currentPosition = 0;
    private String whatSong;
    ArrayList<File> mySongs;
    Thread updateSeekBar;
    Runnable myRunnable;
    private int totalDuration = 0;
    private int color = 0;
    // VARIÁVEIS PLAYER

    // SAVE
    private SharedPreferences savePlayer;
    private SharedPreferences.Editor savePlayerEditor;
    // SAVE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initializeViews();
        thread();
        colorRandom();
        getIntentMain();
        eventClick();
        save();

    }

    @Override
    protected void onStart() {
        super.onStart();
        hideSystemUI();
        save();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        save();
    }

    // FULLSCREEN
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    // FULLSCREEN


    // CONNECT COM O LIST VIEW E SETAR A MUSICA
    public void initializeViews(){
        textSong = findViewById(R.id.txtRecebeSong);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrevius);
        btnMenu = findViewById(R.id.btnChamaMenu);
        progressSong = findViewById(R.id.progressSong);
        constraintLayout = findViewById(R.id.layoutPlayer);
        texTime = findViewById(R.id.textTime);
        texTimer2 = findViewById(R.id.textTimer2);
        lottieAnimationView = findViewById(R.id.lav_windmill);
        lottieAnimationView2 = findViewById(R.id.lav_windmill2);

    }
    private void save(){
        savePlayer = getSharedPreferences("save2", MODE_PRIVATE);
        whatSong = savePlayer.getString("save", "");
        whatSong = mySongs.get(position).getName().toString();
        savePlayerEditor = savePlayer.edit();
        savePlayerEditor.putString("save", whatSong);
        savePlayerEditor.apply();
    }
    public void thread(){
        myRunnable = new Runnable() {
            @Override
            public void run() {
                totalDuration = music.getDuration();
                currentPosition = 0;
                while (currentPosition < totalDuration){
                    try {
                        Thread.sleep(1000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    progressSong.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            currentPosition = music.getCurrentPosition();
                            progressSong.setProgress(currentPosition);
                            // Displaying Total Duration time
                            int duration = music.getCurrentPosition();
                            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                                    TimeUnit.MILLISECONDS.toMinutes(duration),
                                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                            );
                            texTime.setText(time);

                            if (currentPosition == totalDuration){
                                music.stop();
                                music.release();
                                music = null;
                                position = ((position + 1) % mySongs.size());
                                Uri u = Uri.parse(mySongs.get(position).toString());
                                music = MediaPlayer.create(getApplicationContext(), u);
                                totalDuration = music.getDuration();
                                currentPosition = 0;
                                progressSong.setMax(music.getDuration());
                                yName = mySongs.get(position).getName();
                                whatSong = yName;
                                textSong.setText(yName);
                                music.start();
                                int duration2 = music.getDuration();
                                @SuppressLint("DefaultLocale") String time2 = String.format("%02d:%02d",
                                        TimeUnit.MILLISECONDS.toMinutes(duration2),
                                        TimeUnit.MILLISECONDS.toSeconds(duration2) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration2))
                                );
                                texTimer2.setText(time2);
                            }

                        }
                    });
                }

            }
        };
        if (music!=null){
            music.stop();
            music.release();
            music = null;
        }

    }
    @SuppressLint("SetTextI18n")
    public void getIntentMain(){

        try {
            Intent i = getIntent();
            Bundle bundle = i.getExtras();
            assert bundle != null;
            mySongs= (ArrayList) bundle.getParcelableArrayList("songs");
            assert mySongs != null;
            yName = mySongs.get(position).getName();
            String songName = i.getStringExtra("songNameMusic");
            textSong.setText(songName);
            textSong.setSelected(true);

            position = bundle.getInt("position",0);
            Uri u = Uri.parse(mySongs.get(position).toString());
            music = MediaPlayer.create(getApplicationContext(),u);
            music.start();

            int duration = music.getDuration();
            @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(duration),
                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
            );
            texTimer2.setText(time);
            updateSeekBar = new Thread(myRunnable);
            updateSeekBar.start();
            progressSong.setMax(music.getDuration());


            if (music.isPlaying()){
                lottieAnimationView.playAnimation();
                lottieAnimationView2.setMinAndMaxFrame(61,119);
                lottieAnimationView2.playAnimation();
            }

            progressSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    music.seekTo(seekBar.getProgress());

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    // CONECTAR COM O LIST VIEW E SETAR A MUSICA

    // EVENTOS DE CLICK
    public void backMain(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void eventClick(){

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.stop();
                music.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                music = MediaPlayer.create(getApplicationContext(),u);
                yName = mySongs.get(position).getName();
                textSong.setText(yName);

                music.start();
                totalDuration = music.getDuration();
                currentPosition = 0;
                progressSong.setMax(music.getDuration());
                int duration2 = music.getDuration();
                @SuppressLint("DefaultLocale") String time2 = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration2),
                        TimeUnit.MILLISECONDS.toSeconds(duration2) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration2))
                );
                texTimer2.setText(time2);
                if (music.isPlaying()){
                    lottieAnimationView.playAnimation();
                    lottieAnimationView2.setMinAndMaxFrame(61,119);
                    lottieAnimationView2.playAnimation();
                }
                colorRandom();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music.stop();
                music.release();
                position = ((position-1)<0) ? (mySongs.size()-1):(position-1);

                Uri u = Uri.parse(mySongs.get(position).toString());
                music = MediaPlayer.create(getApplicationContext(),u);

                yName = mySongs.get(position).getName();
                textSong.setText(yName);

                music.start();
                totalDuration = music.getDuration();
                currentPosition = 0;
                progressSong.setMax(music.getDuration());
                int duration2 = music.getDuration();
                @SuppressLint("DefaultLocale") String time2 = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration2),
                        TimeUnit.MILLISECONDS.toSeconds(duration2) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration2))
                );
                texTimer2.setText(time2);
                if (music.isPlaying()){
                    lottieAnimationView.playAnimation();
                    lottieAnimationView2.setMinAndMaxFrame(61,119);
                    lottieAnimationView2.playAnimation();
                }
                colorRandom();
            }
        });

        lottieAnimationView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressSong.setMax(music.getDuration());

                if (music.isPlaying()){
                    music.pause();
                    lottieAnimationView2.setMinAndMaxFrame(0,60);
                    lottieAnimationView2.playAnimation();
                    lottieAnimationView.pauseAnimation();
                }else{
                    music.start();
                    lottieAnimationView2.setMinAndMaxFrame(61,119);
                    lottieAnimationView2.playAnimation();
                    lottieAnimationView.playAnimation();
                }
            }
        });



    }
    // EVENTOS DE CLICK

    // MENU
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(PlayerActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.bottom_navigation, popup.getMenu());
                popup.show();


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navAnim1:
                                lottieAnimationView.setAnimation(R.raw.music);
                                //progressSong.setBackgroundColor(Color.BLACK);
                                //int color = 0xFF00FF00;
                                //progressSong.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
                                if (music.isPlaying()){
                                    lottieAnimationView.playAnimation();
                                }
                                Toast.makeText(PlayerActivity.this,"Music Animation is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.navAnim2:
                                lottieAnimationView.setAnimation(R.raw.japanscene);
                                if (music.isPlaying()){
                                    lottieAnimationView.playAnimation();
                                }
                                Toast.makeText(PlayerActivity.this,"Japan Animation is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.navAnim3:
                                lottieAnimationView.setAnimation(R.raw.skitouringbackcountryskiing);
                                if (music.isPlaying()){
                                    lottieAnimationView.playAnimation();
                                }
                                Toast.makeText(PlayerActivity.this,"SkiTour Animation is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.navAnim4:
                                lottieAnimationView.setAnimation(R.raw.tetonselk);
                                if (music.isPlaying()){
                                    lottieAnimationView.playAnimation();
                                }
                                Toast.makeText(PlayerActivity.this,"Forest Animation is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color0:
                                colorRandom();
                                Toast.makeText(PlayerActivity.this,"Random is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color1:
                                color = Color.rgb(255,215,0);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Gold is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color2:
                                color = Color.rgb(123,104,238);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Medium SlateBlue is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color3:
                                color = Color.rgb(50,205,50);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Lime Green is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color4:
                                color = Color.rgb(244,164,96);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Sandy Brown is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color5:
                                color = Color.rgb(178,34,34);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Fire Brick is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.color6:
                                color = Color.rgb(153,50,204);
                                constraintLayout.setBackgroundColor(color);
                                Toast.makeText(PlayerActivity.this,"Dark Orchid is Selected",Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return false;
                        }

                    }
                });

            }
        });
        return true;
    }
    // MENU

    // COLOR RANDOM
    private void colorRandom(){
            Random random = new Random();
            int rgb = random.nextInt(256);
            if (rgb < 50) {
                color = Color.rgb(255, 215, 0);
            } else if (rgb > 50 && rgb <= 120) {
                color = Color.rgb(123, 104, 238);
            } else if (rgb > 120 && rgb <= 180) {
                color = Color.rgb(47, 79, 79);

            } else {
                color = Color.rgb(135, 206, 250);
            }
            constraintLayout.setBackgroundColor(color);
    }
    // COLOR RANDOM

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        hideSystemUI();
        save();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.stop();
        finishAffinity();
    }
}