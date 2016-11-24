package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.ui.XCFlowLayout;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/6 10:54
 * 修改人：whisper
 * 修改时间：2016/10/6 10:54
 * 修改备注：
 */
public class VarietyClassAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<VarietyData> mData;
    String[] mNames;

    public VarietyClassAdapter(Context context, ArrayList<VarietyData> data) {
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
        final ViewHolder holder;
        final VarietyData itemData;
        itemData = mData.get(i);
        holder = new VarietyClassAdapter.ViewHolder();
        view = _LayoutInflater
                .inflate(R.layout.varietyclass_list_items, null);
        holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, itemData.getVarietyname(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.flowlayout = (XCFlowLayout) view.findViewById(R.id.flowlayout);
        mNames = null;
        mNames = itemData.getBrand_names().split(",");
        holder.tv_name.setText(itemData.getVarietyname());
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int j = 0; j < mNames.length; j++) {
            holder.flow_view = new TextView(mContext);
            holder.flow_view.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            holder.flow_view.getPaint().setAntiAlias(true);//抗锯齿
            holder.flow_view.setText(mNames[j]);
            holder.flow_view.setTextColor(Color.parseColor("#FF0000"));
            holder.flowlayout.addView(holder.flow_view, lp);
            holder.flow_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, holder.flow_view.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    class ViewHolder {
        XCFlowLayout flowlayout;
        TextView tv_name;
        TextView flow_view;
    }
}
