package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.TVShowInfo.Episode;

import java.util.List;

@Dao
public interface EpisodeDao {
    @Query("SELECT * FROM Episode")
    List<Episode> getAll();

    @Query("SELECT * FROM Episode WHERE id LIKE :id")
    Episode find(int id);

    @Query("SELECT * FROM Episode WHERE fileName LIKE :fileName")
    Episode findByFileName(String fileName);

    @Query("SELECT * FROM Episode WHERE season_id=:season_id AND played=0 order by episode_number limit 1 ")
    Episode getNextEpisodeInSeason(int season_id);

    @Query("SELECT * FROM Episode WHERE show_id=:show_id AND played=0 order by season_number,episode_number limit 1 ")
    Episode getNextEpisodeInTVShow(int show_id);

    @Query("SELECT * FROM Episode WHERE id LIKE :id")
    List<Episode> byEpisodeId(int id);

    @Query("SELECT * FROM Episode WHERE id LIKE :id ORDER BY cast(size as unsigned) desc limit 1")
    Episode byEpisodeIdLargest(int id);

    @Query("UPDATE Episode SET played = 1 WHERE id LIKE :episodeId")
    void updatePlayed(int episodeId);

    @Query("SELECT * FROM Episode WHERE show_id=:show_id AND season_id=:season_id GROUP BY id ORDER BY episode_number ASC")
    List<Episode> getFromThisSeason(int show_id, int season_id);


    @Query("SELECT * FROM Episode WHERE season_id=:season_id")
    List<Episode> getFromSeasonOnly(int season_id);

    @Insert
    void insert(Episode... episodes);

    @Delete
    void delete(Episode episode);

    @Query("Delete from Episode where urlString like :urlString || '%' ")
    void deleteAllFromthisIndex(String urlString);
}
