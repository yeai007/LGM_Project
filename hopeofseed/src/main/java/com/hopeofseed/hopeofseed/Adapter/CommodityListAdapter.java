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
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.SettingDistributorActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class CommodityListAdapter extends RecyclerView.Adapter<CommodityListAdapter.ViewHolder> {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<CommodityData> mList;

    public CommodityListAdapter(Context context, List<CommodityData> list) {
        super();
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.commodity_list_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CommodityData itemData;
        itemData = mList.get(position);
        String[] arrImage = itemData.getCommodityImgs().split(";");
        if (arrImage.length > 0 && (!TextUtils.isEmpty(arrImage[0]))) {


            Glide.with(mContext)
                    .load(Const.IMG_URL + arrImage[0])
                    .centerCrop()
                    .into(holder.img);
        }

        holder.create_time.setText(DateTools.StringDateTimeToDate(itemData.getCreateTime()));
        holder.tv_name.setText(itemData.getCommodityName());
        holder.tv_content.setText(itemData.getCommodityTitle());
        holder.tv_price.setText(itemData.getCommodityPrice());
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
        holder.btn_distributor_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SettingDistributorActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv_name, tv_content, tv_price, btn_distributor_setting, create_time;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            img = (ImageView) itemView.findViewById(R.id.img);
            create_time = (TextView) itemView.findViewById(R.id.create_time);
            btn_distributor_setting = (TextView) itemView.findViewById(R.id.btn_distributor_setting);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
