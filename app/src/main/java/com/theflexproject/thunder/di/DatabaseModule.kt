package com.theflexproject.thunder.di

import android.content.Context
import androidx.room.Room
import com.theflexproject.thunder.database.AppDatabase
import com.theflexproject.thunder.database.EpisodeDao
import com.theflexproject.thunder.database.IndexLinksDao
import com.theflexproject.thunder.database.MovieDao
import com.theflexproject.thunder.database.ResFormatDao
import com.theflexproject.thunder.database.TVShowDao
import com.theflexproject.thunder.database.TVShowSeasonDetailsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }
    @Singleton
    @Provides
    fun provideEpDao(database: AppDatabase): EpisodeDao {
        return database.episodeDao()
    }
    @Singleton
    @Provides
    fun provideIndexLinksDao(database: AppDatabase): IndexLinksDao {
        return database.indexLinksDao()
    }
    @Singleton
    @Provides
    fun provideResFormatDao(database: AppDatabase): ResFormatDao {
        return database.resFormatDao()
    }
    @Singleton
    @Provides
    fun provideTVShowDao(database: AppDatabase): TVShowDao {
        return database.tvShowDao()
    }
    @Singleton
    @Provides
    fun provideTVShowSeasonDetailsDao(database: AppDatabase): TVShowSeasonDetailsDao {
        return database.tvShowSeasonDetailsDao()
    }
}
