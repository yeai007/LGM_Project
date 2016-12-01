package com.hopeofseed.hopeofseed.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/1 18:27
 * 修改人：whisper
 * 修改时间：2016/12/1 18:27
 * 修改备注：
 */
public class ScrollViewListView extends ListView {
    public ScrollViewListView(Context context) {
        super(context);
    }

    public ScrollViewListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 1, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
