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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
    Button play;

    TableRow air_date;
    TextView air_date_text;

    TVShow tvShow;
    TVShowSeasonDetails tvShowSeasonDetails;

    Episode largestFile;//default episode played on click of Play button
    Episode episode;

    RecyclerView recyclerViewEpisodeFiles;
    List<Episode> episodeFileList;
    FileItemAdapter fileAdapter;
    FileItemAdapter.OnItemClickListener listenerFileItem;

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
        return inflater.inflate(R.layout.fragment_episode_details , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {

        initWidgets();
        loadDetails();

        super.onViewCreated(view , savedInstanceState);
    }

    private void initWidgets() {
        showName = mActivity.findViewById(R.id.tvShowTitleInEpisodeDetails);
        episodeName = mActivity.findViewById(R.id.episodeNameInEpisodeDetails);
        poster = mActivity.findViewById(R.id.tvShowPosterInEpisodeDetails);
        episodeStill = mActivity.findViewById(R.id.stillInEpisodeDetails);
        seasonNumber = mActivity.findViewById(R.id.seasonNumberInEpisodeDetails);
        runtime = mActivity.findViewById(R.id.runtimeInEpisodeDetails);
        ratings = mActivity.findViewById(R.id.ratingsInEpisodeDetails);
        ratingsText = mActivity.findViewById(R.id.ratingsTextInEpisodeDetails);
        overview = mActivity.findViewById(R.id.overviewInEpisodeDetails);
        overviewText = mActivity.findViewById(R.id.overviewDescInEpisodeDetails);

        air_date = mActivity.findViewById(R.id.episodeAirDate);
        air_date_text = mActivity.findViewById(R.id.episodeAirDateText);

        play = mActivity.findViewById(R.id.playInEpisodeDetails);

    }

    private void loadDetails() {
        if (episode.getName() != null) {
            episodeName.setText(episode.getName());
        } else {
            episodeName.setText("Episose" + episode.getEpisode_number());
        }
        if (episode.getVote_average() > 0) {
            ratings.setVisibility(View.VISIBLE);
            ratingsText.setVisibility(View.VISIBLE);
            ratingsText.setText((int) (episode.getVote_average() * 10) + "%");
        }
        if (tvShowSeasonDetails.getPoster_path() != null) {
            Glide.with(mActivity)
                    .load(TMDB_IMAGE_BASE_URL + tvShowSeasonDetails.getPoster_path())
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .into(poster);
        } else {
            if (tvShow.getPoster_path() != null) {
                Glide.with(mActivity)
                        .load(TMDB_IMAGE_BASE_URL + tvShow.getPoster_path())
                        .placeholder(new ColorDrawable(Color.BLACK))
                        .into(poster);
            }

        }
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
            overview.setVisibility(View.VISIBLE);
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
        if (tvShow.getName() != null) {
            showName.setText(tvShow.getName());
        }
        if (!(tvShowSeasonDetails.getSeason_number() < 0)) {
            seasonNumber.setText("Season " + tvShowSeasonDetails.getSeason_number());
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
                        fileAdapter = new FileItemAdapter(getContext() ,(List<MyMedia>)(List<?>) episodeFileList , listenerFileItem);
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