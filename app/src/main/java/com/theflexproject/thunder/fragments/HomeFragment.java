package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.MainActivity.mCtx;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.BannerRecyclerAdapter;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;


public class HomeFragment extends BaseFragment {


    List<File> bannerList;
    RecyclerView bannerRecycler;
    BannerRecyclerAdapter bannerRecyclerAdapter;
    BannerRecyclerAdapter.OnItemClickListener listener2;

    RecyclerView recyclerView;
    List<File> newmovieList;
    MovieRecyclerAdapter.OnItemClickListener listener1;

    RecyclerView topRatedRecycler;
    List<File> topRatedList;
    MovieRecyclerAdapter.OnItemClickListener listener3;

    RecyclerView lastPlayedRecycler;
    List<File> lastPlayedList;
    MovieRecyclerAdapter.OnItemClickListener listener4;

    MovieRecyclerAdapter movieRecyclerAdapter;

    TextView lastPlayedTexView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadbanners();
        newreleases();
        toprated();
        lastPlayed();

    }
    void loadbanners(){


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                bannerList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getrecentlyadded();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", bannerList.toString());
                        bannerRecycler = mActivity.findViewById(R.id.recentlyaddedrecycler);
                        bannerRecycler.setLayoutManager(new ScaleCenterItemLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        bannerRecycler.setHasFixedSize(true);
                        bannerRecyclerAdapter = new BannerRecyclerAdapter(getContext(),bannerList,listener2);
                        bannerRecycler.setAdapter(bannerRecyclerAdapter);
                    }
                });
            }});
        thread.start();
    }
    void newreleases(){

            setOnClickListner();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                newmovieList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getrecentreleases();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", newmovieList.toString());
                        recyclerView = mActivity.findViewById(R.id.recyclernewmovies);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setHasFixedSize(true);
                        movieRecyclerAdapter = new MovieRecyclerAdapter(getContext(),newmovieList,listener1);
                        recyclerView.setAdapter(movieRecyclerAdapter);
                        movieRecyclerAdapter.notifyDataSetChanged();
                    }
                });
            }});
        thread.start();

    }
    void toprated(){

        setOnClickListner();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                topRatedList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getTopRated();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", topRatedList.toString());
                        topRatedRecycler = mActivity.findViewById(R.id.recyclertoprated);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        topRatedRecycler.setLayoutManager(linearLayoutManager);
                        topRatedRecycler.setHasFixedSize(true);
                        movieRecyclerAdapter = new MovieRecyclerAdapter(getContext(),topRatedList,listener3);
                        topRatedRecycler.setAdapter(movieRecyclerAdapter);
                        movieRecyclerAdapter.notifyDataSetChanged();
                    }
                });
            }});
        thread.start();

    }

    void lastPlayed(){

        setOnClickListner();
        lastPlayedTexView= mActivity.findViewById(R.id.LastPlayed);


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                lastPlayedList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getPlayed();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", lastPlayedList.toString());
                        lastPlayedRecycler = mActivity.findViewById(R.id.recyclerlastplayed);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        lastPlayedRecycler.setLayoutManager(linearLayoutManager);
                        lastPlayedRecycler.setHasFixedSize(true);
                        movieRecyclerAdapter = new MovieRecyclerAdapter(getContext(),lastPlayedList,listener4);
                        lastPlayedRecycler.setAdapter(movieRecyclerAdapter);
                        movieRecyclerAdapter.notifyDataSetChanged();
                    }
                });
            }});
        thread.start();
        if(lastPlayedList!=null){
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lastPlayedTexView.setVisibility(View.VISIBLE);
                }
            });
        }

    }

    private void setOnClickListner() {
        listener1 = new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(newmovieList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
        listener2 = new BannerRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(bannerList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
        listener3 = new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(topRatedList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
        listener4 = new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(lastPlayedList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
    }
}