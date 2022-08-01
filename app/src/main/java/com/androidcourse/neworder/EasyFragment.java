package com.androidcourse.neworder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class EasyFragment extends Fragment {
    private ProgressDialog mProgressDialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TextView textToShow,scoreTxt;
    private UserInDBAdapterEasy userInDBAdapter;
    private ArrayList<UserInDBPracable> filterArray = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_easy, container, false);
        assert getArguments() != null;
        ArrayList<UserInDBPracable> filterArray= getArguments().getParcelableArrayList("Easy");
        recyclerView=view.findViewById(R.id.easy_rec);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        userInDBAdapter=new UserInDBAdapterEasy(filterArray);
        recyclerView.setAdapter(userInDBAdapter);


        return view;

    }





}