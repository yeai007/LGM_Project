package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.ExpertData;
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
public class ExpertDataAdapter extends RecyclerView.Adapter<ExpertDataAdapter.ViewHolder> {
    Context mContext;
    List<ExpertData> mlist;

    public ExpertDataAdapter(Context context, ArrayList<ExpertData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.expert_items, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExpertData mData = mlist.get(position);
        holder.tv_name.setText(mData.getExpertName());
        holder.tv_address.setText("地址： " + mData.getExpertProvince() + "  " + mData.getExpertCity() + "  " + mData.getExpertZone());
        holder.tv_danwei.setText(mData.getExpertAddressDetail());
        holder.tv_pol.setText(mData.getExpertPolitic());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), holder);
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                intent.putExtra("UserRole",Integer.parseInt( mData.getUser_role()));
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

    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                if (!(userInfo.getAvatarFile() == null)) {

                    Glide.with(mContext)
                            .load(userInfo.getAvatarFile())
                            .centerCrop()
                            .into(holder.img_user_avatar);
                }
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_address, tv_danwei, tv_pol;
        ImageView img_user_avatar;
RelativeLayout item_view;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            tv_danwei = (TextView) itemView.findViewById(R.id.tv_danwei);
            tv_pol = (TextView) itemView.findViewById(R.id.tv_pol);
            item_view=(RelativeLayout)itemView.findViewById(R.id.item_view);
        }
    }
}
