package com.androidcourse.neworder;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.Objects;

public class BackgroundSoundService extends Service {

    private MediaPlayer mediaPlayer_btn_click;
    private MediaPlayer mediaPlayer_game_sound;
    private MediaPlayer mediaPlayer_victory;
    private MediaPlayer mediaPlayer_move;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editUserPrefs;

    private boolean isMuted;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer_btn_click=MediaPlayer.create(this, R.raw.sherlock1);
        mediaPlayer_game_sound=MediaPlayer.create(this, R.raw.music_game);
        mediaPlayer_victory=MediaPlayer.create(this, R.raw.victory_win);
        mediaPlayer_move=MediaPlayer.create(this, R.raw.sherlock);

        mediaPlayer_game_sound.setLooping(true); // Set looping

        sharedPreferences = getSharedPreferences("userPrefs",0);
        editUserPrefs = sharedPreferences.edit();
    }



    @Override
    public int onStartCommand(@NonNull Intent intent, int flags, int startId) {
        if(Objects.equals(intent.getExtras().getString("STOP_PLAYING_SOUND"), "STOP_PLAYING_SOUND")){
            editUserPrefs.putBoolean("isMuted",true);
            editUserPrefs.commit();
            Toast.makeText(getApplicationContext(), R.string.music_stop_silent,    Toast.LENGTH_SHORT).show();
            return  startId;
        }
        if(Objects.equals(intent.getExtras().getString("START_PLAYING_SOUND"), "START_PLAYING_SOUND")){
            editUserPrefs.putBoolean("isMuted",false);
            editUserPrefs.commit();
            Toast.makeText(getApplicationContext(), R.string.music_is_playing, Toast.LENGTH_SHORT).show();
            return  startId;

        }
        if (Objects.equals(intent.getExtras().getString("PLAY_GAME_MUSIC"), "PLAY_GAME_MUSIC")) {
            isMuted = sharedPreferences.getBoolean("isMuted",false);
            if(!mediaPlayer_game_sound.isPlaying() && !isMuted) {
                mediaPlayer_game_sound.start();
            }
            return  startId;

        }
        if (Objects.equals(intent.getExtras().getString("STOP_GAME_MUSIC"), "STOP_GAME_MUSIC")) {
            isMuted = sharedPreferences.getBoolean("isMuted",false);
            if(mediaPlayer_game_sound.isPlaying() && isMuted)
                mediaPlayer_game_sound.pause();
            return  startId;

        }

        if(Objects.equals(intent.getExtras().getString("PLAY_BTN_PRESS"), "PLAY_BTN_PRESS")) {
            isMuted = sharedPreferences.getBoolean("isMuted",false);
            if(!isMuted) {
                mediaPlayer_btn_click.start();
            }
            return  startId;

        }

        if(Objects.equals(intent.getExtras().getString("VICTORY_MUSIC"), "VICTORY_MUSIC")) {
            isMuted = sharedPreferences.getBoolean("isMuted",false);
            if(!isMuted) {
                mediaPlayer_victory.start();
            }
            return  startId;
        }

        if(Objects.equals(intent.getExtras().getString("MOVE_SOUND"), "MOVE_SOUND")) {
            isMuted = sharedPreferences.getBoolean("isMuted",false);
            if(!isMuted) {
                mediaPlayer_move.start();
            }
            return  startId;
        }


        return startId;
    }

    @Override
    public void onDestroy() {
        mediaPlayer_move.stop();
        mediaPlayer_move.release();

        mediaPlayer_btn_click.stop();
        mediaPlayer_btn_click.release();

        mediaPlayer_victory.stop();
        mediaPlayer_victory.release();

        mediaPlayer_game_sound.stop();
        mediaPlayer_game_sound.release();
        stopSelf();
    }
}