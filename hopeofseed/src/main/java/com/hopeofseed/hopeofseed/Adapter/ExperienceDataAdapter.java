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

import com.hopeofseed.hopeofseed.Activitys.ExperienceActivity;
import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.ExperienceData;
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
public class ExperienceDataAdapter extends RecyclerView.Adapter<ExperienceDataAdapter.ViewHolder> {
    Context mContext;
    List<ExperienceData> mlist;

    public ExperienceDataAdapter(Context context, ArrayList<ExperienceData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_enterprise_noexpert_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExperienceData mData = mlist.get(position);
        holder.tv_name.setText("【" + mData.getNickname() + "】");

        holder.tv_title.setText(mData.getExperienceTitle());
        holder.tv_content.setText(mData.getExperienceContent());
        holder.tv_content.setSingleLine(false);
        holder.tv_content.setMaxLines(3);
        holder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                mContext.startActivity(intent);
            }
        });
        holder.rel_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 3);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(mData.getExperienceId()));
                mContext.startActivity(intent);
            }
        });
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExperienceActivity.class);
                intent.putExtra("ExperienceId", mData.getExperienceId());
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
