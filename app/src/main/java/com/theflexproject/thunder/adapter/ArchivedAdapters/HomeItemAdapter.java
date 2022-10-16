//package com.theflexproject.thunder.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.fragment.app.FragmentActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.theflexproject.thunder.R;
//import com.theflexproject.thunder.fragments.MovieDetailsFragment;
//import com.theflexproject.thunder.fragments.TvShowDetailsFragment;
//import com.theflexproject.thunder.model.Movie;
//import com.theflexproject.thunder.model.TVShowInfo.TVShow;
//import com.theflexproject.thunder.utils.Pair;
//import com.theflexproject.thunder.utils.PairMovies;
//import com.theflexproject.thunder.utils.PairTvShows;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.HomeItemAdapterHolder> {
//
//
//    Context context;
//    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//
//    List<Pair> pairList;
//    List<PairMovies> pairMoviesList = new ArrayList<>();
//    List<PairTvShows> pairTvShowsList  = new ArrayList<>();
//    int cutList;
//
//    List<Movie> mediaList;
//    MovieRecyclerAdapter.OnItemClickListener listenerMovieItem;
//    MovieRecyclerAdapter movieRecyclerAdapter;
//
//    List<TVShow> tvShowList;
//    TVShowRecyclerAdapter.OnItemClickListener listenerTvItem;
//    TVShowRecyclerAdapter tvShowRecyclerAdapter;
//
//
//    public HomeItemAdapter(Context context , List<Pair> pairList , int cutList) {
//        this.context = context;
//        this.pairList = pairList;
//        this.cutList = cutList;
//        for (int i = 0; i < pairList.size(); i++) {
//            if (i < cutList) {
//                pairMoviesList.add((PairMovies) pairList.get(i));
//            } else {
//                pairTvShowsList.add((PairTvShows) pairList.get(i));
//            }
//        }
//    }
//
//    @NonNull
//    @Override
//    public HomeItemAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item , parent , false);
//        return new HomeItemAdapterHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull HomeItemAdapterHolder holder , int position) {
//            if(position<cutList){
//                mediaList = pairMoviesList.get(position).getmediaList();
//
//                holder.recyclerTitle.setText(pairMoviesList.get(position).getRecyclerTitle());
//                ScaleCenterItemLayoutManager linearLayoutManager = new ScaleCenterItemLayoutManager(holder.recyclerView.getContext() , LinearLayoutManager.HORIZONTAL , false);
//                linearLayoutManager.setInitialPrefetchItemCount(mediaList.size());
//
//
//                listenerMovieItem = new MovieRecyclerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onClick(View view , int moviePosition) {
//                        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment(mediaList.get(moviePosition).getId());
//                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                                .setCustomAnimations(R.anim.fade_in , R.anim.fade_out , R.anim.fade_in , R.anim.fade_out)
//                                .add(R.id.container , movieDetailsFragment).addToBackStack(null).commit();
//                    }
//                };
//                movieRecyclerAdapter = new MovieRecyclerAdapter(holder.recyclerView.getContext() , mediaList , listenerMovieItem);
//                holder.recyclerView.setLayoutManager(linearLayoutManager);
//                holder.recyclerView.setAdapter(movieRecyclerAdapter);
//                holder.recyclerView.setRecycledViewPool(viewPool);
//                movieRecyclerAdapter.notifyDataSetChanged();
//            }else if(position<pairList.size()){
//
//                tvShowList = pairTvShowsList.get(position-pairMoviesList.size()).getTvShowList();
//
//                holder.recyclerTitle.setText(pairTvShowsList.get(position-pairMoviesList.size()).getRecyclerTitle());
//                ScaleCenterItemLayoutManager linearLayoutManager1 = new ScaleCenterItemLayoutManager(holder.recyclerView.getContext() , LinearLayoutManager.HORIZONTAL , false);
//                linearLayoutManager1.setInitialPrefetchItemCount(tvShowList.size());
//
//                listenerTvItem = new TVShowRecyclerAdapter.OnItemClickListener() {
//                    @Override
//                    public void onClick(View view , int tvShowPosition) {
//                        TvShowDetailsFragment tvShowDetailsFragment = new TvShowDetailsFragment(tvShowList.get(tvShowPosition).getId());
//                        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
//                                .setCustomAnimations(R.anim.fade_in , R.anim.fade_out , R.anim.fade_in , R.anim.fade_out)
//                                .add(R.id.container , tvShowDetailsFragment).addToBackStack(null).commit();
//                    }
//                };
//
//                tvShowRecyclerAdapter = new TVShowRecyclerAdapter(holder.recyclerView.getContext() , tvShowList , listenerTvItem);
//                holder.recyclerView.setLayoutManager(linearLayoutManager1);
//                holder.recyclerView.setAdapter(tvShowRecyclerAdapter);
//                holder.recyclerView.setRecycledViewPool(viewPool);
//                tvShowRecyclerAdapter.notifyDataSetChanged();
//            }
//    }
//
//    @Override
//    public int getItemCount() {
//        return pairList.size();
//    }
//
//
//    class HomeItemAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        TextView recyclerTitle;
//        RecyclerView recyclerView;
//
//        public HomeItemAdapterHolder(@NonNull View itemView) {
//            super(itemView);
//            recyclerTitle = itemView.findViewById(R.id.recyclerTitleInHomeItem);
//            recyclerView = itemView.findViewById(R.id.recyclerInHomeItem);
//
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//        }
//    }
//
//    private void setAnimation(View itemView , int position) {
//        Animation popIn = AnimationUtils.loadAnimation(context , R.anim.pop_in);
//        itemView.startAnimation(popIn);
//    }
//
//
//}
//
