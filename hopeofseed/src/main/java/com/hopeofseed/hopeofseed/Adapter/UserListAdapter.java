package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.SystemNofityDetailActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXData.NotifyDataNorealm;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static android.R.attr.id;
import static com.hopeofseed.hopeofseed.R.id.img_item;
import static com.hopeofseed.hopeofseed.R.id.ischeck;
import static com.hopeofseed.hopeofseed.R.id.item_content;
import static com.hopeofseed.hopeofseed.R.id.item_title;
import static com.hopeofseed.hopeofseed.R.id.rel_item;
import static com.hopeofseed.hopeofseed.R.id.tv_time;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<UserDataNoRealm> mList;
    Context mContext;
    private LayoutInflater inflater;
    public static HashMap<Integer, Boolean> isSelected;

    public UserListAdapter(Context context, List<UserDataNoRealm> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
        init();
    }

    // 初始化 设置所有checkbox都为未选择
    public void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < mList.size(); i++) {
            isSelected.put(i, false);
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final UserDataNoRealm itemData = mList.get(position);
        holder.tv_name.setText(itemData.getNickname());
        holder.tv_address.setText(itemData.getUserProvince() + " " + itemData.getUserCity() + " " + itemData.getUserZone());
        holder.ischeck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isSelected.put(position, true);
                    holder.ischeck.setChecked(true);
                } else {
                    holder.ischeck.setChecked(false);
                    isSelected.put(position, false);
                }
            }
        });
//        // 根据isSelected来设置checkbox的选中状况
        if (getIsSelected().containsKey(position)) {
            holder.ischeck.setChecked(getIsSelected().get(position));
        } else {
            isSelected.put(position, false);
        }
        getUserJpushInfo(holder, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_avatar, img_corner;
        TextView tv_name, tv_address, tv_user_role;
        CheckBox ischeck;

        public ViewHolder(View itemView) {
            super(itemView);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            img_corner = (ImageView) itemView.findViewById(R.id.img_corner);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_user_role = (TextView) itemView.findViewById(R.id.tv_user_role);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            ischeck = (CheckBox) itemView.findViewById(R.id.ischeck);
        }
    }

    private void getUserJpushInfo(ViewHolder holder, int position) {
        final UserDataNoRealm mData = ObjectUtil.cast(mList.get(position));
        ImageView img_corner = holder.img_corner;
        ImageView img_user = holder.img_user_avatar;
        final ImageView finalImg_corner = img_corner;
        final ImageView finalImg_user = img_user;
        JMessageClient.getUserInfo(Const.JPUSH_PREFIX + mData.getUser_id(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                //     Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                int user_role = Integer.parseInt(mData.getUser_role());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_user_default).centerCrop().into(finalImg_user);
                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 1:
                        Glide.with(mContext).load(R.drawable.corner_distributor).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_distributor_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 2:
                        Glide.with(mContext).load(R.drawable.corner_enterprise).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_enterprise_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 3:
                        Glide.with(mContext).load(R.drawable.corner_expert).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_expert_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 4:
                        Glide.with(mContext).load(R.drawable.corner_author).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.header_author_default).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                }

            }
        });
    }
}
