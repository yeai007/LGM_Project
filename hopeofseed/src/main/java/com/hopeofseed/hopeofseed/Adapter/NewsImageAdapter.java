package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.ShowBigImage;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.ShowImage;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 9:42
 * 修改人：whisper
 * 修改时间：2016/9/26 9:42
 * 修改备注：
 */
public class NewsImageAdapter extends RecyclerView.Adapter<NewsImageAdapter.ViewHolder> {
    private static final String TAG = "NewsImageAdapter";
    Context mContext;
    List<String> images = new ArrayList<>();

    public NewsImageAdapter(Context context, List<String> images_temp) {
        super();
        mContext = context;
        images = images_temp;
    }

    @Override
    public NewsImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NewsImageAdapter.ViewHolder holder, final int position) {

      //  Log.e(TAG, "onBindViewHolder: " + Const.IMG_URL + images.get(position));

        Glide.with(mContext)
                .load(Const.IMG_URL + images.get(position))
                .centerCrop()
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShowBigImage.class);
                intent.putExtra("IMG_URL", Const.IMG_URL + images.get(position));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}