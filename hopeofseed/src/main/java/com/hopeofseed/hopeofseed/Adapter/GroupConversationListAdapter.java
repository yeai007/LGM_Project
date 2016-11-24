package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hopeofseed.hopeofseed.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

import cn.jpush.im.android.api.content.MessageContent;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class GroupConversationListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<cn.jpush.im.android.api.model.Message> mList;

    public GroupConversationListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        if (mList == null) {
            return 0;
        } else {
            return mList.size();
        }

    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        cn.jpush.im.android.api.model.Message itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.group_conersation_list_items, null);
            holder.tv_msg = (TextView) view.findViewById(R.id.tv_msg);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        MessageContent mc = itemData.getContent();
        try {
            JSONObject js = new JSONObject(itemData.getContent().toJson());
            holder.tv_msg.setText(js.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }

    class ViewHolder {
        TextView tv_msg;
    }


}
