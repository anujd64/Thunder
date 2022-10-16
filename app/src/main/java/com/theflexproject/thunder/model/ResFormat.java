package com.theflexproject.thunder.model;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResFormat{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public Data data;
    public String nextPageToken;
    public String curPageIndex;
    public int code;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public String getCurPageIndex() {
        return curPageIndex;
    }

    public void setCurPageIndex(String curPageIndex) {
        this.curPageIndex = curPageIndex;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResFormat{" +
                "nextPageToken='" + nextPageToken + '\'' +
                ", curPageIndex='" + curPageIndex + '\'' +
                ", data=" + data.getFiles().toString() +
                '}';
    }
}

