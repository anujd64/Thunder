package com.theflexproject.thunder.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.theflexproject.thunder.R;
import com.theflexproject.thunder.adapter.FragmentViewPagerAdapter;

public class LibraryFragment extends BaseFragment {




    AutoCompleteTextView autoCompleteTextView;
    String[] sort_methods;
    ArrayAdapter<String> arrayAdapter;

    TabLayout tabLayout ;
    ViewPager2 viewPagerLibrary;
    FragmentViewPagerAdapter fragmentViewPagerAdapter;

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initWidgets();
        tabLayout = mActivity.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("TV Shows"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerLibrary.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerLibrary.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    private void initWidgets() {

//        sort_methods = mActivity.getResources().getStringArray(R.array.sort_methods);
//        arrayAdapter = new ArrayAdapter<>(mActivity,R.layout.dropdown_item,sort_methods);
//        autoCompleteTextView = mActivity.findViewById(R.id.AutoCompleteTextview);
//        autoCompleteTextView.setAdapter(arrayAdapter);

        tabLayout = mActivity.findViewById(R.id.tabLayout);
        viewPagerLibrary = mActivity.findViewById(R.id.viewPagerLibrary);
        fragmentViewPagerAdapter = new FragmentViewPagerAdapter(this);
        viewPagerLibrary.setSaveEnabled(false);
        viewPagerLibrary.setAdapter(fragmentViewPagerAdapter);


    }







//
//        Thread threadmediaList = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                        String methodChosen = autoCompleteTextView.getText().toString();
////                        Log.i("Method",methodChosen);
////                        switch(methodChosen){
////                            case "FileName":
////                                sortName();
////                            case "ReleaseDate":
////                                sortReleaseDate();
////                            case "Size":
////                                sortSize();
////                            case "TimeModified":
////                                sortTimeModified();
////                            case "IndexLink":
////                                sortIndexLink();
////                            case "Title":
////                                sortTitle();
////                        }
////
////                        Toast.makeText(mActivity.getApplicationContext(), "" + autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//        });
//        threadmediaList.setPriority(10);
//        threadmediaList.start();

//    void sortName(){
//        setOnClickListner(); /**set this every time*/
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmediaList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByFileName();
//                showRecycler(newmediaList);
//            }
//        });
//
//
//    }

//    void sortReleaseDate(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("inside sort function",methodChosen);
//                newmediaList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByRelease();
//                Log.i("inside sort function",newmediaList.toString());
//                showRecycler(newmediaList);
//            }
//        });
//
//    }

//    void sortSize(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmediaList = DatabaseClient
//                                .getInstance(mActivity)
//                                .getAppDatabase()
//                                .fileDao()
//                                .sortBySize();
////                showRecycler(newmediaList);
//            }
//        });
//
//    }
//
//    void sortIndexLink() {
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmediaList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByIndex();
////                showRecycler(newmediaList);
//            }
//        });
//
//
//    }
//
//    void sortTimeModified(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString();
//                Log.i("Method",methodChosen);
//                newmediaList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByTime();
////                showRecycler(newmediaList);
//            }
//        });
//
//    }
//
//    void sortTitle(){
//        Executors.newSingleThreadExecutor().execute(new Runnable() {
//            @Override
//            public void run() {
//                String methodChosen = autoCompleteTextView.getText().toString().trim();
//                Log.i("Method",methodChosen);
//                newmediaList = DatabaseClient
//                        .getInstance(mActivity)
//                        .getAppDatabase()
//                        .fileDao()
//                        .sortByTitle();
////                showRecycler(newmediaList);
//            }
//        });
//
//    }
}