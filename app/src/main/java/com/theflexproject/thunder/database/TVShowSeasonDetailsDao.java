package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;

import java.util.List;

@Dao
public interface TVShowSeasonDetailsDao {
    @Query("SELECT * FROM TVShowSeasonDetails")
    List<TVShowSeasonDetails> getAll();

    @Insert
    void insert(TVShowSeasonDetails... movies);

    @Delete
    void delete(TVShowSeasonDetails movie);

    @Query("SELECT * FROM TVShowSeasonDetails WHERE id like :id")
    TVShowSeasonDetails find(int id);

    @Query("Delete FROM TVShowSeasonDetails WHERE id like :season_id")
    void deleteById(int season_id);

    @Query("SELECT * FROM TVShowSeasonDetails WHERE show_id like :show_id")
    List<TVShowSeasonDetails> findByShowId(int show_id);

}
