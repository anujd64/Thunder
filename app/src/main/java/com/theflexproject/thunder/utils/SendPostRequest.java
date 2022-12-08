package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.Constants.CF_CACHE_TOKEN;
import static com.theflexproject.thunder.Constants.SIMPLE_PROGRAM_DOWNLOAD_API;
import static com.theflexproject.thunder.MainActivity.context;
import static com.theflexproject.thunder.utils.SendGetRequestTMDB.sendGet2;
import static com.theflexproject.thunder.utils.SendGetRequestTMDB.sendGetTVShow;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.ResFormat;
import com.theflexproject.thunder.model.SimpleLink;
import com.theflexproject.thunder.model.TVShowInfo.Episode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SendPostRequest {

    private static String nextPageToken = "";
    private static int pageIndex = 0;
//    private static int retryLimit = 3;


    public static void postRequestGDIndex(String urlString , String user , String pass , boolean isTVShow , int index_id) {
        try {
            if (urlString.charAt(urlString.length() - 1) != '/') {
                urlString += '/';
            }

            URL url = new URL(urlString);

            String user_pass = user + ":" + pass;
            byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);

            String authHeaderValue;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
            } else {
                authHeaderValue = "Basic " + Arrays.toString(android.util.Base64.encode(user_pass_array , android.util.Base64.DEFAULT));// Unresolved reference: decode
            }

//        String authHeaderValue = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);

            System.out.println(authHeaderValue);


            Log.i("token" , authHeaderValue);
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("authorization" , authHeaderValue);
            params.put("page_token" , nextPageToken);
            params.put("page_index" , pageIndex);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey() , "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()) , "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = null;

            boolean failed = false;


            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length" , String.valueOf(postDataBytes.length));
            conn.setRequestProperty("authorization" , authHeaderValue);
//            conn.setRequestProperty("Referer" , url.toString());
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
            try {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));

            } catch (FileNotFoundException e) {
                failed = true;
            }

            int code = conn.getResponseCode();
            System.out.println(("HTTP CODE" + code));

            int tryCount = 0;
            while ((code != 200 || failed) && tryCount < 5) {
                System.out.println("Request is not successful - " + tryCount);
                tryCount++;
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type" , "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length" , String.valueOf(postDataBytes.length));
                conn.setRequestProperty("authorization" , authHeaderValue);
//            conn.setRequestProperty("Referer" , url.toString());
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                    code = conn.getResponseCode();
                    System.out.println(("HTTP CODE" + code));
                } catch (FileNotFoundException e) {
                    failed = true;
                    System.out.println("Failed again while retrying " + e);
                }
            }


            if (br != null) {
                StringBuilder sb = new StringBuilder();
                for (int c; (c = br.read()) >= 0; )
                    sb.append((char) c);
                br.close();


                System.out.println("Undecrypted response" + sb.toString());

                if (sb.length() > 0 && sb.toString().contains("window.drive_names")) {
                    String res = sb.substring(sb.indexOf("window.drive_names = JSON.parse('[") + 34 , sb.indexOf("]"));
                    res = res.replaceAll("\"" , "");
                    String[] strings = res.split(",");
                    System.out.println(Arrays.toString(strings));

                    for (int i = 0; i < strings.length; i++) {

                        postRequestGDIndex(urlString + i + ":/" , user , pass , isTVShow , index_id);

                    }

                }

                //Only for gd index as the response in encrypted
                StringBuilder reverseSb = sb.reverse();
                String encodedString = reverseSb.substring(24 , reverseSb.length() - 20);

                byte[] decodedBytes;


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    decodedBytes = Base64.getDecoder().decode(encodedString);
                } else {
                    decodedBytes = android.util.Base64.decode(encodedString , android.util.Base64.DEFAULT);// Unresolved reference: decode
                }

