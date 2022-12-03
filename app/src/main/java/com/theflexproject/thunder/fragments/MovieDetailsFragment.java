package com.theflexproject.thunder.fragments;

import static android.content.Context.DOWNLOAD_SERVICE;
import static com.theflexproject.thunder.Constants.TMDB_BACKDROP_IMAGE_BASE_URL;

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
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
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
    String movieFileName;
    ImageView logo;
    TextView titleText;
    TextView yearText;
    TextView runtime;
    ImageButton play;
    ImageButton changeSource;
    ImageButton addToList;
    ImageButton download;
    TextView overview;
//    Button changeTMDB;
//    TextView size;
//    Button externalPlayer;


    TableRow director;
    TableRow writer;
    TableRow genres;




    TextView directorText;
    TextView writerText;
    TextView genresText;
    TextView ratingsText;
    ImageView dot1;
    ImageView ratings;

    ImageView poster;
    ImageView backdrop;
    Movie movieDetails;


    RecyclerView recyclerViewMovieFiles;
    List<Movie> movieFileList;
    FileItemAdapter fileAdapter;
    FileItemAdapter.OnItemClickListener listenerFileItem;
    Movie largestFile;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }
    public MovieDetailsFragment(int id){
        this.movieId =id;
    }

    public MovieDetailsFragment(String fileName) {
        this.movieFileName = fileName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_details_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initWidgets(view);
        loadDetails();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadMovieFilesRecycler();
            }
        },300);

