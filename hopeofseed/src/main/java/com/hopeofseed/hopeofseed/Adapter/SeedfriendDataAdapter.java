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

        updateUserAvata(holder.img_corner, holder.img_user_avatar, Integer.parseInt(mData.getUser_role()), mData.getUserAvatar());
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
                imageConner.setVisibility(View.GONE);
                Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(imageConner);
                Glide.with(mContext).load(avatarURL).placeholder(R.drawable.user_system).dontAnimate().centerCrop().into(ImageAvatar);
                break;
        }
    }


}
