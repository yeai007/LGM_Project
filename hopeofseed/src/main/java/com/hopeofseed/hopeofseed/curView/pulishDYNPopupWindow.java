package com.hopeofseed.hopeofseed.curView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.view.View.OnClickListener;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.Const;

/**
 * @FileName:com.mgkj.smamootwo.curView
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/16
 * @Copyright:2014-2016 Moogeek
 */
public class pulishDYNPopupWindow extends PopupWindow {
    private Button btn_publish_photo, btn_publish_video, btn_publish_text, btn_publish_share_experience, btn_publish_problem, btn_publish_share_yield, btn_cancel;
    private View mMenuView;

    public pulishDYNPopupWindow(Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_alert_dialog, null);
        btn_publish_photo = (Button) mMenuView.findViewById(R.id.btn_publish_photo);
        btn_publish_photo.setTypeface(Const.typeFace);

        btn_publish_video = (Button) mMenuView.findViewById(R.id.btn_publish_video);
        btn_publish_video.setTypeface(Const.typeFace);
        btn_publish_text = (Button) mMenuView.findViewById(R.id.btn_publish_text);
        btn_publish_text.setTypeface(Const.typeFace);
       // btn_publish_take_photo = (Button) mMenuView.findViewById(R.id.btn_publish_take_photo);


        //btn_publish_take_photo.setTypeface(Const.typeFace);
        btn_publish_share_experience = (Button) mMenuView.findViewById(R.id.btn_publish_share_experience);
        btn_publish_share_experience.setTypeface(Const.typeFace);
        btn_publish_problem = (Button) mMenuView.findViewById(R.id.btn_publish_problem);
        btn_publish_problem.setTypeface(Const.typeFace);
        btn_publish_share_yield = (Button) mMenuView.findViewById(R.id.btn_publish_share_yield);
        btn_publish_share_yield.setTypeface(Const.typeFace);
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
        btn_cancel.setTypeface(Const.typeFace);
        btn_publish_photo.setOnClickListener(itemsOnClick);
        btn_publish_video.setOnClickListener(itemsOnClick);
        btn_publish_text.setOnClickListener(itemsOnClick);
        //btn_publish_take_photo.setOnClickListener(itemsOnClick);
        btn_publish_share_experience.setOnClickListener(itemsOnClick);

        btn_publish_problem.setOnClickListener(itemsOnClick);
        btn_publish_share_yield.setOnClickListener(itemsOnClick);
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
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
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
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
}
