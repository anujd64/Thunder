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
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;

import java.util.List;


public class TvShowsLibraryFragment extends BaseFragment {

    RecyclerView recyclerViewTVShows;
    MediaAdapter mediaAdapter;
    MediaAdapter.OnItemClickListener listenerTVShow;
    List<TVShow> tvShowList;
    public TvShowsLibraryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_shows_library , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);
        showLibraryTVShows();
    }
    void showLibraryTVShows() {
        setOnClickListner();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                if(tvShowList==null){
                    tvShowList = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .tvShowDao().getAll();

//                    List<TVShow> emptyTVShows = new ArrayList<>();
//                    for(TVShow tvShow : tvShowList) {
//                        List<TVShowSeasonDetails> seasonsList = DatabaseClient
//                                .getInstance(mActivity)
//                                .getAppDatabase()
//                                .tvShowSeasonDetailsDao()
//                                .findByShowId(tvShow.getId());
//                        if(seasonsList==null|| seasonsList.size()==0){emptyTVShows.add(tvShow);
//                            DatabaseClient.getInstance(mActivity).getAppDatabase().tvShowDao().deleteById(tvShow.getId());
//                        }
//                    }
//                    tvShowList.removeAll(emptyTVShows);
                }
                showRecyclerTVShows(tvShowList);
            }
        });
        thread.start();
    }

    private void showRecyclerTVShows(List<TVShow> tvShowList) {
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
            Log.i(" " , tvShowList.toString());
            recyclerViewTVShows = mActivity.findViewById(R.id.recyclerLibraryTVShows);
            if(recyclerViewTVShows!=null){
                recyclerViewTVShows.setLayoutManager(new GridLayoutManager(mActivity , noOfItems));
                recyclerViewTVShows.setHasFixedSize(true);
                mediaAdapter = new MediaAdapter(mActivity, (List<MyMedia>)(List<?>)tvShowList, listenerTVShow);
                recyclerViewTVShows.setAdapter(mediaAdapter);
                mediaAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setOnClickListner() {
        listenerTVShow = (view , position) -> {
            TvShowDetailsFragment tvShowDetailsFragment = new TvShowDetailsFragment(tvShowList.get(position).getId());
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in , R.anim.fade_out , R.anim.fade_in , R.anim.fade_out)
                    .replace(R.id.container , tvShowDetailsFragment).addToBackStack(null).commit();
        };
    }

}