package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.lgm.utils.ObjectUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/6 14:49
 * 修改人：whisper
 * 修改时间：2017/1/6 14:49
 * 修改备注：
 */
public class CommodityDistributorAdapter extends RecyclerView.Adapter<CommodityDistributorAdapter.ViewHolder> {
    Context mContext;
    List<DistributorData> mList;
    Handler mHandler = new Handler();
    pushFileResultTmp mCommResultTmp2;
    int DeletePostion;
    String CommodityId;

    public CommodityDistributorAdapter(Context context, List<DistributorData> list, String commodityId) {
        super();
        mContext = context;
        mList = list;
        CommodityId = commodityId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        View view = _LayoutInflater.inflate(R.layout.commodity_distirbutor_list_items, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final DistributorData itemData = mList.get(position);
        holder.tv_name.setText(itemData.getDistributorName());
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
                                deleteThis(itemData.getDistributorId());
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
        RelativeLayout item_view;
        TextView tv_name;

        public ViewHolder(View itemView) {
            super(itemView);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    private void deleteThis(String distributorId) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("DistributorId", distributorId);
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
                notifyItemRangeChanged(DeletePostion,mList.size());
            } else {
                Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
