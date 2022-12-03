package com.theflexproject.thunder.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.theflexproject.thunder.model.ResFormat;

import java.util.List;

@Dao
public interface ResFormatDao {
    @Query("SELECT * FROM ResFormat")
    List<ResFormat> getAll();

    @Insert
    void insert(ResFormat... resFormats);

    @Delete
    void delete(ResFormat resFormat);
}

