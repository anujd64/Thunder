package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.Constants.FANART_IMAGE_BASE_URL;
import static com.theflexproject.thunder.Constants.TMDB_API_KEY;
import static com.theflexproject.thunder.Constants.TMDB_BASE_URL;
import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;
import static com.theflexproject.thunder.Constants.getFanartApiKey;
import static com.theflexproject.thunder.MainActivity.context;
import static com.theflexproject.thunder.utils.PlexMovieExtractor.getTMDBId;
import static com.theflexproject.thunder.utils.ShowUtils.EPNUM;
import static com.theflexproject.thunder.utils.ShowUtils.SEASON;
import static com.theflexproject.thunder.utils.ShowUtils.SHOW;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.ExternalIds;
import com.theflexproject.thunder.model.FanArt.FanArtMovieResponseModel;
import com.theflexproject.thunder.model.FanArt.FanArtTvResponseModel;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MoviesResponseFromTMDB;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.Result;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.model.TVShowInfo.TVShowsResponseFromTMDB;
import com.theflexproject.thunder.model.tmdbImages.Logo;
import com.theflexproject.thunder.model.tmdbImages.TMDBImagesResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import me.xdrop.fuzzywuzzy.model.ExtractedResult;

public class SendGetRequestTMDB {


    static void sendGet2(Movie movie) {
        String fileName = movie.getFileName();

        //Match if the fileName contains tmdb id
        String movieIdFromPlexExtractor = (getTMDBId(fileName));

        if (movieIdFromPlexExtractor != null) {
            System.out.println("movieId from plex extractor");
            getMovieById(Long.parseLong(movieIdFromPlexExtractor) , movie);
        }


        //Match if the fileName follows scene naming pattern
        String[] titleYear;
        titleYear = SceneMovieTitleExtractor.getTitleYear(fileName.replaceAll("Copy of " , ""));

        //Match if the fileName follows other loosely followed naming conventions
        if (titleYear == null) {
            titleYear = MovieTitleExtractor2.getTitle2(fileName.replaceAll("Copy of " , ""));
        }

        //Extract title and year
        String titleExtracted = "", yearExtracted = "0";
        if (titleYear != null) {
            if (titleYear[0] != null) {
                titleExtracted = titleYear[0];
                if (titleYear[1] != null && titleYear[1].length() > 3) {
                    yearExtracted = titleYear[1];
                } else {
                    yearExtracted = ParseUtils.yearExtractor(fileName).second;
                }
            }
        }

        //Sends tmdb request to search on tmdb and receive multiple responses
        String response = searchMovieOnTmdbByName(titleExtracted , yearExtracted);

        Gson gson = new Gson();
        MoviesResponseFromTMDB moviesResponseFromTMDB = null;
        try {
            moviesResponseFromTMDB = gson.fromJson(response , MoviesResponseFromTMDB.class);
        } catch (Exception e) {
            System.out.println(e);
        }

        ArrayList<String> titlesAndYearsFromTMDB = new ArrayList<>();

        if (moviesResponseFromTMDB != null && moviesResponseFromTMDB.results.size() != 0) {
            for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
                String releaseDate = moviesResponseFromTMDB.results.get(i).getRelease_date();
                if (releaseDate != null)
                    if (releaseDate.length() == 4) {
                        titlesAndYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + releaseDate);
                    } else {
                        titlesAndYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + releaseDate.split("-")[0]);
                    }
            }
            int finalIndex = 0;
            if (!titleExtracted.equals("") && !yearExtracted.equals("0")) {
                finalIndex = findIndexOfClosestMatch(titleExtracted + " " + yearExtracted , titlesAndYearsFromTMDB);
            }
            int movieId = moviesResponseFromTMDB.results.get(finalIndex).getId();
            getMovieById(movieId , movie);
        }

        else if (movie.getTitle() == null) {
            System.out.println("file with no tmdb info" + movie);
            DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
        }
    }

    private static String searchMovieOnTmdbByName(String titleExtracted , String yearExtracted) {
        StringBuilder response = new StringBuilder();
        try {
            String finalUrl = TMDB_BASE_URL + "search/movie?api_key=" + TMDB_API_KEY + "&year=" + yearExtracted + "&language=en-US&page=1&include_adult=false&query=" + URLEncoder.encode(titleExtracted , "UTF-8");
            URL url = new URL(finalUrl);
            System.out.println("TMDB GET REQUEST URL INSIDE searchMovieOnTmdbByName " + finalUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            System.out.println("TMDB RESPONSE CODE" + responseCode);
            if (responseCode == HttpURLConnection.HTTP_OK) {
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
        return response.toString();
    }

    private static void getMovieById(long id , MyMedia myMedia) {
        //Get Movie by id
        //Get results from tmdb by Name
        Movie movie = (Movie) myMedia;

        DatabaseClient.getInstance(context).getAppDatabase().movieDao().delete(movie);

        StringBuilder responseById = new StringBuilder();
        try {
            String TMDB_MOVIE_BY_ID_URL = TMDB_BASE_URL + "movie/" + id + "?api_key=" + TMDB_API_KEY + "&language=en-US";
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

        System.out.println("responseById" + responseById.toString());

        String fileName = movie.getFileName();
        String mimeType = movie.getMimeType();
        Date modifiedTime = movie.getModifiedTime();
        String size = movie.getSize();
        String urlString = movie.getUrlString();
        String gd_id = movie.getGd_id();
        int index_id = movie.getIndex_id();
        String logo_path = getLogo2(id , false);
        if (logo_path.equals("")) logo_path = getLogo(id , false);

        System.out.println("logo_path in sendget2" + logo_path);

        try {
            Gson gson = new Gson();
            movie = gson.fromJson(responseById.toString() , Movie.class);
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException" + e);
        }


        movie.setFileName(fileName);
        movie.setMimeType(mimeType);
        movie.setModifiedTime(modifiedTime);
        movie.setSize(size);
        movie.setUrlString(urlString);
        movie.setGd_id(gd_id);
        movie.setIndex_id(index_id);


        movie.setLogo_path(logo_path);

        System.out.println("movie after adding everything" + movie);

        if (DatabaseClient.getInstance(context).getAppDatabase().movieDao().getByFileName(movie.getGd_id()) == null) {
            DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
        }

    }

    static void sendGetTVShow(Episode episode) {

        String episodeFileName = episode.getFileName().replaceAll("Copy of " , "");
        Map<String, String> result = ShowUtils.parseShowName(episodeFileName);
        String finalShowName = null;
        String finalSeasonNumber = null;
        String finalEpisodeNumber = null;
        if (result != null) {
            finalShowName = result.get(SHOW);
            finalSeasonNumber = result.get(SEASON);
            finalEpisodeNumber = result.get(EPNUM);
        }
        String finalurl = "";
        long tvShowId = 0;

        boolean failed = false;
        try {
            String string = getTMDBId(episode.getUrlString());
            tvShowId = Long.parseLong(string);
            System.out.println("parsed show id " + tvShowId);

            TVShow tvShow = new TVShow();
            getTVById(tvShowId , tvShow);

            //Request for season details
            if (tvShowId != 0)
                getTVSeasonById2(tvShowId , finalSeasonNumber , finalEpisodeNumber , episode);


        } catch (NumberFormatException | NullPointerException e) {
            failed = true;
            System.out.println("parse failed get tv" + e.getMessage());
        }

        if (finalShowName != null) {
            TVShow test = DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().findByName(finalShowName);
            if (test != null && test.getId() != 0) {
                getTVSeasonById2(test.getId() , finalSeasonNumber , finalEpisodeNumber , episode);
            }
            if (test == null && failed) {
                //request TV show results by name to extract showId
                StringBuilder response = new StringBuilder();
                try {
                    finalurl = TMDB_BASE_URL + "search/tv?api_key=" + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query=" + URLEncoder.encode(finalShowName , "UTF-8");

                    System.out.println("finalUrl in sendGetTVShow" + finalurl);

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

                    /*better method for selection of tv show */
                    if (tvShowsResponseFromTMDB.results.size() > 0) {
                        ArrayList<String> tvTitlesFromTMDB = new ArrayList<>();

                        for (int i = 0; i < tvShowsResponseFromTMDB.getResults().size(); i++) {
                            Result tv = tvShowsResponseFromTMDB.getResults().get(i);
                            tvTitlesFromTMDB.add(tv.getName());
                        }
                        ExtractedResult matchedTvTitle;
                        int finalIndex = 0;
                        matchedTvTitle = FuzzySearch.extractOne(finalShowName , tvTitlesFromTMDB);
                        if (matchedTvTitle.getScore() == 100) {
                            finalIndex = matchedTvTitle.getIndex();
                        } else if (matchedTvTitle.getScore() > 70) {
                            finalIndex = matchedTvTitle.getIndex();
                        }
                        tvShowId = tvShowsResponseFromTMDB.results.get(finalIndex).getId();

                    }

                    if (tvShowId != 0) {
                        //Request for tv show by id and
                        TVShow tvShow = new TVShow();
                        getTVById(tvShowId , tvShow);
                        //Request for season details
                        getTVSeasonById2(tvShowId , finalSeasonNumber , finalEpisodeNumber , episode);
                    }

                } catch (IOException e) {
                    System.out.println(e.toString());
                }

            }


        }


    }

    private static void getTVById(long tvShowId , MyMedia myMedia) {
        TVShow tvShow = (TVShow) myMedia;

        long oldTVShowID = tvShow.getId();
        DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().delete(tvShow);
        DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().deleteByShowId(oldTVShowID);


        TVShow test = DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().find(tvShowId);


        if (test == null && tvShowId != 0) {
            String finalurl = TMDB_BASE_URL + "tv/" + tvShowId + "?api_key=" + TMDB_API_KEY + "&language=en-US";
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

//                long tvdb_id = getTVDBId(tvShowId);


                Gson gson = new Gson();
                tvShow = gson.fromJson(responseTvShowDetails.toString() , TVShow.class);

//                String logoPath = getLogo(tvdb_id,true);
                String logoPath = getLogo2(tvShowId , true);

                if (logoPath.equals("")) {
                    tvdb_id = getTVDBId(tvShowId);
                    logoPath = getLogo(tvdb_id , true);
                }

                tvShow.setLogo_path(logoPath);

                Log.i("tvShow" , tvShow.toString());

                DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().insert(tvShow);

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        //get all the episodes to be update with new info
        List<Episode> episodeList = DatabaseClient.getInstance(context).getAppDatabase().episodeDao().getFromThisShow(oldTVShowID);
        for (Episode e : episodeList) {
            System.out.println("Inside for loop");

            String episodeFileName = e.getFileName().replaceAll("Copy of " , "");
            Map<String, String> result = ShowUtils.parseShowName(episodeFileName);
            String finalShowName = null;
            String finalSeasonNumber = null;
            String finalEpisodeNumber = null;
            if (result != null) {
                finalShowName = result.get(SHOW);
                finalSeasonNumber = result.get(SEASON);
                finalEpisodeNumber = result.get(EPNUM);
            }
            getTVSeasonById2(tvShowId , finalSeasonNumber , finalEpisodeNumber , e);
        }

    }

    private static void getTVSeasonById2(long tvShowId , String finalSeasonNumber , String finalEpisodeNumber , Episode episode) {
        try {
            if (finalSeasonNumber != null) {
//                TVShowSeasonDetails tvShowSeasonDetails =null;
                TVShowSeasonDetails tvShowSeasonDetails = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().findByShowIdAndSeasonNumber(tvShowId , finalSeasonNumber);


                if (tvShowSeasonDetails == null) {
                    StringBuilder responseSeasonDetails = new StringBuilder();


                    String finalUrl = TMDB_BASE_URL + "tv/" + tvShowId + "/season/" + finalSeasonNumber + "?api_key=" + TMDB_API_KEY + "&language=en-US";
                    System.out.println("Season Number " + finalSeasonNumber + "finalurl " + finalUrl);

                    URL url = new URL(finalUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    int responseCode = con.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) { // success

                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                        String inputLine;
                        responseSeasonDetails = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            responseSeasonDetails.append(inputLine);
                        }
                        in.close();
                    } else {
                        System.out.println("Season details 2 GET request did not work");
                    }

                    Gson gson = new Gson();

                    System.out.println("the received string season"+ String.valueOf(responseSeasonDetails));




                    try{

                        String s = responseSeasonDetails.toString();
                        Pattern pattern = Pattern.compile(Matcher.quoteReplacement("\\\""));
                        Matcher matcher = pattern.matcher(s);
                        s = matcher.replaceAll("");

                        System.out.println("changed string"+s);

                        tvShowSeasonDetails = gson.fromJson(s, TVShowSeasonDetails.class);

//
//                        JSONObject json = (JSONObject) new JSONParser().parse(responseSeasonDetails.toString().replaceAll(Matcher.quoteReplacement("\""),"").replaceAll(Matcher.quoteReplacement("\n"),""));
//                        tvShowSeasonDetails = gson.fromJson(json.toJSONString(), TVShowSeasonDetails.class);
//                        tvShowSeasonDetails = gson.fromJson(String.valueOf(responseSeasonDetails), TVShowSeasonDetails.class);
//                        tvShowSeasonDetails = gson.fromJson(String.valueOf(responseSeasonDetails).replace("\n","\\\n").replace("\"","\\\"") , TVShowSeasonDetails.class);
                    }catch (Exception e){
                        System.out.println("MalformedURLException");
                    }


                    if (tvShowSeasonDetails != null && tvShowSeasonDetails.getEpisodes() != null) {

                        if (tvShowId != 0) {
                            tvShowSeasonDetails.setShow_id(tvShowId);
                        }

                        Log.i("tvShowSeasonDetails" , tvShowSeasonDetails.toString());
                        System.out.println("tvShowSeasonDetails in getTVSeasonById2" + tvShowSeasonDetails);
//                        TVShowSeasonDetails test = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
//                        if (test == null)

                        for (Episode e : tvShowSeasonDetails.getEpisodes()) {
                            e.setModifiedTime(new Date());
                        }
                        DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);

                        System.out.println("Episode Number" + finalEpisodeNumber);


                    }


                }


                try {
                    System.out.println("getTVSeasonById2 tvShowSeasonDetails " + tvShowSeasonDetails.toString());
                    for (Episode e: tvShowSeasonDetails.getEpisodes()) {
                        if(Integer.parseInt(finalEpisodeNumber)== e.getEpisode_number()){

                                System.out.println("Episode before inserting in db found in tvseason details" + e.toString());
                                e.setFileName(episode.getFileName());
                                e.setMimeType(episode.getMimeType());
                                e.setModifiedTime(episode.getModifiedTime());
                                e.setSize(episode.getSize());
                                e.setUrlString(episode.getUrlString());
                                e.setGd_id(episode.getGd_id());
                                e.setIndex_id(episode.getIndex_id());

                                e.setSeason_id(tvShowSeasonDetails.getId());
                                e.setShow_id(tvShowId);

                                DatabaseClient.getInstance(context).getAppDatabase().episodeDao().deleteByLink(e.getGd_id());

                                DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(e);
                                System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());

                        }
                        else {
                            episode.setShow_id(tvShowId);
                            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(episode);
                        }

                    }

//                    Episode e = tvShowSeasonDetails.getEpisodes().stream()
//                            .filter(episode1 -> (Integer.parseInt(finalEpisodeNumber) == episode1.getEpisode_number()))
//                            .findAny()
//                            .orElse(null);



                } catch (NullPointerException e) {

                    System.out.println("caught exception in getTVSeasonById2.2");
                }

            }

        } catch (IOException e) {
            System.out.println("caught exception in getTVSeasonById2" + e);
        }

    }

    public static void tmdbGetByID(MyMedia myMedia , long id , boolean isShow) {
        try {
            if (isShow) {
                getTVById(id , myMedia);
            }

            if (!isShow) {
                getMovieById(id , myMedia);
            }

        } catch (NullPointerException e) {
            System.out.println(e);
        }

    }

    private static int findIndexOfClosestMatch(String s , ArrayList<String> titlesAndYearsFromTMDB) {
        try {
            ExtractedResult result = FuzzySearch.extractOne(s , titlesAndYearsFromTMDB);
            System.out.println("findIndexOfClosestMatch FuzzySearch RESULT" + result.toString());
            System.out.println("final Title chosen by findIndexOfClosestMatch" + result.getString());
            return result.getIndex();
        } catch (JsonSyntaxException | NoSuchElementException elementException) {
            elementException.printStackTrace();
        }
        return 0;
    }

    static String urlLogo = "";

    private static String getLogo2(Long id , boolean isShow) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    urlLogo = "";

                    String typeOfMedia = "movie";
                    if (isShow) typeOfMedia = "tv";


                    URL url = new URL(TMDB_BASE_URL + typeOfMedia + "/" + id + "/images?api_key=" + TMDB_API_KEY);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int code = conn.getResponseCode();
                    System.out.println(("HTTP CODE" + code));


                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = br.read()) >= 0; )
                        sb.append((char) c);
                    br.close();

                    Gson om = new Gson();

                    TMDBImagesResponse tmdbImagesResponse;
                    tmdbImagesResponse = om.fromJson(sb.toString() , TMDBImagesResponse.class);

                    ArrayList<Logo> logoArrayList = tmdbImagesResponse.getLogos();
