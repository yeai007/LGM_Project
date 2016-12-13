package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Application;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.chatting.BaseActivity;
import com.hopeofseed.hopeofseed.ui.chatting.ChatActivity;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.util.List;

import cn.jpush.im.android.api.model.Conversation;


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
    private static final String TAG = "UnReadConversationListA";
    List<Conversation> mList;
    Context mContext;
    private LayoutInflater inflater;

    public UnReadConversationListAdapter(Context context, List<Conversation> list) {
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder:getUnReadMsgCnt " + position);
        final Conversation itemData = mList.get(position);
        String date = DateTools.getDateToString(String.valueOf(itemData.getLastMsgDate()));
        updateTime(holder, date);
        Log.e(TAG, "onBindViewHolder: getUnReadMsgCnt" + itemData.getUnReadMsgCnt());
        //holder.item_content.setText();
        if (itemData.getUnReadMsgCnt() > 99) {
            holder.tv_unread_count.setText("99");
        } else {
            holder.tv_unread_count.setText(String.valueOf(itemData.getUnReadMsgCnt()));
        }
        holder.rel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent notificationIntent = new Intent(mContext, ChatActivity.class);

                notificationIntent.putExtra(Application.TARGET_APP_KEY, itemData.getTargetAppKey());
                notificationIntent.putExtra(Application.GROUP_ID, Long.parseLong(itemData.getTargetId()));

                notificationIntent.putExtra("fromGroup", false);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mContext.startActivity(notificationIntent);
            }
        });
        holder.rel_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e(TAG, "onLongClick: success");
                iosDialog mIosDialog = new iosDialog.Builder(mContext)
                        .setMessage("删除确认！")
                        .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG, "onClick: 确认删除");
                                itemData.setUnReadMessageCnt(0);
                                mList.remove(position);
                                notifyItemRemoved(position);
                                dialog.dismiss();
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    private void delThisItem() {
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView item_content, tv_unread_count, tv_time;
        RelativeLayout rel_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            tv_unread_count = (TextView) itemView.findViewById(R.id.tv_unread_count);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    private void updateTime(ViewHolder holder, String time) {
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
                holder.tv_time.setText("刚刚");
            } else {
                holder.tv_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.tv_time.setText(diffHour + "小时前");
        } else {
            holder.tv_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }
}