//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);


                String decodedString = new String(decodedBytes , StandardCharsets.UTF_8);

                if(!decodedString.equals("undefined")){


                System.out.println("Response from GD Index" + decodedString);
                Gson gson = new Gson();
                ResFormat target = gson.fromJson(decodedString , ResFormat.class);

                List<String> folders = new ArrayList<>();
                List<File> files = target.getData().getFiles();

                DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

//                if (files == null) {
//                    while (retryLimit >= 0) {
//                        postRequestGDIndex(urlString , user , pass , isTVShow , index_id);
//                        retryLimit--;
//                    }
//                }

                //send files to tmdb and insert them to db
                if (!isTVShow) {
                    checkFilesAndSendToTMDBMovie(folders , files , urlString , index_id);
                } else {
                    checkFilesAndSendToTMDBTVShow(folders , files , urlString , index_id);
                }


                if (target.nextPageToken != null) {
                    nextPageToken = target.getNextPageToken();
                    pageIndex++;
                    postRequestGDIndex(urlString , user , pass , isTVShow , index_id);
                }
                for (int i = 0; i < folders.size(); i++) {
                    pageIndex = 0;
                    nextPageToken = "";
                    postRequestGDIndex(folders.get(i) , user , pass , isTVShow , index_id);
                    Log.i("Folder" , folders.get(i));
                }

                }
            }
        } catch (IOException e) {
            System.out.println("IOExceptio Occured in gdIndex");
        }

    }

    public static void postRequestGoIndex(String urlString , String user , String pass , boolean isTVShow , int index_id) {
        try {

            if (urlString.charAt(urlString.length() - 1) != '/') {
                urlString += '/';
            }

            URL url = new URL(urlString);

            String user_pass = user + ":" + pass;
            byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
            String authHeaderValue;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
            } else {
                authHeaderValue = "Basic " + Arrays.toString(android.util.Base64.encode(user_pass_array , android.util.Base64.DEFAULT));// Unresolved reference: decode
            }

//        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);

            System.out.println(authHeaderValue);

            String json = "{ \"q\":\"\",\"password\": null , \"page_index\":" + pageIndex + "}";
            byte[] postDataBytes = json.getBytes(StandardCharsets.UTF_8);

            BufferedReader br = null;

            boolean failed = false;

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            try {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type" , "application/json");
                conn.setRequestProperty("Accept" , "application/json");
                conn.setRequestProperty("Authorization" , authHeaderValue);
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
            } catch (FileNotFoundException e) {
                failed = true;
                System.out.println("Failed in the initial attempt " + e);
            }

            int code = conn.getResponseCode();
            System.out.println(("HTTP CODE" + code));


            int tryCount = 0;
            while ((code != 200 || failed) && tryCount < 10) {

                System.out.println("Request is not successful - " + tryCount);
                tryCount++;
//            conn.getInputStream().close();
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type" , "application/json");
                conn.setRequestProperty("Accept" , "application/json");
                conn.setRequestProperty("Authorization" , authHeaderValue);
                conn.setDoOutput(true);
                conn.getOutputStream().write(postDataBytes);
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                } catch (FileNotFoundException e) {
                    failed = true;
                    System.out.println("Failed in the retry attempt" + e);
                }
                code = conn.getResponseCode();
                System.out.println(("HTTP CODE" + code));
            }

            if (br != null) {
                StringBuilder sb = new StringBuilder();
                for (int c; (c = br.read()) >= 0; )
                    sb.append((char) c);
                br.close();
                System.out.println("" + sb);
                Gson gson = new Gson();
                ResFormat target = gson.fromJson(String.valueOf(sb) , ResFormat.class);

                List<String> folders = new ArrayList<>();
                List<File> files = target.getData().getFiles();

//                if (files == null) {
//                    while (retryLimit >= 0) {
//                        postRequestGoIndex(urlString , user , pass , isTVShow , index_id);
//                        retryLimit--;
//                    }
//                }

                DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

                if (!isTVShow) {
                    checkFilesAndSendToTMDBMovie(folders , files , urlString , index_id);
                } else {
                    checkFilesAndSendToTMDBTVShow(folders , files , urlString , index_id);
                }


                if (target.nextPageToken != null) {
                    nextPageToken = target.nextPageToken;
                    pageIndex++;
                    postRequestGoIndex(urlString , user , pass , isTVShow , index_id);
                }

                for (int i = 0; i < folders.size(); i++) {
                    pageIndex = 0;
                    nextPageToken = "";
                    postRequestGoIndex(folders.get(i) , user , pass , isTVShow , index_id);
                    Log.i("Folder" , folders.get(i));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in Goindex");
        }


    }

    public static void postRequestMapleIndex(String urlString , String user , String pass , boolean isTVShow , int index_id) {

        try {
            if (urlString.charAt(urlString.length() - 1) != '/') {
                urlString += '/';
            }
            urlString += "?rootId=root";

            URL url = new URL(urlString);

            String user_pass = user + ":" + pass;
            byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);

            String authHeaderValue;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
            } else {
                authHeaderValue = "Basic " + Arrays.toString(android.util.Base64.encode(user_pass_array , android.util.Base64.DEFAULT));// Unresolved reference: decode
            }
