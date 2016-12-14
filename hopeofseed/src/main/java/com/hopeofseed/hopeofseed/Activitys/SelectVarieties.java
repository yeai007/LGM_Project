package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/10/6 9:38
 * 修改人：whisper
 * 修改时间：2016/10/6 9:38
 * 修改备注：
 */
public class SelectVarieties extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "SelectVarieties";
    PullToRefreshListView lv_list;
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
        lv_list = (PullToRefreshListView) findViewById(R.id.lv_list);
        mCropDataAdapter = new CropDataAdapter(getApplicationContext(), arr_CropData);
        lv_list.setAdapter(mCropDataAdapter);
        lv_list.setMode(PullToRefreshBase.Mode.BOTH);
        lv_list.setOnItemClickListener(myListener);
    }

    private AdapterView.OnItemClickListener myListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(SelectVarieties.this, SearchAcitvity.class);
            intent.putExtra("FirstShow","Crop");
            intent.putExtra("StrSearch", arr_CropDataTmp.get(i - 1).getVarietyName());
            startActivity(intent);
        }
    };

    private void initList() {
//        getNewsData();
        lv_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String str = DateUtils.formatDateTime(getApplicationContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                // 下拉刷新 业务代码
                if (refreshView.isShownHeader()) {
                    lv_list.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                    lv_list.getLoadingLayoutProxy().setPullLabel("下拉刷新");
                    lv_list.getLoadingLayoutProxy().setReleaseLabel("释放开始刷新");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新时间:" + str);
                    PageNo = 0;
                    getData(Str_search);
                }
                // 上拉加载更多 业务代码
                if (refreshView.isShownFooter()) {
                    lv_list.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                    lv_list.getLoadingLayoutProxy().setPullLabel("上拉加载更多");
                    lv_list.getLoadingLayoutProxy().setReleaseLabel("释放加载更多");
                    refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后加载时间:" + str);
                    PageNo = PageNo + 1;
                    getData(Str_search);
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
            updateTabs();
        } else if (rspBaseBean.RequestSign.equals("GetSearchCropResult")) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
            updateView();
        }
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateView() {
        Message msg = updateViewHandle.obtainMessage();
        msg.sendToTarget();
    }

    private void updateTabs() {
        Message msg = updateTabsHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateTabsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (catalogs.size() > 0) {
                tabs.setData(catalogs);
            }
            Str_search = catalogs.get(0);
            getData(Str_search);
        }
    };
    private Handler updateViewHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (PageNo == 0) {
                arr_CropData.clear();
                arr_CropData.addAll(arr_CropDataTmp);
                mCropDataAdapter.notifyDataSetChanged();
                lv_list.onRefreshComplete();
            } else {
                arr_CropData.addAll(arr_CropDataTmp);
                mCropDataAdapter.notifyDataSetChanged();
                lv_list.onRefreshComplete();
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
        }
    };

    public void Search(String text) {
        Str_search = text;
        getData(Str_search);
    }
}
