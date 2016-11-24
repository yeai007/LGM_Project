package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 9:34
 * 修改人：whisper
 * 修改时间：2016/7/30 9:34
 * 修改备注：
 */
public class AssImgAdapter extends BaseAdapter {
    Context mContext;
    String[] mData;

    public AssImgAdapter(Context context, String[] data) {
        super();
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.length;
    }

    @Override
    public Object getItem(int i) {
        return mData[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        view = _LayoutInflater.inflate(R.layout.gv_discoverlist_items, null);
        if (view != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(mData[i]);
        }
        return view;
    }
}
