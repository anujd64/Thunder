package com.theflexproject.thunder.utils;

import com.theflexproject.thunder.model.TVShowInfo.TVShow;

import java.util.List;

public class PairTvShows implements Pair {

    private String recyclerTitle;

    private List<TVShow> tvShowList;


    public PairTvShows(String recyclerTitle , List<TVShow> tvShowList){
        this.tvShowList= tvShowList;
        this.recyclerTitle= recyclerTitle;
    }

    public String getRecyclerTitle() {
        return recyclerTitle;
    }

    public void setRecyclerTitle(String recyclerTitle) {
        this.recyclerTitle = recyclerTitle;
    }


    public List<TVShow> getTvShowList() {
        return tvShowList;
    }

    public void setTvShowList(List<TVShow> tvShowList) {
        this.tvShowList = tvShowList;
    }

}
