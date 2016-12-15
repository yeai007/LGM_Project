package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.hopeofseed.hopeofseed.R;
import java.util.ArrayList;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/28 10:45
 * 修改人：whisper
 * 修改时间：2016/7/28 10:45
 * 修改备注：
 */
public class SpStringAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> mList = new ArrayList<>();

    public SpStringAdapter(Context pContext, ArrayList<String> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = _LayoutInflater.inflate(R.layout.sp_string_item, null);
            holder.tv_name = (TextView) convertView.findViewById(R.id.text_item);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(mList.get(position));
        return convertView;
    }

    class ViewHolder {
        TextView tv_name;
    }
}