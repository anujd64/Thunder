package com.theflexproject.thunder.adapter;

import static com.theflexproject.thunder.utils.IndexUtils.deleteIndex;
import static com.theflexproject.thunder.utils.IndexUtils.disableIndex;
import static com.theflexproject.thunder.utils.IndexUtils.enableIndex;
import static com.theflexproject.thunder.utils.IndexUtils.getNoOfMedia;
import static com.theflexproject.thunder.utils.IndexUtils.refreshIndex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.model.IndexLink;

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
        View view = LayoutInflater.from(mCtx).inflate(R.layout.index_item , parent , false);
        return new IndexViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IndexViewHolder holder , int position) {
        IndexLink t = indexLinkList.get(position);
        holder.textViewLink.setText(t.getLink());
//        holder.textViewUsername.setText(t.getUsername());
//        holder.textViewPassword.setText(t.getPassword());

        holder.indexType.setText(t.getIndexType());
        holder.folderType.setText(t.getFolderType());

        int noOfMedia = getNoOfMedia(holder.itemView.getContext(), t);
        String s = noOfMedia+" "+t.getFolderType();
        holder.noOfMedia.setText(s);



//        if (t.getUsername().length() > 0 && t.getPassword().length() > 0) {
//            holder.textViewUsername.setVisibility(View.VISIBLE);
//            holder.textViewPassword.setVisibility(View.VISIBLE);
//        }

        holder.refreshIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext() , "Refreshing...",Toast.LENGTH_LONG).show();
                refreshIndex(holder.itemView.getContext() , t);

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!deleteIndex(holder.itemView.getContext(), t)){
                    Toast.makeText(view.getContext() , "Deleted",Toast.LENGTH_LONG).show();
                }
            }
        });

        if(t.getDisabled()==1){
            holder.enableIndex.setChecked(false);
        }
        holder.enableIndex.setOnCheckedChangeListener((buttonView , isChecked) -> {
            if(!isChecked){
                System.out.println("disable index pressed");
                disableIndex(holder.itemView.getContext(), t);
            }
            if(isChecked){
                enableIndex(holder.itemView.getContext(), t);
            }
        });


//        holder.actv.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String changeTo =s.toString();
//                 new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(changeTo.equals("GDIndex")){
//                            deleteIndex(t);
//                            postRequestGDIndex(t.getLink(),t.getUsername(),t.getPassword(), !t.folderType.equals("Movies"));
//                        }if(changeTo.equals("GoIndex")){
//                            deleteIndex(t);
//                            postRequestGoIndex(t.getLink(),t.getUsername(),t.getPassword(), !t.folderType.equals("Movies"));
//                        }if(changeTo.equals("Maple")){
//                            deleteIndex(t);
//                            postRequestMapleIndex(t.getLink(),t.getUsername(),t.getPassword(), !t.folderType.equals("Movies"));
//                        }
//                        if(changeTo.equals("SimpleProgram")){
//                            deleteIndex(t);
//                            postRequestSimpleProgramIndex(t.getLink(),t.getUsername(),t.getPassword(), !t.folderType.equals("Movies"));
//                        }
//                    }
//                }).start();
//
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }


    @Override
    public int getItemCount() {
        if (indexLinkList == null) {
            return 0;
        }
        return indexLinkList.size();
    }


    protected class IndexViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewLink, textViewUsername, textViewPassword, indexType, folderType, noOfMedia;
        ImageButton refreshIndex;
        ImageButton delete;
        SwitchCompat enableIndex;



        public IndexViewHolder(View itemView) {
            super(itemView);
            textViewLink = itemView.findViewById(R.id.textViewLink);
            indexType = itemView.findViewById(R.id.indexTypeInIndexAdapter);
            folderType = itemView.findViewById(R.id.folderTypeInIndexAdapter);
            noOfMedia = itemView.findViewById(R.id.noOfMedia);
            refreshIndex = itemView.findViewById(R.id.refreshButton);
            delete = itemView.findViewById(R.id.deletebutton);
            enableIndex = itemView.findViewById(R.id.enableIndexToggle);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

        }
    }

}


