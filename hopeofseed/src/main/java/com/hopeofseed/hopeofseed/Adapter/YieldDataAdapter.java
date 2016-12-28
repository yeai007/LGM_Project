package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.JNXData.YieldData;
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
public class YieldDataAdapter extends RecyclerView.Adapter<YieldDataAdapter.ViewHolder> {
    Context mContext;
    List<YieldData> mlist;

    public YieldDataAdapter(Context context, ArrayList<YieldData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_yield_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final YieldData mData = mlist.get(position);
        holder.tv_crop_class.setText("【产量表现】");
        holder.tv_name.setText("【" + mData.getYieldVariety() + "】");
        holder.tv_content.setText("总产量：" + mData.getYieldSum() + "\n种植面积：" + mData.getYieldArea() + "\n平均产量：" + mData.getYieldYield() + "\n表现:" + mData.getYieldEssay().replace("\\n", "\n"));
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 4);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(mData.getYieldId()));
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
        TextView tv_crop_class, tv_name, tv_content;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_crop_class = (TextView) itemView.findViewById(R.id.tv_crop_class);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
