package com.hopeofseed.hopeofseed.Activitys;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hopeofseed.hopeofseed.Adapter.FriendViewAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.FriendData;
import com.hopeofseed.hopeofseed.JNXDataTmp.FriendDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/27 8:45
 * 修改人：whisper
 * 修改时间：2016/12/27 8:45
 * 修改备注：
 */
public class MyFollowFragment extends Fragment implements NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    Handler mHandler = new Handler();
    private int PageNo = 0;
    RecyclerView recy_list;
    private FriendViewAdapter mFriendViewAdapter;
    boolean isLoading = false;
    private SwipeRefreshLayout mRefreshLayout;
    ArrayList<FriendData> arrFriendData = new ArrayList<>();
    ArrayList<FriendData> arrFriendDataTmp = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.my_followed_fragment, null);
        initList(v);
        getData();
        return v;
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("PageNo", String.valueOf(PageNo));
        opt_map.put("ClassId", "1");
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetFollowAndFan.php", opt_map, FriendDataTmp.class, this);
    }

    private void initList(View v) {
        mRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) v.findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recy_list.setLayoutManager(manager);
        mFriendViewAdapter = new FriendViewAdapter(getActivity(), arrFriendData);
        recy_list.setAdapter(mFriendViewAdapter);
        //滚动监听，在滚动监听里面去实现加载更多的功能
        recy_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                int totalItemCount = manager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (!isLoading) {//一个布尔的变量，默认是false
                        isLoading = true;
                        PageNo = PageNo + 1;
                        getData();
                    }
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData();
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrFriendDataTmp = ((FriendDataTmp) rspBaseBean).getDetail();
        mHandler.post(updateList);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arrFriendData.clear();
            }
            arrFriendData.addAll(arrFriendDataTmp);
            mFriendViewAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
}
