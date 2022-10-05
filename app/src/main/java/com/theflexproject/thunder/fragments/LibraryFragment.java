package com.theflexproject.thunder.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MovieRecyclerAdapterLibrary;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;

import java.util.List;

public class LibraryFragment extends BaseFragment {

    RecyclerView recyclerView;
    MovieRecyclerAdapterLibrary movieRecyclerAdapterLibrary;
    List<File> newmovieList;
    MovieRecyclerAdapterLibrary.OnItemClickListener listener;
    public static Context context;

    AutoCompleteTextView autoCompleteTextView;
    String[] sort_methods;
    ArrayAdapter<String> arrayAdapter;


    public LibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sort_methods = mActivity.getResources().getStringArray(R.array.sort_methods);
        arrayAdapter = new ArrayAdapter<>(mActivity,R.layout.dropdown_item,sort_methods);
        autoCompleteTextView = mActivity.findViewById(R.id.AutoCompleteTextview);
        autoCompleteTextView.setAdapter(arrayAdapter);

        showLibrary();
//
//        Thread threadmovielist = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        String methodChosen = autoCompleteTextView.getText().toString();
////                        Log.i("Method",methodChosen);
////                        switch(methodChosen){
////                            case "FileName":
////                                sortName();
////                            case "ReleaseDate":
////                                sortReleaseDate();
////                            case "Size":
////                                sortSize();
////                            case "TimeModified":
////                                sortTimeModified();
////                            case "IndexLink":
////                                sortIndexLink();
////                            case "Title":
////                                sortTitle();
////                        }
////
////                        Toast.makeText(mActivity.getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
//        threadmovielist.setPriority(10);
//        threadmovielist.start();



    }

    private void setOnClickListner() {
        listener = (view , position) -> {
            MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(newmovieList.get(position).getName());
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                    .replace(R.id.container,movieDetailsFragment).addToBackStack(null).commit();
        };
    }

    void showLibrary(){
        setOnClickListner();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                newmovieList = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .fileDao()
                        .getAll();
                showRecycler(newmovieList);
            }
        });
        thread.start();
    }

    private void showRecycler(List<File> newmovieList) {
        mActivity.runOnUiThread(() -> {
            DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            int noOfItems;
            if(dpWidth < 600f){noOfItems = 3;}
            else if(dpWidth < 840f){noOfItems = 6;}
            else {noOfItems = 8; }
            Log.i(" " , newmovieList.toString());
            recyclerView = mActivity.findViewById(R.id.recyclerLibrary);
            recyclerView.setLayoutManager(new GridLayoutManager(context , noOfItems));
            recyclerView.setHasFixedSize(true);
            movieRecyclerAdapterLibrary = new MovieRecyclerAdapterLibrary(mActivity , newmovieList , listener);
            recyclerView.setAdapter(movieRecyclerAdapterLibrary);
            movieRecyclerAdapterLibrary.notifyDataSetChanged();
        });
    }

//    void sortName(){
//        setOnClickListner(); /**set this every time*/
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmovieList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByFileName();
//                showRecycler(newmovieList);
//            }
//        });
//
//
//    }

//    void sortReleaseDate(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("inside sort function",methodChosen);
//                newmovieList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByRelease();
//                Log.i("inside sort function",newmovieList.toString());
//                showRecycler(newmovieList);
//            }
//        });
//
//    }

//    void sortSize(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmovieList = DatabaseClient
//                                .getInstance(mActivity)
//                                .getAppDatabase()
//                                .fileDao()
//                                .sortBySize();
////                showRecycler(newmovieList);
//            }
//        });
//
//    }
//
//    void sortIndexLink() {
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmovieList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByIndex();
////                showRecycler(newmovieList);
//            }
//        });
//
//
//    }
//
//    void sortTimeModified(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString();
//                Log.i("Method",methodChosen);
//                newmovieList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByTime();
////                showRecycler(newmovieList);
//            }
//        });
//
//    }
//
//    void sortTitle(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmovieList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByTitle();
////                showRecycler(newmovieList);
//            }
//        });
//
//    }
}