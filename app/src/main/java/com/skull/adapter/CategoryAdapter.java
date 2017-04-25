package com.skull.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.skull.MyApplication;
import com.skull.R;
import com.skull.activities.BrowseDetail;
import com.skull.utils.Constants;
import com.skull.views.LabelTextView;
import com.webservices.models.Category;

import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 4/5/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Category> mCategroyList;
    private Context mContext;
    private Random mRandom = new Random();

    public CategoryAdapter(Context context, List<Category> DataSet) {
        mCategroyList = DataSet;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public LabelTextView title;

        public ViewHolder(View v) {
            super(v);
            title = (LabelTextView) v.findViewById(R.id.title);
            imageView = (ImageView) v.findViewById(R.id.image);
        }
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new View
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_single_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(mCategroyList.get(position).getName());
        //holder.imageView.getLayoutParams().height = getRandomIntInRange(250, 175);
        MyApplication.getInstance().setImageUrl(holder.imageView,mCategroyList.get(position).getImage());

        View.OnClickListener categroyDetail = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.KEY_CAT_NAME = mCategroyList.get(position).getName();
                Intent detail = new Intent(mContext, BrowseDetail.class);
                detail.putExtra(Constants.KEY_CAT_ID, mCategroyList.get(position).getId());
                mContext.startActivity(detail);
            }
        };

        holder.imageView.setOnClickListener(categroyDetail);
    }

    @Override
    public int getItemCount() {
        return mCategroyList.size();
    }

    // Custom method to get a random number between a range
    protected int getRandomIntInRange(int max, int min) {
        return mRandom.nextInt((max - min) + min) + min;
    }


}
