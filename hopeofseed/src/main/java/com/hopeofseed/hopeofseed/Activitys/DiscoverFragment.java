package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.JNXDataTmp.GroupDataTmp;
import com.hopeofseed.hopeofseed.Adapter.DiscoversGridViewAdapter;
import com.hopeofseed.hopeofseed.Adapter.DiscoversListAdapter;
import com.hopeofseed.hopeofseed.Adapter.EventListAdapter;
import com.hopeofseed.hopeofseed.Adapter.GroupsListAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CompanyData;
import com.hopeofseed.hopeofseed.JNXData.GroupData;
import com.hopeofseed.hopeofseed.JNXData.NewsData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CompanyDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：liguangming
 * 类描述：发现主页面
 * 创建人：whisper
 * 创建时间：2016/7/27 14:41
 * 修改人：whisper
 * 修改时间：2016/7/27 14:41
 * 修改备注：
 */
public class DiscoverFragment extends Fragment implements NetCallBack, View.OnClickListener {

    GridView gv_discover;
    ArrayList<CompanyData> arr_CompanyData = new ArrayList<>();
    DiscoversGridViewAdapter discoversGridViewAdapter;
    ArrayList<String> arr_title = new ArrayList<>();
    public static final int SELECT_VARIETIES = 0;//找品种
    public static final int SELECT_DISTRIBUTOR = 1;//找经销商
    public static final int SELECT_EXPERT = 2;//找专家
    public static final int SELECT_BUSINESS = 3;//找企业
    public static final int SELECT_AUTHOR = 4;//找机构
    public static final int SELECT_SEED_FRIEND = 5;//找种友
    int LV_TYPE = 0;
    ArrayList<CompanyData> arr_CompanyData_tmp = new ArrayList<>();
    ArrayList<NewsData> arr_NewsData = new ArrayList<>();
    NewsDataTmp newsDataTmp = new NewsDataTmp();
    GroupDataTmp groupDataTmp = new GroupDataTmp();
    DiscoversListAdapter discoversListAdapter;
    EventListAdapter eventListAdapter;
    GroupsListAdapter groupsListAdapter;
    PullToRefreshListView lv_hot, lv_group, lv_event;
    ArrayList<GroupData> arr_GroupData = new ArrayList<>();
    Button tv_hot, tv_event, tv_group;
    RelativeLayout rel_search;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_discover, null);
        initData();
        initView(v);
        initList(v);
        return v;
    }

    private void initData() {
        arr_title.add("找品种");
        arr_title.add("附近经销商");
        arr_title.add("找专家");
        arr_title.add("找企业");
        arr_title.add("找机构");
        arr_title.add("找种友");
    }

    private void initView(View v) {
        rel_search = (RelativeLayout) v.findViewById(R.id.rel_search);
        rel_search.setOnClickListener(listener);
        tv_hot = (Button) v.findViewById(R.id.tv_hot);
        tv_event = (Button) v.findViewById(R.id.tv_event);
        tv_group = (Button) v.findViewById(R.id.tv_group);
        tv_hot.setOnClickListener(this);
        tv_event.setOnClickListener(this);
        tv_group.setOnClickListener(this);
        lv_hot = (PullToRefreshListView) v.findViewById(R.id.lv_hot);
        discoversListAdapter = new DiscoversListAdapter(getActivity(), arr_NewsData);
        lv_hot.setAdapter(discoversListAdapter);
        lv_group = (PullToRefreshListView) v.findViewById(R.id.lv_group);
        groupsListAdapter = new GroupsListAdapter(getActivity(), arr_GroupData);
        lv_group.setAdapter(groupsListAdapter);

        lv_event = (PullToRefreshListView) v.findViewById(R.id.lv_event);
        /*eventListAdapter = new EventListAdapter(getActivity(), arr_GroupData);
        lv_event.setAdapter(eventListAdapter);*/


        gv_discover = (GridView) v.findViewById(R.id.gv_discover);
        discoversGridViewAdapter = new DiscoversGridViewAdapter(getActivity(), arr_title);
        gv_discover.setAdapter(discoversGridViewAdapter);
        gv_discover.setOnItemClickListener(GridListener);
        getStarCompany();
    }

    private void getStarCompany() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getStarCompany.php", opt_map, CompanyDataTmp.class, this);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rel_search:
                    Intent intent = new Intent(getActivity(), SearchAcitvity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    public AdapterView.OnItemClickListener GridListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            switch (position) {
                case SELECT_VARIETIES://找品种
                    intent = new Intent(getActivity(), SelectVarieties.class);
                    startActivity(intent);
                    break;
                case SELECT_DISTRIBUTOR://找经销商
                    intent = new Intent(getActivity(), SelectDistributor.class);
                    startActivity(intent);
                    break;
                case SELECT_EXPERT://找专家
                    intent = new Intent(getActivity(), SelectExpert.class);
                    startActivity(intent);
                    break;
                case SELECT_BUSINESS://找企业
                    intent = new Intent(getActivity(), SelectEnterprise.class);
                    startActivity(intent);
                    break;
                case SELECT_AUTHOR://找机构
                    intent = new Intent(getActivity(), SelectAuthor.class);
                    startActivity(intent);
                    break;
                case SELECT_SEED_FRIEND://找种友
                    intent = new Intent(getActivity(), SelectSeedFriend.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("getStarCompany")) {
            arr_CompanyData_tmp = ObjectUtil.cast(rspBaseBean.result);
            updateView();
        } else if (rspBaseBean.RequestSign.equals("getNews")) {

            newsDataTmp = ObjectUtil.cast(rspBaseBean);
            updateHotView();
        } else if (rspBaseBean.RequestSign.equals("GetDiscoveryGroup")) {
            groupDataTmp = ObjectUtil.cast(rspBaseBean);
            updateGroupView();
        }
    }

    private void updateGroupView() {
        Message msg = groupViewHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private void updateHotView() {
        Message msg = hotViewHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    private void updateView() {
        Message msg = getStarCompanyHandle.obtainMessage();
        msg.arg1 = 1;
        msg.sendToTarget();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private Handler groupViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    arr_GroupData.clear();
                    arr_GroupData.addAll(groupDataTmp.getDetail());
                    groupsListAdapter.notifyDataSetChanged();
                    lv_group.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };
    private Handler hotViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    arr_NewsData.clear();
                    arr_NewsData.addAll(newsDataTmp.getDetail());
                    discoversListAdapter.notifyDataSetChanged();
                    lv_hot.onRefreshComplete();
                    break;
                case 2:

                    break;
            }
        }
    };
    private Handler getStarCompanyHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:
                    arr_CompanyData.clear();
                    arr_CompanyData.addAll(arr_CompanyData_tmp);
                    discoversGridViewAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    break;
            }
        }
    };


    //*****************************************************8
    private void initList(View v) {
        getData();
        lv_hot.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_hot.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_hot.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_hot.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_hot.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_hot.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_hot.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getData();
                }

            }
        });

        lv_group.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_group.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_group.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_group.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getGroupData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_group.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_group.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_group.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getGroupData();
                }

            }
        });

    }

    private void getGroupData() {
        HashMap<String, String> opt_map = new HashMap<>();
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDiscoveryGroup.php", opt_map, GroupDataTmp.class, this);

    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("classid", String.valueOf(0));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "get_News.php", opt_map, NewsDataTmp.class, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hot:
                lv_hot.setVisibility(View.VISIBLE);
                lv_group.setVisibility(View.INVISIBLE);
                lv_event.setVisibility(View.INVISIBLE);
                getData();
                break;
            case R.id.tv_event:
                lv_event.setVisibility(View.VISIBLE);
                lv_group.setVisibility(View.INVISIBLE);
                lv_hot.setVisibility(View.INVISIBLE);
                break;
            case R.id.tv_group:
                lv_group.setVisibility(View.VISIBLE);
                lv_hot.setVisibility(View.INVISIBLE);
                lv_event.setVisibility(View.INVISIBLE);
                getGroupData();
                break;
        }
    }
}
