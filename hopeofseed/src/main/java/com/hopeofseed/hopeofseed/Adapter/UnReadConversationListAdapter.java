package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommentAboutMe;
import com.hopeofseed.hopeofseed.Activitys.GroupNofityActivity;
import com.hopeofseed.hopeofseed.Activitys.HomePageActivity;
import com.hopeofseed.hopeofseed.Activitys.SystemNofityActivity;
import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.ConvAndGroupData;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.hopeofseed.hopeofseed.util.FileUtil;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetGroupInfoCallback;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.content.EventNotificationContent;
import cn.jpush.im.android.api.content.TextContent;
import cn.jpush.im.android.api.content.VoiceContent;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.GroupInfo;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;
import io.realm.Realm;
import io.realm.RealmResults;

import static cn.jpush.im.android.api.JMessageClient.deleteSingleConversation;
import static cn.jpush.im.android.api.enums.ConversationType.single;
import static com.hopeofseed.hopeofseed.Activitys.MessageFragment.MESSAGE_UPDATE_LIST;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class UnReadConversationListAdapter extends RecyclerView.Adapter<UnReadConversationListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListAdapter";
    List<ConvAndGroupData> mList;
    Context mContext;
    private LayoutInflater inflater;
    int PinglunCount = 0;
    Realm myRealm = Realm.getDefaultInstance();
    boolean isShow = false;

    public UnReadConversationListAdapter(Context context, List<ConvAndGroupData> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mList.size() + 4;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder: position" + position);
        if (position < 4) {
            switch (position) {
                case 0:
                    holder.tv_time.setVisibility(View.GONE);
                    Log.e(TAG, "onBindViewHolder: PinglunCount" + PinglunCount);
                    Glide.with(mContext)
                            .load(R.drawable.img_pinglun)
                            .centerCrop()
                            .into(holder.img_item);
                    if (PinglunCount > 0) {
                        Log.e(TAG, "onBindViewHolder: updateview1");
                        holder.item_title.setText("评论");
                        holder.item_content.setText("您有新的评论消息");
                        holder.item_content.setVisibility(View.VISIBLE);
                        holder.item_title.setVisibility(View.VISIBLE);
                        holder.tv_unread_count.setText(String.valueOf(PinglunCount));
                        holder.tv_unread_count.setVisibility(View.VISIBLE);
                        holder.img_unread_count.setVisibility(View.VISIBLE);

                        isShow = true;
                        updateStatus();
                    } else {
                        Log.e(TAG, "onBindViewHolder: updateview2");
                        holder.item_title.setText("评论");
                        holder.item_content.setVisibility(View.GONE);
                        holder.tv_unread_count.setVisibility(View.GONE);
                        holder.img_unread_count.setVisibility(View.GONE);
                    }
/*            holder.item_title.setText("新加的");*/
                    holder.item_title.setVisibility(View.VISIBLE);
                    holder.rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CommentAboutMe.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    holder.tv_time.setVisibility(View.GONE);
                    RealmResults<NotifyData> results1 =
                            myRealm.where(NotifyData.class).equalTo("NotifyIsRead", "0").equalTo("NotifyToUser",String.valueOf(Const.currentUser.user_id)).equalTo("NotifyType", "1").findAll();
                    Glide.with(mContext)
                            .load(R.drawable.img_system)
                            .centerCrop()
                            .into(holder.img_item);
                    if (results1.size() > 0) {
                        Log.e(TAG, "onBindViewHolder: updateview1");
                        holder.item_title.setText("系统通知");
                        holder.item_content.setText("您有新的系统通知");
                        holder.item_content.setVisibility(View.VISIBLE);
                        holder.item_title.setVisibility(View.VISIBLE);
                        holder.tv_unread_count.setText(String.valueOf(results1.size()));
                        holder.tv_unread_count.setVisibility(View.VISIBLE);
                        holder.img_unread_count.setVisibility(View.VISIBLE);
                        isShow = true;
                        updateStatus();
                    } else {
                        Log.e(TAG, "onBindViewHolder: updateview2");
                        holder.item_title.setText("系统通知");
                        holder.item_content.setVisibility(View.GONE);
                        holder.tv_unread_count.setVisibility(View.GONE);
                        holder.img_unread_count.setVisibility(View.GONE);
                    }

                    holder.item_title.setVisibility(View.VISIBLE);
                    holder.rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, SystemNofityActivity.class);
                            intent.putExtra("type", "1");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 2:
                    holder.tv_time.setVisibility(View.GONE);
                    //行业通知
                    RealmResults<NotifyData> results2 =
                            myRealm.where(NotifyData.class).equalTo("NotifyIsRead", "0").equalTo("NotifyToUser",String.valueOf(Const.currentUser.user_id)).equalTo("NotifyType", "2").findAll();
                    Glide.with(mContext)
                            .load(R.drawable.img_hangye)
                            .centerCrop()
                            .into(holder.img_item);


                    if (results2.size() > 0) {
                        Log.e(TAG, "onBindViewHolder: updateview1");
                        holder.item_title.setText("行业通知");
                        holder.item_content.setText("您有新的行业通知");
                        holder.item_content.setVisibility(View.VISIBLE);
                        holder.item_title.setVisibility(View.VISIBLE);
                        holder.tv_unread_count.setText(String.valueOf(results2.size()));
                        holder.tv_unread_count.setVisibility(View.VISIBLE);
                        holder.img_unread_count.setVisibility(View.VISIBLE);
                        isShow = true;
                        updateStatus();
                    } else {
                        Log.e(TAG, "onBindViewHolder: updateview2");
                        holder.item_title.setText("行业通知");
                        holder.item_content.setVisibility(View.GONE);
                        holder.tv_unread_count.setVisibility(View.GONE);
                        holder.img_unread_count.setVisibility(View.GONE);
                    }

                    holder.item_title.setVisibility(View.VISIBLE);
                    holder.rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, SystemNofityActivity.class);
                            intent.putExtra("type", "2");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 3:
                    holder.tv_time.setVisibility(View.GONE);
                    //群通知
                    RealmResults<NotifyData> results3 =
                            myRealm.where(NotifyData.class).equalTo("NotifyIsRead", "0").equalTo("NotifyToUser",String.valueOf(Const.currentUser.user_id)).equalTo("NotifyType", "3").findAll();

                    Glide.with(mContext)
                            .load(R.drawable.img_group_default)
                            .centerCrop()
                            .into(holder.img_item);
                    if (results3.size() > 0) {
                        Log.e(TAG, "onBindViewHolder: updateview1");
                        holder.item_title.setText("群通知");
                        holder.item_content.setText("您有新的群通知");
                        holder.item_content.setVisibility(View.VISIBLE);
                        holder.item_title.setVisibility(View.VISIBLE);
                        holder.tv_unread_count.setText(String.valueOf(results3.size()));
                        holder.tv_unread_count.setVisibility(View.VISIBLE);
                        holder.img_unread_count.setVisibility(View.VISIBLE);
                        isShow = true;
                        updateStatus();
                    } else {
                        Log.e(TAG, "onBindViewHolder: updateview2");
                        holder.item_title.setText("群通知");
                        holder.item_content.setVisibility(View.GONE);
                        holder.tv_unread_count.setVisibility(View.GONE);
                        holder.img_unread_count.setVisibility(View.GONE);
                    }
                    holder.item_title.setVisibility(View.VISIBLE);
                    holder.rel_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, GroupNofityActivity.class);
                            intent.putExtra("type", "3");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        }
                    });
                    break;
            }

        } else {
            Conversation itemData = mList.get(position - 4).getConversation();
            if (mList.get(position - 4).getConverType() == 0) {
/**
 * 群聊
 * */
                final GroupData itemGroup = mList.get(position - 4).getGroupData();
/*                if (itemData == null) {
                    itemData = Conversation.createGroupConversation(Long.parseLong(itemGroup.getAppJpushGroupId()));
                }*/
                JMessageClient.getGroupInfo(Long.parseLong(itemGroup.getAppJpushGroupId()), new GetGroupInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, GroupInfo groupInfo) {
                        GroupInfo getGroup = groupInfo;
                        holder.item_title.setText(getGroup.getGroupName());
                    }
                });
                holder.item_title.setVisibility(View.VISIBLE);
                //头像

                if (!TextUtils.isEmpty(itemGroup.getAppGroupAvatar())) {
                    Glide.with(mContext)
                            .load(Const.IMG_URL + itemGroup.getAppGroupAvatar())
                            .centerCrop()
                            .into(holder.img_item);
                } else {
                    holder.img_item.setImageResource(R.drawable.img_group_default);
                }
                holder.rel_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent notificationIntent = new Intent(mContext, ChatActivity.class);

                        notificationIntent.putExtra(Application.TARGET_APP_KEY, itemGroup.getAppJpushGroupId());
                        notificationIntent.putExtra(Application.GROUP_ID, Long.parseLong(itemGroup.getAppJpushGroupId()));

                        notificationIntent.putExtra("fromGroup", false);
                        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(notificationIntent);
                        Intent intent_update = new Intent();  //Itent就是我们要发送的内容
                        intent_update.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                        mContext.sendBroadcast(intent_update);   //发送广播
                    }
                });
            } else if (mList.get(position - 4).getConverType() == 1) {
                /**
                 * 单聊
                 * */
                // UserInfo userInfo = (UserInfo) itemData.getTargetInfo();
                JMessageClient.getUserInfo((String) itemData.getTargetId(), new GetUserInfoCallback() {
                    @Override
                    public void gotResult(int i, String s, UserInfo userInfo) {
                        final UserInfo mUserInfo = userInfo;
                        //title
                        holder.item_title.setText(mUserInfo.getNickname());

                        holder.item_title.setVisibility(View.VISIBLE);
                        //头像
                        if (mUserInfo.getAvatarFile() == null) {
                            //   Glide.with(mContext).load(R.drawable.default_avater).centerCrop().into(holder.img_item);
                            holder.img_item.setImageResource(R.drawable.default_avater);
                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(holder.img_item);
                        }

                        holder.rel_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent notificationIntent = new Intent(mContext, ChatActivity.class);

                                notificationIntent.putExtra(Application.TARGET_APP_KEY, mUserInfo.getAppKey());
                                notificationIntent.putExtra(Application.TARGET_ID, mUserInfo.getUserName());

                                notificationIntent.putExtra("fromGroup", false);
                                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                mContext.startActivity(notificationIntent);
                                Intent intent_update = new Intent();  //Itent就是我们要发送的内容
                                intent_update.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                                mContext.sendBroadcast(intent_update);   //发送广播
                            }
                        });
                    }
                });
            }
            if (itemData != null) {
                if (itemData.getLatestMessage() != null) {
                    Message lastMessage = itemData.getLatestMessage();
                    if (itemData.getLatestMessage() != null) {
                        String time = DateTools.getLongToString(lastMessage.getCreateTime());
                        Long[] longDiff = null;
                        String NowTime = null;
                        try {
                            NowTime = DateTools.getNowTime();
                            longDiff = DateTools.getDiffTime(NowTime, time);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String diffDay = String.valueOf(longDiff[1]);
                        String diffHour = String.valueOf(longDiff[2]);
                        String diffMinutes = String.valueOf(longDiff[3]);
                        if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) == 0) {
                            if (Integer.parseInt(diffMinutes) < 5) {
                                holder.tv_time.setVisibility(View.VISIBLE);
                                holder.tv_time.setText("刚刚");
                            } else {
                                holder.tv_time.setVisibility(View.VISIBLE);
                                holder.tv_time.setText(diffMinutes + "分钟前");
                            }
                        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
                            holder.tv_time.setVisibility(View.VISIBLE);
                            holder.tv_time.setText(diffHour + "小时前");
                        } else {
                            holder.tv_time.setVisibility(View.VISIBLE);
                            holder.tv_time.setText(DateTools.StringDateTimeToDateNoYear(time));
                        }
                    }
                } else {
                    holder.tv_time.setVisibility(View.GONE);
                }
                holder.rel_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Log.e(TAG, "onLongClick: success");
                        iosDialog mIosDialog = new iosDialog.Builder(mContext)
                                .setMessage("删除确认！")
                                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (mList.get(position - 4).getConverType() == 0) {
                                            Log.e(TAG, "onClick: 删除群聊");
                                            /**
                                             * 删除群组
                                             * */
                                            if (JMessageClient.deleteGroupConversation(Long.parseLong(mList.get(position - 4).getGroupData().getAppJpushGroupId()))) {
                                                Log.e(TAG, "onClick: 群聊删除成功");
                                            }
                                        } else if (mList.get(position - 4).getConverType() == 1) {
                                            Log.e(TAG, "onClick: 删除单聊");

                                            /**
                                             * 删除单聊
                                             * */
                                            if (JMessageClient.deleteSingleConversation(mList.get(position - 4).getConversation().getTargetId(), mList.get(position - 4).getConversation().getTargetAppKey())) {
                                                Log.e(TAG, "onClick: 单聊删除成功");
                                            }

                                        }
                                        Log.e(TAG, "onClick: 确认删除");
                                        mList.get(position - 4).getConversation().setUnReadMessageCnt(0);
                                        mList.remove(position - 4);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, mList.size());
                                        dialog.dismiss();
                                      /*  Intent intent_update = new Intent();  //Itent就是我们要发送的内容
                                        intent_update.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                                        mContext.sendBroadcast(intent_update);   //发送广播*/
                                    }
                                })
                                .setNegativeButton("暂不删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.e(TAG, "onClick: 暂不删除");
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("种愿").create();
                        mIosDialog.show();
                        return true;
                    }
                });

                if (itemData.getUnReadMsgCnt() > 99) {
                    isShow = true;
                    updateStatus();

                    Message lastMessage = itemData.getLatestMessage();
                    holder.tv_unread_count.setText("99");
                    holder.item_content.setVisibility(View.VISIBLE);
                    switch (lastMessage.getContentType()) {
                        case text:
                            TextContent textContent = (TextContent) lastMessage.getContent();
                            holder.item_content.setText(textContent.getText());
                            break;
                        case image:
                            holder.item_content.setText("[图片消息]");
                            break;
                        case voice:
                            VoiceContent voiceContent = (VoiceContent) lastMessage.getContent();
                            voiceContent.getLocalPath();//语音文件本地地址
                            voiceContent.getDuration();//语音文件时长
                            holder.item_content.setText("[语音消息]");
                            break;
                        case custom:
                            holder.item_content.setText("新消息");
                            break;
                        case eventNotification:
                            //处理事件提醒消息
                            EventNotificationContent eventNotificationContent = (EventNotificationContent) lastMessage.getContent();
                            switch (eventNotificationContent.getEventNotificationType()) {
                                case group_member_added:
                                    //群成员加群事件
                                    holder.item_content.setText(eventNotificationContent.getEventText());
                                    holder.item_content.setSingleLine(false);
                                    holder.item_content.setMaxLines(1);
                                    holder.item_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                                    break;
                                case group_member_removed:
                                    //群成员被踢事件
                                    holder.item_content.setText(eventNotificationContent.getEventText());
                                    break;
                                case group_member_exit:
                                    //群成员退群事件
                                    holder.item_content.setText(eventNotificationContent.getEventText());
                                    break;
                            }
                            break;
                    }

                } else {
                    Message lastMessage = itemData.getLatestMessage();
                    if (lastMessage == null) {
                        holder.tv_unread_count.setVisibility(View.GONE);
                        holder.img_unread_count.setVisibility(View.GONE);
                        holder.item_content.setVisibility(View.GONE);
                    } else if (lastMessage != null) {
                        if (itemData.getUnReadMsgCnt() > 0) {
                            isShow = true;
                            updateStatus();
                            holder.tv_unread_count.setVisibility(View.VISIBLE);
                            holder.img_unread_count.setVisibility(View.VISIBLE);
                            holder.item_content.setVisibility(View.VISIBLE);

                        } else {
                            holder.tv_unread_count.setVisibility(View.GONE);
                            holder.img_unread_count.setVisibility(View.GONE);
                            holder.item_content.setVisibility(View.GONE);
                        }

                        holder.item_content.setVisibility(View.VISIBLE);
                        switch (lastMessage.getContentType()) {
                            case text:
                                TextContent textContent = (TextContent) lastMessage.getContent();
                                holder.item_content.setText(textContent.getText());
                                break;
                            case image:
                                holder.item_content.setText("[图片消息]");
                                break;
                            case voice:
                                VoiceContent voiceContent = (VoiceContent) lastMessage.getContent();
                                voiceContent.getLocalPath();//语音文件本地地址
                                voiceContent.getDuration();//语音文件时长
                                holder.item_content.setText("[语音消息]");
                                break;
                            case custom:
                                holder.item_content.setText("新消息");
                                break;
                            case eventNotification:
                                //处理事件提醒消息
                                EventNotificationContent eventNotificationContent = (EventNotificationContent) lastMessage.getContent();
                                switch (eventNotificationContent.getEventNotificationType()) {
                                    case group_member_added:
                                        //群成员加群事件
                                        holder.item_content.setText(eventNotificationContent.getEventText());
                                        holder.item_content.setSingleLine(false);
                                        holder.item_content.setMaxLines(1);
                                        holder.item_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                                        holder.item_content.setVisibility(View.VISIBLE);
                                        break;
                                    case group_member_removed:
                                        //群成员被踢事件
                                        holder.item_content.setText(eventNotificationContent.getEventText());
                                        holder.item_content.setVisibility(View.VISIBLE);
                                        break;
                                    case group_member_exit:
                                        //群成员退群事件
                                        holder.item_content.setText(eventNotificationContent.getEventText());
                                        holder.item_content.setVisibility(View.VISIBLE);
                                        break;
                                }
                                break;
                        }
                    }
                    holder.tv_unread_count.setText(String.valueOf(itemData.getUnReadMsgCnt()));
                }
            }

        }
        updateStatus();
    }

    public void setPinglun(int unReadCount) {
        PinglunCount = unReadCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item, img_unread_count;
        TextView item_content, tv_unread_count, tv_time, item_title;
        RelativeLayout rel_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            img_unread_count = (ImageView) itemView.findViewById(R.id.img_unread_count);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            tv_unread_count = (TextView) itemView.findViewById(R.id.tv_unread_count);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
        }
    }


    public void updateStatus() {
        if (isShow) {
            ((HomePageActivity) mContext).isHavaMessage(true);
        } else {
            ((HomePageActivity) mContext).isHavaMessage(false);
        }
    }

    public void setStatus() {
        isShow = false;
    }
}
