package com.theflexproject.thunder.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.theflexproject.thunder.model.IndexLink;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.ResFormat;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;

@Database(entities = {ResFormat.class, Movie.class, IndexLink.class, TVShow.class, TVShowSeasonDetails.class, Episode.class},
        version = 23)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract ResFormatDao resFormatDao();
    public abstract MovieDao movieDao();
    public abstract TVShowDao tvShowDao();
    public abstract EpisodeDao episodeDao();
    public abstract TVShowSeasonDetailsDao tvShowSeasonDetailsDao();
    public abstract IndexLinksDao indexLinksDao();
}
