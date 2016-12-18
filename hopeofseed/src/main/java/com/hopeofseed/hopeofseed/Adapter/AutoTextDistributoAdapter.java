package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Activitys.DistributorListForFromWeb;
import com.hopeofseed.hopeofseed.Activitys.MyCommodity;
import com.hopeofseed.hopeofseed.Activitys.SettingCommodityActivity;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class AutoTextDistributoAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    Context mContext;
    List<DistributorData> mlist;
    private ArrayList<DistributorData> mUnfilteredData;
    CharSequence StrSearch;

    public AutoTextDistributoAdapter(Context context, ArrayList<DistributorData> list) {
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
        final DistributorData mData;
        mData = mlist.get(i);
        view = _LayoutInflater.inflate(R.layout.auto_text_items, null);
        if (view != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            Button btn_search_from_web = (Button) view.findViewById(R.id.btn_search_from_web);
            if (i == mlist.size() - 1) {
                btn_search_from_web.setVisibility(View.VISIBLE);
            }
            tv_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, SettingCommodityActivity.class);
                    intent.putExtra("DistributorId", mData.getDistributorId());
                    mContext.startActivity(intent);
                }
            });
            btn_search_from_web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String putStr;
                    Intent intent = new Intent(mContext, DistributorListForFromWeb.class);
                    if (StrSearch == null || StrSearch.length() == 0) {
                        putStr = "";
                    } else {
                        putStr = StrSearch.toString().toLowerCase();
                    }
                    intent.putExtra("StrSearch", putStr);
                    mContext.startActivity(intent);
                }
            });

            Log.e(TAG, "getView: " + mData.getDistributorName());
            tv_name.setText(mData.getDistributorName());
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private class ArrayFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            StrSearch = prefix;
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<DistributorData>(mlist);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<DistributorData> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<DistributorData> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<DistributorData> newValues = new ArrayList<DistributorData>(count);

                for (int i = 0; i < count; i++) {
                    DistributorData pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if (pc.getDistributorName() != null && pc.getDistributorName().startsWith(prefixString)) {

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mlist = (List<DistributorData>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}

