package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CropActivity;
import com.hopeofseed.hopeofseed.Activitys.SearchAcitvity;
import com.hopeofseed.hopeofseed.Activitys.SelectVarieties;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
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
public class CropDataAdapter extends RecyclerView.Adapter<CropDataAdapter.ViewHolder> {
    Context mContext;
    List<CropData> mlist;
    Boolean IsSearch = false;

    public CropDataAdapter(Context context, ArrayList<CropData> list, boolean isSearch) {
        super();
        this.mContext = context;
        this.mlist = list;
        this.IsSearch = isSearch;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.search_crop_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CropData mData = mlist.get(position);
        holder.tv_name.setText(mData.getVarietyName());
        holder.tv_crop_class.setText("【" + mData.getCropCategory2() + "】");
        Glide.with(mContext)
                .load(Const.IMG_URL_FINAL + mData.getCataImgUrl()).placeholder(R.drawable.no_have_img).dontAnimate()
                .centerCrop()
                .into(holder.img_user_avatar);
        holder.tv_address.setText(mData.getAuthorizeNumber() + "   " + mData.getIsGen());
        holder.rel_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsSearch) {
                    Intent intent = new Intent(mContext, CropActivity.class);
                    intent.putExtra("CropId", String.valueOf(mData.getCropId()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(mContext, SearchAcitvity.class);
                    intent.putExtra("FirstShow", "Crop");
                    intent.putExtra("StrSearch", mData.getVarietyName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            }
        });
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
        TextView tv_name, tv_crop_class, tv_address;
        ImageView img_user_avatar;
        RelativeLayout rel_title;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_crop_class = (TextView) itemView.findViewById(R.id.tv_crop_class);
            img_user_avatar = (ImageView) itemView.findViewById(R.id.img_user_avatar);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            rel_title = (RelativeLayout) itemView.findViewById(R.id.rel_title);
        }
    }
}
