package com.hopeofseed.hopeofseed.curView;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.Const;

/**
 * @FileName:com.mgkj.smamootwo.curView
 * @Desc:设置字体的Button
 * @Author:liguangming
 * @Date:2016/5/13
 * @Copyright:2014-2016 Moogeek
 */
public class fontButton extends Button {
    public fontButton(Context context) {
        super(context);
        this.setTypeface(Const.typeFace);
        this.setTextColor(Color.parseColor("#ffffff"));
        this.setBackgroundResource(R.drawable.button_corner);
    }

    public fontButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Const.typeFace);
        this.setTextColor(Color.parseColor("#ffffff"));
        this.setBackgroundResource(R.drawable.button_corner);
    }

    public fontButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setTypeface(Const.typeFace);
        this.setTextColor(Color.parseColor("#ffffff"));
        this.setBackgroundResource(R.drawable.button_corner);
    }


}
