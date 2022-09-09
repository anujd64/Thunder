package com.theflexproject.thunder.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.IndexLink;
import com.theflexproject.thunder.model.ResFormat;

@Database(entities = {ResFormat.class, File.class, IndexLink.class}, version = 10)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract ResFormatDao resFormatDao();
    public abstract FileDao fileDao();
    public abstract IndexLinksDao indexLinksDao();
}
