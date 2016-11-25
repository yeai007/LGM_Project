package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hopeofseed.hopeofseed.Adapter.AutoTextDataAdapter;
import com.hopeofseed.hopeofseed.Adapter.Sp_VarietyAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.AddNewCommodity;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.CommodityVarietyData;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXData.SortsData;
import com.hopeofseed.hopeofseed.JNXData.VarietyData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityVarietyDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CropDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.SortsDataTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.GetImagePath;
import com.lgm.utils.ObjectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static cn.jpush.im.android.api.model.UserInfo.Gender.male;
import static com.hopeofseed.hopeofseed.Activitys.PubishMainActivity.verifyStoragePermissions;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/18 11:06
 * 修改人：whisper
 * 修改时间：2016/9/18 11:06
 * 修改备注：
 */
public class AddCommodity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "AddCommodity";
    EditText et_name, et_title, et_discribe, et_price;
    CommodityData comodityData = new CommodityData();
    private RecyclerView resultRecyclerView;
    private ArrayList<String> images = new ArrayList<>();
    GridAdapter gridAdapter;
    public static final int TO_SELECT_PHOTO = 3;
    private String mPicturePath;
    public static Bitmap mBitmap;
    String commodityId;
    boolean isAdd = true;
    CommodityData commodityData = new CommodityData();
    AutoCompleteTextView et_variety;
    ArrayList<CropData> arr_CropData = new ArrayList<>();
    ArrayList<CropData> arr_CropDataTmp = new ArrayList<>();
    AutoTextDataAdapter mAutoTextDataAdapter;
    RadioGroup rg_class;
    RadioButton rb_seed, rb_fertilizer, rb_pesticide;
    String commodityclass = "种子";
    Spinner sp_variety_1, sp_variety_2;
    CommodityVarietyData mCommodityVarietyData = new CommodityVarietyData();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData = new ArrayList<>();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_1 = new ArrayList<>();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_2 = new ArrayList<>();
    ArrayList<CommodityVarietyData> arr_Variety_Data_2 = new ArrayList<>();
    Sp_VarietyAdapter sp_VarietyAdapter_1, sp_VarietyAdapter_2;
    String varietyid;
    String Variety_1, Variety_2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_commodity);
        Intent intent = getIntent();
        commodityId = intent.getStringExtra("commodityId");
        if (commodityId.equals("0")) {
            isAdd = true;
        } else {
            isAdd = false;
            String data = intent.getStringExtra("com_data");
            commodityData = new Gson().fromJson(data, CommodityData.class);
        }
        initView();
        initData();
    }

    private void initView() {
        rg_class = (RadioGroup) findViewById(R.id.rg_class);
        rg_class.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
        rb_seed = (RadioButton) findViewById(R.id.rb_seed);
        rb_fertilizer = (RadioButton) findViewById(R.id.rb_fertilizer);
        rb_pesticide = (RadioButton) findViewById(R.id.rb_pesticide);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setText("添加");
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_title = (EditText) findViewById(R.id.et_title);
        et_discribe = (EditText) findViewById(R.id.et_discribe);
        et_price = (EditText) findViewById(R.id.et_price);
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("添加商品");

        images.add("add");
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        gridAdapter = new GridAdapter();
        resultRecyclerView.setAdapter(gridAdapter);
        if (!isAdd) {
            updateView();
        }
        et_variety = (AutoCompleteTextView) findViewById(R.id.et_variety);
        mAutoTextDataAdapter = new AutoTextDataAdapter(getApplicationContext(), arr_CropData);
        et_variety.setAdapter(mAutoTextDataAdapter);

        //品种分类添加
        sp_variety_1 = (Spinner) findViewById(R.id.sp_variety_1);
        sp_variety_2 = (Spinner) findViewById(R.id.sp_variety_2);
        sp_VarietyAdapter_1 = new Sp_VarietyAdapter(getApplicationContext(), arrCommodityVarietyData_1);
        sp_VarietyAdapter_2 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_2);
        sp_variety_1.setAdapter(sp_VarietyAdapter_1);
        sp_variety_2.setAdapter(sp_VarietyAdapter_2);
        sp_variety_1.setOnItemSelectedListener(spTitleListener_1);
        sp_variety_2.setOnItemSelectedListener(spTitleListener_2);
        getVarietyData();
    }

    AdapterView.OnItemSelectedListener spTitleListener_1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Variety_1 = arrCommodityVarietyData_1.get(i).getVarietyname();
            Log.e(TAG, "onItemSelected: Variety_1" + arrCommodityVarietyData_1.get(i).getVarietyname());
            arr_Variety_Data_2.clear();
            for (int j = 0; j < arrCommodityVarietyData_2.size(); j++) {
                CommodityVarietyData varietyData = new CommodityVarietyData();
                varietyData = arrCommodityVarietyData_2.get(j);
                if (Integer.parseInt(varietyData.getVarietyclassid()) == Integer.parseInt(arrCommodityVarietyData_1.get(i).getVarietyid())) {
                    arr_Variety_Data_2.add(varietyData);
                }
            }
            Log.e(TAG, "onItemSelected: " + arr_Variety_Data_2.size());
