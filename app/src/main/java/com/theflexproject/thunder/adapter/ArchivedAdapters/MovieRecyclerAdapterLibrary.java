//package com.theflexproject.thunder.adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;
//import com.theflexproject.thunder.Constants;
//import com.theflexproject.thunder.R;
//import com.theflexproject.thunder.model.Movie;
//
//import java.util.List;
//
//
//
//public class MovieRecyclerAdapterLibrary extends RecyclerView.Adapter<MovieRecyclerAdapterLibrary.MovieRecyclerAdapterLibraryHolder> {
//
//    Context context;
//    List<Movie> mediaList;
//    private MovieRecyclerAdapterLibrary.OnItemClickListener listener;
//
//    public MovieRecyclerAdapterLibrary(Context context, List<Movie> mediaList, MovieRecyclerAdapterLibrary.OnItemClickListener listener) {
//        this.context = context;
//        this.mediaList = mediaList;
//        this.listener= listener;
//    }
//
//    @NonNull
//    @Override
//    public MovieRecyclerAdapterLibraryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_library, parent, false);
//        return new MovieRecyclerAdapterLibraryHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MovieRecyclerAdapterLibraryHolder holder, int position) {
//        if(mediaList.get(position).getTitle()!=null){
//            holder.name.setText(mediaList.get(position).getTitle());
//            Glide.with(context)
//                    .load(Constants.TMDB_IMAGE_BASE_URL+mediaList.get(position).getPoster_path())
//                    .placeholder(new ColorDrawable(Color.BLACK))
//                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
//                    .into(holder.poster);
//        }else{
//            holder.name.setText(mediaList.get(position).getFileName());
//            Glide.with(context).clear(holder.poster);
//        }
//
//        String year = mediaList.get(position).getRelease_date();
//        if(year!=null&&year.length()>4) {
//            holder.movieYear.setVisibility(View.VISIBLE);
//            holder.movieYear.setText(year.substring(0,year.indexOf('-')));
//        }else {
//            holder.movieYear.setVisibility(View.VISIBLE);
//            holder.movieYear.setText(year);
//        }
//
//        setAnimation(holder.itemView,position);
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return (mediaList==null)?0:mediaList.size();
//    }
//
//
//
//    public class MovieRecyclerAdapterLibraryHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        TextView name;
//        ImageView poster;
//        TextView movieYear;
//        public MovieRecyclerAdapterLibraryHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.movieName);
//            poster= itemView.findViewById(R.id.moviePoster2);
//            movieYear = itemView.findViewById(R.id.movieYear);
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            listener.onClick(v,getAbsoluteAdapterPosition());
//        }
//    }
//    public interface OnItemClickListener {
//        public void onClick(View view, int position);
//    }
//
//
//    private void setAnimation(View itemView , int position){
//        Animation popIn = AnimationUtils.loadAnimation(context,R.anim.pop_in);
//        itemView.startAnimation(popIn);
//    }
//}
