package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

import static com.hopeofseed.hopeofseed.R.id.view;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class SelectExpertDataAdapter extends RecyclerView.Adapter<SelectExpertDataAdapter.ViewHolder> {
    Context mContext;
    List<ExpertEnterperiseData> mlist;

    public SelectExpertDataAdapter(Context context, ArrayList<ExpertEnterperiseData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_enterprise_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final ExpertEnterperiseData mData = mlist.get(position);
        viewHolder.tv_name.setText("【" + mData.getExpertName() + "】");

        viewHolder.tv_title.setText(mData.getExperienceTitle());
        viewHolder.tv_content.setText(mData.getExperienceContent());
        viewHolder.tv_content.setSingleLine(false);
        viewHolder.tv_content.setMaxLines(3);
        viewHolder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                intent.putExtra("userid", mData.getUser_id());
                intent.putExtra("InfoId", mData.getExperienceId());
                mContext.startActivity(intent);
            }
        });
        viewHolder.rel_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                intent.putExtra("isInfo", true);
                intent.putExtra("NewClass", 3);
                intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero(mData.getExperienceId()));
                mContext.startActivity(intent);
            }
        });
        viewHolder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", String.valueOf(mData.getUser_id()));
                intent.putExtra("UserRole", Integer.parseInt(mData.getUser_role()));
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
