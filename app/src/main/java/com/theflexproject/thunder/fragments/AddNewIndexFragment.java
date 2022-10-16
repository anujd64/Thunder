package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndexTVShow;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGoIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestMapleIndex;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;

import org.json.JSONException;

import java.io.IOException;


public class AddNewIndexFragment extends BaseFragment {

    public static final String TAG = "ActionBottomDialog";
    private EditText indexLinkView;
    private EditText userNameView;
    private EditText passWordView;
    private Button save;
    private TextView refreshSuggest;

    private RadioGroup radioIndexTypeGroup;
    private RadioButton radioIndexTypeButton;

    private RadioGroup radioFolderTypeGroup;
    private RadioButton radioFolderTypeButton;

    public static AddNewIndexFragment newInstance(){
        return new AddNewIndexFragment();
    }


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_index, container, false);
    }


    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        indexLinkView =view.findViewById(R.id.indexlink);
        userNameView = view.findViewById(R.id.username);
        passWordView = view.findViewById(R.id.password);
        save = view.findViewById(R.id.save);
        refreshSuggest = view.findViewById(R.id.suggestRefresh);

        radioIndexTypeGroup=(RadioGroup)mActivity.findViewById(R.id.rb_group);
        radioFolderTypeGroup=(RadioGroup)mActivity.findViewById(R.id.folderTypeRadioGroup);





        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                IndexLink indexLink = new IndexLink();
                indexLink.setLink(indexLinkView.getText().toString());
                indexLink.setUsername(userNameView.getText().toString());
                indexLink.setPassword(passWordView.getText().toString());



                int indexTypeButtonSelectedId=radioIndexTypeGroup.getCheckedRadioButtonId();
                int folderTypeButtonSelectedId=radioFolderTypeGroup.getCheckedRadioButtonId();
                radioIndexTypeButton= mActivity.findViewById(indexTypeButtonSelectedId);
                radioFolderTypeButton= mActivity.findViewById(folderTypeButtonSelectedId);


                try{
                    if(indexLink.getLink().length()<1){
                        refreshSuggest.setVisibility(View.VISIBLE);
                        refreshSuggest.setText("Enter Index Link");
                    }
                    if(radioIndexTypeButton ==null || radioFolderTypeButton == null){
                    refreshSuggest.setVisibility(View.VISIBLE);
                    refreshSuggest.setText("Select Index and Folder Type");
                    }
                    if(indexLink.getUsername().length()<1 && indexLink.getPassword().length()<1){
                            userNameView.setVisibility(View.GONE);
                            passWordView.setVisibility(View.GONE);
                    }

                    indexLink.setIndexType(radioIndexTypeButton.getText().toString());
                    indexLink.setFolderType(radioFolderTypeButton.getText().toString());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().find(indexLink.getLink())!=null){
                                //refresh instead
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshSuggest.setVisibility(View.VISIBLE);
                                    }
                                });
                            }else {
                                DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().insert(indexLink);
                                save.setText("Adding");
                                if(radioFolderTypeButton.getText().toString().equals("Movies")) {
                                    switch (radioIndexTypeButton.getText().toString()) {
                                        //Case statements
                                        case "GDIndex":
                                            try {
                                                postRequestGDIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "GoIndex":
                                            try {
                                                postRequestGoIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                                            } catch (IOException | JSONException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        case "MapleIndex":
                                            try {
                                                postRequestMapleIndex(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                    }
                                    save.setText("Done");
                                }else {
                                        switch (radioIndexTypeButton.getText().toString()) {
                                            //Case statements
                                            case "GDIndex":
                                                try {
                                                    postRequestGDIndexTVShow(indexLink.getLink() , indexLink.getUsername() , indexLink.getPassword());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                                break;
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
                                        save.setText("Done");
                                }


                            }

                        }
                    });
                    thread.start();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if(save.getText().equals("Done")){
                        mActivity.getSupportFragmentManager().popBackStack();
                    }

            }
        });
    }

}