package com.theflexproject.thunder.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.MainActivity;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapter;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapterLibrary;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;

public class LibraryFragment extends Fragment {

    RecyclerView recyclerView;
    MovieRecyclerAdapterLibrary movieRecyclerAdapterLibrary;
    List<File> newmovieList;
    MovieRecyclerAdapter.OnItemClickListener listener;
    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String param1, String param2) {
        LibraryFragment fragment = new LibraryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showLibrary();

    }

    void showLibrary(){

        setOnClickListner();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                newmovieList = DatabaseClient
                        .getInstance(MainActivity.mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getAll();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", newmovieList.toString());
                        recyclerView = getView().findViewById(R.id.recyclernewmovies);
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