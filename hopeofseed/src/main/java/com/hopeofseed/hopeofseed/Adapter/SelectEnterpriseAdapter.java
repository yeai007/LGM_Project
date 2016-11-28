package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class SelectEnterpriseAdapter extends BaseAdapter {
    Context mContext;
    List<EnterpriseCommodityArray> mlist;

    public SelectEnterpriseAdapter(Context context, ArrayList<EnterpriseCommodityArray> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        EnterpriseCommodityArray mData;
        mData = mlist.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.select_enterprise_items, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_enterprise_name);
            viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_enterprise_address);
            viewHolder.rel_title = (RelativeLayout) view.findViewById(R.id.rel_title);
            viewHolder.rel_title.setTag(R.id.key_commodity_title_id, mData.getUser_id());
            viewHolder.rel_title.setOnClickListener(pullToListViewItemOnClickListener);
            viewHolder.img_user_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
            viewHolder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            viewHolder.tv_enterprise_address_detail = (TextView) view.findViewById(R.id.tv_enterprise_address_detail);
            viewHolder.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
            viewHolder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommodityImageAdapter gridAdapter = new CommodityImageAdapter(mContext, mData.getCommodityData());
        viewHolder.resultRecyclerView.setAdapter(gridAdapter);
        viewHolder.tv_name.setText(mData.getEnterpriseName());
        viewHolder.tv_address.setText(mData.getEnterpriseProvince() + "  " + mData.getEnterpriseCity() + " " + mData.getEnterpriseZone());
        viewHolder.tv_enterprise_address_detail.setText(mData.getEnterpriseAddressDetail());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), viewHolder);
        return view;
    }

    class ViewHolder {
        ImageView img_user_avatar;
        TextView tv_name, tv_address, tv_distance, tv_enterprise_address_detail;
        RecyclerView resultRecyclerView;
        RelativeLayout rel_title;

    }

    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.rel_title:
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
