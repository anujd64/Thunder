package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.Constants.TMDB_BACKDROP_IMAGE_BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.EpisodeAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.Season;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.player.PlayerActivity;

import java.util.List;


public class SeasonDetailsFragment extends BaseFragment {

    String tvShowName;
    TextView tvShowTitleText;
    TextView seasonName;
//    TextView seasonNumber;
    TextView numberOfEpisodes;

    TextView overview;
    TextView overviewText;
    ImageButton play;

    ImageView logo;
    TextView continueWatching;
    ImageView dot3;
    TextView episodeTitle;

//    ImageView poster;
    ImageView backdrop;

    TVShowSeasonDetails tvShowSeasonDetails;
    TVShow tvShow;
    Season season;

    RecyclerView episodesRecycler;
    EpisodeAdapter episodeAdapter;
    EpisodeAdapter.OnItemClickListener listener;
    List<Episode> episodes;

    Episode nextEpisode;


    public SeasonDetailsFragment() {
        // Required empty public constructor
    }

    public SeasonDetailsFragment(TVShow tvShow , TVShowSeasonDetails tvShowSeasonDetails) {
        this.tvShow = tvShow;
        this.tvShowSeasonDetails = tvShowSeasonDetails;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_season_details_new , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        initWidgets(view);
        loadDetails();

    }

    private void initWidgets(View view) {
        tvShowTitleText = view.findViewById(R.id.showTitle);
        seasonName = view.findViewById(R.id.seasonTitle);
//        seasonNumber = view.findViewById(R.id.seasonNumber);
        numberOfEpisodes = view.findViewById(R.id.noOfEpisodesInSeason);
        overview = view.findViewById(R.id.overviewDescTVShowSeason);
//        overviewText = view.findViewById(R.id.overviewTextInSeason);
//        poster = view.findViewById(R.id.seasonPosterInDetails);
        backdrop = view.findViewById(R.id.tvShowBackdropInSeason);


        logo = view.findViewById(R.id.tvLogoInSeason);
        continueWatching = view.findViewById(R.id.continueWatchingText);
        episodeTitle = view.findViewById(R.id.episodeNameInTvSeason);
        dot3 = view.findViewById(R.id.dot3);


        play = view.findViewById(R.id.playInSeasonDetails);

        episodesRecycler = view.findViewById(R.id.recyclerEpisodes);
    }

    private void loadDetails() {
        try {
            tvShowName = tvShow.getName();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    tvShowSeasonDetails = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .tvShowSeasonDetailsDao()
                            .find(tvShowSeasonDetails.getId());

                    episodes = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .episodeDao()
                            .getFromThisSeason(tvShow.getId() , tvShowSeasonDetails.getId());

                    nextEpisode = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .episodeDao()
                            .getNextEpisodeInSeason(tvShowSeasonDetails.getId());

                    System.out.println("episodes in season details" + episodes);
                    Log.i("tvShowDetails Object" , tvShowSeasonDetails.toString());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            String logoLink = tvShow.getLogo_path();
                            System.out.println("Logo Link"+logoLink);

                            if(!logoLink.equals("")){
                                logo.setVisibility(View.VISIBLE);
                                Glide.with(mActivity)
                                        .load(logoLink)
                                        .apply(new RequestOptions()
                                                .fitCenter()
                                                .override(Target.SIZE_ORIGINAL))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(new ColorDrawable(Color.TRANSPARENT))
                                        .into(logo);
                            }
                            if(logoLink.equals("")&&tvShow.getName()!=null){
                                tvShowTitleText.setVisibility(View.VISIBLE);
                                tvShowTitleText.setText(tvShow.getName());
                            }
                            if (tvShowSeasonDetails.getName() != null) {
                                seasonName.setVisibility(View.VISIBLE);
                                seasonName.setText(tvShowSeasonDetails.getName());
                            } else {
                                seasonName.setVisibility(View.VISIBLE);
                                seasonName.setText("Season" + tvShowSeasonDetails.getSeason_number());
                            }

                            int season_number = tvShowSeasonDetails.getSeason_number();
                            int number_of_episodes = episodes.size();
                            if (number_of_episodes > 0) {
//                                seasonNumber.setVisibility(View.VISIBLE);
//                                seasonNumber.setText("Season " + season_number);
                                numberOfEpisodes.setVisibility(View.VISIBLE);
                                numberOfEpisodes.setText(number_of_episodes + " Episodes");
                            }
//                            if (tvShow.getName() != null) {
//                                tvShowTitleText.setText(tvShow.getName());
//                            }
                            if (tvShowSeasonDetails.getOverview().length() > 1) {
//                                overviewText.setVisibility(View.VISIBLE);
//                                overview.setVisibility(View.VISIBLE);
                                overview.setText(tvShowSeasonDetails.getOverview());
                            }
//                            if (tvShowSeasonDetails.getPoster_path() != null) {
//                                Glide.with(mActivity)
//                                        .load(TMDB_IMAGE_BASE_URL + tvShowSeasonDetails.getPoster_path())
//                                        .placeholder(new ColorDrawable(Color.BLACK))
//                                        .into(poster);
//                            } else {
//                                Glide.with(mActivity)
//                                        .load(TMDB_IMAGE_BASE_URL + tvShow.getPoster_path())
//                                        .placeholder(new ColorDrawable(Color.BLACK))
//                                        .into(poster);
//                            }
                            if (nextEpisode != null && nextEpisode.getStill_path() != null) {
                                Glide.with(mActivity)
                                        .load(TMDB_BACKDROP_IMAGE_BASE_URL + nextEpisode.getStill_path())
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                        .into(backdrop);
                            }else if (tvShowSeasonDetails.getPoster_path() != null) {
                                Glide.with(mActivity)
                                        .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShowSeasonDetails.getPoster_path())
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                        .into(backdrop);
                            }else if (tvShow.getBackdrop_path() != null) {
                                Glide.with(mActivity)
                                        .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShow.getBackdrop_path())
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                        .into(backdrop);
                            } else {
                                if (tvShowSeasonDetails.getPoster_path() != null) {
                                    Glide.with(mActivity)
                                            .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShowSeasonDetails.getPoster_path())
                                            .placeholder(new ColorDrawable(Color.BLACK))
                                            .into(backdrop);
                                }
                            }
                            if (nextEpisode != null) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        continueWatching.setText("S" + nextEpisode.getSeason_number() + " E" + nextEpisode.getEpisode_number());
