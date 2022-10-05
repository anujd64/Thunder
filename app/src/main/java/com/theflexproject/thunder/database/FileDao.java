package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.File;

import java.util.List;

@Dao
public interface FileDao {
    @Query("SELECT * FROM File")
    List<File> getAll();

    @Query("SELECT * FROM file WHERE name LIKE '%' || :string || '%' OR title like '%' || :string || '%' OR urlString like '%' || :string || '%' or overview like '%' || :string || '%'")
    List<File> getSearchQuery(String string);

    @Query("SELECT * FROM file WHERE name LIKE :fileName")
    File getByFileName(String fileName);

    @Query("SELECT * FROM File WHERE poster_path IS NOT NULL group by TITLE ORDER BY vote_average DESC Limit 10")
    List<File> getTopRated();

    @Query("SELECT * from file  WHERE backdrop_path IS NOT NULL group by TITLE ORDER BY modifiedTime DESC Limit 10")
    List<File> getrecentlyadded();

    @Query("SELECT * FROM File WHERE poster_path IS NOT NULL group by TITLE ORDER BY release_date DESC Limit 10")
    List<File> getrecentreleases();

    @Query("Delete from file where urlString like '%'|| :indexlink || '%'")
    void deleteByIndexLink(String indexlink);

    @Insert
    void insert(File... files);

    @Delete
    void delete(File file);

    @Query("Delete from file where urlString like :indexLink || '%'")
    void deleteAllFromthisIndex(String indexLink);

    @Query("SELECT * FROM FILE WHERE Played = 1")
    List<File> getPlayed();

    @Query("Update file set played = 1 where name like :filename")
    void updatePlayed(String filename);

    @Query("SELECT * FROM File ORDER BY urlString")
    List<File> sortByIndex();

    @Query("SELECT * FROM File ORDER BY cast(size as unsigned) desc")
    List<File> sortBySize();

    @Query("SELECT * FROM File ORDER BY release_date desc")
    List<File> sortByRelease();

    @Query("SELECT * FROM File ORDER BY modifiedTime desc")
    List<File> sortByTime();

    @Query("SELECT * FROM File ORDER BY name")
    List<File> sortByFileName();

    @Query("SELECT * FROM File ORDER BY title")
    List<File> sortByTitle();
}
