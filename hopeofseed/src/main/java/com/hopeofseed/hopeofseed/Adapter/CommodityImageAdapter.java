package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityDataNoUser;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/26 9:42
 * 修改人：whisper
 * 修改时间：2016/9/26 9:42
 * 修改备注：
 */
public class CommodityImageAdapter extends RecyclerView.Adapter<CommodityImageAdapter.ViewHolder> {
    private static final String TAG = "NewsImageAdapter";
    Context mContext;
    ArrayList<CommodityDataNoUser> images = new ArrayList<>();

    public CommodityImageAdapter(Context context, ArrayList<CommodityDataNoUser> images_temp) {
        super();
        mContext = context;

        if (images_temp.size() > 3) {
            for (int i = 0; i < 3; i++) {
                images.add(images_temp.get(i));
            }
        } else {
            images = images_temp;
        }

    }

    @Override
    public CommodityImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result_commodity, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CommodityImageAdapter.ViewHolder holder, final int position) {
        CommodityDataNoUser itemdata = new CommodityDataNoUser();
        itemdata = images.get(position);
        if (!(TextUtils.isEmpty(itemdata.getCommodityImgs()))) {
            Log.e(TAG, "onBindViewHolder: "+itemdata.getCommodityImgs());
            String[] arrImage = itemdata.getCommodityImgs().split(";");
            for (int i = 0; i < arrImage.length; i++) {
                arrImage[i] = Const.IMG_URL + arrImage[i];
            }
/*            if (arrImage.length > 0 ) {*/
                Log.e(TAG, "onBindViewHolder: "+Const.IMG_URL + arrImage[0]);
                Glide.with(mContext)
                        .load(arrImage[0]).placeholder(R.drawable.no_have_img)
                        .centerCrop()
                        .into(holder.imageView);
       /*     }*/
        }
        holder.tv_commodity_name.setText(itemdata.getCommodityName());
        holder.tv_commodity_name.getBackground().setAlpha(80);
        final CommodityDataNoUser finalItemdata = itemdata;
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", finalItemdata.getCommodityId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });
        if(!TextUtils.isEmpty(itemdata.getOwnerClass())){
        if (Integer.parseInt(itemdata.getOwnerClass()) == 2) {
            holder.img_guaranteed.setVisibility(View.VISIBLE);
        } else {
            holder.img_guaranteed.setVisibility(View.GONE);
        }}
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,img_guaranteed;
        TextView tv_commodity_name;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            tv_commodity_name = (TextView) itemView.findViewById(R.id.tv_commodity_name);
            img_guaranteed=(ImageView)itemView.findViewById(R.id.img_guaranteed);
        }
    }
}