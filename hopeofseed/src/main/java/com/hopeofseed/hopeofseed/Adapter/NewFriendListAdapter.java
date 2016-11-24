package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.AddFriend;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.R;

import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class NewFriendListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<UserData> mList;

    public NewFriendListAdapter(Context context, List list) {
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
        UserData itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.newfriend_items, null);
            holder.img_user_head = (ImageView) view.findViewById(R.id.img_user_head);
            holder.tv_username = (TextView) view.findViewById(R.id.tv_username);
            holder.add_friend = (TextView) view.findViewById(R.id.add_friend);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.add_friend.setTag(R.id.key_addnewfriend, itemData.getUser_id());
        //ImageLoaderUtil.loadImageHttp(holder.img_user, itemData.getAssimgurl1(), 1);
        holder.add_friend.setOnClickListener(pullToListViewItemOnClickListener);
        holder.tv_username.setText(itemData.getUser_name());
        return view;
    }

    class ViewHolder {
        ImageView img_user_head;
        TextView tv_username, add_friend;
    }

    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.tv_username:
//                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_addnewfriend)));
//                    AddFriend(String.valueOf(view.getTag(R.id.key_addnewfriend)));
                    break;
                case R.id.add_friend:
                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_addnewfriend)));
                    AddFriend(String.valueOf(view.getTag(R.id.key_addnewfriend)));
                    break;
            }
        }
    };

    public void AddFriend(final String position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddFriend addFriend = new AddFriend();
                addFriend.UserId = String.valueOf(Const.currentUser.user_id);
                addFriend.AddUserId = position;
                Boolean bRet = addFriend.RunData();
                Message msg = AddFriendHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler AddFriendHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Log.e(TAG, "handleMessage: 1111");

                    break;
                case 1:
                    Toast.makeText(mContext, "关注成功", Toast.LENGTH_SHORT).show();

                    Log.e(TAG, "handleMessage: 222");
                    break;
                case 2:
                    Log.e(TAG, "handleMessage: 2333");
                    break;
            }
        }
    };
}
