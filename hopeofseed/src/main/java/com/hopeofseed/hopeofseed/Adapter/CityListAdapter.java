package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.CityData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class CityListAdapter extends BaseAdapter {
    Context mContext;
    List<CityData> mlist;

    public CityListAdapter(Context context, ArrayList<CityData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     */
    public int getSectionForPosition(int position) {
        return mlist.get(position).getCityPY().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = mlist.get(i).getCityPY();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }


    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        CityData mData;
        mData = mlist.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.city_list_items, null);
            holder.tv_title_py = (TextView) view.findViewById(R.id.tv_title_py);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.lin_title = (LinearLayout) view.findViewById(R.id.lin_title);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (i == 0) {
            holder.lin_title.setVisibility(View.VISIBLE);
            holder.tv_title_py.setText(mData.getCityPY().substring(0, 1));
        } else {
            if (mData.getCityPY().substring(0, 1).equals(mlist.get(i - 1).getCityPY().substring(0, 1))) {
                holder.lin_title.setVisibility(View.GONE);
            } else {
                holder.lin_title.setVisibility(View.VISIBLE);
                holder.tv_title_py.setText(mData.getCityPY().substring(0, 1));
            }
        }
        holder.tv_name.setText(mData.getCityName());
        return view;
    }

    class ViewHolder {
        TextView tv_title_py, tv_name;
        LinearLayout lin_title;
    }

}
