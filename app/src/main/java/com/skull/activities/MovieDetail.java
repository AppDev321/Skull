package com.skull.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.downloadmanager.activity.ADownloadManager;
import com.skull.MyApplication;
import com.skull.R;
import com.skull.adapter.AlsoLikedVideo;
import com.skull.databases.CategoryDatabaseManager;
import com.skull.databases.MoviesDatabaseManager;
import com.skull.player.CustomPlayer;
import com.skull.player.YouTubeVideo;
import com.skull.utils.Constants;
import com.skull.views.MyBounceInterpolator;
import com.squareup.picasso.Picasso;
import com.streaming.download.StreamLink;
import com.webservices.models.Link;
import com.webservices.models.MovieByCategory;
import com.webservices.models.ParseRespone;
import com.webservices.models.RequestMovieByCat;
import com.webservices.models.StreamResponse;
import com.webservices.models.YoutubePareUrl;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Retrofit;

/**
 * Created by Administrator on 4/6/2017.
 */

public class MovieDetail extends AppCompatActivity {
    TextView txt_year, txt_title, txt_generie, txt_rating, txt_description, txt_writer, txt_producer, txt_cast, txt_director;
    MovieByCategory movieByCategory;
    ImageView _coverImage;

    LinearLayout btnDownloadMovie, btnWatchMovie;

