package com.hopeofseed.hopeofseed.curView;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hopeofseed.hopeofseed.Adapter.SelectRadioAdapter;
import com.hopeofseed.hopeofseed.JNXData.CommodityClassData;
import com.hopeofseed.hopeofseed.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/30 10:04
 * 修改人：whisper
 * 修改时间：2016/12/30 10:04
 * 修改备注：
 */
public class SelectRadioActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SelectRadioActivity";
    RecyclerView recy_list;
    SelectRadioAdapter selectRadioAdapter;
    ArrayList<String> arrCommSelectData = new ArrayList<>();
    Set<String> setCommSelectData = new HashSet<String>();
    int Type = 0;
    Realm myRealm = Realm.getDefaultInstance();
    String condition = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_radio_activity);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        Type = intent.getIntExtra("type", 0);
        condition = intent.getStringExtra("condition");
        RealmResults<CommodityClassData> results1 = null;
        switch (Type) {
            case 0:
                results1 = myRealm.where(CommodityClassData.class).findAll().sort("VC2COUNT", Sort.ASCENDING);
                break;
            case 1:
                if (!TextUtils.isEmpty(condition)) {
                    if (!condition.equals("全部")) {
                        results1 = myRealm.where(CommodityClassData.class).equalTo("CommodityVariety_2", condition.trim()).findAll().sort("VC1COUNT", Sort.ASCENDING);
                    } else {
                        results1 = myRealm.where(CommodityClassData.class).findAll().sort("VC1COUNT", Sort.ASCENDING);
                    }
                } else {
                    results1 = myRealm.where(CommodityClassData.class).findAll().sort("VC1COUNT", Sort.ASCENDING);
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(condition)) {
                    if (!condition.equals("全部")) {
                        results1 = myRealm.where(CommodityClassData.class).equalTo("CommodityVariety", condition.trim()).findAll().sort("VC1COUNT", Sort.ASCENDING);
                    } else {
                        results1 = myRealm.where(CommodityClassData.class).findAll().sort("VC1COUNT", Sort.ASCENDING);
                    }
                } else {
                    results1 = myRealm.where(CommodityClassData.class).findAll().sort("VC1COUNT", Sort.ASCENDING);
                }
                break;
        }
        setCommSelectData.clear();
        setCommSelectData.add("全部");
        if (Type == 0) {
            for (int i = 0; i < results1.size(); i++) {
/*                CommSelectData item = new CommSelectData();
                item.setContent(results1.get(i).getCommodityVariety_1());*/
                if (!setCommSelectData.contains(results1.get(i).getCommodityVariety_2())) {
                    setCommSelectData.add(results1.get(i).getCommodityVariety_2());
                }
            }
            arrCommSelectData.addAll(new ArrayList<String>(setCommSelectData));
        }
        if (Type == 1) {
            for (int i = 0; i < results1.size(); i++) {
/*                CommSelectData item = new CommSelectData();
                item.setContent(results1.get(i).getCommodityVariety_1());*/
                if (!setCommSelectData.contains(results1.get(i).getCommodityVariety())) {
                    setCommSelectData.add(results1.get(i).getCommodityVariety());
                }
            }
            arrCommSelectData.addAll(new ArrayList<String>(setCommSelectData));
        }
        if (Type == 2) {
            for (int i = 0; i < results1.size(); i++) {
/*                CommSelectData item = new CommSelectData();
                item.setContent(results1.get(i).getCommodityVariety_1());*/
                if (!setCommSelectData.contains(results1.get(i).getCommodityName())) {
                    setCommSelectData.add(results1.get(i).getCommodityName());
                }
            }
            arrCommSelectData.addAll(new ArrayList<String>(setCommSelectData));
        }
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("类别选择");
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        GridLayoutManager manager = new GridLayoutManager(SelectRadioActivity.this, 4);
        recy_list = (RecyclerView) findViewById(R.id.recy_list);
        recy_list.setLayoutManager(manager);
        selectRadioAdapter = new SelectRadioAdapter(SelectRadioActivity.this, arrCommSelectData);
        recy_list.setAdapter(selectRadioAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
        }
    }
}
