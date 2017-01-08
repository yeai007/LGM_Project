package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/8 10:00
 * 修改人：whisper
 * 修改时间：2017/1/8 10:00
 * 修改备注：
 */
public class DistributorCommodityAdapter extends RecyclerView.Adapter<DistributorCommodityAdapter.ViewHolder> {
    Context mContext;
    List<CommodityData> mList;
    String DistributorId;
    Handler mHandler = new Handler();
    pushFileResultTmp mCommResultTmp2;
    int DeletePostion;

    public DistributorCommodityAdapter(Context context, List<CommodityData> list, String distributorId) {
        super();
        mContext = context;
        mList = list;
        DistributorId = distributorId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.commodity_for_distributor_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CommodityData itemData = mList.get(position);
        String[] arrImage = itemData.getCommodityImgs().split(";");
     /*   if (arrImage.length > 0 && (!TextUtils.isEmpty(arrImage[0]))) {*/
            Glide.with(mContext)
                    .load(Const.IMG_URL + arrImage[0])    .dontAnimate()
                    .placeholder(R.drawable.no_have_img)
                    .centerCrop()
                    .into(holder.img);
      /*  }*/
        holder.tv_name.setText(itemData.getCommodityName());
        holder.create_time.setText(DateTools.StringDateTimeToDate(itemData.getCreateTime()));
        holder.tv_content.setText(itemData.getCommodityTitle());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                iosDialog mIosDialog = new iosDialog.Builder(mContext)
                        .setMessage("确认删除代理关系吗？\n")
                        .setPositiveButton("我要删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                DeletePostion = position;
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
        TextView tv_name, tv_content, create_time;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            create_time = (TextView) itemView.findViewById(R.id.create_time);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }

    private void deleteThis(String CommodityId) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("DistributorId", DistributorId);
        opt_map.put("CommodityId", CommodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "DeleteReCommodityDistributorById.php", opt_map, pushFileResultTmp.class, new NetCallBack() {
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
                Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                mList.remove(DeletePostion);
/*                mList.remove(DeletePostion);*/
                notifyItemRemoved(DeletePostion);
                notifyItemRangeChanged(DeletePostion, mList.size());
            } else {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