//                                        play.setText("S" + nextEpisode.getSeason_number() + " E" + nextEpisode.getEpisode_number());
                                    }
                                });
                                if(nextEpisode.getName()!=null){
                                    dot3.setVisibility(View.VISIBLE);
                                    episodeTitle.setVisibility(View.VISIBLE);
                                    episodeTitle.setText(nextEpisode.getName());
                                }
                            }


                            loadEpisodesRecycler();
                        }
                    });
                }
            });
            thread.start();
        } catch (NullPointerException exception) {
            Log.i("Error" , exception.toString());
        }
    }

    private void loadEpisodesRecycler() {
        setOnClickListner();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(" " , episodes.toString());

                        episodesRecycler = mActivity.findViewById(R.id.recyclerEpisodes);
                        episodesRecycler.setVisibility(View.VISIBLE);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        episodesRecycler.setLayoutManager(linearLayoutManager);
                        episodesRecycler.setHasFixedSize(true);
                        episodeAdapter = new EpisodeAdapter(mActivity, episodes , listener);
                        episodesRecycler.setAdapter(episodeAdapter);
                        episodeAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }


    private void setOnClickListner() {
        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Settings" , Context.MODE_PRIVATE);
        boolean savedEXT = sharedPreferences.getBoolean("EXTERNAL_SETTING" , false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedEXT) {
                    //External Player
                    addToLastPlayed();
                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(nextEpisode.getUrlString()));
                    intent.setDataAndType(Uri.parse(nextEpisode.getUrlString()) , "video/*");
                    startActivity(intent);
                } else {
                    //Play video
                    addToLastPlayed();
                    Intent in = new Intent(getActivity() , PlayerActivity.class);
                    in.putExtra("url" , nextEpisode.getUrlString());
                    startActivity(in);
                    Toast.makeText(getContext() , "Play" , Toast.LENGTH_LONG).show();
                }
            }

            private void addToLastPlayed() {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseClient.getInstance(getContext()).getAppDatabase().episodeDao().updatePlayed(nextEpisode.getId());
                    }
                });
                thread.start();
            }
        });
        listener = new EpisodeAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view , int position) {
                EpisodeDetailsFragment episodeDetailsFragment = new EpisodeDetailsFragment(tvShow , tvShowSeasonDetails , episodes.get(position));
                mActivity.getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.fade_in , R.anim.fade_out , R.anim.fade_in , R.anim.fade_out)
                        .add(R.id.container , episodeDetailsFragment).addToBackStack(null).commit();
            }
        };
    }
}