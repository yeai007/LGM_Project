package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodity;
import com.hopeofseed.hopeofseed.JNXData.DistributorCommodityArray;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.hopeofseed.hopeofseed.R.id.tv_name;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class DistributorDataAdapter extends BaseAdapter {
    Context mContext;
    List<DistributorCommodityArray> mlist;

    public DistributorDataAdapter(Context context, ArrayList<DistributorCommodityArray> list) {
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
        DistributorCommodityArray mData;
        mData = mlist.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.distributor_items, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_distributor_name);
            viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_distributor_address);
            viewHolder.img_user_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
            Log.e(TAG, "getView: " + mData.getDistributorName());
            viewHolder.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
            viewHolder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        CommodityImageAdapter gridAdapter = new CommodityImageAdapter(mContext, mData.getCommodityData());
        viewHolder.resultRecyclerView.setAdapter(gridAdapter);
        viewHolder.tv_name.setText(mData.getDistributorName());
        viewHolder.tv_address.setText(mData.getDistributorProvince() + "  " + mData.getDistributorCity() + " " + mData.getDistributorZone());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), viewHolder);
        return view;
    }

    class ViewHolder {

        ImageView img_user_avatar, img_corner;
        TextView tv_name, tv_address;
        RecyclerView resultRecyclerView;

    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

                if (userInfo.getAvatarFile() == null) {
                    Glide.with(mContext)
                            .load(R.drawable.header_user_default)
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