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
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.R;

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
public class CommodityRecycleListAdapter extends RecyclerView.Adapter<CommodityRecycleListAdapter.ViewHolder> {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<CommodityData> mList;

    public CommodityRecycleListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.commdity_list_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CommodityData itemData = mList.get(position);
        String[] arrImage = itemData.getCommodityImgs().split(";");
        if (arrImage.length > 0 && (!TextUtils.isEmpty(arrImage[0]))) {
            Log.e(TAG, "getView: " + Const.IMG_URL + arrImage[0]);
            Glide.with(mContext)
                    .load(Const.IMG_URL + arrImage[0])
                    .centerCrop()
                    .into(holder.commodity_img);
        }
        holder.commodity_name.setText(itemData.getCommodityName());
        holder.commodity_price.setText("￥" + itemData.getCommodityPrice());
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CommodityId", itemData.getCommodityId());
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
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView commodity_img;
        TextView commodity_name, commodity_price;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            commodity_img = (ImageView) itemView.findViewById(R.id.commodity_img);
            commodity_name = (TextView) itemView.findViewById(R.id.commodity_name);
            commodity_price = (TextView) itemView.findViewById(R.id.commodity_price);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
