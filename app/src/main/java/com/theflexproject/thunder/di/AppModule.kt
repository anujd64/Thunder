//package com.theflexproject.thunder.di
//
//import android.content.Context
//import androidx.room.Room
//import com.theflexproject.thunder.database.AppDatabase
//import com.theflexproject.thunder.utils.IndexUtils
//import com.theflexproject.thunder.utils.SendGetRequestTMDB
//import com.theflexproject.thunder.utils.SendPostRequest
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//@Module
//@InstallIn(SingletonComponent::class)
//class AppModule {
//
//    @Singleton
//    @Provides
//    fun provideIndexUtils(appDatabase: AppDatabase, sendPostRequest: SendPostRequest): IndexUtils {
//        return IndexUtils(appDatabase,sendPostRequest)
//    }
//    @Singleton
//    @Provides
//    fun provideSendPostRequest(@ApplicationContext context: Context,appDatabase: AppDatabase, sendGetRequestTMDB: SendGetRequestTMDB): SendPostRequest {
//        return SendPostRequest(context,appDatabase,sendGetRequestTMDB)
//    }
//
//    @Singleton
//    @Provides
//    fun provideSendGetRequestTMDB(@ApplicationContext context: Context,appDatabase: AppDatabase, sendGetRequestTMDB: SendGetRequestTMDB): SendGetRequestTMDB {
//        return SendGetRequestTMDB(context,appDatabase)
//    }
//
//}