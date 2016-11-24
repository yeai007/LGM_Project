package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommentNew;
import com.hopeofseed.hopeofseed.Activitys.ForwardNew;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateZambia;
import com.hopeofseed.hopeofseed.JNXData.FollowedFriend;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.R;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class MyFollowedListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<FollowedFriend> mList;


    public MyFollowedListAdapter(Context context, List<FollowedFriend> list) {
        super();
        this.mContext = context;
        this.mList = list;
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
        FollowedFriend itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.followed_list_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.img_show = (ImageView) view.findViewById(R.id.img_show);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        Log.e(TAG, "getView: "+itemData.getNickname());
        holder.tv_name.setText(itemData.getNickname());
        //getUserJpushInfo(itemData.getUser_name(), holder);
        return view;
    }

    class ViewHolder {

        ImageView img_show;
        TextView tv_name;
    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Log.e(TAG, "gotResult: " + userInfo.getAvatar());
                Glide.with(mContext)
                        .load(userInfo.getAvatarFile())
                        .centerCrop()
                        .into(holder.img_show);
            }
        });
    }

}
