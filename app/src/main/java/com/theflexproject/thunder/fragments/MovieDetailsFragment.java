package com.theflexproject.thunder.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.theflexproject.thunder.Constants.TMDB_BACKDROP_IMAGE_BASE_URL;
import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
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
import com.theflexproject.thunder.adapter.FileItemAdapter;
import com.theflexproject.thunder.adapter.ScaleCenterItemLayoutManager;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Genre;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.player.PlayerActivity;
import com.theflexproject.thunder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MovieDetailsFragment extends BaseFragment{

    BlurView blurView;
    ViewGroup rootView;
    View decorView;


    int movieId;
    TextView titleText;
    TextView yearText;
    TextView runtime;
//    TextView size;
    TextView overview;
    Button play;
    Button download;
//    Button externalPlayer;


    TableRow director;
    TableRow writer;
    TableRow genres;



    TextView directorText;
    TextView writerText;
    TextView genresText;
    TextView ratingsText;
    ImageView ratings;

    ImageView poster;
    ImageView backdrop;
    Movie movieDetails;


    RecyclerView recyclerViewMovieFiles;
    List<MyMedia> movieFileList;
    FileItemAdapter fileAdapter;
    FileItemAdapter.OnItemClickListener listenerFileItem;
    Movie largestFile;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }
    public MovieDetailsFragment(int id){
        this.movieId =id;
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

        initWidgets(view);
        loadDetails();
        loadMovieFilesRecycler();
//        setMyOnClickLiseners();

    }

    private void initWidgets(View view) {
//        blurView = view.findViewById(R.id.blurView4);
//        decorView =  ((Activity) mActivity).getWindow().getDecorView();
//        rootView = decorView.findViewById(android.R.id.content);
//


        titleText = view.findViewById(R.id.title_text);
        yearText = view.findViewById(R.id.year_text);
        runtime = view.findViewById(R.id.RuntimeText);
//        size = view.findViewById(R.id.size);
        overview = view.findViewById(R.id.overviewdesc);
        poster = view.findViewById(R.id.moviePosterInDetails);
        backdrop = view.findViewById(R.id.movieBackdrop);

        director = view.findViewById(R.id.Director);
        writer = view.findViewById(R.id.WrittenBy);
        genres = view.findViewById(R.id.Genres);
        directorText = view.findViewById(R.id.DirectorText);
        writerText = view.findViewById(R.id.WrittenByText);
        genresText = view.findViewById(R.id.GenresText);
        ratings = view.findViewById(R.id.ratings);
        ratingsText = view.findViewById(R.id.ratingsText);

        DisplayMetrics displayMetrics = mActivity.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        if(dpWidth < 600f){poster.setScaleType(ImageView.ScaleType.CENTER_CROP);}
        else{poster.setScaleType(ImageView.ScaleType.CENTER_INSIDE);}


        play = view.findViewById(R.id.play);
        download = view.findViewById(R.id.download);

//        blurBottom();
    }

    private void loadDetails(){
       try {
           Thread thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   Log.i("movieId",movieId+"");

                   movieDetails = DatabaseClient
                           .getInstance(mActivity)
                           .getAppDatabase()
                           .movieDao()
                           .byId(movieId);

                   Log.i("insideLoadDetails",movieDetails.toString());
                   mActivity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if(movieDetails.getTitle()!=null){
                               titleText.setText(movieDetails.getTitle());
                           }else {
                               titleText.setText(movieDetails.getFileName());
                           }
                           if(movieDetails.getRuntime()>0){
                               String result = StringUtils.runtimeIntegerToString(movieDetails.getRuntime());
                               runtime.setVisibility(View.VISIBLE);
                               runtime.setText(result);
                           }
                           if(movieDetails.getGenres()!=null){
                               genres.setVisibility(View.VISIBLE);
                               ArrayList<Genre> genres = movieDetails.getGenres();
                               StringBuilder sb = new StringBuilder();
                               for (int i=0;i<genres.size();i++) {
                                   Genre genre = genres.get(i);
                                   if(i== genres.size()-1){sb.append(genre.getName());}
                                   else {sb.append(genre.getName()).append(", ");}
                               }
                               genresText.setText(sb.toString());
                           }
                           if(movieDetails.getVote_average()!=0){
                               ratings.setVisibility(View.VISIBLE);
                               ratingsText.setVisibility(View.VISIBLE);
                               ratingsText.setText((int)(movieDetails.getVote_average()*10)+"%");
                           }
                           String year = movieDetails.getRelease_date();
                           if(movieDetails.getRelease_date()!=null && movieDetails.getRelease_date().length()>1) {
                               yearText.setVisibility(View.VISIBLE);
                               yearText.setText(year.substring(0,year.indexOf('-')));
                           }
                           if(movieDetails.getOverview()!=null){overview.setText(movieDetails.getOverview());}
                           if(movieDetails.getPoster_path()!=null) {
                               Glide.with(mActivity)
                                       .load(TMDB_IMAGE_BASE_URL + movieDetails.getPoster_path())
                                       .placeholder(new ColorDrawable(Color.BLACK))
                                       .into(poster);
                           }
                           if(movieDetails.getBackdrop_path()!=null) {
                               Glide.with(mActivity)
                                       .load(TMDB_BACKDROP_IMAGE_BASE_URL + movieDetails.getBackdrop_path())
                                       .placeholder(new ColorDrawable(Color.BLACK))
                                       .into(backdrop);
                           }else {
                               if(movieDetails.getPoster_path()!=null) {
                                   Glide.with(mActivity)
                                           .load(TMDB_BACKDROP_IMAGE_BASE_URL + movieDetails.getPoster_path())
                                           .placeholder(new ColorDrawable(Color.BLACK))
                                           .into(backdrop);
                               }
                           }
                       }
                   });
               }});
           thread.start();
       }catch (NullPointerException exception){Log.i("Error",exception.toString());}

        setMyOnClickLiseners();
    }

    void blurBottom(){

        ((Activity) mActivity).getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        ((Activity) mActivity).getWindow().setStatusBarColor(Color.TRANSPARENT);
        final float radius = 14f;
        final Drawable windowBackground = ((Activity) mActivity).getWindow().getDecorView().getBackground();

        blurView.setupWith(rootView, new RenderScriptBlur(mActivity))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(radius);
        blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        blurView.setClipToOutline(true);
    }


    private void setMyOnClickLiseners(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                largestFile = DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .movieDao()
                        .byIdLargest(movieId);
            }
        });
        thread.start();

        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean savedEXT = sharedPreferences.getBoolean("EXTERNAL_SETTING", false);

        if(savedEXT){
            //External Player
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToLastPlayed();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(largestFile.getUrlString()));
                    intent.setDataAndType(Uri.parse(largestFile.getUrlString()), "video/*");
                    startActivity(intent);
                }
            });
        }else {
            //Play video
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        addToLastPlayed();
                        Intent in = new Intent(getActivity(), PlayerActivity.class);
                        in.putExtra("url",movieDetails.getUrlString());
                        startActivity(in);
                        Toast.makeText(getContext(),"Play",Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }

        //Start download
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager manager = (DownloadManager) mActivity.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(largestFile.getUrlString());
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDescription("Downloading");
                long reference = manager.enqueue(request);
                Toast.makeText(getContext(),"Download Started",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void addToLastPlayed() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatabaseClient.getInstance(getContext()).getAppDatabase().movieDao().updatePlayed(movieDetails.getId());
            }
        });
        thread.start();
    }

    private void loadMovieFilesRecycler() {
        setMyOnClickLiseners();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i(" " , "in thread");
                movieFileList =(List<MyMedia>)(List<?>) DatabaseClient
                        .getInstance(mActivity)
                        .getAppDatabase()
                        .movieDao()
                        .getAllById(movieId);
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("movieFileList" , movieFileList.toString());
                        recyclerViewMovieFiles = mActivity.findViewById(R.id.recyclerAvailableMovieFiles);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
                        recyclerViewMovieFiles.setLayoutManager(linearLayoutManager);
                        recyclerViewMovieFiles.setHasFixedSize(true);
                        fileAdapter = new FileItemAdapter(getContext() , movieFileList , listenerFileItem);
                        recyclerViewMovieFiles.setAdapter(fileAdapter);
                        fileAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }

}