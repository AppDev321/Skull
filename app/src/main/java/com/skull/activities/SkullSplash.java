package com.skull.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import com.skull.MyApplication;
import com.skull.databases.BannerDatabaseManager;
import com.skull.databases.DBAdapter;

/**
 * Created by Administrator on 4/17/2017.
 */

public class SkullSplash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    if (MyApplication.getInstance().isOnline()) {


                        RefreshData dataRefresh = new RefreshData(SkullSplash.this, true);
                        //dataRefresh.getBanners();
                        BannerDatabaseManager bannerDB = new BannerDatabaseManager(SkullSplash.this);
                        if (bannerDB.getBannerList() != null && bannerDB.getBannerList().size() > 0) {
                            startActivity(new Intent(SkullSplash.this, HomeActivity.class));
                            finish();
                        } else {

                            dataRefresh.getBanners();
                        }

                    } else {
                        MyApplication.getInstance().showServerFailureDialog(SkullSplash.this);
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
