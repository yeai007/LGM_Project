package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/14 17:40
 * 修改人：whisper
 * 修改时间：2016/12/14 17:40
 * 修改备注：
 */
public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {
    Context mContext;
    List<ExpertEnterperiseData> mlist;

    public ExperienceAdapter(Context context, ArrayList<ExpertEnterperiseData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.expertshare_items, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ExpertEnterperiseData mData = mlist.get(position);
        holder.tv_name.setText("【" + mData.getNickname() + "】");
        holder.tv_title.setText(mData.getExperienceTitle());
        holder.tv_content.setText(mData.getExperienceContent());
        holder.tv_content.setSingleLine(false);
        holder.tv_content.setMaxLines(3);
        holder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
/*        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                intent.putExtra("InfoId", mData.getExperienceId());
                mContext.startActivity(intent);
            }
        });*/
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 3);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(mData.getExperienceId()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_title, tv_content;
        LinearLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            item_view = (LinearLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
