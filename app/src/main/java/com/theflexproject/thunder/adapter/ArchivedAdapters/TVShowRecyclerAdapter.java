package com.theflexproject.thunder.adapter.ArchivedAdapters;

//public class MediaAdapter extends RecyclerView.Adapter<TVShowRecyclerAdapter.TVShowRecyclerAdapterHolder> {
//
//    Context context;
//    List<MyMedia> mediaList;
//    private TVShowRecyclerAdapter.OnItemClickListener listener;
//
////    List<TVShow> tvShowList;
////    List<TVShowSeasonDetails> seasonsList;
////    boolean isSeason;
//
//    public TVShowRecyclerAdapter(Context context, List<MyMedia> mediaList, TVShowRecyclerAdapter.OnItemClickListener listener) {
//        this.context = context;
//        this.mediaList = mediaList;
//        this.listener= listener;
//    }
//
////    public TVShowRecyclerAdapter(Context context, List<TVShowSeasonDetails> seasonsList, boolean isSeason, TVShowRecyclerAdapter.OnItemClickListener listener) {
////        //for using as season adapter
////        this.isSeason =isSeason;
////        this.context = context;
////        this.seasonsList = seasonsList;
////        this.listener= listener;
////    }
//
//    @NonNull
//    @Override
//    public TVShowRecyclerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tvshow_item, parent, false);
//        return new TVShowRecyclerAdapterHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull TVShowRecyclerAdapterHolder holder, int position) {
//
//        if(mediaList.get(position) instanceof TVShow){
//            TVShow tvShow = ((TVShow)mediaList.get(position));
//            if(tvShow.getName()!=null){
//                holder.name.setText(tvShow.getName());
//                Glide.with(context)
//                        .load(Constants.TMDB_IMAGE_BASE_URL+tvShow.getPoster_path())
//                        .placeholder(new ColorDrawable(Color.BLACK))
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
//                        .into(holder.poster);
//            }
//        }
//        if(mediaList.get(position) instanceof TVShowSeasonDetails){
//            TVShowSeasonDetails tvShowSeason = ((TVShowSeasonDetails)mediaList.get(position));
//            if(tvShowSeason.getName()!=null){
//                holder.name.setText(tvShowSeason.getName());
//                String poster_path =null;
//                if(tvShowSeason.getPoster_path()!=null){
//                    poster_path = tvShowSeason.getPoster_path();
//                    Glide.with(context)
//                            .load(Constants.TMDB_IMAGE_BASE_URL+poster_path)
//                            .placeholder(new ColorDrawable(Color.BLACK))
//                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
//                            .into(holder.poster);
//                }
//
//            }
//        }
//
//
//        setAnimation(holder.itemView,position);
//
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        return mediaList.size();
//    }
//
//
//
//    public class TVShowRecyclerAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//
//        TextView name;
//        ImageView poster;
//        public TVShowRecyclerAdapterHolder(@NonNull View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.tvShowName);
//            poster= itemView.findViewById(R.id.tvShowPoster);
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
