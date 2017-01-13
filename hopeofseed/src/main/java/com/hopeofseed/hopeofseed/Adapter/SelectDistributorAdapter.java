package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.hopeofseed.hopeofseed.R.id.tv_name;
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
public class SelectDistributorAdapter extends RecyclerView.Adapter<SelectDistributorAdapter.ViewHolder> {
    Context mContext;
    List<DistributorData> mlist;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public SelectDistributorAdapter(Context context, ArrayList<DistributorData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
        isSelected = new HashMap<>();
        // 初始化数据
        initData();
    }

    // 初始化isSelected的数据
    private void initData() {
        for (int i = 0; i < mlist.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.select_distributor_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DistributorData mData = mlist.get(position);
        holder.tv_name.setText(mData.getDistributorName());
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.check_item.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (isSelected.get(position)) {
                    isSelected.put(position, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(position, true);
                    setIsSelected(isSelected);
                }
            }
        });

//        // 根据isSelected来设置checkbox的选中状况
        if (getIsSelected().containsKey(position)) {
            holder.check_item.setChecked(getIsSelected().get(position));
        } else {
            holder.check_item.setChecked(false);
            isSelected.put(position, false);
        }
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
        ImageView img_show;
        TextView tv_name;
        CheckBox check_item;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            check_item = (CheckBox) itemView.findViewById(R.id.check_item);
            img_show = (ImageView) itemView.findViewById(R.id.img_show);
        }
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SelectDistributorAdapter.isSelected = isSelected;
    }

    public static void setIsSelectedClean() {
        SelectDistributorAdapter.isSelected.clear();
    }
}
