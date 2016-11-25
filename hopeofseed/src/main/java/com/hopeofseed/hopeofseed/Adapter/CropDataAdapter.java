package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.hopeofseed.hopeofseed.R.id.tv_distributor_name;
import static com.hopeofseed.hopeofseed.R.id.tv_name;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class CropDataAdapter extends BaseAdapter {
    Context mContext;
    List<CropData> mlist;

    public CropDataAdapter(Context context, ArrayList<CropData> list) {
        super();
        this.mContext = context;
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int i) {
        return mlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        CropData mData;
        mData = mlist.get(i);

        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.search_crop_items, null);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            viewHolder.tv_crop_class = (TextView) view.findViewById(R.id.tv_crop_class);
            viewHolder.img_user_avatar = (ImageView) view.findViewById(R.id.img_user_avatar);
            viewHolder.tv_address = (TextView) view.findViewById(R.id.tv_address);
            Log.e(TAG, "getView: " + mData.getVarietyName());
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.tv_name.setText(mData.getVarietyName());
        viewHolder.tv_crop_class.setText("【" + mData.getCropCategory2() + "】");
        if (!TextUtils.isEmpty(mData.getCataImgUrl())) {
        Glide.with(mContext)
        .load(Const.IMG_URL_FINAL + mData.getCataImgUrl())
        .centerCrop()
        .into(viewHolder.img_user_avatar);
        }
        viewHolder.tv_address.setText(mData.getAuthorizeNumber() + "   " + mData.getIsGen());
        return view;
        }

class ViewHolder {
    TextView tv_name, tv_crop_class, tv_address;
    ImageView img_user_avatar;
}
}
