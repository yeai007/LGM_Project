package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
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
public class SeedfriendDataAdapter extends BaseAdapter {
    Context mContext;
    List<UserDataNoRealm> mlist;

    public SeedfriendDataAdapter(Context context, ArrayList<UserDataNoRealm> list) {
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
        UserDataNoRealm mData;
        mData = mlist.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.select_seed_friend_list, null);
            viewHolder.tv_name = (TextView) view.findViewById(tv_name);
            viewHolder.img_user_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
            viewHolder.img_corner = (ImageView) view.findViewById(R.id.img_corner);
            viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
            viewHolder.tv_user_role=(TextView)view.findViewById(R.id.tv_user_role);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Log.e(TAG, "getView: " + mData.getNickname());
        viewHolder.tv_name.setText(mData.getNickname());
        viewHolder.tv_address.setText(mData.getUserProvince() + " " + mData.getUserCity() + " " + mData.getUserZone());
        getUserJpushInfo(Const.JPUSH_PREFIX+mData.getUser_id(), viewHolder, Integer.parseInt(mData.getUser_role()));
        return view;
    }

    class ViewHolder {
        ImageView img_user_avatar, img_corner;
        TextView tv_name, tv_address,tv_user_role;

    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                switch (user_role) {
                    case 0:
                        Log.e(TAG, "gotResult: 农友");
                        holder.tv_user_role.setText("【农友】");
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default)
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(R.drawable.header_user_default)
                                .centerCrop()
                                .into(holder.img_user_avatar);
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
                        break;
                    case 1:
                        holder.tv_user_role.setText("【经销商】");
                        Log.e(TAG, "gotResult: ");
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor)
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(R.drawable.header_distributor_default)
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_distributor_default)
                                    .centerCrop()
                                    .into(holder.img_user_avatar);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user_avatar);
                        }
                        break;
                    case 2:
                        holder.tv_user_role.setText("【企业】");
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise)
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(R.drawable.header_enterprise_default)
                                .centerCrop()
                                .into(holder.img_user_avatar);
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
                        break;
                    case 3:
                        holder.tv_user_role.setText("【专家】");
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert)
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(R.drawable.header_expert_default)
                                .centerCrop()
                                .into(holder.img_user_avatar);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_expert_default)
                                    .centerCrop()
                                    .into(holder.img_user_avatar);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user_avatar);
                        }
                        break;
                    case 4:
                        holder.tv_user_role.setText("【机构】");
                        Glide.with(mContext)
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(holder.img_corner);
                        Glide.with(mContext)
                                .load(R.drawable.header_author_default)
                                .centerCrop()
                                .into(holder.img_user_avatar);

                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_author_default)
                                    .centerCrop()
                                    .into(holder.img_user_avatar);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user_avatar);
                        }
                        break;
                }

            }
        });
    }
}
