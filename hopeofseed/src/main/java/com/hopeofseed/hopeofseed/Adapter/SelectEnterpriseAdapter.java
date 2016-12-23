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
import com.hopeofseed.hopeofseed.JNXData.EnterpriseCommodityArray;
import com.hopeofseed.hopeofseed.R;
import java.util.ArrayList;
import java.util.List;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;
import static com.hopeofseed.hopeofseed.R.id.rel_title;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class SelectEnterpriseAdapter extends RecyclerView.Adapter<SelectEnterpriseAdapter.ViewHolder> {
    Context mContext;
    List<EnterpriseCommodityArray> mlist;

    public SelectEnterpriseAdapter(Context context, ArrayList<EnterpriseCommodityArray> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.select_enterprise_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final EnterpriseCommodityArray mData;
        mData = mlist.get(position);
        CommodityImageAdapter gridAdapter = new CommodityImageAdapter(mContext, mData.getCommodityData());
        holder.resultRecyclerView.setAdapter(gridAdapter);
        holder.tv_name.setText(mData.getEnterpriseName());
        holder.tv_address.setText(mData.getEnterpriseProvince() + "  " + mData.getEnterpriseCity() + " " + mData.getEnterpriseZone());
        holder.tv_enterprise_address_detail.setText(mData.getEnterpriseAddressDetail());
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                intent.putExtra("userid", String.valueOf(mData.getUser_id()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), holder);
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
        TextView tv_name, tv_address, tv_distance, tv_enterprise_address_detail;
        RecyclerView resultRecyclerView;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_enterprise_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_enterprise_address);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            tv_distance = (TextView) itemView.findViewById(R.id.tv_distance);
            tv_enterprise_address_detail = (TextView) itemView.findViewById(R.id.tv_enterprise_address_detail);
            resultRecyclerView = (RecyclerView) itemView.findViewById(R.id.result_recycler);
            resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        }
    }

    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case rel_title:
                    intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                    intent.putExtra("userid", String.valueOf(view.getTag(R.id.key_commodity_title_id)));
                    mContext.startActivity(intent);
                    break;
            }
        }
    };

    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

                if (userInfo.getAvatarFile() == null) {
                    Glide.with(mContext)
                            .load(R.drawable.header_enterprise_default)
                            .centerCrop()
                            .into(holder.img_user_avatar);

                } else {
                    Glide.with(mContext)
                            .load(userInfo.getAvatarFile())
                            .centerCrop()
                            .into(holder.img_user_avatar);
                }

            }
        });
    }

}
