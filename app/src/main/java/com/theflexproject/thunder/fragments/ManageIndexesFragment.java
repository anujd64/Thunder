package com.theflexproject.thunder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.IndexAdapter;
import com.theflexproject.thunder.database.DatabaseClient;
import com.theflexproject.thunder.model.IndexLink;

import java.util.List;
import java.util.concurrent.Executors;

public class ManageIndexesFragment extends BaseFragment{
    List<IndexLink> list;
    private RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    public ManageIndexesFragment() {
        // Required empty public constructor
    }

    public static ManageIndexesFragment newInstance(String param1, String param2) {
        ManageIndexesFragment fragment = new ManageIndexesFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_indexes , container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                list = DatabaseClient.getInstance(mActivity).getAppDatabase().indexLinksDao().getAll();
                                mActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView = view.findViewById(R.id.recyclerviewindexes);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                        recyclerView.setHasFixedSize(true);
                                        IndexAdapter indexAdapter = new IndexAdapter(mActivity,list);
                                        recyclerView.setAdapter(indexAdapter);

                                    }
                                });


            }
        });

        floatingActionButton = view.findViewById(R.id.floating_button_add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewIndexFragment nextFrag= new AddNewIndexFragment();
                mActivity.getSupportFragmentManager().beginTransaction().replace(R.id.containersettings, nextFrag).addToBackStack(null).commit();
            }
        });

    }



}