    private List<YoutubePareUrl> youtubePareUrls;
    View youtubeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_view);

        if (getIntent().getExtras() != null) {
            movieByCategory = (MovieByCategory) getIntent().getSerializableExtra(Constants.KEY_MOVIE_ID);
        }

        if (movieByCategory != null) {
            initateLayout(movieByCategory);
        }


        youtubeView = findViewById(R.id.youtube_view);
        didTapButton();
        btnDownloadMovie = (LinearLayout) findViewById(R.id.btn_download);
        btnWatchMovie = (LinearLayout) findViewById(R.id.btn_watch);
        btnWatchMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMovie();
            }
        });
        btnDownloadMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadMovie();

            }
        });


        loadAlsoLikedVideoAdapter();
    }

    public void initateLayout(MovieByCategory movieByCategory) {
        int hours = movieByCategory.getDur() / 60; //since both are ints, you get an int
        int minutes = movieByCategory.getDur() % 60;

        _coverImage = (ImageView) findViewById(R.id.cover_img);

        if (movieByCategory.getCov().isEmpty()) {
            Picasso.with(this).load(R.drawable.no_image).skipMemoryCache().placeholder(R.drawable.no_image).into(_coverImage);
        } else {
            MyApplication.getInstance().setImageUrl(_coverImage, movieByCategory.getImage());

        }

        txt_year = (TextView) findViewById(R.id.txt_year);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_generie = (TextView) findViewById(R.id.txt_generie);
        txt_rating = (TextView) findViewById(R.id.txt_rating);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_writer = (TextView) findViewById(R.id.txt_writer);
        txt_producer = (TextView) findViewById(R.id.txt_producer);
        txt_cast = (TextView) findViewById(R.id.txt_cast);
        txt_director = (TextView) findViewById(R.id.txt_director);


        txt_year.setText("" + movieByCategory.getYear());
        txt_title.setText("" + movieByCategory.getName());
        txt_rating.setText("" + movieByCategory.getRate2());
        txt_cast.setText(movieByCategory.getCast());
        txt_writer.setText(movieByCategory.getWriter());
        txt_director.setText(movieByCategory.getDirector());
        txt_producer.setText(movieByCategory.getProd());

        CategoryDatabaseManager mgr = new CategoryDatabaseManager(this);
        String catName = mgr.getCategoryById(movieByCategory.getCategoryId()).getName();

        txt_generie.setText("" + catName + ", Duration: " + hours + "h:" + minutes + "m");//Get current selected categeroy name

        if (movieByCategory.getPlot().isEmpty()) {
            txt_description.setText("No Description Available");

        } else {
            txt_description.setText("" + movieByCategory.getPlot());
        }

        if (movieByCategory.getRate2().isEmpty()) {
            txt_rating.setText("0");

        }

        if (movieByCategory.getWriter().isEmpty()) {
            String noData = "N/A";
            txt_cast.setText(noData);
            txt_writer.setText(noData);
            txt_director.setText(noData);
            txt_producer.setText(noData);
        }

    }

    void playMovie() {
        if (movieByCategory.getIsYoutube() == 0) {

            if(movieByCategory.getVideoLink().contains("https://drive.google.com") ) {
             // getStreamLink(movieByCategory.getVideoLink(), false);
                Intent i = new Intent(MovieDetail.this, CustomPlayer.class);
               // i.putExtra(Constants.KEY_MOVIE_URL,"http://boxtvhd-f.akamaihd.net/i/clips/527/26527/,KCQPO9AGPZ2D_26527_1200,.mp4.csmil/master.m3u8");
                i.putExtra(Constants.KEY_MOVIE_URL,"http://html5demos.com/assets/dizzy.mp4");
                i.putExtra(Constants.KEY_MOVIE_NAME, movieByCategory.getName());
                startActivity(i);


                Object[] arrayOfObject = new Object[3];
                arrayOfObject[0] ="http://api.boxtv.com/beta/";
                arrayOfObject[1] = "26527";
                arrayOfObject[2] = "&token=&APIKey=x-qubet6wztfrzgx";
                String str = String.format("%s/clips?id=%s&type=details&rows=&%s", arrayOfObject);


                Log.e("yrd",str);
            }
            else
            {
                Intent i = new Intent(MovieDetail.this, CustomPlayer.class);
                i.putExtra(Constants.KEY_MOVIE_URL, movieByCategory.getVideoLink());
                i.putExtra(Constants.KEY_MOVIE_NAME, movieByCategory.getName());
                startActivity(i);
            }
        }
        // Play Youtube Videos

        else {
            Intent i = new Intent(MovieDetail.this, YouTubeVideo.class);
            i.putExtra(Constants.KEY_MOVIE_URL, movieByCategory.getVideoLink());
            startActivity(i);

        }

    }

    void downloadMovie() {
        if (movieByCategory.getIsYoutube() == 0) {
           /* if(movieByCategory.getVideoLink().contains("https://drive.google.com") ) {
                getStreamLink(movieByCategory.getVideoLink(), true);
            }
            else
            {
                Intent i = new Intent(MovieDetail.this, ADownloadManager.class);
                i.putExtra(Constants.KEY_DOWNLOAD_URL, movieByCategory.getVideoLink());
                i.putExtra(Constants.KEY_DOWNLOAD_TITLE, movieByCategory.getName() + ".mp4");
                startActivity(i);
            }*/



         /*   Intent i = new Intent(MovieDetail.this, ADownloadManager.class);
            i.putExtra(Constants.KEY_DOWNLOAD_URL ,"http://player.boxtvhd-f.akamaihd.net/i/clips/527/26527/KCQPO9AGPZ2D_26527_70.mp4");
            i.putExtra(Constants.KEY_DOWNLOAD_TITLE, movieByCategory.getName() + ".mp4");
            startActivity(i);*/
            StreamLink t =new StreamLink(this,"http://boxtvhd-f.akamaihd.net/i/clips/527/26527/,KCQPO9AGPZ2D_26527_1200,.mp4.csmil/master.m3u8");

        } else {
            RequestMovieByCat model = new RequestMovieByCat();
            model.setId(movieByCategory.getVideoLink());
            getYouTubeDownloadLink(model);
            //  new VideoDownload().execute(subLink);
        }
    }

    public void didTapButton() {
        final ImageView button = (ImageView) findViewById(R.id.img_play);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        //  button.startAnimation(myAnim);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.startAnimation(myAnim);
                playMovie();
            }
        });
    }


    public void loadAlsoLikedVideoAdapter() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setFocusableInTouchMode(false);

        MoviesDatabaseManager model = new MoviesDatabaseManager(this);
        List<MovieByCategory> movieList = model.getAlsoLikedMovies(movieByCategory.getCategoryId());

        AlsoLikedVideo mAdapter = new AlsoLikedVideo(MovieDetail.this, movieList);
        mRecyclerView.setAdapter(mAdapter);
    }


    public void getYouTubeDownloadLink(RequestMovieByCat youTubeLink) {
        MyApplication.getInstance().showLoading(this);
        Call<ParseRespone> call = MyApplication.getRestApi().getYouTubeParseUrl(youTubeLink);
        call.enqueue(new retrofit.Callback<ParseRespone>() {
            @Override
            public void onResponse(retrofit.Response<ParseRespone> response, Retrofit retrofit) {
                MyApplication.getInstance().cancelDialog();
                youtubePareUrls = response.body().getData().getUrls();
                if (youtubePareUrls != null) {
                    showCodecDialog();
                } else {
                    Toast.makeText(MovieDetail.this, "There is no link available to download", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-YouTubeParse", t.toString());
                MyApplication.getInstance().cancelDialog();
                Toast.makeText(MovieDetail.this, "Please try after some time", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void showCodecDialog() {
        List<String> myList = new ArrayList<String>();
        for (int i = 0; i < youtubePareUrls.size(); i++) {

            if (youtubePareUrls.get(i).getLabel().contains("(video - no sound)") || youtubePareUrls.get(i).getLabel().contains("(audio - no video)")) {
                continue;

            } else {
                myList.add(youtubePareUrls.get(i).getLabel());
            }
        }

        final String[] url = myList.toArray(new String[myList.size()]);

        AlertDialog a = new AlertDialog.Builder(this)
                .setTitle("Choose Format")
                .setSingleChoiceItems(url, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        String[] parts = url[selectedPosition].split("-");
                        String extension = parts[1].trim();

                        Intent i = new Intent(MovieDetail.this, ADownloadManager.class);
                        i.putExtra(Constants.KEY_DOWNLOAD_URL, youtubePareUrls.get(selectedPosition).getId());
                        i.putExtra(Constants.KEY_DOWNLOAD_TITLE, movieByCategory.getName() + "." + extension);
                        startActivity(i);

                    }
                }).setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }


    //Get Streaming & downloading links
    public void getStreamLink(String link, final boolean isDownload) {
        MyApplication.getInstance().showLoading(MovieDetail.this);
        RequestMovieByCat model = new RequestMovieByCat();
        model.setId(link);
        Call<StreamResponse> call = MyApplication.getRestApi().getStreamLink(model);
        call.enqueue(new retrofit.Callback<StreamResponse>() {
            @Override
            public void onResponse(retrofit.Response<StreamResponse> response, Retrofit retrofit) {
                MyApplication.getInstance().cancelDialog();
                List<Link> modelCategory = response.body().getLinks();
                if (modelCategory != null && modelCategory.size() > 0) {
                    showCodecStreaming(modelCategory, isDownload);
                } else {
                    // Toast.makeText(MovieDetail.this, "There is some problem in network.\nPlease try again", Toast.LENGTH_SHORT).show();
                    Snackbar.make(getWindow().getDecorView().getRootView(), "There is some problem in network.\nPlease try again", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("Failure-MovieStream", t.toString());
                MyApplication.getInstance().cancelDialog();
                MyApplication.getInstance().showServerFailureDialog(MovieDetail.this);
            }
        });
    }

    public void showCodecStreaming(final List<Link> links, final boolean isDownload) {
        List<String> myList = new ArrayList<String>();
        for (int i = 0; i < links.size(); i++) {
            myList.add(links.get(i).getLabel() + " - mp4");
        }

        final String[] url = myList.toArray(new String[myList.size()]);

        AlertDialog a = new AlertDialog.Builder(this)
                .setTitle("Choose Format")
                .setSingleChoiceItems(url, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();

                        if (isDownload) {
                            Intent i = new Intent(MovieDetail.this, ADownloadManager.class);
                            i.putExtra(Constants.KEY_DOWNLOAD_URL, links.get(selectedPosition).getSrc());
                            i.putExtra(Constants.KEY_DOWNLOAD_TITLE, movieByCategory.getName() + ".mp4");
                            startActivity(i);
                        } else {
                            Intent i = new Intent(MovieDetail.this, CustomPlayer.class);
                            i.putExtra(Constants.KEY_MOVIE_URL, links.get(selectedPosition).getSrc());
                            i.putExtra(Constants.KEY_MOVIE_NAME, movieByCategory.getName());
                            startActivity(i);
                        }


                    }
                }).setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .show();
    }




}