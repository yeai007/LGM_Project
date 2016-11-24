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
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/9 10:58
 * 修改人：whisper
 * 修改时间：2016/11/9 10:58
 * 修改备注：
 */
public class HorizontalCategoryAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> mList;

    public HorizontalCategoryAdapter(Context context, ArrayList<String> data) {
        super();
        this.mContext = context;
        mList = data;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.category_list_items, null);
            holder.category_text = (TextView) view.findViewById(R.id.category_text);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.category_text.setText(mList.get(i));
        return view;
    }

    class ViewHolder {
        TextView category_text;
    }
}