//                    Collections.sort(logoArrayList , new imageVotesComparator());
//                    ;

                    System.out.println("TMDBImagesResponse" + tmdbImagesResponse);
                    if (tmdbImagesResponse.getLogos() != null) {
                        for (int i = 0; tmdbImagesResponse.getLogos().size() > 0 && i < tmdbImagesResponse.getLogos().size(); i++) {
                            if (tmdbImagesResponse.getLogos().get(i).getIso_639_1() != null
                                    &&
                                    (tmdbImagesResponse.getLogos().get(i).getIso_639_1().equals("en")
//                                            || tmdbImagesResponse.getLogos().get(i).getIso_639_1().equals("hi")
                                    )
                                    &&
                                    !tmdbImagesResponse.getLogos().get(i).getFile_path().contains(".svg")) {
                                urlLogo = TMDB_IMAGE_BASE_URL + tmdbImagesResponse.getLogos().get(i).getFile_path().toString();
                                break;
                            }
                        }
                    }


                    System.out.println("inside getLogo" + urlLogo);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return urlLogo;
    }

    private static String getLogo(Long id , boolean isShow) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    urlLogo = "";

                    String typeOfMedia = "movies";

                    if (isShow) typeOfMedia = "tv";

                    URL url = new URL(FANART_IMAGE_BASE_URL + typeOfMedia + "/" + id + "?api_key=" + getFanartApiKey());

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    int code = conn.getResponseCode();
                    System.out.println(("HTTP CODE" + code));


                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                    StringBuilder sb = new StringBuilder();
                    for (int c; (c = br.read()) >= 0; )
                        sb.append((char) c);
                    br.close();

                    Gson om = new Gson();

                    FanArtMovieResponseModel root;
                    FanArtTvResponseModel tvResponseModel;
                    if (isShow) {
                        tvResponseModel = om.fromJson(sb.toString() , FanArtTvResponseModel.class);
                        System.out.println("FanArtMovieResponse" + tvResponseModel);
                        if (tvResponseModel != null && tvResponseModel.getHdtvlogo() != null) {
                            for (int i = 0; i < tvResponseModel.getHdtvlogo().size(); i++) {
                                if (tvResponseModel.getHdtvlogo().get(i).getLang().equals("en") || tvResponseModel.getHdtvlogo().get(i).getLang().equals("hi")) {
                                    urlLogo = tvResponseModel.getHdtvlogo().get(i).getUrl().toString();
                                    break;
                                }
                            }
                        }
                        if (urlLogo.equals("") && tvResponseModel != null && tvResponseModel.getClearlogo() != null) {
                            for (int i = 0; i < tvResponseModel.getClearlogo().size(); i++) {
                                if (tvResponseModel.getClearlogo().get(i).getLang().equals("en") || tvResponseModel.getClearlogo().get(i).getLang().equals("hi")) {
                                    urlLogo = tvResponseModel.getClearlogo().get(i).getUrl();
                                    break;
                                }
                            }
                        }
                    } else {
                        root = om.fromJson(sb.toString() , FanArtMovieResponseModel.class);
                        System.out.println("FanArtMovieResponse" + root);
                        if (root != null && root.getHdmovielogo() != null) {
                            for (int i = 0; i < root.getHdmovielogo().size(); i++) {
                                if (root.getHdmovielogo().get(i).getLang().equals("en") || root.getHdmovielogo().get(i).getLang().equals("hi")) {
                                    urlLogo = root.getHdmovielogo().get(i).getUrl().toString();
                                    break;
                                }
                            }
                        }
                        if (urlLogo.equals("") && root != null && root.getMovielogo() != null) {
                            for (int i = 0; i < root.getMovielogo().size(); i++) {
                                if (root.getMovielogo().get(i).getLang().equals("en") || root.getMovielogo().get(i).getLang().equals("hi")) {
                                    urlLogo = root.getMovielogo().get(i).getUrl();
                                    break;
                                }
                            }
                        }
                    }

                    System.out.println("inside getLogo" + urlLogo);
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return urlLogo;
    }

    static long tvdb_id = 0;

    private static long getTVDBId(long tvShowId) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                tvdb_id = 0;
                String finalurl = TMDB_BASE_URL + "tv/" + tvShowId + "/external_ids?api_key=" + TMDB_API_KEY + "&language=en-US";
                StringBuilder responseExternalIds = new StringBuilder();
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
                        responseExternalIds = new StringBuilder();
                        while ((inputLine = in.readLine()) != null) {
                            responseExternalIds.append(inputLine);
                        }
                        in.close();
                    } else {
                        System.out.println("GET request did not work");
                    }
                    Log.i("tvShowById" , responseExternalIds.toString());


                    Gson gson = new Gson();
                    ExternalIds externalIds = gson.fromJson(responseExternalIds.toString() , ExternalIds.class);

                    tvdb_id = externalIds.getTvdb_id();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return tvdb_id;
    }
}


