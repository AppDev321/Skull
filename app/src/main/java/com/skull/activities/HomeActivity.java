package com.skull.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.downloadmanager.activity.ADownloadManager;
import com.google.firebase.messaging.FirebaseMessaging;
import com.skull.R;
import com.skull.adapter.ViewPagerAdapter;
import com.skull.databases.MoviesDatabaseManager;
import com.skull.fragments.Category;
import com.skull.fragments.Discover;
import com.skull.fragments.SearchResult;
import com.skull.utils.Config;
import com.skull.utils.NotificationUtils;
import com.skull.views.CustomTabLayout;
import com.webservices.models.MovieByCategory;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 4/4/2017.
 */

public class HomeActivity extends AppCompatActivity {
    private CustomTabLayout tabLayout;
    ViewPager viewPager;
    ImageView btnDownload, btnRefresh,btnSearch;

    public static HomeActivity homeActivity;
    LinearLayout mSearchLayout;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mSearchLayout = (LinearLayout) findViewById(R.id.layout_search);
        mSearchLayout.setVisibility(View.GONE);
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
                RefreshData refreshData = new RefreshData(HomeActivity.this, false);
                refreshData.getBanners();

            }
        });
        btnSearch=(ImageView)findViewById(R.id.toolbar_btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // searchView();
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

    private void setupSearchViewPager(ViewPager viewPager,List<MovieByCategory> movieByCategories) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Discover discover = Discover.newInstance(HomeActivity.this);
        Category browse = Category.newInstance(HomeActivity.this);
        SearchResult searchResult = SearchResult.newInstance(HomeActivity.this,movieByCategories);


        adapter.addFragment(discover, getResources().getString(R.string.tab_1));
        adapter.addFragment(browse, getResources().getString(R.string.tab_2));
        adapter.addFragment(searchResult,"Search");

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


    void searchView() {
        final MoviesDatabaseManager model = new MoviesDatabaseManager(this);

        mSearchLayout.setVisibility(View.VISIBLE);
        final EditText mSearchText = (EditText) findViewById(R.id.edit_search);
        ImageView backArrow = (ImageView) findViewById(R.id.btn_search_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                mSearchLayout.setVisibility(View.GONE);
                mSearchText.setText("");
                mSearchText.setHint("Search");
                mSearchText.clearFocus();

                viewPager.setAdapter(null);
                setupViewPager(viewPager);
            }
        });

        ImageView btnCross = (ImageView) findViewById(R.id.btn_search_cross);
        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                mSearchText.setText("");
                mSearchText.setHint("Search");
                mSearchText.clearFocus();

                viewPager.setAdapter(null);
                setupViewPager(viewPager);

            }
        });


        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim().toLowerCase(Locale.US);
                searchText = searchText.replace("'", "");
                searchText = searchText.replace("-", "");
                searchText = searchText.replace(" ", "");
                List<MovieByCategory> listMovies = model.getSearchMoviesList(searchText);
                //  viewPager = (ViewPager) findViewById(R.id.viewPager);
                viewPager.setAdapter(null);
                setupSearchViewPager(viewPager, listMovies);
                viewPager.setCurrentItem(3);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

        if(mSearchLayout.getVisibility()==View.VISIBLE)
        {
            mSearchLayout.setVisibility(View.GONE);
            viewPager.setAdapter(null);
            setupViewPager(viewPager);
        }
        else
        {
            super.onBackPressed();
        }

    }
}
