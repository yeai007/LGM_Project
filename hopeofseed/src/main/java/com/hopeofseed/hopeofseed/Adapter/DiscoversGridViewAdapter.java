package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 9:34
 * 修改人：whisper
 * 修改时间：2016/7/30 9:34
 * 修改备注：
 */
public class DiscoversGridViewAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<String> mData = new ArrayList<>();

    public DiscoversGridViewAdapter(Context context, ArrayList<String> data) {
        super();
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        String itemData;
        itemData = mData.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.gv_discoverlist_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_name.setText(itemData);
        return view;
    }

    class ViewHolder {
        TextView tv_name;

    }
}
