package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.JNXData.CropUserData;
import com.hopeofseed.hopeofseed.JNXData.CustomData;
import com.hopeofseed.hopeofseed.R;

import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/9 10:04
 * 修改人：whisper
 * 修改时间：2017/1/9 10:04
 * 修改备注：
 */
public class BreedOrganizationAdapter extends RecyclerView.Adapter<BreedOrganizationAdapter.ViewHolder> {
    Context mContext;
    List<CropUserData> mList;

    public BreedOrganizationAdapter(Context context, List<CropUserData> list) {
        super();
        mContext = context;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.breed_org_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CropUserData itemData = mList.get(position);
        holder.tv_name.setText(itemData.getUserNickName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserActivity.class);
                intent.putExtra("userid", itemData.getUserId());
                intent.putExtra("UserRole", Integer.parseInt(itemData.getCropUserRoleId()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
