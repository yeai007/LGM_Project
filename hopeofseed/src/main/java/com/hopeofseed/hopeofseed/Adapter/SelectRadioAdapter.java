package com.hopeofseed.hopeofseed.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.CommSelectData;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.SelectRadioActivity;

import java.util.List;

import static com.hopeofseed.hopeofseed.R.id.tv_content;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 10:09
 * 修改人：whisper
 * 修改时间：2016/12/30 10:09
 * 修改备注：
 */
public class SelectRadioAdapter extends RecyclerView.Adapter<SelectRadioAdapter.ViewHolder> {
    Context mContext;
    List<String> mList;
    private LayoutInflater inflater;

    public SelectRadioAdapter(Context context, List<String> list) {
        super();
        this.mContext = context;
        this.mList = list;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.select_radio_list_item, null, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
/*        CommSelectData itemData = mList.get(position);*/
        holder.tv_content.setText(mList.get(position));
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("item",mList.get(position));
                ((SelectRadioActivity) mContext).setResult(Activity.RESULT_OK, intent);
                ((SelectRadioActivity) mContext).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;
        LinearLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            item_view = (LinearLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
