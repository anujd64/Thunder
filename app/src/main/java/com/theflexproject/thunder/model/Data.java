package com.theflexproject.thunder.model;

import java.util.ArrayList;

public class Data {
    public ArrayList<File> files;

    @Override
    public String toString() {
        return "Data{" +
                "files=" + files +
                '}';
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }
}
