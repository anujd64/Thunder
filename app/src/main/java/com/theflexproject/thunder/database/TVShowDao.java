package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.TVShowInfo.TVShow;

import java.util.List;

@Dao
public interface TVShowDao {
    @Query("SELECT * FROM TVShow")
    List<TVShow> getAll();

    @Query("SELECT * FROM TVShow WHERE TVShow.name LIKE '%' || :string || '%' OR TVShow.overview like '%' || :string || '%' ")
    List<TVShow> getSearchQuery(String string);

    @Query("SELECT * FROM TVShow WHERE id LIKE :id")
    TVShow find(int id);

    @Query("SELECT * FROM TVShow WHERE name LIKE :name")
    TVShow getByShowName(String name);

    @Query("SELECT * FROM TVShow order by last_air_date desc limit 10")
    List<TVShow> getNewShows();

    @Query("SELECT * FROM TVShow order by vote_average desc limit 10")
    List<TVShow> getTopRated();

    @Query("Delete FROM TVShow WHERE id like :show_id")
    void deleteById(int show_id);

    @Insert
    void insert(TVShow... tvShows);

    @Delete
    void delete(TVShow tvShow);



}
