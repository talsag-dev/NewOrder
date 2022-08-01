package com.androidcourse.neworder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeadBoard extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Intent setButtonSound;
    private ArrayList<UserInDBPracable> userInDBList;
    private ArrayList<UserInDBPracable> usersFilterdEasy = new ArrayList<>();
    private ArrayList<UserInDBPracable> usersFilterdMed = new ArrayList<>();
    private ArrayList<UserInDBPracable> usersFilterdHard = new ArrayList<>();
    private final String PLAY_BTN_PRESS = "PLAY_BTN_PRESS";



    private Button bBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_board);
        userInDBList = getIntent().getParcelableArrayListExtra("UserList");

        setButtonSound = new Intent(this, BackgroundSoundService.class);

        tabLayout = findViewById(R.id.tabs_layout);
        viewPager = findViewById(R.id.view_pager);
        bBack = findViewById(R.id.back_from_lead);
        tabLayout.setupWithViewPager(viewPager);

        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LeadBoard.this,MenuActivity.class);
                YoYo.with(Techniques.Pulse).duration(700).playOn(bBack);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                setButtonSound.putExtra(PLAY_BTN_PRESS, PLAY_BTN_PRESS);
                startService(setButtonSound);
                startActivity(intent);
                finish();
            }
        });
        setBundelsandAdapter();




    }
    private void setBundelsandAdapter(){
        Bundle bundleEasy = new Bundle();
        Bundle bundleMed = new Bundle();
        Bundle bundleHard = new Bundle();

        filterResultEasy();
        filterResultMedium();
        filterResultHard();


        bundleEasy.putParcelableArrayList("Easy", (ArrayList<? extends Parcelable>) usersFilterdEasy);
        bundleMed.putParcelableArrayList("Medium", (ArrayList<? extends Parcelable>) usersFilterdMed);
        bundleHard.putParcelableArrayList("Hard", (ArrayList<? extends Parcelable>) usersFilterdHard);



        EasyFragment easyFragment = new EasyFragment();
        MediumFragment MedFrag = new MediumFragment();
        HardFragment HardFrag = new HardFragment();

        easyFragment.setArguments(bundleEasy);
        MedFrag.setArguments(bundleMed);
        HardFrag.setArguments(bundleHard);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(easyFragment,getResources().getString(R.string.easy));
        vpAdapter.addFragment(MedFrag,getResources().getString(R.string.medium));
        vpAdapter.addFragment(HardFrag,getResources().getString(R.string.hard));

        viewPager.setAdapter(vpAdapter);
    }

    private void filterResultEasy(){
        if(userInDBList.size()==0){
            return;
        }
        if(usersFilterdEasy.size()>0){
            return;
        }
        for(UserInDBPracable user:userInDBList){
            if(user.getLevel9BestScore()>0){
                usersFilterdEasy.add(user);
            }
        }
        Collections.sort(usersFilterdEasy, Comparator.comparing(UserInDBPracable::getLevel9BestScore));

    }
    private void filterResultMedium(){
        if(userInDBList.size()==0){
            return;
        }
        if(usersFilterdMed.size()>0){
            return;
        }
        for(UserInDBPracable user:userInDBList){
            if(user.getLevel16BestScore()>0){
                usersFilterdMed.add(user);
            }
        }
        Collections.sort(usersFilterdMed, Comparator.comparing(UserInDBPracable::getLevel16BestScore));

    }
    private void filterResultHard(){
        if(userInDBList.size()==0){
            return;
        }
        if(usersFilterdHard.size()>0){
            return;
        }
        for(UserInDBPracable user:userInDBList){
            if(user.getLevel25BestScore()>0){
                usersFilterdHard.add(user);
            }
        }
        Collections.sort(usersFilterdHard, Comparator.comparing(UserInDBPracable::getLevel25BestScore));

    }

//    private void mCheckInforInServer(String child) {
//        new DataBase().mReadAll(child, new OnGetDataListener() {
//            @Override
//            public void onStart() {
//                //DO SOME THING WHEN START GET DATA HERE
//                if (mProgressDialog == null) {
//                    mProgressDialog = new ProgressDialog(LeadBoard.this);
//                    mProgressDialog.setMessage("Loading, Please Wait..");
//                    mProgressDialog.setCancelable(false);
//                    mProgressDialog.setCanceledOnTouchOutside(false);
//                }
//
//                mProgressDialog.show();
//            }
//
//            @Override
//            public void onSuccess(DataSnapshot data) {
//                //DO SOME THING WHEN GET DATA SUCCESS HERE
//                DatabaseReference ref = database.getReference().child("Users");
//
//                if(data.exists()){
//                    for(DataSnapshot dataSnapshot:data.getChildren()){
//                        userInDBList.add(dataSnapshot.getValue(UserInDBPracable.class));
//                    }
//                }
//                filterResultEasy();
//                filterResultMedium();
//                filterResultHard();
//                setBundelsandAdapter();
//                if (mProgressDialog != null && mProgressDialog.isShowing()) {
//                    mProgressDialog.dismiss();
//                }
//            }
//
//            @Override
//            public void onFailed(DatabaseError databaseError) {
//
//            }
//        });

//    }



}