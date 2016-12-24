package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.AuthorDataAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.AuthorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.AuthorDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.ObjectUtil;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/7 20:30
 * 修改人：whisper
 * 修改时间：2016/10/7 20:30
 * 修改备注：
 */
public class AuthorListFragment extends Fragment implements NetCallBack {
    private static final String TAG = "AuthorListFragment";
    PullToRefreshListView lv_distributor;
    AuthorDataAdapter mDistributorAdapter;
    ArrayList<AuthorData> arrAuthorData = new ArrayList<>();
    ArrayList<AuthorData> arrAuthorDataTmp = new ArrayList<>();
    Handler mHandle = new Handler();
    int ClassId = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.author_fragment, null);
        initView(v);
        getData();
        return v;
    }

    private void initView(View v) {
        lv_distributor = (PullToRefreshListView) v.findViewById(R.id.lv_list);
        mDistributorAdapter = new AuthorDataAdapter(getActivity(), arrAuthorData);
        lv_distributor.setAdapter(mDistributorAdapter);
        lv_distributor.setOnItemClickListener(myListener);
        initList();
    }
    private void initList() {
        lv_distributor.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_distributor.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_distributor.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_distributor.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    getData();
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_distributor.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_distributor.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_distributor.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    getData();
                }

            }
        });
    }
    private void getData() {
        Log.e(TAG, "getData: 获取经销商数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("Range", "50000");
        opt_map.put("ClassId",String.valueOf(ClassId));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetAuthorDataByClass.php", opt_map, AuthorDataTmp.class, this);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(getActivity(), UserActivity.class);
            intent.putExtra("userid", String.valueOf(arrAuthorData.get(i - 1).getUser_id()));
            intent.putExtra("UserRole", Integer.parseInt(arrAuthorData.get(i - 1).getUser_role()));
            startActivity(intent);
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        arrAuthorDataTmp = ((AuthorDataTmp) ObjectUtil.cast(rspBaseBean)).getDetail();
        mHandle.post(updateList);
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
            arrAuthorData.clear();
            arrAuthorData.addAll(arrAuthorDataTmp);
            mDistributorAdapter.notifyDataSetChanged();
        }
    };
    public void setUserLoc() {
        getData();
    }

    public void setClass(int classId) {
        ClassId=classId;
    }

    public void refreshData() {
        getData();
    }
}
