package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.Constants.TMDB_API_KEY;
import static com.theflexproject.thunder.Constants.TMDB_GET_REQUEST_BASE_URL;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.TMDBResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class SendGetRequestTMDB {


    static void sendGet(File file){
        String finalurl="";
        String[] titleYear1 = MovieTitleExtractor.getTitleYear(file.getName().replaceAll("Copy of ",""));
        String[] titleYear2 = MovieTitleExtractor2.getTitle2(file.getName().replaceAll("Copy of ",""));
        String titleFromFile1 = "",titleFromFile2 = "", yearFromFile1 = "", yearFromFile2 ="";
        if(titleYear1 != null){
            titleFromFile1 = titleYear1[0];
        }else{
            titleFromFile2 = titleYear2[0];
        }
        if(titleYear1 != null){
            yearFromFile1 = titleYear1[0];
        }else{
            yearFromFile2 = titleYear2[0];
        }

        try {
            try{
                if(MovieTitleExtractor.getTitleYear(file.getName())!=null) {
                    finalurl = TMDB_GET_REQUEST_BASE_URL +TMDB_API_KEY+"&language=en-US&page=1&include_adult=false&query="
                        + URLEncoder.encode(titleFromFile1 ,"UTF-8");
                }
                else{
                    finalurl = TMDB_GET_REQUEST_BASE_URL+TMDB_API_KEY+"&language=en-US&page=1&include_adult=false&query="
                            +URLEncoder.encode(titleFromFile2 ,"UTF-8");
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


                //New Changes
                ArrayList<String> titlesFromTMDB = new ArrayList<>();
                ArrayList<String> yearsFromTMDB = new ArrayList<>();
                ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();
                for (int i = 0; i < tmdbResponse.results.size(); i++) {
                    titlesFromTMDB.add(tmdbResponse.results.get(i).getTitle());
                    yearsFromTMDB.add(tmdbResponse.results.get(i).getRelease_date());
                    titlesandYearsFromTMDB.add(tmdbResponse.results.get(i).getTitle()+" "+tmdbResponse.results.get(i).getRelease_date());
                }

                ExtractedResult result;
                String finalTitle ;
                int finalIndex = 0;

                Log.i("list of tmdb" , titlesFromTMDB.toString());
                try{
                    if(titleFromFile1 != null){
                        result = FuzzySearch.extractOne(titleFromFile1 , titlesFromTMDB);
                        Log.i(result.toString(),"result");
                        if(result.getScore()>60){
                            finalTitle = result.getString();
                            finalIndex = result.getIndex();
                            Log.i(result.toString(),finalTitle);
                        }else {
                            result = FuzzySearch.extractOne(titleFromFile1+yearFromFile1 , titlesandYearsFromTMDB);
                            finalTitle = result.getString();
                            finalIndex = result.getIndex();
                            Log.i(result.toString(),finalTitle);
                        }

                    }else {
                        result = FuzzySearch.extractOne(titleFromFile2+" "+yearFromFile2, titlesFromTMDB);
                        if(result.getScore()>60){
                            finalTitle = result.getString();
                            finalIndex = result.getIndex();
                            Log.i(result.toString(),finalTitle);
                        }else {
                            result = FuzzySearch.extractOne(titleFromFile2+yearFromFile2 , titlesandYearsFromTMDB);
                            finalTitle = result.getString();
                            finalIndex = result.getIndex();
                            Log.i(result.toString(),finalTitle);
                        }
                    }
                }catch (Exception e){e.printStackTrace();}




                if(tmdbResponse.results.size()>0){
                    file.setBackdrop_path(tmdbResponse.results.get(finalIndex).getBackdrop_path());
                    file.setTitle(tmdbResponse.results.get(finalIndex).getTitle());
                    file.setOriginal_language(tmdbResponse.results.get(finalIndex).getOriginal_language());
                    file.setOriginal_title(tmdbResponse.results.get(finalIndex).getOriginal_title());
                    file.setOverview(tmdbResponse.results.get(finalIndex).getOverview());
                    file.setPopularity(tmdbResponse.results.get(finalIndex).getPopularity());
                    file.setPoster_path(tmdbResponse.results.get(finalIndex).getPoster_path());
                    file.setRelease_date(tmdbResponse.results.get(finalIndex).getRelease_date());
//                    file.setGenre_ids(tmdbResponse.results.get(0).getGenre_ids());
                    Log.i("sendGet: ",tmdbResponse.results.get(finalIndex).toString());
                }


            } catch (MalformedURLException e){
                Log.i("exe",e.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return file;
    }


    static void sendGet2(File file) throws IOException {
        String finalurl = "";
        String[] titleYear1 = MovieTitleExtractor.getTitleYear(file.getName().replaceAll("Copy of " , ""));
        String[] titleYear2 = MovieTitleExtractor2.getTitle2(file.getName().replaceAll("Copy of " , ""));

        String
                titleFromFile1 = "",
                titleFromFile2 = "",
                yearFromFile1 = ParseUtils.yearExtractor(file.getName()).second,
                yearFromFile2 = ParseUtils.yearExtractor(file.getName()).second;

        ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();

        if (titleYear1 != null) {
            titleFromFile1 = titleYear1[0];
        } else {
            titleFromFile2 = titleYear2[0];
        }
            try {
                if (MovieTitleExtractor.getTitleYear(file.getName()) != null) {
                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
                            + URLEncoder.encode(titleFromFile1 , "UTF-8");
                } else {
                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
                            + URLEncoder.encode(titleFromFile2 , "UTF-8");
                }
            } catch (Exception e) {
                Log.i("exc" , e.toString());
                e.printStackTrace();
            }
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(finalurl);
                Log.i("movie title" , finalurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                Log.i("tmdb api response" , String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                } else {
                    System.out.println("GET request did not work");
                }
                Gson gson = new Gson();
                TMDBResponse tmdbResponse = gson.fromJson(response.toString() , TMDBResponse.class);

                for (int i = 0; i < tmdbResponse.results.size(); i++) {
                    String releaseDate = tmdbResponse.results.get(i).getRelease_date();
                    if (releaseDate != null)
                        titlesandYearsFromTMDB.add(tmdbResponse.results.get(i).getTitle() + " " + releaseDate.split("-")[0]);
                }
                Log.i("result" , titlesandYearsFromTMDB.toString());
                ExtractedResult result;
                String finalTitle;
                int finalIndex = 0;
                try {
                    if (titleFromFile1 != null && yearFromFile1 != null) {
                        result = FuzzySearch.extractOne(titleFromFile1 + " " + yearFromFile1 , titlesandYearsFromTMDB);
                        Log.i("result" , result.toString());
                        finalTitle = result.getString();
                        finalIndex = result.getIndex();
                        Log.i(result.toString() , finalTitle);

                    } else {
                        result = FuzzySearch.extractOne(titleFromFile2 + " " + yearFromFile2 , titlesandYearsFromTMDB);
                        finalTitle = result.getString();
                        finalIndex = result.getIndex();
                        Log.i(result.toString() , finalTitle);
                    }

                    if (tmdbResponse.results.size() > 0) {
                        file.setBackdrop_path(tmdbResponse.results.get(finalIndex).getBackdrop_path());
                        file.setTitle(tmdbResponse.results.get(finalIndex).getTitle());
                        file.setOriginal_language(tmdbResponse.results.get(finalIndex).getOriginal_language());
                        file.setOriginal_title(tmdbResponse.results.get(finalIndex).getOriginal_title());
                        file.setOverview(tmdbResponse.results.get(finalIndex).getOverview());
                        file.setPopularity(tmdbResponse.results.get(finalIndex).getPopularity());
                        file.setPoster_path(tmdbResponse.results.get(finalIndex).getPoster_path());
                        file.setRelease_date(tmdbResponse.results.get(finalIndex).getRelease_date());

                        Log.i("sendGet: " , tmdbResponse.results.get(finalIndex).toString());
                    }
                } catch (JsonSyntaxException | NoSuchElementException elementException) {
                    elementException.printStackTrace();
                }
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
            }
            if(file.getTitle()==null){sendGet(file);}

        }
    }