//    static void sendGet(Movie movie) {
//        String finalurl = "";
//        String[] titleYear1 = SceneMovieTitleExtractor.getTitleYear(movie.getFileName().replaceAll("Copy of " , ""));
//        String[] titleYear2 = MovieTitleExtractor2.getTitle2(movie.getFileName().replaceAll("Copy of " , ""));
//        String titleFromFile1 = "", titleFromFile2 = "", yearFromFile1 = "", yearFromFile2 = "";
//        if (titleYear1 != null) {
//            titleFromFile1 = titleYear1[0];
//        } else {
//            titleFromFile2 = titleYear2[0];
//        }
//        if (titleYear1 != null) {
//            yearFromFile1 = titleYear1[0];
//        } else {
//            yearFromFile2 = titleYear2[0];
//        }
//
//        try {
//            try {
//                if (SceneMovieTitleExtractor.getTitleYear(movie.getFileName()) != null) {
//                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
//                            + URLEncoder.encode(titleFromFile1 , "UTF-8");
//                } else {
//                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
//                            + URLEncoder.encode(titleFromFile2 , "UTF-8");
//                }
//            } catch (Exception e) {
//                Log.i("exc" , e.toString());
//                e.printStackTrace();
//            }
//            StringBuilder response = new StringBuilder();
//            try {
//                URL url = new URL(finalurl);
//                Log.i("movie title" , finalurl);
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                int responseCode = con.getResponseCode();
//                Log.i("tmdb api response" , String.valueOf(responseCode));
//                if (responseCode == HttpURLConnection.HTTP_OK) { // success
//                    BufferedReader in = new BufferedReader(new InputStreamReader(
//                            con.getInputStream()));
//                    String inputLine;
//                    response = new StringBuilder();
//                    while ((inputLine = in.readLine()) != null) {
//                        response.append(inputLine);
//                    }
//                    in.close();
//                } else {
//                    System.out.println("GET request did not work");
//                }
//                Gson gson = new Gson();
//                MoviesResponseFromTMDB moviesResponseFromTMDB = gson.fromJson(response.toString() , MoviesResponseFromTMDB.class);
//
//                if (moviesResponseFromTMDB.results.size() > 0) {
//                    //New Changes
//                    ArrayList<String> titlesFromTMDB = new ArrayList<>();
//                    ArrayList<String> yearsFromTMDB = new ArrayList<>();
//                    ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();
//                    for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
//                        titlesFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle());
//                        yearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getRelease_date());
//                        titlesandYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + moviesResponseFromTMDB.results.get(i).getRelease_date());
//                    }
//
//                    ExtractedResult result;
//                    String finalTitle;
//                    int finalIndex = 0;
//
//                    Log.i("list of tmdb" , titlesFromTMDB.toString());
//                    try {
//                        if (titleFromFile1 != null) {
//                            result = FuzzySearch.extractOne(titleFromFile1 , titlesFromTMDB);
//                            Log.i(result.toString() , "result");
//                            if (result.getScore() > 60) {
//                                finalTitle = result.getString();
//                                finalIndex = result.getIndex();
//                                Log.i(result.toString() , finalTitle);
//                            } else {
//                                result = FuzzySearch.extractOne(titleFromFile1 + yearFromFile1 , titlesandYearsFromTMDB);
//                                finalTitle = result.getString();
//                                finalIndex = result.getIndex();
//                                Log.i(result.toString() , finalTitle);
//                            }
//
//                        } else {
//                            result = FuzzySearch.extractOne(titleFromFile2 + " " + yearFromFile2 , titlesFromTMDB);
//                            if (result.getScore() > 60) {
//                                finalTitle = result.getString();
//                                finalIndex = result.getIndex();
//                                Log.i(result.toString() , finalTitle);
//                            } else {
//                                result = FuzzySearch.extractOne(titleFromFile2 + yearFromFile2 , titlesandYearsFromTMDB);
//                                finalTitle = result.getString();
//                                finalIndex = result.getIndex();
//                                Log.i(result.toString() , finalTitle);
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    String fileName = movie.getFileName();
//                    String mimeType = movie.getMimeType();
//                    Date modifiedTime = movie.getModifiedTime();
//                    String size = movie.getSize();
//                    String urlString = movie.getUrlString();
//
//                    movie = moviesResponseFromTMDB.results.get(finalIndex);
//
//                    movie.setFileName(fileName);
//                    movie.setMimeType(mimeType);
//                    movie.setModifiedTime(modifiedTime);
//                    movie.setSize(size);
//                    movie.setUrlString(urlString);
//
////                    file.setGenre_ids(tmdbResponse.results.get(0).getGenre_ids());
//                    Log.i("sendGet: " , moviesResponseFromTMDB.results.get(finalIndex).toString());
//                }
//
//            } catch (MalformedURLException e) {
//                Log.i("exe" , e.toString());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//        System.out.println("file with no tmdb info" + movie);
//
//        movie.setTitle(movie.getFileName());
//
//
//        if (DatabaseClient.getInstance(context).getAppDatabase().movieDao().getByFileName(movie.getFileName()) == null) {
//            DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
//
//        }
//    }


