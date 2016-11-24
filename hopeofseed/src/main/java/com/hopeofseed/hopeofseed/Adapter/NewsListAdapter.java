package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommentNew;
import com.hopeofseed.hopeofseed.Activitys.HaveCommentNew;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateZambia;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Activitys.ForwardNew;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import static com.hopeofseed.hopeofseed.R.id.rel_content;
import static com.hopeofseed.hopeofseed.R.id.tv_content;


/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 1:55
 * 修改人：whisper
 * 修改时间：2016/7/30 1:55
 * 修改备注：
 */
public class NewsListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    private static int CLASS_GENERAL = 0;
    private static int CLASS_FORWARD = 1;
    Context mContext;
    List<NewsData> mList;

    public NewsListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        //  Log.e(TAG, "getCount: "+mList.size());
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        int type_class = Integer.parseInt(mList.get(position).getNewclass());
        int result = 0;
        switch (type_class) {
            case 0:
                result = CLASS_GENERAL;
                break;
            case 8:
                result = CLASS_FORWARD;
                break;
            default:
                result = CLASS_GENERAL;
                break;
        }


        return result;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        //创建两种不同种类的viewholder0变量
        Viewholder0 holder0 = null;
        Viewholder1 holder1 = null;
        int type = getItemViewType(i);
        NewsData itemData;
        itemData = mList.get(i);
        String[] arrImage = mList.get(i).getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        if (view == null) {
            holder0 = new Viewholder0();
            holder1 = new Viewholder1();
            switch (type) {
                case 0:
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items, null);
                    holder0.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder0.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder0.tv_content = (TextView) view.findViewById(tv_content);
                    holder0.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder0.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder0.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder0.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder0.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder0.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder0.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder0.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder0.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder0.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());

                    if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder0.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder0.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder0.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_geneal, holder0);
                    break;
                case 1:
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_forwad, null);
                    holder1.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder1.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder1.tv_content = (TextView) view.findViewById(R.id.tv_content);
                    holder1.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    holder1.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder1.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder1.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder1.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder1.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder1.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder1.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder1.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder1.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder1.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                    Log.e(TAG, "getView: imagessizi" + images.size());

                    if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder1.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder1.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder1.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_forward, holder1);
                    break;
            }


        } else {
            /*holder0 = (Viewholder0) view.getTag();*/
            //根据不同的type来获得tag
            switch (type) {
                case 0:
                    holder0 = (Viewholder0) view.getTag(R.id.tag_geneal);
                    break;
                case 1:
                    holder1 = (Viewholder1) view.getTag(R.id.tag_forward);
                    break;
            }
        }
        //根据不同的type设置数据
        switch (type) {
            case 0:
                NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
                holder0.resultRecyclerView.setAdapter(gridAdapter);
                updateTime(itemData.getNewcreatetime(), holder0);

                holder0.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                if (itemData.getNickname().equals("")) {
                    holder0.user_name.setText(itemData.getNickname());
                } else {
                    holder0.user_name.setText(itemData.getNickname());
                }
                holder0.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder0.user_name.setTag(R.id.key_username, itemData.getUser_name());
                holder0.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                holder0.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                holder0.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                holder0.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                holder0.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                holder0.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder0.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder0.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder0.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder0.rel_zambia.setTag(R.id.key_listid, i);
                holder0.tv_content.setTag(R.id.key_content, itemData.getId());
                holder0.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder0.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (Integer.parseInt(itemData.getZambia_count()) > 0) {
                    holder0.tv_zambia.setText("赞" + itemData.getZambia_count().replaceAll("^(0+)", ""));
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder0, Integer.parseInt(itemData.getUser_role()));
                break;
            case 1:
                NewsImageAdapter gridAdapter1 = new NewsImageAdapter(mContext, images);
                holder1.resultRecyclerView.setAdapter(gridAdapter1);
                updateTime(itemData.getNewcreatetime(), holder1);
                holder1.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                if (itemData.getNickname().equals("")) {
                    holder1.user_name.setText(itemData.getNickname());
                } else {
                    holder1.user_name.setText(itemData.getNickname());
                }
                holder1.tv_title.setText(itemData.getTitle());
                holder1.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder1.user_name.setTag(R.id.key_username, itemData.getUser_name());
                holder1.rel_content = (RelativeLayout) view.findViewById(R.id.rel_content);
                holder1.rel_content.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                holder1.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder1.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder1.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder1.rel_content.setTag(R.id.key_forward_content, itemData.getFromid());
                holder1.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder1.rel_zambia.setTag(R.id.key_listid, i);
                holder1.tv_content.setTag(R.id.key_content, itemData.getId());
                holder1.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder1.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (Integer.parseInt(itemData.getZambia_count()) > 0) {
                    holder1.tv_zambia.setText("赞" + itemData.getZambia_count().replaceAll("^(0+)", ""));
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder1, Integer.parseInt(itemData.getUser_role()));
                break;
        }
        return view;
    }

    private void updateTime(String time, Viewholder0 holder0) {
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

    private void updateTime(String time, Viewholder1 holder0) {
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

    class Viewholder0 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }

    class Viewholder1 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_title, tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia, rel_content;
    }

    View.OnClickListener pullToListViewItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.rel_forward://转发
                    intent = new Intent(mContext.getApplicationContext(), ForwardNew.class);
                    intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_forward)));
                    mContext.startActivity(intent);
                    break;
                case R.id.rel_comment://评论
                    int comment_count = Integer.parseInt(String.valueOf(view.getTag(R.id.key_comment_count)));
                    if (comment_count > 0) {
                        intent = new Intent(mContext.getApplicationContext(), HaveCommentNew.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_comment)));
                        mContext.startActivity(intent);
                    } else {
                        intent = new Intent(mContext.getApplicationContext(), CommentNew.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_comment)));
                        mContext.startActivity(intent);
                    }

                    break;
                case R.id.rel_zambia:
                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_zambiaid)));
                    UpdateZambia(String.valueOf(view.getTag(R.id.key_zambiaid)));
                    break;
                case R.id.user_name:
                    intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                    intent.putExtra("userid", String.valueOf(view.getTag(R.id.key_userid)));
                    intent.putExtra("username", String.valueOf(view.getTag(R.id.key_username)));
                    mContext.startActivity(intent);
                    break;
                case tv_content:
                    Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_content)));
                    intent = new Intent(mContext.getApplicationContext(), NewsInfoActivity.class);

                    intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_content)));
                    mContext.startActivity(intent);
                    break;
                case rel_content:
                    Log.e(TAG, "onClick: rel_content" );
                    intent = new Intent(mContext.getApplicationContext(), ForwardNew.class);
                    intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_forward_content)));
                    mContext.startActivity(intent);
                    break;
            }
        }
    };

    public void UpdateZambia(final String position) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                UpdateZambia updateZambia = new UpdateZambia();
                updateZambia.UserId = String.valueOf(Const.currentUser.user_id);
                updateZambia.NewId = position;
                Boolean bRet = updateZambia.RunData();
                Message msg = UpdateZambiaHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler UpdateZambiaHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Log.e(TAG, "handleMessage: 1111");

                    break;
                case 1:
                    Log.e(TAG, "handleMessage: 222");
                    break;
                case 2:
                    Log.e(TAG, "handleMessage: 2333");
                    break;
            }
        }
    };

    private void getUserJpushInfo(String user_name, final Viewholder0 holder0, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
           //     Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_user_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 1:
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_distributor_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 2:
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_enterprise_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 3:
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_expert_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 4:
                        Glide.with(mContext)
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_author_default)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                }

            }
        });
    }

    private void getUserJpushInfo(String user_name, final Viewholder1 holder, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext)
                                .load(R.drawable.corner_user_default)
                                .centerCrop()
                                .into(holder.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_user_default)
                                    .centerCrop()
                                    .into(holder.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user);
                        }
                        break;
                    case 1:
                        Glide.with(mContext)
                                .load(R.drawable.corner_distributor)
                                .centerCrop()
                                .into(holder.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_distributor_default)
                                    .centerCrop()
                                    .into(holder.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user);
                        }
                        break;
                    case 2:
                        Glide.with(mContext)
                                .load(R.drawable.corner_enterprise)
                                .centerCrop()
                                .into(holder.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_enterprise_default)
                                    .centerCrop()
                                    .into(holder.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user);
                        }
                        break;
                    case 3:
                        Glide.with(mContext)
                                .load(R.drawable.corner_expert)
                                .centerCrop()
                                .into(holder.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_expert_default)
                                    .centerCrop()
                                    .into(holder.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user);
                        }
                        break;
                    case 4:
                        Glide.with(mContext)
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(holder.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.header_author_default)
                                    .centerCrop()
                                    .into(holder.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder.img_user);
                        }
                        break;
                }

            }
        });
    }
}
