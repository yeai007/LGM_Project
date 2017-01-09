package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.HaveCommentNew;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.Commend2Data;
import com.hopeofseed.hopeofseed.JNXData.CommendData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class CommendListAdapter extends BaseAdapter {
    Context mContext;
    List<CommendData> mlist;
    CommendGalleryAdapter mCommendGalleryAdapter;
    ArrayList<Commend2Data> arrCommend2Data = new ArrayList<>();

    public CommendListAdapter(Context context, ArrayList<CommendData> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        final CommendData mData;
        mData = mlist.get(i);
        final ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.list_item_forward, null);
            viewHolder.user_name = (TextView) view.findViewById(R.id.user_name);
            viewHolder.send_time = (TextView) view.findViewById(R.id.send_time);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.img_user = (ImageView) view.findViewById(R.id.img_user);
            viewHolder.img_corner = (ImageView) view.findViewById(R.id.img_corner);
            viewHolder.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
            //设置布局管理器
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            viewHolder.resultRecyclerView.setLayoutManager(linearLayoutManager);
          //  arrCommend2Data = mData.getSecondcommend();
            //设置适配器
            mCommendGalleryAdapter = new CommendGalleryAdapter(arrCommend2Data);

            viewHolder.resultRecyclerView.setAdapter(mCommendGalleryAdapter);
            mCommendGalleryAdapter.setOnItemClickListener(new CommendGalleryAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Log.e(TAG, "onClick: " + position + arrCommend2Data.get(position).getCommendSecondComment());

                    ((HaveCommentNew) mContext).showInput(mData.getRecordId(), arrCommend2Data.get(position).getUser_id());
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.user_name.setText(mData.getNickname());
        viewHolder.tv_content.setText(mData.getComment());
        updateTime(mData.getRecordCreateTime(), viewHolder);
        getUserJpushInfo(Const.JPUSH_PREFIX + mData.getUser_id(), viewHolder, Integer.parseInt(mData.getUser_role()));
        return view;
    }

    class ViewHolder {
        TextView user_name, send_time, tv_content;
        ImageView img_user, img_corner;
        RecyclerView resultRecyclerView;
    }

    private void updateTime(String time, ViewHolder holder0) {
        Long[] longDiff = null;
        String NowTime = null;
        try {
            NowTime = DateTools.getNowTime();
            longDiff = DateTools.getDiffTime(NowTime, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String diffDay = String.valueOf(longDiff[1]);
        Log.e(TAG, "getView: diffDay:" + diffDay);
        String diffHour = String.valueOf(longDiff[2]);
        Log.e(TAG, "getView: diffHour:" + diffHour);
        String diffMinutes = String.valueOf(longDiff[3]);
        Log.e(TAG, "getView: diffHour:" + diffMinutes);
        if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) == 0) {
            if (Integer.parseInt(diffMinutes) < 5) {
                holder0.send_time.setText("刚刚");
            } else {
                holder0.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder0.send_time.setText(diffHour + "小时前");
        } else {
            holder0.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void getUserJpushInfo(String user_name, final ViewHolder holder0, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                //  Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default).placeholder(R.drawable.corner_user_default) .dontAnimate()
                                .centerCrop()
                                .into(holder0.img_corner);
/*                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_user_default).placeholder(R.drawable.no_have_img)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {*/
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_user_default)   .dontAnimate()
                                    .centerCrop()
                                    .into(holder0.img_user);
                     /*   }*/
                        break;
                    case 1:
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor).placeholder(R.drawable.corner_distributor) .dontAnimate()
                                .centerCrop()
                                .into(holder0.img_corner);
/*                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_distributor_default).placeholder(R.drawable.no_have_img)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {*/
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_distributor_default)  .dontAnimate()
                                    .centerCrop()
                                    .into(holder0.img_user);
                    /*    }*/
                        break;
                    case 2:
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise).placeholder(R.drawable.corner_enterprise).dontAnimate()
                                .centerCrop()
                                .into(holder0.img_corner);
/*                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_enterprise_default).placeholder(R.drawable.no_have_img)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {*/
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_enterprise_default).dontAnimate()
                                    .centerCrop()
                                    .into(holder0.img_user);
                     /*   }*/
                        break;
                    case 3:
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert).placeholder(R.drawable.corner_expert).dontAnimate()
                                .centerCrop()
                                .into(holder0.img_corner);
/*                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_expert_default) .placeholder(R.drawable.no_have_img)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {*/
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_expert_default) .dontAnimate()
                                    .centerCrop()
                                    .into(holder0.img_user);
                      /*  }*/
                        break;
                    case 4:
                        Glide.with(mContext)
                                .load(R.drawable.corner_author).placeholder(R.drawable.corner_author) .dontAnimate()
                                .centerCrop()
                                .into(holder0.img_corner);
/*                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_author_default).placeholder(R.drawable.no_have_img)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {*/
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile()).placeholder(R.drawable.header_author_default) .dontAnimate()
                                    .centerCrop()
                                    .into(holder0.img_user);
                     /*   }*/
                        break;
                }

            }
        });
    }
}
