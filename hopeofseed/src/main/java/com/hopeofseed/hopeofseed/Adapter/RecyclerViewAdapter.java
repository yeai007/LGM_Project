package com.hopeofseed.hopeofseed.Adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.List;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/2 16:42
 * 修改人：whisper
 * 修改时间：2016/12/2 16:42
 * 修改备注：
 */
public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter {
    Context mContext;
    private List<T> mList;//数据集合
    private int itemLayout;//item的布局
    private static final int CLASS_GENERAL = 0;//一般信息
    private static final int CLASS_IMAGE = 1;//图片信息
    private static final int CLASS_VADIO = 2;//视频信息
    private static final int CLASS_EXPERISE = 3;//农技经验
    private static final int CLASS_YIELD = 4;//分享产量
    private static final int CLASS_PROBLEM = 5;//发问
    private static final int CLASS_COMMODITY = 6;//商品
    private static final int CLASS_HUODONG = 7;//活动
    private static final int CLASS_FORWARD = 8;//转发


    public RecyclerViewAdapter(Context context, List<T> list) {
        super();
        mContext = context;
        mList = list;
    }

    public List<T> getList() {
        return mList;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //返回item的布局的holder
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CLASS_GENERAL:
                View v = inflater.inflate(R.layout.newlist_items, parent, false);
                viewHolder = new ViewHolder1(v);
                break;
            case CLASS_IMAGE:

                break;
            case CLASS_VADIO:

                break;
            case CLASS_EXPERISE:

                break;
            case CLASS_YIELD:

                break;
            case CLASS_PROBLEM:

                break;
            case CLASS_COMMODITY:

                break;
            case CLASS_HUODONG:

                break;
            case CLASS_FORWARD:

                break;
            default:
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {
            case CLASS_GENERAL:
               ViewHolder1 vh1 = (ViewHolder1) holder;//转型
                initData(vh1, position);
                break;
            case CLASS_IMAGE:
                ViewHolder1 vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_VADIO:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_EXPERISE:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_YIELD:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_PROBLEM:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_COMMODITY:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_HUODONG:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            case CLASS_FORWARD:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
            default:
                vh = (ViewHolder1) holder;//转型
                initData(vh, position);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int newclass = 0;
        NewsData item = ObjectUtil.cast(mList.get(position));
        return Integer.parseInt((item.getNewclass()));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    protected abstract void initData(RecyclerView.ViewHolder holder, int position);

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        private View findView;
        private View convertView;
        private SparseArray<Object> tags;
        //用来替代Map<Integer,Object>的容器, 效率比map高
        private SparseArray<View> views;

        public ViewHolder1(View itemView) {
            super(itemView);
            this.convertView = itemView;
            this.findView = itemView;
            tags = new SparseArray<>();
            views = new SparseArray<View>();
        }

        public View getConverView() {
            return convertView;
        }

        public View getFindView() {
            return findView;
        }

        public <T extends View> T getView(@IdRes int viewId) {
            View view;
            Object viewObj = findView.getTag(viewId);
            if (null != viewObj) {
                view = (View) viewObj;
            } else {
                view = findView.findViewById(viewId);
                findView.setTag(viewId, view);
            }
            return (T) view;
        }
    }
}
