package com.theflexproject.thunder.adapter;

import static com.theflexproject.thunder.Constants.TMDB_IMAGE_BASE_URL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.R;

import java.util.List;

public class BannerRecyclerAdapter extends RecyclerView.Adapter<BannerRecyclerAdapter.MovieViewHolder> {

    Context context;
    List<Movie> mediaList;
    private BannerRecyclerAdapter.OnItemClickListener listener;
    public BannerRecyclerAdapter(Context context, List<Movie> mediaList, BannerRecyclerAdapter.OnItemClickListener listener) {
        this.context = context;
        this.mediaList = mediaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_banner, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        if(mediaList.get(position).getBackdrop_path()!=null){
            holder.name.setText(mediaList.get(position).getTitle());
            Glide.with(context)
                    .load(TMDB_IMAGE_BASE_URL +mediaList.get(position).getBackdrop_path())
                    .placeholder(new ColorDrawable(Color.BLACK))
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                    .into(holder.poster);
        }
    }

    @Override
    public int getItemCount() {
        return (mediaList==null)?0:mediaList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        ImageView poster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView4);
            poster= itemView.findViewById(R.id.moviePoster);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            listener.onClick(v,getAbsoluteAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

}
