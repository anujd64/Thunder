package com.theflexproject.thunder.ApiUtils;

import static com.theflexproject.thunder.Constants.FANART_IMAGE_BASE_URL;

import com.theflexproject.thunder.model.FanArt.FanArtMovieResponseModel;
import com.theflexproject.thunder.model.FanArt.FanArtTvResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class FanartApi {
    public static FanartService fanartService = null;

    public static FanartService getFanartService() {
        if(fanartService==null){
            Retrofit retrofit =new Retrofit.Builder()
                    .baseUrl(FANART_IMAGE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            fanartService = retrofit.create(FanartService.class);
        }
        return fanartService;
    }

    public interface FanartService{
        @GET( "movies/{id}" )
        Call<FanArtMovieResponseModel> getImagesMovie(@Path("id") long id, @Query("api_key") String key);

        @GET( "tv/{id}" )
        Call<FanArtTvResponseModel> getImagesTV(@Path("id") long id, @Query("api_key") String key);
    }
}

