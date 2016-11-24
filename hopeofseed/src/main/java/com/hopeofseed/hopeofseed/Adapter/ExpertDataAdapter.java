package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.ExpertData;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
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
public class ExpertDataAdapter extends BaseAdapter {
    Context mContext;
    List<ExpertData> mlist;

    public ExpertDataAdapter(Context context, ArrayList<ExpertData> list) {
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
        ExpertData mData;
        mData = mlist.get(i);
        view = _LayoutInflater.inflate(R.layout.search_crop_items, null);
        if (view != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            Log.e(TAG, "getView: " + mData.getExpertName());
            tv_name.setText(mData.getExpertName());
        }
        return view;
    }
}
