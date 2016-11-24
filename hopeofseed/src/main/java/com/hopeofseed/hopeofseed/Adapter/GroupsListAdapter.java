package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.GroupChatActivity;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
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
public class GroupsListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<GroupData> mList;

    public GroupsListAdapter(Context context, List list) {
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
        GroupData itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.grouplist_items, null);
            holder.img_group_head = (ImageView) view.findViewById(R.id.img_group_head);
            holder.tv_groupname = (TextView) view.findViewById(R.id.tv_groupname);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
//        ImageLoaderUtil.loadImageHttp(holder.img_group_head, itemData.getAssimgurl1(), 1);
        holder.tv_groupname.setTag(R.id.key_groupid, itemData.getGroupid());

        holder.tv_groupname.setTag(R.id.key_jpushgroupid, itemData.getJpushGroupId());
        holder.tv_groupname.setOnClickListener(pullToListViewItemOnClickListener);
        holder.tv_groupname.setText(itemData.getGroupname());
        return view;
    }

    class ViewHolder {
        ImageView img_group_head;
        TextView tv_groupname;
    }

    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.tv_groupname:
                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_groupid)));
                    intent = new Intent(mContext.getApplicationContext(), GroupChatActivity.class);
                    intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("GROUPID", String.valueOf(view.getTag(R.id.key_groupid)));
                    intent.putExtra("JPUSHGROUPID", String.valueOf(view.getTag(R.id.key_jpushgroupid)));
                    mContext.startActivity(intent);
                    break;
            }
        }
    };

}
