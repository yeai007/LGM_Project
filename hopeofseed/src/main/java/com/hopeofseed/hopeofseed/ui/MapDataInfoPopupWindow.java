package com.hopeofseed.hopeofseed.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.R;

import static android.R.attr.name;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/22 9:22
 * 修改人：whisper
 * 修改时间：2016/10/22 9:22
 * 修改备注：
 */
public class MapDataInfoPopupWindow extends PopupWindow {
    private TextView map_info_name, map_info_address;
    private Button btn_show_info;
    private View mMenuView;

    public MapDataInfoPopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.map_data_info_popuwindow, null);
        btn_show_info = (Button) mMenuView.findViewById(R.id.btn_show_info);
        map_info_name = (TextView) mMenuView.findViewById(R.id.map_info_name);
        map_info_address = (TextView) mMenuView.findViewById(R.id.map_info_address);
        btn_show_info.setOnClickListener(itemsOnClick);
        map_info_name.setOnClickListener(itemsOnClick);
        map_info_address.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void SetTitle(String title) {
        this.map_info_name.setText(title);
    }
    public void SetAddress(String address) {
        this.map_info_address.setText(address);
    }

}
