package com.theflexproject.thunder.utils;

import androidx.appcompat.app.AppCompatActivity;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.fragments.UpdateAppFragment;
import com.theflexproject.thunder.model.GitHubResponse;

import java.io.IOException;
import java.util.concurrent.Executors;

public class UpdateUtils {
    public static void checkForUpdates(AppCompatActivity appCompatActivity){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    GitHubResponse[] gitHubResponses = new CheckForUpdates().checkForUpdates();
                    if (gitHubResponses != null) {
                        UpdateAppFragment updateAppFragment = new UpdateAppFragment(gitHubResponses);
                        appCompatActivity.getSupportFragmentManager().beginTransaction().add(R.id.container , updateAppFragment).addToBackStack(null).commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
