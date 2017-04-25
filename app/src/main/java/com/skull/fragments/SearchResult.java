package com.skull.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skull.MyApplication;
import com.skull.R;
import com.skull.adapter.MoviesAdapter;
import com.skull.databases.MoviesDatabaseManager;
import com.webservices.models.ApiResponse;
import com.webservices.models.MovieByCategory;
import com.webservices.models.RequestMovieByCat;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;


/**
 * Created by Administrator on 4/4/2017.
 */

public class SearchResult extends Fragment {
    RecyclerView mRecyclerView;
    private Activity mActivity;
    MoviesAdapter mAdapter;
    List<MovieByCategory>moviesList;

    public static SearchResult newInstance(Activity mActivity, List<MovieByCategory>moviesList) {
        SearchResult myFragment = new SearchResult();
        myFragment.mActivity = mActivity;
        myFragment.moviesList = moviesList;

        return myFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_movie_browse, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new MoviesAdapter(mActivity,moviesList);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;

    }



}
