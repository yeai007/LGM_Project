package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.PushSMSToCustom;
import com.hopeofseed.hopeofseed.Activitys.PushToCustom;
import com.hopeofseed.hopeofseed.JNXData.CustomPushData;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;

import static android.content.ContentValues.TAG;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class CustomPushListAdapter extends RecyclerView.Adapter<CustomPushListAdapter.ViewHolder> {
    List<CustomPushData> mList;
    Context mContext;
    private LayoutInflater inflater;

    public CustomPushListAdapter(Context context, List<CustomPushData> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_push_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CustomPushData itemData = mList.get(position);
        holder.tv_name.setText(itemData.getCustomPushName());
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iosDialog mIosDialog = new iosDialog.Builder(mContext)
                        .setMessage("通知类型选择！")
                        .setPositiveButton("短信通知", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, PushSMSToCustom.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("PushId", itemData.getCustomPushId());
                                mContext.startActivity(intent);
                            }
                        })
                        .setNegativeButton("行业通知", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(mContext, PushToCustom.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("PushId", itemData.getCustomPushId());
                                mContext.startActivity(intent);
                            }
                        })
                        .setTitle("种愿").create();
                mIosDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
