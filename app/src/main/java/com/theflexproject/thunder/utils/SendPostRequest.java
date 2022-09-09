package com.theflexproject.thunder.utils;

import static com.theflexproject.thunder.MainActivity.mCtx;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.model.ResFormat;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;



public class SendPostRequest {

private String nextPageToken = "";
private int pageIndex = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
     public void postRequest(String urlString) throws IOException {
        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}
        URL url = new URL(urlString);
         Map<String,Object> params = new LinkedHashMap<>();
         params.put("authorization", "Basic Og==");
         params.put("page_token", "");
         params.put("page_index", 0);

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
         conn.setDoOutput(true);
         conn.getOutputStream().write(postDataBytes);

         Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

         StringBuilder sb = new StringBuilder();
         for (int c; (c = in.read()) >= 0;)
             sb.append((char)c);
         StringBuilder reverseSb = sb.reverse();
         String encodedString = reverseSb.substring(24,reverseSb.length()-20);
         byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
         String decodedString = new String(decodedBytes,StandardCharsets.UTF_8);

         Gson gson = new Gson();
         ResFormat target = gson.fromJson(decodedString, ResFormat.class);

        Log.i("postRequest: ",target.toString());

        DatabaseClient.getInstance(mCtx).getAppDatabase().resFormatDao().insert(target);
        SendGetRequestTMDB sendGetRequestTMDB = new SendGetRequestTMDB();
        for (int i = 0; i < target.data.files.size(); i++) {
            File file = target.data.files.get(i);
            if(DatabaseClient.getInstance(mCtx).getAppDatabase().fileDao().getByFileName(file.getName())==null &&
                    file.getMimeType().equals("video/x-matroska")||file.getMimeType().equals("video/mp4")){
                file.setUrlstring(url+file.getName());
                sendGetRequestTMDB.sendGet(file);
                DatabaseClient.getInstance(mCtx).getAppDatabase().fileDao().insert(target.data.files.get(i));
            }
        }
        if(target.nextPageToken!=null){
            nextPageToken =target.nextPageToken;
            pageIndex++;
            postRequest(urlString);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postRequest(String urlString, String user, String pass) throws IOException {
        if(urlString.charAt(urlString.length()-1)!='/'){urlString+='/';}
        URL url = new URL(urlString);
        String user_pass = user+":"+pass;
        byte[] user_pass_array = user_pass.getBytes(StandardCharsets.UTF_8);
        String token = "Basic "+ Base64.getEncoder().encodeToString(user_pass_array);

        Log.i("token",token);
        Map<String,Object> params = new LinkedHashMap<>();
        params.put("authorization", token);
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
        conn.setRequestProperty("authorization",token);
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
            sb.append((char)c);
        StringBuilder reverseSb = sb.reverse();
        String encodedString = reverseSb.substring(24,reverseSb.length()-20);
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes,StandardCharsets.UTF_8);

        Gson gson = new Gson();
        ResFormat target = gson.fromJson(decodedString, ResFormat.class);

        Log.i("postRequest: ",target.toString());

        DatabaseClient.getInstance(mCtx).getAppDatabase().resFormatDao().insert(target);
        SendGetRequestTMDB sendGetRequestTMDB = new SendGetRequestTMDB();
        for (int i = 0; i < target.data.files.size(); i++) {
            File file = target.data.files.get(i);
            if(file.getMimeType().equals("video/x-matroska")||file.getMimeType().equals("video/mp4")){
                file.setUrlstring(url+file.getName());
                sendGetRequestTMDB.sendGet(file);
                DatabaseClient.getInstance(mCtx).getAppDatabase().fileDao().insert(target.data.files.get(i));
            }
        }
        if(target.nextPageToken!=null){
            nextPageToken =target.nextPageToken;
            pageIndex++;
            postRequest(urlString,user,pass);
        }


    }


}
