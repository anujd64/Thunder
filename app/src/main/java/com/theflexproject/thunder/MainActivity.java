package com.theflexproject.thunder;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theflexproject.thunder.database.AppDatabase;
import com.theflexproject.thunder.fragments.HomeFragment;
import com.theflexproject.thunder.fragments.LibraryFragment;
import com.theflexproject.thunder.fragments.SearchFragment;
import com.theflexproject.thunder.fragments.SettingsFragment;
import com.theflexproject.thunder.fragments.UpdateAppFragment;
import com.theflexproject.thunder.model.GitHubResponse;
import com.theflexproject.thunder.utils.CheckForUpdates;

import java.io.IOException;
import java.util.concurrent.Executors;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    SearchFragment searchFragment = new SearchFragment();
    LibraryFragment libraryFragment = new LibraryFragment();
    SettingsFragment settingsFragment = new SettingsFragment();

    BlurView blurView;
    ViewGroup rootView;
    View decorView;

    public static Context context;

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);

        blurView = findViewById(R.id.blurView);
        decorView = getWindow().getDecorView();
        rootView = decorView.findViewById(android.R.id.content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        context = getApplicationContext();

        blurBottom();
//        verifyStoragePermissions(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container , homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homeFragment:
                    getSupportFragmentManager()
                            .beginTransaction()
//                            .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                            .replace(R.id.container , homeFragment)
                            .commit();
                    return true;
                case R.id.searchFragment:
                    getSupportFragmentManager()
                            .beginTransaction()
//                            .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                            .replace(R.id.container , searchFragment)
                            .commit();
                    return true;
                case R.id.libraryFragment:
                    getSupportFragmentManager()
                            .beginTransaction()
//                            .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                            .replace(R.id.container , libraryFragment)
                            .commit();
                    return true;
                case R.id.settingsFragment:
                    getSupportFragmentManager()
                            .beginTransaction()
//                            .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                            .replace(R.id.container , settingsFragment)
                            .commit();
                    return true;
            }
            return false;
        });


//        //if backup exists
//        verifyStoragePermissions(this);
//
//        File backup = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Thunder" , "ThunderBackup.db");
//        File database = getDatabasePath("MyToDos");
//
//        System.out.println("paths" + backup + "\npath2 " + database);
//
//        try {
//            if (backup.exists()) {
//                System.out.println("backup exists true");
//
//                FileChannel src = new FileInputStream(backup).getChannel();
//                FileChannel dst = new FileOutputStream(database).getChannel();
//                dst.transferFrom(src , 0 , src.size());
//                src.close();
//                dst.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        AppDatabase db = Room.databaseBuilder(getApplicationContext() ,
                        AppDatabase.class , "ResDB")
                .fallbackToDestructiveMigration()
                .build();


//                AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                                AppDatabase.class, "ResDB")
//                        .createFromFile(backup)
//                        .fallbackToDestructiveMigration()
//                        .build();
//            }
//        else {
//                AppDatabase.buildDatabase(this);
//
//                //Build Database
////                Executors.newSingleThreadExecutor().execute(() -> {
////                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
////                            AppDatabase.class, "ResDB").fallbackToDestructiveMigration().build();
////                });
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }


        //Check For Update


        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    GitHubResponse[] gitHubResponses = new CheckForUpdates().checkForUpdates();
                    if (gitHubResponses != null) {
                        UpdateAppFragment updateAppFragment = new UpdateAppFragment(gitHubResponses);
                        getSupportFragmentManager().beginTransaction().add(R.id.container , updateAppFragment).addToBackStack(null).commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    //Blur BottomViewNavigation
    void blurBottom() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        final float radius = 14f;
        final Drawable windowBackground = getWindow().getDecorView().getBackground();

        blurView.setupWith(rootView , new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);
    }

    // Storage Permissions
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
//    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//    };
//
//    public static void verifyStoragePermissions(Activity activity) {
//        // Check if we have write permission
//        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (permission != PackageManager.PERMISSION_GRANTED) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//            );
//        }
//    }
//
//    public static void addToRealm(TVShowSeasonDetails tvShowSeasonDetails) {
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(@NonNull Realm realm) {
//
//                realm.copyToRealm(tvShowSeasonDetails);
//            }
//        });
//    }
}





