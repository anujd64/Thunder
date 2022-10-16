package com.theflexproject.thunder.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MediaAdapter;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MyMedia;

import java.util.List;


public class MovieLibraryFragment extends BaseFragment {

    RecyclerView recyclerViewMovies;
    MediaAdapter mediaAdapter;
    MediaAdapter.OnItemClickListener listenerMovie;
    List<Movie> movieList;

    public MovieLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_library , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        showLibraryMovies();
    }

    void showLibraryMovies() {
        setOnClickListner();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                if(movieList ==null){
                    movieList = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .movieDao().getAll();
                }
                showRecyclerMovies(movieList);
            }
        });
        thread.start();
    }

    private void showRecyclerMovies(List<Movie> newmediaList) {
        mActivity.runOnUiThread(() -> {
            DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfItems;
            if (dpWidth < 600f) {
                noOfItems = 3;
            } else if (dpWidth < 840f) {
                noOfItems = 6;
            } else {
                noOfItems = 8;
            }
            Log.i(" " , newmediaList.toString());
            recyclerViewMovies = mActivity.findViewById(R.id.recyclerLibraryMovies);
            if(recyclerViewMovies!=null){
                recyclerViewMovies.setLayoutManager(new GridLayoutManager(mActivity , noOfItems));
                recyclerViewMovies.setHasFixedSize(true);
                mediaAdapter = new MediaAdapter(mActivity ,(List<MyMedia>)(List<?>) newmediaList , listenerMovie);
                recyclerViewMovies.setAdapter(mediaAdapter);
                mediaAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnClickListner() {
        listenerMovie = (view , position) -> {
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(movieList.get(position).getId());
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in , R.anim.fade_out , R.anim.fade_in , R.anim.fade_out)
                    .replace(R.id.container , movieDetailsFragment).addToBackStack(null).commit();
        };
    }
}