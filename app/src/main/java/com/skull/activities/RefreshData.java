package com.skull.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.skull.MyApplication;
import com.skull.databases.BannerDatabaseManager;
import com.skull.databases.CategoryDatabaseManager;
import com.skull.databases.DBAdapter;
import com.skull.databases.MoviesDatabaseManager;
import com.webservices.models.ApiResponse;
import com.webservices.models.Banner;
import com.webservices.models.Category;
import com.webservices.models.MovieByCategory;

import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Administrator on 4/24/2017.
 */

public class RefreshData {
    Activity mActivity;
    boolean isSplash = false;

    public RefreshData(Activity mActivity, boolean isSplash) {
        this.mActivity = mActivity;
        this.isSplash = isSplash;

    }

    void onErrorFromServer() {
        BannerDatabaseManager bannerDB = new BannerDatabaseManager(mActivity);
        if (bannerDB.getBannerList().size() > 0) {
            if (isSplash) {
                mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
                mActivity.finish();
            } else {
                HomeActivity.homeActivity.slidingTabs();
            }
        }
    }

    public void getBanners() {
        MyApplication.getInstance().showLoading(mActivity);
        Call<ApiResponse> call = MyApplication.getRestApi().getMovieBanners();
        call.enqueue(new retrofit.Callback<ApiResponse>() {

            @Override
            public void onResponse(retrofit.Response<ApiResponse> response, Retrofit retrofit) {

                //  MyApplication.getInstance().cancelDialog();
                List<Banner> modelBanner = response.body().getBannerList();
                if (modelBanner != null) {
                    MyApplication.getInstance().setModelBanner(modelBanner);
                    getCategories();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-Banner", t.toString());
                MyApplication.getInstance().cancelDialog();

               // onErrorFromServer();

                showNetWorkMsg();
            }
        });
    }

    public void getCategories() {

        Call<ApiResponse> call = MyApplication.getRestApi().getCategories();
        call.enqueue(new retrofit.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse> response, Retrofit retrofit) {

                List<Category> modelCategory = response.body().getCategoryList();
                if (modelCategory != null) {
                    MyApplication.getInstance().setModelCategory(modelCategory);
                    getAllMoviesList();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-Category", t.toString());
                MyApplication.getInstance().cancelDialog();
                showNetWorkMsg();
            }
        });
    }

    public void getAllMoviesList() {
        Call<ApiResponse> call = MyApplication.getRestApi().getAllMoviesList();
        call.enqueue(new retrofit.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit.Response<ApiResponse> response, Retrofit retrofit) {

                List<MovieByCategory> modelCategory = response.body().getmMoviesList();
                if (modelCategory != null) {
                    MyApplication.getInstance().setModelAllMovies(modelCategory);
                    insertDataInDatabase();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-Category", t.toString());
                MyApplication.getInstance().cancelDialog();
                onErrorFromServer();
                showNetWorkMsg();
            }
        });
    }



    public void insertDataInDatabase() {
        BannerDatabaseManager bannerDB = new BannerDatabaseManager(mActivity);
        CategoryDatabaseManager categoryDB = new CategoryDatabaseManager(mActivity);
        MoviesDatabaseManager movieDB = new MoviesDatabaseManager(mActivity);

        bannerDB.deleteBannerData();
        movieDB.deleteMoviesData();
        categoryDB.deleteCategoryData();


        List<Banner> modelBanner = MyApplication.getInstance().getModelBanner();
        for (int i = 0; i < modelBanner.size(); i++) {
            bannerDB.insertBannerData(modelBanner.get(i));
            if (i == (modelBanner.size() - 1)) {
                List<MovieByCategory> modelMovies = MyApplication.getInstance().getModelAllMovies();
                for (int j = 0; j < modelMovies.size(); j++) {
                    movieDB.insertMoviesData(modelMovies.get(j));
                    if (j == (modelMovies.size() - 1)) {
                        List<Category> modelCategory = MyApplication.getInstance().getModelCategory();
                        for (int k = 0; k < modelCategory.size(); k++) {
                            categoryDB.insertCategoryData(modelCategory.get(k));
                            if (k == (modelCategory.size() - 1)) {
                                MyApplication.getInstance().cancelDialog();

                                if (isSplash) {
                                    mActivity.startActivity(new Intent(mActivity, HomeActivity.class));
                                    mActivity.finish();
                                }
                                else
                                {
                                    HomeActivity.homeActivity.slidingTabs();
                                }
                            }
                        }

                    }
                }
            }
        }
    }


    public void showNetWorkMsg()
    {
        MyApplication.getInstance().showServerFailureDialog(mActivity);
       // Toast.makeText(mActivity,"There is some problem in network.\nPlease try again",Toast.LENGTH_LONG).show();
    }
}