//            sp_VarietyAdapter_2 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_2);
            sp_VarietyAdapter_2.notifyDataSetChanged();
            //sp_variety_2.setAdapter(sp_VarietyAdapter_2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener spTitleListener_2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Variety_2 = arrCommodityVarietyData_2.get(i).getVarietyname();
            varietyid = arr_Variety_Data_2.get(i).getVarietyid();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private void getVarietyData() {

        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getVariety.php", opt_map, CommodityVarietyDataTmp.class, this);
    }

    private class OnCheckedChangeListenerImp implements RadioGroup.OnCheckedChangeListener {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String temp = null;
            if (rb_seed.getId() == checkedId) {
                commodityclass = "种子";
            } else if (rb_fertilizer.getId() == checkedId) {
                commodityclass = "农药";
            } else if (rb_pesticide.getId() == checkedId) {
                commodityclass = "化肥";
            }

        }
    }

    private void updateView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_title = (EditText) findViewById(R.id.et_title);
        et_discribe = (EditText) findViewById(R.id.et_discribe);
        et_price = (EditText) findViewById(R.id.et_price);
        et_name.setText(commodityData.getCommodityName());
        et_title.setText(commodityData.getCommodityTitle());
        et_discribe.setText(commodityData.getCommodityDescribe());
        et_price.setText(commodityData.getCommodityPrice());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topright:
                addNewCommodity();
                break;

            case R.id.btn_topleft:
                finish();
                break;
        }
    }

    private void addNewCommodity() {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                AddNewCommodity addNewCommodity = new AddNewCommodity();
                addNewCommodity.commodity_title = et_title.getText().toString();
                addNewCommodity.commodity_name = et_name.getText().toString();
                addNewCommodity.commodity_price = et_price.getText().toString();
                addNewCommodity.commodity_describe = et_discribe.getText().toString();
                addNewCommodity.commodity_variety = et_variety.getText().toString();
                addNewCommodity.commodity_class = commodityclass;
                addNewCommodity.images = images;
                addNewCommodity.Variety_1 = Variety_1;
                addNewCommodity.Variety_2 = Variety_2;
                Boolean bRet = addNewCommodity.UploadImg();
                Message msg = addNewCommodityHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler addNewCommodityHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    finish();
                    break;
                case 1:
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            verifyStoragePermissions(this);


            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);


            images.add(mPicturePath);
            gridAdapter.notifyDataSetChanged();
            Log.e(TAG, "onActivityResult: " + images);
        }
    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

        @Override
        public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GridAdapter.ViewHolder holder, int position) {
            if (images.get(position).equals("add")) {
                Glide.with(AddCommodity.this)
                        .load(R.drawable.add_img)
                        .centerCrop()
                        .into(holder.imageView);
            } else {
                Log.e(TAG, "onBindViewHolder: " + images.get(position));
                Glide.with(AddCommodity.this)
                        .load(new File(images.get(position)))
                        .centerCrop()
                        .into(holder.imageView);
            }

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
                imageView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), images.get(getPosition()), Toast.LENGTH_SHORT).show();
                if (images.get(getPosition()).equals("add")) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, TO_SELECT_PHOTO);
                }
            }
        }
    }

    private void initData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetVarietyAutoData.php", opt_map, CropDataTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetVarietyAutoData")) {
            CropDataTmp mCropDataTmp = ObjectUtil.cast(rspBaseBean);
            arr_CropDataTmp = mCropDataTmp.getDetail();
            updateSorts();

        } else if (rspBaseBean.RequestSign.equals("AddNewYieldData")) {
            if (rspBaseBean.resultNote.equals("success")) {
                Log.e(TAG, "onSuccess: success");
                this.finish();
            } else {
                Log.e(TAG, "onSuccess: failed");
                this.finish();
            }
        } else if (rspBaseBean.RequestSign.equals("getVariety")) {
            // Log.e(TAG, "onSuccess: " + rspBaseBean);
            CommodityVarietyDataTmp mCommodityVarietyDataTmp = ObjectUtil.cast(rspBaseBean);
            arrCommodityVarietyData = mCommodityVarietyDataTmp.getDetail();
            updateSpinner();
        }
    }


    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    private void updateSorts() {
        Message msg = updateSortsHandle.obtainMessage();
        msg.sendToTarget();
    }

    private void updateSpinner() {
        Message msg = updateSpinnerHandle.obtainMessage();
        msg.sendToTarget();
    }

    private Handler updateSortsHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "handleMessage: updateview");
            arr_CropData.clear();
            arr_CropData.addAll(arr_CropDataTmp);
            Log.e(TAG, "handleMessage: updateview" + arr_CropData.size());
            mAutoTextDataAdapter.notifyDataSetChanged();

        }
    };
    private Handler updateSpinnerHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ArrayList<CommodityVarietyData> arrCommodityVarietyData_1_tmp = new ArrayList<>();
            ArrayList<CommodityVarietyData> arrCommodityVarietyData_2_tmp = new ArrayList<>();
            for (int i = 0; i < arrCommodityVarietyData.size(); i++) {
                CommodityVarietyData variety = new CommodityVarietyData();
                variety = arrCommodityVarietyData.get(i);
                if (Integer.parseInt(variety.getVarietyclassid()) == 1) {
                    arrCommodityVarietyData_1_tmp.add(variety);
                }
                if (Integer.parseInt(variety.getVarietyclassid()) > 1) {
                    arrCommodityVarietyData_2_tmp.add(variety);
                }
            }
            arrCommodityVarietyData_1.clear();
            arrCommodityVarietyData_1.addAll(arrCommodityVarietyData_1_tmp);
            arrCommodityVarietyData_2.clear();
            arrCommodityVarietyData_2.addAll(arrCommodityVarietyData_2_tmp);
            Log.e(TAG, "handleMessage: " + arrCommodityVarietyData_1 + arrCommodityVarietyData_2);
            /*sp_VarietyAdapter_1 = new Sp_VarietyAdapter(getApplicationContext(), arrCommodityVarietyData_1);
            sp_variety_1.setAdapter(sp_VarietyAdapter_1);*/
            sp_VarietyAdapter_1.notifyDataSetChanged();
        }

    };
}
