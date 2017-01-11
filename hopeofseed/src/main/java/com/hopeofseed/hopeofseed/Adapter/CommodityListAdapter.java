package com.hopeofseed.hopeofseed.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.CommodityActivity;
import com.hopeofseed.hopeofseed.Activitys.SettingDistributorActivity;
import com.hopeofseed.hopeofseed.Activitys.ThisCommodityDistributorActivity;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.DateTools;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;
import java.util.List;

import static java.lang.System.load;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class CommodityListAdapter extends RecyclerView.Adapter<CommodityListAdapter.ViewHolder> {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<CommodityData> mList;
    Handler mHandler = new Handler();
    pushFileResultTmp mCommResultTmp2;
    int DeletePostion;

    public CommodityListAdapter(Context context, List<CommodityData> list) {
        super();
        this.mList = list;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.commodity_list_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CommodityData itemData;
        itemData = mList.get(position);
        String[] arrImage = itemData.getCommodityImgs().split(";");
        Glide.with(mContext)
                .load(Const.IMG_URL + arrImage[0])
                .placeholder(R.drawable.no_have_img)
                .centerCrop()
                .into(holder.img);
        holder.create_time.setText(DateTools.StringDateTimeToDate(itemData.getCreateTime()));
        holder.tv_name.setText(itemData.getCommodityName());
        holder.tv_content.setText(itemData.getCommodityVariety());
        if (TextUtils.isEmpty(itemData.getCommodityPrice()) || itemData.getCommodityPrice().equals("0")) {
            holder.tv_price.setText("￥ " + " 议价");
        } else {
            holder.tv_price.setText("￥ " + itemData.getCommodityPrice());
        }
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommodityActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
        holder.btn_distributor_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ThisCommodityDistributorActivity.class);
                intent.putExtra("CommodityId", itemData.getCommodityId());
                mContext.startActivity(intent);
            }
        });
        holder.item_view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DeletePostion = position;
                iosDialog mIosDialog = new iosDialog.Builder(mContext)
                        .setMessage("确认删除该商品吗！\n删除商品会将该商品所有代理关系同步删除。")
                        .setPositiveButton("我要删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteThis(itemData.getCommodityId());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("种愿").create();
                mIosDialog.setCancelable(true);
                mIosDialog.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tv_name, tv_content, tv_price, btn_distributor_setting, create_time;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            img = (ImageView) itemView.findViewById(R.id.img);
            create_time = (TextView) itemView.findViewById(R.id.create_time);
            btn_distributor_setting = (TextView) itemView.findViewById(R.id.btn_distributor_setting);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }

    private void deleteThis(String commodityId) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", commodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "DeleteCommodityById.php", opt_map, pushFileResultTmp.class, new NetCallBack() {
            @Override
            public void onSuccess(RspBaseBean rspBaseBean) {
                mCommResultTmp2 = ObjectUtil.cast(rspBaseBean);
                mHandler.post(DeleteResult);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFail() {

            }
        });
    }

    Runnable DeleteResult = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp2.getDetail().getContent().equals("删除成功")) {
                mList.remove(DeletePostion);
                notifyDataSetChanged();
            } else {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
