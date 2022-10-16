package com.theflexproject.thunder.model.TVShowInfo;

public class Season {
    public String air_date;
    public int episode_count;
    public int id;
    public String name;
    public String overview;
    public String poster_path;
    public int season_number;

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_count() {
        return episode_count;
    }

    public void setEpisode_count(int episode_count) {
        this.episode_count = episode_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    @Override
    public String toString() {
//        return "{" + "\"Season\"" + ':' +
          return "{" +
                "\"air_date\"" + ':' + '\"' + air_date + '\"'  +
                ", \"episode_count\"" + ':'  + '\"' + episode_count + '\"' +
                ", \"id\"" + ':' +  '\"' +id +'\"'  +
                ", \"name\"" + ':'  +'\"' + name + '\"' +
                ", \"overview\""  + ':' +  '\"' +overview +'\"' +
                ", \"poster_path\""  + ':' + '\"' + poster_path +'\"' +
                ", \"season_number\""  + ':' + '\"' +season_number +'\"' +
                '}' ;
//          +"}" ;
    }
}
