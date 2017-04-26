package com.skull.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.skull.R;
import com.skull.databases.MoviesDatabaseManager;
import com.skull.fragments.SearchResult;
import com.skull.views.TitleTextView;
import com.webservices.models.MovieByCategory;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 4/4/2017.
 */

public class SearchActivity extends AppCompatActivity {


    TitleTextView mResultFound;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final MoviesDatabaseManager model = new MoviesDatabaseManager(this);

        mResultFound = (TitleTextView) findViewById(R.id.txt_no_result);


        mResultFound.setText("");

        final EditText mSearchText = (EditText) findViewById(R.id.edit_search);
        ImageView backArrow = (ImageView) findViewById(R.id.btn_search_back);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyBoard();
                finish();

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
                mResultFound.setVisibility(View.VISIBLE);
                mResultFound.setText("");

                //Remove all fragments from the container of view
                while (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStackImmediate();
                }

            }
        });


        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String searchText = charSequence.toString();
                searchText = searchText.replace("'", "");
                searchText = searchText.replace("-", "");


                if (searchText.length() > 0) {
                    List<MovieByCategory> listMovies = model.getSearchMoviesList(searchText);
                    //  viewPager = (ViewPager) findViewById(R.id.viewPager);

                    if (listMovies.size() == 0) {
                        mResultFound.setVisibility(View.VISIBLE);
                        mResultFound.setText("No Result Found");


                        //Remove all fragments from the container of view
                        while (getFragmentManager().getBackStackEntryCount() > 0) {
                            getFragmentManager().popBackStackImmediate();
                        }

                    } else {
                        mResultFound.setVisibility(View.GONE);
                        SearchResult mySecondFragment = SearchResult.newInstance(SearchActivity.this, listMovies);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment, mySecondFragment)
                                .commit();
                    }

                } else {
                    mResultFound.setVisibility(View.VISIBLE);
                    mResultFound.setText("");
                    //Remove all fragments from the container of view
                    while (getFragmentManager().getBackStackEntryCount() > 0) {
                        getFragmentManager().popBackStackImmediate();
                    }

                }


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


}
