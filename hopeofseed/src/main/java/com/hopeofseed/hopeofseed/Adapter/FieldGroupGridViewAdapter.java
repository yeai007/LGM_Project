package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.VarietyData;
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
public class FieldGroupGridViewAdapter extends BaseAdapter {
    private static final String TAG = "FieldGroupGridViewAdapter";
    Context mContext;
    List<VarietyData> mData;
    private ArrayList<Integer> isSelect = new ArrayList<>();

    public FieldGroupGridViewAdapter(Context context, ArrayList<VarietyData> data) {
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
        VarietyData itemData;
        itemData = mData.get(i);
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.gv_arealist_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_name.setTag(R.id.ischeck, i);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelect.size() > 0) {
                    if (isSelect.contains(holder.tv_name.getTag(R.id.ischeck))) {
                        holder.tv_name.setBackgroundColor(Color.parseColor("#54FF9F"));
                        deleteSelect((Integer) holder.tv_name.getTag(R.id.ischeck));
                        notifyDataSetChanged();
                    } else {
                        setSelect((Integer) holder.tv_name.getTag(R.id.ischeck));
                        holder.tv_name.setBackgroundColor(Color.parseColor("#008B00"));
                        notifyDataSetChanged();
                    }
                } else {
                    setSelect((Integer) holder.tv_name.getTag(R.id.ischeck));
                    holder.tv_name.setBackgroundColor(Color.parseColor("#008B00"));
                    notifyDataSetChanged();
                }
            }


        });
        holder.tv_name.setText(itemData.getVarietyname());
        return view;
    }


    class ViewHolder {
        TextView tv_name;
    }

    public void setSelect(int position) {
        isSelect.add(position);
    }

    public ArrayList<Integer> getSelect() {
        return isSelect;
    }

    public void deleteSelect(int position) {
        if (isSelect.size() > 0) {
            for (int i = 0; i < isSelect.size(); i++) {
                if (isSelect.get(i) == position) {
                    isSelect.remove(i);
                    i--;
                }
            }

        }
    }
}
