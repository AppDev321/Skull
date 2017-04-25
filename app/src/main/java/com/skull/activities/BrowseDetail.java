package com.skull.activities;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.skull.MyApplication;
import com.skull.R;
import com.skull.adapter.ViewPagerAdapter;
import com.skull.databases.CategoryDatabaseManager;
import com.skull.fragments.MovieBrowser;
import com.skull.utils.Constants;
import com.skull.views.CustomTabLayout;
import com.skull.views.TitleTextView;
import com.squareup.picasso.Picasso;
import com.webservices.models.Category;

import java.util.List;


/**
 * Created by Administrator on 4/6/2017.
 */

public class BrowseDetail extends AppCompatActivity {
    ViewPager viewPager;
    CustomTabLayout tabLayout;
    String[] titles = {"All"};//, "Free", "Premium", "Rental"};

    int pos = 0;
    KenBurnsView imageCategory;
    Category modelCategory;
    List<Category> mCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_detail);
        imageCategory = (KenBurnsView) findViewById(R.id.background);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            pos = b.getInt(Constants.KEY_CAT_ID);
        }


       /* mCategoryList = MyApplication.getInstance().getModelCategory();
        for(int i=0;i<mCategoryList.size();i++)
        {
            if(mCategoryList.get(i).getId()==pos)
            {
                modelCategory=mCategoryList.get(i);
            }
        }*/
        CategoryDatabaseManager model = new CategoryDatabaseManager(this);
        modelCategory = model.getCategoryById(pos);
        MyApplication.getInstance().setImageUrl(imageCategory,modelCategory.getImage());


        setCategoryTitle();
        slidingTabs();
    }

    public void setCategoryTitle() {
        TitleTextView categroyText = (TitleTextView) findViewById(R.id.txt_cat_title);
        categroyText.setText(modelCategory.getName());//Gettting cat name from list
    }

    public void slidingTabs() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        setupViewPager(viewPager);

        tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < titles.length; i++) {
            MovieBrowser movieBrowser = MovieBrowser.newInstance(BrowseDetail.this, pos);
            adapter.addFragment(movieBrowser, titles[i]);

        }
        viewPager.setCurrentItem(1);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
    }
}
