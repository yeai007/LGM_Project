package com.hopeofseed.hopeofseed.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import com.lgm.utils.CalculationUtil;
import com.lgm.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmResults;


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
    ArrayList<TreeNode> topNodes = new ArrayList<TreeNode>();
    ArrayList<TreeNode> allNodes = new ArrayList<TreeNode>();
    TreeViewAdapter treeViewAdapter;
    String StrSp1 = "", StrSp2 = "", StrSp3 = "";
    Button btn_search;
    String StrSp1Tmp = "", StrSp2Tmp = "", StrSp3Tmp = "";
    TextView tv_other_sum, tv_zone_sum, tv_city_sum, tv_province_sum,tv_sum_4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.distributor_count_report_activity);
        initView();
        initTree();
        initTop();
        getCommodityClassData();
        getCommodityAddressData();
        getClassCount();
    }

    private void initTree() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListView treeview = (ListView) findViewById(R.id.tree_list);
        treeViewAdapter = new TreeViewAdapter(DistributorCountReportActivity.this, topNodes, allNodes, inflater);
        treeview.setAdapter(treeViewAdapter);
    }

    private void initView() {
        spinner_1 = (Button) findViewById(R.id.spinner_1);
        spinner_2 = (Button) findViewById(R.id.spinner_2);
        spinner_3 = (Button) findViewById(R.id.spinner_3);
        btn_search = (Button) findViewById(R.id.btn_search);
        tv_other_sum = (TextView) findViewById(R.id.tv_other_sum);
        tv_zone_sum = (TextView) findViewById(R.id.tv_zone_sum);
        tv_city_sum = (TextView) findViewById(R.id.tv_city_sum);
        tv_province_sum = (TextView) findViewById(R.id.tv_province_sum);
        tv_sum_4=(TextView)findViewById(R.id.tv_sum_4);
        tv_other_sum.setText("其他：0");
        tv_zone_sum.setText("镇级：0");
        tv_city_sum.setText("区县级：0");
        tv_province_sum.setText("市级：0");
        tv_province_sum.setText("省级：0");
        spinner_1.setOnClickListener(this);
        spinner_2.setOnClickListener(this);
        spinner_3.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        tv_other_sum.setOnClickListener(this);
        tv_zone_sum.setOnClickListener(this);
        tv_city_sum.setOnClickListener(this);
        tv_province_sum.setOnClickListener(this);
        (findViewById(R.id.btn_topright)).setOnClickListener(this);


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
        StrSp1Tmp = StrSp1;
        StrSp2Tmp = StrSp2;
        StrSp3Tmp = StrSp3;
        opt_map.put("StrSp1", StrSp1);
        opt_map.put("StrSp2", StrSp2);
        opt_map.put("StrSp3", StrSp3);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityAddressForReport.php", opt_map, CommodityAddressDataTmp.class, this);
    }

    private void getClassCount() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("StrSp1", StrSp1);
        opt_map.put("StrSp2", StrSp2);
        opt_map.put("StrSp3", StrSp3);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetDistributorCountByClass.php", opt_map, DistributorCountByClassTmp.class, this);
    }

    private void initTop() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("统计分析");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("重置");
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
            case R.id.btn_search:
                getCommodityAddressData();
                getClassCount();
                break;
            case R.id.btn_topright:
                spinner_1.setText("");
                spinner_2.setText("");
                spinner_3.setText("");
                StrSp1 = StrSp2 = StrSp3 = "";
                getCommodityAddressData();
                getClassCount();
                break;
            case R.id.tv_other_sum:
                intent = new Intent(DistributorCountReportActivity.this, DistributorListForReport.class);
                intent.putExtra("Class", 0);
                intent.putExtra("ClassId", 0);
                startActivity(intent);
                break;
            case R.id.tv_zone_sum:
                intent = new Intent(DistributorCountReportActivity.this, DistributorListForReport.class);
                intent.putExtra("Class", 0);
                intent.putExtra("ClassId", 1);
                startActivity(intent);
                break;
            case R.id.tv_city_sum:
                intent = new Intent(DistributorCountReportActivity.this, DistributorListForReport.class);
                intent.putExtra("Class", 0);
                intent.putExtra("ClassId", 2);
                startActivity(intent);
                break;
            case R.id.tv_province_sum:
                intent = new Intent(DistributorCountReportActivity.this, DistributorListForReport.class);
                intent.putExtra("Class", 0);
                intent.putExtra("ClassId", 3);
                startActivity(intent);
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
                    StrSp1 = data.getStringExtra("item").trim();
                } else if (requestCode == 1) {
                    spinner_2.setText(data.getStringExtra("item"));
                    StrSp2 = data.getStringExtra("item").trim();
                } else if (requestCode == 2) {
                    spinner_3.setText(data.getStringExtra("item"));
                    StrSp3 = data.getStringExtra("item").trim();
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
            //Toast.makeText(getApplicationContext(), "更新数据成功", Toast.LENGTH_SHORT).show();
        }
    };
    Runnable updatAddress = new Runnable() {
        @Override
        public void run() {
            commodityAddressDataArrayList.clear();
            commodityAddressDataArrayList.addAll(commodityAddressDataArrayListTmp);
            int sumCount = 0;
            for (int i = 0; i < commodityAddressDataArrayList.size(); i++) {
                sumCount = sumCount + commodityAddressDataArrayList.get(i).getCount();
            }
            topNodes.clear();
            allNodes.clear();
            for (int i = 0; i < commodityAddressDataArrayList.size(); i++) {
                CommodityAddressData itemData = commodityAddressDataArrayList.get(i);
                if (i == 0) {
                    TreeNode node = new TreeNode(itemData.getDistributorProvince() + "  数量：" + itemData.getProvinceCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getProvinceCount(), sumCount, 4)), TreeNode.TOP_LEVEL, itemData.getDistributorProvinceId(), TreeNode.NO_PARENT, true, false);
                    TreeNode node1 = new TreeNode(itemData.getDistributorCity() + "  数量：" + itemData.getCityCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCityCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 1, itemData.getDistributorCityId(), node.getId(), true, false);
                    TreeNode node2 = new TreeNode(itemData.getDistributorZone() + "  数量：" + itemData.getCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 2, itemData.getDistributorZoneId(), node1.getId(), false, false);
                    topNodes.add(node);
                    allNodes.add(node);
                    allNodes.add(node1);
                    allNodes.add(node2);
                } else {
                    CommodityAddressData lastData = commodityAddressDataArrayList.get(i - 1);
                    if (itemData.getDistributorProvince().equals(lastData.getDistributorProvince())) {
                        if (itemData.getDistributorCity().equals(lastData.getDistributorCity())) {
                            TreeNode node1 = new TreeNode(itemData.getDistributorZone() + "  数量：" + itemData.getCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 2, itemData.getDistributorZoneId(), lastData.getDistributorCityId(), false, false);
                            allNodes.add(node1);
                        } else {
                            TreeNode node = new TreeNode(itemData.getDistributorCity() + "  数量：" + itemData.getCityCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCityCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 1, itemData.getDistributorCityId(), lastData.getDistributorProvinceId(), true, false);
                            TreeNode node1 = new TreeNode(itemData.getDistributorZone() + "  数量：" + itemData.getCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 2, itemData.getDistributorZoneId(), node.getId(), false, false);
                            allNodes.add(node);
                            allNodes.add(node1);
                        }
                    } else {
                        TreeNode node = new TreeNode(itemData.getDistributorProvince() + "  数量：" + itemData.getProvinceCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getProvinceCount(), sumCount, 4)), TreeNode.TOP_LEVEL, itemData.getDistributorProvinceId(), TreeNode.NO_PARENT, true, false);
                        TreeNode node1 = new TreeNode(itemData.getDistributorCity() + "  数量：" + itemData.getCityCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCityCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 1, itemData.getDistributorCityId(), node.getId(), true, false);
                        TreeNode node2 = new TreeNode(itemData.getDistributorZone() + "  数量：" + itemData.getCount() + "  占比" + CalculationUtil.StringToPercent(CalculationUtil.IntDivide(itemData.getCount(), sumCount, 4)), TreeNode.TOP_LEVEL + 2, itemData.getDistributorZoneId(), node1.getId(), false, false);
                        topNodes.add(node);
                        allNodes.add(node);
                        allNodes.add(node1);
                        allNodes.add(node2);
                    }
                }
            }
            treeViewAdapter.notifyDataSetChanged();
        }
    };

    Runnable updatClassCount = new Runnable() {
        @Override
        public void run() {
            int all_sum = 0;
            if (arrDistributorCountByClass.size() == 0) {

                tv_other_sum.setText("其他：0");

                tv_zone_sum.setText("镇级：0");

                tv_city_sum.setText("区县级：0");

                tv_province_sum.setText("市级：0");
                tv_sum_4.setText("省级：0");
            }
            for (int i = 0; i < arrDistributorCountByClass.size(); i++) {
                switch (Integer.parseInt(arrDistributorCountByClass.get(i).getDistributorLevel())) {
                    case 0:
                        tv_other_sum.setText("其他：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 1:
                        tv_zone_sum.setText("镇级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 2:
                        tv_city_sum.setText("区县级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 3:
                        tv_province_sum.setText("市级：" + arrDistributorCountByClass.get(i).getCount());
                        all_sum = all_sum + Integer.parseInt(arrDistributorCountByClass.get(i).getCount());
                        break;
                    case 4:
                        tv_sum_4.setText("省级：" + arrDistributorCountByClass.get(i).getCount());
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

    public String[] GetCondition() {
        String userid = String.valueOf(Const.currentUser.user_id);
        if (TextUtils.isEmpty(String.valueOf(Const.currentUser.user_id))) {
            userid = "";
        }
        if (TextUtils.isEmpty(StrSp1Tmp)) {
            StrSp1Tmp = "";
        }
        if (TextUtils.isEmpty(StrSp2Tmp)) {
            StrSp2Tmp = "";
        }
        if (TextUtils.isEmpty(StrSp3Tmp)) {
            StrSp3Tmp = "";
        }
        String[] data = {userid, StrSp1Tmp, StrSp2Tmp, StrSp3Tmp};
        return data;
    }
}
