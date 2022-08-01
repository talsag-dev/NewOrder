package com.androidcourse.neworder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class GameActivity16 extends AppCompatActivity {
    private final int N = 4;
    Cards cards;
    private ImageView[][] buttons;
    private ImageView bNewGame,bBackMenu,bAbout,ibSound;
    TextView tSScore,tScore,tBestScore,tBestSScore;
    private Intent setButtonSound,setBackgroundMusic,setToastMusic,setMoveSound,setWinningSound;
    private boolean check;
    private boolean isMuted;
    private int numbSteps;
    private int numbBestSteps;
    private UserInDBPracable userInDB;
    private final String DB_URL = "https://neworder-44a59-default-rtdb.europe-west1.firebasedatabase.app/";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance(DB_URL);
    private ProgressDialog mProgressDialog;

    private HelpDialog helpDialog;

    private final int BUTTON_ID[][] = {{R.id.b1500, R.id.b1501, R.id.b1502,R.id.b1503},
            {R.id.b1510, R.id.b1511, R.id.b1512,R.id.b1513},
            {R.id.b1520, R.id.b1521, R.id.b1522,R.id.b1523},
            {R.id.b1530, R.id.b1531, R.id.b1532,R.id.b1533}};
    private final int CADRS_ID[] = {R.drawable.card0,R.drawable.card1,R.drawable.card2,R.drawable.card3,R.drawable.card4,R.drawable.card5,R.drawable.card6,R.drawable.card7,R.drawable.card8,R.drawable.card9,R.drawable.card10,R.drawable.card11,R.drawable.card12,R.drawable.card13,R.drawable.card14,R.drawable.card15};

    private SharedPreferences sharedPreferences;

    private final String PLAY_GAME_MUSIC="PLAY_GAME_MUSIC";
    private final String STOP_GAME_MUSIC="STOP_GAME_MUSIC";
    private final String PLAY_BTN_PRESS="PLAY_BTN_PRESS";
    private final String STOP_PLAYING_SOUND="STOP_PLAYING_SOUND";
    private final String START_PLAYING_SOUND="START_PLAYING_SOUND";
    private final String MOVE_SOUND="MOVE_SOUND";
    private final String VICTORY_MUSIC="VICTORY_MUSIC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemBars();
        userInDB=(UserInDBPracable) getIntent().getParcelableExtra("UserLoggedIn");
        setContentView(R.layout.activity_game16);
        helpDialog = new HelpDialog();
        buttons = new ImageView[N][N];


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!check)
                    for(int i = 0; i < N; i++)
                        for(int j = 0; j < N; j++)
                            if(v.getId() == BUTTON_ID[i][j])
                                buttonFunction(i, j);

                switch(v.getId()) {
                    case R.id.bNewGame:
                        newGame();
                        break;
                    case R.id.bBackMenu:
                        backMenu();
                        break;
                    case R.id.bAboutGame:
                        aboutOnClick();
                        break;
                    case R.id.sound_off_reg:
                        soundOffOn();
                        break;
                    default:
                        break;
                }
                setButtonSound.putExtra(PLAY_BTN_PRESS,PLAY_BTN_PRESS);
                startService(setButtonSound); }
        };

        bNewGame = findViewById(R.id.bNewGame);
        bBackMenu = findViewById(R.id.bBackMenu);
        bAbout = findViewById(R.id.bAboutGame);
        ibSound = findViewById(R.id.sound_off_reg);

        bAbout.setOnClickListener(onClickListener);
        ibSound.setOnClickListener(onClickListener);
        bBackMenu.setOnClickListener(onClickListener);
        bNewGame.setOnClickListener(onClickListener);

        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                buttons[i][j] = findViewById(BUTTON_ID[i][j]);
                buttons[i][j].setOnClickListener(onClickListener);
            }

        tSScore = findViewById(R.id.tSScore);
        tScore =findViewById(R.id.tScore);
        tBestSScore =findViewById(R.id.tBestSScore);
        tBestScore = findViewById(R.id.tBestScore);


        setButtonSound = new Intent(this,BackgroundSoundService.class);
        sharedPreferences = getSharedPreferences("userPrefs",0);
        isMuted = sharedPreferences.getBoolean("isMuted",false);

        cards = new Cards(N, N);
        try {
            if(getIntent().getExtras().getInt("keyGame") == 1) {
                continueGame();
                checkFinish();
            } else newGame();
        } catch (Exception e) {
            newGame();
        }
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



    public void buttonFunction(int row, int columb) {
        setMoveSound = new Intent(this,BackgroundSoundService.class);
        cards.moveCards(row, columb);
        if(cards.resultMove()) {
            setMoveSound.putExtra(MOVE_SOUND,MOVE_SOUND);
            startService(setMoveSound);
            numbSteps++;
            showGame();
            checkFinish();
        }
    }

    public void newGame() {
        cards.getNewCards();
        numbSteps = 0;
        numbBestSteps = userInDB.getLevel16BestScore();
        tBestScore.setText(Integer.toString(numbBestSteps));
        showGame();
        bouncingCards();

        check = false;
    }

    private void continueGame() {
        String text = userInDB.getSavedBoard();
        int k = 0;
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                cards.setValueBoard(i, j,  Integer.parseInt("" + text.charAt(k) + text.charAt(k + 1)));
                k += 2;
            }

        numbSteps = userInDB.getSaveNumberSteps();
        numbBestSteps = userInDB.getLevel9BestScore();
        tBestScore.setText(Integer.toString(numbBestSteps));

        showGame();
        bouncingCards();

        check = false;
    }

    public void backMenu() {
        saveValueBoard();
        saveToDB();
        Intent intent = new Intent(this, MenuActivity.class);
//        intent.putExtra("UserLoggedIn",userInDB);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void saveToDB(){
        Query query = database.getReference("Users").orderByChild("email").equalTo(userInDB.getEmail());
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Task<Void> task = data.getRef().setValue(userInDB);
                        task.addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                try{
                                    Log.e("INFO","task completed with res: "+task.getResult().toString());
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }


    private void saveValueBoard() {
        String text = "";
        for(int i = 0; i < N; i++)
            for(int j = 0; j < N; j++) {
                if(cards.getValueBoard(i, j) < 10)
                    text += "0" + cards.getValueBoard(i, j);
                else text += cards.getValueBoard(i, j);
            }

        userInDB.setSavedBoard(text);
        userInDB.setSaveNumberSteps(numbSteps);
        userInDB.setLevelSaved(N);
    }

    public void aboutOnClick() {
        helpDialog.showDialog(this);
    }

    public void showGame() {
        tScore.setText(Integer.toString(numbSteps));
        for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                buttons[i][j].setImageResource(CADRS_ID[cards.getValueBoard(i, j)]);
            }
        }
    }

    public void checkFinish(){
        if(cards.finished(N, N)){
            showGame();
            setWinningSound = new Intent(this,BackgroundSoundService.class);
            setWinningSound.putExtra(VICTORY_MUSIC,VICTORY_MUSIC);
            startService(setWinningSound);
            Toast.makeText(this, R.string.you_won, Toast.LENGTH_SHORT).show();
            if ((numbSteps < numbBestSteps) || (numbBestSteps == 0)) {
                userInDB.setLevel16BestScore(numbSteps);
                tBestScore.setText(Integer.toString(numbSteps));
            }
            check = true;
        }
    }
    public void bouncingCards() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                YoYo.with(Techniques.RubberBand).duration(700).playOn(buttons[i][j]);
            }
        }
    }

    public void soundOffOn() {
        isMuted = sharedPreferences.getBoolean("isMuted",false);
        setBackgroundMusic = new Intent(this,BackgroundSoundService.class);
        setToastMusic = new Intent(this,BackgroundSoundService.class);
        if (!isMuted) {
            setToastMusic.putExtra(STOP_PLAYING_SOUND,STOP_PLAYING_SOUND);
            setBackgroundMusic.putExtra(STOP_GAME_MUSIC,STOP_GAME_MUSIC);
            ibSound.setImageResource(R.drawable.sound_off);
        }
        else {
            setToastMusic.putExtra(START_PLAYING_SOUND,START_PLAYING_SOUND);
            setBackgroundMusic.putExtra(PLAY_GAME_MUSIC,PLAY_GAME_MUSIC);
            ibSound.setImageResource(R.drawable.sound_on);
        }
        YoYo.with(Techniques.RubberBand).duration(700).playOn(ibSound);
        startService(setToastMusic);
        startService(setBackgroundMusic);
        showGame();
    }



}