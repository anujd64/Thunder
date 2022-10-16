package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.Constants.TMDB_API_KEY;
import static com.theflexproject.thunder.Constants.TMDB_GET_REQUEST_BASE_URL;
import static com.theflexproject.thunder.MainActivity.context;
import static com.theflexproject.thunder.utils.ShowUtils.EPNUM;
import static com.theflexproject.thunder.utils.ShowUtils.SEASON;
import static com.theflexproject.thunder.utils.ShowUtils.SHOW;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MoviesResponseFromTMDB;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.model.TVShowInfo.TVShowsResponseFromTMDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class SendGetRequestTMDB {


    static void sendGet(Movie movie) {
        String finalurl = "";
        String[] titleYear1 = MovieTitleExtractor.getTitleYear(movie.getFileName().replaceAll("Copy of " , ""));
        String[] titleYear2 = MovieTitleExtractor2.getTitle2(movie.getFileName().replaceAll("Copy of " , ""));
        String titleFromFile1 = "", titleFromFile2 = "", yearFromFile1 = "", yearFromFile2 = "";
        if (titleYear1 != null) {
            titleFromFile1 = titleYear1[0];
        } else {
            titleFromFile2 = titleYear2[0];
        }
        if (titleYear1 != null) {
            yearFromFile1 = titleYear1[0];
        } else {
            yearFromFile2 = titleYear2[0];
        }

        try {
            try {
                if (MovieTitleExtractor.getTitleYear(movie.getFileName()) != null) {
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
                MoviesResponseFromTMDB moviesResponseFromTMDB = gson.fromJson(response.toString() , MoviesResponseFromTMDB.class);

                if (moviesResponseFromTMDB.results.size() > 0) {
                    //New Changes
                    ArrayList<String> titlesFromTMDB = new ArrayList<>();
                    ArrayList<String> yearsFromTMDB = new ArrayList<>();
                    ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();
                    for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
                        titlesFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle());
                        yearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getRelease_date());
                        titlesandYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + moviesResponseFromTMDB.results.get(i).getRelease_date());
                    }

                    ExtractedResult result;
                    String finalTitle;
                    int finalIndex = 0;

                    Log.i("list of tmdb" , titlesFromTMDB.toString());
                    try {
                        if (titleFromFile1 != null) {
                            result = FuzzySearch.extractOne(titleFromFile1 , titlesFromTMDB);
                            Log.i(result.toString() , "result");
                            if (result.getScore() > 60) {
                                finalTitle = result.getString();
                                finalIndex = result.getIndex();
                                Log.i(result.toString() , finalTitle);
                            } else {
                                result = FuzzySearch.extractOne(titleFromFile1 + yearFromFile1 , titlesandYearsFromTMDB);
                                finalTitle = result.getString();
                                finalIndex = result.getIndex();
                                Log.i(result.toString() , finalTitle);
                            }

                        } else {
                            result = FuzzySearch.extractOne(titleFromFile2 + " " + yearFromFile2 , titlesFromTMDB);
                            if (result.getScore() > 60) {
                                finalTitle = result.getString();
                                finalIndex = result.getIndex();
                                Log.i(result.toString() , finalTitle);
                            } else {
                                result = FuzzySearch.extractOne(titleFromFile2 + yearFromFile2 , titlesandYearsFromTMDB);
                                finalTitle = result.getString();
                                finalIndex = result.getIndex();
                                Log.i(result.toString() , finalTitle);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    movie.setBackdrop_path(moviesResponseFromTMDB.results.get(finalIndex).getBackdrop_path());
                    movie.setTitle(moviesResponseFromTMDB.results.get(finalIndex).getTitle());
                    movie.setOriginal_language(moviesResponseFromTMDB.results.get(finalIndex).getOriginal_language());
                    movie.setOriginal_title(moviesResponseFromTMDB.results.get(finalIndex).getOriginal_title());
                    movie.setOverview(moviesResponseFromTMDB.results.get(finalIndex).getOverview());
                    movie.setPopularity(moviesResponseFromTMDB.results.get(finalIndex).getPopularity());
                    movie.setPoster_path(moviesResponseFromTMDB.results.get(finalIndex).getPoster_path());
                    movie.setRelease_date(moviesResponseFromTMDB.results.get(finalIndex).getRelease_date());
//                    file.setGenre_ids(tmdbResponse.results.get(0).getGenre_ids());
                    Log.i("sendGet: " , moviesResponseFromTMDB.results.get(finalIndex).toString());
                }

            } catch (MalformedURLException e) {
                Log.i("exe" , e.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("file with no tmdb info"+movie.toString());
        movie.setTitle(movie.getFileName());
        DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
        return;
//        return file;
    }


    static void sendGet2(Movie movie) throws IOException {
        String finalurl = "";
        String[] titleYear1 = MovieTitleExtractor.getTitleYear(movie.getFileName().replaceAll("Copy of " , ""));
        String[] titleYear2 = MovieTitleExtractor2.getTitle2(movie.getFileName().replaceAll("Copy of " , ""));

        String
                titleFromFile1 = "",
                titleFromFile2 = "",
                yearFromFile1 = ParseUtils.yearExtractor(movie.getFileName()).second,
                yearFromFile2 = ParseUtils.yearExtractor(movie.getFileName()).second;

        ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();

        if (titleYear1 != null) {
            titleFromFile1 = titleYear1[0];
        } else {
            titleFromFile2 = titleYear2[0];
        }
        try {
            if (MovieTitleExtractor.getTitleYear(movie.getFileName()) != null) {
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

        //Get results from tmdb by Name
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
        } catch (IOException | JsonSyntaxException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        MoviesResponseFromTMDB moviesResponseFromTMDB;

        moviesResponseFromTMDB = gson.fromJson(response.toString() , MoviesResponseFromTMDB.class);
        if(moviesResponseFromTMDB.results.size()!=0){
        for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
            String releaseDate = moviesResponseFromTMDB.results.get(i).getRelease_date();
            if (releaseDate != null)
                titlesandYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + releaseDate.split("-")[0]);
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

        } catch (JsonSyntaxException | NoSuchElementException elementException) {
            elementException.printStackTrace();
        }

            int movieId = moviesResponseFromTMDB.results.get(finalIndex).getId();

            //Get Movie by id
            //Get results from tmdb by Name
            StringBuilder responseById = new StringBuilder();
            try {
                String TMDB_MOVIE_BY_ID_URL =" https://api.themoviedb.org/3/movie/"+movieId+"?api_key="+TMDB_API_KEY+"&language=en-US";
                URL url = new URL(TMDB_MOVIE_BY_ID_URL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                Log.i("tmdb api response code" , String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    responseById = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseById.append(inputLine);
                    }
                    in.close();
                } else {
                    System.out.println("GET request did not work");
                }
            } catch (IOException | JsonSyntaxException e) {
                e.printStackTrace();
            }

            System.out.println("responseById"+responseById.toString());

            String fileName = movie.getFileName();
            String mimeType = movie.getMimeType();
            Date modifiedTime = movie.getModifiedTime();
            String size = movie.getSize();
            String urlString = movie.getUrlString();

            try {
                movie = gson.fromJson(responseById.toString() , Movie.class);
            }catch (NumberFormatException e){
                System.out.println("NumberFormatExce"+ e);
            }

            movie.setFileName(fileName);
            movie.setMimeType(mimeType);
            movie.setModifiedTime(modifiedTime);
            movie.setSize(size);
            movie.setUrlString(urlString);

            Log.i("Movie in sendGet2: " , movie.toString());

            DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
        }

        if (movie.getTitle() == null) {
            sendGet(movie);
        }

    }
    static void sendGetTVShow(String episodeFileName , Episode episode) {

        episodeFileName = episodeFileName.replaceAll("Copy of ","");
        Map<String,String> result = ShowUtils.parseShowName(episodeFileName);
        String finalShowName = null;
        String finalSeasonNumber = null;
        String finalEpisodeNumber = null;
        if(result!=null){
            finalShowName= result.get(SHOW);
            finalSeasonNumber = result.get(SEASON);
            finalEpisodeNumber = result.get(EPNUM);
        }
        String finalurl = "";
        int tvShowId =0;
        String finalShowSeasonNumber ="";

        //request TV show results by name to extract showId
        StringBuilder response = new StringBuilder();
        try {
            if (finalShowName != null) {
                finalurl = "https://api.themoviedb.org/3/search/tv?api_key=" + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query=" + URLEncoder.encode(finalShowName , "UTF-8");
            }

            URL url = new URL(finalurl);
            Log.i("Show title" , finalurl);
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
            TVShowsResponseFromTMDB tvShowsResponseFromTMDB = gson.fromJson(response.toString() , TVShowsResponseFromTMDB.class);

            Log.i("tvShowsResponseFromTMDB" , tvShowsResponseFromTMDB.toString());
            if(tvShowsResponseFromTMDB.results.size()>0){
                tvShowId = tvShowsResponseFromTMDB.results.get(0).getId();
            }
            /** better method for selection of tv show */


        }catch (IOException e){}

        //request for tv show by id and
        TVShow testTVShow = DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().find(tvShowId);
        if(testTVShow==null && tvShowId!=0){
            finalurl ="https://api.themoviedb.org/3/tv/"+tvShowId+"?api_key=" + TMDB_API_KEY + "&language=en-US";
            StringBuilder responseTvShowDetails = new StringBuilder();
            try {
                URL url = new URL(finalurl);
                Log.i("Show title" , finalurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                Log.i("tmdb api response" , String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    responseTvShowDetails = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseTvShowDetails.append(inputLine);
                    }
                    in.close();
                } else {
                    System.out.println("GET request did not work");
                }
                Log.i("tvShowById" , responseTvShowDetails.toString());

                Gson gson = new Gson();
                TVShow tvShow = gson.fromJson(responseTvShowDetails.toString() , TVShow.class);

                Log.i("tvShow" , tvShow.toString());

               DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().insert(tvShow);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        Request for season details
        StringBuilder responseSeasonDetails = new StringBuilder();
        try {
            if (finalSeasonNumber != null) {
                finalurl ="https://api.themoviedb.org/3/tv/"+tvShowId+"/season/"+finalSeasonNumber+"?api_key=" + TMDB_API_KEY + "&language=en-US";
                URL url = new URL(finalurl);
                Log.i("Show title" , finalurl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                int responseCode = con.getResponseCode();
                Log.i("tmdb api response" , String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_OK) { // success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            con.getInputStream()));
                    String inputLine;
                    responseSeasonDetails = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        responseSeasonDetails.append(inputLine);
                    }
                    in.close();
                } else {
                    System.out.println("GET request did not work");
                }

                Gson gson = new Gson();

                TVShowSeasonDetails tvShowSeasonDetails = gson.fromJson(responseSeasonDetails.toString() , TVShowSeasonDetails.class);


                if(tvShowSeasonDetails!=null){
                    if(tvShowId!=0) {tvShowSeasonDetails.setShow_id(tvShowId);}


                    Log.i("tvShowSeasonDetails" , tvShowSeasonDetails.toString());
                    TVShowSeasonDetails test = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
                    if(test== null) DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);

                    System.out.println("Episode Number" +finalEpisodeNumber);
                    for (Episode e :tvShowSeasonDetails.getEpisodes() ) {
                        assert finalEpisodeNumber != null;
                        if(e.getEpisode_number()== Integer.parseInt(finalEpisodeNumber) ){
                            System.out.println("Episode Number from title of the file " +Integer.parseInt(finalEpisodeNumber)+" Ep from the season details"+e.getEpisode_number());

                             e.setFileName(episode.getFileName());
                             e.setMimeType(episode.getMimeType());
                             e.setModifiedTime(episode.getModifiedTime());;
                            e.setSize(episode.getSize());
                            e.setUrlString(episode.getUrlString());
                            e.setSeason_id(tvShowSeasonDetails.getId());
                            //why null here?
                            System.out.println("ep in season in seasondetails"+episode.toString());

//                    episode = gson.fromJson(e.toString(),Episode.class);

//                            episode = e;
//                            episode.setSeason_id(tvShowSeasonDetails.getId());
//                            episode.setFileName(fileName);
//                            episode.setMimeType(mimeType);
//                            episode.setModifiedTime(modifiedTime);
//                            episode.setSize(size);
//                            episode.setUrlString(urlString);

                            System.out.println("ep in season in seasondetails after adding everything"+e.toString());
                            Episode testEp = DatabaseClient.getInstance(context).getAppDatabase().episodeDao().findByFileName(e.getFileName());
                            if(testEp == null){
                                DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(e);
                            }
                        }
                    }
                }

            }

        } catch (IOException e) {
            System.out.println(e);
        }

    }

}