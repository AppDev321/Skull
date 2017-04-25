package com.skull.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.skull.MyApplication;
import com.skull.R;
import com.skull.activities.MovieDetail;
import com.skull.models.SectionDiscoverItems;
import com.skull.utils.Constants;
import com.skull.views.LabelTextView;
import com.webservices.models.MovieByCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 4/5/2017.
 */

public class SectionListDataAdapter extends RecyclerView.Adapter<SectionListDataAdapter.SingleItemRowHolder> {

    private  List<MovieByCategory> itemsList;
    private Context mContext;

    public SectionListDataAdapter(Context context, List<MovieByCategory> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_list_item, null);
        SingleItemRowHolder mh = new SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int i) {

        final MovieByCategory singleItem = itemsList.get(i);
        holder.tvTitle.setText(singleItem.getName());

        MyApplication.getInstance().setImageUrl(holder.itemImage,singleItem.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MovieDetail.class);
                intent.putExtra(Constants.KEY_MOVIE_ID,singleItem);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected LabelTextView tvTitle;

        protected ImageView itemImage;


        public SingleItemRowHolder(View view) {
            super(view);

            this.tvTitle = (LabelTextView) view.findViewById(R.id.tvTitle);
            this.itemImage = (ImageView) view.findViewById(R.id.itemImage);





        }

    }

}