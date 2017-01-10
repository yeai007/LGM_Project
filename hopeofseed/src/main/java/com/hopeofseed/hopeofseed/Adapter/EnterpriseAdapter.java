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
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class EnterpriseAdapter extends RecyclerView.Adapter<EnterpriseAdapter.ViewHolder> {
    Context mContext;
    List<EnterpriseData> mlist;


    public EnterpriseAdapter(Context context, ArrayList<EnterpriseData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.enterprise_items, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final EnterpriseData mData = mlist.get(position);
        holder.tv_address.setText(mData.getEnterpriseProvince() + "  " + mData.getEnterpriseCity() + " " + mData.getEnterpriseZone());
        holder.tv_address_detail.setText(mData.getEnterpriseAddressDetail());
        holder.tv_name.setText(mData.getEnterpriseName());
        Glide.with(mContext)
                .load(mData.getUserAvatar()).placeholder(R.drawable.header_enterprise_default)
                .centerCrop()
                .into(holder.img_user_avatar);
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
        ImageView img_user_avatar;
        TextView tv_name, tv_address, tv_address_detail;
        RelativeLayout rel_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_address_detail = (TextView) itemView.findViewById(R.id.tv_address_detail);
            rel_title = (RelativeLayout) itemView.findViewById(R.id.rel_title);
        }
    }

}