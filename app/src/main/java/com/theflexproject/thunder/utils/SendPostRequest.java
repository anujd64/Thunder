package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.MainActivity.context;
import static com.theflexproject.thunder.utils.SendGetRequestTMDB.sendGet2;
import static com.theflexproject.thunder.utils.SendGetRequestTMDB.sendGetTVShow;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.ResFormat;
import com.theflexproject.thunder.model.TVShowInfo.Episode;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class SendPostRequest {

private static String nextPageToken = "";
private static int pageIndex = 0;
private static String user = "";
private static String pass = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void postRequestGDIndex(String urlString , String user , String pass) throws IOException {

        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}

        URL url = new URL(urlString);

        String user_pass = user+":"+pass;
        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
        String authHeaderValue = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);

        System.out.println(authHeaderValue);


        Log.i("token",authHeaderValue);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("authorization", authHeaderValue);
        params.put("page_token", nextPageToken);
        params.put("page_index", pageIndex);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8" ));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),  "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("authorization",authHeaderValue);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);


        int code = conn.getResponseCode();
        System.out.println(("HTTP CODE" + String.valueOf(code)));

        /** Infinite recursion not a good solution */
        if(code==500){
            postRequestGDIndex(urlString,user,pass);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = br.read()) >= 0;)
            sb.append((char)c);
        br.close();

        //Only for gdindex as the response in encrypted
        StringBuilder reverseSb = sb.reverse();
        String encodedString = reverseSb.substring(24,reverseSb.length()-20);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes,StandardCharsets.UTF_8);

        System.out.println("Response from GD Index"+decodedString);
        Gson gson = new Gson();
        ResFormat target = gson.fromJson(decodedString, ResFormat.class);

        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

        List<String> folders = new ArrayList<>();
        List<File> files = target.getData().getFiles();

        if(target.getData().getFiles()!=null){
            try{
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if(file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")){
                        file.setUrlstring(url+ file.getName());
                        Movie movie = gson.fromJson(file.toString(),Movie.class);
                        movie.setFileName(file.getName());
                        movie.setModifiedTime(file.getModifiedTime());
                        sendGet2(movie);//tmdbrequest
                        Log.i("Movie",movie.toString());
                    }else if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                        folders.add(url+ file.getName()+"/");
                    }
                }
            }catch (NullPointerException e){Log.i("Exception null pointer",e.toString());}

        }




        if(target.nextPageToken!=null){
            nextPageToken =target.getNextPageToken();
            pageIndex++;
            postRequestGDIndex(urlString,user,pass);
        }
        for (int i = 0; i < folders.size(); i++) {
            pageIndex = 0;
            nextPageToken = "";
            postRequestGDIndex(folders.get(i),user,pass);
            Log.i("Folder", folders.get(i));
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void postRequestGoIndex(String urlString, String user, String pass) throws IOException, JSONException {
        if (urlString.charAt(urlString.length() - 1) != '/') {urlString += '/';}

        URL url = new URL(urlString);

        String user_pass = user + ":" + pass;
        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);

        System.out.println(authHeaderValue);

        String json = "{ \"q\":\"\",\"password\": null , \"page_index\":" + pageIndex + "}";
        byte[] postDataBytes = json.getBytes(StandardCharsets.UTF_8);



        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", authHeaderValue);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        int code = conn.getResponseCode();
        System.out.println(("HTTP CODE" + String.valueOf(code)));

        /** Infinite recursion not a good solution */
        if(code==500){
            postRequestGoIndex(urlString,user,pass);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(),"utf-8"));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = br.read()) >= 0;)
            sb.append((char)c);
        br.close();
        System.out.println(""+sb.toString());
        Gson gson = new Gson();
        ResFormat target = gson.fromJson(String.valueOf(sb), ResFormat.class);

        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

        List<String> folders = new ArrayList<>();
        List<File> files = target.getData().getFiles();

        if(target.getData().getFiles() !=null){
            try{
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if(file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")){
                        file.setUrlstring(url+ file.getName());
                        Movie movie = gson.fromJson(file.toString(),Movie.class);
                        movie.setFileName(file.getName());
                        movie.setModifiedTime(file.getModifiedTime());
                        sendGet2(movie);//tmdbrequest
                        Log.i("Movie",movie.toString());
                    }else if(file.getMimeType().equals("application/vnd.google-apps.folder")){
                        folders.add(url+ file.getName()+"/");
                    }
                }
            }catch (NullPointerException e){Log.i("Exception",e.toString());}

        }



        if(target.nextPageToken!=null){
            nextPageToken =target.nextPageToken;
            pageIndex++;
            postRequestGoIndex(urlString,user,pass);
        }

        for (int i = 0; i < folders.size(); i++) {
            pageIndex = 0;
            nextPageToken = "";
            postRequestGoIndex(folders.get(i),user,pass);
            Log.i("Folder", folders.get(i));
        }

    }

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void postRequestMapleIndex(String urlString, String user, String pass) throws IOException {

        if (urlString.charAt(urlString.length() - 1) != '/') {urlString += '/';}
        urlString +="?rootId=root";

        URL url = new URL(urlString);

        String user_pass = user + ":" + pass;
        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(user_pass_array);

        System.out.println(authHeaderValue);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", authHeaderValue);
        conn.setDoOutput(true);


        int code = conn.getResponseCode();
        System.out.println(("HTTP CODE" + String.valueOf(code)));

        /** Infinite recursion not a good solution */
        if(code==500){
            postRequestMapleIndex(urlString,user,pass);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader( conn.getInputStream(),"utf-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = br.read()) >= 0;)
            sb.append((char)c);
        br.close();

        System.out.println("Response from Maple Index"+ sb);
        Gson gson = new Gson();
        ResFormat target = gson.fromJson(sb.toString(), ResFormat.class);

        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

        List<String> folders = new ArrayList<>();
        List<File> files = target.getData().getFiles();

        if(target.getData().getFiles()!=null) {
            try {
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")) {
                        file.setUrlstring(url + file.getName());
                        Movie movie = gson.fromJson(file.toString() , Movie.class);
                        movie.setFileName(file.getName());
                        movie.setModifiedTime(file.getModifiedTime());
                        sendGet2(movie);//tmdbrequest
                        Log.i("Movie" , movie.toString());
                        DatabaseClient.getInstance(context).getAppDatabase().movieDao().insert(movie);
                    } else if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                        folders.add(url + file.getName() + "/");
                    }
                }
            } catch (NullPointerException e) {
                Log.i("Exception" , e.toString());
            }
        }



        if(target.nextPageToken!=null){
            nextPageToken =target.getNextPageToken();
            pageIndex++;
            postRequestMapleIndex(urlString,user,pass);
        }

        for (int i = 0; i < folders.size(); i++) {
            pageIndex = 0;
            nextPageToken = "";
            postRequestMapleIndex(folders.get(i),user,pass);
            Log.i("Folder",folders.get(i).toString());
        }



    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void postRequestGDIndexTVShow(String urlString , String user , String pass) throws IOException {

        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}

        URL url = new URL(urlString);

        String user_pass = user+":"+pass;
        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
        String authHeaderValue = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);

        System.out.println(authHeaderValue);


        Map<String,Object> params = new LinkedHashMap<>();
        params.put("authorization", authHeaderValue);
        params.put("page_token", nextPageToken);
        params.put("page_index", pageIndex);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8" ));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()),  "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setRequestProperty("authorization",authHeaderValue);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);


        int code = conn.getResponseCode();
        System.out.println(("HTTP CODE" + String.valueOf(code)));

        /** Infinite recursion not a good solution */
        if(code==500){
            postRequestGDIndexTVShow(urlString,user,pass);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = br.read()) >= 0;)
            sb.append((char)c);
        br.close();

        //Only for gdindex as the response in encrypted
        StringBuilder reverseSb = sb.reverse();
        String encodedString = reverseSb.substring(24,reverseSb.length()-20);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes,StandardCharsets.UTF_8);

        System.out.println("Response from GD Index"+decodedString);
        Gson gson = new Gson();
        ResFormat target = gson.fromJson(decodedString, ResFormat.class);

        DatabaseClient.getInstance(context).getAppDatabase().resFormatDao().insert(target);

        List<String> showFolders = new ArrayList<>();
        List<File> files = target.getData().getFiles();

        if(target.getData().getFiles()!=null) {
            try {
                for (int i = 0; i < files.size(); i++) {
                    File file = files.get(i);
                    if (file.getMimeType().equals("video/x-matroska")
                            || file.getMimeType().equals("video/mp4")
                            || file.getMimeType().equals("video/x-msvideo")
                            || file.getMimeType().equals("video/mpeg")
                            || file.getMimeType().equals("video/webm")) {
                        file.setUrlstring(url + file.getName());
                        Episode episode = gson.fromJson(file.toString() , Episode.class);
                        episode.setFileName(file.getName());
                        episode.setModifiedTime(file.getModifiedTime());
                        System.out.println("episode before tmdb" + episode);
                        sendGetTVShow(file.getName() , episode);//tmdbrequest
                    } else if (file.getMimeType().equals("application/vnd.google-apps.folder")) {
                        showFolders.add(url + file.getName() + "/");
                        Log.i("showFolders" , showFolders.toString() + file.getName());
                    }
                }
            } catch (NumberFormatException e) {
                Log.i("Exception" , e.toString());
            }
        }




        if(target.nextPageToken!=null){
            nextPageToken =target.getNextPageToken();
            pageIndex++;
            postRequestGDIndexTVShow(urlString,user,pass);
        }
        for (int i = 0; i < showFolders.size(); i++) {
            pageIndex = 0;
            nextPageToken = "";
            postRequestGDIndexTVShow(showFolders.get(i),user,pass);
            Log.i("Folder",showFolders.get(i).toString());
        }



    }





}
