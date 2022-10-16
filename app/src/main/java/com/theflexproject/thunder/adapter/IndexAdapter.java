package com.theflexproject.thunder.adapter;

import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndexTVShow;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGoIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestMapleIndex;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;
import com.theflexproject.thunder.model.TVShowInfo.TVShowSeasonDetails;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

    private Context mCtx;
    private List<IndexLink> indexLinkList;

    public IndexAdapter(Context mCtx , List<IndexLink> indexLinkList) {
        this.mCtx = mCtx;
        this.indexLinkList = indexLinkList;
    }

    @Override
    public IndexViewHolder onCreateViewHolder(ViewGroup parent , int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.activity_recycler_view_layout , parent , false);
        return new IndexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IndexViewHolder holder , int position) {
        IndexLink t = indexLinkList.get(position);
        holder.textViewLink.setText(t.getLink());
        holder.textViewUsername.setText(t.getUsername());
        holder.textViewPassword.setText(t.getPassword());


        if (t.getUsername().length() > 0 && t.getPassword().length() > 0) {
            holder.textViewUsername.setVisibility(View.VISIBLE);
            holder.textViewPassword.setVisibility(View.VISIBLE);
        }

        holder.refreshIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.refreshIndexMovies(t);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteIndex(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (indexLinkList == null) {
            return 0;
        }
        return indexLinkList.size();
    }


    class IndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewLink, textViewUsername, textViewPassword;
        ImageButton refreshIndex;
        ImageButton delete;

        public IndexViewHolder(View itemView) {
            super(itemView);

            textViewLink = itemView.findViewById(R.id.textViewLink);
            textViewUsername = itemView.findViewById(R.id.textViewUsername);
            textViewPassword = itemView.findViewById(R.id.textViewPassword);
            refreshIndex = itemView.findViewById(R.id.refreshButton);
            delete = itemView.findViewById(R.id.deletebutton);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        void refreshIndexMovies(IndexLink indexLink) {
            Toast.makeText(itemView.getContext() , "Refreshing..." , Toast.LENGTH_LONG).show();
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
//
                    if (indexLink.getFolderType().equals("Movies")) {
                        DatabaseClient.getInstance(null).getAppDatabase().movieDao().deleteAllFromthisIndex(indexLink.getLink());
                        if (indexLink.getIndexType().equals("GDIndex")) {
                            try {
                                postRequestGDIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (indexLink.getIndexType().equals("GoIndex")) {
                            try {
                                postRequestGoIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if (indexLink.getIndexType().equals("MapleIndex")) {
                            try {
                                postRequestMapleIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (indexLink.getFolderType().equals("TVShows")) {
                        DatabaseClient.getInstance(null).getAppDatabase().episodeDao().deleteAllFromthisIndex(indexLink.getLink());
                        if ("GDIndex".equals(indexLink.getIndexType())) {
                            try {
                                postRequestGDIndexTVShow(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        //                                            case "GoIndex":
//                                                try {
//                                                    postRequestGoIndexTVShow(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
//                                                } catch (IOException | JSONException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                break;
//                                            case "MapleIndex":
//                                                try {
//                                                    postRequestMapleIndexTVShow(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                                break;

                    }
                }
            });
            thread.start();
            Toast.makeText(itemView.getContext() , "Done" , Toast.LENGTH_LONG).show();
        }

        public void deleteIndex(IndexLink indexLink) {
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    if (indexLink.getFolderType().equals("Movies")) {
                        DatabaseClient.getInstance(itemView.getContext())
                                .getAppDatabase()
                                .movieDao()
                                .deleteAllFromthisIndex(indexLink.getLink());
                    }
                    if (indexLink.getFolderType().equals("TVShows")) {
                        DatabaseClient.getInstance(itemView.getContext())
                                .getAppDatabase()
                                .episodeDao()
                                .deleteAllFromthisIndex(indexLink.getLink());


                        List<TVShowSeasonDetails> seasonsList = DatabaseClient
                                .getInstance(itemView.getContext())
                                .getAppDatabase()
                                .tvShowSeasonDetailsDao()
                                .getAll();

                        for(TVShowSeasonDetails season : seasonsList) {
                            List<Episode> episodeList = DatabaseClient
                                    .getInstance(itemView.getContext())
                                    .getAppDatabase()
                                    .episodeDao()
                                    .getFromSeasonOnly(season.getId());
                            if(episodeList==null || episodeList.size()==0){
                                DatabaseClient.getInstance(itemView.getContext()).getAppDatabase().tvShowSeasonDetailsDao().deleteById(season.getId());
                            }
                        }

                        List<TVShow> tvShowList = DatabaseClient
                                .getInstance(itemView.getContext())
                                .getAppDatabase()
                                .tvShowDao().getAll();

                        for(TVShow tvShow : tvShowList) {
                            List<TVShowSeasonDetails> seasonsInThisShow = DatabaseClient
                                    .getInstance(itemView.getContext())
                                    .getAppDatabase()
                                    .tvShowSeasonDetailsDao()
                                    .findByShowId(tvShow.getId());
                            if(seasonsInThisShow==null|| seasonsInThisShow.size()==0){
                                DatabaseClient.getInstance(itemView.getContext()).getAppDatabase().tvShowDao().deleteById(tvShow.getId());
                            }
                        }

                    }
                    DatabaseClient.getInstance(itemView.getContext())
                            .getAppDatabase()
                            .indexLinksDao()
                            .deleteIndexLink(indexLink.getLink());
                }
            });
            thread.start();
            Toast.makeText(itemView.getContext() , "Deleted" , Toast.LENGTH_LONG).show();
        }
    }

}