//    private static void getSeasonInfo(int tvShowId, String seasonNumber) {
//
//
//        //Request for season details
//        StringBuilder responseSeasonDetails = new StringBuilder();
//        try {
//            if (tvShowSeasonId != 0) {
//                finalurl ="https://api.themoviedb.org/3/tv/"+tvShowId+"/season/"+finalSeasonNumber+"?api_key=" + TMDB_API_KEY + "&language=en-US";
//                URL url = new URL(finalurl);
//                Log.i("Show title" , finalurl);
//                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("GET");
//                int responseCode = con.getResponseCode();
//                Log.i("tmdb api response" , String.valueOf(responseCode));
//                if (responseCode == HttpURLConnection.HTTP_OK) { // success
//                    BufferedReader in = new BufferedReader(new InputStreamReader(
//                            con.getInputStream()));
//                    String inputLine;
//                    responseSeasonDetails = new StringBuilder();
//                    while ((inputLine = in.readLine()) != null) {
//                        responseSeasonDetails.append(inputLine);
//                    }
//                    in.close();
//                } else {
//                    System.out.println("GET request did not work");
//                }
//
//                Gson gson = new Gson();
//
//                TVShowSeasonDetails tvShowSeasonDetails = gson.fromJson(responseSeasonDetails.toString() , TVShowSeasonDetails.class);
//
//
//
//
//            }
//
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//
//
//
//
//            TVShowSeasonDetails test = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
//            if(test== null) DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
//
//            System.out.println("Episode Number" +finalEpisodeNumber);
//            for (Episode e :tvShowSeasonDetails.getEpisodes() ) {
//                assert finalEpisodeNumber != null;
//                if(e.getEpisode_number()== Integer.parseInt(finalEpisodeNumber) ){
//                    System.out.println("Episode Number from title of the file " +Integer.parseInt(finalEpisodeNumber)+" Ep from the season details"+e.getEpisode_number());
//
//                    e.setFileName(episode.getFileName());
//                    e.setMimeType(episode.getMimeType());
//                    e.setModifiedTime(episode.getModifiedTime());;
//                    e.setSize(episode.getSize());
//                    e.setUrlString(episode.getUrlString());
//                    e.setSeason_id(tvShowSeasonDetails.getId());
//                    //why null here?
//                    System.out.println("ep in season in seasondetails"+episode.toString());
//
////                    episode = gson.fromJson(e.toString(),Episode.class);
//
////                            episode = e;
////                            episode.setSeason_id(tvShowSeasonDetails.getId());
////                            episode.setFileName(fileName);
////                            episode.setMimeType(mimeType);
////                            episode.setModifiedTime(modifiedTime);
////                            episode.setSize(size);
////                            episode.setUrlString(urlString);
//
//                    System.out.println("ep in season in seasondetails after adding everything"+e.toString());
//                    Episode testEp = DatabaseClient.getInstance(context).getAppDatabase().episodeDao().findByFileName(e.getFileName());
//                    if(testEp == null){
//                        DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(e);
//                    }
//                }
//            }
//        }


