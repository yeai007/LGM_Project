package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.SystemNofityDetailActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.GroupNotifyDataNorealm;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.JNXData.NotifyDataNorealm;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.realm.Realm;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class GroupNotifyListAdapter extends RecyclerView.Adapter<GroupNotifyListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<GroupNotifyDataNorealm> mList;
    Context mContext;
    private LayoutInflater inflater;
    Realm myRealm = Realm.getDefaultInstance();

    public GroupNotifyListAdapter(Context context, List<GroupNotifyDataNorealm> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.group_notify_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.e(TAG, "onBindViewHolder:getUnReadMsgCnt " + position);
        final GroupNotifyDataNorealm itemData = mList.get(position);
        updateTime(holder, itemData.getAppGroupApplyTime());
        holder.item_title.setText(itemData.getAppGroupApplyTitle());
        holder.item_content.setText(itemData.getAppGroupApplyContent());
        if (itemData.getAppGroupApplyStatus().equals("1")) {
            holder.is_agree.setText("已同意");
            holder.is_agree.setClickable(false);
        } else {
            holder.is_agree.setText("同意");
            holder.is_agree.setClickable(true);
        }
        final ArrayList<String> arrUser = new ArrayList<>();
        arrUser.add(Const.JPUSH_PREFIX + itemData.getAppGroupApplyUserId());
        holder.rel_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iosDialog mIosDialog = new iosDialog.Builder(mContext)
                        .setMessage("删除确认！")
                        .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e(TAG, "onClick: 确认删除");
                                NotifyData results1 =
                                        myRealm.where(NotifyData.class).equalTo("NotifyId", itemData.getNotifyId()).findFirst();
                                myRealm.beginTransaction();
                                results1.deleteFromRealm();
                                myRealm.commitTransaction();
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
        holder.is_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyData results1 =
                        myRealm.where(NotifyData.class).equalTo("NotifyId", itemData.getNotifyId()).findFirst();
                myRealm.beginTransaction();
                results1.setNotifyIsRead("1");

                NotifyData inNotifyData = myRealm.copyToRealmOrUpdate(results1);
                myRealm.commitTransaction();


                //Log.e(TAG, "onClick: "+itemData.getAppGroupApplyGroupId()+arrUser);
                JMessageClient.addGroupMembers(Long.parseLong(itemData.getAppGroupApplyGroupId()), arrUser, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        if (i == 0) {
                            NotifyData results1 =
                                    myRealm.where(NotifyData.class).equalTo("NotifyId", itemData.getNotifyId()).findFirst();
                            itemData.setAppGroupApplyStatus("1");
                            myRealm.beginTransaction();
                            results1.setNotifyData(itemData.toString());
                            results1.setNotifyIsRead("1");
                            NotifyData inNotifyData = myRealm.copyToRealmOrUpdate(results1);
                            myRealm.commitTransaction();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView item_content, item_title, tv_time;
        RelativeLayout rel_item;
        Button is_agree;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
            is_agree = (Button) itemView.findViewById(R.id.is_agree);
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
