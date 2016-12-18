package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.Activitys.SettingCommodityActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
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
public class DistributorForCommodityAdapter extends RecyclerView.Adapter<DistributorForCommodityAdapter.ViewHolder> {
    private static final String TAG = "DistributorForCommodity";
    Context mContext;
    List<DistributorData> mlist;

    public DistributorForCommodityAdapter(Context context, ArrayList<DistributorData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.distributor_for_commodity_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DistributorData mData;
        mData = mlist.get(position);
        holder.tv_distributor_name.setText(mData.getDistributorName());
        holder.tv_address.setText(mData.getDistributorProvince() + " " + mData.getDistributorCity() + " " + mData.getDistributorZone() + "\n" + mData.getDistributorAddressDetail());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), holder);
        holder.btn_commodity_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingCommodityActivity.class);
                intent.putExtra("DistributorId", mData.getDistributorId());
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
        ImageView img_user;
        TextView tv_distributor_name, btn_commodity_setting, tv_address;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_distributor_name = (TextView) itemView.findViewById(R.id.tv_distributor_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            btn_commodity_setting = (TextView) itemView.findViewById(R.id.btn_commodity_setting);
        }
    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                Glide.with(mContext)
                        .load(userInfo.getAvatarFile())
                        .centerCrop()
                        .into(holder.img_user);
            }
        });
    }
}
