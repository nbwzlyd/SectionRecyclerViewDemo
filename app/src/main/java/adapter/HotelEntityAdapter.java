package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import entity.HotelEntity;
import utils.HotelUtils;
import view.lyd.com.sectionrecyclerviewdemo.R;

/**
 * Created by lyd10892 on 2016/8/23.
 */

public class HotelEntityAdapter extends SectionedRecyclerViewAdapter<HeaderHolder, DescHolder, RecyclerView.ViewHolder> {


    public ArrayList<HotelEntity.TagsEntity> allTagList;
    private Context mContext;
    private LayoutInflater mInflater;

    private SparseBooleanArray mBooleanMap;

    public HotelEntityAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mBooleanMap = new SparseBooleanArray();
    }

    public void setData(ArrayList<HotelEntity.TagsEntity> allTagList) {
        this.allTagList = allTagList;
        notifyDataSetChanged();
    }

    @Override
    protected int getSectionCount() {
        return HotelUtils.isEmpty(allTagList) ? 0 : allTagList.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        int count = allTagList.get(section).tagInfoList.size();
        if (count >= 8 && !mBooleanMap.get(section)) {
            count = 8;
        }

        return HotelUtils.isEmpty(allTagList.get(section).tagInfoList) ? 0 : count;
    }

    //是否有footer布局
    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HeaderHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderHolder(mInflater.inflate(R.layout.hotel_title_item, parent, false));
    }


    @Override
    protected RecyclerView.ViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected DescHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new DescHolder(mInflater.inflate(R.layout.hotel_desc_item, parent, false));
    }


    @Override
    protected void onBindSectionHeaderViewHolder(final HeaderHolder holder, final int section) {
        holder.openView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOpen = mBooleanMap.get(section);
                String text = isOpen ? "展开" : "关闭";
                mBooleanMap.put(section, !isOpen);
                holder.openView.setText(text);
                notifyDataSetChanged();
            }
        });

        holder.titleView.setText(allTagList.get(section).tagsName);
        holder.openView.setText(mBooleanMap.get(section) ? "关闭" : "展开");

    }


    @Override
    protected void onBindSectionFooterViewHolder(RecyclerView.ViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(DescHolder holder, int section, int position) {
        holder.descView.setText(allTagList.get(section).tagInfoList.get(position).tagName);

    }
}
