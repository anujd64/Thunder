package com.theflexproject.thunder.adapter;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.player.PlayerActivity;
import com.theflexproject.thunder.utils.MovieQualityExtractor;
import com.theflexproject.thunder.utils.sizetoReadablesize;

import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;


public class FileItemAdapter extends RecyclerView.Adapter<FileItemAdapter.FileItemAdapterHolder> {

    Context context;
    List<MyMedia> mediaList;
    private FileItemAdapter.OnItemClickListener listener;

    public FileItemAdapter(Context context, List<MyMedia> mediaList, FileItemAdapter.OnItemClickListener listener) {
        this.context = context;
        this.mediaList = mediaList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public FileItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);
        return new FileItemAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileItemAdapterHolder holder, int position) {

        if (mediaList.get(position) instanceof Movie) {
            if (((Movie)mediaList.get(position)).getUrlString() != null) {
                holder.link.setText(((Movie)mediaList.get(position)).getUrlString());
                holder.fileName.setText(((Movie)mediaList.get(position)).getFileName());
                holder.size.setText(new sizetoReadablesize().humanReadableByteCountBin(Long.parseLong(((Movie)mediaList.get(position)).getSize())));
            }
            String qualityStr = MovieQualityExtractor.extractQualtiy(((Movie)mediaList.get(position)).getFileName());
            if(qualityStr!=null){
                holder.quality.setVisibility(View.VISIBLE);
                holder.quality.setText(qualityStr);
            }


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.playMedia(((Movie)mediaList.get(position)).getUrlString());
                    addToLastPlayed();

                }
                private void addToLastPlayed() {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseClient.getInstance(context).getAppDatabase().movieDao().updatePlayed(((Movie)mediaList.get(position)).getId());
                        }
                    });
                    thread.start();
                }
            });


            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  holder.downloadMedia(((Movie)mediaList.get(position)).getUrlString());
                }
            });



        }

        if (mediaList.get(position) instanceof Episode) {
            if (((Episode)mediaList.get(position)).getUrlString() != null) {
                holder.link.setText(((Episode)mediaList.get(position)).getUrlString());
                holder.fileName.setText(((Episode)mediaList.get(position)).getFileName());
                holder.size.setText(new sizetoReadablesize().humanReadableByteCountBin(Long.parseLong(((Episode)mediaList.get(position)).getSize())));
            }
            String qualityStr = MovieQualityExtractor.extractQualtiy(((Episode)mediaList.get(position)).getFileName());
            if(qualityStr!=null){
                holder.quality.setVisibility(View.VISIBLE);
                holder.quality.setText(qualityStr);
            }


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.playMedia(((Episode)mediaList.get(position)).getUrlString());
                    addToLastPlayed();
                }
                private void addToLastPlayed() {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            DatabaseClient.getInstance(context).getAppDatabase().episodeDao().updatePlayed(((Episode)mediaList.get(position)).getId());
                        }
                    });
                    thread.start();
                }
            });
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.downloadMedia(((Episode)mediaList.get(position)).getUrlString());
                }
            });
        }

        setAnimation(holder.itemView , position);

        }



    @Override
        public int getItemCount () {
            return mediaList.size();
        }


        public class FileItemAdapterHolder extends RecyclerView.ViewHolder{

            BlurView blurView;
            ViewGroup rootView;
            View decorView;

            TextView fileName;
            TextView link;
            TextView size;
            TextView quality;
            Button play;
            Button download;
            SharedPreferences sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
            boolean savedEXT = sharedPreferences.getBoolean("EXTERNAL_SETTING", false);


            public FileItemAdapterHolder(@NonNull View itemView) {
                super(itemView);
                blurView = itemView.findViewById(R.id.blurView2);
                decorView =  ((Activity) context).getWindow().getDecorView();
                rootView = decorView.findViewById(android.R.id.content);

                fileName = itemView.findViewById(R.id.fileNameInFileItem);
                link = itemView.findViewById(R.id.fileLinkInFileItem);
                size = itemView.findViewById(R.id.sizeTextInFileItem);
                quality = itemView.findViewById(R.id.videoQualityTextInFileItem);
                play = itemView.findViewById(R.id.playInFileItem);
                download = itemView.findViewById(R.id.downloadInFileItem);

                blurBottom();
//                itemView.setOnClickListener(this);
            }


            private void playMedia(String url) {
                if (savedEXT) {
                    //External Player
                    Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(url));
                    intent.setDataAndType(Uri.parse(url) , "video/*");
                    context.startActivity(intent);
                } else {
                    //Play video
                    Intent in = new Intent(context , PlayerActivity.class);
                    in.putExtra("url" , (url));
                    context.startActivity(in);
                    Toast.makeText(context , "Play" , Toast.LENGTH_LONG).show();
                }
            }
            private void downloadMedia(String url) {
                DownloadManager manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(url);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setDescription("Downloading");
                long reference = manager.enqueue(request);
                Toast.makeText(context,"Download Started",Toast.LENGTH_LONG).show();
            }

            private void blurBottom(){

                ((Activity) context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                ((Activity) context).getWindow().setStatusBarColor(Color.TRANSPARENT);
                final float radius = 14f;
                final Drawable windowBackground = ((Activity) context).getWindow().getDecorView().getBackground();

                blurView.setupWith(rootView, new RenderScriptBlur(context))
                        .setFrameClearDrawable(windowBackground)
                        .setBlurRadius(radius);
                blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
                blurView.setClipToOutline(true);
            }
        }
        public interface OnItemClickListener {
            public void onClick(View view , int position);
        }


        private void setAnimation (View itemView ,int position){
            Animation popIn = AnimationUtils.loadAnimation(context , R.anim.pop_in);
            itemView.startAnimation(popIn);
        }
}

