package com.theflexproject.thunder.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.theflexproject.thunder.R;
import com.theflexproject.thunder.model.GitHubResponse;

public class UpdateAppFragment extends BaseFragment {


    GitHubResponse[] gitHubResponses;
    TextView whatsNew;
    Button update;
    Button notNow;


    public UpdateAppFragment(GitHubResponse[] gitHubResponse) {
        // Required empty public constructor
        this.gitHubResponses=gitHubResponse;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_app , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view , @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view , savedInstanceState);

        update = mActivity.findViewById(R.id.update);
        notNow = mActivity.findViewById(R.id.NotNow);

        String whatsNewString = gitHubResponses[0].body;
        whatsNew= mActivity.findViewById(R.id.whatsNew);
        whatsNew.setText(whatsNewString);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(gitHubResponses[0].html_url)));
            }
        });
        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.getSupportFragmentManager().popBackStack();
            }
        });
    }


}