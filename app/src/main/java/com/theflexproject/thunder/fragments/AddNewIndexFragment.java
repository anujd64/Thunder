package com.theflexproject.thunder.fragments;

import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGDIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestGoIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestMapleIndex;
import static com.theflexproject.thunder.utils.SendPostRequest.postRequestSimpleProgramIndex;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;


public class AddNewIndexFragment extends BaseFragment {

    private EditText indexLinkView;
    private EditText userNameView;
    private EditText passWordView;
    private Button save;
    private ProgressBar progress_circular;
    private TextView refreshSuggest;

//    private MaterialButtonToggleGroup toggleIndexTypeGroup;
//    private MaterialButton toggleIndexTypeButton;
//
//    private MaterialButtonToggleGroup toggleFolderTypeGroup;
//    private MaterialButton toggleFolderTypeButton;


    private AutoCompleteTextView actv1;
    private AutoCompleteTextView actv2;

//    private RadioGroup radioIndexTypeGroup;
//    private RadioButton radioIndexTypeButton;
//
//    private RadioGroup radioFolderTypeGroup;
//    private RadioButton radioFolderTypeButton;


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
        progress_circular = view.findViewById(R.id.progress_circular);
        refreshSuggest = view.findViewById(R.id.suggestRefresh);

//        toggleIndexTypeGroup = view.findViewById(R.id.index_type_group);
//        toggleFolderTypeGroup = view.findViewById(R.id.folder_type_group);


        actv1 = view.findViewById(R.id.actv);
        actv2 = view.findViewById(R.id.actv2);

        String[] index_types = mActivity.getResources().getStringArray(R.array.index_types);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(mActivity , R.layout.item_index_type , index_types);
        actv1.setAdapter(arrayAdapter1);


        String[] folder_types = mActivity.getResources().getStringArray(R.array.folder_types);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(mActivity , R.layout.item_folder_type , folder_types);
        actv2.setAdapter(arrayAdapter2);





//        radioIndexTypeGroup=(RadioGroup)view.findViewById(R.id.rb_group);
//        radioFolderTypeGroup=(RadioGroup)view.findViewById(R.id.folderTypeRadioGroup);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexLink indexLink = new IndexLink();
                if(indexLinkView.getText().toString().equals("")){
                    indexLinkView.setError("Enter index link");
                }else{
                    indexLink.setLink(indexLinkView.getText().toString());
                }
                indexLink.setUsername(userNameView.getText().toString());
                indexLink.setPassword(passWordView.getText().toString());
                indexLink.setIndexType(actv1.getText().toString());
                indexLink.setFolderType(actv2.getText().toString());



//                int IndexTypeCheckedButtonId = toggleIndexTypeGroup.getCheckedButtonId();
//                toggleIndexTypeButton  = view.findViewById(IndexTypeCheckedButtonId);
//                int FolderTypeCheckedButtonId = toggleFolderTypeGroup.getCheckedButtonId();
//                toggleFolderTypeButton = view.findViewById(FolderTypeCheckedButtonId);

//                int indexTypeButtonSelectedId=radioIndexTypeGroup.getCheckedRadioButtonId();
//                int folderTypeButtonSelectedId=radioFolderTypeGroup.getCheckedRadioButtonId();
//                radioIndexTypeButton= mActivity.findViewById(indexTypeButtonSelectedId);
//                radioFolderTypeButton= mActivity.findViewById(folderTypeButtonSelectedId);


                try{
                    if(indexLink.getLink().length()<1){
                       indexLinkView.setError("Invalid Link");
                    }
//                    if(toggleIndexTypeButton == null || toggleFolderTypeButton == null){
//                    refreshSuggest.setVisibility(View.VISIBLE);
//                    refreshSuggest.setText("Select Index and Folder Type");
//                    }
//                    if(toggleIndexTypeButton != null || toggleFolderTypeButton != null && indexLink.getUsername().length()<1 && indexLink.getPassword().length()<1){
//                            userNameView.setVisibility(View.GONE);
//                            passWordView.setVisibility(View.GONE);
//                        indexLink.setIndexType(toggleIndexTypeButton.getText().toString());
//                        indexLink.setFolderType(toggleFolderTypeButton.getText().toString());
//                    }


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
                            }
                            else if(indexLink.getLink()!=null) { DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().insert(indexLink);
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        save.setText("Adding");
                                        progress_circular.setVisibility(View.VISIBLE);
                                    }
                                });



                                String folderType = indexLink.getFolderType();
                                String indexType = indexLink.getIndexType();

                                String link =indexLink.getLink();
                                String user =indexLink.getUsername();
                                String pass =indexLink.getPassword();

                                System.out.println("Before setting id"+ indexLink.getId());

                                IndexLink indexLinkAgain = DatabaseClient.getInstance(mActivity)
                                        .getAppDatabase()
                                        .indexLinksDao()
                                        .find(link);

                                int index_id = indexLinkAgain.getId();

                                System.out.println("After setting id"+ indexLinkAgain.getId());

                                if(folderType.equals("Movies")) {
                                    if(indexType.equals("GDIndex")) {
                                        postRequestGDIndex(link,user,pass,false,index_id);
                                    }
                                    if(indexType.equals("GoIndex")) {
                                        postRequestGoIndex(link,user,pass,false,index_id);
                                    }
                                    if(indexType.equals("Maple")){
                                        postRequestMapleIndex(link,user,pass,false,index_id);
                                    }
                                    if(indexType.equals("SimpleProgram")){
                                        postRequestSimpleProgramIndex(link,user,pass,false,index_id);
                                    }
                                }

                                if(folderType.equals("TVShows")){
                                    if(indexType.equals("GDIndex")) {
                                        postRequestGDIndex(link,user,pass,true,index_id);
                                    }
                                    if(indexType.equals("GoIndex")) {
                                        postRequestGoIndex(link,user,pass,true,index_id);
                                    }
                                    if(indexType.equals("MapleIndex")){
                                        postRequestMapleIndex(link,user,pass,true,index_id);
                                    }
                                    if(indexType.equals("SimpleProgram")){
                                        postRequestSimpleProgramIndex(link,user,pass,true,index_id);
                                    }
                                }
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        save.setText("Done");
                                        progress_circular.setVisibility(View.GONE);
                                    }
                                });
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