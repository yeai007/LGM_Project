package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.SystemNofityActivity;
import com.hopeofseed.hopeofseed.Activitys.SystemNofityDetailActivity;
import com.hopeofseed.hopeofseed.JNXData.NotifyData;
import com.hopeofseed.hopeofseed.JNXData.NotifyDataNorealm;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.DateTools;

import java.text.ParseException;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

import static android.R.attr.type;
import static com.hopeofseed.hopeofseed.Activitys.MessageFragment.MESSAGE_UPDATE_LIST;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class NotifyListAdapter extends RecyclerView.Adapter<NotifyListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<NotifyDataNorealm> mList;
    Context mContext;
    private LayoutInflater inflater;
    Realm myRealm = Realm.getDefaultInstance();

    public NotifyListAdapter(Context context, List<NotifyDataNorealm> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.notify_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotifyDataNorealm itemData = mList.get(position);
        updateTime(holder, itemData.getNotifyCreateTime());
        holder.item_title.setText(itemData.getNotifyShowTitle());
        holder.item_content.setText(itemData.getNotifyShowTitle());
        holder.rel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NotifyData results1 =
                        myRealm.where(NotifyData.class).equalTo("NotifyId", itemData.getNotifyId()).findFirst();

                myRealm.beginTransaction();
                results1.setNotifyIsRead("1");
                NotifyData inNotifyData = myRealm.copyToRealmOrUpdate(results1);
                myRealm.commitTransaction();
                Intent intent = new Intent(mContext, SystemNofityDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", itemData);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                Intent intent_update = new Intent();  //Itent就是我们要发送的内容
                intent_update.setAction(MESSAGE_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                mContext.sendBroadcast(intent_update);   //发送广播
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView item_content, item_title, tv_time;
        RelativeLayout rel_item;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
            rel_item = (RelativeLayout) itemView.findViewById(R.id.rel_item);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            item_title = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    private void updateTime(ViewHolder holder, String time) {
        Long[] longDiff = null;
        String NowTime = null;
        try {
            NowTime = DateTools.getNowTime();
            longDiff = DateTools.getDiffTime(NowTime, time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String diffDay = String.valueOf(longDiff[1]);
        String diffHour = String.valueOf(longDiff[2]);
        String diffMinutes = String.valueOf(longDiff[3]);
        if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) == 0) {
            if (Integer.parseInt(diffMinutes) < 5) {
                holder.tv_time.setText("刚刚");
            } else {
                holder.tv_time.setText(diffMinutes + "分钟前");
            }
        } else if (Integer.parseInt(diffDay) == 0 && Integer.parseInt(diffHour) > 0) {
            holder.tv_time.setText(diffHour + "小时前");
        } else {
            holder.tv_time.setText(DateTools.StringDateTimeToDateNoYear(time));
        }
    }
}
