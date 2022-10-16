package com.theflexproject.thunder.model.TVShowInfo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.theflexproject.thunder.model.Genre;
import com.theflexproject.thunder.model.MyMedia;

import java.util.ArrayList;

@Entity
public class TVShow implements MyMedia {
    @PrimaryKey (autoGenerate = true)
    @NonNull
    public int idForDB;
    public boolean adult;
    public String backdrop_path;
    public String homepage;
    public String first_air_date;
    public int id;
    public boolean in_production;
    public String last_air_date;
    public String name;
    public int number_of_episodes;
    public int number_of_seasons;
    public String original_name;
    public String overview;
    public double popularity;
    public String original_language;
    public String poster_path;
    public String status;
    public ArrayList<Season> seasons;
    public String tagline;
    public String type;
    public double vote_average;
    public int vote_count;
    public ArrayList<Genre> genres;

    //    public ArrayList<CreatedBy> created_by;
//    public ArrayList<Integer> episode_run_time;
    //    public ArrayList<Network> networks;
//    public ArrayList<ProductionCompany> production_companies;
//    public ArrayList<ProductionCountry> production_countries;
//    public ArrayList<SpokenLanguage> spoken_languages;
//    public ArrayList<String> origin_country;
//    public Object next_episode_to_air;
//    public LastEpisodeToAir last_episode_to_air;
//    public ArrayList<String> languages;

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

//    public ArrayList<CreatedBy> getCreated_by() {
//        return created_by;
//    }
//
//    public void setCreated_by(ArrayList<CreatedBy> created_by) {
//        this.created_by = created_by;
//    }
//
//    public ArrayList<Integer> getEpisode_run_time() {
//        return episode_run_time;
//    }
//
//    public void setEpisode_run_time(ArrayList<Integer> episode_run_time) {
//        this.episode_run_time = episode_run_time;
//    }

    public String getFirst_air_date() {
        return first_air_date;
    }

    public void setFirst_air_date(String first_air_date) {
        this.first_air_date = first_air_date;
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

    public boolean isIn_production() {
        return in_production;
    }

    public void setIn_production(boolean in_production) {
        this.in_production = in_production;
    }

//    public ArrayList<String> getLanguages() {
//        return languages;
//    }

//    public void setLanguages(ArrayList<String> languages) {
//        this.languages = languages;
//    }

    public String getLast_air_date() {
        return last_air_date;
    }

    public void setLast_air_date(String last_air_date) {
        this.last_air_date = last_air_date;
    }

//    public LastEpisodeToAir getLast_episode_to_air() {
//        return last_episode_to_air;
//    }

//    public void setLast_episode_to_air(LastEpisodeToAir last_episode_to_air) {
//        this.last_episode_to_air = last_episode_to_air;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Object getNext_episode_to_air() {
//        return next_episode_to_air;
//    }
//
//    public void setNext_episode_to_air(Object next_episode_to_air) {
//        this.next_episode_to_air = next_episode_to_air;
//    }
//
//    public ArrayList<Network> getNetworks() {
//        return networks;
//    }
//
//    public void setNetworks(ArrayList<Network> networks) {
//        this.networks = networks;
//    }

    public int getNumber_of_episodes() {
        return number_of_episodes;
    }

    public void setNumber_of_episodes(int number_of_episodes) {
        this.number_of_episodes = number_of_episodes;
    }

    public int getNumber_of_seasons() {
        return number_of_seasons;
    }

    public void setNumber_of_seasons(int number_of_seasons) {
        this.number_of_seasons = number_of_seasons;
    }

//    public ArrayList<String> getOrigin_country() {
//        return origin_country;
//    }
//
//    public void setOrigin_country(ArrayList<String> origin_country) {
//        this.origin_country = origin_country;
//    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_name() {
        return original_name;
    }

    public void setOriginal_name(String original_name) {
        this.original_name = original_name;
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
//
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
//
    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public void setSeasons(ArrayList<Season> seasons) {
        this.seasons = seasons;
    }
//
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public int getIdForDB() {
        return idForDB;
    }

    public void setIdForDB(int idForDB) {
        this.idForDB = idForDB;
    }

    @Override
    public String toString() {
        return "TVShow{" +
                "idForDB=" + idForDB +
                ", adult=" + adult +
                ", backdrop_path='" + backdrop_path + '\'' +
                ", first_air_date='" + first_air_date + '\'' +
                ", homepage='" + homepage + '\'' +
                ", id=" + id +
                ", in_production=" + in_production +
                ", last_air_date='" + last_air_date + '\'' +
                ", name='" + name + '\'' +
                ", number_of_episodes=" + number_of_episodes +
                ", number_of_seasons=" + number_of_seasons +
                ", original_language='" + original_language + '\'' +
                ", original_name='" + original_name + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", poster_path='" + poster_path + '\'' +
                ", seasons=" + seasons +
                ", status='" + status + '\'' +
                ", tagline='" + tagline + '\'' +
                ", type='" + type + '\'' +
                ", vote_average=" + vote_average +
                ", vote_count=" + vote_count +
                '}';
    }
}

