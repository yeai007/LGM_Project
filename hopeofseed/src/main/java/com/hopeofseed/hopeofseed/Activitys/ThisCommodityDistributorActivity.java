package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.CommodityDistributorAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.DistributorData;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorDataTmp;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.hopeofseed.hopeofseed.R.id.search_et_input;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2017/1/6 14:39
 * 修改人：whisper
 * 修改时间：2017/1/6 14:39
 * 修改备注：
 */
public class ThisCommodityDistributorActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recy_list;
    SwipeRefreshLayout mRefreshLayout;
    int PageNo = 0;
    Handler mHandler = new Handler();
    boolean isLoading = false;
    ArrayList<DistributorData> arr_DistributorData = new ArrayList<>();
    ArrayList<DistributorData> arr_DistributorDataTmp = new ArrayList<>();
    CommodityDistributorAdapter commodityDistributorAdapter;
    String CommodityId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.this_commodity_distributor_activity);
        Intent intent = getIntent();
        CommodityId = intent.getStringExtra("CommodityId");
        initView();
        initRecyle();
        getData();
    }

    private void initRecyle() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout_swipe_refresh);
        mRefreshLayout.setColorSchemeResources(
                R.color.colorRed,
                R.color.colorYellow,
                R.color.colorGreen
        );
        mRefreshLayout.setOnRefreshListener(this);
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        final LinearLayoutManager manager = new LinearLayoutManager(ThisCommodityDistributorActivity.this);
        recy_list.setLayoutManager(manager);
        commodityDistributorAdapter = new CommodityDistributorAdapter(ThisCommodityDistributorActivity.this, arr_DistributorData, CommodityId);
        recy_list.setAdapter(commodityDistributorAdapter);
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

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("维护经销商");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("新添加");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                Intent intent = new Intent(ThisCommodityDistributorActivity.this, SettingDistributorActivity.class);
                intent.putExtra("CommodityId", CommodityId);
                startActivity(intent);
                break;


        }
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", CommodityId);
        opt_map.put("PageNo", String.valueOf(PageNo));


        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorByCommodityId.php", opt_map, DistributorDataTmp.class, new NetCallBack() {
            @Override
            public void onSuccess(RspBaseBean rspBaseBean) {
                arr_DistributorDataTmp = ((DistributorDataTmp) rspBaseBean).getDetail();
                mHandler.post(updateList);
            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFail() {

            }
        });
    }

    Runnable updateList = new Runnable() {
        @Override
        public void run() {
            if (PageNo == 0) {
                arr_DistributorData.clear();
            }
            arr_DistributorData.addAll(arr_DistributorDataTmp);
            commodityDistributorAdapter.notifyDataSetChanged();
            mRefreshLayout.setRefreshing(false);
        }
    };

    @Override
    public void onRefresh() {
        getData();
    }
}
