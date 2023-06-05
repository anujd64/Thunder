package com.theflexproject.thunder.di

import com.theflexproject.thunder.ApiUtils.GDIndexApi.GDIndexService
import com.theflexproject.thunder.ApiUtils.GoIndexApi.GoIndexService
import com.theflexproject.thunder.ApiUtils.TMDBApi.TMDBService
import com.theflexproject.thunder.ApiUtils.TgIndexApi.TgIndexService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {




    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit = Retrofit.Builder()
            .baseUrl("http://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Singleton
    @Provides
    fun providesGDIndexService(retrofit: Retrofit) : GDIndexService = retrofit.create(GDIndexService::class.java)

    @Singleton
    @Provides
    fun providesGoIndexService(retrofit: Retrofit) : GoIndexService = retrofit.create(GoIndexService::class.java)

    @Singleton
    @Provides
    fun providesTGIndexService(retrofit: Retrofit) : TgIndexService = retrofit.create(TgIndexService::class.java)

    @Singleton
    @Provides
    fun providesTMDBApiService(retrofit: Retrofit) : TMDBService = retrofit.create(TMDBService::class.java)




//    @Singleton
//    @Provides
//    fun providesGson(): Gson = GsonBuilder().create()
//
//    @Provides
//    @Named("TMDB_BASE_URL")
//    fun provideSimpleProgramBaseUrl() = Constants.TMDB_BASE_URL
//
//    @Provides
//    @Named("FANART_IMAGE_BASE_URL")
//    fun provideConfigApiBaseUrl() = Constants.FANART_IMAGE_BASE_URL

//    @Singleton
//    @Provides
//    @Named("retrofitSimpleProgram")
//    fun provideRetrofitSimpleProgram(@Named("SimpleProgramBaseUrl") baseUrl: String, gson: Gson): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .baseUrl(baseUrl)
//            .build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideSimpleProgramApiService(@Named("retrofitSimpleProgram") retrofit: Retrofit): SimpleProgramApiService {
//        return retrofit.create(SimpleProgramApiService::class.java)
//    }
//
//    @Singleton
//    @Provides
//    @Named("retrofitConfigApi")
//    fun provideRetrofitConfigApi(@Named("ConfigApiBaseUrl") baseUrl: String, gson: Gson): Retrofit {
//        return Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .baseUrl(baseUrl)
//            .build()
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideConfigApiService(@Named("retrofitConfigApi") retrofit: Retrofit): ConfigApiService {
//        return retrofit.create(ConfigApiService::class.java)
//    }
}