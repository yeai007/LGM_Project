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
import com.hopeofseed.hopeofseed.JNXData.CommentOrForward;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static cn.jpush.im.android.eventbus.EventBus.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/16 16:19
 * 修改人：whisper
 * 修改时间：2016/10/16 16:19
 * 修改备注：
 */
public class CommentForwarAdapter extends BaseAdapter {
    Context mContext;
    List<CommentOrForward> mList;

    public CommentForwarAdapter(Context context, ArrayList<CommentOrForward> arrCommentOrForward) {
        super();
        this.mContext = context;
        this.mList = arrCommentOrForward;
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
        CommentOrForward itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new CommentForwarAdapter.ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.com_forwar_list_items, null);
            holder.tv_username = (TextView) view.findViewById(R.id.tv_username);
            holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.user_img = (ImageView) view.findViewById(R.id.user_img);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_username.setText(itemData.getNickname());
        holder.tv_info.setText(itemData.getRecordCreateTime());
        holder.tv_content.setText(itemData.getComment());
        getUserJpushInfo(Const.JPUSH_PREFIX+itemData.getUser_id(), holder);
        return view;
    }

    class ViewHolder {
        ImageView user_img;
        TextView tv_username, tv_info, tv_content;
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
                        .into(holder.user_img);
            }
        });
    }
}
