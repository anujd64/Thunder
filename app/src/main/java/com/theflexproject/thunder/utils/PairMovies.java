package com.theflexproject.thunder.utils;

import com.theflexproject.thunder.model.Movie;

import java.util.List;

public class PairMovies implements Pair {

        private String recyclerTitle;

        private List<Movie> mediaList;


        public PairMovies(String recyclerTitle , List<Movie> mediaList){
                this.mediaList= mediaList;
                this.recyclerTitle= recyclerTitle;

        }

        public String getRecyclerTitle() {
                return recyclerTitle;
        }

        public void setRecyclerTitle(String recyclerTitle) {
                this.recyclerTitle = recyclerTitle;
        }

        public List<Movie> getmediaList() {
                return mediaList;
        }

        public void setmediaList(List<Movie> mediaList) {
                this.mediaList = mediaList;
        }


}
