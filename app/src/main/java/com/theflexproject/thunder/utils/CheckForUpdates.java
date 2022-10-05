package com.theflexproject.thunder.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.theflexproject.thunder.BuildConfig;
import com.theflexproject.thunder.fragments.BaseFragment;
import com.theflexproject.thunder.model.GitHubResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CheckForUpdates extends BaseFragment {
    public GitHubResponse[] checkForUpdates() throws IOException {
        String GET_URL = "https://api.github.com/repos/anujd64/Thunder/releases";
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        GitHubResponse[] gitHubResponse;
        StringBuffer response = null;
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            gitHubResponse = gson.fromJson(response.toString() ,GitHubResponse[].class);
            String gitHubVersion = gitHubResponse[0].tag_name.substring(1);
            Log.i("Check fOr Update",gitHubVersion + "current" + versionName );

                if(versionCompare(gitHubVersion,versionName)==1){
                    /**UpdateFragment*/
                    return gitHubResponse;
                }

        } else {
            System.out.println("GET request did not work");
        }

        assert response != null;
        Log.i("Check fOr Update",response.toString());

    return null;

    }

    static int versionCompare(String v1, String v2)
    {

        int vnum1 = 0, vnum2 = 0;

        for (int i = 0, j = 0; (i < v1.length()
                || j < v2.length());) {
            while (i < v1.length()
                    && v1.charAt(i) != '.') {
                vnum1 = vnum1 * 10
                        + (v1.charAt(i) - '0');
                i++;
            }
            while (j < v2.length()
                    && v2.charAt(j) != '.') {
                vnum2 = vnum2 * 10
                        + (v2.charAt(j) - '0');
                j++;
            }

            if (vnum1 > vnum2)
                return 1;
            if (vnum2 > vnum1)
                return -1;
            vnum1 = vnum2 = 0;
            i++;
            j++;
        }
        return 0;
    }

}
