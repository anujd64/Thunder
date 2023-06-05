package com.theflexproject.thunder.ApiUtils;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class GoIndexApi {
    public static GoIndexService goIndexService = null;

    public static GoIndexService getGoIndexService() {
        if (goIndexService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://example.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            goIndexService = retrofit.create(GoIndexService.class);
        }
        return goIndexService;
    }

    public interface GoIndexService {
        @FormUrlEncoded
        @POST
        Call<ResponseBody> getData(
                @Url String url,
                @Body RequestBody body,
                @Header("Content-Type") String contentType,
                @Header("Accept") String accept,
                @Header("Authorization") String authHeaderValue
        );
    }
}