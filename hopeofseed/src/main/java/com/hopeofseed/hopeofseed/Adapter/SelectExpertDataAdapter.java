package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.NewsInfoNewActivity;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.ExpertEnterperiseData;
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
public class SelectExpertDataAdapter extends BaseAdapter {
    Context mContext;
    List<ExpertEnterperiseData> mlist;

    public SelectExpertDataAdapter(Context context, ArrayList<ExpertEnterperiseData> list) {
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        final ExpertEnterperiseData mData;
        mData = mlist.get(i);
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.search_enterprise_items, null);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(), UserActivity.class);
                    intent.putExtra("userid", mData.getUser_id());
                    intent.putExtra("InfoId", mData.getExperienceId());
                    mContext.startActivity(intent);
                }
            });
            viewHolder.rel_content = (RelativeLayout) view.findViewById(R.id.rel_content);
            viewHolder.rel_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(), NewsInfoNewActivity.class);
                    intent.putExtra("isInfo",true);
                    intent.putExtra("NewClass", 3);
                    intent.putExtra("InfoId", ObjectUtil.RemoveOpenZero( mData.getExperienceId()));
                    mContext.startActivity(intent);
                }
            });
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText("【" + mData.getExpertName() + "】");

        viewHolder.tv_title.setText(mData.getExperienceTitle());
        viewHolder.tv_content.setText(mData.getExperienceContent());
        viewHolder.tv_content.setSingleLine(false);
        viewHolder.tv_content.setMaxLines(3);
        viewHolder.tv_content.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
        return view;
    }

    class ViewHolder {
        TextView tv_title, tv_content, tv_name;
        RelativeLayout rel_content;
    }
}
