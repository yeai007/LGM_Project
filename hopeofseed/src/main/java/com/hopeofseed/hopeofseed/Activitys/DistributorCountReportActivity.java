package com.hopeofseed.hopeofseed.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Adapter.DistributorCountAddressAdapter;
import com.hopeofseed.hopeofseed.Adapter.TreeViewAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityAddressData;
import com.hopeofseed.hopeofseed.JNXData.CommodityClassData;
import com.hopeofseed.hopeofseed.JNXData.DistributorCountByClass;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityAddressDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityClassDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.DistributorCountByClassTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.curView.SelectRadioActivity;
import com.hopeofseed.hopeofseed.curView.TreeNode;
import com.hopeofseed.hopeofseed.curView.TreeViewItemClickListener;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.hopeofseed.hopeofseed.R.id.recycler_list;
import static com.hopeofseed.hopeofseed.R.id.top;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/29 16:12
 * 修改人：whisper
 * 修改时间：2016/12/29 16:12
 * 修改备注：
 */
public class DistributorCountReportActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "DistributorCountReportA";
    ArrayList<CommodityClassData> commodityClassDataArrayList = new ArrayList<>();
    ArrayList<CommodityAddressData> commodityAddressDataArrayList = new ArrayList<>();
    ArrayList<CommodityAddressData> commodityAddressDataArrayListTmp = new ArrayList<>();
    ArrayList<DistributorCountByClass> arrDistributorCountByClass = new ArrayList<>();
    Handler handler = new Handler();
    Button spinner_1, spinner_2, spinner_3;
    // RecyclerView recycler_list;
    //   DistributorCountAddressAdapter distributorCountAddressAdapter;
    ArrayList<TreeNode> topNodes = new ArrayList<TreeNode>();
    ArrayList<TreeNode> allNodes = new ArrayList<TreeNode>();
    TreeViewAdapter treeViewAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributor_count_report_activity);
        initView();
        /*initAddressList();*/
        initTree();
        initTop();
        getCommodityClassData();
        getCommodityAddressData();
        getClassCount();
    }

    private void initTree() {

//		allNodes.add(node7);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        ListView treeview = (ListView) findViewById(R.id.tree_list);
        treeViewAdapter = new TreeViewAdapter(topNodes, allNodes, inflater);
        TreeViewItemClickListener treeViewItemClickListener = new TreeViewItemClickListener(treeViewAdapter);
        treeview.setAdapter(treeViewAdapter);
        treeview.setOnItemClickListener(treeViewItemClickListener);


    }

   /* private void initAddressList() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(DistributorCountReportActivity.this);

        recycler_list = (RecyclerView) findViewById(recycler_list);
        recycler_list.setLayoutManager(layoutManager);
        distributorCountAddressAdapter = new DistributorCountAddressAdapter(DistributorCountReportActivity.this, commodityAddressDataArrayList);
        recycler_list.setAdapter(distributorCountAddressAdapter);
    }*/


    private void initView() {
        spinner_1 = (Button) findViewById(R.id.spinner_1);
        spinner_2 = (Button) findViewById(R.id.spinner_2);
        spinner_3 = (Button) findViewById(R.id.spinner_3);
        spinner_1.setOnClickListener(this);
        spinner_2.setOnClickListener(this);
        spinner_3.setOnClickListener(this);
    }

    private void getCommodityClassData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityClassForReport.php", opt_map, CommodityClassDataTmp.class, this);
    }

    private void getCommodityAddressData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityAddressForReport.php", opt_map, CommodityAddressDataTmp.class, this);
    }

    private void getClassCount() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorCountByClass.php", opt_map, DistributorCountByClassTmp.class, this);
    }

    private void initTop() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("统计分析");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("更新");
        btn_topright.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.spinner_1:
                intent = new Intent(DistributorCountReportActivity.this, SelectRadioActivity.class);
                intent.putExtra("type", 0);
                startActivityForResult(intent, 0);
                break;
            case R.id.spinner_2:
                intent = new Intent(DistributorCountReportActivity.this, SelectRadioActivity.class);
                intent.putExtra("type", 1);
                if (!TextUtils.isEmpty(spinner_1.getText().toString())) {
                    intent.putExtra("condition", spinner_1.getText().toString().trim());
                }
                startActivityForResult(intent, 1);
                break;
            case R.id.spinner_3:
                intent = new Intent(DistributorCountReportActivity.this, SelectRadioActivity.class);
                if (!TextUtils.isEmpty(spinner_2.getText().toString())) {
                    intent.putExtra("condition", spinner_2.getText().toString().trim());
                }
                intent.putExtra("type", 2);
                startActivityForResult(intent, 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Log.e(TAG, "onActivityResult: 有返回" + data.getStringExtra("item"));
                if (requestCode == 0) {
                    spinner_1.setText(data.getStringExtra("item"));
                } else if (requestCode == 1) {
                    spinner_2.setText(data.getStringExtra("item"));
                } else if (requestCode == 2) {
                    spinner_3.setText(data.getStringExtra("item"));
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetCommodityClassForReport")) {
            commodityClassDataArrayList = ((CommodityClassDataTmp) rspBaseBean).getDetail();
            updateRealmData(rspBaseBean);

        } else if (rspBaseBean.RequestSign.equals("GetCommodityAddressForReport")) {

            commodityAddressDataArrayListTmp = ((CommodityAddressDataTmp) rspBaseBean).getDetail();
            handler.post(updatAddress);
        } else if (rspBaseBean.RequestSign.equals("GetDistributorCountByClass")) {
            arrDistributorCountByClass = ((DistributorCountByClassTmp) rspBaseBean).getDetail();
            handler.post(updatClassCount);
        }
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    Runnable updateClass = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), "更新数据成功", Toast.LENGTH_SHORT).show();
        }
    };
    Runnable updatAddress = new Runnable() {
        @Override
        public void run() {
            //   commodityAddressDataArrayList.clear();
            commodityAddressDataArrayList.clear();
            commodityAddressDataArrayList.addAll(commodityAddressDataArrayListTmp);
            topNodes.clear();
            allNodes.clear();
            int nId = 0;
            for (int i = 0; i < commodityAddressDataArrayList.size(); i++) {
                CommodityAddressData itemData = commodityAddressDataArrayList.get(i);
                if (i == 0) {
                    TreeNode node = new TreeNode(itemData.getDistributorProvince(), TreeNode.TOP_LEVEL, nId, TreeNode.NO_PARENT, true, false);
                    nId++;
                    TreeNode node1 = new TreeNode(itemData.getDistributorCity(), TreeNode.TOP_LEVEL + 1, nId, node.getId(), true, false);
                    nId++;
                    TreeNode node2 = new TreeNode(itemData.getDistributorZone(), TreeNode.TOP_LEVEL + 2, nId, node1.getId(), false, false);
                    nId++;
                    topNodes.add(node);
                    allNodes.add(node);
                    allNodes.add(node1);
                    allNodes.add(node2);
                } else {
                   /* CommodityAddressData lastData = commodityAddressDataArrayList.get(i - 1);
                    if (itemData.getDistributorProvince().equals(lastData.getDistributorProvince())) {
                        if (itemData.getDistributorCity().equals(lastData.getDistributorCity())) {
                            TreeNode node1 = new TreeNode(itemData.getDistributorZone(), TreeNode.TOP_LEVEL, nId, allNodes.get(allNodes.size() - 1).getParendId(), false, false);
                            nId++;
                            allNodes.add(node1);
                        } else {
                            TreeNode node = new TreeNode(itemData.getDistributorCity(), TreeNode.TOP_LEVEL, nId, Integer.parseInt(lastData.getId()), true, false);
                            nId++;
                            TreeNode node1 = new TreeNode(itemData.getDistributorZone(), TreeNode.TOP_LEVEL, nId, Integer.parseInt(itemData.getId()), false, false);
                            nId++;
                            allNodes.add(node);
                            allNodes.add(node1);
                        }
                    } else {
                        TreeNode node = new TreeNode(itemData.getDistributorProvince(), TreeNode.TOP_LEVEL, nId, TreeNode.NO_PARENT, true, false);
                        nId++;
                        TreeNode node1 = new TreeNode(itemData.getDistributorCity(), TreeNode.TOP_LEVEL, nId, Integer.parseInt(itemData.getId()), true, false);
                        nId++;
                        TreeNode node2 = new TreeNode(itemData.getDistributorZone(), TreeNode.TOP_LEVEL, nId, Integer.parseInt(itemData.getId()), false, false);
                        nId++;
                        topNodes.add(node);
                        allNodes.add(node);
                        allNodes.add(node1);
                        allNodes.add(node2);
                    }*/
                }
            }
            treeViewAdapter.notifyDataSetChanged();
        }
    };

    Runnable updatClassCount = new Runnable() {
        @Override
        public void run() {
            int all_sum = 0;
            for (int i = 0; i < arrDistributorCountByClass.size(); i++) {
                switch (i) {
                    case 0:
                        TextView tv_other_sum = (TextView) findViewById(R.id.tv_other_sum);
                        tv_other_sum.setText("其他：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 1:
                        TextView tv_zone_sum = (TextView) findViewById(R.id.tv_zone_sum);
                        tv_zone_sum.setText("镇级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 2:
                        TextView tv_city_sum = (TextView) findViewById(R.id.tv_city_sum);
                        tv_city_sum.setText("区县级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 3:
                        TextView tv_province_sum = (TextView) findViewById(R.id.tv_province_sum);
                        tv_province_sum.setText("市级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                }
            }
            TextView tv_sum = (TextView) findViewById(R.id.tv_sum);
            tv_sum.setText("经销商总数量：" + all_sum);
        }
    };

    private void updateRealmData(RspBaseBean rspBaseBean) {
        CommodityClassDataTmp newsDataTmp = new CommodityClassDataTmp();
        newsDataTmp = ObjectUtil.cast(rspBaseBean);
        Realm insertRealm = Realm.getDefaultInstance();
        RealmResults<CommodityClassData> results_del = insertRealm.where(CommodityClassData.class).findAll();
        insertRealm.beginTransaction();
        results_del.deleteAllFromRealm();
        insertRealm.commitTransaction();
        for (CommodityClassData o : newsDataTmp.getDetail()) {
            insertRealm.beginTransaction();
            CommodityClassData newdata = insertRealm.copyToRealmOrUpdate(o);
            insertRealm.commitTransaction();
        }
        handler.post(updateClass);
    }
}
