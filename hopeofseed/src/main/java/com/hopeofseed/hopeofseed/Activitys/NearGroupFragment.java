package com.hopeofseed.hopeofseed.Activitys;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.GroupListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/13 9:14
 * 修改人：whisper
 * 修改时间：2016/12/13 9:14
 * 修改备注：
 */
public class NearGroupFragment extends Fragment implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "NearGroupFragment";
    RecyclerView recycler_list;
    GroupListAdapter mAdapter;
    ArrayList<GroupData> mList = new ArrayList<>();
    ArrayList<GroupData> mListTmp = new ArrayList<>();
    Handler mHandler = new Handler();
    private SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_near_group, null);
        initView(v);
        mHandler.post(getdatapost);
        getData();
        return v;
    }

    Runnable updatelist = new Runnable() {
        @Override
        public void run() {
            mList.clear();
            mList.addAll(mListTmp);
            mAdapter.notifyDataSetChanged();

            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
    Runnable getdatapost = new Runnable() {
        @Override
        public void run() {
            getData();
        }
    };

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        opt_map.put("StrSearch", "");
        opt_map.put("PageNo", String.valueOf(PageNo));
        hu.httpPost(Const.BASE_URL + "GetGroupList.php", opt_map, GroupDataTmp.class, this);
    }

    private void initView(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recycler_list = (RecyclerView) v.findViewById(R.id.recycler_list);
        recycler_list.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recycler_list.setLayoutManager(layoutManager);
        mAdapter = new GroupListAdapter(getContext(), mList);
        recycler_list.setAdapter(mAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        Log.e(TAG, "onScrolled: loadingmaore");
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    } else if (mListTmp.size() < 20) {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        isLoading = false;
        GroupDataTmp groupDataTmp = ObjectUtil.cast(rspBaseBean);
        mListTmp = groupDataTmp.getDetail();
        mHandler.post(updatelist);
    }

    @Override
    public void onError(String error) {
        isLoading = false;
    }

    @Override
    public void onFail() {
        isLoading = false;
    }


    @Override
    public void onRefresh() {
        Log.e(TAG, "onRefresh: ");
        PageNo = 0;
        getData();
    }
}
