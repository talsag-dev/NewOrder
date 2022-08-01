package com.androidcourse.neworder;


import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MenuActivity extends Activity {

    private ImageView bSound;
    private Button bHelp, bСontinue, bExit, bGame, bLogout, bLeadBoard;
    private TextView tGame;
    private HelpDialog helpDialog;
    private SharedPreferences sharedPreferences;
    private UserInDBPracable userLoggedIn;
    private ArrayList<UserInDBPracable> allUsersInDB = new ArrayList<>();
    private final String DB_URL = "https://neworder-44a59-default-rtdb.europe-west1.firebasedatabase.app/";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance(DB_URL);

    private final String PLAY_GAME_MUSIC = "PLAY_GAME_MUSIC";
    private final String STOP_GAME_MUSIC = "STOP_GAME_MUSIC";
    private final String PLAY_BTN_PRESS = "PLAY_BTN_PRESS";
    private final String STOP_PLAYING_SOUND = "STOP_PLAYING_SOUND";
    private final String START_PLAYING_SOUND = "START_PLAYING_SOUND";


    private Intent setButtonSound, setToastMusic, setBackgroundMusic;
    private Boolean MuteSound;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSystemBars();
        setContentView(R.layout.activity_menu);
        helpDialog = new HelpDialog();
        initUsersDB();
//        getServerData();
//        mCheckInforInServer("Users");
        getCreateUserFromDB();
        currentUserlistener();
        setButtonSound = new Intent(this, BackgroundSoundService.class);
        setToastMusic = new Intent(this, BackgroundSoundService.class);
        setBackgroundMusic = new Intent(this, BackgroundSoundService.class);

        sharedPreferences = getSharedPreferences("userPrefs", 0);
        MuteSound = sharedPreferences.getBoolean("isMuted", false);
        bLeadBoard = findViewById(R.id.button_leadboard);
        bGame = findViewById(R.id.bNewGame);
        bСontinue = findViewById(R.id.bСontinue);
        bHelp = findViewById(R.id.bHelp);
        bExit = findViewById(R.id.bExit);
        bSound = findViewById(R.id.sound_off_reg);
        bLogout = findViewById(R.id.logout_main_menu);
        tGame = findViewById(R.id.tGame);
        if (!MuteSound) {
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC, PLAY_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_on);
            startService(setBackgroundMusic);
        } else {
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC, STOP_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_off);
            startService(setBackgroundMusic);
        }

        YoYo.with(Techniques.Pulse).repeat(-1).playOn(tGame);

        ;
    }
    private  void initUsersDB(){
        database.getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && allUsersInDB.size()==0){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        UserInDBPracable newUser = snapshot1.getValue(UserInDBPracable.class);
                        allUsersInDB.add(newUser);
                    }
                    Log.e("INFO","all users updated all time "+allUsersInDB.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    private void getCreateUserFromDB() {
        FirebaseUser userLoggedInAuth = FirebaseAuth.getInstance().getCurrentUser();
        Query query = database.getReference("Users").orderByChild("email").equalTo(userLoggedInAuth.getEmail());
        DatabaseReference ref = database.getReference("Users");
        query.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isComplete()) {
                    if (task.getResult().exists()) {
                        for (DataSnapshot data : task.getResult().getChildren()) {
                            userLoggedIn = data.getValue(UserInDBPracable.class);
                            Log.e("INFO", "got user from db" + userLoggedIn.toString());
                        }
                    } else {
                        userLoggedIn = new UserInDBPracable(userLoggedInAuth.getEmail(), 0, 0, "", 0, 0, 0);
                        ref.push().setValue(userLoggedIn);
                        Log.e("INFO", "added new user to db" + userLoggedIn.toString());
                    }
                }
            }
        });




    }

    private void currentUserlistener(){
        FirebaseUser userLoggedInAuth = FirebaseAuth.getInstance().getCurrentUser();
        Query query = database.getReference("Users").orderByChild("email").equalTo(userLoggedInAuth.getEmail());
        DatabaseReference ref = database.getReference("Users");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(userLoggedIn!=null){
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        userLoggedIn = snapshot1.getValue(UserInDBPracable.class);
                        Log.e("INFO","user update "+userLoggedIn.toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }





    @Override
    protected void onStart() {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bNewGame:
                        newGame();
                        break;
                    case R.id.bСontinue:
                        continueGame();
                        break;
                    case R.id.bHelp:
                        help();
                        break;
                    case R.id.bExit:
                        onDestroy();
                        break;
                    case R.id.sound_off_reg:
                        soundOffOn();
                        break;
                    case R.id.logout_main_menu:
                        logout();
                        break;
                    case R.id.button_leadboard:
                        leadBoard();
                        break;
                    default:
                        break;
                }
                setButtonSound.putExtra(PLAY_BTN_PRESS, PLAY_BTN_PRESS);
                startService(setButtonSound);
            }
        };
        bLogout.setOnClickListener(onClickListener);

        bHelp.setOnClickListener(onClickListener);
        bСontinue.setOnClickListener(onClickListener);
        bExit.setOnClickListener(onClickListener);
        bGame.setOnClickListener(onClickListener);
        bSound.setOnClickListener(onClickListener);
        bLeadBoard.setOnClickListener(onClickListener);


        super.onStart();
    }

    private void leadBoard() {
        Intent intent = new Intent(MenuActivity.this, LeadBoard.class);
        intent.putExtra("UserList", allUsersInDB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void newGame() {
        Intent intent = new Intent(MenuActivity.this, LevelActivity.class);
        intent.putExtra("UserLoggedIn", userLoggedIn);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
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

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MenuActivity.this, "Signed-out", Toast.LENGTH_LONG);
        Intent moveToLogin = new Intent(this, LoginActivity.class);
        moveToLogin.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(moveToLogin);
        finish();

    }

    private void continueGame() {
        try {
            int n = userLoggedIn.getLevelSaved();
            if (n == 0) {  //new user with no saved level
                throw new Exception("No saved game exist");
            }
            Intent intent = new Intent();
            switch (n) {
                case 3:
                    intent = new Intent(MenuActivity.this, GameActivity9.class);
                    break;
                case 4:
                    intent = new Intent(MenuActivity.this, GameActivity16.class);
                    break;
                case 5:
                    intent = new Intent(MenuActivity.this, GameActivity25.class);
                    break;
            }
            intent.putExtra("keyGame", 1);
            intent.putExtra("UserLoggedIn", userLoggedIn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(MenuActivity.this, R.string.notSave, Toast.LENGTH_SHORT).show();
        }
    }

    private void help() {
        helpDialog.showDialog(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void soundOffOn() {
        MuteSound = sharedPreferences.getBoolean("isMuted", false);
        setBackgroundMusic = new Intent(this, BackgroundSoundService.class);
        setToastMusic = new Intent(this, BackgroundSoundService.class);
        if (!MuteSound) {
            setToastMusic.putExtra(STOP_PLAYING_SOUND, STOP_PLAYING_SOUND);
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC, STOP_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_off);
        } else {
            setToastMusic.putExtra(START_PLAYING_SOUND, START_PLAYING_SOUND);
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC, PLAY_GAME_MUSIC);
            bSound.setImageResource(R.drawable.sound_on);
        }
        YoYo.with(Techniques.RubberBand).duration(700).playOn(bSound);
        startService(setToastMusic);
        startService(setBackgroundMusic);

    }

}