package com.theflexproject.thunder.model.TVShowInfo;

public class LastEpisodeToAir {
    public String air_date;
    public int episode_number;
    public int id;
    public String name;
    public String overview;
    public String production_code;
    public int runtime;
    public int season_number;
    public int show_id;
    public String still_path;
    public double vote_average;
    public int vote_count;

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getEpisode_number() {
        return episode_number;
    }

    public void setEpisode_number(int episode_number) {
        this.episode_number = episode_number;
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

    public String getProduction_code() {
        return production_code;
    }

    public void setProduction_code(String production_code) {
        this.production_code = production_code;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public int getSeason_number() {
        return season_number;
    }

    public void setSeason_number(int season_number) {
        this.season_number = season_number;
    }

    public int getShow_id() {
        return show_id;
    }

    public void setShow_id(int show_id) {
        this.show_id = show_id;
    }

    public String getStill_path() {
        return still_path;
    }

    public void setStill_path(String still_path) {
        this.still_path = still_path;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    @Override
    public String toString() {
        return "LastEpisodeToAir{" +
                "air_date='" + air_date + '\'' +
                ", episode_number=" + episode_number +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", production_code='" + production_code + '\'' +
                ", runtime=" + runtime +
                ", season_number=" + season_number +
                ", show_id=" + show_id +
                ", still_path='" + still_path + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                '}';
    }
}
