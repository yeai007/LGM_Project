package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.AutoData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/17 15:09
 * 修改人：whisper
 * 修改时间：2016/10/17 15:09
 * 修改备注：
 */
public class AutoTextViewAdapter extends BaseAdapter implements Filterable {
    private ArrayFilter mFilter;
    Context mContext;
    List<AutoData> mlist;
    private ArrayList<AutoData> mUnfilteredData;

    public AutoTextViewAdapter(Context context, ArrayList<AutoData> list) {
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
        return mlist.get(i).getAutoData();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        AutoData mData;
        mData = mlist.get(i);
        view = _LayoutInflater.inflate(R.layout.auto_text_items, null);
        if (view != null) {
            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_name.setText(mData.getAutoData());
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
            FilterResults results = new FilterResults();

            if (mUnfilteredData == null) {
                mUnfilteredData = new ArrayList<AutoData>(mlist);
            }

            if (prefix == null || prefix.length() == 0) {
                ArrayList<AutoData> list = mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<AutoData> unfilteredValues = mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<AutoData> newValues = new ArrayList<AutoData>(count);

                for (int i = 0; i < count; i++) {
                    AutoData pc = unfilteredValues.get(i);
                    if (pc != null) {

                        if (pc.getAutoData() != null && pc.getAutoData().startsWith(prefixString)) {

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
            mlist = (List<AutoData>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}

