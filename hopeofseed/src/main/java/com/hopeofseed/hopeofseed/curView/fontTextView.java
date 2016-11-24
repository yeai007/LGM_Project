package com.hopeofseed.hopeofseed.curView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.lgm.utils.Const;

/**
 * @FileName:smamoo.curView
 * @Desc:设置字体的Ｔ
 * @Author:liguangming
 * @Date:2016/5/4
 * @Copyright:2014-2016 Moogeek
 */
public class fontTextView extends TextView {

    public fontTextView(Context context) {
        super(context);
        this.setTypeface(Const.typeFace);
    }

    public fontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Const.typeFace);
    }

    public fontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTypeface(Const.typeFace);
    }
}

