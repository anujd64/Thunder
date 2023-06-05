//package com.theflexproject.thunder.utils;
//
//package com.theflexproject.thunder.utils;
//
//import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;
//import static com.theflexproject.thunder.Constants.getFanartApiKey;
//import static com.theflexproject.thunder.utils.PlexMovieExtractor.getTMDBId;
//import static com.theflexproject.thunder.utils.ShowUtils.EPNUM;
//import static com.theflexproject.thunder.utils.ShowUtils.SEASON;
//import static com.theflexproject.thunder.utils.ShowUtils.SHOW;
//
//import android.content.Context;
//import android.util.Log;
//
//import com.google.common.collect.Iterables;
//import com.google.gson.JsonSyntaxException;
//import com.theflexproject.thunder.ApiUtils.FanartApi;
//import com.theflexproject.thunder.ApiUtils.TMDBApi;
//import com.theflexproject.thunder.database.AppDatabase;
//import com.theflexproject.thunder.model.ExternalIds;
//import com.theflexproject.thunder.model.FanArt.Clearlogo;
//import com.theflexproject.thunder.model.FanArt.FanArtMovieResponseModel;
//import com.theflexproject.thunder.model.FanArt.FanArtTvResponseModel;
//import com.theflexproject.thunder.model.FanArt.Hdmovielogo;
//import com.theflexproject.thunder.model.FanArt.Hdtvlogo;
//import com.theflexproject.thunder.model.FanArt.Movielogo;
//import com.theflexproject.thunder.model.Movie;
//import com.theflexproject.thunder.model.MoviesResponseFromTMDB;
//import com.theflexproject.thunder.model.MyMedia;
//import com.theflexproject.thunder.model.TVShowInfo.Episode;
//import com.theflexproject.thunder.model.TVShowInfo.Result;
//import com.theflexproject.thunder.model.TVShowInfo.TVShow;
//import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
//import com.theflexproject.thunder.model.TVShowInfo.TVShowsResponseFromTMDB;
//import com.theflexproject.thunder.model.tmdbImages.Logo;
//import com.theflexproject.thunder.model.tmdbImages.TMDBImagesResponse;
//
//import java.io.IOException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//
//import javax.inject.Inject;
//
//import dagger.hilt.android.AndroidEntryPoint;
//import dagger.hilt.android.qualifiers.ActivityContext;
//import me.xdrop.fuzzywuzzy.FuzzySearch;
//import me.xdrop.fuzzywuzzy.model.ExtractedResult;
//import retrofit2.Call;
//import retrofit2.Response;
//@AndroidEntryPoint
//public class SendGetRequestWithHilt {
//
//    AppDatabase appDatabase;
//    Context context;
//    @Inject
//    public SendGetRequestWithHilt(
//            @ActivityContext Context context,
//            AppDatabase appDatabase
//    ){
//        this.context = context;
//        this.appDatabase = appDatabase;
//    }
//
//
//    String urlLogo = "";
//    TMDBImagesResponse tmdbImagesResponse;
//    FanArtMovieResponseModel fanArtMovieResponseModel;
//    FanArtTvResponseModel fanArtTvResponseModel;
//    long tvdb_id = 0;
//    TVShowSeasonDetails tvShowSeasonDetails;
//
//
////     void sendGet2(Movie movie) {
////        String fileName = movie.getFileName();
////
////        //Match if the fileName contains tmdb id
////        String movieIdFromPlexExtractor = (getTMDBId(movie.getUrlString()));
////
////        if (movieIdFromPlexExtractor != null) {
////            System.out.println("movieId from plex extractor");
////            getMovieById(Long.parseLong(movieIdFromPlexExtractor), movie);
////        }
////
////
////        //Match if the fileName follows scene naming pattern
////        String[] titleYear;
////        titleYear = SceneMovieTitleExtractor.getTitleYear(fileName.replaceAll("Copy of ", ""));
////
////        //Match if the fileName follows other loosely followed naming conventions
////        if (titleYear == null) {
////            titleYear = MovieTitleExtractor2.getTitle2(fileName.replaceAll("Copy of ", ""));
////        }
////
////        //Extract title and year
////        String titleExtracted = "", yearExtracted = "0";
////        if (titleYear != null) {
////            if (titleYear[0] != null) {
////                titleExtracted = titleYear[0];
////                if (titleYear[1] != null && titleYear[1].length() > 3) {
////                    yearExtracted = titleYear[1];
////                } else {
////                    yearExtracted = ParseUtils.yearExtractor(fileName).second;
////                }
////            }
////        }
////
////
////        //Sends tmdb request to search on tmdb and receive multiple responses
//////        String response = searchMovieOnTmdbByName(titleExtracted, yearExtracted);
////
//////        Gson gson = new Gson();
////        MoviesResponseFromTMDB moviesResponseFromTMDB = searchMovieOnTmdbByName(titleExtracted, yearExtracted);
////        ;
////
////        ArrayList<String> titlesAndYearsFromTMDB = new ArrayList<>();
////
////        if (moviesResponseFromTMDB != null && moviesResponseFromTMDB.results.size() != 0) {
////            for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
////                String releaseDate = moviesResponseFromTMDB.results.get(i).getRelease_date();
////                if (releaseDate != null)
////                    if (releaseDate.length() == 4) {
////                        titlesAndYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + releaseDate);
////                    } else {
////                        titlesAndYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + releaseDate.split("-")[0]);
////                    }
////            }
////            int finalIndex = 0;
////            if (!titleExtracted.equals("") && !yearExtracted.equals("0")) {
////                finalIndex = findIndexOfClosestMatch(titleExtracted + " " + yearExtracted, titlesAndYearsFromTMDB);
////            }
////            int movieId = moviesResponseFromTMDB.results.get(finalIndex).getId();
////            getMovieById(movieId, movie);
////        } else if (movie.getTitle() == null) {
////            System.out.println("file with no tmdb info" + movie);
////            appDatabase.movieDao().insert(movie);
////        }
////    }
//
//    void sendGet2(Movie movie) {
//        String fileName = movie.getFileName();
//        String movieIdFromPlexExtractor = getTMDBId(movie.getUrlString());
//
//
//        if (movieIdFromPlexExtractor != null) {
//            //If the file name contains tmdb id then get the movie details from tmdb
//            getMovieById(Long.parseLong(movieIdFromPlexExtractor), movie);
//        }else{
//            //If the file name does not contain tmdb id then get the movie details from tmdb by searching on tmdb
//
//            String[] titleYear = SceneMovieTitleExtractor.getTitleYear(fileName.replaceAll("Copy of ", ""));
//            if (titleYear == null) {
//                titleYear = MovieTitleExtractor2.getTitle2(fileName.replaceAll("Copy of ", ""));
//            }
//
//            String titleExtracted = "", yearExtracted = "0";
//            if (titleYear != null) {
//                titleExtracted = titleYear[0];
//                yearExtracted = titleYear[1] != null && titleYear[1].length() > 3 ? titleYear[1] : ParseUtils.yearExtractor(fileName).second;
//            }
//
//            MoviesResponseFromTMDB moviesResponseFromTMDB = searchMovieOnTmdbByName(titleExtracted, yearExtracted);
//            if (moviesResponseFromTMDB == null || moviesResponseFromTMDB.results.isEmpty()) {
//                if (movie.getTitle() == null) {
//                    System.out.println("file with no tmdb info " + movie);
//                    appDatabase.movieDao().insert(movie);
//                }
//                return;
//            }
//
//            ArrayList<String> titlesAndYearsFromTMDB = new ArrayList<>();
//            for (Movie result : moviesResponseFromTMDB.results) {
//                String releaseDate = result.getRelease_date();
//                if (releaseDate != null) {
//                    titlesAndYearsFromTMDB.add(result.getTitle() + " " + (releaseDate.length() == 4 ? releaseDate : releaseDate.split("-")[0]));
//                }
//            }
//
//            int finalIndex = !titleExtracted.isEmpty() && !yearExtracted.equals("0") ? findIndexOfClosestMatch(titleExtracted + " " + yearExtracted, titlesAndYearsFromTMDB) : 0;
//            int movieId = moviesResponseFromTMDB.results.get(finalIndex).getId();
//            getMovieById(movieId, movie);
//        }
//
//
//    }
//    private  MoviesResponseFromTMDB searchMovieOnTmdbByName(String titleExtracted, String yearExtracted) {
//        try {
//            Call<MoviesResponseFromTMDB> responseCall = TMDBApi.getTMDBService().searchMovie(
//                    titleExtracted,
//                    yearExtracted,
//                    false,
//                    1
//            );
//
//            Response<MoviesResponseFromTMDB> response = responseCall.execute();
//            if (response != null && response.isSuccessful() && response.body() != null) {
//                Log.d("searchMovieOnTmdbByName", response.body().toString());
//                return response.body();
//            }
//        } catch (Exception e) {
//            Log.d("searchMovieOnTmdbByName", e.toString());
//        }
//        return null;
//    }
//    private  void getMovieById(long id, MyMedia myMedia) {
//        Movie movie = (Movie) myMedia;
//
//        if (appDatabase.movieDao().getByGdId(movie.getGd_id()) != null) {
//            appDatabase.movieDao().deleteByGdId(movie.getGd_id());
//        }
//        try {
//            String fileName = movie.getFileName();
//            String mimeType = movie.getMimeType();
//            Date modifiedTime = movie.getModifiedTime();
//            String size = movie.getSize();
//            String urlString = movie.getUrlString();
//            String gd_id = movie.getGd_id();
//            int index_id = movie.getIndex_id();
//
//            Call<Movie> responseCall = TMDBApi.getTMDBService().getMovieById(id);
//            Response<Movie> response = responseCall.execute();
//
//            if (response != null && response.isSuccessful() && response.body() != null) {
//                movie = response.body();
//                Log.d("responseById ", movie.toString());
//
//                String logo_path = getLogoTMDB(id, false);
//                if (logo_path.equals("")) logo_path = getLogoFanArt(id, false);
//                movie.setLogo_path(logo_path);
//
//                movie.setFileName(fileName);
//                movie.setMimeType(mimeType);
//                movie.setModifiedTime(modifiedTime);
//                movie.setSize(size);
//                movie.setUrlString(urlString);
//                movie.setGd_id(gd_id);
//                movie.setIndex_id(index_id);
//
//                Log.d("responseById ", "movie after adding everything" + movie.toString());
//
//                if (appDatabase.movieDao().getByTMDBID(movie.getId()) == null) {
//                    appDatabase.movieDao().insert(movie);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    void sendGetTVShow(Episode episode) {
//
//        String episodeFileName = episode.getFileName().replaceAll("Copy of ", "");
//        Map<String, String> result = ShowUtils.parseShowName(episodeFileName);
//        String finalShowName = null;
//        String finalSeasonNumber = null;
//        String finalEpisodeNumber = null;
//        if (result != null) {
//            finalShowName = result.get(SHOW);
//            finalSeasonNumber = result.get(SEASON);
//            finalEpisodeNumber = result.get(EPNUM);
//        }
//        String finalurl = "";
//        long tvShowId = 0;
//
//        boolean failed = false;
//        try {
//            tvShowId = Long.parseLong(getTMDBId(episode.getUrlString()));
//            ;
//            System.out.println("parsed show id " + tvShowId);
//
//            TVShow tvShow = new TVShow();
////            if(tvShowId != 0) {
////                failed = true;
////                getTVById(tvShowId, tvShow);
////            }
//            //Request for season details
//            if (tvShowId != 0)
//                getTVSeasonById2(tvShowId, finalSeasonNumber, finalEpisodeNumber, episode);
//
//
//        } catch (NumberFormatException | NullPointerException e) {
//            failed = true;
//            System.out.println("parse failed get tv" + e.getMessage());
//        }
//
//        if (finalShowName != null) {
//            TVShow test = appDatabase.tvShowDao().findByName(finalShowName);
//            if (test != null && test.getId() != 0) {
//                getTVSeasonById2(test.getId(), finalSeasonNumber, finalEpisodeNumber, episode);
//            }
//            if (test == null && tvShowId == 0) {
//                try {
//                    String encodedShowName = URLEncoder.encode(finalShowName, "UTF-8");
//
//                    Call<TVShowsResponseFromTMDB> responseCall = TMDBApi.getTMDBService().searchTVShow(
//                            encodedShowName,
//                            false,
//                            1
//                    );
//
//                    Response<TVShowsResponseFromTMDB> response = responseCall.execute();
//
//                    if (response != null && response.isSuccessful() && response.body() != null) {
//                        TVShowsResponseFromTMDB tvShowsResponseFromTMDB = response.body();
//
//                        Log.i("tvShowsResponseFromTMDB", "Success: " + tvShowsResponseFromTMDB);
//
//                        /*better method for selection of tv show */
//                        if (tvShowsResponseFromTMDB.results.size() > 0) {
//                            ArrayList<String> tvTitlesFromTMDB = new ArrayList<>();
//
//                            for (int i = 0; i < tvShowsResponseFromTMDB.getResults().size(); i++) {
//                                Result tv = tvShowsResponseFromTMDB.getResults().get(i);
//                                tvTitlesFromTMDB.add(tv.getName());
//                            }
//                            ExtractedResult matchedTvTitle;
//                            int finalIndex = 0;
//                            matchedTvTitle = FuzzySearch.extractOne(finalShowName, tvTitlesFromTMDB);
//                            if (matchedTvTitle.getScore() == 100) {
//                                finalIndex = matchedTvTitle.getIndex();
//                            } else if (matchedTvTitle.getScore() > 70) {
//                                finalIndex = matchedTvTitle.getIndex();
//                            }
//                            tvShowId = tvShowsResponseFromTMDB.results.get(finalIndex).getId();
//                            if (tvShowId != 0) {
//                                //Request for tv show by id and
//                                TVShow tvShow = new TVShow();
//                                getTVById(tvShowId, tvShow);
//                                //Request for season details
//                                getTVSeasonById2(tvShowId, finalSeasonNumber, finalEpisodeNumber, episode);
//                            }
//                        }
//
//
//                    }
//                } catch (IOException e) {
//                    System.out.println(e.toString());
//                }
//
//            }
//
//
//        }
//
//
//    }
//    private  void getTVById(long tvShowId, MyMedia myMedia) {
//        TVShow tvShow = (TVShow) myMedia;
//
//        long oldTVShowID = tvShow.getId();
//        appDatabase.tvShowDao().delete(tvShow);
//        appDatabase.tvShowSeasonDetailsDao().deleteByShowId(oldTVShowID);
//
//
//        TVShow test = appDatabase.tvShowDao().find(tvShowId);
//
//        try {
//            if (test == null && tvShowId != 0) {
//                Call<TVShow> responseCall = TMDBApi.getTMDBService().getTVShowById(tvShowId);
//                Response<TVShow> response = responseCall.execute();
//                if (response != null && response.isSuccessful() && response.body() != null) {
//                    tvShow = response.body();
//
//                    Log.d("getTVById ", "tvShowResponse Success: " + tvShow);
//
//                    String logoPath = getLogoTMDB(tvShowId, true);
//                    if (logoPath.equals("")) {
//                        tvdb_id = getTVDBId(tvShowId);
//                        logoPath = getLogoFanArt(tvdb_id, true);
//                    }
//
//                    tvShow.setLogo_path(logoPath);
//                    appDatabase.tvShowDao().insert(tvShow);
//                }
//            }
//
//            //get all the episodes to be update with new info
//            List<Episode> episodeList = appDatabase.episodeDao().getFromThisShow(oldTVShowID);
//            for (Episode e : episodeList) {
//                System.out.println("Inside for loop");
//
//                String episodeFileName = e.getFileName().replaceAll("Copy of ", "");
//                Map<String, String> result = ShowUtils.parseShowName(episodeFileName);
////            String finalShowName = null;
//                String finalSeasonNumber = null;
//                String finalEpisodeNumber = null;
//                if (result != null) {
////                finalShowName = result.get(SHOW);
//                    finalSeasonNumber = result.get(SEASON);
//                    finalEpisodeNumber = result.get(EPNUM);
//                }
//                getTVSeasonById2(tvShowId, finalSeasonNumber, finalEpisodeNumber, e);
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }
//    private  void getTVSeasonById2(long tvShowId, String finalSeasonNumber, String finalEpisodeNumber, Episode episode) {
//        try {
//            if (finalSeasonNumber != null) {
//
//                tvShowSeasonDetails = appDatabase.tvShowSeasonDetailsDao().findByShowIdAndSeasonNumber(tvShowId, finalSeasonNumber);
//
//                if (tvShowSeasonDetails == null) {
//
//                    Call<TVShowSeasonDetails> call = TMDBApi.getTMDBService().getSeasonByTVId(tvShowId, finalSeasonNumber);
//                    Response<TVShowSeasonDetails> response = call.execute();
//
//                    if (response != null && response.isSuccessful() && response.body() != null) {
//                        tvShowSeasonDetails = response.body();
//                        tvShowSeasonDetails.setShow_id(tvShowId);
//                        appDatabase.tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
//                        Log.d("getTVSeasonById2", "Success From Network" + tvShowSeasonDetails);
//                    } else {
//                        Log.d("getTVSeasonById2", "Failure");
//                    }
//                } else {
//                    Log.d("getTVSeasonById2", "Success From DB" + tvShowSeasonDetails);
//                }
//
//                Log.d("tvShowSeasonDetails ", tvShowSeasonDetails.toString());
//
//                Episode matchingEpisode = Iterables.tryFind(
//                        tvShowSeasonDetails.getEpisodes(),
//                        e -> Integer.parseInt(finalEpisodeNumber) == e.getEpisode_number()
//                ).orNull();
//
//                if (matchingEpisode != null) {
//                    matchingEpisode.setFileName(episode.getFileName());
//                    matchingEpisode.setMimeType(episode.getMimeType());
//                    matchingEpisode.setModifiedTime(episode.getModifiedTime());
//                    matchingEpisode.setSize(episode.getSize());
//                    matchingEpisode.setUrlString(episode.getUrlString());
//                    matchingEpisode.setGd_id(episode.getGd_id());
//                    matchingEpisode.setIndex_id(episode.getIndex_id());
//
//                    matchingEpisode.setSeason_id(tvShowSeasonDetails.getId());
//                    matchingEpisode.setShow_id(tvShowId);
//
////                    appDatabase.episodeDao().deleteByLink(matchingEpisode.getGd_id());
//                    appDatabase.episodeDao().insert(matchingEpisode);
//
//                    System.out.println("Episode before inserting in db found in tvseason details" + matchingEpisode);
//                    System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + "Ep from the season details" + matchingEpisode.getEpisode_number());
//                } else {
//                    episode.setShow_id(tvShowId);
//                    appDatabase.episodeDao().insert(episode);
//                }
//            }
//        } catch (Exception e) {
//            Log.d("getTVSeasonById2", e.toString());
//        }
//
//    }
//    public  void tmdbGetByID(MyMedia myMedia, long id, boolean isShow) {
//        try {
//            if (isShow) {
//                getTVById(id, myMedia);
//            } else {
//                getMovieById(id, myMedia);
//            }
//        } catch (NullPointerException e) {
//            Log.d("tmdbGetByID ", "Exception: " + e);
//        }
//    }
//    private  int findIndexOfClosestMatch(String s, ArrayList<String> titlesAndYearsFromTMDB) {
//        try {
//            ExtractedResult result = FuzzySearch.extractOne(s, titlesAndYearsFromTMDB);
//            Log.d("findIndexOfClosestMatch", "Success: " + result);
//            return result.getIndex();
//        } catch (JsonSyntaxException | NoSuchElementException elementException) {
//            Log.d("findIndexOfClosestMatch", "Exception: " + elementException);
//        }
//        return 0;
//    }
//    private  String getLogoTMDB(long id, boolean isShow) {
//
//        try {
//            urlLogo = "";
//
//            Call<TMDBImagesResponse> call;
//            if (isShow) {
//                call = TMDBApi.getTMDBService().getImagesForTVFromTMDB(id);
//            } else {
//                call = TMDBApi.getTMDBService().getImagesForMovieFromTMDB(id);
//            }
//            Response<TMDBImagesResponse> response = call.execute();
//            ;
//            if (response != null && response.isSuccessful() && response.body() != null) {
//                tmdbImagesResponse = response.body();
//
//                assert tmdbImagesResponse != null;
//
//                List<Logo> logoList = tmdbImagesResponse.getLogos();
//                Optional<String> optionalUrl = logoList.stream()
//                        .filter(logo -> logo.getIso_639_1() != null && logo.getIso_639_1().equals("en") && !logo.getFile_path().contains(".svg"))
//                        .map(logo -> TMDB_IMAGE_BASE_URL + logo.getFile_path())
//                        .findFirst();
//
//                optionalUrl.ifPresent(s -> urlLogo = s);
//                Log.d("getLogoTMDB", "Success " + urlLogo);
//                tmdbImagesResponse = null;
//                return urlLogo;
//            }
//        } catch (Exception e) {
//            Log.d("getLogoTMDB", "Exception: " + e);
//        }
//        return "";
//    }
//    private  String getLogoFanArt(long id, boolean isShow) {
//        try {
//            urlLogo = "";
//            if (isShow) {
//                Call<FanArtTvResponseModel> responseCall = FanartApi.getFanartService().getImagesTV(id, getFanartApiKey());
//                Response<FanArtTvResponseModel> response = responseCall.execute();
//                if (response != null && response.isSuccessful() && response.body() != null) {
//                    fanArtTvResponseModel = response.body();
//                    if (fanArtTvResponseModel != null) {
//                        urlLogo = Iterables.tryFind(fanArtTvResponseModel.getHdtvlogo(), input -> "en".equals(input.getLang()) || "hi".equals(input.getLang()))
//                                .transform(Hdtvlogo::getUrl)
//                                .or("");
//
//                        if (urlLogo.isEmpty()) {
//                            urlLogo = Iterables.tryFind(fanArtTvResponseModel.getClearlogo(), input -> "en".equals(input.getLang()) || "hi".equals(input.getLang()))
//                                    .transform(Clearlogo::getUrl)
//                                    .or("");
//                            Log.d("getLogoFanArt", "urlLogo " + urlLogo);
//                        }
//                        fanArtTvResponseModel = null;
//                    }
//                }
//            } else {
//                Call<FanArtMovieResponseModel> responseCall = FanartApi.getFanartService().getImagesMovie(id, getFanartApiKey());
//                Response<FanArtMovieResponseModel> response = responseCall.execute();
//                if (response != null && response.isSuccessful() && response.body() != null) {
//                    fanArtMovieResponseModel = response.body();
//
//                    if (fanArtMovieResponseModel != null) {
//                        urlLogo = Iterables.tryFind(fanArtMovieResponseModel.getHdmovielogo(), input -> "en".equals(input.getLang()) || "hi".equals(input.getLang()))
//                                .transform(Hdmovielogo::getUrl)
//                                .or("");
//                        Log.d("getLogoFanArt", "urlLogo " + urlLogo);
//                        if (urlLogo.isEmpty()) {
//                            urlLogo = Iterables.tryFind(fanArtMovieResponseModel.getMovielogo(), input -> "en".equals(input.getLang()) || "hi".equals(input.getLang()))
//                                    .transform(Movielogo::getUrl)
//                                    .or("");
//                        }
//                        fanArtMovieResponseModel = null;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.d("getLogoFanArt", "Exception: " + e);
//        }
//        return urlLogo;
//    }
//    private  long getTVDBId(long tvShowId) {
//        try {
//            tvdb_id = 0;
//            Call<ExternalIds> call = TMDBApi.getTMDBService().getExternalIdsForTV(tvShowId);
//            Response<ExternalIds> response = call.execute();
//            if (response != null && response.isSuccessful() && response.body() != null) {
//                ExternalIds externalIds = response.body();
//                if (externalIds != null) {
//                    tvdb_id = externalIds.getTvdb_id();
//                    Log.d("getTVDBIds", "retrieved externalId " + externalIds.tvdb_id);
//                    return tvdb_id;
//                }
//            }
//        } catch (Exception e) {
//            Log.d("getTVDBId", "Exception: " + e);
//        }
//        return tvdb_id;
//    }
//
//}
//
////     void sendGet(Movie movie) {
////        String finalurl = "";
////        String[] titleYear1 = SceneMovieTitleExtractor.getTitleYear(movie.getFileName().replaceAll("Copy of " , ""));
////        String[] titleYear2 = MovieTitleExtractor2.getTitle2(movie.getFileName().replaceAll("Copy of " , ""));
////        String titleFromFile1 = "", titleFromFile2 = "", yearFromFile1 = "", yearFromFile2 = "";
////        if (titleYear1 != null) {
////            titleFromFile1 = titleYear1[0];
////        } else {
////            titleFromFile2 = titleYear2[0];
////        }
////        if (titleYear1 != null) {
////            yearFromFile1 = titleYear1[0];
////        } else {
////            yearFromFile2 = titleYear2[0];
////        }
////
////        try {
////            try {
////                if (SceneMovieTitleExtractor.getTitleYear(movie.getFileName()) != null) {
////                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
////                            + URLEncoder.encode(titleFromFile1 , "UTF-8");
////                } else {
////                    finalurl = TMDB_GET_REQUEST_BASE_URL + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query="
////                            + URLEncoder.encode(titleFromFile2 , "UTF-8");
////                }
////            } catch (Exception e) {
////                Log.i("exc" , e.toString());
////                e.printStackTrace();
////            }
////            StringBuilder response = new StringBuilder();
////            try {
////                URL url = new URL(finalurl);
////                Log.i("movie title" , finalurl);
////                HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                con.setRequestMethod("GET");
////                int responseCode = con.getResponseCode();
////                Log.i("tmdb api response" , String.valueOf(responseCode));
////                if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                    BufferedReader in = new BufferedReader(new InputStreamReader(
////                            con.getInputStream()));
////                    String inputLine;
////                    response = new StringBuilder();
////                    while ((inputLine = in.readLine()) != null) {
////                        response.append(inputLine);
////                    }
////                    in.close();
////                } else {
////                    System.out.println("GET request did not work");
////                }
////                Gson gson = new Gson();
////                MoviesResponseFromTMDB moviesResponseFromTMDB = gson.fromJson(response.toString() , MoviesResponseFromTMDB.class);
////
////                if (moviesResponseFromTMDB.results.size() > 0) {
////                    //New Changes
////                    ArrayList<String> titlesFromTMDB = new ArrayList<>();
////                    ArrayList<String> yearsFromTMDB = new ArrayList<>();
////                    ArrayList<String> titlesandYearsFromTMDB = new ArrayList<>();
////                    for (int i = 0; i < moviesResponseFromTMDB.results.size(); i++) {
////                        titlesFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle());
////                        yearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getRelease_date());
////                        titlesandYearsFromTMDB.add(moviesResponseFromTMDB.results.get(i).getTitle() + " " + moviesResponseFromTMDB.results.get(i).getRelease_date());
////                    }
////
////                    ExtractedResult result;
////                    String finalTitle;
////                    int finalIndex = 0;
////
////                    Log.i("list of tmdb" , titlesFromTMDB.toString());
////                    try {
////                        if (titleFromFile1 != null) {
////                            result = FuzzySearch.extractOne(titleFromFile1 , titlesFromTMDB);
////                            Log.i(result.toString() , "result");
////                            if (result.getScore() > 60) {
////                                finalTitle = result.getString();
////                                finalIndex = result.getIndex();
////                                Log.i(result.toString() , finalTitle);
////                            } else {
////                                result = FuzzySearch.extractOne(titleFromFile1 + yearFromFile1 , titlesandYearsFromTMDB);
////                                finalTitle = result.getString();
////                                finalIndex = result.getIndex();
////                                Log.i(result.toString() , finalTitle);
////                            }
////
////                        } else {
////                            result = FuzzySearch.extractOne(titleFromFile2 + " " + yearFromFile2 , titlesFromTMDB);
////                            if (result.getScore() > 60) {
////                                finalTitle = result.getString();
////                                finalIndex = result.getIndex();
////                                Log.i(result.toString() , finalTitle);
////                            } else {
////                                result = FuzzySearch.extractOne(titleFromFile2 + yearFromFile2 , titlesandYearsFromTMDB);
////                                finalTitle = result.getString();
////                                finalIndex = result.getIndex();
////                                Log.i(result.toString() , finalTitle);
////                            }
////                        }
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                    String fileName = movie.getFileName();
////                    String mimeType = movie.getMimeType();
////                    Date modifiedTime = movie.getModifiedTime();
////                    String size = movie.getSize();
////                    String urlString = movie.getUrlString();
////
////                    movie = moviesResponseFromTMDB.results.get(finalIndex);
////
////                    movie.setFileName(fileName);
////                    movie.setMimeType(mimeType);
////                    movie.setModifiedTime(modifiedTime);
////                    movie.setSize(size);
////                    movie.setUrlString(urlString);
////
//////                    file.setGenre_ids(tmdbResponse.results.get(0).getGenre_ids());
////                    Log.i("sendGet: " , moviesResponseFromTMDB.results.get(finalIndex).toString());
////                }
////
////            } catch (MalformedURLException e) {
////                Log.i("exe" , e.toString());
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////
////        System.out.println("file with no tmdb info" + movie);
////
////        movie.setTitle(movie.getFileName());
////
////
////        if (appDatabase.movieDao().getByFileName(movie.getFileName()) == null) {
////            appDatabase.movieDao().insert(movie);
////
////        }
////    }
//
//
////    private  void getSeasonInfo(int tvShowId, String seasonNumber) {
////
////
////        //Request for season details
////        StringBuilder responseSeasonDetails = new StringBuilder();
////        try {
////            if (tvShowSeasonId != 0) {
////                finalurl ="https://api.themoviedb.org/3/tv/"+tvShowId+"/season/"+finalSeasonNumber+"?api_key=" + TMDB_API_KEY + "&language=en-US";
////                URL url = new URL(finalurl);
////                Log.i("Show title" , finalurl);
////                HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                con.setRequestMethod("GET");
////                int responseCode = con.getResponseCode();
////                Log.i("tmdb api response" , String.valueOf(responseCode));
////                if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                    BufferedReader in = new BufferedReader(new InputStreamReader(
////                            con.getInputStream()));
////                    String inputLine;
////                    responseSeasonDetails = new StringBuilder();
////                    while ((inputLine = in.readLine()) != null) {
////                        responseSeasonDetails.append(inputLine);
////                    }
////                    in.close();
////                } else {
////                    System.out.println("GET request did not work");
////                }
////
////                Gson gson = new Gson();
////
////                TVShowSeasonDetails tvShowSeasonDetails = gson.fromJson(responseSeasonDetails.toString() , TVShowSeasonDetails.class);
////
////
////
////
////            }
////
////        } catch (IOException e) {
////            System.out.println(e);
////        }
////
////
////
////
////
////            TVShowSeasonDetails test = appDatabase.tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
////            if(test== null) appDatabase.tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
////
////            System.out.println("Episode Number" +finalEpisodeNumber);
////            for (Episode e :tvShowSeasonDetails.getEpisodes() ) {
////                assert finalEpisodeNumber != null;
////                if(e.getEpisode_number()== Integer.parseInt(finalEpisodeNumber) ){
////                    System.out.println("Episode Number from title of the file " +Integer.parseInt(finalEpisodeNumber)+" Ep from the season details"+e.getEpisode_number());
////
////                    e.setFileName(episode.getFileName());
////                    e.setMimeType(episode.getMimeType());
////                    e.setModifiedTime(episode.getModifiedTime());;
////                    e.setSize(episode.getSize());
////                    e.setUrlString(episode.getUrlString());
////                    e.setSeason_id(tvShowSeasonDetails.getId());
////                    //why null here?
////                    System.out.println("ep in season in seasondetails"+episode.toString());
////
//////                    episode = gson.fromJson(e.toString(),Episode.class);
////
//////                            episode = e;
//////                            episode.setSeason_id(tvShowSeasonDetails.getId());
//////                            episode.setFileName(fileName);
//////                            episode.setMimeType(mimeType);
//////                            episode.setModifiedTime(modifiedTime);
//////                            episode.setSize(size);
//////                            episode.setUrlString(urlString);
////
////                    System.out.println("ep in season in seasondetails after adding everything"+e.toString());
////                    Episode testEp = appDatabase.episodeDao().findByFileName(e.getFileName());
////                    if(testEp == null){
////                        appDatabase.episodeDao().insert(e);
////                    }
////                }
////            }
////        }
//
//
////    private  void getTVSeasonById(long tvShowId , String finalSeasonNumber , String finalEpisodeNumber , Episode episode) {
////        StringBuilder responseSeasonDetails = new StringBuilder();
////        try {
////            if (finalSeasonNumber != null) {
////                TVShowSeasonDetails tvShowSeasonDetails = appDatabase.tvShowSeasonDetailsDao().findByShowIdAndSeasonNumber(tvShowId , finalSeasonNumber);
////                if (tvShowSeasonDetails == null) {
////                    System.out.println("Season Number " + finalSeasonNumber);
////
////                    String finalUrl = "https://api.themoviedb.org/3/tv/" + tvShowId + "/season/" + finalSeasonNumber + "?api_key=" + TMDB_API_KEY + "&language=en-US";
////                    URL url = new URL(finalUrl);
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    con.setRequestMethod("GET");
////                    int responseCode = con.getResponseCode();
////                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                        BufferedReader in = new BufferedReader(new InputStreamReader(
////                                con.getInputStream()));
////                        String inputLine;
////                        responseSeasonDetails = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            responseSeasonDetails.append(inputLine);
////                        }
////                        in.close();
////                    } else {
////                        System.out.println("GET request did not work");
////                    }
////
////                    Gson gson = new Gson();
////
////                    tvShowSeasonDetails = gson.fromJson(responseSeasonDetails.toString() , TVShowSeasonDetails.class);
////
////
////                    if (tvShowSeasonDetails != null) {
////
////                        if (tvShowId != 0) {
////                            tvShowSeasonDetails.setShow_id(tvShowId);
////                        }
////
////                    System.out.println("tvShowSeasonDetails " + tvShowSeasonDetails);
////                    TVShowSeasonDetails test = appDatabase.tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
////                    if (test == null)
////                        appDatabase.tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
////
////                    }
////
////
////                }
////
////                System.out.println("Episode Number" + finalEpisodeNumber);
////
////                try {
////
////                    Episode e = tvShowSeasonDetails.getEpisodes().stream()
////                        .filter(episode1 -> (Integer.parseInt(finalEpisodeNumber)==episode1.getEpisode_number()))
////                        .findAny()
////                        .orElse(null);
////
////
////                    System.out.println("Episode before inserting in db found in tvseason details" +e.toString());
////                    if(e!=null) {
////                        e.setFileName(episode.getFileName());
////                        e.setMimeType(episode.getMimeType());
////                        e.setModifiedTime(episode.getModifiedTime());
////                        e.setSize(episode.getSize());
////                        e.setUrlString(episode.getUrlString());
////                        e.setGd_id(episode.getGd_id());
////                        e.setIndex_id(episode.getIndex_id());
////
////                        e.setSeason_id(tvShowSeasonDetails.getId());
////                        e.setShow_id(tvShowId);
////
//////                            appDatabase.episodeDao().deleteByLink(e.getUrlString());
////
////                        appDatabase.episodeDao().insert(e);
////                        System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());
////                    }
////
////                }catch (NullPointerException e){
////
////                }
////
////
//////                    for (Episode e : tvShowSeasonDetails.getEpisodes()) {
//////                        if (e.getEpisode_number() == Integer.parseInt(finalEpisodeNumber)) {
//////                            System.out.println("Episode Number from title of the file " + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details " + e.getEpisode_number());
//////
//////                            e.setFileName(episode.getFileName());
//////                            e.setMimeType(episode.getMimeType());
//////                            e.setModifiedTime(episode.getModifiedTime());
//////                            e.setSize(episode.getSize());
//////                            e.setUrlString(episode.getUrlString());
//////                            e.setGd_id(episode.getGd_id());
//////                            e.setIndex_id(episode.getIndex_id());
//////
//////                            e.setSeason_id(tvShowSeasonDetails.getId());
//////                            e.setShow_id(tvShowId);
//////
////////                            appDatabase.episodeDao().deleteByLink(e.getUrlString());
//////
//////                            appDatabase.episodeDao().insert(e);
//////                            System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());
//////
//////                        }
//////                        else {
//////                            episode.setShow_id(tvShowId);
//////                            if (appDatabase.episodeDao().findByLink(episode.getIndex_id()) == null)
//////                                appDatabase.episodeDao().insert(episode);
//////                        }
//////                    }
//////                }
////
////
////                }
////
////            } catch (IOException e) {
////            System.out.println(e);
////        }
////    }
//
//
////THIS CODE WAS INSIDE TMDBGETBYID
////            //request for tv show by id and
////            TVShow oldTV = (TVShow) myMedia;
////
////            //delete tv show (with older wrong info)
////            appDatabase.tvShowDao().deleteById(oldTV.getId());
////
////
////            for (Season tvseason: oldTV.getSeasons()) {
////                int tvSeasonId = tvseason.getId();
////                //delete all the seasons
////                appDatabase.tvShowSeasonDetailsDao().deleteById(tvSeasonId);
////            }
////
////
////            if(id!=null){
////                String finalurl ="https://api.themoviedb.org/3/tv/"+id+"?api_key=" + TMDB_API_KEY + "&language=en-US";
////                StringBuilder responseTvShowDetails = new StringBuilder();
////                try {
////                    URL url = new URL(finalurl);
////                    Log.i("Show title" , finalurl);
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    con.setRequestMethod("GET");
////                    int responseCode = con.getResponseCode();
////                    Log.i("tmdb api response" , String.valueOf(responseCode));
////                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                        BufferedReader in = new BufferedReader(new InputStreamReader(
////                                con.getInputStream()));
////                        String inputLine;
////                        responseTvShowDetails = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            responseTvShowDetails.append(inputLine);
////                        }
////                        in.close();
////                    } else {
////                        System.out.println("GET request did not work");
////                    }
////                    Log.i("responseTvShowDetails" , responseTvShowDetails.toString());
////
////                    Gson gson = new Gson();
////                    myMedia = gson.fromJson(responseTvShowDetails.toString() , TVShow.class);
////
////                    Log.i("tvShow" , myMedia.toString());
////
////                    appDatabase.tvShowDao().insert((TVShow) myMedia);
////
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////
////            //get all the episodes to be update with new info
////            List<Episode> episodeList = appDatabase.episodeDao().getFromThisShow(oldTV.getId());
////
////            for (Episode episode: episodeList) {
//////                getSeasonInfo(episode.getSeason_number(),episode.getEpisode_number());
////            }
//
//
////Fanart old code
////        Thread thread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                    String typeOfMedia = "movies";
////
////                    if (isShow) typeOfMedia = "tv";
////
////                    URL url = new URL(FANART_IMAGE_BASE_URL + typeOfMedia + "/" + id + "?api_key=" + getFanartApiKey());
////
////                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                    conn.setRequestMethod("GET");
////
////                    int code = conn.getResponseCode();
////                    System.out.println(("HTTP CODE" + code));
////
////
////                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
////                    StringBuilder sb = new StringBuilder();
////                    for (int c; (c = br.read()) >= 0; )
////                        sb.append((char) c);
////                    br.close();
////
////                    Gson om = new Gson();
////                    if (isShow) {
//////                        tvResponseModel = om.fromJson(sb.toString(), FanArtTvResponseModel.class);
//////                        System.out.println("FanArtMovieResponse" + tvResponseModel);
////                        if (fanArtTvResponseModel != null && fanArtTvResponseModel.getHdtvlogo() != null) {
////                            for (int i = 0; i < fanArtTvResponseModel.getHdtvlogo().size(); i++) {
////                                if (fanArtTvResponseModel.getHdtvlogo().get(i).getLang().equals("en") || fanArtTvResponseModel.getHdtvlogo().get(i).getLang().equals("hi")) {
////                                    urlLogo = fanArtTvResponseModel.getHdtvlogo().get(i).getUrl().toString();
////                                    break;
////                                }
////                            }
////                        }
////                        if (urlLogo.equals("") && fanArtTvResponseModel != null && fanArtTvResponseModel.getClearlogo() != null) {
////                            for (int i = 0; i < fanArtTvResponseModel.getClearlogo().size(); i++) {
////                                if (fanArtTvResponseModel.getClearlogo().get(i).getLang().equals("en") || fanArtTvResponseModel.getClearlogo().get(i).getLang().equals("hi")) {
////                                    urlLogo = fanArtTvResponseModel.getClearlogo().get(i).getUrl();
////                                    break;
////                                }
////                            }
////                        }
////                    } else {
//////                        root = om.fromJson(sb.toString(), FanArtMovieResponseModel.class);
//////                        System.out.println("FanArtMovieResponse" + root);
////                        if (fanArtMovieResponseModel != null && fanArtMovieResponseModel.getHdmovielogo() != null) {
////                            for (int i = 0; i < fanArtMovieResponseModel.getHdmovielogo().size(); i++) {
////                                if (fanArtMovieResponseModel.getHdmovielogo().get(i).getLang().equals("en") || fanArtMovieResponseModel.getHdmovielogo().get(i).getLang().equals("hi")) {
////                                    urlLogo = fanArtMovieResponseModel.getHdmovielogo().get(i).getUrl().toString();
////                                    break;
////                                }
////                            }
////                        }
////                        if (urlLogo.equals("") && fanArtMovieResponseModel != null && fanArtMovieResponseModel.getMovielogo() != null) {
////                            for (int i = 0; i < fanArtMovieResponseModel.getMovielogo().size(); i++) {
////                                if (fanArtMovieResponseModel.getMovielogo().get(i).getLang().equals("en") || fanArtMovieResponseModel.getMovielogo().get(i).getLang().equals("hi")) {
////                                    urlLogo = fanArtMovieResponseModel.getMovielogo().get(i).getUrl();
////                                    break;
////                                }
////                            }
////                        }
////                    }
////    }
////        });
////        thread.start();
////        try {
////            thread.join();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////            Call<FanArtMovieResponseModel> fanArtMovieResponseModelCall = FanartApi.getFanartService().getImagesMovie(id, getFanartApiKey());
////            Call<FanArtTvResponseModel> fanArtTvResponseModelCall = FanartApi.getFanartService().getImagesTV(id, getFanartApiKey());
////            if (isShow) {
////                fanArtTvResponseModelCall.enqueue(new Callback<FanArtTvResponseModel>() {
////                    @Override
////                    public void onResponse(@NonNull Call<FanArtTvResponseModel> call, @NonNull Response<FanArtTvResponseModel> response) {
////                        fanArtTvResponseModel = response.body();
////                    }
////
////                    @Override
////                    public void onFailure(@NonNull Call<FanArtTvResponseModel> call, @NonNull Throwable t) {
////
////                        Log.d("FanartApi ", "Failure (TV)");
////                    }
////                });
////            } else {
////                fanArtMovieResponseModelCall.enqueue(new Callback<FanArtMovieResponseModel>() {
////                    @Override
////                    public void onResponse(@NonNull Call<FanArtMovieResponseModel> call, @NonNull Response<FanArtMovieResponseModel> response) {
////                        fanArtMovieResponseModel = response.body();
////                    }
////
////                    @Override
////                    public void onFailure(@NonNull Call<FanArtMovieResponseModel> call, @NonNull Throwable t) {
////                        Log.d("FanartApi ", "Failure (Movie)");
////                    }
////                });
////            }
//
////TMDB Logo / Images old code
//
////        Thread thread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////
////
////                    URL url = new URL(TMDB_BASE_URL + typeOfMedia + "/" + id + "/images?api_key=" + TMDB_API_KEY);
////
////                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
////                    conn.setRequestMethod("GET");
////
////                    int code = conn.getResponseCode();
////                    System.out.println(("HTTP CODE" + code));
////
////
////                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
////                    StringBuilder sb = new StringBuilder();
////                    for (int c; (c = br.read()) >= 0; )
////                        sb.append((char) c);
////                    br.close();
////
////                    Gson om = new Gson();
////
////                    TMDBImagesResponse tmdbImagesResponse;
////                    tmdbImagesResponse = om.fromJson(sb.toString() , TMDBImagesResponse.class);
////            }
////        });
////        thread.start();
////        try {
////            thread.join();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
////                    if (logoArrayList != null) {
////                        for (int i = 0; logoArrayList.size() > 0 && i < logoArrayList.size(); i++) {
////                            if (logoArrayList.get(i).getIso_639_1() != null && (logoArrayList.get(i).getIso_639_1().equals("en")) && !logoArrayList.get(i).getFile_path().contains(".svg")) {
////                                urlLogo = TMDB_IMAGE_BASE_URL + logoArrayList.get(i).getFile_path();
////                                tmdbImagesResponse = null;
////                                break;
////                            }
////                        }
////                    }
////            Call<TMDBImagesResponse> tmdbImagesResponseCall;
////            if (!isShow) {
////                tmdbImagesResponseCall = TMDBApi.getTMDBService().getImagesForMovieFromTMDB(id);
////            } else {
////                tmdbImagesResponseCall = TMDBApi.getTMDBService().getImagesForTVFromTMDB(id);
////            }
//
////            tmdbImagesResponseCall.enqueue(new Callback<TMDBImagesResponse>() {
////                @Override
////                public void onResponse(@NonNull Call<TMDBImagesResponse> call, @NonNull Response<TMDBImagesResponse> response) {
////                    tmdbImagesResponse = response.body();
////
////                    if (tmdbImagesResponse != null) {
////                        List<Logo> logoList = tmdbImagesResponse.getLogos();
////                        Optional<String> optionalUrl = logoList.stream()
////                                .filter(logo -> logo.getIso_639_1() != null && logo.getIso_639_1().equals("en") && !logo.getFile_path().contains(".svg"))
////                                .map(logo -> TMDB_IMAGE_BASE_URL + logo.getFile_path())
////                                .findFirst();
////
////                        optionalUrl.ifPresent(s -> urlLogo = s);
////                        Log.d("getLogoTMDB", "Success" + urlLogo);
////
////                    }
////                }
//
////                @Override
////                public void onFailure(@NonNull Call<TMDBImagesResponse> call, @NonNull Throwable t) {
////                    Log.d("getLogoTMDB", "Failed To retrieve images");
////                }
////            });
////            tmdbImagesResponse = null;
//
//
////External Ids /TVDBID old code
//
////            Call<ExternalIds> externalIdsCall = TMDBApi.getTMDBService().getExternalIdsForTV(tvShowId);
////            externalIdsCall.enqueue(new Callback<ExternalIds>() {
////                @Override
////                public void onResponse(@NonNull Call<ExternalIds> call, @NonNull Response<ExternalIds> response) {
////                    ExternalIds externalIds = response.body();
////                    if (externalIds != null) {
////                        tvdb_id = externalIds.getTvdb_id();
////                        Log.d("getTVDBIds", "retrieved externalId " + externalIds.tvdb_id);
////                    }
////
////                }
////
////                @Override
////                public void onFailure(@NonNull Call<ExternalIds> call, @NonNull Throwable t) {
////                    Log.d("getTVDBIds", "Failed To retrieve externalIds");
////                }
////            });
//
////        Thread thread = new Thread(new Runnable() {
////            @Override
////            public void run() {
////                tvdb_id = 0;
////                String finalurl = TMDB_BASE_URL + "tv/" + tvShowId + "/external_ids?api_key=" + TMDB_API_KEY + "&language=en-US";
////                StringBuilder responseExternalIds = new StringBuilder();
//
////                    URL url = new URL(finalurl);
////                    Log.i("Show title" , finalurl);
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    con.setRequestMethod("GET");
////                    int responseCode = con.getResponseCode();
////                    Log.i("tmdb api response" , String.valueOf(responseCode));
////                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                        BufferedReader in = new BufferedReader(new InputStreamReader(
////                                con.getInputStream()));
////                        String inputLine;
////                        responseExternalIds = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            responseExternalIds.append(inputLine);
////                        }
////                        in.close();
////                    } else {
////                        System.out.println("GET request did not work");
////                    }
////                    Log.i("tvShowById" , responseExternalIds.toString());
////
////
////                    Gson gson = new Gson();
//
//
////                    ExternalIds externalIds = gson.fromJson(responseExternalIds.toString() , ExternalIds.class);
//
////                    tvdb_id = externalIds.getTvdb_id();
////            }
////        });
////        thread.start();
////        try {
////            thread.join();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//
//
////getTVSeasonById2
//
////                if (tvShowSeasonDetails == null) {
////                    StringBuilder responseSeasonDetails = new StringBuilder();
////
////
////                    String finalUrl = TMDB_BASE_URL + "tv/" + tvShowId + "/season/" + finalSeasonNumber + "?api_key=" + TMDB_API_KEY + "&language=en-US";
////                    System.out.println("Season Number " + finalSeasonNumber + "finalurl " + finalUrl);
////
////                    URL url = new URL(finalUrl);
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    con.setRequestMethod("GET");
////                    int responseCode = con.getResponseCode();
////                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
////
////                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
////                        String inputLine;
////                        responseSeasonDetails = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            responseSeasonDetails.append(inputLine);
////                        }
////                        in.close();
////                    } else {
////                        System.out.println("Season details 2 GET request did not work");
////                    }
////
////                    Gson gson = new Gson();
////
////                    System.out.println("the received string season" + String.valueOf(responseSeasonDetails));
////
////
////                    try {
////
////                        String s = responseSeasonDetails.toString();
////                        Pattern pattern = Pattern.compile(Matcher.quoteReplacement("\\\""));
////                        Matcher matcher = pattern.matcher(s);
////                        s = matcher.replaceAll("");
////
////                        System.out.println("changed string" + s);
////
////                        tvShowSeasonDetails = gson.fromJson(s, TVShowSeasonDetails.class);
////
//////
//////                        JSONObject json = (JSONObject) new JSONParser().parse(responseSeasonDetails.toString().replaceAll(Matcher.quoteReplacement("\""),"").replaceAll(Matcher.quoteReplacement("\n"),""));
//////                        tvShowSeasonDetails = gson.fromJson(json.toJSONString(), TVShowSeasonDetails.class);
//////                        tvShowSeasonDetails = gson.fromJson(String.valueOf(responseSeasonDetails), TVShowSeasonDetails.class);
//////                        tvShowSeasonDetails = gson.fromJson(String.valueOf(responseSeasonDetails).replace("\n","\\\n").replace("\"","\\\"") , TVShowSeasonDetails.class);
////                    } catch (Exception e) {
////                        System.out.println("MalformedURLException");
////                    }
////
////
////                    if (tvShowSeasonDetails != null && tvShowSeasonDetails.getEpisodes() != null) {
////
////                        if (tvShowId != 0) {
////                            tvShowSeasonDetails.setShow_id(tvShowId);
////                        }
////
////                        Log.i("tvShowSeasonDetails", tvShowSeasonDetails.toString());
////                        System.out.println("tvShowSeasonDetails in getTVSeasonById2" + tvShowSeasonDetails);
//////                        TVShowSeasonDetails test = appDatabase.tvShowSeasonDetailsDao().find(tvShowSeasonDetails.getId());
//////                        if (test == null)
////
////                        for (Episode e : tvShowSeasonDetails.getEpisodes()) {
////                            e.setModifiedTime(new Date());
////                        }
////                        appDatabase.tvShowSeasonDetailsDao().insert(tvShowSeasonDetails);
////
////                        System.out.println("Episode Number" + finalEpisodeNumber);
////
////
////                    }
////
////
////                }
//
////                    for (Episode e : tvShowSeasonDetails.getEpisodes()) {
////                        if (Integer.parseInt(finalEpisodeNumber) == e.getEpisode_number()) {
////
////                            System.out.println("Episode before inserting in db found in tvseason details" + e.toString());
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
////                            appDatabase.episodeDao().deleteByLink(e.getGd_id());
////
////                            appDatabase.episodeDao().insert(e);
////                            System.out.println("Episode after inserting in db" + Integer.parseInt(finalEpisodeNumber) + " Ep from the season details" + e.getEpisode_number());
////
////                        } else {
////                            episode.setShow_id(tvShowId);
////                            appDatabase.episodeDao().insert(episode);
////                        }
////
////                    }
//
////                    Episode e = tvShowSeasonDetails.getEpisodes().stream()
////                            .filter(episode1 -> (Integer.parseInt(finalEpisodeNumber) == episode1.getEpisode_number()))
////                            .findAny()
////                            .orElse(null);
//
//
////                    Call<TVShowSeasonDetails> tvShowSeasonDetailsCall = TMDBApi.getTMDBService().getSeasonByTVId(tvShowId, finalSeasonNumber);
////                    tvShowSeasonDetailsCall.enqueue(new Callback<TVShowSeasonDetails>() {
////                        @Override
////                        public void onResponse(@NonNull Call<TVShowSeasonDetails> call, @NonNull Response<TVShowSeasonDetails> response) {
////                            tvShowSeasonDetails = response.body();
////                        }
////
////                        @Override
////                        public void onFailure(@NonNull Call<TVShowSeasonDetails> call, @NonNull Throwable t) {
////                            Log.d("getTVSeasonById2", "Failure");
////                        }
////                    });
//
//
////sendGetTVShow
//
////                    finalurl = TMDB_BASE_URL + "search/tv?api_key=" + TMDB_API_KEY + "&language=en-US&page=1&include_adult=false&query=" + URLEncoder.encode(finalShowName, "UTF-8");
////
////                    System.out.println("finalUrl in sendGetTVShow" + finalurl);
////
////                    URL url = new URL(finalurl);
////                    Log.i("Show title", finalurl);
////                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                    con.setRequestMethod("GET");
////                    int responseCode = con.getResponseCode();
////                    Log.i("tmdb api response", String.valueOf(responseCode));
////                    if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                        BufferedReader in = new BufferedReader(new InputStreamReader(
////                                con.getInputStream()));
////                        String inputLine;
////                        response = new StringBuilder();
////                        while ((inputLine = in.readLine()) != null) {
////                            response.append(inputLine);
////                        }
////                        in.close();
////                    } else {
////                        System.out.println("GET request did not work");
////                    }
////                    Gson gson = new Gson();
////                    TVShowsResponseFromTMDB tvShowsResponseFromTMDB = gson.fromJson(response.toString(), TVShowsResponseFromTMDB.class);
//
////searchMovieOnTmdb by name
//
////            String finalUrl = TMDB_BASE_URL + "search/movie?api_key=" + TMDB_API_KEY + "&year=" + yearExtracted + "&language=en-US&page=1&include_adult=false&query=" + URLEncoder.encode(titleExtracted, "UTF-8");
////            URL url = new URL(finalUrl);
////            System.out.println("TMDB GET REQUEST URL INSIDE searchMovieOnTmdbByName " + finalUrl);
////            HttpURLConnection con = (HttpURLConnection) url.openConnection();
////            con.setRequestMethod("GET");
////            int responseCode = con.getResponseCode();
////            System.out.println("TMDB RESPONSE CODE" + responseCode);
////            if (responseCode == HttpURLConnection.HTTP_OK) {
////                BufferedReader in = new BufferedReader(new InputStreamReader(
////                        con.getInputStream()));
////                String inputLine;
////                response = new StringBuilder();
////                while ((inputLine = in.readLine()) != null) {
////                    response.append(inputLine);
////                }
////                in.close();
////            } else {
////                System.out.println("GET request did not work");
////            }
//
////getMovieByIf=d
////        StringBuilder responseById = new StringBuilder();
////        try {
////            String TMDB_MOVIE_BY_ID_URL = TMDB_BASE_URL + "movie/" + id + "?api_key=" + TMDB_API_KEY + "&language=en-US";
////            URL url = new URL(TMDB_MOVIE_BY_ID_URL);
////            HttpURLConnection con = (HttpURLConnection) url.openConnection();
////            con.setRequestMethod("GET");
////            int responseCode = con.getResponseCode();
////            Log.i("tmdb api response code", String.valueOf(responseCode));
////            if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                BufferedReader in = new BufferedReader(new InputStreamReader(
////                        con.getInputStream()));
////                String inputLine;
////                responseById = new StringBuilder();
////                while ((inputLine = in.readLine()) != null) {
////                    responseById.append(inputLine);
////                }
////                in.close();
////            } else {
////                System.out.println("GET request did not work");
////            }
////        } catch (IOException | JsonSyntaxException e) {
////            e.printStackTrace();
////        }
//
////getvByid
////            String finalurl = TMDB_BASE_URL + "tv/" + tvShowId + "?api_key=" + TMDB_API_KEY + "&language=en-US";
////            StringBuilder responseTvShowDetails = new StringBuilder();
////            try {
////                URL url = new URL(finalurl);
////                Log.i("Show title", finalurl);
////                HttpURLConnection con = (HttpURLConnection) url.openConnection();
////                con.setRequestMethod("GET");
////                int responseCode = con.getResponseCode();
////                Log.i("tmdb api response", String.valueOf(responseCode));
////                if (responseCode == HttpURLConnection.HTTP_OK) { // success
////                    BufferedReader in = new BufferedReader(new InputStreamReader(
////                            con.getInputStream()));
////                    String inputLine;
////                    responseTvShowDetails = new StringBuilder();
////                    while ((inputLine = in.readLine()) != null) {
////                        responseTvShowDetails.append(inputLine);
////                    }
////                    in.close();
////                } else {
////                    System.out.println("GET request did not work");
////                }
////                Log.i("tvShowById", responseTvShowDetails.toString());
////                long tvdb_id = getTVDBId(tvShowId);
////                Gson gson = new Gson();
////                tvShow = gson.fromJson(responseTvShowDetails.toString(), TVShow.class);
//
////                String logoPath = getLogo(tvdb_id,true);
////                String logoPath = getLogoTMDB(tvShowId, true);
////
////                if (logoPath.equals("")) {
////                    tvdb_id = getTVDBId(tvShowId);
////                    logoPath = getLogoFanArt(tvdb_id, true);
////                }
//
////                tvShow.setLogo_path(logoPath);
////
////                Log.i("tvShow", tvShow.toString());
////
////                appDatabase.tvShowDao().insert(tvShow);
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
