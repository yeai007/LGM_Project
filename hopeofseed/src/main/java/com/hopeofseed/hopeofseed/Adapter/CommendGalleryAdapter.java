package com.hopeofseed.hopeofseed.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.Commend2Data;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/11/20 9:22
 * 修改人：whisper
 * 修改时间：2016/11/20 9:22
 * 修改备注：
 */
public class CommendGalleryAdapter extends RecyclerView.Adapter<CommendGalleryAdapter.ViewHolder> implements View.OnClickListener {
    // 数据集

    ArrayList<Commend2Data> mCommentOrForward = new ArrayList<>();
    private OnItemClickListener myOnItemClickListener = null;

    public CommendGalleryAdapter(ArrayList<Commend2Data> arrCommentOrForward) {
        super();
        mCommentOrForward = arrCommentOrForward;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
// 创建一个View，简单起见直接使用系统提供的布局，就是一个TextView
        View view = View.inflate(viewGroup.getContext(), R.layout.commend_item_review, null);
// 创建一个ViewHolder
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View v) {
        if (myOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            myOnItemClickListener.onClick(v, (int) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.myOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Commend2Data itemData = mCommentOrForward.get(i);
        viewHolder.tv_review.setText(itemData.getCommendSecondComment());
        viewHolder.tv_user_name.setText(itemData.getNickname() + ":");
        viewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return mCommentOrForward.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_user_name, tv_review;
        private OnItemClickListener myOnItemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_user_name = (TextView) itemView.findViewById(R.id.tv_user_name);
            tv_review = (TextView) itemView.findViewById(R.id.tv_review);
        }
    }

}
