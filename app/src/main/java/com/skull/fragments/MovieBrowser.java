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
import com.skull.activities.BrowseDetail;
import com.skull.adapter.MoviesAdapter;
import com.skull.databases.MoviesDatabaseManager;
import com.skull.models.DataModel;
import com.webservices.models.*;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;

import static android.R.id.list;


/**
 * Created by Administrator on 4/4/2017.
 */

public class MovieBrowser extends Fragment {
    RecyclerView mRecyclerView;
    private Activity mActivity;
    MoviesAdapter mAdapter;
    int categoryId = 0;

    public static MovieBrowser newInstance(Activity mActivity, int categoryId) {
        MovieBrowser myFragment = new MovieBrowser();
        myFragment.mActivity = mActivity;
        myFragment.categoryId = categoryId;

        return myFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragement_movie_browse, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);


        //RequestMovieByCat model = new RequestMovieByCat();
       // model.setId(""+categoryId);
      //  getBrowseCategories(model);

        MoviesDatabaseManager model=new MoviesDatabaseManager(mActivity);
       List<MovieByCategory>moviesList= model.getMovieByCategory(categoryId);
        if(moviesList == null)
        {

            moviesList=new ArrayList<>();
        }

        mAdapter = new MoviesAdapter(mActivity,moviesList);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;

    }

    public void getCategories(RequestMovieByCat model) {
        MyApplication.getInstance().showLoading(mActivity);
        Call<ApiResponse> call = MyApplication.getRestApi().getMovieByCategory(model);
        call.enqueue(new retrofit.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse> response, Retrofit retrofit) {
                MyApplication.getInstance().cancelDialog();
                List<MovieByCategory> modelCategory = response.body().getMoviesByCategory();
                if (modelCategory != null) {
                    MyApplication.getInstance().setModelMovieByCategory(modelCategory);
                    //Send movies to adapter via list
                    mAdapter = new MoviesAdapter(mActivity, MyApplication.getInstance().getModelMovieByCategory());
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-Movie by Cat", t.toString());
                MyApplication.getInstance().cancelDialog();
            }
        });
    }

}
