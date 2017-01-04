package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class DistributorListAdapter extends RecyclerView.Adapter<DistributorListAdapter.ViewHolder> {
    Context mContext;
    List<DistributorData> mlist;


    public DistributorListAdapter(Context context, ArrayList<DistributorData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.distributor_list_items, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DistributorData itemData = mlist.get(position);
        holder.tv_distributor_name.setText(itemData.getDistributorName());
        holder.tv_distributor_address.setText("地址：  " + itemData.getDistributorProvince() + "  " + itemData.getDistributorCity() + "  " + itemData.getDistributorZone());
        holder.tv_distributor_address_detail.setText("详细地址：  " + itemData.getDistributorAddressDetail());
        if (!TextUtils.isEmpty(itemData.getDistance())) {
            holder.tv_distance.setText(itemData.getDistance());
            String strDistance = "0";
            int iDistance = Integer.parseInt(itemData.getDistance());
            if (iDistance > 1000) {
                double c = ObjectUtil.roundDouble((double) iDistance / (double) 1000, 2);
                strDistance = String.valueOf(c);
                holder.tv_distance.setText(strDistance + "Km");
            } else {
                holder.tv_distance.setText(itemData.getDistance() + "M");
            }
        }
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", itemData.getUser_id());
                intent.putExtra("UserRole", Integer.parseInt(itemData.getUser_role()));
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
        TextView tv_distributor_name, tv_distributor_address, tv_distributor_address_detail, tv_distance;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_distributor_name = (TextView) itemView.findViewById(R.id.tv_distributor_name);
            tv_distributor_address = (TextView) itemView.findViewById(R.id.tv_distributor_address);
            tv_distributor_address_detail = (TextView) itemView.findViewById(R.id.tv_distributor_address_detail);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
        }
    }
}