//    private static void getTVSeasonById(long tvShowId , String finalSeasonNumber , String finalEpisodeNumber , Episode episode) {
//        StringBuilder responseSeasonDetails = new StringBuilder();
//        try {
//            if (finalSeasonNumber != null) {
//                TVShowSeasonDetails tvShowSeasonDetails = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().findByShowIdAndSeasonNumber(tvShowId , finalSeasonNumber);
//                if (tvShowSeasonDetails == null) {
//                    System.out.println("Season Number " + finalSeasonNumber);
//
//                    String finalUrl = "https://api.themoviedb.org/3/tv/" + tvShowId + "/season/" + finalSeasonNumber + "?api_key=" + TMDB_API_KEY + "&language=en-US";
//                    URL url = new URL(finalUrl);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("GET");
//                    int responseCode = con.getResponseCode();
//                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
//                        BufferedReader in = new BufferedReader(new InputStreamReader(
//                                con.getInputStream()));
//                        String inputLine;
//                        responseSeasonDetails = new StringBuilder();
//                        while ((inputLine = in.readLine()) != null) {
//                            responseSeasonDetails.append(inputLine);
//                        }
//                        in.close();
//                    } else {
//                        System.out.println("GET request did not work");
//                    }
//
//                    Gson gson = new Gson();
//
//                    tvShowSeasonDetails = gson.fromJson(responseSeasonDetails.toString() , TVShowSeasonDetails.class);
//
//
//                    if (tvShowSeasonDetails != null) {
//
//                        if (tvShowId != 0) {
//                            tvShowSeasonDetails.setShow_id(tvShowId);
//                        }
//
//                    System.out.println("tvShowSeasonDetails " + tvShowSeasonDetails);
//                    TVShowSeasonDetails test = DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
//                    if (test == null)
//                        DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
//
//                    }
//
//
//                }
//
//                System.out.println("Episode Number" + finalEpisodeNumber);
//
//                try {
//
//                    Episode e = tvShowSeasonDetails.getEpisodes().stream()
//                        .filter(episode1 -> (Integer.parseInt(finalEpisodeNumber)==episode1.getEpisode_number()))
//                        .findAny()
//                        .orElse(null);
//
//
//                    System.out.println("Episode before inserting in db found in tvseason details" +e.toString());
//                    if(e!=null) {
//                        e.setFileName(episode.getFileName());
//                        e.setMimeType(episode.getMimeType());
//                        e.setModifiedTime(episode.getModifiedTime());
//                        e.setSize(episode.getSize());
//                        e.setUrlString(episode.getUrlString());
//                        e.setGd_id(episode.getGd_id());
//                        e.setIndex_id(episode.getIndex_id());
//
//                        e.setSeason_id(tvShowSeasonDetails.getId());
//                        e.setShow_id(tvShowId);
//
////                            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().deleteByLink(e.getUrlString());
//
//                        DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(e);
//                        System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());
//                    }
//
//                }catch (NullPointerException e){
//
//                }
//
//
////                    for (Episode e : tvShowSeasonDetails.getEpisodes()) {
////                        if (e.getEpisode_number() == Integer.parseInt(finalEpisodeNumber)) {
////                            System.out.println("Episode Number from title of the file " + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details " + e.getEpisode_number());
////
////                            e.setFileName(episode.getFileName());
////                            e.setMimeType(episode.getMimeType());
////                            e.setModifiedTime(episode.getModifiedTime());
////                            e.setSize(episode.getSize());
////                            e.setUrlString(episode.getUrlString());
////                            e.setGd_id(episode.getGd_id());
////                            e.setIndex_id(episode.getIndex_id());
////
////                            e.setSeason_id(tvShowSeasonDetails.getId());
////                            e.setShow_id(tvShowId);
////
//////                            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().deleteByLink(e.getUrlString());
////
////                            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(e);
////                            System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());
////
////                        }
////                        else {
////                            episode.setShow_id(tvShowId);
////                            if (DatabaseClient.getInstance(context).getAppDatabase().episodeDao().findByLink(episode.getIndex_id()) == null)
////                                DatabaseClient.getInstance(context).getAppDatabase().episodeDao().insert(episode);
////                        }
////                    }
////                }
//
//
//                }
//
//            } catch (IOException e) {
//            System.out.println(e);
//        }
//    }


