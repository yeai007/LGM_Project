package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
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
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoActivity;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
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
public class DiscoversListAdapter extends BaseAdapter {
    private static final String TAG = "DiscoversListAdapter";
    Context mContext;
    ArrayList<NewsData> mList = new ArrayList<>();

    public DiscoversListAdapter(Context context, ArrayList<NewsData> data) {
        super();
        this.mContext = context;
        this.mList = data;
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
        NewsData itemData;
        itemData = mList.get(i);
        String[] arrImage = mList.get(i).getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.discoverlist_items, null);
            holder.img_user = (ImageView) view.findViewById(R.id.img_user);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.user_name = (TextView) view.findViewById(R.id.user_name);
            view.setTag(holder);
            holder.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
            holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
            holder.resultRecyclerView.setAdapter(gridAdapter);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_title.setText(itemData.getTitle());
        holder.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
        if (itemData.getNickname().equals("")) {
            holder.user_name.setText(itemData.getNickname());
        } else {
            holder.user_name.setText(itemData.getNickname());
        }
        holder.user_name.setTag(R.id.key_userid, itemData.getUser_id());
        holder.user_name.setTag(R.id.key_username, itemData.getUser_name());
        holder.user_name.setOnClickListener(pullToListViewItemOnClickListener);
        holder.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
        holder.tv_content.setTag(R.id.key_content, itemData.getId());
        getUserJpushInfo(Const.JPUSH_PREFIX+itemData.getUser_id(), holder);
        return view;
    }

    class ViewHolder {
        RecyclerView resultRecyclerView;
        ImageView img_user;
        TextView tv_title, tv_content, tv_zambia, user_name, tv_forward, tv_comment;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }
    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                Log.e(TAG, "gotResult: " + userInfo.getAvatar());
                Glide.with(mContext)
                        .load(userInfo.getAvatarFile())
                        .centerCrop()
                        .into(holder.img_user);
            }
        });
    }
    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.user_name:
                    intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                    intent.putExtra("userid", String.valueOf(view.getTag(R.id.key_userid)));
                    intent.putExtra("username", String.valueOf(view.getTag(R.id.key_username)));
                    mContext.startActivity(intent);
                    break;
                case R.id.tv_content:
                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_content)));
                    intent = new Intent(mContext.getApplicationContext(), NewsInfoActivity.class);
                    intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_content)));
                    mContext.startActivity(intent);
                    break;
            }
        }
    };
}
