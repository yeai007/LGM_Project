package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXData.UpdateZiabamResult;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

import java.text.ParseException;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.GetUserInfoCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/2 16:42
 * 修改人：whisper
 * 修改时间：2016/12/2 16:42
 * 修改备注：
 */
public class RecyclerViewAdapter<T> extends RecyclerView.Adapter implements View.OnClickListener {
    private static final String TAG = "RecyclerViewAdapter";
    Context mContext;
    private List<T> mList;//数据集合
    private int itemLayout;//item的布局
    private static final int TYPE_ITEM = 10;
    private static final int TYPE_FOOTER = 11;
    private static final int CLASS_GENERAL = 0;//一般信息
    private static final int CLASS_IMAGE = 1;//图片信息
    private static final int CLASS_VADIO = 2;//视频信息
    private static final int CLASS_EXPERISE = 3;//农技经验
    private static final int CLASS_YIELD = 4;//分享产量
    private static final int CLASS_PROBLEM = 5;//发问
    private static final int CLASS_COMMODITY = 6;//商品
    private static final int CLASS_HUODONG = 7;//活动
    private static final int CLASS_FORWARD = 8;//转发

    private boolean isNeedMore;//是否需要加载更多的view

    public boolean isNeedMore() {
        return isNeedMore;
    }

