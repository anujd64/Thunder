package com.theflexproject.thunder;

import static com.theflexproject.thunder.utils.UpdateUtils.checkForUpdates;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.theflexproject.thunder.database.AppDatabase;
import com.theflexproject.thunder.fragments.HomeFragment;
import com.theflexproject.thunder.fragments.LibraryFragment;
import com.theflexproject.thunder.fragments.SearchFragment;
import com.theflexproject.thunder.fragments.SettingsFragment;
import com.theflexproject.thunder.utils.RefreshWorker;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        initWidgets();

        setUpBottomNavigationView();

        getSupportFragmentManager().beginTransaction().replace(R.id.container , homeFragment).commit();

        checkForUpdates(this);

        AppDatabase db = Room.databaseBuilder(getApplicationContext() ,
                        AppDatabase.class , "MyToDos")
//                .fallbackToDestructiveMigration()
                .build();


        //refresh index if set
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean savedREF = sharedPreferences.getBoolean("REFRESH_SETTING", false);
        int savedTime = sharedPreferences.getInt("REFRESH_TIME", 0);
        if(savedREF){
            scheduleWork(savedTime,0);
//            WorkManager.getInstance(context).cancelAllWork();
//            OneTimeWorkRequest impWork = new OneTimeWorkRequest.Builder(RefreshWorker.class)
//                    .setInitialDelay(8,TimeUnit.HOURS)
//                    .build();
//            PeriodicWorkRequest periodicWork = new PeriodicWorkRequest.Builder(RefreshWorker.class, 24, TimeUnit.HOURS)
//                    .setInitialDelay(24,TimeUnit.HOURS)
//                    .build();
//            WorkManager.getInstance(this).enqueue(impWork);
        }




    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0)
            getSupportFragmentManager().popBackStackImmediate();
        else super.onBackPressed();
    }

    private void initWidgets() {
        blurView = findViewById(R.id.blurView);
        decorView = getWindow().getDecorView();
        rootView = decorView.findViewById(android.R.id.content);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        blurBottom();
    }

    private void setUpBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId()==R.id.homeFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                        .replace(R.id.container , homeFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.searchFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                        .replace(R.id.container , searchFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.libraryFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                        .replace(R.id.container , libraryFragment)
                        .commit();
                return true;
            }else if(item.getItemId()==R.id.settingsFragment){
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.from_right,R.anim.to_left,R.anim.from_left,R.anim.to_right)
                        .replace(R.id.container , settingsFragment)
                        .commit();
                return true;
            }
            return false;
        });

    }

    private void blurBottom() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS , WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        final float radius = 12f;
        final Drawable windowBackground = getWindow().getDecorView().getBackground();

        blurView.setupWith(rootView , new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);


    }

    private void scheduleWork(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        long nowMillis = calendar.getTimeInMillis();

        if(calendar.get(Calendar.HOUR_OF_DAY) > hour ||
                (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE)+1 >= minute)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY,hour);
        calendar.set(Calendar.MINUTE,minute);

        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        long diff = calendar.getTimeInMillis() - nowMillis;

        WorkManager mWorkManager = WorkManager.getInstance(context);
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        mWorkManager.cancelAllWork();
        OneTimeWorkRequest mRequest = new OneTimeWorkRequest.Builder(RefreshWorker.class)
                .setConstraints(constraints)
                .setInitialDelay(diff,TimeUnit.MILLISECONDS)
                .build();
        mWorkManager.enqueue(mRequest);

    }

}





