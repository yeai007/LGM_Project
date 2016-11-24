package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.R;

import java.util.HashMap;
import java.util.List;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/7/30 8:55
 * 修改人：whisper
 * 修改时间：2016/7/30 8:55
 * 修改备注：
 */
public class MyFriendsListAdapter extends BaseAdapter {
    private static final String TAG = "NewsListAdapter";
    Context mContext;
    List<UserData> mList;
    // 用来控制CheckBox的选中状况
    private static HashMap<Integer, Boolean> isSelected;
    public MyFriendsListAdapter(Context context, List list) {
        super();
        this.mContext = context;
        this.mList = list;
        isSelected = new HashMap<>();

        // 初始化数据
        initData();
    }

    // 初始化isSelected的数据
    private void initData() {
        for (int i = 0; i < mList.size(); i++) {
            getIsSelected().put(mList.get(i).getUser_id(), false);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        UserData itemData;
        itemData = mList.get(i);
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = _LayoutInflater
                    .inflate(R.layout.friends_list_items, null);
            holder.img_user_head = (ImageView) view.findViewById(R.id.img_group_head);
            holder.tv_username = (TextView) view.findViewById(R.id.tv_username);
            holder.cb = (CheckBox) view.findViewById(R.id.check_item);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.tv_username.setText(itemData.getUser_name());
        // 监听checkBox并根据原来的状态来设置新的状态
        holder.cb.setOnClickListener(new View.OnClickListener() {

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
            holder.cb.setChecked(getIsSelected().get(i));
        } else {
            isSelected.put(i, false);
        }
        return view;
    }

    class ViewHolder {
        ImageView img_user_head;
        TextView tv_username;
        CheckBox cb;
    }

    public static HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        MyFriendsListAdapter.isSelected = isSelected;
    }
}
