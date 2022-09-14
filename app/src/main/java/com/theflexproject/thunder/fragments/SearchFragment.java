package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.MainActivity.mCtx;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapter;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapterLibrary;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SearchFragment extends BaseFragment {
    RecyclerView recyclerView;
    RecyclerView recyclerViewGenres;
    List<Integer> genreList;
    MovieRecyclerAdapterLibrary movieRecyclerAdapterLibrary;
    List<File> newmovieList;
    MovieRecyclerAdapter.OnItemClickListener listener;

    EditText searchBox;
    Button search;
    ScrollView scrollview;

    public SearchFragment() {
    }


    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchBox = view.findViewById(R.id.search_input);
        search = view.findViewById(R.id.search_button);
        scrollview= view.findViewById(R.id.scrollview);
        recyclerView = mActivity.findViewById(R.id.recyclersearch);
        scrollview.setVisibility(View.VISIBLE);
        showSearchResults();
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchResults();
            }
        });



    }

    void showSearchResults(){
        setOnClickListner();
        try{
            searchBox.addTextChangedListener(
                    new TextWatcher() {
                        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        private Timer timer = new Timer();
                        private final long DELAY = 500; // milliseconds

                        @Override
                        public void afterTextChanged(final Editable s) {
                            timer.cancel();
                            timer = new Timer();
                            timer.schedule(
                                    new TimerTask() {
                                        @Override
                                        public void run() {
                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.i(" ", "in thread");
                                                    newmovieList = DatabaseClient
                                                            .getInstance(mCtx)
                                                            .getAppDatabase()
                                                            .fileDao()
                                                            .getSearchQuery(searchBox.getText().toString());
                                                    mActivity.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Log.i(" ", newmovieList.toString());
                                                            recyclerView.setLayoutManager(new GridLayoutManager(mActivity , 3));
                                                            recyclerView.setHasFixedSize(true);
                                                            movieRecyclerAdapterLibrary = new MovieRecyclerAdapterLibrary(mActivity,newmovieList,listener);
                                                            recyclerView.setAdapter(movieRecyclerAdapterLibrary);
                                                            movieRecyclerAdapterLibrary.notifyDataSetChanged();
                                                        }
                                                    });
                                                }});
                                            thread.start();
                                        }
                                    },
                                    DELAY
                            );
                        }
                    }
            );
        }catch (Exception e){
            Log.i(e.toString(),"Exception");
        }

    }

    /** To-Do */
    void showbyGenre(){
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(" ", "in thread");
//                genreList = DatabaseClient
//                        .getInstance(mCtx)
//                        .getAppDatabase()
//                        .fileDao()
//                        .getSearchQuery(searchBox.getText().toString());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.i(" ", newmovieList.toString());
//                        recyclerViewGenres = getView().findViewById(R.id.recyclersearch);
//                        recyclerViewGenres.setLayoutManager(new GridLayoutManager(getContext(), 2));
//                        recyclerViewGenres.setHasFixedSize(true);
//                        movieRecyclerAdapterLibrary = new MovieRecyclerAdapterLibrary(getContext(),newmovieList,listener);
//                        recyclerViewGenres.setAdapter(movieRecyclerAdapterLibrary);
//                        movieRecyclerAdapterLibrary.notifyDataSetChanged();
//                    }
//                });
//            }});
//        thread.start();
    }

    private void setOnClickListner() {
        listener = new MovieRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(newmovieList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
    }
}