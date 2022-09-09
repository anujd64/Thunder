package com.theflexproject.thunder.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;
import static com.theflexproject.thunder.MainActivity.mCtx;

import android.app.DownloadManager;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.player.PlayerActivity;
import com.theflexproject.thunder.utils.MovieQualityExtractor;
import com.theflexproject.thunder.utils.sizetoReadablesize;

public class MovieDetailsFragment extends Fragment{
    String movieTitle;
    int flag;
    TextView titleText;
    TextView yearText;
    TextView size;
    TextView videoQuality;
    TextView overview;
    TextView listOfFiles;
    Button play;
    Button download;
    ImageView poster;
    File movieDetails;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }
    public MovieDetailsFragment(String title){
        movieTitle=title;
    }
    public MovieDetailsFragment(String title, int flag){
        // if object created using this constructor then get file details
        movieTitle=title;
        this.flag =flag;
    }

    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();

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
        return inflater.inflate(R.layout.fragment_movie_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        titleText = view.findViewById(R.id.title_text);
        yearText = view.findViewById(R.id.year_text);
        videoQuality = view.findViewById(R.id.videoQuality);
        size = view.findViewById(R.id.size);
        overview = view.findViewById(R.id.overviewdesc);
        listOfFiles = view.findViewById(R.id.listoffiles);
        poster = view.findViewById(R.id.moviePosterInDetails);
//        backdrop = view.findViewById(R.id.backdrop);

        play = view.findViewById(R.id.play);
        download = view.findViewById(R.id.download);

        if (flag == 1) {
            loadDetailsFile();
        }else {
            loadDetails();
        }


        //Play video
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseClient.getInstance(getContext()).getAppDatabase().fileDao().updatePlayed(movieDetails.getName());
                    }
                });
                thread.start();

                Intent in = new Intent(getActivity(), PlayerActivity.class);
                in.putExtra("url",movieDetails.getUrlstring());
                startActivity(in);
                Toast.makeText(getContext(),"Play",Toast.LENGTH_LONG).show();
            }
        });

        //Start download
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager) getActivity().getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(movieDetails.getUrlstring());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDescription("Downloading");
                long reference = manager.enqueue(request);
                Toast.makeText(getContext(),"Download Started",Toast.LENGTH_LONG).show();
            }
        });

    }

    void loadDetails(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                movieDetails = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getbyName(movieTitle);
                Log.i("details ", movieDetails.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        titleText.setText(movieDetails.getTitle());
                        String year = movieDetails.getRelease_date();
                        yearText.setText(year.substring(0,year.indexOf('-')));
                        videoQuality.setText(MovieQualityExtractor.extractQualtiy(movieDetails.getName()));
                        size.setText(sizetoReadablesize.humanReadableByteCountBin(Long.parseLong(movieDetails.getSize())));
                        overview.setText(movieDetails.getOverview());
                        listOfFiles.setText(movieDetails.getUrlstring());
                        Glide.with(getContext())
                                .load(TMDB_IMAGE_BASE_URL+movieDetails.getPoster_path())
                                .placeholder(new ColorDrawable(Color.BLACK))
                                .into(poster);
                    }
                });
            }});
        thread.start();
    }


    private void loadDetailsFile() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" ", "in thread");
                movieDetails = DatabaseClient
                        .getInstance(mCtx)
                        .getAppDatabase()
                        .fileDao()
                        .getByFileName(movieTitle);
                Log.i("details ", movieDetails.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if(movieDetails.getTitle()!=null){
                            titleText.setText(movieDetails.getTitle());
                            String year = movieDetails.getRelease_date();
                            yearText.setText(year.substring(0,year.indexOf('-')));
                            overview.setText(movieDetails.getOverview());
                            Glide.with(getContext())
                                    .load(TMDB_IMAGE_BASE_URL+movieDetails.getPoster_path())
                                    .placeholder(new ColorDrawable(Color.BLACK))
                                    .into(poster);
                        }else {
                            titleText.setText(movieDetails.getName());
                            titleText.setTextSize(25);
                            yearText.setText("N/A");
                        }
                        videoQuality.setText(MovieQualityExtractor.extractQualtiy(movieDetails.getName()));
                        size.setText(sizetoReadablesize.humanReadableByteCountBin(Long.parseLong(movieDetails.getSize())));
                        listOfFiles.setText(movieDetails.getUrlstring());

                    }
                });
            }});
        thread.start();
    }



}