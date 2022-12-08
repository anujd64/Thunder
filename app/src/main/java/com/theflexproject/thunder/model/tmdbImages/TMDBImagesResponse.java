package com.theflexproject.thunder.model.tmdbImages;


import java.util.ArrayList;

public class TMDBImagesResponse{
    public ArrayList<Backdrop> backdrops;
    public int id;
    public ArrayList<Logo> logos;
    public ArrayList<Poster> posters;

    public ArrayList<Backdrop> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Backdrop> backdrops) {
        this.backdrops = backdrops;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Logo> getLogos() {
        return logos;
    }

    public void setLogos(ArrayList<Logo> logos) {
        this.logos = logos;
    }

    public ArrayList<Poster> getPosters() {
        return posters;
    }

    public void setPosters(ArrayList<Poster> posters) {
        this.posters = posters;
    }
}