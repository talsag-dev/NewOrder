package com.androidcourse.neworder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button registerBtn, btnExit, btnSignIn;
    private EditText emailInput, passwordInput;
    private ImageView btnSound;
    private Intent moveToReg, setBackgroundMusic, setToastMusic, playBtnPress;
    private SharedPreferences sharedPreferences;
    private boolean isMuted;

    private final String PLAY_GAME_MUSIC = "PLAY_GAME_MUSIC";
    private final String STOP_GAME_MUSIC = "STOP_GAME_MUSIC";
    private final String PLAY_BTN_PRESS = "PLAY_BTN_PRESS";
    private final String STOP_PLAYING_SOUND = "STOP_PLAYING_SOUND";
    private final String START_PLAYING_SOUND = "START_PLAYING_SOUND";


    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemBars();
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("userPrefs", 0);
        isMuted = sharedPreferences.getBoolean("isMuted", false);


        moveToReg = new Intent(this, RegisterActivity.class);
        setBackgroundMusic = new Intent(this, BackgroundSoundService.class);
        playBtnPress = new Intent(this, BackgroundSoundService.class);

        emailInput = findViewById(R.id.editTextTextEmailAddress_login);
        passwordInput = findViewById(R.id.editTextTextPassword_log);
        btnSound = findViewById(R.id.sound_off_login);
        registerBtn = findViewById(R.id.reg_button);
        btnExit = findViewById(R.id.exit_btn_login);
        btnSignIn = findViewById(R.id.sign_in_button);
        View.OnClickListener exitClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit();
            }
        };
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == registerBtn) {
                    moveToReg.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    playBtnPress.putExtra(PLAY_BTN_PRESS, PLAY_BTN_PRESS);
                    startService(playBtnPress);
                    startActivity(moveToReg);
                    finish();
                }

            }
        };

        View.OnClickListener btnSoundListner = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == btnSound) {
                    soundOffOn();
                    playBtnPress.putExtra(PLAY_BTN_PRESS, PLAY_BTN_PRESS);
                    startService(playBtnPress);
                }

            }
        };
        btnExit.setOnClickListener(exitClickListener);
        registerBtn.setOnClickListener(clickListener);
        btnSound.setOnClickListener(btnSoundListner);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                playBtnPress.putExtra(PLAY_BTN_PRESS, PLAY_BTN_PRESS);
                startService(playBtnPress);

            }
        });

        //init game music for user
        if (!isMuted) {
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC, PLAY_GAME_MUSIC);
            btnSound.setImageResource(R.drawable.sound_on);
            startService(setBackgroundMusic);

        } else {
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC, STOP_GAME_MUSIC);
            btnSound.setImageResource(R.drawable.sound_off);
            startService(setBackgroundMusic);

        }
    }


    private void login() {
        String email, password;
        email = emailInput.getText().toString();
        password = passwordInput.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            YoYo.with(Techniques.RubberBand).duration(700).playOn(emailInput);

            return;
        }

        if (password.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            YoYo.with(Techniques.RubberBand).duration(700).playOn(passwordInput);
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                                    "Login successful!!",
                                                    Toast.LENGTH_LONG)
                                            .show();


                                    // if sign-in is successful
                                    // intent to home activity
                                    Intent intent
                                            = new Intent(LoginActivity.this,
                                            MenuActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                } else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();


                                }
                            }
                        });


    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    private void exit() {
        onDestroy();
    }

    public void soundOffOn() {
        isMuted = sharedPreferences.getBoolean("isMuted", false);
        setBackgroundMusic = new Intent(this, BackgroundSoundService.class);
        setToastMusic = new Intent(this, BackgroundSoundService.class);
        if (!isMuted) {
            setToastMusic.putExtra(STOP_PLAYING_SOUND, STOP_PLAYING_SOUND);
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC, STOP_GAME_MUSIC);
            btnSound.setImageResource(R.drawable.sound_off);
        } else {
            setToastMusic.putExtra(START_PLAYING_SOUND, START_PLAYING_SOUND);
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC, PLAY_GAME_MUSIC);
            btnSound.setImageResource(R.drawable.sound_on);
        }
        YoYo.with(Techniques.RubberBand).duration(700).playOn(btnSound);
        startService(setToastMusic);
        startService(setBackgroundMusic);

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


}