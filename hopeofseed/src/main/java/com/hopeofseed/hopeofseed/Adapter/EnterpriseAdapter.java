package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.EnterpriseData;
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
public class EnterpriseAdapter extends BaseAdapter {
    Context mContext;
    List<EnterpriseData> mlist;

    public EnterpriseAdapter(Context context, ArrayList<EnterpriseData> list) {
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
        EnterpriseData mData;
        mData = mlist.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.enterprise_items, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.img_user_avatar=(ImageView) view.findViewById(R.id.img_user_avatar);
            viewHolder.tv_address=(TextView)view.findViewById(R.id.tv_address);
            viewHolder.tv_address_detail=(TextView)view.findViewById(R.id.tv_address_detail);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_address.setText(mData.getEnterpriseProvince() + "  " + mData.getEnterpriseCity() + " " + mData.getEnterpriseZone());
        viewHolder.tv_address_detail.setText(mData.getEnterpriseAddressDetail());
        viewHolder.tv_name.setText(mData.getEnterpriseName());
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), viewHolder);
        return view;
    }

    class ViewHolder {
        ImageView img_user_avatar;
        TextView tv_name, tv_address, tv_address_detail;
    }
    private void getUserJpushInfo(String user_name, final ViewHolder holder) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

/*                if (userInfo.getAvatarFile() == null) {
                    Glide.with(mContext)
                            .load(R.drawable.header_enterprise_default).placeholder(R.drawable.no_have_img)
                            .centerCrop()
                            .into(holder.img_user_avatar);

                } else {*/
                    Glide.with(mContext)
                            .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_enterprise_default)
                            .centerCrop()
                            .into(holder.img_user_avatar);
              /*  }*/

            }
        });
    }
}