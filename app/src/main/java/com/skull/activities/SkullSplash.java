package com.skull.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.skull.MyApplication;
import com.skull.R;
import com.skull.databases.BannerDatabaseManager;
import com.skull.databases.DBAdapter;
import com.skull.views.TitleTextView;

import static android.R.attr.animation;

/**
 * Created by Administrator on 4/17/2017.
 */

public class SkullSplash extends Activity {

    TitleTextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        titleTextView = (TitleTextView) findViewById(R.id.text_splash);
        titleTextView.startAnimation(alphaAnimation);

        new LongOperation().execute("");


    }

    public class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            DBAdapter mDbHelper;
            mDbHelper = new DBAdapter(SkullSplash.this);
            mDbHelper.createDatabase();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    RefreshData dataRefresh = new RefreshData(SkullSplash.this, true);
                    //dataRefresh.getBanners();
                    BannerDatabaseManager bannerDB = new BannerDatabaseManager(SkullSplash.this);
                    if (bannerDB.getBannerList() != null && bannerDB.getBannerList().size() > 0) {
                        startActivity(new Intent(SkullSplash.this, HomeActivity.class));
                        finish();
                    } else {
                        if (MyApplication.getInstance().isOnline()) {
                            dataRefresh.getBanners();
                        } else {
                            MyApplication.getInstance().showServerFailureDialog(SkullSplash.this);
                        }
                    }


                }
            }, 4000);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
