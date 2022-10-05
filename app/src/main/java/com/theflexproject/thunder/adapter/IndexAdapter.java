package com.theflexproject.thunder.adapter;

import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndex;
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

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

    private Context mCtx;
    private List<IndexLink> indexLinkList;

    public IndexAdapter(Context mCtx, List<IndexLink> indexLinkList) {
        this.mCtx = mCtx;
        this.indexLinkList = indexLinkList;
    }

    @Override
    public IndexViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.activity_recycler_view_layout, parent, false);
        return new IndexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IndexViewHolder holder, int position) {
        IndexLink t = indexLinkList.get(position);
        holder.textViewLink.setText(t.getLink());
        holder.textViewUsername.setText(t.getUsername());
        holder.textViewPassword.setText(t.getPassword());



        if(t.getUsername().length()>0 && t.getPassword().length()>0){
            holder.textViewUsername.setVisibility(View.VISIBLE);
            holder.textViewPassword.setVisibility(View.VISIBLE);
        }

        holder.refreshIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.refreshIndexMovies();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteIndex();
            }
        });
    }
    @Override
    public int getItemCount () {
        if(indexLinkList==null){
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

        void refreshIndexMovies(){
            Toast.makeText(itemView.getContext(),"Refreshing...",Toast.LENGTH_LONG).show();
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    try {
                        DatabaseClient.getInstance(null).getAppDatabase().fileDao().deleteAllFromthisIndex(textViewLink.getText().toString());
                        String link = textViewLink.getText().toString();
                        IndexLink indexLink =DatabaseClient.getInstance(null).getAppDatabase().indexLinksDao().find(link);
                        switch (indexLink.getType()) {
                            case "GDIndex":
                                postRequestGDIndex(textViewLink.getText().toString(), textViewUsername.getText().toString(), textViewPassword.getText().toString());
                                break;
                            case "GoIndex":
                                postRequestGoIndex(textViewLink.getText().toString(), textViewUsername.getText().toString(), textViewPassword.getText().toString());

                                break;
                            case "MapleIndex":
                                postRequestMapleIndex(textViewLink.getText().toString(), textViewUsername.getText().toString(), textViewPassword.getText().toString());
                                break;
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

            Toast.makeText(itemView.getContext(),"Done",Toast.LENGTH_LONG).show();


        }

        public void deleteIndex() {
            Thread thread = new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    DatabaseClient.getInstance(itemView.getContext())
                            .getAppDatabase()
                            .fileDao()
                            .deleteAllFromthisIndex(textViewLink.getText().toString());
                    DatabaseClient.getInstance(itemView.getContext())
                            .getAppDatabase()
                            .indexLinksDao()
                            .deleteIndexLink(textViewLink.getText().toString());
                }
            });
            thread.start();
            Toast.makeText(itemView.getContext(),"Deleted",Toast.LENGTH_LONG).show();
        }
    }

    }


