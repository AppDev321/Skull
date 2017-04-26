package com.skull.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skull.MyApplication;
import com.skull.R;
import com.webservices.models.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/5/2017.
 */

public class BannerSliderAdapter extends PagerAdapter {

    private Context ctx;
    private LayoutInflater inflater;
    List<Banner>images;
    public BannerSliderAdapter(Context ctx,List<Banner> images){
        this.ctx = ctx;
        this.images=images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view ==(LinearLayout)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.banner_slide_view,container,false);
        ImageView img =(ImageView)v.findViewById(R.id.banner_image_view);
        MyApplication.getInstance().setImageUrl(img,images.get(position).getUrl());

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        container.refreshDrawableState();
    }
}