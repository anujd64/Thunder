package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.utils.SendGetRequestTMDB.tmdbGetByID;
import static com.theflexproject.thunder.utils.StringUtils.tmdbIdExtractor_FromLink;
import static com.theflexproject.thunder.utils.StringUtils.tmdbIdExtractor_FromLink_TV;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.model.Movie;
import com.theflexproject.thunder.model.MyMedia;
import com.theflexproject.thunder.model.TVShowInfo.Episode;
import com.theflexproject.thunder.model.TVShowInfo.TVShow;


public class ChangeTMDBFragment extends BaseFragment {

    MyMedia myMedia;

    EditText tmdbLinkText;
    Button changeTMDBIdButton;
    TextView invalidTMDBLink;

    public ChangeTMDBFragment() {
        // Required empty public constructor
    }

    public ChangeTMDBFragment(MyMedia myMedia) {
        this.myMedia=myMedia;
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_tmdb , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        tmdbLinkText = view.findViewById(R.id.tmdbLinkText);
        changeTMDBIdButton = view.findViewById(R.id.changeTMDBIdButton);
        invalidTMDBLink = view.findViewById(R.id.invalidTMDBLink);


        changeTMDBIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(tmdbLinkText.getText()!=null){

                            String link = tmdbLinkText.getText().toString();
                            if(myMedia instanceof Movie){
                                //send get request tmdb movie
                                long id = tmdbIdExtractor_FromLink(link);

                                if(id!=0){
                                    tmdbGetByID(myMedia,id,false);
                                }
                                else {
                                    boolean failed = false;
                                    try {
                                        id =Long.parseLong(link);
                                        tmdbGetByID(myMedia,id,false);
                                    }catch (Exception e){
                                        failed = true;
                                        System.out.println("Failed to parse long"+e);
                                        mActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                tmdbLinkText.setError("Invalid Link");
                                            }
                                        });
                                    }
                                }
                            }
                            else if(myMedia instanceof TVShow || myMedia instanceof  Episode){
                                //send get request tmdb tvshow
                                long id = tmdbIdExtractor_FromLink_TV(link);
                                if(id!=0){
                                    tmdbGetByID(myMedia,id,true);
                                }
                                else if(link.length()<15){
                                    try {
                                        id =Long.parseLong(link);
                                        tmdbGetByID(myMedia,id,true);
                                    }catch (Exception e){
                                        System.out.println("Failed to parse long"+e);
                                    }
                                }
                                else {
                                    invalidTMDBLink.setVisibility(View.VISIBLE);
                                }

                            }

                        }

                    }
                });
                thread.start();

                Toast.makeText(mActivity,"Changed",Toast.LENGTH_LONG).show();
            }
        });



    }




}