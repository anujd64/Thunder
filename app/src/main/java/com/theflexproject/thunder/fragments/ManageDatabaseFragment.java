package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.utils.StorageUtils.verifyStoragePermissions;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.AppDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ManageDatabaseFragment extends BaseFragment {

    Button importDatabase;
    Button exportDatabase;

    public ManageDatabaseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_database , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        initWidgets();

        setMyOnClickListeners();


    }

    private void setMyOnClickListeners() {
        importDatabase.setOnClickListener(v -> {
            mActivity.deleteDatabase("MyToDos");

//            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Thunder");
//            File[] listOfFiles = folder.listFiles();
//
//            File database = mActivity.getDatabasePath("MyToDos");
//            System.out.println("paths" + backup + "\npath2 " + database);
//
//            if(listOfFiles!=null)
//                for (File file : listOfFiles) {
//                    if (file.exists() && file.isFile()) {
//                        try {
//                                System.out.println("backup exists true");
//                                FileChannel src = new FileInputStream(file).getChannel();
//                                FileChannel dst = new FileOutputStream(database).getChannel();
//                                dst.transferFrom(src , 0 , src.size());
////                            fco.transferFrom(fc2, fc1.size() - 1, fc2.size());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println(file.getName());
//                    }
//                }

            File backup = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Thunder" , "ThunderBackup.db");
            File database = mActivity.getDatabasePath("MyToDos");
            System.out.println("paths" + backup + "\npath2 " + database);

            try {
                if (backup.exists()) {
                    System.out.println("backup exists true");

                    FileChannel src = new FileInputStream(backup).getChannel();
                    FileChannel dst = new FileOutputStream(database).getChannel();
                    dst.transferFrom(src , 0 , src.size());
                    src.close();
                    dst.close();
                    Room.databaseBuilder(mActivity ,
                                    AppDatabase.class , "MyToDos")
                            .fallbackToDestructiveMigration()
                            .createFromFile(database)
                            .build();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(mActivity,"Import Successful",Toast.LENGTH_LONG).show();

        });

        exportDatabase.setOnClickListener(v -> {

            verifyStoragePermissions(mActivity);

            int EXTERNAL_STORAGE_PERMISSION_CODE = 23;
            // Requesting Permission to access External Storage
            ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CODE);

            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File backupDB = new File(folder +"/Thunder", "ThunderBackup.db");
            File currentDB = new File(mActivity.getDatabasePath("MyToDos").toString());

            try {

                File backupDir = new File(folder+"/Thunder");
                if(!backupDir.exists()) backupDir.mkdir();
                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src , 0 , src.size());
                    src.close();
                    dst.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Toast.makeText(mActivity,"Export Successful",Toast.LENGTH_LONG).show();

        });
    }

    private void initWidgets() {
        importDatabase = mActivity.findViewById(R.id.importDatabase);
        exportDatabase = mActivity.findViewById(R.id.exportDatabase);

    }
}