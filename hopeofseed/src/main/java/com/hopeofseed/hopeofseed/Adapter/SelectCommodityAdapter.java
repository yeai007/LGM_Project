package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
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
public class SelectCommodityAdapter extends BaseAdapter {
    Context mContext;
    List<CommodityData> mlist;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;

    public SelectCommodityAdapter(Context context, ArrayList<CommodityData> list) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        CommodityData mData;
        mData = mlist.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater.inflate(R.layout.select_commodity_items, null);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
            holder.check_item = (CheckBox) view.findViewById(R.id.check_item);
            holder.img_show = (ImageView) view.findViewById(R.id.img_show);
            Log.e(TAG, "getView: " + mData.getCommodityName());
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        String[] arrImage = mData.getCommodityImgs().split(";");
        if (arrImage.length > 0) {
            Log.e(TAG, "getView: " + Const.IMG_URL + arrImage[0]);
            Glide.with(mContext)
                    .load(Const.IMG_URL + arrImage[0])
                    .centerCrop()
                    .into(holder.img_show);
        }
        holder.tv_name.setText(mData.getCommodityName());
        holder.tv_content.setText(mData.getCommodityTitle());
        holder.tv_price.setText(mData.getCommodityPrice());
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.check_item.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (isSelected.get(i)) {
                    isSelected.put(i, false);
                    setIsSelected(isSelected);
                } else {
                    isSelected.put(i, true);
                    setIsSelected(isSelected);
                }
            }
        });

//        // 根据isSelected来设置checkbox的选中状况
        if (getIsSelected().containsKey(i)) {
            holder.check_item.setChecked(getIsSelected().get(i));
        } else {
            isSelected.put(i, false);
        }
        return view;
    }

    class ViewHolder {
        ImageView img_show;
        TextView tv_name, tv_content, tv_price;
        CheckBox check_item;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        SelectCommodityAdapter.isSelected = isSelected;
    }
}
