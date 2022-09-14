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

import com.bumptech.glide.Glide;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.player.PlayerActivity;
import com.theflexproject.thunder.utils.MovieQualityExtractor;
import com.theflexproject.thunder.utils.sizetoReadablesize;

public class MovieDetailsFragment extends BaseFragment{
    String movieFileName;
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
    public MovieDetailsFragment(String name){
        movieFileName =name;
    }


    public static MovieDetailsFragment newInstance(String param1, String param2) {

        return new MovieDetailsFragment();
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

        loadDetails();
        //Play video
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
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
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        //Start download
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager) mActivity.getSystemService(DOWNLOAD_SERVICE);
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
       try {
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   movieDetails = DatabaseClient
                           .getInstance(mCtx)
                           .getAppDatabase()
                           .fileDao()
                           .getByFileName(movieFileName);

                   Log.i("title",movieDetails.toString());
                   mActivity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if(movieDetails.getTitle()!=null){
                               titleText.setText(movieDetails.getTitle());
                           }else {
                               titleText.setText(movieDetails.getName());
                               titleText.setTextSize(25);
                               yearText.setText("N/A");
                           }
                           String year = movieDetails.getRelease_date();
                           if(movieDetails.getRelease_date()!=null && movieDetails.getRelease_date().length()>1) {yearText.setText(year.substring(0,year.indexOf('-')));}else {yearText.setText("N/A");}
                           if(movieDetails.getOverview()!=null){overview.setText(movieDetails.getOverview());}
                           if(movieDetails.getPoster_path()!=null) {
                               Glide.with(mActivity)
                                       .load(TMDB_IMAGE_BASE_URL + movieDetails.getPoster_path())
                                       .placeholder(new ColorDrawable(Color.BLACK))
                                       .into(poster);
                           }
                           String quality = MovieQualityExtractor.extractQualtiy(movieDetails.getName());
                           if(quality!=null){
                               videoQuality.setText(quality);
                           }
                           size.setText(sizetoReadablesize.humanReadableByteCountBin(Long.parseLong(movieDetails.getSize())));
                           listOfFiles.setText(movieDetails.getUrlstring());
                       }
                   });
               }});
           thread.start();
       }catch (NullPointerException exception){Log.i("Error",exception.toString());}
    }

}