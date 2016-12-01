package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.YieldData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.hopeofseed.hopeofseed.R.id.tv_crop_class;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class YieldDataAdapter extends BaseAdapter {
    Context mContext;
    List<YieldData> mlist;

    public YieldDataAdapter(Context context, ArrayList<YieldData> list) {
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
        YieldData mData;
        mData = mlist.get(i);

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.search_yield_items, null);
            viewHolder.tv_crop_class=(TextView)view.findViewById(R.id.tv_crop_class);
            viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_name);
            viewHolder.tv_content=(TextView)view.findViewById(R.id.tv_content);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_crop_class.setText("【产量表现】");
        viewHolder.tv_name.setText("【"+mData.getYieldVariety()+"】");
        viewHolder.tv_content.setText("总产量：" + mData.getYieldSum() + "\n种植面积：" + mData.getYieldArea() + "\n平均产量：" + mData.getYieldYield() + "\n表现:" + mData.getYieldEssay().replace("\\n", "\n"));
        return view;
    }
    class ViewHolder
    {
        TextView tv_crop_class,tv_name,tv_content;
    }
}
