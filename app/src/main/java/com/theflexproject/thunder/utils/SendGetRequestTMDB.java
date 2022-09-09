package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.Constants.TMDB_API_KEY;
import static com.theflexproject.thunder.Constants.TMDB_GET_REQUEST_BASE_URL;

import android.util.Log;

import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.TMDBResponse;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SendGetRequestTMDB {
    void sendGet(File file){
        String finalurl="";

        try {
            try{
                if(MovieTitleExtractor.getTitleYear(file.getName())!=null) {
                    finalurl = TMDB_GET_REQUEST_BASE_URL +TMDB_API_KEY+"&language=en-US&page=1&include_adult=true&query="
                        + URLEncoder.encode(MovieTitleExtractor.getTitleYear(file.getName()),"UTF-8");
                }
                else{
                    finalurl = TMDB_GET_REQUEST_BASE_URL+TMDB_API_KEY+"&language=en-US&page=1&include_adult=true&query="
                            +URLEncoder.encode(MovieTitleExtractor2.getTitle2(file.getName()),"UTF-8");
                }
            }catch (Exception e){
                Log.i("exc",e.toString());
                e.printStackTrace();
            }
            StringBuilder response=new StringBuilder();
            try {
                URL url = new URL(finalurl);
                Log.i("movie title", finalurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                Log.i("tmdb api response", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
            }else {
                    System.out.println("GET request did not work");
                }
                Gson gson = new Gson();
                TMDBResponse tmdbResponse = gson.fromJson(response.toString(), TMDBResponse.class);
                if(tmdbResponse.results.size()>0){
                    file.setBackdrop_path(tmdbResponse.results.get(0).getBackdrop_path());
                    file.setTitle(tmdbResponse.results.get(0).getTitle());
                    file.setOriginal_language(tmdbResponse.results.get(0).getOriginal_language());
                    file.setOriginal_title(tmdbResponse.results.get(0).getOriginal_title());
                    file.setOverview(tmdbResponse.results.get(0).getOverview());
                    file.setPopularity(tmdbResponse.results.get(0).getPopularity());
                    file.setPoster_path(tmdbResponse.results.get(0).getPoster_path());
                    file.setRelease_date(tmdbResponse.results.get(0).getRelease_date());
//                    file.setGenre_ids(tmdbResponse.results.get(0).getGenre_ids());

                    Log.i("sendGet: ",tmdbResponse.results.get(0).toString());
                }


            } catch (MalformedURLException e){
                Log.i("exe",e.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return file;
    }
}
