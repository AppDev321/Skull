package com.skull.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rd.PageIndicatorView;
import com.skull.MyApplication;
import com.skull.R;
import com.skull.activities.HomeActivity;
import com.skull.adapter.BannerSliderAdapter;
import com.skull.adapter.DiscoverSectionDataAdapter;
import com.skull.databases.BannerDatabaseManager;
import com.skull.databases.CategoryDatabaseManager;
import com.skull.databases.MoviesDatabaseManager;
import com.skull.models.DiscoverModel;
import com.webservices.models.Banner;
import com.webservices.models.MovieByCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/4/2017.
 */

public class Discover extends Fragment {

    private HomeActivity mActivity;
    private Handler handler;
    private int delay = 5000; //milliseconds
    private ViewPager viewPager;
    private int page = 0;
    ArrayList<DiscoverModel> allSampleData;
    RecyclerView recyclerDiscove;


    Runnable runnable = new Runnable() {
        public void run() {
            if (4 == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    public static Discover newInstance(HomeActivity mActivity) {
        Discover myFragment = new Discover();
        myFragment.mActivity = mActivity;
        return myFragment;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discover, container, false);
        handler = new Handler();
        setupSlider(rootView);
        getDiscoverItems();


        recyclerDiscove = (RecyclerView) rootView.findViewById(R.id.recycler_discover);
        recyclerDiscove.setHasFixedSize(true);
        DiscoverSectionDataAdapter adapter = new DiscoverSectionDataAdapter(mActivity, allSampleData);
        recyclerDiscove.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerDiscove.setAdapter(adapter);


        return rootView;

    }

    private void setupSlider(View root) {
        BannerDatabaseManager model=new BannerDatabaseManager(mActivity);
        ;

        BannerSliderAdapter adapter = new BannerSliderAdapter(mActivity,model.getBannerList());

        viewPager = (ViewPager) root.findViewById(R.id.banner_viewpager);
        viewPager.setAdapter(adapter);


    }





    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    public void getDiscoverItems() {
        allSampleData = new ArrayList<DiscoverModel>();
        CategoryDatabaseManager model = new CategoryDatabaseManager(mActivity);
        MoviesDatabaseManager modelMovie = new MoviesDatabaseManager(mActivity);
        List<com.webservices.models.Category> cat = model.getDiscoverCategories();

        for (int i = 0; i < cat.size(); i++) {
            DiscoverModel dm = new DiscoverModel();
            dm.setId(cat.get(i).getId());
            dm.setName(cat.get(i).getName());
            dm.setUrl(cat.get(i).getImage());
            List<MovieByCategory> movieDiscover = modelMovie.getDiscoverCatMovies(cat.get(i).getId());
            dm.setAllItemsInSection(movieDiscover);
            allSampleData.add(dm);

        }


    }
}
