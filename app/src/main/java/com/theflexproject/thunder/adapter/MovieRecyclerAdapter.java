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
import com.theflexproject.thunder.model.File;
import com.theflexproject.thunder.R;

import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.MovieRecyclerHolder> {

    Context context;
    List<File> movieList;
    private OnItemClickListener listener;
    public MovieRecyclerAdapter(Context context, List<File> movieList,OnItemClickListener listener) {
        this.context = context;
        this.movieList = movieList;
        this.listener= listener;
    }

    @NonNull
    @Override
    public MovieRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_recycler, parent, false);
        return new MovieRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerHolder holder, int position) {
        holder.name.setText(movieList.get(position).getTitle());

        Glide.with(context)
                .load(TMDB_IMAGE_BASE_URL+movieList.get(position).getPoster_path())
                .placeholder(new ColorDrawable(Color.BLACK))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                .into(holder.poster);



    }

    @Override
    public int getItemCount() {
        return (movieList==null)?0:movieList.size();
    }

    public interface OnItemClickListener {
        public void onClick(View view, int position);
    }

    public class MovieRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        ImageView poster;
        public MovieRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.movieName);
            poster= itemView.findViewById(R.id.moviePoster2);
            itemView.setOnClickListener(this);
        }



        @Override
        public void onClick(View v) {
            listener.onClick(v,getAbsoluteAdapterPosition());
        }
    }
}
