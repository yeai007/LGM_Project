package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.AreaData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 9:34
 * 修改人：whisper
 * 修改时间：2016/7/30 9:34
 * 修改备注：
 */
public class SelectAraeGridViewAdapter extends BaseAdapter {
    Context mContext;
    List<AreaData> mData;

    public SelectAraeGridViewAdapter(Context context, ArrayList<AreaData> data) {
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
        AreaData itemData;
        itemData = mData.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.gv_arealist_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Log.e("111111", "getView: "+itemData.getAreaname());
        holder.tv_name.setText(itemData.getAreaname());

        return view;
    }

    class ViewHolder {
        TextView tv_name;

    }
}
