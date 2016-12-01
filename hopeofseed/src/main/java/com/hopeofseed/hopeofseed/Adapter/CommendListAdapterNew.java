package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.HaveCommentNew;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommentDataNew;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class CommendListAdapterNew extends BaseAdapter {
    Context mContext;
    List<CommentDataNew> mlist;
    public CommendListAdapterNew(Context context, ArrayList<CommentDataNew> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }
    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        final CommentDataNew mData;
        mData = mlist.get(i);
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.list_item_commend_new, null);
            viewHolder.user_name = (TextView) view.findViewById(R.id.user_name);
            viewHolder.send_time = (TextView) view.findViewById(R.id.send_time);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.img_user = (ImageView) view.findViewById(R.id.img_user);
            viewHolder.tv_huifu = (TextView) view.findViewById(R.id.tv_huifu);
            viewHolder.tv_to_user_name = (TextView) view.findViewById(R.id.tv_to_user_name);
            viewHolder.img_corner = (ImageView) view.findViewById(R.id.img_corner);
            viewHolder.btn_huifu = (Button) view.findViewById(R.id.btn_huifu);
            viewHolder.btn_huifu.setTag(R.id.record_id, mData.getCommentRecrodId());
            viewHolder.btn_huifu.setTag(R.id.record_user, mData.getUser_id());
            viewHolder.btn_huifu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HaveCommentNew) mContext).showInput(mData.getCommentRecrodId(), mData.getUser_id());
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (mData.getTo_nickname().equals("")) {
            viewHolder.user_name.setText(mData.getNickname() + mData.getTo_nickname() + ":");
            viewHolder.tv_huifu.setVisibility(View.GONE);
            viewHolder.tv_to_user_name.setVisibility(View.GONE);
        } else {
            viewHolder.user_name.setText(mData.getNickname());
            viewHolder.tv_huifu.setText("回复");
            viewHolder.tv_to_user_name.setText(mData.getTo_nickname() + ":");
            viewHolder.tv_huifu.setVisibility(View.VISIBLE);
            viewHolder.tv_to_user_name.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_content.setText(mData.getCommentComment());
        updateTime(mData.getCommentRecordCreateTime(), viewHolder);
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), viewHolder, Integer.parseInt(mData.getUser_role()));
        return view;
    }
    class ViewHolder {
        TextView user_name, send_time, tv_content, tv_huifu, tv_to_user_name;
        ImageView img_user, img_corner;
        Button btn_huifu;
    }
    private void updateTime(String time, ViewHolder holder0) {
        Long[] longDiff = null;
        String NowTime = null;
        try {
            NowTime = DateTools.getNowTime();
            longDiff = DateTools.getDiffTime(NowTime, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String diffDay = String.valueOf(longDiff[1]);
        Log.e(TAG, "getView: diffDay:" + diffDay);
        String diffHour = String.valueOf(longDiff[2]);
        Log.e(TAG, "getView: diffHour:" + diffHour);
        String diffMinutes = String.valueOf(longDiff[3]);
        Log.e(TAG, "getView: diffHour:" + diffMinutes);
        if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) == 0) {
            if (Integer.parseInt(diffMinutes) < 5) {
                holder0.send_time.setText("刚刚");
            } else {
                holder0.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder0.send_time.setText(diffHour + "小时前");
        } else {
            holder0.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }
    private void getUserJpushInfo(String user_name, final ViewHolder holder0, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                //  Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_user_default)
                                    .centerCrop()
                                    .into(holder0.img_user);
                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 1:
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_distributor_default)
                                    .centerCrop()
                                    .into(holder0.img_user);
                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 2:
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_enterprise_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 3:
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_expert_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 4:
                        Glide.with(mContext)
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_author_default)
                                    .centerCrop()
                                    .into(holder0.img_user);
                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                }

            }
        });
    }
}