    public void setIsNeedMore(boolean isNeedMore) {
        this.isNeedMore = isNeedMore;
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    UpdateZiabamResult mUpdateZiabamResult = new UpdateZiabamResult();

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (NewsData) v.getTag());
        }
    }

    //define interface
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, NewsData data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecyclerViewAdapter(Context context, List<T> list, boolean isNeedMore) {
        super();

        mContext = context;
        mList = list;
        this.isNeedMore = isNeedMore;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //返回item的布局的holder
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case CLASS_GENERAL:
                View v0 = inflater.inflate(R.layout.newlist_items, parent, false);
                viewHolder = new ViewHolder0(v0);
                v0.setOnClickListener(this);
                break;
            case CLASS_IMAGE:
                View v1 = inflater.inflate(R.layout.newlist_items, parent, false);
                viewHolder = new ViewHolder1(v1);
                v1.setOnClickListener(this);
                break;
            case CLASS_VADIO:
                View v2 = inflater.inflate(R.layout.newlist_items, parent, false);
                viewHolder = new ViewHolder2(v2);
                v2.setOnClickListener(this);
                break;
            case CLASS_EXPERISE:
                View v3 = inflater.inflate(R.layout.newlist_items_exprise, parent, false);
                viewHolder = new ViewHolder3(v3);

                v3.setOnClickListener(this);
                break;
            case CLASS_YIELD:
                View v4 = inflater.inflate(R.layout.newlist_items_share_field, parent, false);
                viewHolder = new ViewHolder4(v4);
                v4.setOnClickListener(this);
                break;
            case CLASS_PROBLEM:
                View v5 = inflater.inflate(R.layout.newlist_items_problem, parent, false);
                viewHolder = new ViewHolder5(v5);
                v5.setOnClickListener(this);
                break;
            case CLASS_COMMODITY:
                View v6 = inflater.inflate(R.layout.newlist_items_commodity, parent, false);
                viewHolder = new ViewHolder6(v6);
                v6.setOnClickListener(this);
                break;
            case CLASS_HUODONG:
                View v7 = inflater.inflate(R.layout.newlist_items_huodong, parent, false);
                viewHolder = new ViewHolder7(v7);
                v7.setOnClickListener(this);
                break;
            case CLASS_FORWARD:
                View v8 = inflater.inflate(R.layout.newlist_items_forwad, parent, false);
                viewHolder = new ViewHolder8(v8);
                v8.setOnClickListener(this);
                break;
            case TYPE_FOOTER:
                View v11 = inflater.inflate(R.layout.recycle_foot_view, parent, false);
                viewHolder = new FooterViewHolder(v11);
                break;
            default:
                View v = inflater.inflate(R.layout.newlist_items, parent, false);
                viewHolder = new ViewHolder0(v);
                v.setOnClickListener(this);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder0) {
            initData((ViewHolder0) holder, position);
        } else if (holder instanceof ViewHolder1) {
            initData((ViewHolder1) holder, position);
        } else if (holder instanceof ViewHolder2) {
            initData((ViewHolder2) holder, position);
        } else if (holder instanceof ViewHolder3) {
            initData((ViewHolder3) holder, position);
        } else if (holder instanceof ViewHolder4) {
            initData((ViewHolder4) holder, position);
        } else if (holder instanceof ViewHolder5) {
            initData((ViewHolder5) holder, position);
        } else if (holder instanceof ViewHolder6) {
            initData((ViewHolder6) holder, position);
        } else if (holder instanceof ViewHolder7) {
            initData((ViewHolder7) holder, position);
        } else if (holder instanceof ViewHolder8) {
            initData((ViewHolder8) holder, position);
        } else if (holder instanceof FooterViewHolder) {

        }
        if (holder instanceof FooterViewHolder) {
        } else {
            getUserJpushInfo(holder, position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (!isNeedMore) {
            return super.getItemViewType(position);
        }
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            NewsData item = ObjectUtil.cast(mList.get(position));
            return Integer.parseInt((item.getNewclass()));
        }


    }

    @Override
    public int getItemCount() {
        if (isNeedMore) {
            return mList.size() == 0 ? 0 : mList.size() + 1;
        } else {
            return mList.size();
        }

    }

    /**
     * 普通信息
     */
    protected void initData(ViewHolder0 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }


        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        getUserJpushInfo(holder,position);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 图片信息
     */
    protected void initData(ViewHolder1 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 视频信息
     */
    protected void initData(ViewHolder2 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 农技经验
     */
    protected void initData(ViewHolder3 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_title)).setText("【农技】" + "【" + mData.getTitle() + "】");
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 分享产量
     */
    protected void initData(ViewHolder4 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_title)).setText("【产量表现】" + "【" + mData.getTitle() + "】");
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 发问
     */
    protected void initData(ViewHolder5 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_title)).setText("【问题】" + "【" + mData.getTitle() + "】");
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 商品
     */
    protected void initData(ViewHolder6 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_title)).setText("【商品】" + "【" + mData.getTitle() + "】");
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 活动
     */
    protected void initData(ViewHolder7 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        holder.itemView.setTag(mData);
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_title)).setText("【活动】" + "【" + mData.getTitle() + "】");
        TextView tv_content = ((TextView) holder.getView(R.id.tv_content));
        tv_content.setText(mData.getContent().replace("\\n", "\n"));
        tv_content.setSingleLine(false);
        tv_content.setMaxLines(3);
        tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        holder.resultRecyclerView = holder.getView(R.id.result_recycler);
        holder.resultRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        if (images.size() == 0) {
            holder.resultRecyclerView.setVisibility(View.GONE);
        } else if (images.size() == 1) {
            if (TextUtils.isEmpty(images.get(0))) {
                holder.resultRecyclerView.setVisibility(View.GONE);
            } else {
                holder.resultRecyclerView.setVisibility(View.VISIBLE);
            }
        } else {
            holder.resultRecyclerView.setVisibility(View.VISIBLE);
        }
        NewsImageAdapter gridAdapter = new NewsImageAdapter(mContext, images);
        holder.resultRecyclerView.setAdapter(gridAdapter);
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_content).setTag(mData);
        holder.getView(R.id.rel_content).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************
    }

    /**
     * 转发
     */
    protected void initData(ViewHolder8 holder, int position) {
        NewsData mData = ObjectUtil.cast(mList.get(position));
        String[] arrImage = mData.getAssimgurl().split(";");
        List<String> images = java.util.Arrays.asList(arrImage);
        if (images.size() > 0) {
            Glide.with(mContext)
                    .load(Const.IMG_URL + images.get(0))
                    .centerCrop()
                    .into(((ImageView) holder.getView(R.id.img_share_new)));
        } else {
        }
        ((TextView) holder.getView(R.id.tv_title)).setText("【转发】" + mData.getForwardComment());
        ((TextView) holder.getView(R.id.tv_share_new_title)).setText(mData.getTitle());
        if (mData.getTitle().equals(mData.getContent())) {
            ((TextView) holder.getView(R.id.tv_share_new_content)).setVisibility(View.GONE);
        } else {
            ((TextView) holder.getView(R.id.tv_share_new_content)).setText(mData.getContent());

        }
        ((TextView) holder.getView(R.id.user_name)).setText(mData.getNickname());
        ((TextView) holder.getView(R.id.tv_forward)).setText(Integer.parseInt(mData.getForwardCount()) > 0 ? mData.getForwardCount() : "转发");
        ((TextView) holder.getView(R.id.tv_comment)).setText(Integer.parseInt(mData.getCommentCount()) > 0 ? mData.getCommentCount() : "评论");
        Log.e(TAG, "initData: " + mData.getZambiaCount());
        ((TextView) holder.getView(R.id.tv_zambia)).setText(mData.getZambiaCount() > 0 ? String.valueOf(mData.getZambiaCount()) : "赞");
        Glide.with(mContext)
                .load(mData.getZambiaCount() > 0 ? R.drawable.zambia_hava_img : R.drawable.zambia_img)
                .centerCrop()
                .into(((ImageView) holder.getView(R.id.img_zambia)));
        updateTime(mData.getNewcreatetime(), holder);
        //*************************OnclickBegin*******************************************************
        holder.getView(R.id.rel_forward).setTag(mData);
        holder.getView(R.id.rel_forward).setOnClickListener(this);
        holder.getView(R.id.rel_comment).setTag(mData);
        holder.getView(R.id.rel_comment).setOnClickListener(this);
        holder.getView(R.id.rel_zambia).setTag(mData);
        holder.getView(R.id.rel_zambia).setOnClickListener(this);
        holder.getView(R.id.rel_share_new).setTag(mData);
        holder.getView(R.id.rel_share_new).setOnClickListener(this);
        holder.getView(R.id.user_name).setTag(mData);
        holder.getView(R.id.user_name).setOnClickListener(this);
        holder.getView(R.id.rel_img_user_avatar).setTag(mData);
        holder.getView(R.id.rel_img_user_avatar).setOnClickListener(this);
        //*****************************OnclickEnd****************************************************

    }

    public static class ViewHolder0 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder0(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;

            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }


        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder1(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder2(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder3 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder3(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder4 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder4(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder5 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder5(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder6 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder6(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder7 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;
        RecyclerView resultRecyclerView;

        public ViewHolder7(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class ViewHolder8 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;

        public ViewHolder8(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    private void getUserJpushInfo(RecyclerView.ViewHolder holder, int position) {
        final NewsData mData = ObjectUtil.cast(mList.get(position));
        ImageView img_corner = null;
        ImageView img_user = null;
        if (holder instanceof ViewHolder0) {
            img_corner = ((ImageView) ((ViewHolder0) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder0) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder1) {
            img_corner = ((ImageView) ((ViewHolder1) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder1) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder2) {
            img_corner = ((ImageView) ((ViewHolder2) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder2) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder3) {
            img_corner = ((ImageView) ((ViewHolder3) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder3) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder4) {
            img_corner = ((ImageView) ((ViewHolder4) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder4) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder5) {
            img_corner = ((ImageView) ((ViewHolder5) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder5) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder6) {
            img_corner = ((ImageView) ((ViewHolder6) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder6) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder7) {
            img_corner = ((ImageView) ((ViewHolder7) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder7) holder).getView(R.id.img_user_avatar));
        } else if (holder instanceof ViewHolder8) {
            img_corner = ((ImageView) ((ViewHolder8) holder).getView(R.id.img_corner));
            img_user = ((ImageView) ((ViewHolder8) holder).getView(R.id.img_user_avatar));
        }
        final ImageView finalImg_corner = img_corner;
        final ImageView finalImg_user = img_user;
        JMessageClient.getUserInfo(Const.JPUSH_PREFIX + mData.getUser_id(), new GetUserInfoCallback() {
            @Override
            public void gotResult(int i, String s, UserInfo userInfo) {

                int user_role = Integer.parseInt(mData.getUser_role());
                finalImg_corner.setVisibility(View.VISIBLE);
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
                    case 5:
                        finalImg_corner.setVisibility(View.GONE);
                        Glide.with(mContext).load(R.drawable.corner_author).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.user_media).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                    case 6:
                        finalImg_corner.setVisibility(View.GONE);
                        Glide.with(mContext).load(R.drawable.corner_author).centerCrop().into(finalImg_corner);
                        if (userInfo.getAvatarFile() == null) {
                            Glide.with(mContext).load(R.drawable.user_system).centerCrop().into(finalImg_user);

                        } else {
                            Glide.with(mContext).load(userInfo.getAvatarFile()).centerCrop().into(finalImg_user);
                        }
                        break;
                }

            }
        });
    }

    private void updateTime(String time, RecyclerView.ViewHolder holder) {
        TextView send_time = null;
        if (holder instanceof ViewHolder0) {
            send_time = ((TextView) ((ViewHolder0) holder).getView(R.id.send_time));
        } else if (holder instanceof ViewHolder1) {
            send_time = ((TextView) ((ViewHolder1) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder2) {
            send_time = ((TextView) ((ViewHolder2) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder3) {
            send_time = ((TextView) ((ViewHolder3) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder4) {
            send_time = ((TextView) ((ViewHolder4) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder5) {
            send_time = ((TextView) ((ViewHolder5) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder6) {
            send_time = ((TextView) ((ViewHolder6) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder7) {
            send_time = ((TextView) ((ViewHolder7) holder).getView(R.id.send_time));

        } else if (holder instanceof ViewHolder8) {
            send_time = ((TextView) ((ViewHolder8) holder).getView(R.id.send_time));

        }
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
                send_time.setText("刚刚");
            } else {
                send_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            send_time.setText(diffHour + "小时前");
        } else {
            send_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }
}
