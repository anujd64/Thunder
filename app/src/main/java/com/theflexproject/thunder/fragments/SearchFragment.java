package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.MainActivity.mCtx;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapter;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapterLibrary;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;


public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    RecyclerView recyclerViewGenres;
    List<Integer> genreList;
    MovieRecyclerAdapterLibrary movieRecyclerAdapterLibrary;
    List<File> newmovieList;
    MovieRecyclerAdapter.OnItemClickListener listener;

    TextView searchBox;
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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchResults();
            }
        });



    }

    void showSearchResults(){
        setOnClickListner();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scrollview.setVisibility(View.VISIBLE);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                newmovieList = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getSearchQuery(searchBox.getText().toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", newmovieList.toString());
                        recyclerView = getView().findViewById(R.id.recyclersearch);
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                        recyclerView.setHasFixedSize(true);
                        movieRecyclerAdapterLibrary = new MovieRecyclerAdapterLibrary(getContext(),newmovieList,listener);
                        recyclerView.setAdapter(movieRecyclerAdapterLibrary);
                        movieRecyclerAdapterLibrary.notifyDataSetChanged();
                    }
                });
            }});
        thread.start();

    }

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
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(newmovieList.get(position).getName(),1);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
    }
}