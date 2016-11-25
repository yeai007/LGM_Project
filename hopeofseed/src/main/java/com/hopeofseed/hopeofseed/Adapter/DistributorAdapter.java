package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class DistributorAdapter extends BaseAdapter {
    Context mContext;
    List<DistributorData> mlist;

    public DistributorAdapter(Context context, ArrayList<DistributorData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
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
        DistributorData mData;
        mData = mlist.get(i);
        view = _LayoutInflater.inflate(R.layout.distributor_items, null);
        if (view != null) {
            TextView tv_distributor_name = (TextView) view.findViewById(R.id.tv_distributor_name);
            TextView tv_distributor_address=(TextView)view.findViewById(R.id.tv_distributor_address);
            Log.e(TAG, "getView: "+mData.getDistributorName());
            tv_distributor_name.setText(mData.getDistributorName());
            tv_distributor_address.setText("地址："+mData.getDistributorProvince()+" "+mData.getDistributorCity()+" "+mData.getDistributorZone()+" "+mData.getDistributorAddressDetail());
        }
        return view;
    }
}
