package com.theflexproject.thunder.ApiUtils;

import static com.theflexproject.thunder.Constants.APP_LANGUAGE;
import static com.theflexproject.thunder.Constants.TMDB_API_KEY;
import static com.theflexproject.thunder.Constants.TMDB_BASE_URL;

import com.theflexproject.thunder.model.ExternalIds;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MoviesResponseFromTMDB;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.model.TVShowInfo.TVShowsResponseFromTMDB;
import com.theflexproject.thunder.model.tmdbImages.TMDBImagesResponse;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TMDBApi {

    public static TMDBService tmdbService = null;

    public static TMDBService getTMDBService() {
        if (tmdbService == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new ApiKeyInterceptor(TMDB_API_KEY, APP_LANGUAGE))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tmdbService = retrofit.create(TMDBService.class);
        }
        return tmdbService;
    }

    public interface TMDBService {
//        @GET("movie/{id}/images?api_key=" + key)
//        Response<TMDBImagesResponse> getImagesForMovieFromTMDB(@Path("id") long id);
//
//        @GET("tv/{id}/images?api_key=" + key)
//        Response<TMDBImagesResponse> getImagesForTVFromTMDB(@Path("id") long id);
//
//        @GET("tv/{tvShowId}/external_ids?api_key=" + TMDB_API_KEY)
//        Response<ExternalIds> getExternalIdsForTV(@Path("tvShowId") long tvShowId);
//
//        @GET("movie/{id}?api_key=" + TMDB_API_KEY + "&language=en-US")
//        Response<Movie> getMovieByTMDBId(@Path("id") long id);
//
//        @GET("tv/{id}/season/{seasonNumber}?api_key=" + TMDB_API_KEY + "&language=en-US")
//        Response<TVShowSeasonDetails> getSeasonByTVId(@Path("id") long id, @Path("seasonNumber") String seasonNumber);
//
//        @GET("tv/{id}?api_key=" + TMDB_API_KEY + "&language=en-US")
//        Response<TVShow> getByTVId(@Path("id") long id);
//
//        @GET("movie/{id}")
//        Response<Movie> getMovieById(
//                @Path("id") long id,
//                @Query("api_key") String apiKey,
//                @Query("language") String language
//        );
        @GET("movie/{id}/images")
        Call<TMDBImagesResponse> getImagesForMovieFromTMDB(
                @Path("id") long id
        );

        @GET("tv/{id}/images")
        Call<TMDBImagesResponse> getImagesForTVFromTMDB(
                @Path("id") long id
        );

        @GET("tv/{tvShowId}/external_ids")
        Call<ExternalIds> getExternalIdsForTV(
                @Path("tvShowId") long tvShowId
        );

        @GET("movie/{id}")
        Call<Movie> getMovieByTMDBId(
                @Path("id") long id
        );

        @GET("tv/{id}/season/{seasonNumber}")
        Call<TVShowSeasonDetails> getSeasonByTVId(
                @Path("id") long id,
                @Path("seasonNumber") String seasonNumber
        );

        @GET("tv/{id}")
        Call<TVShow> getByTVId(
                @Path("id") long id,
                @Query("language") String language
        );

        @GET("search/tv")
        Call<TVShowsResponseFromTMDB> searchTVShow(
                @Query("query") String showName,
                @Query("include_adult") boolean includeAdult,
                @Query("page") int page
        );

        @GET("search/movie")
        Call<MoviesResponseFromTMDB> searchMovie(
                @Query("query") String title,
                @Query("year") String year,
                @Query("include_adult") boolean includeAdult,
                @Query("page") int page
        );

        @GET("movie/{id}")
        Call<Movie> getMovieById(
                @Path("id") long id
        );

        @GET("tv/{id}")
        Call<TVShow> getTVShowById(
                @Path("id") long id
        );


    }

}
