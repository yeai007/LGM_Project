package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommentAboutMeData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.hopeofseed.hopeofseed.R.id.send_time;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/5 17:58
 * 修改人：whisper
 * 修改时间：2016/12/5 17:58
 * 修改备注：
 */
public class CommentAboutMeRecyclerAdapter extends RecyclerView.Adapter<CommentAboutMeRecyclerAdapter.ViewHolder> {
    Context mContext;
    List<CommentAboutMeData> mList;
    private LayoutInflater inflater;

    public CommentAboutMeRecyclerAdapter(Context context, ArrayList<CommentAboutMeData> arrCommentAboutMeData) {
        this.mContext = context;
        this.mList = arrCommentAboutMeData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.comment_about_me_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommentAboutMeData itemData = new CommentAboutMeData();
        itemData = mList.get(position);
        holder.tv_comment_content.setText(itemData.getCommentComment());
        holder.tv_comment_user_nickname.setText(itemData.getNickname());
        updateTime(holder, itemData.getCommentRecordCreateTime());
        String[] arrImage = itemData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        if (images.size() > 0) {
            Glide.with(mContext)
                    .load(Const.IMG_URL + images.get(0))
                    .centerCrop()
                    .into(holder.img_new);
        } else {
        }
        getJpushUserHead(holder, itemData);
        final CommentAboutMeData finalItemData = itemData;
        holder.rel_comment_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", finalItemData.getUser_id());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.rel_comment_content.setOnClickListener(new
                                                              View.OnClickListener() {
                                                                  @Override
                                                                  public void onClick(View v) {
                                                                      Intent intent = new Intent(mContext, NewsInfoNewActivity.class);
                                                                      intent.putExtra("NEWID", String.valueOf(finalItemData.getId()));
                                                                      intent.putExtra("NewClass", finalItemData.getNewclass());
                                                                      intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                                                      mContext.startActivity(intent);
                                                                  }
                                                              });
        holder.rel_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsInfoNewActivity.class);
                intent.putExtra("NEWID", String.valueOf(finalItemData.getId()));
                intent.putExtra("NewClass", finalItemData.getNewclass());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_user_avatar, img_corner, img_new;
        TextView tv_comment_user_nickname, tv_comment_content, tv_time;
        RelativeLayout rel_comment_user, rel_new, rel_comment_content;

        public ViewHolder(View view) {
            super(view);
            img_user_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
            img_corner = (ImageView) view.findViewById(R.id.img_corner);
            img_new = (ImageView) view.findViewById(R.id.img_new);
            tv_comment_user_nickname = (TextView) view.findViewById(R.id.tv_comment_user_nickname);
            tv_comment_content = (TextView) view.findViewById(R.id.tv_comment_content);
            rel_comment_user = (RelativeLayout) view.findViewById(R.id.rel_comment_user);
            rel_new = (RelativeLayout) view.findViewById(R.id.rel_new);
            rel_comment_content = (RelativeLayout) view.findViewById(R.id.rel_comment_content);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
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

    private void getJpushUserHead(ViewHolder holder, final CommentAboutMeData itemData) {
        ImageView img_corner = holder.img_corner;
        ImageView img_user = holder.img_user_avatar;
        final ImageView finalImg_corner = img_corner;
        final ImageView finalImg_user = img_user;
        JMessageClient.getUserInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                //     Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                int user_role = Integer.parseInt(itemData.getUser_role());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_user_default).centerCrop().into(finalImg_user);
                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 1:
                        Glide.with(mContext).load(R.drawable.corner_distributor).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_distributor_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 2:
                        Glide.with(mContext).load(R.drawable.corner_enterprise).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_enterprise_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 3:
                        Glide.with(mContext).load(R.drawable.corner_expert).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_expert_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 4:
                        Glide.with(mContext).load(R.drawable.corner_author).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_author_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                }

            }
        });
    }
}