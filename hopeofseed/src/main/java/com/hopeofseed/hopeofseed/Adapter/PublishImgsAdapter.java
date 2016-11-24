package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.Map;
/**
 * @FileName:com.mgkj.smamootwo.Adapter
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/16
 * @Copyright:2014-2016 Moogeek
 */
public class PublishImgsAdapter extends BaseAdapter {
    private ArrayList<Map<String, String>> mList = new ArrayList<>();
    Context mContext;

    public PublishImgsAdapter(Context context, ArrayList<Map<String, String>> arrList) {
        this.mContext = context;
        this.mList = arrList;
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
        Map<String, String> itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.gv_publish_items, null);
            holder.img_content = (ImageView) view.findViewById(R.id.img_content);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ImageLoaderUtil.loadImage(holder.img_content, R.drawable.baby, 0);

        return view;
    }

    class ViewHolder {
        ImageView img_content;

    }

}
