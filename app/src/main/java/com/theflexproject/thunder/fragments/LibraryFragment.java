package com.theflexproject.thunder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.MainActivity;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapter;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapterLibrary;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;

public class LibraryFragment extends BaseFragment {

    RecyclerView recyclerView;
    MovieRecyclerAdapterLibrary movieRecyclerAdapterLibrary;
    List<File> newmovieList;
    MovieRecyclerAdapter.OnItemClickListener listener;
    public static Context context;

    public LibraryFragment() {
        // Required empty public constructor
    }

    public static LibraryFragment newInstance(String param1, String param2) {

        return new LibraryFragment();
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
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        return view;
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
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" ", newmovieList.toString());
                        recyclerView = mActivity.findViewById(R.id.recyclerLibrary);
                        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
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
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(newmovieList.get(position).getName());
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
            }
        };
    }


}