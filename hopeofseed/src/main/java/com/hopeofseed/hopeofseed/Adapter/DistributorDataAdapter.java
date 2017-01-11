package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodityArray;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

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
public class DistributorDataAdapter extends RecyclerView.Adapter<DistributorDataAdapter.ViewHolder> {
    Context mContext;
    List<DistributorCommodityArray> mlist;

    public DistributorDataAdapter(Context context, ArrayList<DistributorCommodityArray> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.distributor_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DistributorCommodityArray mData = mlist.get(position);
        CommodityImageAdapter gridAdapter = new CommodityImageAdapter(mContext, mData.getCommodityData());
        holder.resultRecyclerView.setAdapter(gridAdapter);
        holder.tv_name.setText(mData.getDistributorName());
        holder.tv_address.setText(mData.getDistributorProvince() + "  " + mData.getDistributorCity() + " " + mData.getDistributorZone());
        String strDistance = "0";
        int iDistance = Integer.parseInt(mData.getDistance());
        if (iDistance > 1000) {
            double c = ObjectUtil.roundDouble((double) iDistance / (double) 1000, 2);

            strDistance = String.valueOf(c);
            holder.tv_distance.setText(strDistance + "Km");
        } else {
            holder.tv_distance.setText(mData.getDistance() + "M");
        }

        holder.tv_distributor_address_detail.setText(mData.getDistributorAddressDetail());
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                intent.putExtra("UserRole", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext)
                .load(mData.getUserAvatar()).placeholder(R.drawable.header_distributor_default).dontAnimate()
                .centerCrop()
                .into(holder.img_user_avatar);
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
        TextView tv_name, tv_address, tv_distance, tv_distributor_address_detail;
        RecyclerView resultRecyclerView;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_distributor_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_distributor_address);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            tv_distributor_address_detail = (TextView) itemView.findViewById(R.id.tv_distributor_address_detail);
            resultRecyclerView = (RecyclerView) itemView.findViewById(R.id.result_recycler);
            resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
    }
}