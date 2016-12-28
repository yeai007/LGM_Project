package com.hopeofseed.hopeofseed.curView;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.R;


/**
 * @FileName:com.mgkj.smamootwo.curView
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/16
 * @Copyright:2014-2016 Moogeek
 */
public class InputPopupWindow extends PopupWindow {

    private View mMenuView;
    private EditText et_input;
    Button btn_submit;

    public InputPopupWindow(final Activity context, OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.input_popup_alert_dialog, null);
        et_input = (EditText) mMenuView.findViewById(R.id.et_input);
        btn_submit = (Button) mMenuView.findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(itemsOnClick);
        et_input.requestFocus();
        et_input.setText(Const.InputCathe);
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

                int height = mMenuView.findViewById(R.id.et_input).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        Const.InputCathe = et_input.getText().toString();
                        dismiss();
                       /* InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        mMenuView.setVisibility(View.GONE);*/
                    }
                }
                return true;
            }
        });

    }

    public String getInput() {
        String result = "";
        result = et_input.getText().toString().trim();
        return result;
    }
}
