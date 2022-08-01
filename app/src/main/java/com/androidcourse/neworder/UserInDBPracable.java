package com.androidcourse.neworder;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;
import java.lang.Number;
import java.util.Map;

public class UserInDBPracable implements  Parcelable {
    private String email;
    private int levelSaved; //fileN
    private int saveNumberSteps; //fileScore
    private String savedBoard; //saveBoardPoints , the saved board
    private int level9BestScore;
    private int level16BestScore;
    private int level25BestScore;

    public UserInDBPracable(){

    }

    protected UserInDBPracable(Parcel in) {
        email = in.readString();
        levelSaved = in.readInt();
        saveNumberSteps = in.readInt();
        savedBoard = in.readString();
        level9BestScore = in.readInt();
        level16BestScore = in.readInt();
        level25BestScore = in.readInt();
    }

    public static final Creator<UserInDBPracable> CREATOR = new Creator<UserInDBPracable>() {
        @Override
        public UserInDBPracable createFromParcel(Parcel in) {
            return new UserInDBPracable(in);
        }

        @Override
        public UserInDBPracable[] newArray(int size) {
            return new UserInDBPracable[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevelSaved() {
        return levelSaved;
    }

    public void setLevelSaved(int levelSaved) {
        this.levelSaved = levelSaved;
    }

    public int getSaveNumberSteps() {
        return saveNumberSteps;
    }

    public void setSaveNumberSteps(int saveNumberSteps) {
        this.saveNumberSteps = saveNumberSteps;
    }

    public String getSavedBoard() {
        return savedBoard;
    }

    public void setSavedBoard(String savedBoard) {
        this.savedBoard = savedBoard;
    }

    public int getLevel9BestScore() {
        return level9BestScore;
    }

    public void setLevel9BestScore(int level9BestScore) {
        this.level9BestScore = level9BestScore;
    }

    public int getLevel16BestScore() {
        return level16BestScore;
    }

    public void setLevel16BestScore(int level16BestScore) {
        this.level16BestScore = level16BestScore;
    }

    public int getLevel25BestScore() {
        return level25BestScore;
    }

    public void setLevel25BestScore(int level25BestScore) {
        this.level25BestScore = level25BestScore;
    }




    public UserInDBPracable(String email, int levelSaved, int saveNumberSteps, String savedBoard, int level9BestScore, int level16BestScore, int level25BestScore) {
        this.email = email;
        this.levelSaved = levelSaved;
        this.saveNumberSteps = saveNumberSteps;
        this.savedBoard = savedBoard;
        this.level9BestScore = level9BestScore;
        this.level16BestScore = level16BestScore;
        this.level25BestScore = level25BestScore;
    }

    @Override
    public String toString() {
        return "UserInDB{" +
                "email='" + email + '\'' +
                ", levelSaved=" + levelSaved +
                ", saveNumberSteps=" + saveNumberSteps +
                ", savedBoard='" + savedBoard + '\'' +
                ", level9BestScore=" + level9BestScore +
                ", level16BestScore=" + level16BestScore +
                ", level25BestScore=" + level25BestScore +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeInt(levelSaved);
        parcel.writeInt(saveNumberSteps);
        parcel.writeString(savedBoard);
        parcel.writeInt(level9BestScore);
        parcel.writeInt(level16BestScore);
        parcel.writeInt(level25BestScore);
    }
}
