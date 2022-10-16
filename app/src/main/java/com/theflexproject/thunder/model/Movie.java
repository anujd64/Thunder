package com.theflexproject.thunder.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;

//@Entity
//public class Movie {
//    //index attributes
//    @PrimaryKey(autoGenerate = true)
//    @NonNull
//    public int fileid;
//    public String id;
//    public String name;
//    public String mimeType;
//    public Date modifiedTime;
//    public String size;
//    public String urlString;
//
//    public int Played;
//
//    public int isPlayed() {
//        return Played;
//    }
//
//    public void setPlayed(int played) {
//        Played = played;
//    }
//    //tmdb attributes
//
//
//    public String title;
//    public boolean adult;
//
//    public String backdrop_path;
//    public String original_language;
//
//
//
////    public int[] genre_ids;
//
//    public String original_title;
//    public String overview;
//    public double popularity;
//
//    public String poster_path;
//    public String release_date;
//
//    public boolean video;
//    public double vote_average;
//    public int vote_count;
//
//    public boolean isAdult() {
//        return adult;
//    }
//
//    public void setAdult(boolean adult) {
//        this.adult = adult;
//    }
//
//    public String getBackdrop_path() {
//        return backdrop_path;
//    }
//
//    public void setBackdrop_path(String backdrop_path) {
//        this.backdrop_path = backdrop_path;
//    }
//
//    public String getOriginal_language() {
//        return original_language;
//    }
//
//    public void setOriginal_language(String original_language) {
//        this.original_language = original_language;
//    }
//
//    public String getOriginal_title() {
//        return original_title;
//    }
//
//    public void setOriginal_title(String original_title) {
//        this.original_title = original_title;
//    }
//
//    public String getOverview() {
//        return overview;
//    }
//
//    public void setOverview(String overview) {
//        this.overview = overview;
//    }
//
//    public double getPopularity() {
//        return popularity;
//    }
//
//    public void setPopularity(double popularity) {
//        this.popularity = popularity;
//    }
//
//    public String getPoster_path() {
//        return poster_path;
//    }
//
//    public void setPoster_path(String poster_path) {
//        this.poster_path = poster_path;
//    }
//
//    public String getRelease_date() {
//        return release_date;
//    }
//
//    public void setRelease_date(String release_date) {
//        this.release_date = release_date;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public boolean isVideo() {
//        return video;
//    }
//
//    public void setVideo(boolean video) {
//        this.video = video;
//    }
//
//    public double getVote_average() {
//        return vote_average;
//    }
//
//    public void setVote_average(double vote_average) {
//        this.vote_average = vote_average;
//    }
//
//    public int getVote_count() {
//        return vote_count;
//    }
//
//    public void setVote_count(int vote_count) {
//        this.vote_count = vote_count;
//    }
//
//    public String getUrlstring() {
//        return urlString;
//    }
//
//    public void setUrlstring(String urlString) {
//        this.urlString = urlString;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getMimeType() {
//        return mimeType;
//    }
//
//    public void setMimeType(String mimeType) {
//        this.mimeType = mimeType;
//    }
//
//    public Date getModifiedTime() {
//        return modifiedTime;
//    }
//
//    public void setModifiedTime(Date modifiedTime) {
//        this.modifiedTime = modifiedTime;
//    }
//
//    public String getSize() {
//        return size;
//    }
//
//    public void setSize(String size) {
//        this.size = size;
//    }
//
////    public int[] getGenre_ids() {
////        return genre_ids;
////    }
////
////    public void setGenre_ids(int[] genre_ids) {
////        this.genre_ids = genre_ids;
////    }
//
//    @Override
//    public String toString() {
//        return "File{" +
//                "fileid=" + fileid +
//                ", id='" + id + '\'' +
//                ", name='" + name + '\'' +
//                ", mimeType='" + mimeType + '\'' +
//                ", modifiedTime=" + modifiedTime +
//                ", size='" + size + '\'' +
//                ", urlString='" + urlString + '\'' +
//                ", Played=" + Played +
//                ", title='" + title + '\'' +
//                ", adult=" + adult +
//                ", backdrop_path='" + backdrop_path + '\'' +
//                ", original_language='" + original_language + '\'' +
//                ", original_title='" + original_title + '\'' +
//                ", overview='" + overview + '\'' +
//                ", popularity=" + popularity +
//                ", poster_path='" + poster_path + '\'' +
//                ", release_date='" + release_date + '\'' +
//                ", video=" + video +
//                ", vote_average=" + vote_average +
//                ", vote_count=" + vote_count +
//                '}';
//    }
//
//}

@Entity
public class Movie implements MyMedia{
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int fileidForDB;
    public String fileName;
    public String mimeType;
    public Date modifiedTime;
    public String size;
    public String urlString;

    public int Played;


