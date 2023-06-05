package com.theflexproject.thunder.ApiUtils;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {
    private final String apiKey;
    private final String language;

    public ApiKeyInterceptor(String apiKey, String language) {
        this.apiKey = apiKey;
        this.language = language;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl originalUrl = originalRequest.url();

        HttpUrl url = originalUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .addQueryParameter("language", language)
                .build();

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .url(url);

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
