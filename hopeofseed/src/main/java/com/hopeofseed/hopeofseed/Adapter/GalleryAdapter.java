package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 16:37
 * 修改人：whisper
 * 修改时间：2016/10/7 16:37
 * 修改备注：
 */
public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> implements View.OnClickListener {

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String data);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private LayoutInflater mInflater;
    private List<String> mDatas = new ArrayList<>();

    public GalleryAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.recy_items,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.id_index_gallery_item_text);
        //viewHolder.mTxt.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        viewHolder.mTxt.getPaint().setAntiAlias(true);//抗锯齿
        view.setOnClickListener(this);
        return viewHolder;

    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Log.e(TAG, "onBindViewHolder: " + mDatas.get(i).toString());
        viewHolder.mTxt.setText(mDatas.get(i).toString());
        viewHolder.itemView.setTag(mDatas.get(i).toString());
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View view) {
        Log.e(TAG, "onClick: "+(String) view.getTag());
        mOnItemClickListener.onItemClick(view, (String) view.getTag());
    }

}