//        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);

            System.out.println(authHeaderValue);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = null;

            boolean failed = false;

            try {
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type" , "application/json");
                conn.setRequestProperty("Accept" , "application/json");
                conn.setRequestProperty("Authorization" , authHeaderValue);
                conn.setDoOutput(true);
                br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));

            } catch (FileNotFoundException e) {
                failed = true;
                System.out.println("Failed in the initial attempt " + e);
            }

            int code = conn.getResponseCode();
            System.out.println(("HTTP CODE" + code));


            int tryCount = 0;
            while ((code != 200 || failed) && tryCount < 5) {

                System.out.println("Request is not successful - " + tryCount);
                tryCount++;
//            conn.getInputStream().close();
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type" , "application/json");
                conn.setRequestProperty("Accept" , "application/json");
                conn.setRequestProperty("Authorization" , authHeaderValue);
                conn.setDoOutput(true);
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                } catch (FileNotFoundException e) {
                    failed = true;
                    System.out.println("Failed in the retry attempt " + e);
                }
            }

            if (br != null) {
                StringBuilder sb = new StringBuilder();
                for (int c; (c = br.read()) >= 0; )
                    sb.append((char) c);
                br.close();

                System.out.println("Response from Maple Index" + sb);
                Gson gson = new Gson();
                ResFormat target = gson.fromJson(sb.toString() , ResFormat.class);

                List<String> folders = new ArrayList<>();
                List<File> files = target.getData().getFiles();

//                if (files == null) {
//
//                    while (retryLimit >= 0) {
//                        postRequestMapleIndex(urlString , user , pass , isTVShow , index_id);
//                        retryLimit--;
//                    }
//                }

                DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

                if (!isTVShow) {
                    checkFilesAndSendToTMDBMovie(folders , files , urlString , index_id);
                } else {
                    checkFilesAndSendToTMDBTVShow(folders , files , urlString , index_id);
                }


                if (target.nextPageToken != null) {
                    nextPageToken = target.getNextPageToken();
                    pageIndex++;
                    postRequestMapleIndex(urlString , user , pass , isTVShow , index_id);
                }

                for (int i = 0; i < folders.size(); i++) {
                    pageIndex = 0;
                    nextPageToken = "";
                    postRequestMapleIndex(folders.get(i) , user , pass , isTVShow , index_id);
                    Log.i("Folder" , folders.get(i));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in maple");
        }


    }

    public static void postRequestSimpleProgramIndex(String urlString , String user , String pass , boolean isTVShow , int index_id) {

        try {

            if (urlString.charAt(urlString.length() - 1) != '/') {
                urlString += '/';
            }

            Map<String, Object> params = new LinkedHashMap<>();
            params.put("page_token" , nextPageToken);
            params.put("page_index" , pageIndex);
            params.put("password" , "");


            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey() , "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()) , "UTF-8"));
            }

            String s = "?" + postData;

            System.out.println("inside simple" + urlString);
            URL url = new URL(urlString + s);


            System.out.println("Url built with params" + postData);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            BufferedReader br = null;

            boolean failed = false;

            try {
                conn.setRequestMethod("GET");
                conn.setRequestProperty("cf_cache_token" ,CF_CACHE_TOKEN);
                br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));

            } catch (FileNotFoundException e) {
                failed = true;
                System.out.println("Failed in the initial attempt " + e);
            }
            int code = conn.getResponseCode();
            System.out.println(("HTTP CODE" + code));

            int tryCount = 0;
            while ((code != 200 || failed) && tryCount < 5) {

                System.out.println("Request is not successful - " + tryCount);
                tryCount++;
//            conn.getInputStream().close();
                conn.disconnect();
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("cf_cache_token" , "UKsVpQqBMxB56gBfhYKbfCVkRIXMh42pk6G4DdkXXoVh7j4BjV");
                try {
                    br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
                } catch (FileNotFoundException e) {
                    failed = true;
                    System.out.println("Failed in the retry attempt " + e);
                }
            }


            if (br != null) {
                StringBuilder sb = new StringBuilder();
                for (int c; (c = br.read()) >= 0; )
                    sb.append((char) c);
                br.close();

                System.out.println("Response from GD Index" + sb);
                Gson gson = new Gson();
                ResFormat target = gson.fromJson(sb.toString() , ResFormat.class);


                List<String> folders = new ArrayList<>();
                List<File> files = target.getData().getFiles();


                DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);


                //send files to tmdb and insert them to db
                if (!isTVShow) {
                    checkFilesAndSendToTMDBMovie(folders , files , urlString , index_id);
                } else {
                    checkFilesAndSendToTMDBTVShow(folders , files , urlString , index_id);
                }


                if (target.nextPageToken != null) {
                    nextPageToken = target.getNextPageToken();
                    pageIndex++;
                    postRequestSimpleProgramIndex(urlString , user , pass , isTVShow , index_id);
                }
