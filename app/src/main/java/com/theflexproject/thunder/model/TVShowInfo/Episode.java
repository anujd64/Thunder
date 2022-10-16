package com.theflexproject.thunder.model.TVShowInfo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.theflexproject.thunder.model.MyMedia;

import java.util.Date;

@Entity
public class Episode implements MyMedia {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int idForDB;
    public String fileName;
    public String mimeType;
    public Date modifiedTime;
    public String size;
    public String urlString;

    public int Played;

    public int season_id;

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
//    public ArrayList<Crew> crew;
//    public ArrayList<GuestStar> guest_stars;

    @Override
    public String toString() {
        return
//                "{" + "\"Episode\"" + ':' +
                "{" +
                "\"idForDB\"" + ':' + '\"' + idForDB + '\"'  +
                ", \"fileName\"" + ':' + '\"' + fileName + '\"'  +
                ", \"mimeType\"" + ':' + '\"' + mimeType + '\"'  +
                ", \"modifiedTime\"" + ':' + '\"' + modifiedTime + '\"'  +
                ", \"size\"" + ':' + '\"' + size + '\"'  +
                ", \"urlString\"" + ':' + '\"' + urlString + '\"'  +
                ", \"Played\"" + ':' + '\"' + Played + '\"'  +
                ", \"season_id\"" + ':' + '\"' + season_id + '\"'  +
                ", \"air_date\"" + ':' + '\"' + air_date + '\"'  +
                        ", \"episode_number\"" + ':' + '\"' + episode_number + '\"'  +
                ", \"production_code\"" + ':' + '\"' + production_code + '\"'  +
                ", \"runtime\"" + ':' + '\"' + runtime + '\"'  +
                ", \"season_number\"" + ':' + '\"' + season_number + '\"'  +
                ", \"still_path\"" + ':' + '\"' + still_path + '\"'  +
                ", \"id\"" + ':' +  '\"' +id +'\"'  +
                ", \"name\"" + ':'  +'\"' + name + '\"' +
                ", \"overview\""  + ':' +  '\"' +overview +'\"' +
                ", \"vote_average\""  + ':' +  '\"' +vote_average +'\"' +
                ", \"vote_count\""  + ':' +  '\"' +vote_count +'\"' +
                ", \"season_number\""  + ':' + '\"' +season_number +'\"' +
                ", \"show_id\"" + ':' + '\"' + show_id + '\"'  +
                '}' ;
//                        +"}" ;
    }

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

//    public ArrayList<Crew> getCrew() {
//        return crew;
//    }
//
//    public void setCrew(ArrayList<Crew> crew) {
//        this.crew = crew;
//    }
//
//    public ArrayList<GuestStar> getGuest_stars() {
//        return guest_stars;
//    }
//
//    public void setGuest_stars(ArrayList<GuestStar> guest_stars) {
//        this.guest_stars = guest_stars;
//    }

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public int getIdForDB() {
        return idForDB;
    }

    public void setIdForDB(int idForDB) {
        this.idForDB = idForDB;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPlayed() {
        return Played;
    }

    public void setPlayed(int played) {
        Played = played;
    }


    public int getSeason_id() {
        return season_id;
    }

    public void setSeason_id(int season_id) {
        this.season_id = season_id;
    }
}
