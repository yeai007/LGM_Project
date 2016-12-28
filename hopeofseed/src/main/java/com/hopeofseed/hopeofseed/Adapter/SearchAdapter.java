package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;

import com.hopeofseed.hopeofseed.model.BeanNoRealm;
import com.hopeofseed.hopeofseed.ui.CommonAdapter;
import com.hopeofseed.hopeofseed.ui.ViewHolder;
import com.hopeofseed.hopeofseed.R;

import java.util.List;


/**
 * Created by yetwish on 2015-05-11
 */

public class SearchAdapter extends CommonAdapter<BeanNoRealm> {

    public SearchAdapter(Context context, List<BeanNoRealm> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        holder.setImageResource(R.id.item_search_iv_icon, R.id.item_search_iv_icon)
                .setText(R.id.item_search_tv_title, mData.get(position).getTagName())
                .setText(R.id.item_search_tv_content, String.valueOf(mData.get(position).getTagID()))
                .setText(R.id.item_search_tv_comments, mData.get(position).getTagLevel());
    }
}
