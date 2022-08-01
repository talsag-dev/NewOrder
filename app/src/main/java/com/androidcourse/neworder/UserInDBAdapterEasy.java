package com.androidcourse.neworder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserInDBAdapterEasy extends RecyclerView.Adapter<UserInDBAdapterEasy.ViewHolder> {

    private ArrayList<UserInDBPracable> userList;

    public UserInDBAdapterEasy(ArrayList<UserInDBPracable> userList){
        this.userList = userList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public UserInDBAdapterEasy.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserInDBAdapterEasy.ViewHolder holder, int position) {
        UserInDBPracable userInDB=userList.get(position);
        holder.textView.setText(userInDB.getEmail());
        holder.score_to_be_up3.setVisibility(View.VISIBLE);
        holder.score_to_be_up3.setText(Integer.toString(userInDB.getLevel9BestScore()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView,score_to_be_up3,score_to_be_up4,score_to_be_up5;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.text_view);
            score_to_be_up3 = itemView.findViewById(R.id.score_to_be_up3);



        }
    }
}
