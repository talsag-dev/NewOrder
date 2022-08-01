package com.androidcourse.neworder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private ImageView backBtn,soundBtn;
    private Intent playBtnPress,setBackgroundMusic,setToastMusic;
    private EditText emailTextBox,passwordTextbox,confirmPasswordTextBox;
    private Button regButton;
    private final String PLAY_GAME_MUSIC="PLAY_GAME_MUSIC";
    private final String STOP_GAME_MUSIC="STOP_GAME_MUSIC";
    private final String PLAY_BTN_PRESS="PLAY_BTN_PRESS";
    private final String STOP_PLAYING_SOUND="STOP_PLAYING_SOUND";
    private final String START_PLAYING_SOUND="START_PLAYING_SOUND";
    private final String emailPattern = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private SharedPreferences sharedPreferences;
    private boolean isMuted;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser mUser;
    private String email,password,confirmPassword;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemBars();
        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("userPrefs",0);
        isMuted = sharedPreferences.getBoolean("isMuted",false);


        Intent moveBacktoLogin = new Intent(this,LoginActivity.class);
        playBtnPress = new Intent(this,BackgroundSoundService.class);
        setToastMusic= new Intent(this,BackgroundSoundService.class);
        setBackgroundMusic = new Intent(this,BackgroundSoundService.class);


        emailTextBox = findViewById(R.id.editTextTextEmailAddress_reg);
        passwordTextbox = findViewById(R.id.editTextTextPassword);
        confirmPasswordTextBox = findViewById(R.id.confirm_password);
        regButton = findViewById(R.id.reg_button);

        soundBtn = findViewById(R.id.sound_off_reg);
        backBtn = findViewById(R.id.backBtnReg);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(backBtn==view){

                    moveBacktoLogin.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(moveBacktoLogin);
                    finish();
                }
            }
        };

        View.OnClickListener onClickSoundBtn = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view == soundBtn){
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(soundBtn);
                    soundOffOn();
                    playBtnPress.putExtra(PLAY_BTN_PRESS,PLAY_BTN_PRESS);
                    startService(playBtnPress);
                }
            }
        };


        if (!isMuted) {
            soundBtn.setImageResource(R.drawable.sound_on);
        }
        else {
            soundBtn.setImageResource(R.drawable.sound_off);
        }


        soundBtn.setOnClickListener(onClickSoundBtn);
        backBtn.setOnClickListener(onClickListener);
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth();
                playBtnPress.putExtra(PLAY_BTN_PRESS,PLAY_BTN_PRESS);
                startService(playBtnPress);
            }
        });

    }

    private void Auth(){
        email = emailTextBox.getText().toString();
        password = passwordTextbox.getText().toString();
        confirmPassword = confirmPasswordTextBox.getText().toString();


        if(!email.matches(emailPattern)){
            Toast.makeText(getApplicationContext(),
                            "Enter Correct Email pattern!!",
                            Toast.LENGTH_LONG)
                    .show();
            YoYo.with(Techniques.RubberBand).duration(700).playOn(emailTextBox);

        }else{
            if(password.isEmpty() || password.length() < 6){
                Toast.makeText(getApplicationContext(),
                                "Enter more than 6 char length password",
                                Toast.LENGTH_LONG)
                        .show();
                YoYo.with(Techniques.RubberBand).duration(700).playOn(passwordTextbox);


            }else{
                if(!password.equals(confirmPassword)){
                    Toast.makeText(getApplicationContext(),
                                    "Passwords don't match",
                                    Toast.LENGTH_LONG)
                            .show();
                    YoYo.with(Techniques.RubberBand).duration(700).playOn(confirmPasswordTextBox);

                }else{
                    //all ok
                    regUSer();
                }
            }
        }



    }

    private void regUSer(){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("INFO", "createUserWithEmail:success");
                            Toast.makeText(RegisterActivity.this, "Logged in", Toast.LENGTH_LONG).show();
                            sendUSertoNextActivity();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("INFO", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "The email address is already in use by another account.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }



    private void sendUSertoNextActivity(){
        Intent moveToMenu = new Intent(this, MenuActivity.class);
        moveToMenu.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(moveToMenu);
        finish();
    }

    private void soundOffOn() {
        isMuted = sharedPreferences.getBoolean("isMuted",false);
        setBackgroundMusic = new Intent(this,BackgroundSoundService.class);
        setToastMusic = new Intent(this,BackgroundSoundService.class);
        if (!isMuted) {
            setToastMusic.putExtra(STOP_PLAYING_SOUND,STOP_PLAYING_SOUND);
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC,STOP_GAME_MUSIC);
            soundBtn.setImageResource(R.drawable.sound_off);
        }
        else {
            setToastMusic.putExtra(START_PLAYING_SOUND,START_PLAYING_SOUND);
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC,PLAY_GAME_MUSIC);
            soundBtn.setImageResource(R.drawable.sound_on);
        }
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