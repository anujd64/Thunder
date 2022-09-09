package com.theflexproject.thunder.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.theflexproject.thunder.MainActivity;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;
import com.theflexproject.thunder.utils.SendPostRequest;

import java.io.IOException;


public class AddNewIndexFragment extends Fragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText indexLinkView;
    private EditText userNameView;
    private EditText passWordView;
    private Button save;
    private TextView refreshSuggest;


    public static AddNewIndexFragment newInstance(){
        return new AddNewIndexFragment();
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_index, container, false);

        return view;
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        indexLinkView =view.findViewById(R.id.indexlink);
        userNameView = view.findViewById(R.id.username);
        passWordView = view.findViewById(R.id.password);
        save = view.findViewById(R.id.save);
        refreshSuggest = view.findViewById(R.id.suggestRefresh);




        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                IndexLink indexLink = new IndexLink();
                indexLink.setLink(indexLinkView.getText().toString());
                indexLink.setUsername(userNameView.getText().toString());
                indexLink.setPassword(passWordView.getText().toString());
                if(indexLink.getUsername().length()<1 && indexLink.getPassword().length()<1){
                    userNameView.setVisibility(View.GONE);
                    passWordView.setVisibility(View.GONE);
                }


                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(DatabaseClient.getInstance(MainActivity.mCtx).getAppDatabase().indexLinksDao().find(indexLink.getLink())!=null){
                                //refresh instead
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshSuggest.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else {
                                DatabaseClient.getInstance(MainActivity.mCtx).getAppDatabase().indexLinksDao().insert(indexLink);
                                SendPostRequest snd = new SendPostRequest();
                                save.setText("Adding");
                                try {
                                    if(indexLink.getUsername().length()<1 && indexLink.getPassword().length()<1){
                                        snd.postRequest(indexLink.getLink());
                                    }
                                    else {
                                        snd.postRequest(indexLink.getLink(),indexLink.getUsername(),indexLink.getPassword());
                                    }
                                    save.setText("Done");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                thread.start();
                if(save.getText().equals("Done")){
                    getActivity().getSupportFragmentManager().popBackStack();
                }




            }
        });




    }

}