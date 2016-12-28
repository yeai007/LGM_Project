package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.AllCommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.JNXData.StringVariety;
import com.hopeofseed.hopeofseed.R;

import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class StringGridListAdapter extends RecyclerView.Adapter<StringGridListAdapter.ViewHolder> {
    private static final String TAG = "StringGridListAdapter";
    Context mContext;
    List<StringVariety> mList;

    public StringGridListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.string_list_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final StringVariety itemData = mList.get(position);
        holder.tv_title.setText(itemData.getVariety() + "(" + itemData.getCount() + ")");
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof AllCommodityActivity) {
                    ((AllCommodityActivity) mContext).getDataForOut(itemData.getVariety(), position);
                } else if (mContext instanceof MyCommodity) {
                    ((MyCommodity) mContext).getDataForOut(itemData.getVariety(), position);
                }
            }
        });
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
