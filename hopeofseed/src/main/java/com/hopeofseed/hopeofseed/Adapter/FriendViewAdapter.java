package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FriendData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/27 9:53
 * 修改人：whisper
 * 修改时间：2016/12/27 9:53
 * 修改备注：
 */
public class FriendViewAdapter extends RecyclerView.Adapter<FriendViewAdapter.ViewHolder> implements NetCallBack {
    Context mContext;
    List<FriendData> mlist;
    int modity_position = 0;
    Handler mHandler = new Handler(Looper.getMainLooper());
    int IsFriend = 0;
    int ChechResult = 0;

    public FriendViewAdapter(Context context, ArrayList<FriendData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.friend_items, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final FriendData itemData = mlist.get(position);
        holder.tv_name.setText(itemData.getNickname());
        holder.tv_address.setText(itemData.getUserProvince() + "  " + itemData.getUserCity() + "  " + itemData.getUserZone());

        getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder, Integer.parseInt(itemData.getUser_role()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", itemData.getUser_id_1());
                intent.putExtra("UserRole", Integer.parseInt(itemData.getUser_role()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        final int isFriend = Integer.parseInt(itemData.getIsFriend());
        String alert = "";
        if (isFriend == 3) {
            holder.modify_friend.setText("互相关注");
            alert = "确认取消关注？";
        }
        if (isFriend == 2) {
            holder.modify_friend.setText("已关注");
            alert = "确认取消关注？";
        }
        if (isFriend == 1) {
            holder.modify_friend.setText("+关注");
        }
        if (isFriend == 0) {
            holder.modify_friend.setText("+关注");
        }
        if (Integer.parseInt(itemData.getIsRead()) == 0) {
            holder.img_is_read.setVisibility(View.VISIBLE);
        } else {
            holder.img_is_read.setVisibility(View.GONE);
        }
        final String dialog_alert = alert;
        holder.modify_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFriend == 2 || isFriend == 3) {
                    iosDialog mIosDialog = new iosDialog.Builder(mContext)
                            .setTitle("种愿")
                            .setMessage(dialog_alert)
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modity_position = position;
                                    IsFriend = isFriend;
                                    AddOrDelFollowed(String.valueOf(Const.currentUser.user_id), itemData.getUser_id_1(), isFriend);

                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("种愿").create();
                    mIosDialog.show();
                } else {
                    modity_position = position;
                    IsFriend = isFriend;
                    AddOrDelFollowed(String.valueOf(Const.currentUser.user_id), itemData.getUser_id_1(), isFriend);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("AddNewFriend")) {
            mHandler.post(checkResult);
        } else if (rspBaseBean.RequestSign.equals("GetIsFriend")) {
            CommResultTmp mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            Log.e(TAG, "onSuccess: " + rspBaseBean.resultNote + mCommResultTmp.getDetail());
            ChechResult = Integer.valueOf(mCommResultTmp.getDetail());
            mHandler.post(updateThisItem);
        }
    }

    @Override
    public void onError(String error) {
        FriendData mData = new FriendData();
        mData = mlist.get(modity_position);
        getIsFriend(String.valueOf(Const.currentUser.user_id), mData.getUser_id());
    }

    @Override
    public void onFail() {

    }

    Runnable checkResult = new Runnable() {
        @Override
        public void run() {
            FriendData mData = new FriendData();
            mData = mlist.get(modity_position);
            getIsFriend(String.valueOf(Const.currentUser.user_id), mData.getUser_id());
            // notifyItemChanged(modity_position, mData);
        }
    };
    Runnable updateThisItem = new Runnable() {
        @Override
        public void run() {
            FriendData mData = new FriendData();
            mData = mlist.get(modity_position);
            switch (ChechResult) {
                case 0://双方未关注
                    mData.setIsFriend("0");
                    break;
                case 1://当前关注用户，用户未关注当前页帐号
                    mData.setIsFriend("1");
                    break;
                case 2://用户关注当前页帐号
                    mData.setIsFriend("2");
                    break;

                case 3://双向关注
                    mData.setIsFriend("3");
                    break;
            }
            notifyItemChanged(modity_position, mData);
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout item_view;
        ImageView img_user_avatar, img_corner, img_is_read;
        TextView tv_user_role, tv_name, tv_address;
        Button modify_friend;

        public ViewHolder(View itemView) {
            super(itemView);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            img_corner = (ImageView) itemView.findViewById(R.id.img_corner);
            tv_user_role = (TextView) itemView.findViewById(R.id.tv_user_role);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            modify_friend = (Button) itemView.findViewById(R.id.modify_friend);
            img_is_read = (ImageView) itemView.findViewById(R.id.img_is_read);
        }
    }

    private void getIsFriend(String UserId, String AddUserId) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        opt_map.put("AddUserId", AddUserId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetIsFriend.php", opt_map, CommResultTmp.class, this);
    }

    private void AddOrDelFollowed(String UserId, String AddUserId, int isAddOrDel) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", UserId);
        opt_map.put("AddUserId", AddUserId);
        if (isAddOrDel == 3) {
            opt_map.put("isAddOrDel", String.valueOf(0));
        } else if (isAddOrDel == 2) {
            opt_map.put("isAddOrDel", String.valueOf(0));
        } else if (isAddOrDel == 1) {
            opt_map.put("isAddOrDel", String.valueOf(1));
        } else if (isAddOrDel == 0) {
            opt_map.put("isAddOrDel", String.valueOf(1));
        }
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "AddNewFriend.php", opt_map, CommResultTmp.class, this);
    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                holder.img_corner.setVisibility(View.GONE);
                switch (user_role) {
                    case 0:
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
                    case 6:
                        holder.tv_user_role.setText("【媒体】");
                        holder.img_corner.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(R.drawable.user_media)
                                .centerCrop()
                                .into(holder.img_user_avatar);

                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.user_media)
                                    .centerCrop()
                                    .into(holder.img_user_avatar);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user_avatar);
                        }
                        break;
                    case 5:
                        holder.tv_user_role.setText("【管理员】");
                        holder.img_corner.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(R.drawable.user_system)
                                .centerCrop()
                                .into(holder.img_user_avatar);

                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.user_system)
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
