package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.ChatDetailActivity;
import com.hopeofseed.hopeofseed.Activitys.JoinTheGroup;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.lgm.utils.DateTools;

import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

import java.io.Serializable;
import java.text.ParseException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetGroupMembersCallback;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.UserInfo;

import static android.R.attr.targetId;
import static com.hopeofseed.hopeofseed.Application.TARGET_APP_KEY;
import static com.hopeofseed.hopeofseed.Application.TARGET_ID;
import static com.hopeofseed.hopeofseed.R.id.item_content;
import static com.hopeofseed.hopeofseed.R.id.tv_time;
import static com.hopeofseed.hopeofseed.R.id.tv_unread_count;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<GroupData> mList;
    Context mContext;
    private LayoutInflater inflater;

    public GroupListAdapter(Context context, List<GroupData> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.group_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final GroupData itemData = mList.get(position);
        holder.item_content.setText(itemData.getAppGroupName());
        holder.item_address.setText(itemData.getAppGroupProvince() + "  " + itemData.getAppGroupCity() + "   " + itemData.getAppGroupZone());
        final boolean[] isMember = {false};
        JMessageClient.getGroupMembers(Long.parseLong(itemData.getAppJpushGroupId()), new GetGroupMembersCallback() {
            @Override
            public void gotResult(int i, String s, List<UserInfo> list) {
                holder.item_members_count.setText(list.size() + "人");
                if (list.contains(JMessageClient.getMyInfo())) {
                    isMember[0] = true;
                    holder.img_btn_join.setImageResource(R.drawable.is_join);
                    holder.rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext.getApplicationContext(), ChatActivity.class);
                            intent.putExtra(Application.GROUP_ID, Long.parseLong(itemData.getAppJpushGroupId()));
                            intent.putExtra("fromGroup", false);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            mContext.startActivity(intent);
                        }
                    });
                } else {
                    isMember[0] = false;
                    holder.img_btn_join.setImageResource(R.drawable.is_join_null);
                    holder.img_btn_join.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, JoinTheGroup.class);
                            intent.putExtra("GroupId", String.valueOf(itemData.getAppJpushGroupId()));
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
        });
        holder.rel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMember[0]) {
                    Intent intent = new Intent(mContext.getApplicationContext(), ChatActivity.class);
                    intent.putExtra(Application.GROUP_ID, Long.parseLong(itemData.getAppJpushGroupId()));
                    intent.putExtra("fromGroup", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext.getApplicationContext(), ChatDetailActivity.class);
                    intent.putExtra(Application.GROUP_ID, Long.parseLong(itemData.getAppJpushGroupId()));
                    intent.putExtra("type", 1);
                    intent.putExtra("fromGroup", false);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView item_content, item_members_count, item_address;
        RelativeLayout rel_item;
        ImageButton img_btn_join;
        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            img_btn_join = (ImageButton) itemView.findViewById(R.id.img_btn_join);
            item_members_count = (TextView) itemView.findViewById(R.id.item_members_count);
            item_address = (TextView) itemView.findViewById(R.id.item_address);
        }
    }
}
