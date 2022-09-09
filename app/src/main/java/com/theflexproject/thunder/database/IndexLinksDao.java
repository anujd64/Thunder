package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.IndexLink;

import java.util.List;

@Dao
public interface IndexLinksDao {
        @Query("SELECT * FROM IndexLink")
        List<IndexLink> getAll ();
        @Query("SELECT * FROM INDEXLINK WHERE LINK LIKE :link")
        IndexLink find(String link);

        @Insert
        void insert (IndexLink...indexlinks);

        @Delete
        void delete (IndexLink indexLink);

        @Query("Delete from indexlink where link like :link")
        void deleteIndexLink(String link);
}
