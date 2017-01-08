package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hopeofseed.hopeofseed.Adapter.CropDataAdapter;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.SortsData;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.SortsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.CategoryTabStripNoPager;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.lv_list;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/6 9:38
 * 修改人：whisper
 * 修改时间：2016/10/6 9:38
 * 修改备注：
 */
public class SelectVarieties extends AppCompatActivity implements View.OnClickListener, NetCallBack, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "SelectVarieties";
    CropDataAdapter mCropDataAdapter;
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    static String Str_search = "";
    CategoryTabStripNoPager tabs;
    private ArrayList<String> catalogs = new ArrayList<String>();
    private int PageNo = 0;
    ArrayList<SortsData> arr_SortsDataTmp = new ArrayList<>();
    EditText search_et_input;
    private boolean isSearch = false;
    RecyclerView recy_list;
    private SwipeRefreshLayout mRefreshLayout;
    Handler mHandler = new Handler();
    boolean isLoading = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_varieties);
        initData();
        initView();
        initList();
        getHotSortData();
    }

    private void initData() {
        catalogs.add("全部");
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("找品种");
        Button btn_search = (Button) findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
        search_et_input = (EditText) findViewById(R.id.search_et_input);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        tabs = (CategoryTabStripNoPager) findViewById(R.id.category_strip);
        tabs.setData(catalogs);
        tabs.setOnSelectListener(new CategoryTabStripNoPager.OnSelectListener() {
            @Override
            public void onSelect(int position) {
                PageNo = 0;
                Log.e(TAG, "updateSelect: " + position);
                Str_search = catalogs.get(position).trim();
                getData(catalogs.get(position).trim());
            }
        });
    }


    private void initList() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        //这个是下拉刷新出现的那个圈圈要显示的颜色
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(SelectVarieties.this);
        recy_list.setLayoutManager(manager);
        mCropDataAdapter = new CropDataAdapter(SelectVarieties.this, arr_CropData,true);
        recy_list.setAdapter(mCropDataAdapter);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_search:
                searchThisList();
                break;
        }
    }

    private void searchThisList() {
        Str_search = search_et_input.getText().toString().trim();
     /*   if (Str_search.equals("")) {*/
        PageNo = 0;
        isSearch = true;
        getData(Str_search);
/*        }*/

    }

    private void getHotSortData() {
        Log.e(TAG, "getData: 获取热门分类数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSortsData.php", opt_map, SortsDataTmp.class, this);
    }

    private void getData(String Str_search) {
        Log.e(TAG, "getData: 获取品种数据");
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("StrSearch", Str_search);
        opt_map.put("PageNo", String.valueOf(PageNo));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetSearchCropResult.php", opt_map, CropDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {

        if (rspBaseBean.RequestSign.equals("GetSortsData")) {
            SortsDataTmp mSortsDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_SortsDataTmp = mSortsDataTmp.getDetail();
            for (int i = 0; i < arr_SortsDataTmp.size(); i++) {
                catalogs.add(arr_SortsDataTmp.get(i).getVarietyname());
            }
            mHandler.post(updateTabs);
        } else if (rspBaseBean.RequestSign.equals("GetSearchCropResult")) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
            mHandler.post(upadteList);
        }
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateTabs = new Runnable() {
        @Override
        public void run() {
            if (catalogs.size() > 0) {
                tabs.setData(catalogs);
            }
            Str_search = catalogs.get(0);
            getData(Str_search);
        }
    };

    Runnable upadteList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_CropData.clear();
                arr_CropData.addAll(arr_CropDataTmp);
                mCropDataAdapter.notifyDataSetChanged();
            } else {
                arr_CropData.addAll(arr_CropDataTmp);
                mCropDataAdapter.notifyDataSetChanged();
            }
            if (isSearch) {
                if (!(arr_CropDataTmp.size() == 0)) {
                    catalogs.clear();
                    for (int i = 0; i < arr_CropDataTmp.size(); i++) {
                        if (i == 0) {
                            catalogs.add(arr_CropDataTmp.get(i).getCropCategory2());
                        } else {
                            if (!catalogs.contains(arr_CropDataTmp.get(i).getCropCategory2())) {
                                catalogs.add(arr_CropDataTmp.get(i).getCropCategory2());
                            }
                        }
                    }
                    tabs.setData(catalogs);
                    isSearch = false;
                }
            }
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
