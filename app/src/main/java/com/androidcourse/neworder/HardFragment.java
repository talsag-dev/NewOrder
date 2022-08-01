package com.androidcourse.neworder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HardFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private RecyclerView recyclerView;
    private ArrayList<UserInDBPracable> userInDBList = new ArrayList<>();
    private ArrayList<UserInDBPracable> usersFilterd = new ArrayList<>();
    private UserInDBAdapterHard userInDBAdapter;
    private final String DB_URL="https://neworder-44a59-default-rtdb.europe-west1.firebasedatabase.app/";
    private final FirebaseDatabase database = FirebaseDatabase.getInstance(DB_URL);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_hard, container, false);
        ArrayList<UserInDBPracable> filterArray= getArguments().getParcelableArrayList("Hard");
        recyclerView=view.findViewById(R.id.hard_rec);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        userInDBAdapter=new UserInDBAdapterHard(filterArray);
        recyclerView.setAdapter(userInDBAdapter);


        return view;

    }


    //    private void getEasyUserRank(){
//        DatabaseReference ref = database.getReference().child("Users");
//
//        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isComplete()) {
//                    if (task.getResult().exists()) {
//                        for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
//                            userInDBList.add(dataSnapshot.getValue(UserInDB.class));
//                            Log.e("INFO", userInDBList.toString());
//                        }
//                } else {
//                        textToShow.setVisibility(View.VISIBLE);
//                }
//                }
//            }
//        });
//    }


}