package com.androidcourse.neworder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class LevelActivity extends AppCompatActivity {

    private ImageView bSound,bHelp,bBack;
    private TextView tGame;
    private Button bLevel9,bLevel15,bLevel24;
    private HelpDialog helpDialog;
    private SharedPreferences sharedPreferences;
    private boolean isMuted;
    private Intent setButtonSound,setBackgroundMusic,setToastMusic;
    private int numbSteps;
    private UserInDBPracable userLoggedIn;

    private final String PLAY_GAME_MUSIC="PLAY_GAME_MUSIC";
    private final String STOP_GAME_MUSIC="STOP_GAME_MUSIC";
    private final String PLAY_BTN_PRESS="PLAY_BTN_PRESS";
    private final String STOP_PLAYING_SOUND="STOP_PLAYING_SOUND";
    private final String START_PLAYING_SOUND="START_PLAYING_SOUND";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemBars();
        setContentView(R.layout.activity_level);
        helpDialog = new HelpDialog();
        sharedPreferences = getSharedPreferences("userPrefs",0);
        setButtonSound = new Intent(this,BackgroundSoundService.class);
        isMuted = sharedPreferences.getBoolean("isMuted",false);
        userLoggedIn = (UserInDBPracable) getIntent().getParcelableExtra("UserLoggedIn");



        bLevel9 = findViewById(R.id.bLevel9);
        bLevel15 =findViewById(R.id.bLevel24);
        bLevel24 = findViewById(R.id.bLevel15);
        tGame = findViewById(R.id.tGame);
        bSound = findViewById(R.id.sound_off_reg);
        if (!isMuted)
            bSound.setImageResource(R.drawable.sound_on);
        else bSound.setImageResource(R.drawable.sound_off);

        bBack = findViewById(R.id.bBackMenu);
        bHelp = findViewById(R.id.btnhelp);

        YoYo.with(Techniques.Pulse).repeat(-1).playOn(tGame);
    }

    @Override
    protected void onStart() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bLevel9:
                        newGame(3);
                        break;
                    case R.id.bLevel15:
                        newGame(4);
                        break;
                    case R.id.bLevel24:
                        newGame(5);
                        break;
                    case R.id.sound_off_reg:
                        soundOffOn();
                        break;
                    case R.id.bBackMenu:
                        backMenu();
                        break;
                    case R.id.btnhelp:
                        displayHelp();
                        break;
                    default:
                        break;
                }
                setButtonSound.putExtra(PLAY_BTN_PRESS,PLAY_BTN_PRESS);
                startService(setButtonSound); }
        };

        bBack.setOnClickListener(onClickListener);
        bHelp.setOnClickListener(onClickListener);
        bSound.setOnClickListener(onClickListener);
        bLevel9.setOnClickListener(onClickListener);
        bLevel15.setOnClickListener(onClickListener);
        bLevel24.setOnClickListener(onClickListener);


        super.onStart();
    }



    private void hideSystemBars() {
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }

    private void newGame(int level) {
        Intent intent = new Intent();
        switch (level) {
            case 3:
                intent = new Intent(LevelActivity.this, GameActivity9.class);
                break;
            case 4:
                intent = new Intent(LevelActivity.this, GameActivity16.class);
                break;
            case 5:
                intent = new Intent(LevelActivity.this, GameActivity25.class);
                break;
//
        }
        intent.putExtra("UserLoggedIn",userLoggedIn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void soundOffOn() {
        isMuted = sharedPreferences.getBoolean("isMuted",false);
        setBackgroundMusic = new Intent(this,BackgroundSoundService.class);
        setToastMusic = new Intent(this,BackgroundSoundService.class);
        if (!isMuted) {
            setToastMusic.putExtra(STOP_PLAYING_SOUND,STOP_PLAYING_SOUND);
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC,STOP_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_off);
        }
        else {
            setToastMusic.putExtra(START_PLAYING_SOUND,START_PLAYING_SOUND);
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC,PLAY_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_on);
        }
        startService(setToastMusic);
        startService(setBackgroundMusic);

    }

    public void displayHelp() {
        helpDialog.showDialog(this);
    }

    public void backMenu() {
        Intent intent = new Intent(LevelActivity.this, MenuActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }
}