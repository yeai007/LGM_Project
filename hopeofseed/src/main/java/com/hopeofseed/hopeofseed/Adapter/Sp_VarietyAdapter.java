package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.CommodityVarietyData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/28 10:45
 * 修改人：whisper
 * 修改时间：2016/7/28 10:45
 * 修改备注：
 */
public class Sp_VarietyAdapter extends BaseAdapter {
    private List<CommodityVarietyData> mList;
    private Context mContext;

    public Sp_VarietyAdapter(Context pContext, List pList) {
        this.mContext = pContext;
        this.mList = ObjectUtil.cast(pList);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.sp_variety_item, null);
        if (convertView != null) {
            TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_name.setText(mList.get(position).getVarietyname());
        }
        return convertView;
    }
}