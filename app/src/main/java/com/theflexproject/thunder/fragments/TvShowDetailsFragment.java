package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.Constants.TMDB_BACKDROP_IMAGE_BASE_URL;
import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.MediaAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Genre;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.player.PlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class TvShowDetailsFragment extends BaseFragment {

    int tvShowId;
    TextView tvShowTitleText;
    TextView numberOfSeasons;
    TextView numberOfEpisodes;

    TextView overview;
    TextView listOfFiles;
    Button play;



    TableRow genres;
    TableRow status;
    TableRow type;
    TableRow votesAvg;
    TableRow votesCount;

    TextView statusText;
    TextView typeText;
    TextView genresText;
    TextView voteAvgText;
    TextView votesCountText;
    TextView ratingsText;
    ImageView ratings;

    ImageView poster;
    ImageView backdrop;
    TVShow tvShowDetails;

    RecyclerView recyclerViewSeasons;
    List<TVShowSeasonDetails> seasonsList;
    MediaAdapter mediaAdapter;
    MediaAdapter.OnItemClickListener listenerSeasonItem;

    Episode nextEpisode;


    public TvShowDetailsFragment() {
        // Required empty public constructor
    }
    public TvShowDetailsFragment(int tvShowId) {
        this.tvShowId = tvShowId;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_details , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        initWidgets(view);
        loadDetails();
    }

    private void initWidgets(View view) {
        tvShowTitleText = view.findViewById(R.id.tvShowTitle);
        numberOfSeasons = view.findViewById(R.id.noOfSeasons);
        numberOfEpisodes = view.findViewById(R.id.noOfEpisodes);
        overview = view.findViewById(R.id.overviewDescTVShow);
        poster = view.findViewById(R.id.tvShowPosterInDetails);
        backdrop = view.findViewById(R.id.tvShowBackdrop);

        //TableRows that do not change
        type = view.findViewById(R.id.tvShowType);
        status = view.findViewById(R.id.tvShowStatus);
        genres = view.findViewById(R.id.tvShowGenres);

        //TextViews that change
        statusText = view.findViewById(R.id.tvShowStatusText);
        typeText = view.findViewById(R.id.tvShowtypeText);
        genresText = view.findViewById(R.id.tvShowGenresText);
        ratings = view.findViewById(R.id.ratingsTV);
        ratingsText = view.findViewById(R.id.ratingsTVText);

        play = view.findViewById(R.id.playInTVShowDetails);



    }

    private void loadDetails(){
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    tvShowDetails = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .tvShowDao()
                            .find(tvShowId);

                    nextEpisode = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .episodeDao()
                            .getNextEpisodeInTVShow(tvShowDetails.getId());

                    Log.i("tvShowDetails Object",tvShowDetails.toString());
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(tvShowDetails.getName()!=null){
                                tvShowTitleText.setText(tvShowDetails.getName());
                            }
                            if(tvShowDetails.getGenres()!=null){
                                genres.setVisibility(View.VISIBLE);
                                ArrayList<Genre> genres = tvShowDetails.getGenres();
                                StringBuilder sb = new StringBuilder();
                                for (int i=0;i<genres.size();i++) {
                                    Genre genre = genres.get(i);
                                    if(genre!=null){
                                        if(i== genres.size()-1){sb.append(genre.getName());}
                                        else {sb.append(genre.getName()).append(", ");}
                                    }
                                }
                                System.out.println("genres"+ genres);
                                genresText.setText(sb.toString());
                            }
                            if(tvShowDetails.getVote_average()!=0){
                                ratings.setVisibility(View.VISIBLE);
                                ratingsText.setVisibility(View.VISIBLE);
                                ratingsText.setText((int)(tvShowDetails.getVote_average()*10)+"%");
                            }
                            int number_of_seasons = tvShowDetails.getNumber_of_seasons();
                            int number_of_episodes = tvShowDetails.getNumber_of_episodes();
                            if(number_of_seasons!=0) {
                                numberOfSeasons.setVisibility(View.VISIBLE);
                                numberOfSeasons.setText(+number_of_seasons+" Seasons");
                                numberOfEpisodes.setVisibility(View.VISIBLE);
                                numberOfEpisodes.setText(number_of_episodes+" Episodes");
                            }
                            if(tvShowDetails.getType()!=null){
                                type.setVisibility(View.VISIBLE);
                                typeText.setText(tvShowDetails.getType());
                            }
                            if(tvShowDetails.getStatus()!=null){
                                status.setVisibility(View.VISIBLE);
                                statusText.setText(tvShowDetails.getStatus());
                            }
                            if(tvShowDetails.getOverview()!=null){overview.setText(tvShowDetails.getOverview());}
                            if(tvShowDetails.getPoster_path()!=null) {
                                Glide.with(mActivity)
                                        .load(TMDB_IMAGE_BASE_URL + tvShowDetails.getPoster_path())
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                        .into(poster);
                            }
                            if(tvShowDetails.getBackdrop_path()!=null) {
                                Glide.with(mActivity)
                                        .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShowDetails.getBackdrop_path())
                                        .placeholder(new ColorDrawable(Color.BLACK))
                                        .into(backdrop);
                            }else {
                                if(tvShowDetails.getPoster_path()!=null) {
                                    Glide.with(mActivity)
                                            .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShowDetails.getPoster_path())
                                            .placeholder(new ColorDrawable(Color.BLACK))
                                            .into(backdrop);
                                }
                            }

                            if (nextEpisode != null) {
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        play.setText("S" + nextEpisode.getSeason_number() + " E" + nextEpisode.getEpisode_number());
                                    }
                                });
                            }

                            loadSeasonRecycler();
                        }
                    });
                }});
            thread.start();
        }catch (NullPointerException exception){Log.i("Error",exception.toString());}
    }

    private void loadSeasonRecycler() {

        setOnClickListner();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
//                seasonsList = tvShowDetails.getSeasons();
                seasonsList = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .tvShowSeasonDetailsDao()
                        .findByShowId(tvShowDetails.getId());

//                List<TVShowSeasonDetails> emptySeasons = new ArrayList<>();
//                for(TVShowSeasonDetails season : seasonsList) {
//                    List<Episode> episodeList = DatabaseClient
//                            .getInstance(mActivity)
//                            .getAppDatabase()
//                            .episodeDao()
//                            .getFromThisSeason(tvShowDetails.getId(),season.getId());
//                    if(episodeList==null || episodeList.size()==0){
//                        emptySeasons.add(season);
//                        DatabaseClient.getInstance(mActivity).getAppDatabase().tvShowSeasonDetailsDao().deleteById(season.getId());
//                    }
//                }
//                seasonsList.removeAll(emptySeasons);


                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("SeasonListin loadSeason", seasonsList.toString());
                        recyclerViewSeasons = mActivity.findViewById(R.id.recyclerSeasons);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                        recyclerViewSeasons.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        recyclerViewSeasons.setLayoutManager(linearLayoutManager);
                        recyclerViewSeasons.setHasFixedSize(true);
                        mediaAdapter = new MediaAdapter(getContext(),(List<MyMedia>)(List<?>) seasonsList,listenerSeasonItem);
                        recyclerViewSeasons.setAdapter(mediaAdapter);
                        mediaAdapter.notifyDataSetChanged();
                    }
                });
            }});
        thread.start();
    }

    private void setOnClickListner(){
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
        listenerSeasonItem = (view , position) -> {
            SeasonDetailsFragment seasonDetailsFragment = new SeasonDetailsFragment(tvShowDetails,seasonsList.get(position));
            mActivity.getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
                    .replace(R.id.container,seasonDetailsFragment).addToBackStack(null).commit();
        };
    }
}