//        setMyOnClickLiseners();

    }

    private void initWidgets(View view) {
//        blurView = view.findViewById(R.id.blurView4);
//        decorView =  ((Activity) mActivity).getWindow().getDecorView();
//        rootView = decorView.findViewById(android.R.id.content);
//


        titleText = view.findViewById(R.id.title_text);
        logo = view.findViewById(R.id.movieLogo);
        yearText = view.findViewById(R.id.year_text);
        runtime = view.findViewById(R.id.RuntimeText);
//        size = view.findViewById(R.id.size);
        overview = view.findViewById(R.id.overviewdesc);
//        poster = view.findViewById(R.id.moviePosterInDetails);
        backdrop = view.findViewById(R.id.movieBackdrop);

        director = view.findViewById(R.id.Director);
        writer = view.findViewById(R.id.WrittenBy);
        genres = view.findViewById(R.id.Genres);
        directorText = view.findViewById(R.id.DirectorText);
        writerText = view.findViewById(R.id.WrittenByText);
        genresText = view.findViewById(R.id.GenresText);
        ratings = view.findViewById(R.id.ratings);
        dot1 = view.findViewById(R.id.dot);
        ratingsText = view.findViewById(R.id.ratingsText);



        play = view.findViewById(R.id.play);
        download = view.findViewById(R.id.downloadButton);
        addToList = view.findViewById(R.id.addToListButton);
        changeSource = view.findViewById(R.id.changeSourceButton);
//        changeTMDB = view.findViewById(R.id.changeTMDBId);

//        blurView = view.findViewById(R.id.blurView4);

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
                   if(movieDetails==null){
                       movieDetails = DatabaseClient
                               .getInstance(mActivity)
                               .getAppDatabase()
                               .movieDao()
                               .getByFileName(movieFileName);
                   }

                   if(movieDetails!=null){
                       Log.i("insideLoadDetails",movieDetails.toString());
                       mActivity.runOnUiThread(new Runnable() {
                           @Override
                           public void run() {


                               String logoLink = movieDetails.getLogo_path();
                               System.out.println("Logo Link"+logoLink);

                               if(logoLink!=null && !logoLink.equals("")){
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
                               if(logoLink!=null && logoLink.equals("") && movieDetails.getTitle()!=null){
                                   titleText.setVisibility(View.VISIBLE);
                                   titleText.setText(movieDetails.getTitle());
                               }else {
                                   titleText.setVisibility(View.VISIBLE);
                                   titleText.setText(movieFileName);
                               }

                               if(movieDetails.getRuntime()>0){
                                   String result = StringUtils.runtimeIntegerToString(movieDetails.getRuntime());
                                   runtime.setVisibility(View.VISIBLE);
                                   runtime.setText(result);
                               }
                               if(movieDetails.getGenres()!=null){
                                   genresText.setVisibility(View.VISIBLE);
                                   ArrayList<Genre> genres = movieDetails.getGenres();
                                   StringBuilder sb = new StringBuilder();
                                   for (int i=0;i<genres.size();i++) {
                                       Genre genre = genres.get(i);
                                       if(i == genres.size()-1 && genre != null){sb.append(genre.getName());}
                                       else if(genre!= null){sb.append(genre.getName()).append(", ");}
                                   }
                                   genresText.setText(sb.toString());
                               }
                               if(movieDetails.getVote_average()!=0){
//                                   ratings.setVisibility(View.VISIBLE);
                                   dot1.setVisibility(View.VISIBLE);
                                   ratingsText.setVisibility(View.VISIBLE);
                                   String rating = (int)(movieDetails.getVote_average()*10)+"%";
                                   ratingsText.setText(rating);
                               }
                               String year = movieDetails.getRelease_date();
                               if(movieDetails.getRelease_date()!=null && movieDetails.getRelease_date().length()>1) {
                                   yearText.setVisibility(View.VISIBLE);
                                   yearText.setText(year.substring(0,year.indexOf('-')));
                               }
                               if(movieDetails.getOverview()!=null){overview.setVisibility(View.VISIBLE); overview.setText(movieDetails.getOverview());}
                               if(movieDetails.getPoster_path()!=null) {
//                                           Glide.with(mActivity)
//                                                   .load(TMDB_IMAGE_BASE_URL + movieDetails.getPoster_path())
//                                                   .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                                   .placeholder(new ColorDrawable(Color.TRANSPARENT))
//                                                   .into(poster);
                               }
                               if(movieDetails.getBackdrop_path()!=null) {
//                                   Glide.with(mActivity)
//                                           .load(TMDB_BACKDROP_IMAGE_BASE_URL + movieDetails.getBackdrop_path())
//                                           .apply(bitmapTransform(new BlurTransformation(10, 3)))
//                                           .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                           .into(backdrop);

                                           Glide.with(mActivity)
                                                   .load(TMDB_BACKDROP_IMAGE_BASE_URL + movieDetails.getBackdrop_path())
                                                   .apply(new RequestOptions()
                                                           .fitCenter()
                                                           .override(Target.SIZE_ORIGINAL))
//                                                   .apply(bitmapTransform(new BlurTransformation(5, 3)))
                                                   .placeholder(new ColorDrawable(Color.TRANSPARENT))
                                                   .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                   .into(backdrop);
                               }else {
                                   if(movieDetails.getPoster_path()!=null) {
                                               Glide.with(mActivity)
                                                       .load(TMDB_BACKDROP_IMAGE_BASE_URL + movieDetails.getPoster_path())
                                                       .apply(new RequestOptions()
                                                               .fitCenter()
                                                               .override(Target.SIZE_ORIGINAL))
//                                                       .apply(bitmapTransform(new BlurTransformation(5, 3)))
                                                       .placeholder(new ColorDrawable(Color.TRANSPARENT))
                                                       .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                       .into(backdrop);
                                   }
                               }

                           }
                       });
                   }

               }});
           thread.start();
       }catch (NullPointerException exception){Log.i("Error",exception.toString());}

        setMyOnClickLiseners();
    }

    String urlLogo ;

//    private String getLogo(Long id)  {
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    URL url = new URL("https://webservice.fanart.tv/v3/movies/"+ id +"?api_key="+getFanartApiKey());
//
//                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//                    conn.setRequestMethod("GET");
//
//                    int code = conn.getResponseCode();
//                    System.out.println(("HTTP CODE" + code));
//
//
//                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//                    StringBuilder sb = new StringBuilder();
//                    for (int c; (c = br.read()) >= 0;)
//                        sb.append((char)c);
//                    br.close();
//
//                    Gson om = new Gson();
//                    FanArtMovieResponseModel root = om.fromJson(sb.toString(), FanArtMovieResponseModel.class);
//                    System.out.println("FanArtMovieResponse"+root);
//                    if(root!=null && root.getHdmovielogo()!=null){
//                        for (int i = 0; i < root.getHdmovielogo().size(); i++) {
//                            if(root.getHdmovielogo().get(i).getLang().equals("en") || root.getHdmovielogo().get(i).getLang().equals("hi")){
//                                urlLogo = root.getHdmovielogo().get(i).getUrl().toString();
//                                break;
//                            }
//                        }
//                    }
//                    if(urlLogo==null && root!=null && root.getMovielogo()!=null){
//                        root.getMovielogo().get(0).getUrl().toString();
//
//                    }
//                    System.out.println("inside getLogo"+urlLogo);
//                }catch (IOException e) {
//                    System.out.println(e.toString());
//                }
//
//            }
//        });
//        thread.start();
//
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return urlLogo;
//
//    }

    //to blur the backdrop
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

