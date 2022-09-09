package com.theflexproject.thunder.model;

import java.util.ArrayList;
public class TMDBResponse{
    public int page;
    public ArrayList<File> results;
    public int total_pages;

    @Override
    public String toString() {
        return "TMDBResponse{" +
                "page=" + page +
                ", results=" + results +
                ", total_pages=" + total_pages +
                ", total_results=" + total_results +
                '}';
    }

    public int total_results;
}
