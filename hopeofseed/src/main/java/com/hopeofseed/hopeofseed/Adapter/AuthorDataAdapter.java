package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

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
public class AuthorDataAdapter extends RecyclerView.Adapter<AuthorDataAdapter.ViewHolder> {
    Context mContext;
    List<AuthorData> mlist;

    public AuthorDataAdapter(Context context, ArrayList<AuthorData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_author_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AuthorData mData = mlist.get(position);
        holder.tv_name.setText(mData.getAuthorName());
        holder.tv_address.setText(mData.getAuthorProvince() + " " + mData.getAuthorCity() + " " + mData.getAuthorZone());
        Glide.with(mContext)
                .load(mData.getUserAvatar()).placeholder(R.drawable.header_author_default).dontAnimate()
                .centerCrop()
                .into(holder.img_user_avatar);
        holder.rel_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", String.valueOf(mData.getUser_id()));
                intent.putExtra("UserRole", Integer.parseInt(mData.getUser_role()));
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
        TextView tv_name, tv_address;
RelativeLayout rel_title;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            rel_title=(RelativeLayout)itemView.findViewById(R.id.rel_title);
        }
    }
}
