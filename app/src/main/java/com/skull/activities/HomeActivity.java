package com.skull.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.downloadmanager.activity.ADownloadManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skull.R;
import com.skull.adapter.ViewPagerAdapter;
import com.skull.fragments.Category;
import com.skull.fragments.Discover;
import com.skull.utils.Config;
import com.skull.utils.NotificationUtils;
import com.skull.views.CustomTabLayout;

/**
 * Created by Administrator on 4/4/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private CustomTabLayout tabLayout;
    ViewPager viewPager;
    ImageView btnDownload, btnRefresh, btnSearch;

    public static HomeActivity homeActivity;


    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        homeActivity = this;
        slidingTabs();

        btnDownload = (ImageView) findViewById(R.id.toolbar_download);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ADownloadManager.class));
            }
        });
        btnRefresh = (ImageView) findViewById(R.id.toolbar_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(HomeActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm")
                        .setMessage("Are you sure you want to refresh data.\nThis will take time to update your movies data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                RefreshData refreshData = new RefreshData(HomeActivity.this, false);
                                refreshData.getBanners();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        });
        btnSearch = (ImageView) findViewById(R.id.toolbar_btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
            }
        });

        //Register for fcm notification hurrah
        FCMRegistration();
    }

    public void slidingTabs() {

        Log.e("enjoy", "Dehsat Gardi ka maza");

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setupViewPager(viewPager);

        tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Discover discover = Discover.newInstance(HomeActivity.this);
        Category browse = Category.newInstance(HomeActivity.this);
        adapter.addFragment(discover, getResources().getString(R.string.tab_1));
        adapter.addFragment(browse, getResources().getString(R.string.tab_2));

        viewPager.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());
        super.onResume();
    }

    public void FCMRegistration() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        // String regId = pref.getString("regId", null);


    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }
    }


}