//                if (files == null) {
//                    while (retryLimit >= 0) {
//                        System.out.println("likely a worker exception occurred");
//                        postRequestSimpleProgramIndex(urlString , user , pass , isTVShow , index_id);
//                        retryLimit--;
//                    }
//                }
                for (int i = 0; i < folders.size(); i++) {
                    pageIndex = 0;
                    nextPageToken = "";
                    postRequestSimpleProgramIndex(folders.get(i) , user , pass , isTVShow , index_id);
                    Log.i("Folder" , folders.get(i));
                }
            }
        } catch (IOException e) {
            System.out.println("Exception in simple");
        }


    }

    private static void checkFilesAndSendToTMDBMovie(List<String> folders , List<File> files , String urlString , int index_id) {
        Gson gson = new Gson();
        if (files != null) {
            try {
                System.out.println("Files from the index" + files);
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")) {
                        boolean test = isAlreadyPresent(file.getId() , file.getModifiedTime());

                        if (!test) {
                            System.out.println("the file is not present");
                            String callingMethodName = Thread.currentThread().getStackTrace()[3].getMethodName();
                            System.out.println("Calling method name " + callingMethodName);
                            if (callingMethodName.equals("postRequestSimpleProgramIndex")) {
                                file.setUrlstring(generateDownloadLinkSimpleProgram(file.getId()));
                            } else {
                                file.setUrlstring(urlString + file.getName());
                            }


                            System.out.println("File to string" +file.toString());


                            try {
                                JsonReader reader = new JsonReader(new StringReader(file.toString()));
                                reader.setLenient(true);
                                Movie movie = gson.fromJson(reader, Movie.class);

//                            Movie movie = gson.fromJson(file.toString() , Movie.class);
                                movie.setFileName(file.getName());
                                movie.setGd_id(file.getId());
                                movie.setIndex_id(index_id);

//                            long modifiedTimeLong = dateToLong(file.getModifiedTime());

                                movie.setModifiedTime(file.getModifiedTime());
//                            movie.setModifiedTime(modifiedTimeLong);

                                sendGet2(movie);//tmdbrequest
                                Log.i("Movie" , movie.toString());
                            }catch (Exception e){
                                System.out.println("Malformed Json in checkFilesAndSendToTMDBMovie"+e.toString());
                            }
                        }

                    } else if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                        folders.add(urlString + file.getName() + "/");
                    }
                }
            } catch (NullPointerException e) {
                Log.i("Exception null pointer" , e.toString());
            }

        }

    }

    private static boolean isAlreadyPresent(String id , Date modifiedTime) {
        System.out.println("id to test " + id);
        Movie movie = DatabaseClient.getInstance(context).getAppDatabase().movieDao().getByGdId(id);
        Episode episode = DatabaseClient.getInstance(context).getAppDatabase().episodeDao().findByGdId(id);
        if (movie != null && modifiedTime.after(movie.getModifiedTime())) {
            DatabaseClient.getInstance(context).getAppDatabase().movieDao().deleteByGdId(id);
            return false;
        }
        if (episode != null && modifiedTime.after(episode.getModifiedTime())) {
            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().deleteByGdId(id);
            return false;
        }
        if (movie == null && episode == null) {
            System.out.println("not present in movies");
            return false;
        } else {
            return true;
        }
    }

    private static String generateDownloadLinkSimpleProgram(String id) {
        try {
            URL url = new URL(SIMPLE_PROGRAM_DOWNLOAD_API+ id);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            System.out.println(("HTTP CODE" + code));

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream() , StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = br.read()) >= 0; )
                sb.append((char) c);
            br.close();

            Gson gson = new Gson();
            SimpleLink target = gson.fromJson(sb.toString() , SimpleLink.class);
            System.out.println("Response from link generator" + target);

            return target.getUrl();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    private static void checkFilesAndSendToTMDBTVShow(List<String> showFolders , List<File> files , String urlString , int index_id) {
        Gson gson = new Gson();
        if (files != null) {
            try {
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")) {

                        boolean test = isAlreadyPresent(file.getId() , file.getModifiedTime());

                        if (!test) {
                            String callingMethodName = Thread.currentThread().getStackTrace()[3].getMethodName();
                            if (callingMethodName.equals("postRequestSimpleProgramIndex")) {
                                file.setUrlstring(generateDownloadLinkSimpleProgram(file.getId()));
                            } else {
                                file.setUrlstring(urlString + file.getName());
                            }
                            Episode episode = gson.fromJson(file.toString() , Episode.class);
                            episode.setFileName(file.getName());

                            episode.setModifiedTime(file.getModifiedTime());
                            episode.setGd_id(file.getId());
                            episode.setIndex_id(index_id);
                            System.out.println("episode before tmdb" + episode);
                            sendGetTVShow(episode);//tmdbrequest
                        }


                    } else if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                        showFolders.add(urlString + file.getName() + "/");
                        Log.i("showFolders" , showFolders + file.getName());
                    }
                }
            } catch (NumberFormatException e) {
                Log.i("Exception" , e.toString());
            }
        }

    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void postRequestGDIndexTVShow(String urlString , String user , String pass) throws IOException {
//
//        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}
//
//        URL url = new URL(urlString);
//
//        String user_pass = user+":"+pass;
//        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
//        String authHeaderValue = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);
//
//        System.out.println(authHeaderValue);
//
//
//        Map<String,Object> params = new LinkedHashMap<>();
//        params.put("authorization", authHeaderValue);
//        params.put("page_token", nextPageToken);
//        params.put("page_index", pageIndex);
//
//        StringBuilder postData = new StringBuilder();
//        for (Map.Entry<String,Object> param : params.entrySet()) {
//            if (postData.length() != 0) postData.append('&');
//            postData.append(URLEncoder.encode(param.getKey(), "UTF-8" ));
//            postData.append('=');
//            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),  "UTF-8"));
//        }
//        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
//
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//        conn.setRequestProperty("authorization",authHeaderValue);
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
//
//
//        int code = conn.getResponseCode();
//        System.out.println(("HTTP CODE" + code));
//
//        /* Infinite recursion not a good solution */
//        if(code==500){
//            postRequestGDIndexTVShow(urlString,user,pass);
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
//        StringBuilder sb = new StringBuilder();
//        for (int c; (c = br.read()) >= 0;)
//            sb.append((char)c);
//        br.close();
//
//        //Only for gdindex as the response in encrypted
//        StringBuilder reverseSb = sb.reverse();
//        String encodedString = reverseSb.substring(24,reverseSb.length()-20);
//        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
//        String decodedString = new String(decodedBytes,StandardCharsets.UTF_8);
//
//        System.out.println("Response from GD Index"+decodedString);
//        Gson gson = new Gson();
//        ResFormat target = gson.fromJson(decodedString, ResFormat.class);
//
//        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);
//
//        List<String> showFolders = new ArrayList<>();
//        List<File> files = target.getData().getFiles();
//
//        checkFilesAndSendToTMDBTVShow(showFolders,files,url);
//
//        if(target.nextPageToken!=null){
//            nextPageToken =target.getNextPageToken();
//            pageIndex++;
//            postRequestGDIndexTVShow(urlString,user,pass);
//        }
//        for (int i = 0; i < showFolders.size(); i++) {
//            pageIndex = 0;
//            nextPageToken = "";
//            postRequestGDIndexTVShow(showFolders.get(i),user,pass);
//            Log.i("Folder", showFolders.get(i));
//        }
//
//
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void postRequestGoIndexTVShow(String urlString, String user, String pass) throws IOException, JSONException {
//        if (urlString.charAt(urlString.length() - 1) != '/') {urlString += '/';}
//
//        URL url = new URL(urlString);
//
//        String user_pass = user + ":" + pass;
//        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
//        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
//
//        System.out.println(authHeaderValue);
//
//        String json = "{ \"q\":\"\",\"password\": null , \"page_index\":" + pageIndex + "}";
//        byte[] postDataBytes = json.getBytes(StandardCharsets.UTF_8);
//
//
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("Authorization", authHeaderValue);
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
//
//        int code = conn.getResponseCode();
//        System.out.println(("HTTP CODE" + code));
//
//        /* Infinite recursion not a good solution */
//        if(code==500){
//            postRequestGoIndexTVShow(urlString,user,pass);
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(), StandardCharsets.UTF_8));
//
//        StringBuilder sb = new StringBuilder();
//        for (int c; (c = br.read()) >= 0;)
//            sb.append((char)c);
//        br.close();
//        System.out.println(""+ sb);
//        Gson gson = new Gson();
//        ResFormat target = gson.fromJson(String.valueOf(sb), ResFormat.class);
//
//        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);
//
//        List<String> folders = new ArrayList<>();
//        List<File> files = target.getData().getFiles();
//
//
//        checkFilesAndSendToTMDBTVShow(folders,files,url);
//
//
//
//
//        if(target.nextPageToken!=null){
//            nextPageToken =target.nextPageToken;
//            pageIndex++;
//            postRequestGoIndexTVShow(urlString,user,pass);
//        }
//
//        for (int i = 0; i < folders.size(); i++) {
//            pageIndex = 0;
//            nextPageToken = "";
//            postRequestGoIndexTVShow(folders.get(i),user,pass);
//            Log.i("Folder", folders.get(i));
//        }
//
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void postRequestMapleIndexTVShow(String urlString, String user, String pass) throws IOException {
//
//        if (urlString.charAt(urlString.length() - 1) != '/') {urlString += '/';}
//        urlString +="?rootId=root";
//
//        URL url = new URL(urlString);
//
//        String user_pass = user + ":" + pass;
//        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
//        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);
//
//        System.out.println(authHeaderValue);
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/json");
//        conn.setRequestProperty("Accept", "application/json");
//        conn.setRequestProperty("Authorization", authHeaderValue);
//        conn.setDoOutput(true);
//
//
//        int code = conn.getResponseCode();
//        System.out.println(("HTTP CODE" + code));
//
//        /* Infinite recursion not a good solution */
//        if(code==500){
//            postRequestMapleIndexTVShow(urlString,user,pass);
//        }
//
//        BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(), StandardCharsets.UTF_8));
//        StringBuilder sb = new StringBuilder();
//        for (int c; (c = br.read()) >= 0;)
//            sb.append((char)c);
//        br.close();
//
//        System.out.println("Response from Maple Index"+ sb);
//        Gson gson = new Gson();
//        ResFormat target = gson.fromJson(sb.toString(), ResFormat.class);
//
//        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);
//
//        List<String> folders = new ArrayList<>();
//        List<File> files = target.getData().getFiles();
//
//        checkFilesAndSendToTMDBTVShow(folders,files,url);
//
//        if(target.nextPageToken!=null){
//            nextPageToken =target.getNextPageToken();
//            pageIndex++;
//            postRequestMapleIndexTVShow(urlString,user,pass);
//        }
//
//        for (int i = 0; i < folders.size(); i++) {
//            pageIndex = 0;
//            nextPageToken = "";
//            postRequestMapleIndexTVShow(folders.get(i),user,pass);
//            Log.i("Folder", folders.get(i));
//        }
//
//
//
//    }


//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void postRequestGoExtendedIndex(String urlString, String user, String pass) throws IOException {
//        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}
//        URL url = new URL(urlString);
//        String user_pass = user+":"+pass;
//        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
//        String token = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);
//
//        Log.i("token",token);
//        Map<String,Object> params = new LinkedHashMap<>();
//        params.put("authorization", token);
//        params.put("page_token", nextPageToken);
//        params.put("page_index", pageIndex);
//
//
//        StringBuilder postData = new StringBuilder();
//        for (Map.Entry<String,Object> param : params.entrySet()) {
//            if (postData.length() != 0) postData.append('&');
//            postData.append(URLEncoder.encode(param.getKey(), "UTF-8" ));
//            postData.append('=');
//            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),  "UTF-8"));
//        }
//        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);
//
//        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
//        conn.setRequestProperty("authorization",token);
//        conn.setDoOutput(true);
//        conn.getOutputStream().write(postDataBytes);
//
//
//        int code = conn.getResponseCode();
//        System.out.println(("HTTP CODE" + String.valueOf(code)));
//
//        /** Infinite recursion not a good solution */
//        if(code==500){
//            postRequestGDIndex(urlString,user,pass);
//        }
//
//        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//
//        StringBuilder sb = new StringBuilder();
//        for (int c; (c = in.read()) >= 0;)
//            sb.append((char)c);
//
//
//        Gson gson = new Gson();
//        ResFormat target = gson.fromJson(sb.toString(), ResFormat.class);
//
//        try{
//            Log.i("Error",sb.toString());
//        }catch (NullPointerException w){ w.toString();}
//
//        DatabaseClient.getInstance(mCtx).getAppDatabase().resFormatDao().insert(target);
//
//        List<String> folders = new ArrayList<>();
//
//        try{
//            for (int i = 0; i < target.data.files.size(); i++) {
//                File file = target.data.files.get(i);
//                if(file.getMimeType().equals("video/x-matroska")
//                        ||file.getMimeType().equals("video/mp4")
//                        || file.getMimeType().equals("video/x-msvideo")
//                        || file.getMimeType().equals("video/mpeg")
//                        || file.getMimeType().equals("video/webm")){
//                    file.setUrlstring(url+file.getName());
//                    sendGet2(file);
//                    DatabaseClient.getInstance(mCtx).getAppDatabase().fileDao().insert(target.data.files.get(i));
//                }else if( file.getMimeType().equals("application/vnd.google-apps.folder")){
//                    folders.add(url+file.getName()+"/");
//                }
//            }
//        }catch (NullPointerException e){Log.i("Exception",e.toString());}
//        Log.i("Folder",folders.toString());
//        if(target.nextPageToken!=null){
//            nextPageToken =target.nextPageToken;
//            pageIndex++;
//            postRequestGDIndex(urlString,user,pass);
//        }
//        for (int i = 0; i < folders.size(); i++) {
//            pageIndex = 0;
//            nextPageToken = "";
//            postRequestGDIndex(folders.get(i),user,pass);
//            Log.i("Folder",folders.get(i).toString());
//        }
//
//
//
//    }

}
