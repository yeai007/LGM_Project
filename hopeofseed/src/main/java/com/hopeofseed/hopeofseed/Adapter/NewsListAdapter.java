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
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.HaveCommentNew;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoActivity;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.DataForHttp.UpdateZambia;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Activitys.ForwardNew;

import java.text.ParseException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import static com.hopeofseed.hopeofseed.R.id.img_share_new;
import static com.hopeofseed.hopeofseed.R.id.tv_content;
import static com.hopeofseed.hopeofseed.R.id.tv_share_new_content;
import static com.hopeofseed.hopeofseed.R.id.tv_share_new_title;
import static com.hopeofseed.hopeofseed.R.id.tv_title;


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
    private static int CLASS_EXPERISE = 2;
    private static int CLASS_SHARE_FIELD = 3;
    private static int CLASS_COMMODITY = 4;
    private static int CLASS_PROBLEM = 5;
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
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        int type_class = Integer.parseInt(mList.get(position).getNewclass());
        Log.e(TAG, "getItemViewType:type_class " + type_class);
        int result = 0;
        switch (type_class) {
            case 0:
                result = CLASS_GENERAL;
                break;
            case 8:
                result = CLASS_FORWARD;
                break;
            case 3:
                result = CLASS_EXPERISE;
                break;
            case 4:
                result = CLASS_SHARE_FIELD;
                break;
            case 6:
                result = CLASS_COMMODITY;
                break;
            case 5:
                result = CLASS_PROBLEM;
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
        Viewholder2 holder2 = null;
        Viewholder3 holder3 = null;
        Viewholder4 holder4 = null;
        Viewholder5 holder5 = null;
        int type = getItemViewType(i);
        NewsData itemData;
        itemData = mList.get(i);
        String[] arrImage = mList.get(i).getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        if (view == null) {
            holder0 = new Viewholder0();
            holder1 = new Viewholder1();
            holder2 = new Viewholder2();
            holder3 = new Viewholder3();
            holder4 = new Viewholder4();
            holder5 = new Viewholder5();
            Log.e(TAG, "getView: type" + type);
            switch (type) {
                case 0:
                    Log.e(TAG, "getView: 0");
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
                    holder0.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder0.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder0.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder0.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder0.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                    holder0.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder0.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());
                    Log.e(TAG, "getView: " + itemData.getAssimgurl() + "---" + images.size());
                    if (images.size() == 0) {
                        holder0.resultRecyclerView.setVisibility(View.GONE);
                    } else if (images.size() == 1) {
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
                    Log.e(TAG, "getView: 1");
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_forwad, null);

                    holder1.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder1.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder1.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    holder1.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder1.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder1.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder1.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);

                    holder1.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder1.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder1.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder1.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder1.img_share_new = (ImageView) view.findViewById(img_share_new);
                    holder1.rel_share_new = (RelativeLayout) view.findViewById(R.id.rel_share_new);
                    holder1.tv_share_new_title = (TextView) view.findViewById(tv_share_new_title);
                    holder1.tv_share_new_content = (TextView) view.findViewById(tv_share_new_content);
                    holder1.rel_share_new.setOnClickListener(pullToListViewItemOnClickListener);
                    holder1.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder1.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder1.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder1.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder1.tv_title.setOnClickListener(pullToListViewItemOnClickListener);
                    view.setTag(R.id.tag_forward, holder1);
                    break;
                case 2:
                    Log.e(TAG, "getView: 2");
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_exprise, null);
                    holder2.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder2.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder2.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    holder2.tv_content = (TextView) view.findViewById(R.id.tv_content);
                    holder2.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder2.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder2.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder2.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder2.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder2.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder2.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder2.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder2.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder2.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder2.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder2.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder2.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                    holder2.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder2.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());
                    if (images.size() == 0) {
                        holder2.resultRecyclerView.setVisibility(View.GONE);
                    } else if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder2.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder2.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder2.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_experise, holder2);
                    break;
                case 3:
                    Log.e(TAG, "getView: 3");
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_share_field, null);
                    holder3.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder3.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder3.tv_content = (TextView) view.findViewById(tv_content);
                    holder3.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder3.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder3.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder3.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder3.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder3.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder3.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder3.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder3.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder3.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder3.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder3.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder3.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                    holder3.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder3.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());
                    if (images.size() == 0) {
                        holder3.resultRecyclerView.setVisibility(View.GONE);
                    } else if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder3.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder3.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder3.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_share_field, holder3);
                    break;
                case 4:
                    Log.e(TAG, "getView: 3");
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_commodity, null);
                    holder4.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder4.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder4.tv_content = (TextView) view.findViewById(tv_content);
                    holder4.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder4.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder4.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder4.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder4.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder4.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder4.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder4.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder4.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder4.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder4.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder4.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder4.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                    holder4.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder4.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());
                    if (images.size() == 0) {
                        holder4.resultRecyclerView.setVisibility(View.GONE);
                    } else if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder4.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder4.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder4.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_commodity, holder4);
                    break;

                case 5:
                    Log.e(TAG, "getView: 5");
                    view = _LayoutInflater
                            .inflate(R.layout.newlist_items_problem, null);
                    holder5.img_user = (ImageView) view.findViewById(R.id.img_user_avatar);
                    holder5.img_corner = (ImageView) view.findViewById(R.id.img_corner);
                    holder5.tv_content = (TextView) view.findViewById(tv_content);
                    holder5.tv_zambia = (TextView) view.findViewById(R.id.tv_zambia);
                    holder5.tv_title = (TextView) view.findViewById(R.id.tv_title);
                    holder5.rel_forward = (RelativeLayout) view.findViewById(R.id.rel_forward);
                    holder5.rel_comment = (RelativeLayout) view.findViewById(R.id.rel_comment);
                    holder5.rel_zambia = (RelativeLayout) view.findViewById(R.id.rel_zambia);
                    holder5.send_time = (TextView) view.findViewById(R.id.send_time);
                    holder5.user_name = (TextView) view.findViewById(R.id.user_name);
                    holder5.tv_forward = (TextView) view.findViewById(R.id.tv_forward);
                    holder5.tv_comment = (TextView) view.findViewById(R.id.tv_comment);
                    holder5.rel_comment.setOnClickListener(pullToListViewItemOnClickListener);
                    holder5.rel_forward.setOnClickListener(pullToListViewItemOnClickListener);
                    holder5.rel_zambia.setOnClickListener(pullToListViewItemOnClickListener);
                    holder5.user_name.setOnClickListener(pullToListViewItemOnClickListener);
                    holder5.tv_content.setOnClickListener(pullToListViewItemOnClickListener);
                    holder5.resultRecyclerView = (RecyclerView) view.findViewById(R.id.result_recycler);
                    holder5.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));

                    Log.e(TAG, "getView: imagessizi" + images.size());
                    if (images.size() == 0) {
                        holder5.resultRecyclerView.setVisibility(View.GONE);
                    } else if (images.size() == 1) {
                        if (TextUtils.isEmpty(images.get(0))) {
                            holder5.resultRecyclerView.setVisibility(View.GONE);
                        } else {
                            holder5.resultRecyclerView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        holder5.resultRecyclerView.setVisibility(View.VISIBLE);
                    }
                    view.setTag(R.id.tag_problem, holder5);
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
                case 2:
                    holder2 = (Viewholder2) view.getTag(R.id.tag_experise);
                    break;
                case 3:
                    holder3 = (Viewholder3) view.getTag(R.id.tag_share_field);
                    break;
                case 4:
                    holder4 = (Viewholder4) view.getTag(R.id.tag_commodity);

                case 5:
                    holder5 = (Viewholder5) view.getTag(R.id.tag_problem);
            }
        }
        //根据不同的type设置数据
        switch (type) {
            case 0:
                NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
                holder0.resultRecyclerView.setAdapter(gridAdapter);
                updateTime(itemData.getNewcreatetime(), holder0);

                holder0.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                holder0.tv_content.setSingleLine(false);
                holder0.tv_content.setMaxLines(3);
                holder0.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                if (itemData.getNickname().equals("")) {
                    holder0.user_name.setText(itemData.getNickname());
                } else {
                    holder0.user_name.setText(itemData.getNickname());
                }
                holder0.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder0.user_name.setTag(R.id.key_username, itemData.getUser_name());

                holder0.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder0.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder0.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder0.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder0.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder0.rel_zambia.setTag(R.id.key_listid, i);
                holder0.tv_content.setTag(R.id.key_content, itemData.getId());
                holder0.tv_content.setTag(R.id.key_content_new_class, itemData.getNewclass());
                holder0.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder0.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount() > 0) {
                    holder0.tv_zambia.setText("赞" + String.valueOf(itemData.getZambiaCount()));
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder0, Integer.parseInt(itemData.getUser_role()));
                break;
            case 1:
                String[] arrImageForward = itemData.getAssimgurl().split(";");
                List<String> arrImageForward1 = java.util.Arrays.asList(arrImageForward);
                if (images.size() > 0) {
                    Glide.with(mContext)
                            .load(Const.IMG_URL + arrImageForward1.get(0))
                            .centerCrop()
                            .into(holder1.img_share_new);
                } else {
                }
                updateTime(itemData.getNewcreatetime(), holder1);
                if (itemData.getNickname().equals("")) {
                    holder1.user_name.setText(itemData.getNickname());
                } else {
                    holder1.user_name.setText(itemData.getNickname());
                }
                holder1.tv_title.setText(itemData.getForwardComment());
                holder1.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder1.user_name.setTag(R.id.key_username, itemData.getUser_name());
                holder1.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder1.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder1.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder1.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder1.rel_share_new.setTag(R.id.key_forward_content, itemData.getFromid());
                holder1.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder1.rel_zambia.setTag(R.id.key_listid, i);
                holder1.tv_share_new_title.setText(itemData.getTitle());

                if (itemData.getTitle().equals(itemData.getContent())) {
                    holder1.tv_share_new_content.setVisibility(View.GONE);
                } else {
                    holder1.tv_share_new_content.setText(itemData.getContent());

                }

                holder1.tv_title.setTag(R.id.key_title, itemData.getId());
                holder1.tv_title.setTag(R.id.key_title_new_class, itemData.getNewclass());

                holder1.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder1.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount() > 0) {
                    holder1.tv_zambia.setText("赞" + String.valueOf(itemData.getZambiaCount()));
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder1, Integer.parseInt(itemData.getUser_role()));
                break;
            case 2:
                NewsImageAdapter gridAdapter2 = new NewsImageAdapter(mContext, images);
                holder2.resultRecyclerView.setAdapter(gridAdapter2);
                updateTime(itemData.getNewcreatetime(), holder2);
                holder2.tv_title.setText(itemData.getTitle());
                holder2.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                holder2.tv_content.setSingleLine(false);
                holder2.tv_content.setMaxLines(3);
                holder2.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                if (itemData.getNickname().equals("")) {
                    holder2.user_name.setText(itemData.getNickname());
                } else {
                    holder2.user_name.setText(itemData.getNickname());
                }
                holder2.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder2.user_name.setTag(R.id.key_username, itemData.getUser_name());

                holder2.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder2.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder2.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder2.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder2.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder2.rel_zambia.setTag(R.id.key_listid, i);
                holder2.tv_content.setTag(R.id.key_content, itemData.getId());
                holder2.tv_title.setTag(R.id.key_title, itemData.getId());
                holder2.tv_content.setTag(R.id.key_content_new_class, itemData.getNewclass());
                holder2.tv_title.setTag(R.id.key_title_new_class, itemData.getNewclass());
                holder2.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder2.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount()> 0) {
                    holder2.tv_zambia.setText("赞" + itemData.getZambiaCount());
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder2, Integer.parseInt(itemData.getUser_role()));
                break;
            case 3:
                NewsImageAdapter gridAdapter3 = new NewsImageAdapter(mContext, images);
                holder3.resultRecyclerView.setAdapter(gridAdapter3);
                updateTime(itemData.getNewcreatetime(), holder3);

                holder3.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                holder3.tv_content.setSingleLine(false);
                holder3.tv_content.setMaxLines(3);
                holder3.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                if (itemData.getNickname().equals("")) {
                    holder3.user_name.setText(itemData.getNickname());
                } else {
                    holder3.user_name.setText(itemData.getNickname());
                }
                holder3.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder3.user_name.setTag(R.id.key_username, itemData.getUser_name());

                holder3.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder3.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder3.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder3.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder3.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder3.rel_zambia.setTag(R.id.key_listid, i);
                holder3.tv_content.setTag(R.id.key_content, itemData.getId());
                holder3.tv_content.setTag(R.id.key_content_new_class, itemData.getNewclass());
                holder3.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder3.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount() > 0) {
                    holder3.tv_zambia.setText("赞" + itemData.getZambiaCount());
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder3, Integer.parseInt(itemData.getUser_role()));
                break;
            case 4:
                NewsImageAdapter gridAdapter4 = new NewsImageAdapter(mContext, images);
                holder4.resultRecyclerView.setAdapter(gridAdapter4);
                updateTime(itemData.getNewcreatetime(), holder4);

                holder4.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                holder4.tv_content.setSingleLine(false);
                holder4.tv_content.setMaxLines(3);
                holder4.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                if (itemData.getNickname().equals("")) {
                    holder4.user_name.setText(itemData.getNickname());
                } else {
                    holder4.user_name.setText(itemData.getNickname());
                }
                holder4.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder4.user_name.setTag(R.id.key_username, itemData.getUser_name());

                holder4.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder4.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder4.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder4.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder4.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder4.rel_zambia.setTag(R.id.key_listid, i);
                holder4.tv_content.setTag(R.id.key_content, itemData.getInfoid());
                holder4.tv_content.setTag(R.id.key_content_new_class, itemData.getNewclass());
                holder4.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder4.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount()> 0) {
                    holder4.tv_zambia.setText("赞" + itemData.getZambiaCount());
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder4, Integer.parseInt(itemData.getUser_role()));
                break;
            case 5:
                NewsImageAdapter gridAdapter5 = new NewsImageAdapter(mContext, images);
                holder5.resultRecyclerView.setAdapter(gridAdapter5);
                updateTime(itemData.getNewcreatetime(), holder5);
                holder5.tv_title.setText(itemData.getTitle().replace("\\n", "\n"));
                holder5.tv_content.setText(itemData.getContent().replace("\\n", "\n"));
                holder5.tv_content.setSingleLine(false);
                holder5.tv_content.setMaxLines(3);
                holder5.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
                if (itemData.getNickname().equals("")) {
                    holder5.user_name.setText(itemData.getNickname());
                } else {
                    holder5.user_name.setText(itemData.getNickname());
                }
                holder5.user_name.setTag(R.id.key_userid, itemData.getUser_id());
                holder5.user_name.setTag(R.id.key_username, itemData.getUser_name());

                holder5.rel_zambia.setTag(R.id.key_zambiaid, itemData.getId());
                holder5.rel_forward.setTag(R.id.key_forward, itemData.getId());
                holder5.rel_comment.setTag(R.id.key_comment, itemData.getId());
                holder5.rel_comment.setTag(R.id.key_comment_count, itemData.getCommentCount());
                holder5.rel_comment.setTag(R.id.key_comment_new_class, itemData.getNewclass());
                holder5.rel_zambia.setTag(R.id.key_listid, i);
                holder5.tv_content.setTag(R.id.key_content, itemData.getId());
                holder5.tv_title.setTag(R.id.key_content, itemData.getId());
                holder5.tv_content.setTag(R.id.key_content_new_class, itemData.getNewclass());
                holder5.tv_title.setTag(R.id.key_title_new_class, itemData.getNewclass());
                holder5.tv_forward.setText(Integer.parseInt(itemData.getForwardCount()) > 0 ? itemData.getForwardCount() : "转发");
                holder5.tv_comment.setText(Integer.parseInt(itemData.getCommentCount()) > 0 ? itemData.getCommentCount() : "评论");
                if (itemData.getZambiaCount() > 0) {
                    holder5.tv_zambia.setText("赞" + itemData.getZambiaCount());
                }
                getUserJpushInfo(Const.JPUSH_PREFIX + itemData.getUser_id(), holder5, Integer.parseInt(itemData.getUser_role()));
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

    private void updateTime(String time, Viewholder1 holder) {
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void updateTime(String time, Viewholder2 holder) {
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void updateTime(String time, Viewholder3 holder) {
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void updateTime(String time, Viewholder4 holder) {
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    private void updateTime(String time, Viewholder5 holder) {
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
                holder.send_time.setText("刚刚");
            } else {
                holder.send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.send_time.setText(diffHour + "小时前");
        } else {
            holder.send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }

    class Viewholder0 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }

    class Viewholder1 {
        ImageView img_user, img_corner, img_share_new;
        TextView tv_title, tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time, tv_share_new_title, tv_share_new_content;
        RelativeLayout rel_forward, rel_comment, rel_zambia, rel_share_new;
    }

    class Viewholder2 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time, tv_title;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }

    class Viewholder3 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }

    class Viewholder4 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
    }

    class Viewholder5 {
        RecyclerView resultRecyclerView;
        ImageView img_user, img_corner;
        TextView tv_title, tv_content, tv_zambia, user_name, tv_forward, tv_comment, send_time;
        RelativeLayout rel_forward, rel_comment, rel_zambia;
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
                        intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_comment)));
                        intent.putExtra("NewClass", Integer.valueOf(String.valueOf(view.getTag(R.id.key_comment_new_class))));
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

                case R.id.tv_title:
                 /*   if (Integer.valueOf(String.valueOf(view.getTag(R.id.key_title_new_class))) == 8) {
                        intent = new Intent(mContext.getApplicationContext(), HaveCommentNew.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_title)));
                        mContext.startActivity(intent);
                    } else {*/
                    intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                    intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_title)));
                    intent.putExtra("NewClass", Integer.valueOf(String.valueOf(view.getTag(R.id.key_title_new_class))));
                    mContext.startActivity(intent);
                     /*   intent = new Intent(mContext.getApplicationContext(), NewsInfoActivity.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_title)));
                        intent.putExtra("NewClass", Integer.valueOf(String.valueOf(view.getTag(R.id.key_title_new_class))));
                        mContext.startActivity(intent);*/
                  /*  }*/

                    break;
                case R.id.tv_content:
                    if (Integer.valueOf(String.valueOf(view.getTag(R.id.key_content_new_class))) == 6) {


                        intent = new Intent(mContext.getApplicationContext(), CommodityActivity.class);
                        intent.putExtra("CommodityId", String.valueOf(view.getTag(R.id.key_content)));
                        mContext.startActivity(intent);
                    } else if (Integer.valueOf(String.valueOf(view.getTag(R.id.key_content_new_class))) == 0) {
                        intent = new Intent(mContext.getApplicationContext(), HaveCommentNew.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_content)));
                        mContext.startActivity(intent);
                    } else {
                        Log.e(TAG, "onClick: " + String.valueOf(view.getTag(R.id.key_content_new_class)));
/*                        intent = new Intent(mContext.getApplicationContext(), NewsInfoActivity.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_content)));
                        intent.putExtra("NewClass", Integer.valueOf(String.valueOf(view.getTag(R.id.key_content_new_class))));
                        mContext.startActivity(intent);*/


                        intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                        intent.putExtra("NEWID", String.valueOf(view.getTag(R.id.key_content)));
                        intent.putExtra("NewClass", Integer.valueOf(String.valueOf(view.getTag(R.id.key_content_new_class))));
                        mContext.startActivity(intent);

                    }
                    break;
                case R.id.rel_share_new:
                    intent = new Intent(mContext.getApplicationContext(), HaveCommentNew.class);
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
                holder0.img_corner.setVisibility(View.VISIBLE);
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
                    case 5:
                        holder0.img_corner.setVisibility(View.GONE);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.user_media)
                                    .centerCrop()
                                    .into(holder0.img_user);

                        } else {
                            Glide.with(mContext)
                                    .load(userInfo.getAvatarFile())
                                    .centerCrop()
                                    .into(holder0.img_user);
                        }
                        break;
                    case 6:
                        holder0.img_corner.setVisibility(View.GONE);
                        Glide.with(mContext)
                                .load(R.drawable.corner_author)
                                .centerCrop()
                                .into(holder0.img_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext)
                                    .load(R.drawable.user_system)
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

    private void getUserJpushInfo(String user_name, final Viewholder2 holder0, final int user_role) {
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

    private void getUserJpushInfo(String user_name, final Viewholder3 holder, final int user_role) {
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

    private void getUserJpushInfo(String user_name, final Viewholder4 holder, final int user_role) {
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

    private void getUserJpushInfo(String user_name, final Viewholder5 holder, final int user_role) {
        JMessageClient.getUserInfo(user_name, new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {
                //  Log.i("CreateGroupTextMsgActivity", "JMessageClient.createGroupTextMessage" + ", responseCode = " + i + " ; LoginDesc = " + s);
                //     Log.e(TAG, "gotResult: " + userInfo.getUserName() + userInfo.getNickname());
                switch (user_role) {
                    case 0:
                        Glide.with(mContext).load(R.drawable.corner_user_default).centerCrop().into(holder.img_corner);
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
