package com.theflexproject.thunder.ApiUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Url;

public class TgIndexApi {
    public static TgIndexApi.TgIndexService tgIndexService = null;

    public static TgIndexApi.TgIndexService getTgIndexService() {
        if (tgIndexService == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://example.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            tgIndexService = retrofit.create(TgIndexApi.TgIndexService.class);
        }
        return tgIndexService;
    }

    public interface TgIndexService {
        @GET
        Call<ResponseBody> getResponseBody(
                @Url String url
        );
    }

}
