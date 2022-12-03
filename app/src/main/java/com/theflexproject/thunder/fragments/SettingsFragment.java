package com.theflexproject.thunder.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.model.GitHubResponse;
import com.theflexproject.thunder.utils.CheckForUpdates;
import com.theflexproject.thunder.utils.SettingsManager;

import java.io.IOException;
import java.util.concurrent.Executors;

public class SettingsFragment extends BaseFragment {

    AppCompatButton addIndex;
    AppCompatButton viewIndexes;
    AppCompatButton importExportDatabase;
    AppCompatButton checkForUpdate;

    ImageButton discord;
    ImageButton github;
    ImageButton telegram;

   SwitchCompat externalPlayerToggle;
   SwitchMaterial castButtonToggle;
   SwitchCompat refreshPeriodicallyToggle;

    Dialog d;

    SettingsManager settingsManager;

    public SettingsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        initWidgets();
        setStatesOfToggleSwitches();
        setMyOnClickListeners();
    }

    private void initWidgets() {
        addIndex = mActivity.findViewById(R.id.addIndexButton);
        viewIndexes = mActivity.findViewById(R.id.viewIndexes);
        importExportDatabase = mActivity.findViewById(R.id.importExportDatabase);

        checkForUpdate = mActivity.findViewById(R.id.checkforUpdates);

        discord = mActivity.findViewById(R.id.discordImageButton);
        github = mActivity.findViewById(R.id.githubImageButton);
        telegram = mActivity.findViewById(R.id.telegramImageButton);


        externalPlayerToggle = mActivity.findViewById(R.id.externalPlayerToggle);
//        castButtonToggle = mActivity.findViewById(R.id.castButtonVisibility);
        refreshPeriodicallyToggle =mActivity.findViewById(R.id.refreshPeriodicallyToggle);

        settingsManager = new SettingsManager(mActivity);

        d = new Dialog(mActivity);
    }

    private void setMyOnClickListeners() {

        addIndex.setOnClickListener(v -> {
            AddNewIndexFragment nextFrag= new AddNewIndexFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out)
                    .replace(R.id.settingsContainer, nextFrag).addToBackStack(null).commit();
        });

        viewIndexes.setOnClickListener(v -> {
            ManageIndexesFragment manageIndexesFragment = new ManageIndexesFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                    .replace(R.id.settingsContainer, manageIndexesFragment).addToBackStack(null).commit();
        });

        importExportDatabase.setOnClickListener( v -> {
            ManageDatabaseFragment manageDatabaseFragment = new ManageDatabaseFragment();
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                    .replace(R.id.settingsContainer, manageDatabaseFragment).addToBackStack(null).commit();
        });

        externalPlayerToggle.setOnCheckedChangeListener((buttonView , isChecked) -> {
                    settingsManager.saveExternal(isChecked);
                });

        refreshPeriodicallyToggle.setOnCheckedChangeListener((buttonView , isChecked) -> {
//            settingsManager.saveRefresh(isChecked);
            if(isChecked){
                showTimeDialog();
            }else {
                settingsManager.saveRefresh(false,0);
            }
        });

        checkForUpdate.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        GitHubResponse[] gitHubResponses = new CheckForUpdates().checkForUpdates();
                        if(gitHubResponses!=null){
                            UpdateAppFragment updateAppFragment = new UpdateAppFragment(gitHubResponses);
                            mActivity.getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                                    .add(R.id.container,updateAppFragment).addToBackStack(null).commit();
                        }else {
                            mActivity.runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(mActivity,"Latest Version",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        });

        //        castButtonToggle.setOnCheckedChangeListener(
//                (buttonView , isChecked) -> {
//                    if(isChecked){settingsManager.saveCast(true);}else{settingsManager.saveCast(false);}
//                });


        discord.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://discord.gg/NWrz5euMJs"))));
        github.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/anujd64/Thunder"))));
        telegram.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+qbLDmvEgC65lMWI1"))));

    }

    private void setStatesOfToggleSwitches() {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean savedREF = sharedPreferences.getBoolean("REFRESH_SETTING", false);
        String s = "Refresh at : "+ sharedPreferences.getInt("REFRESH_TIME",0)+":00";

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                refreshPeriodicallyToggle.setText(s);
            }
        });

        boolean savedEXT = sharedPreferences.getBoolean("EXTERNAL_SETTING", false);
        boolean savedCAST = sharedPreferences.getBoolean("CAST_SETTING", false);
        externalPlayerToggle.setChecked(savedEXT);
        refreshPeriodicallyToggle.setChecked(savedREF);

//        castButtonToggle.setChecked(savedCAST);
    }


    Integer timeToRefresh =0;
    public void showTimeDialog()
    {

        d.setTitle("SelectTime");
        d.setContentView(R.layout.refresh_time_dialog);
        Button b1 = d.findViewById(R.id.button1);
//        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = d.findViewById(R.id.numberPicker1);
        np.setMaxValue(23);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker , int oldVal , int newVal) {
                timeToRefresh =newVal;
            }
        });
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                settingsManager.saveRefresh(true,timeToRefresh);
                d.dismiss();
            }
        });
//        b2.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                d.dismiss();
//            }
//        });


        d.show();

    }





}