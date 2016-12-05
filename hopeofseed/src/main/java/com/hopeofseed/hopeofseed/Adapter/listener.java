package com.hopeofseed.hopeofseed.Adapter;

import android.view.View;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/5 16:22
 * 修改人：whisper
 * 修改时间：2016/12/5 16:22
 * 修改备注：
 */
public class listener {
    public abstract interface OnItemClickListener {
        void onClick(View view, Object item);
    }

    public abstract interface OnItemLongClickListener {
        void onLongClick(View view, Object position);
    }
}
