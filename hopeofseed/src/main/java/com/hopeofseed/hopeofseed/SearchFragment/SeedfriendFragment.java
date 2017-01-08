package com.hopeofseed.hopeofseed.SearchFragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Activitys.UserActivity;
import com.hopeofseed.hopeofseed.Adapter.ExperienceDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.SeedfriendDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserDataNoRealm;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataNoRealmTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.handmark.pulltorefresh.library.PullToRefreshBase.Mode.BOTH;
import static com.hopeofseed.hopeofseed.R.id.lv_list;
import static com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment.arr_ExperienceData;
import static com.hopeofseed.hopeofseed.SearchFragment.ExperienceFragment.mExperienceDataAdapter;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * 种友
 */
public class SeedfriendFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_POSITION = "position";
    private static final String STR_SEARCH = "STR_SEARCH";
    private int position;
    SeedfriendDataAdapter mSeedfriendDataAdapter;
    ArrayList<UserDataNoRealm> arr_UserDataNoRealm = new ArrayList<>();
    ArrayList<UserDataNoRealm> arr_UserDataNoRealmTmp = new ArrayList<>();
    static String Str_search = "";
    int PageNo = 0;
    RecyclerView recy_list;
    private SwipeRefreshLayout mRefreshLayout;
    Handler mHandler = new Handler();
    boolean isLoading = false;

    public SeedfriendFragment(String strSearch) {
        Str_search = strSearch;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
        Str_search = getArguments().getString(STR_SEARCH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.search_user_fragment, null);
        getData(Str_search);
        initList(v);
        return v;
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
        final LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recy_list.setLayoutManager(manager);
        mSeedfriendDataAdapter = new SeedfriendDataAdapter(getContext(), arr_UserDataNoRealm);
        recy_list.setAdapter(mSeedfriendDataAdapter);
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
                        getData(Str_search);
                    } else {
                        //当没有更多的数据的时候去掉加载更多的布局
/*                        RecyclerViewAdapter adapter = (RecyclerViewAdapter) recy_news.getAdapter();
                        adapter.setIsNeedMore(false);
                        adapter.notifyDataSetChanged();*/
                    }
                }
            }
        });
    }

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSearch", Str_search);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchSeedfriendData.php", opt_map, UserDataNoRealmTmp.class, netCallBack);
    }


    private NetCallBack netCallBack = new NetCallBack() {
        @Override
        public void onSuccess(RspBaseBean rspBaseBean) {
            Log.e(TAG, "onSuccess: " + rspBaseBean.toString());
            UserDataNoRealmTmp mUserDataNoRealmTmp = ObjectUtil.cast(rspBaseBean);
            arr_UserDataNoRealmTmp = mUserDataNoRealmTmp.getDetail();
            mHandler.post(updateList);

        }

        @Override
        public void onError(String error) {
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }

        @Override
        public void onFail() {
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };
    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_UserDataNoRealm.clear();
            }
            arr_UserDataNoRealm.addAll(arr_UserDataNoRealmTmp);
            mSeedfriendDataAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
            isLoading = false;
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }

    @Override
    public void onRefresh() {
        PageNo = 0;
        getData(Str_search);
    }
}