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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.theflexproject.thunder.Constants;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.FileItemAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;
import com.theflexproject.thunder.player.PlayerActivity;
import com.theflexproject.thunder.utils.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EpisodeDetailsFragment extends BaseFragment {


    TextView showName;
    TextView episodeName;
    ImageView episodeStill;
    ImageView poster;
    TextView seasonNumber;
    TextView runtime;
    TextView overview;
    TextView overviewText;
    ImageView ratings;
    TextView ratingsText;
    ImageButton play;

    TableRow air_date;
    TextView air_date_text;

    ImageView logo;
    TextView continueWatching;
    ImageView dot1;
    ImageView dot2;
    ImageView dot3;
    TextView episodeTitle;

    TVShow tvShow;
    TVShowSeasonDetails tvShowSeasonDetails;

    Episode largestFile;//default episode played on click of Play button
    Episode episode;

    RecyclerView recyclerViewEpisodeFiles;
    List<Episode> episodeFileList;
    FileItemAdapter fileAdapter;

    public EpisodeDetailsFragment() {
        // Required empty public constructor
    }

    public EpisodeDetailsFragment(TVShow tvShow , TVShowSeasonDetails tvShowSeasonDetails , Episode episode) {
        this.tvShow = tvShow;
        this.tvShowSeasonDetails = tvShowSeasonDetails;
        this.episode = episode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episode_details_new , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {

        initWidgets(view);
        loadDetails();

        super.onViewCreated(view , savedInstanceState);
    }

    private void initWidgets(View view) {
        showName = view.findViewById(R.id.tvShowTitleInEpisodeDetails);
        logo = view.findViewById(R.id.tvLogoInEp);
        episodeName = view.findViewById(R.id.episodeNameInEpisodeDetails);
        poster = view.findViewById(R.id.tvShowPosterInEpisodeDetails);
        episodeStill = view.findViewById(R.id.stillInEpisodeDetails);
        seasonNumber = view.findViewById(R.id.seasonNumberInEpisodeDetails);
        runtime = view.findViewById(R.id.runtimeInEpisodeDetails);
//        ratings = view.findViewById(R.id.ratingsInEpisodeDetails);
        ratingsText = view.findViewById(R.id.ratingsTextInEpisodeDetails);
        overview = view.findViewById(R.id.overviewInEpisodeDetails);
        overviewText = view.findViewById(R.id.overviewDescInEpisodeDetails);

        air_date = view.findViewById(R.id.episodeAirDate);
        air_date_text = view.findViewById(R.id.episodeAirDateText);

        continueWatching = view.findViewById(R.id.continueWatchingText);
        dot1 = view.findViewById(R.id.dot);
        dot2 = view.findViewById(R.id.dot2);
        dot3 = view.findViewById(R.id.dot3);


        play = view.findViewById(R.id.playInEpisodeDetails);

    }

    private void loadDetails() {


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
            showName.setVisibility(View.VISIBLE);
            showName.setText(tvShow.getName());
        }


        String buttonText = "S" + episode.getSeason_number() + " E" + episode.getEpisode_number();
        System.out.println(buttonText);
        continueWatching.setText(buttonText);
//                            play.setText(buttonText);
        if(episode.getName()!=null){
            dot3.setVisibility(View.VISIBLE);
            episodeName.setVisibility(View.VISIBLE);
            episodeName.setText(episode.getName());
        }

//        if (episode.getName() != null) {
//            episodeName.setText(episode.getName());
//        }
        else {
            String name = "Episode" + episode.getEpisode_number();
            episodeName.setText(name);
        }
        if (episode.getVote_average() > 0) {
//            ratings.setVisibility(View.VISIBLE);
            dot1.setVisibility(View.VISIBLE);
            ratingsText.setVisibility(View.VISIBLE);
            String rating =(int) (episode.getVote_average() * 10) + "%";
            ratingsText.setText(rating);
        }
//        if (tvShowSeasonDetails.getPoster_path() != null) {
//            Glide.with(mActivity)
//                    .load(TMDB_IMAGE_BASE_URL + tvShowSeasonDetails.getPoster_path())
//                    .placeholder(new ColorDrawable(Color.BLACK))
//                    .into(poster);
//        } else {
//            if (tvShow.getPoster_path() != null) {
//                Glide.with(mActivity)
//                        .load(TMDB_IMAGE_BASE_URL + tvShow.getPoster_path())
//                        .placeholder(new ColorDrawable(Color.BLACK))
//                        .into(poster);
//            }
//        }
        if (episode.getStill_path() != null) {
            Glide.with(mActivity.getApplicationContext())
                    .load(Constants.TMDB_BACKDROP_IMAGE_BASE_URL + episode.getStill_path())
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                    .into(episodeStill);
        } else {
            if (tvShow.getPoster_path() != null) {
                Glide.with(mActivity)
                        .load(TMDB_BACKDROP_IMAGE_BASE_URL + tvShow.getPoster_path())
                        .placeholder(new ColorDrawable(Color.BLACK))
                        .into(episodeStill);
            }
        }
        if (episode.getOverview() != null) {
//            overview.setVisibility(View.VISIBLE);
            overviewText.setVisibility(View.VISIBLE);
            overviewText.setText(episode.getOverview());
        }
        if (episode.getAir_date() != null) {
            air_date.setVisibility(View.VISIBLE);
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd" , Locale.ENGLISH);
                Date date = formatter.parse(episode.getAir_date());
                SimpleDateFormat formatter2 = new SimpleDateFormat("MMMM dd , yyyy", Locale.ENGLISH);
                String strDate = formatter2.format(date);
                air_date_text.setText(strDate);
            } catch (ParseException e) {
                System.out.println("parse Exception Date"+e);
            }
        }
        if (episode.getRuntime() != 0) {
            String result = StringUtils.runtimeIntegerToString(episode.getRuntime());
            runtime.setVisibility(View.VISIBLE);
            runtime.setText(result);
        }
        if (!(tvShowSeasonDetails.getSeason_number() < 0)) {
            String season = "Season " + tvShowSeasonDetails.getSeason_number();
            seasonNumber.setText(season);
        }
        loadEpisodeFilesRecycler();

    }

    private void loadEpisodeFilesRecycler() {
        setOnClickListner();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                episodeFileList = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .episodeDao()
                        .byEpisodeId(episode.getId());
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("episodeFileList" , episodeFileList.toString());
                        recyclerViewEpisodeFiles = mActivity.findViewById(R.id.recyclerAvailableEpisodeFiles);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
                        recyclerViewEpisodeFiles.setLayoutManager(linearLayoutManager);
                        recyclerViewEpisodeFiles.setHasFixedSize(true);
                        fileAdapter = new FileItemAdapter(getContext() ,(List<MyMedia>)(List<?>) episodeFileList);
                        recyclerViewEpisodeFiles.setAdapter(fileAdapter);
                        fileAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }


    private void setOnClickListner() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                largestFile = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .episodeDao()
                        .byEpisodeIdLargest(episode.getId());
            }
        });
        thread.start();

        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Settings" , Context.MODE_PRIVATE);
        boolean savedEXT = sharedPreferences.getBoolean("EXTERNAL_SETTING" , false);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedEXT) {
                    //External Player
                    addToLastPlayed();
                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(largestFile.getUrlString()));
                    intent.setDataAndType(Uri.parse(largestFile.getUrlString()) , "video/*");
                    startActivity(intent);
                } else {
                    //Play video
                    addToLastPlayed();
                    Intent in = new Intent(getActivity() , PlayerActivity.class);
                    in.putExtra("url" , largestFile.getUrlString());
                    startActivity(in);
                    Toast.makeText(getContext() , "Play" , Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void addToLastPlayed() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(getContext()).getAppDatabase().episodeDao().updatePlayed(episode.getId());
            }
        });
        thread.start();
    }
}