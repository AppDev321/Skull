package com.skull.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.skull.MyApplication;
import com.skull.R;
import com.skull.activities.MovieDetail;
import com.skull.models.DataModel;
import com.skull.utils.Constants;
import com.skull.views.LabelTextView;
import com.skull.views.MyBounceInterpolator;
import com.skull.views.TitleTextView;
import com.squareup.picasso.Picasso;
import com.webservices.models.MovieByCategory;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 4/5/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private Activity mContext;
    private Random mRandom = new Random();
    int length = 0;
    List<MovieByCategory> list;

    public MoviesAdapter(Activity context, List<MovieByCategory> list) {

        mContext = context;
        length = list.size();
        this.list = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView _cover,_play;
        public ImageView _background;
        public TitleTextView _movies;
        public LabelTextView _plot;
        public TitleTextView _releaseDate;
        public View _vw_blayer;


        public ViewHolder(View convertview) {
            super(convertview);
            _play=(ImageView)convertview.findViewById(R.id.img_play);
            _cover = (ImageView) convertview.findViewById(R.id.img_cover_d);
            _background = (ImageView) convertview.findViewById(R.id.img_background);
            _movies = (TitleTextView) convertview.findViewById(R.id.txt_movie_details);
            _plot = (LabelTextView) convertview.findViewById(R.id.txt_plot_d);
            _releaseDate = (TitleTextView) convertview.findViewById(R.id.txt_release_d);
            _vw_blayer = convertview.findViewById(R.id.vw_blacklayer);
        }
    }

    @Override
    public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.movie_single_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MovieByCategory movieByCategory = list.get(position);

        holder._movies.setText(movieByCategory.getName());
        if(movieByCategory.getPlotout().isEmpty())
        {
            holder._plot.setText("No Description Available");
        }else {

            holder._plot.setText(movieByCategory.getPlotout());
        }

        holder._releaseDate.setText("o Release: " + movieByCategory.getYear());

        if (movieByCategory.getCov().isEmpty()) {
            Picasso.with(mContext).load(R.drawable.no_image).into(holder._cover);
        } else {
            MyApplication.getInstance().setImageUrl(holder._cover,movieByCategory.getCov());
         }

        MyApplication.getInstance().setImageUrl(holder._background,movieByCategory.getImage());

        ObjectAnimator fade = ObjectAnimator.ofFloat(holder._vw_blayer, View.ALPHA, 1f, .3f);
        fade.setDuration(1500);
        fade.setInterpolator(new LinearInterpolator());
        fade.start();

        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        holder._play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // holder._play.startAnimation(myAnim);

                Intent intent = new Intent(mContext, MovieDetail.class);
                intent.putExtra(Constants.KEY_MOVIE_ID,movieByCategory);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return length;
    }

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;
    }


}
