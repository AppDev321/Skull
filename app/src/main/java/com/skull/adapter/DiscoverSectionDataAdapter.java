package com.skull.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.skull.R;
import com.skull.activities.BrowseDetail;
import com.skull.models.DiscoverModel;
import com.skull.utils.Constants;
import com.skull.views.CustomButton;
import com.skull.views.TitleTextView;

import java.util.ArrayList;

/**
 * Created by Administrator on 4/5/2017.
 */

public class DiscoverSectionDataAdapter extends RecyclerView.Adapter<DiscoverSectionDataAdapter.ItemRowHolder> {

    private ArrayList<DiscoverModel> dataList;
    private Context mContext;

    public DiscoverSectionDataAdapter(Context context, ArrayList<DiscoverModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_header_list, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder,final int i) {

        final String sectionName = dataList.get(i).getName();
        itemRowHolder.itemTitle.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, dataList.get(i).getAllItemsInSection());

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(v.getContext(), "click event on more, "+sectionName , Toast.LENGTH_SHORT).show();

                Constants.KEY_CAT_NAME =sectionName;

                Intent detail = new Intent(mContext, BrowseDetail.class);
                detail.putExtra(Constants.KEY_CAT_ID, dataList.get(i).getId());
                mContext.startActivity(detail);
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TitleTextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected CustomButton btnMore;

        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TitleTextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (CustomButton) view.findViewById(R.id.btnMore);


        }

    }

}