//        Start download
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


        addToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(movieDetails.getAddToList()!=1){

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseClient
                                    .getInstance(mActivity)
                                    .getAppDatabase()
                                    .movieDao()
                                    .updateAddToList(movieId);
                        }
                    }).start();

                    Toast.makeText(mActivity , "Added To List" , Toast.LENGTH_LONG).show();
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseClient
                                    .getInstance(mActivity)
                                    .getAppDatabase()
                                    .movieDao()
                                    .updateRemoveFromList(movieId);
                        }
                    }).start();

                    Toast.makeText(mActivity , "Removed From List" , Toast.LENGTH_LONG).show();
                }

            }
        });

//        changeTMDB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangeTMDBFragment changeTMDBFragment = new ChangeTMDBFragment(movieDetails);
//
//                mActivity.getSupportFragmentManager().beginTransaction()
//                        .setCustomAnimations(R.anim.fade_in,R.anim.fade_out,R.anim.fade_in,R.anim.fade_out)
//                        .add(R.id.container,changeTMDBFragment).addToBackStack(null).commit();
//            }
//        });





        final int[] checkedItem = {-1};
        int indexSelected = 0;
        changeSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomFileListDialogFragment dialog = new CustomFileListDialogFragment(mActivity,changeSource,(List<MyMedia>)(List<?>) movieFileList);

               mActivity.
                       getSupportFragmentManager()
                       .beginTransaction()
                       .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                       .add(android.R.id.content, dialog)
                       .addToBackStack(null)
                       .commit();
//                dialog.show(mActivity.getSupportFragmentManager(),null);
                dialog.mOnInputListener = new CustomFileListDialogFragment.OnInputListener() {
                    @Override
                    public void sendInput(int selection) {
                        largestFile = movieFileList.get(selection);
                        System.out.println("selected file"+largestFile.getFileName());

                    }
                };
//              largestFile = movieFileList.get(indexSelected);

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
//
//                alertDialog.setTitle("Choose a File");
//
//                String[] listItems = new String[movieFileList.size()];
//                for(int i=0; i<movieFileList.size();i++){
//                    listItems[i]=movieFileList.get(i).getFileName();
//                }
//                // the function setSingleChoiceItems is the function which
//                // builds the alert dialog with the single item selection
//                alertDialog.setSingleChoiceItems(listItems, checkedItem[0] , (dialog, which) -> {
//                    checkedItem[0]=which;
//                    largestFile = movieFileList.get(checkedItem[0]);
//                    dialog.dismiss();
//                });
//
//                // set the negative button if the user is not interested to select or change already selected item
//                alertDialog.setNegativeButton("Cancel", (dialog, which) -> {
//
//                });
//
//                // create and build the AlertDialog instance with the AlertDialog builder instance
//                AlertDialog customAlertDialog = alertDialog.create();
//
//                // show the alert dialog when the button is clicked
//                customAlertDialog.show();
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
                if(movieId==0){
                    movieFileList = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .movieDao()
                            .getAllByFileName(movieFileName);
                }else {
                    movieFileList = DatabaseClient
                            .getInstance(mActivity)
                            .getAppDatabase()
                            .movieDao()
                            .getAllById(movieId);
                }
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("movieFileList" , movieFileList.toString());
                        recyclerViewMovieFiles = mActivity.findViewById(R.id.recyclerAvailableMovieFiles);
                        ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(mActivity , LinearLayoutManager.VERTICAL , false);
                        recyclerViewMovieFiles.setLayoutManager(linearLayoutManager);
                        recyclerViewMovieFiles.setHasFixedSize(true);
                        fileAdapter = new FileItemAdapter(getContext() , (List<MyMedia>)(List<?>) movieFileList);
                        recyclerViewMovieFiles.setAdapter(fileAdapter);
                        fileAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        thread.start();
    }
}