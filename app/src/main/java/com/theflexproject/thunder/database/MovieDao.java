package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie GROUP BY title")
    List<Movie> getAll();

    @Query("SELECT * FROM Movie WHERE id=:id")
    Movie byId(int id);

    @Query("SELECT * FROM Movie WHERE id=:id")
    List<Movie> getAllById(int id);

    @Query("SELECT * FROM Movie WHERE id=:id ORDER BY size DESC limit 1")
    Movie byIdLargest(int id);

    @Query("SELECT * FROM Movie WHERE fileName LIKE '%' || :string || '%' OR title like '%' || :string || '%' OR urlString like '%' || :string || '%' or overview like '%' || :string || '%'")
    List<Movie> getSearchQuery(String string);

    @Query("SELECT * FROM Movie WHERE fileName LIKE :fileName")
    Movie getByFileName(String fileName);

    @Query("SELECT * FROM Movie WHERE poster_path IS NOT NULL group by TITLE ORDER BY vote_average DESC Limit 10")
    List<Movie> getTopRated();

    @Query("SELECT * from Movie  WHERE backdrop_path IS NOT NULL group by TITLE ORDER BY modifiedTime DESC Limit 10")
    List<Movie> getrecentlyadded();

    @Query("SELECT * FROM Movie WHERE poster_path IS NOT NULL group by TITLE ORDER BY release_date DESC Limit 10")
    List<Movie> getrecentreleases();

    @Query("Delete from Movie where urlString like '%'|| :indexlink || '%'")
    void deleteByIndexLink(String indexlink);

    @Insert
    void insert(Movie... movies);

    @Delete
    void delete(Movie movie);

    @Query("Delete from Movie where urlString like :indexLink || '%'")
    void deleteAllFromthisIndex(String indexLink);

    @Query("SELECT Distinct * FROM Movie WHERE Played = 1")
    List<Movie> getPlayed();

    @Query("Update Movie set played = 1 where id = :id")
    void updatePlayed(int id);

    @Query("SELECT * FROM Movie ORDER BY urlString")
    List<Movie> sortByIndex();

    @Query("SELECT * FROM Movie ORDER BY cast(size as unsigned) desc")
    List<Movie> sortBySize();

    @Query("SELECT * FROM Movie ORDER BY release_date desc")
    List<Movie> sortByRelease();

    @Query("SELECT * FROM Movie ORDER BY modifiedTime desc")
    List<Movie> sortByTime();

    @Query("SELECT * FROM Movie ORDER BY fileName")
    List<Movie> sortByFileName();

    @Query("SELECT * FROM Movie ORDER BY title")
    List<Movie> sortByTitle();
}
