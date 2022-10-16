package com.theflexproject.thunder.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class IndexLink{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    public String link;
    public String username;
    public String password;
    public String indexType;
    public String folderType;

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}