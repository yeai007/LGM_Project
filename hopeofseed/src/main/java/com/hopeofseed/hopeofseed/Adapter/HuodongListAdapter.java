package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.HuodongData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

import java.text.ParseException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class HuodongListAdapter extends RecyclerView.Adapter<HuodongListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<HuodongData> mList;
    Context mContext;
    private LayoutInflater inflater;

    public HuodongListAdapter(Context context, List<HuodongData> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.huodong_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HuodongData itemData = mList.get(position);
        String[] arrImage = itemData.getHuodongImg().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        holder.result_recycler.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.result_recycler.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.result_recycler.setVisibility(View.GONE);
            } else {
                holder.result_recycler.setVisibility(View.VISIBLE);
            }
        } else {
            holder.result_recycler.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.result_recycler.setAdapter(gridAdapter);
        holder.user_name.setText(itemData.getNickname());
        holder.tv_title.setText("【活动】" + "【" + itemData.getHuodongTitle() + "】");
        holder.tv_content.setText(itemData.getHuodongContent().replace("\\n", "\n"));
        holder.tv_content.setSingleLine(false);
        holder.tv_content.setMaxLines(3);
        holder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        updateTime(itemData.getHuodongCreateTime(), holder);
        updateUserAvata(holder.img_corner, holder.img_user_avatar, Integer.parseInt(itemData.getUser_role()), itemData.getUserAvatar());
        holder.rel_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 7);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(itemData.getHuodongId()));
                mContext.startActivity(intent);
            }
        });
    }

    private void updateUserAvata(ImageView imageConner, ImageView ImageAvatar, int UserRole, String avatarURL) {
        imageConner.setVisibility(View.VISIBLE);
        avatarURL = Const.IMG_URL + avatarURL;
        switch (UserRole) {
            case 0:
                Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.header_user_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 1:
                Glide.with(mContext).load(R.drawable.corner_distributor).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.header_distributor_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 2:
                Glide.with(mContext).load(R.drawable.corner_enterprise).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.header_enterprise_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 3:
                Glide.with(mContext).load(R.drawable.corner_expert).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.header_expert_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 4:
                Glide.with(mContext).load(R.drawable.corner_author).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.header_author_default).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 5:
                imageConner.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.user_media).dontAnimate().centerCrop().into(ImageAvatar);
                break;
            case 6:
                Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.user_system).dontAnimate().centerCrop().into(ImageAvatar);
                break;
        }
    }

    private void updateTime(String huodongCreateTime, ViewHolder holder) {
        Long[] longDiff = null;
        String NowTime = null;
        try {
            NowTime = DateTools.getNowTime();
            longDiff = DateTools.getDiffTime(NowTime, huodongCreateTime);
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(huodongCreateTime));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_avatar, img_corner;
        TextView user_name, send_time, tv_title, tv_content;
        RecyclerView result_recycler;
        RelativeLayout rel_content;

        public ViewHolder(View itemView) {
            super(itemView);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            img_corner = (ImageView) itemView.findViewById(R.id.img_corner);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            send_time = (TextView) itemView.findViewById(R.id.send_time);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            result_recycler = (RecyclerView) itemView.findViewById(R.id.result_recycler);
            rel_content = (RelativeLayout) itemView.findViewById(R.id.rel_content);
        }
    }
}
