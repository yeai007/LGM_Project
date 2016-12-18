package com.hopeofseed.hopeofseed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.Activitys.SettingDistributorActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import java.util.Date;
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
public class CommodityListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<CommodityData> mList;

    public CommodityListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        final CommodityData itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.commodity_list_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.create_time = (TextView) view.findViewById(R.id.create_time);
            holder.btn_distributor_setting = (TextView) view.findViewById(R.id.btn_distributor_setting);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String[] arrImage = itemData.getCommodityImgs().split(";");
        if (arrImage.length > 0) {
            Log.e(TAG, "getView: " + Const.IMG_URL + arrImage[0]);
            Glide.with(mContext)
                    .load(Const.IMG_URL + arrImage[0])
                    .centerCrop()
                    .into(holder.img);
        }

        holder.create_time.setText(DateTools.StringDateTimeToDate(itemData.getCreateTime()));
        holder.tv_name.setText(itemData.getCommodityName());
        holder.tv_content.setText(itemData.getCommodityTitle());
        holder.tv_price.setText(itemData.getCommodityPrice());
        holder.tv_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
        holder.tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder {
        ImageView img;
        TextView tv_name, tv_content, tv_price, btn_distributor_setting, create_time;
    }
}
