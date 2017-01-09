package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.hopeofseed.hopeofseed.R.id.tv_name;
import static com.hopeofseed.hopeofseed.R.id.view;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class SeedfriendDataAdapter extends RecyclerView.Adapter<SeedfriendDataAdapter.ViewHolder> {
    Context mContext;
    List<UserDataNoRealm> mlist;

    public SeedfriendDataAdapter(Context context, ArrayList<UserDataNoRealm> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.select_seed_friend_list, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserDataNoRealm mData = mlist.get(position);
        holder.tv_name.setText(mData.getNickname());
        holder.tv_address.setText(mData.getUserProvince() + " " + mData.getUserCity() + " " + mData.getUserZone());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), holder, Integer.parseInt(mData.getUser_role()));
        holder.rel_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", String.valueOf(mData.getUser_id()));
                intent.putExtra("UserRole", Integer.parseInt(mData.getUser_role()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_avatar, img_corner;
        TextView tv_name, tv_address, tv_user_role;
        RelativeLayout rel_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            img_corner = (ImageView) itemView.findViewById(R.id.img_corner);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_user_role = (TextView) itemView.findViewById(R.id.tv_user_role);
            rel_title = (RelativeLayout) itemView.findViewById(R.id.rel_title);
        }
    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                holder.img_corner.setVisibility(View.VISIBLE);
                switch (user_role) {
                    case 0:
                        holder.tv_user_role.setText("【农友】");
                        holder.img_corner.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default).placeholder(R.drawable.corner_user_default).dontAnimate()
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_user_default).dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                    case 1:
                        holder.tv_user_role.setText("【经销商】");
                        holder.img_corner.setVisibility(View.VISIBLE);
                        Log.e(TAG, "gotResult: ");
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor).placeholder(R.drawable.corner_distributor).dontAnimate()
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_distributor_default).dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                    case 2:
                        holder.tv_user_role.setText("【企业】");
                        holder.img_corner.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise).placeholder(R.drawable.corner_enterprise).dontAnimate()
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_enterprise_default).dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                    case 3:
                        holder.tv_user_role.setText("【专家】");
                        holder.img_corner.setVisibility(View.VISIBLE);
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert).placeholder(R.drawable.corner_expert) .dontAnimate()
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_expert_default) .dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                    case 4:
                        holder.img_corner.setVisibility(View.VISIBLE);
                        holder.tv_user_role.setText("【机构】");
                        Glide.with(mContext)
                                .load(R.drawable.corner_author).placeholder(R.drawable.corner_author) .dontAnimate()
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_author_default)  .dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                    case 6:
                        holder.tv_user_role.setText("【媒体】");
                        holder.img_corner.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(R.drawable.user_media).placeholder(R.drawable.user_media) .dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.user_media)  .dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                    case 5:
                        holder.tv_user_role.setText("【管理员】");
                        holder.img_corner.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(R.drawable.user_system).placeholder(R.drawable.user_system).dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        Glide.with(mContext)
                                .load(userInfo.getAvatarFile()).placeholder(R.drawable.user_system) .dontAnimate()
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        break;
                }

            }
        });
    }
}
