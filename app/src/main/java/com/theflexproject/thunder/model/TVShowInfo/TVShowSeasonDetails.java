
package com.theflexproject.thunder.model.TVShowInfo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.theflexproject.thunder.model.MyMedia;

import java.util.ArrayList;

@Entity
public class TVShowSeasonDetails implements MyMedia {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int idfordb;
    public String _id;
    public String air_date;
    public ArrayList<Episode> episodes;
    public String name;
    public String overview;
    public int id;
    public String poster_path;
    public int season_number;

    public int show_id;

    @NonNull
    public String get_id() {
        return _id;
    }

    public void set_id(@NonNull String _id) {
        this._id = _id;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public ArrayList<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(ArrayList<Episode> episodes) {
        this.episodes = episodes;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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



    @NonNull
    public int getIdfordb() {
        return idfordb;
    }

    public void setIdfordb(int idfordb) {
        this.idfordb = idfordb;
    }

    @Override
    public String toString() {
        return "TVShowSeasonDetails{" +
                "idfordb='" + idfordb + '\'' +
                ", _id='" + _id + '\'' +
                ", air_date='" + air_date + '\'' +
                ", episodes=" + episodes +
                ", name='" + name + '\'' +
                ", overview='" + overview + '\'' +
                ", id=" + id +
                ", show_id=" + show_id +
                ", poster_path='" + poster_path + '\'' +
                ", season_number=" + season_number +
                '}';
    }

    public int getShow_id() {
        return show_id;
    }

    public void setShow_id(int show_id) {
        this.show_id = show_id;
    }
}




