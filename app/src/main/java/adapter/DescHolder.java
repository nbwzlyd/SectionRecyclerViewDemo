package adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import view.lyd.com.sectionrecyclerviewdemo.R;

/**
 * Created by lyd10892 on 2016/8/23.
 */

public class DescHolder extends RecyclerView.ViewHolder {
    public TextView descView;

    public DescHolder(View itemView) {
        super(itemView);
        initView();
    }

    private void initView() {
        descView = (TextView) itemView.findViewById(R.id.tv_desc);
    }
}
