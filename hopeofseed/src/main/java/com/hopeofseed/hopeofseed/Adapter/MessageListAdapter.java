package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserMessageData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

import static com.hopeofseed.hopeofseed.R.id.tv_name;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class MessageListAdapter extends BaseAdapter {
    private static final String TAG = "MessageListAdapter";
    Context mContext;
    ArrayList<UserMessageData> mData = new ArrayList<>();
    public MessageListAdapter(Context context, ArrayList<UserMessageData> data) {
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
        UserMessageData itemData = new UserMessageData();
        itemData = mData.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.messagelist_items, null);
            holder.tv_name = (TextView) view.findViewById(tv_name);
            holder.message_img = (ImageView) view.findViewById(R.id.message_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_name.setText(itemData.getMessageTitle());
        Log.e(TAG, "getView: " + Const.IMG_URL_FINAL + itemData.getMessageImageUrl());
        Glide.with(mContext)
                .load(Const.IMG_URL_FINAL + itemData.getMessageImageUrl()).placeholder(R.drawable.no_have_img)
                .centerCrop()
                .into(holder.message_img);
        return view;
    }
    class ViewHolder {
        TextView tv_name;
        ImageView message_img;
    }

}