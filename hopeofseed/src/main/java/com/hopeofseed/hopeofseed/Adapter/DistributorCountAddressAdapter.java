package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.CommodityAddressData;
import com.hopeofseed.hopeofseed.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 17:58
 * 修改人：whisper
 * 修改时间：2016/12/30 17:58
 * 修改备注：
 */
public class DistributorCountAddressAdapter extends RecyclerView.Adapter<DistributorCountAddressAdapter.ViewHolder> {
    private static final String TAG = "DistributorCountAddress";
    Context mContext;
    List<CommodityAddressData> mList;
    private LayoutInflater inflater;

    public DistributorCountAddressAdapter(Context context, List<CommodityAddressData> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.distributor_count_address_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CommodityAddressData itemData = mList.get(position);
        Log.e(TAG, "onBindViewHolder: " + itemData.getDistributorProvince() + itemData.getDistributorCity() + itemData.getDistributorZone());
        CommodityAddressData lastitemData = null;
        if (position > 0) {
            lastitemData = mList.get(position - 1);
        }
        if (position == 0) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.tv_province.setText(itemData.getDistributorProvince());
        } else if (itemData.getDistributorProvince().equals(lastitemData.getDistributorProvince())) {
            holder.itemView.setVisibility(View.GONE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.tv_province.setText(itemData.getDistributorProvince());
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_view;
        TextView tv_province;
        RelativeLayout item_rel;

        public ViewHolder(View itemView) {
            super(itemView);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            tv_province = (TextView) itemView.findViewById(R.id.tv_province);
            item_rel = (RelativeLayout) itemView.findViewById(R.id.item_rel);
        }
    }
}
