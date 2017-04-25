package com.skull.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.skull.R;
import com.skull.activities.HomeActivity;
import com.skull.adapter.CategoryAdapter;
import com.skull.databases.CategoryDatabaseManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/5/2017.
 */

public class Category extends Fragment {

    private CategoryAdapter mAdapter;
    private HomeActivity mActivity;
    RecyclerView mRecyclerView;

    public static Category newInstance(HomeActivity mActivity) {
        Category myFragment = new Category();
        myFragment.mActivity = mActivity;

        return myFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.category_fragment, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_category);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        CategoryDatabaseManager model = new CategoryDatabaseManager(mActivity);
        List<com.webservices.models.Category> categoryList = model.getBrowseCategories();
        if (categoryList == null) {

            categoryList = new ArrayList<>();
        }

        mAdapter = new CategoryAdapter(mActivity, categoryList);
        mRecyclerView.setAdapter(mAdapter);


        return rootView;

    }


}
