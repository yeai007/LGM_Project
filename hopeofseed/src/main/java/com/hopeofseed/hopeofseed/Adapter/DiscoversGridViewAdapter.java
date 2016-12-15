package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Activitys.SelectAuthor;
import com.hopeofseed.hopeofseed.Activitys.SelectDistributor;
import com.hopeofseed.hopeofseed.Activitys.SelectEnterprise;
import com.hopeofseed.hopeofseed.Activitys.SelectExpert;
import com.hopeofseed.hopeofseed.Activitys.SelectSeedFriend;
import com.hopeofseed.hopeofseed.Activitys.SelectVarieties;
import com.hopeofseed.hopeofseed.R;
import java.util.ArrayList;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_AUTHOR;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_BUSINESS;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_DISTRIBUTOR;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_EXPERT;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_SEED_FRIEND;
import static com.hopeofseed.hopeofseed.Activitys.DiscoverFragment.SELECT_VARIETIES;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 9:34
 * 修改人：whisper
 * 修改时间：2016/7/30 9:34
 * 修改备注：
 */
public class DiscoversGridViewAdapter extends RecyclerView.Adapter<DiscoversGridViewAdapter.ViewHolder> {
    Context mContext;
    ArrayList<String> mData = new ArrayList<>();

    public DiscoversGridViewAdapter(Context context, ArrayList<String> data) {
        super();
        this.mContext = context;
        this.mData = data;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gv_discoverlist_items, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
       // holder.img_content.setImageResource(R.drawable.img_group_default);
        Glide.with(mContext)
                .load(R.drawable.img_group_default)
                .centerCrop()
                .into(holder.img_content);
        holder.tv_name.setText(mData.get(position));
        switch (position) {
            case SELECT_VARIETIES://找品种
                Glide.with(mContext)
                        .load(R.drawable.faxian_search_crop)
                        .centerCrop()
                        .into( holder.img_content);
                break;
            case SELECT_DISTRIBUTOR://找经销商
                Glide.with(mContext)
                        .load(R.drawable.faxian_near_distributor)
                        .centerCrop()
                        .into( holder.img_content);
                break;
            case SELECT_EXPERT://找专家
                Glide.with(mContext)
                        .load(R.drawable.faxian_search_expert)
                        .centerCrop()
                        .into( holder.img_content);
                break;
            case SELECT_BUSINESS://找企业
                Glide.with(mContext)
                        .load(R.drawable.faxian_search_busniss)
                        .centerCrop()
                        .into( holder.img_content);
                break;
            case SELECT_AUTHOR://找机构
                Glide.with(mContext)
                        .load(R.drawable.faxian_search_autor)
                        .centerCrop()
                        .into( holder.img_content);
                break;
            case SELECT_SEED_FRIEND://找种友
                Glide.with(mContext)
                        .load(R.drawable.faxian_near_friend)
                        .centerCrop()
                        .into( holder.img_content);
                break;
        }
        holder.item_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                switch (position) {
                    case SELECT_VARIETIES://找品种
                        intent = new Intent(mContext, SelectVarieties.class);
                        mContext.startActivity(intent);
                        break;
                    case SELECT_DISTRIBUTOR://找经销商
                        intent = new Intent(mContext, SelectDistributor.class);
                        mContext.startActivity(intent);
                        break;
                    case SELECT_EXPERT://找专家
                        intent = new Intent(mContext, SelectExpert.class);
                        mContext.startActivity(intent);
                        break;
                    case SELECT_BUSINESS://找企业
                        intent = new Intent(mContext, SelectEnterprise.class);
                        mContext.startActivity(intent);
                        break;
                    case SELECT_AUTHOR://找机构
                        intent = new Intent(mContext, SelectAuthor.class);
                        mContext.startActivity(intent);
                        break;
                    case SELECT_SEED_FRIEND://找种友
                        intent = new Intent(mContext, SelectSeedFriend.class);
                        mContext.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        ImageView img_content;
        RelativeLayout item_view;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            img_content = (ImageView) itemView.findViewById(R.id.img_content);
            item_view = (RelativeLayout) itemView.findViewById(R.id.item_view);
        }
    }
}