//THIS CODE WAS INSIDE TMDBGETBYID
//            //request for tv show by id and
//            TVShow oldTV = (TVShow) myMedia;
//
//            //delete tv show (with older wrong info)
//            DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().deleteById(oldTV.getId());
//
//
//            for (Season tvseason: oldTV.getSeasons()) {
//                int tvSeasonId = tvseason.getId();
//                //delete all the seasons
//                DatabaseClient.getInstance(context).getAppDatabase().tvShowSeasonDetailsDao().deleteById(tvSeasonId);
//            }
//
//
//            if(id!=null){
//                String finalurl ="https://api.themoviedb.org/3/tv/"+id+"?api_key=" + TMDB_API_KEY + "&language=en-US";
//                StringBuilder responseTvShowDetails = new StringBuilder();
//                try {
//                    URL url = new URL(finalurl);
//                    Log.i("Show title" , finalurl);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setRequestMethod("GET");
//                    int responseCode = con.getResponseCode();
//                    Log.i("tmdb api response" , String.valueOf(responseCode));
//                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
//                        BufferedReader in = new BufferedReader(new InputStreamReader(
//                                con.getInputStream()));
//                        String inputLine;
//                        responseTvShowDetails = new StringBuilder();
//                        while ((inputLine = in.readLine()) != null) {
//                            responseTvShowDetails.append(inputLine);
//                        }
//                        in.close();
//                    } else {
//                        System.out.println("GET request did not work");
//                    }
//                    Log.i("responseTvShowDetails" , responseTvShowDetails.toString());
//
//                    Gson gson = new Gson();
//                    myMedia = gson.fromJson(responseTvShowDetails.toString() , TVShow.class);
//
//                    Log.i("tvShow" , myMedia.toString());
//
//                    DatabaseClient.getInstance(context).getAppDatabase().tvShowDao().insert((TVShow) myMedia);
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            //get all the episodes to be update with new info
//            List<Episode> episodeList = DatabaseClient.getInstance(context).getAppDatabase().episodeDao().getFromThisShow(oldTV.getId());
//
//            for (Episode episode: episodeList) {
////                getSeasonInfo(episode.getSeason_number(),episode.getEpisode_number());
//            }



