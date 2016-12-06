package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;



/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/6 17:50
 * 修改人：whisper
 * 修改时间：2016/12/6 17:50
 * 修改备注：
 */
public class UnReadConversationListAdapter extends RecyclerView.Adapter<UnReadConversationListAdapter.ViewHolder> {
    private static final String TAG = "UnReadConversationListA";
    List<Conversation> mList;
    Context mContext;
    private LayoutInflater inflater;

    public UnReadConversationListAdapter(Context context, List<Conversation> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder:getUnReadMsgCnt "+position);
        Conversation itemData = mList.get(position);
        Log.e(TAG, "onBindViewHolder: getUnReadMsgCnt"+itemData.getUnReadMsgCnt());
        holder.item_content.setText("未读消息(" + itemData.getUnReadMsgCnt() + ")");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_item;
        TextView item_content;

        public ViewHolder(View itemView) {
            super(itemView);
            img_item = (ImageView) itemView.findViewById(R.id.img_item);
            item_content = (TextView) itemView.findViewById(R.id.item_content);
        }
    }
}
