package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.ProblemActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.ProblemData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class ProblemDataAdapter extends RecyclerView.Adapter<ProblemDataAdapter.ViewHolder> {
    Context mContext;
    List<ProblemData> mlist;

    public ProblemDataAdapter(Context context, ArrayList<ProblemData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_problem_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProblemData mData = mlist.get(position);
        holder.tv_name.setText("【" + mData.getNickname() + "】");
        holder.tv_title.setText(mData.getProblemTitle());
        holder.tv_content.setText(mData.getProblemContent());
        holder.tv_content.setSingleLine(false);
        holder.tv_content.setMaxLines(3);
        holder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.rel_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 5);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(mData.getProblemId()));
                mContext.startActivity(intent);
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                mContext.startActivity(intent);
            }
        });
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProblemActivity.class);
                intent.putExtra("ProblemId", mData.getProblemId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_content, tv_name;
        RelativeLayout rel_content;
        LinearLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            rel_content = (RelativeLayout) itemView.findViewById(R.id.rel_content);
            item_view = (LinearLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
