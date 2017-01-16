package com.hopeofseed.hopeofseed.curView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Activitys.CustomPushActivity;
import com.hopeofseed.hopeofseed.Activitys.DistributorCountReportActivity;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.Activitys.PubishComprehensiveActivity;
import com.hopeofseed.hopeofseed.Activitys.PubishHuoDongActivity;
import com.hopeofseed.hopeofseed.Activitys.PubishMainActivity;
import com.hopeofseed.hopeofseed.Activitys.PublishProblem;
import com.hopeofseed.hopeofseed.Activitys.ShareExperience;
import com.hopeofseed.hopeofseed.Activitys.ShareYield;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.UserMenu;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhuguohui on 2016/11/8.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static List<UserMenu> mList = new ArrayList<>();
    Context mContext;
    PopupWindow mPopupWindow;

    public MyAdapter(Context context, List<UserMenu> list, PopupWindow popupWindow) {
        super();
        this.mContext = context;
        this.mList = list;
        this.mPopupWindow = popupWindow;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final UserMenu itemData = mList.get(position);
        holder.tv_title.setText(itemData.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Intent intent;
                                                   if (itemData.getTitle().equals("活动")) {
                                                       if (Const.isVip) {
                                                           intent = new Intent(mContext, PubishHuoDongActivity.class);
                                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           mContext.startActivity(intent);
                                                       } else {
                                                           Toast.makeText(mContext, "您的会员已到期\n请升级", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                                   if (itemData.getTitle().equals("商品")) {
                                                       if (Const.isVip) {
                                                           intent = new Intent(mContext, MyCommodity.class);
                                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           intent.putExtra("commodityId", "0");
                                                           mContext.startActivity(intent);
                                                       } else {
                                                           Toast.makeText(mContext, "您的会员已到期\n请升级", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                                   if (itemData.getTitle().equals("文字")) {

                                                       intent = new Intent(mContext, PubishMainActivity.class);
                                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                       mContext.startActivity(intent);
                                                   }
                                                   if (itemData.getTitle().equals("农技经验")) {
                                                       intent = new Intent(mContext, ShareExperience.class);
                                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                       mContext.startActivity(intent);
                                                   }
                                                   if (itemData.getTitle().equals("产量表现")) {
                                                       intent = new Intent(mContext, ShareYield.class);
                                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                       mContext.startActivity(intent);
                                                   }
                                                   if (itemData.getTitle().equals("发问")) {
                                                       intent = new Intent(mContext, PublishProblem.class);
                                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                       mContext.startActivity(intent);
                                                   }
                                                   if (itemData.getTitle().equals("云通知")) {
                                                       if (Const.isVip) {
                                                           intent = new Intent(mContext, CustomPushActivity.class);
                                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           mContext.startActivity(intent);
                                                       } else {
                                                           Toast.makeText(mContext, "您的会员已到期\n请升级", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                                   if (itemData.getTitle().equals("统计分析")) {
                                                       if (Const.isVip) {
                                                           intent = new Intent(mContext, DistributorCountReportActivity.class);
                                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           mContext.startActivity(intent);
                                                       } else {
                                                           Toast.makeText(mContext, "您的会员已到期\n请升级", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                                   if (itemData.getTitle().equals("更多")) {
                                                       ((pulishPopupRecyle) mPopupWindow).setNextPage();
                                                   }
                                                   if (itemData.getTitle().equals("综合信息")) {
                                                       if (Const.isVip) {
                                                           intent = new Intent(mContext, PubishComprehensiveActivity.class);
                                                           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                           mContext.startActivity(intent);
                                                       } else {
                                                           Toast.makeText(mContext, "您的会员已到期\n请升级", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               }
                                           }

        );
        holder.btn_publish_text.setImageResource(itemData.getResId());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        ImageView btn_publish_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            btn_publish_text = (ImageView) itemView.findViewById(R.id.btn_publish_text);
        }
    }
}
