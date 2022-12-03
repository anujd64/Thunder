package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.TVShowInfo.Episode;

import java.util.List;

@Dao
public interface EpisodeDao {
    @Query("SELECT * FROM Episode ")
    List<Episode> getAll();

    @Query("SELECT * FROM Episode WHERE id LIKE :id and disabled =0")
    Episode find(int id);

    @Query("SELECT * FROM Episode WHERE fileName LIKE :fileName and disabled =0")
    Episode findByFileName(String fileName);

    @Query("SELECT * FROM Episode WHERE season_id=:season_id AND played=0 and disabled =0 order by episode_number limit 1 ")
    Episode getNextEpisodeInSeason(int season_id);

    @Query("SELECT * FROM Episode WHERE show_id=:show_id AND played=0 and disabled =0 order by season_number,episode_number limit 1")
    Episode getNextEpisodeInTVShow(int show_id);

    @Query("SELECT * FROM Episode WHERE id=:id and disabled =0")
    List<Episode> byEpisodeId(int id);

    @Query("SELECT * FROM Episode WHERE id=:id and disabled =0 ORDER BY cast(size as unsigned) desc limit 1")
    Episode byEpisodeIdLargest(int id);

    @Query("UPDATE Episode SET played = 1 WHERE id = :episodeId and disabled =0")
    void updatePlayed(int episodeId);

    @Query("SELECT * FROM Episode WHERE show_id=:show_id AND season_id=:season_id and disabled =0 GROUP BY id ORDER BY episode_number ASC")
    List<Episode> getFromThisSeason(int show_id, int season_id);


    @Query("SELECT * FROM Episode WHERE season_id=:season_id and disabled =0")
    List<Episode> getFromSeasonOnly(int season_id);


    @Query("SELECT * FROM Episode WHERE show_id=:show_id and disabled =0")
    List<Episode> getFromThisShow(long show_id);
    @Insert
    void insert(Episode... episodes);

    @Delete
    void delete(Episode episode);

    @Query("Delete from Episode where  index_id = :index_id")
    void deleteAllFromThisIndex(int index_id);


    @Query("select * from Episode where  index_id = :index_id and disabled =0")
    Episode findByLink(int index_id);

    @Query("Delete from Episode where urlString=:link ")
    void deleteByLink(String link);

    @Query("select count( distinct show_id ) from Episode  where index_id = :index_id ")
    int getNoOfShows(int index_id);

    @Query("UPDATE Episode set disabled=1 WHERE  index_id = :index_id")
    void disableFromThisIndex(int index_id);

    @Query("UPDATE Episode set disabled=0 WHERE index_id = :index_id")
    void enableFromThisIndex(int index_id);

    @Query("SELECT * FROM Episode WHERE show_id=:show_id and disabled =0 order by season_number,episode_number limit 1 ")
    Episode getFirstAvailableEpisode(long show_id);


    @Query("SELECT * FROM Episode WHERE gd_id =:id")
    Episode findByGdId(String id);

    @Query("DELETE FROM Episode WHERE gd_id =:id")
    void deleteByGdId(String id);
}
