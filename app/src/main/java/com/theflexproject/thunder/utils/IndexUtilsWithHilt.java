//package com.theflexproject.thunder.utils;
//
//import com.theflexproject.thunder.database.AppDatabase;
//import com.theflexproject.thunder.model.IndexLink;
//import com.theflexproject.thunder.model.TVShowInfo.Episode;
//import com.theflexproject.thunder.model.TVShowInfo.TVShow;
//import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
//
//import java.util.List;
//
//import dagger.hilt.android.AndroidEntryPoint;
//
//
//@AndroidEntryPoint
//public class IndexUtilsWithHilt{
//
//    AppDatabase appDatabase;
//    SendPostRequest sendPostRequest;
//
//    public IndexUtils(AppDatabase appDatabase, SendPostRequest sendPostRequest) {
//        this.appDatabase = appDatabase;
//        this.sendPostRequest = sendPostRequest;
//    }
//
//    public void refreshIndex(IndexLink indexLink) {
//        Thread thread = null;
////        if(!deleteIndex(indexLink)){
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String folderType = indexLink.getFolderType();
//                String indexType = indexLink.getIndexType();
//
//                String link = indexLink.getLink();
//                String user = indexLink.getUsername();
//                String pass = indexLink.getPassword();
//
//                System.out.println("Before setting id" + indexLink.getId());
//
////                    int id = indexLink.getId();
////                    DatabaseClient.getInstance(context).getAppDatabase().indexLinksDao().deleteById(indexLink.getId());
//
//                IndexLink indexLinkAgain = appDatabase.indexLinksDao().find(link);
//                if (indexLinkAgain == null) {
//                    appDatabase.indexLinksDao().insert(indexLink);
//                }
//                int id = indexLinkAgain.getId();
//
//                System.out.println("After setting id" + indexLinkAgain.getId());
//
//
//                if (folderType.equals("Movies")) {
//                    if (indexType.equals("GDIndex")) {
//                        sendPostRequest.postRequestGDIndex(link, user, pass, false, id);
//                    }
//                    if (indexType.equals("GoIndex")) {
//                        sendPostRequest.postRequestGoIndex(link, user, pass, false, id);
//                    }
//                    if (indexType.equals("MapleIndex")) {
//                        sendPostRequest.postRequestMapleIndex(link, user, pass, false, id);
//                    }
//                    if (indexType.equals("SimpleProgram")) {
//                        sendPostRequest.postRequestSimpleProgramIndex(link, user, pass, false, id);
//                    }
//                }
//
//                if (folderType.equals("TVShows")) {
//                    if (indexType.equals("GDIndex")) {
//                        sendPostRequest.postRequestGDIndex(link, user, pass, true, id);
//                    }
//                    if (indexType.equals("GoIndex")) {
//                        sendPostRequest.postRequestGoIndex(link, user, pass, true, id);
//                    }
//                    if (indexType.equals("MapleIndex")) {
//                        sendPostRequest.postRequestMapleIndex(link, user, pass, true, id);
//                    }
//                    if (indexType.equals("SimpleProgram")) {
//                        sendPostRequest.postRequestSimpleProgramIndex(link, user, pass, true, id);
//                    }
//                }
//
//            }
//        });
//        thread.start();
//        thread.isAlive();
////        }
////        return thread.isAlive();
//    }
//
//    public boolean deleteIndex( IndexLink indexLink) {
//        Thread thread = new Thread(() -> {
//            if (indexLink.getFolderType().equals("Movies")) {
//                appDatabase.movieDao().deleteAllFromthisIndex(indexLink.getId());
//            }
//            if (indexLink.getFolderType().equals("TVShows")) {
//                appDatabase.episodeDao().deleteAllFromThisIndex(indexLink.getId());
//
//
//                List<TVShowSeasonDetails> seasonsList = appDatabase.tvShowSeasonDetailsDao().getAll();
//
//                for (TVShowSeasonDetails season : seasonsList) {
//                    List<Episode> episodeList = appDatabase.episodeDao().getFromSeasonOnly(season.getId());
//                    if (episodeList == null || episodeList.size() == 0) {
//                        appDatabase.tvShowSeasonDetailsDao().deleteById(season.getId());
//                    }
//                }
//
//                List<TVShow> tvShowList = appDatabase.tvShowDao().getAll();
//
//                for (TVShow tvShow : tvShowList) {
//                    List<TVShowSeasonDetails> seasonsInThisShow = appDatabase.tvShowSeasonDetailsDao().findByShowId(tvShow.getId());
//                    if (seasonsInThisShow == null || seasonsInThisShow.size() == 0) {
//                        appDatabase.tvShowDao().deleteById(tvShow.getId());
//                    }
//                }
//
//            }
//            appDatabase.indexLinksDao().deleteById(indexLink.getId());
//        });
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            System.out.println(e.toString());
//        }
//
//        return thread.isAlive();
//    }
//
//    int noOfMedia = 0;
//
//    public int getNoOfMedia( IndexLink t) {
//
//        Thread thread = new Thread(() -> {
//            noOfMedia = 0;
//            if (t.getFolderType() != null && t.getFolderType().equals("Movies")) {
//                noOfMedia = appDatabase.movieDao().getNoOfMovies(t.getId());
//                System.out.println("noOfMedia after calculation" + noOfMedia);
//            }
//            if (t.getFolderType() != null && t.getFolderType().equals("TVShows")) {
//                noOfMedia = appDatabase.episodeDao().getNoOfShows(t.getId());
//                System.out.println("noOfMedia after calculation" + noOfMedia);
//            }
//        });
//        thread.start();
//
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("noOfMedia after calculation" + noOfMedia);
//
//
//        return noOfMedia;
//    }
//
//    public void disableIndex( IndexLink indexLink) {
//        Thread thread = new Thread(() -> {
//            if (indexLink.getFolderType().equals("Movies")) {
//                appDatabase.movieDao().disableFromThisIndex(indexLink.getId());
//            }
//            if (indexLink.getFolderType().equals("TVShows")) {
//                appDatabase.episodeDao().disableFromThisIndex(indexLink.getId());
//            }
//            appDatabase.indexLinksDao().disableIndex(indexLink.getId());
//
//        });
//        thread.start();
//
//    }
//
//    public void enableIndex( IndexLink indexLink) {
//        Thread thread = new Thread(() -> {
//            if (indexLink.getFolderType().equals("Movies")) {
//                appDatabase.movieDao().enableFromThisIndex(indexLink.getId());
//            }
//            if (indexLink.getFolderType().equals("TVShows")) {
//                appDatabase.episodeDao().enableFromThisIndex(indexLink.getId());
//                System.out.println("noOfMedia after calculation" + noOfMedia);
//            }
//            appDatabase.indexLinksDao().enableIndex(indexLink.getId());
//
//        });
//        thread.start();
//    }
//}
