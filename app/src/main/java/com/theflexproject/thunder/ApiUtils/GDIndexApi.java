package com.theflexproject.thunder.ApiUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Url;

public class GDIndexApi {
    public static GDIndexService gdIndexService = null;

    public static GDIndexService getGDIndexService() {
        if (gdIndexService == null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://example.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gdIndexService = retrofit.create(GDIndexService.class);
        }
        return gdIndexService;
    }

    public interface GDIndexService {
        @FormUrlEncoded
        @POST
        Call<ResponseBody> getData(
                @Url String url,
                @Header("Authorization") String authHeader,
                @Field("page_token") String pageToken,
                @Field("page_index") int pageIndex
        );
    }
}