    public boolean adult;
    public String backdrop_path;
//    public Object belongs_to_collection;
    public int budget;
    public ArrayList<Genre> genres;
    public String homepage;
    public int id;
    public String imdb_id;
    public String original_language;
    public String original_title;
    public String overview;
    public double popularity;
    public String poster_path;
//    public ArrayList<ProductionCompany> production_companies;
//    public ArrayList<ProductionCountry> production_countries;
    public String release_date;
    public int revenue;
    public int runtime;
//    public ArrayList<SpokenLanguage> spoken_languages;
    public String status;
    public String tagline;
    public String title;
    public boolean video;
    public double vote_average;
    public int vote_count;

    @Override
    public String toString() {
        return "{" + "\"Movie\"" + ':' + "{" +
                "\"fileidForDB\"" + ':' + '\"' + fileidForDB + '\"'  +
                ", \"fileName\"" + ':' + '\"' + fileName + '\"'  +
                ", \"mimeType\"" + ':' + '\"' + mimeType + '\"'  +
                ", \"modifiedTime\"" + ':' + '\"' + modifiedTime + '\"'  +
                ", \"size\"" + ':' + '\"' + size + '\"'  +
                ", \"urlString\"" + ':' + '\"' + urlString + '\"'  +
                ", \"Played\"" + ':' + '\"' + Played + '\"'  +
                ", \"runtime\"" + ':' + '\"' + runtime + '\"'  +
                ", \"id\"" + ':' +  '\"' +id +'\"'  +
                ", \"overview\""  + ':' +  '\"' +overview +'\"' +
                ", \"vote_average\""  + ':' +  '\"' +vote_average +'\"' +
                ", \"vote_count\""  + ':' +  '\"' +vote_count +'\"' +
                ", \"original_language\""  + ':' +  '\"' +original_language +'\"' +
                ", \"imdb_id\""  + ':' +  '\"' +imdb_id +'\"' +
                ", \"id\""  + ':' +  '\"' +id +'\"' +
                ", \"homepage\""  + ':' +  '\"' +homepage +'\"' +
                ", \"genres\""  + ':' +  '\"' +genres +'\"' +
                ", \"budget\""  + ':' +  '\"' +budget +'\"' +
                ", \"backdrop_path\""  + ':' +  '\"' +backdrop_path +'\"' +
                ", \"adult\""  + ':' +  '\"' +adult +'\"' +
                ", \"original_title\""  + ':' +  '\"' +original_title +'\"' +
                ", \"overview\""  + ':' +  '\"' +overview +'\"' +
                ", \"popularity\""  + ':' +  '\"' +popularity +'\"' +
                ", \"poster_path\""  + ':' +  '\"' +poster_path +'\"' +
                ", \"release_date\""  + ':' +  '\"' +release_date +'\"' +
                ", \"revenue\""  + ':' +  '\"' +revenue +'\"' +
                ", \"runtime\""  + ':' +  '\"' +runtime +'\"' +
                ", \"video\""  + ':' +  '\"' +video +'\"' +
                ", \"title\""  + ':' +  '\"' +title +'\"' +
                ", \"tagline\""  + ':' +  '\"' +tagline +'\"' +
                ", \"status\""  + ':' +  '\"' +status +'\"' +
//                ", production_companies=" + production_companies +
//                ", production_countries=" + production_countries +
//                ", spoken_languages=" + spoken_languages +
                '}' +"}";
    }

    public int getFileidForDB() {
        return fileidForDB;
    }

    public void setFileidForDB(int fileidForDB) {
        this.fileidForDB = fileidForDB;
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

    public String getUrlString() {
        return urlString;
    }

    public void setUrlString(String urlString) {
        this.urlString = urlString;
    }

    public int getPlayed() {
        return Played;
    }

    public void setPlayed(int played) {
        Played = played;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

//    public Object getBelongs_to_collection() {
//        return belongs_to_collection;
//    }
//
//    public void setBelongs_to_collection(Object belongs_to_collection) {
//        this.belongs_to_collection = belongs_to_collection;
//    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Genre> genres) {
        this.genres = genres;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

//    public ArrayList<ProductionCompany> getProduction_companies() {
//        return production_companies;
//    }
//
//    public void setProduction_companies(ArrayList<ProductionCompany> production_companies) {
//        this.production_companies = production_companies;
//    }
//
//    public ArrayList<ProductionCountry> getProduction_countries() {
//        return production_countries;
//    }
//
//    public void setProduction_countries(ArrayList<ProductionCountry> production_countries) {
//        this.production_countries = production_countries;
//    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

//    public ArrayList<SpokenLanguage> getSpoken_languages() {
//        return spoken_languages;
//    }
//
//    public void setSpoken_languages(ArrayList<SpokenLanguage> spoken_languages) {
//        this.spoken_languages = spoken_languages;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
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
}

