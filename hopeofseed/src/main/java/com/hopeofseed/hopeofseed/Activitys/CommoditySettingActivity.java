package com.hopeofseed.hopeofseed.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.hopeofseed.hopeofseed.Adapter.Sp_VarietyAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.CommodityData;
import com.hopeofseed.hopeofseed.JNXData.CommodityVarietyData;
import com.hopeofseed.hopeofseed.JNXData.CropData;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommodityVarietyDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.ui.iosDialog;
import com.hopeofseed.hopeofseed.util.GetImagePath;
import com.lgm.utils.AppPermissions;
import com.lgm.utils.ObjectUtil;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.shaohui.advancedluban.Luban;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.hopeofseed.hopeofseed.Application.REQUEST_CODE_FILES;


/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/16 17:13
 * 修改人：whisper
 * 修改时间：2016/12/16 17:13
 * 修改备注：
 */
public class CommoditySettingActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "CommoditySettingActivit";
    String CommodityId;
    CommodityData mCommodityData = new CommodityData();
    AutoCompleteTextView et_variety;
    Handler mHandler = new Handler();
    EditText et_name, et_title, et_price, et_discribe, et_order;
    RecyclerView result_recycler;
    private RecyclerView resultRecyclerView;
    private ArrayList<String> images = new ArrayList<>();
    GridAdapter gridAdapter;
    public static final int TO_SELECT_PHOTO = 3;
    private String mPicturePath;
    public static Bitmap mBitmap;
    int isAdd = 0;
    boolean isSp2Select = false;
    //***********
    Spinner sp_variety_1, sp_variety_2;
    ArrayList<CommodityVarietyData> arrCommodityVarietyData = new ArrayList<>();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_1 = new ArrayList<>();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_2 = new ArrayList<>();
    ArrayList<CommodityVarietyData> arr_Variety_Data_2 = new ArrayList<>();
    Sp_VarietyAdapter sp_VarietyAdapter_1, sp_VarietyAdapter_2;
    String varietyid;
    String Variety_1, Variety_2;
    String commodityclass = "种子";

    RadioGroup rg_class;
    RadioButton rb_seed, rb_fertilizer, rb_pesticide;
    //上传文件
    ArrayList<File> arrFile = new ArrayList<>();
    long start;
    pushFileResultTmp mCommResultTmp;
    String DelImage = "";
    pushFileResultTmp mCommResultTmp2 = new pushFileResultTmp();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_1_tmp = new ArrayList<>();
    ArrayList<CommodityVarietyData> arrCommodityVarietyData_2_tmp = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commodity_setting_activity);
        Intent intent = getIntent();
        CommodityId = intent.getStringExtra("CommodityId");
        initView();
        getData();
    }

    private void initView() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("商品修改");
        rg_class = (RadioGroup) findViewById(R.id.rg_class);
        rg_class.setOnCheckedChangeListener(new OnCheckedChangeListenerImp());
        rb_seed = (RadioButton) findViewById(R.id.rb_seed);
        rb_fertilizer = (RadioButton) findViewById(R.id.rb_fertilizer);
        rb_pesticide = (RadioButton) findViewById(R.id.rb_pesticide);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        (findViewById(R.id.del_this)).setOnClickListener(this);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setVisibility(View.VISIBLE);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("更新");
        et_variety = (AutoCompleteTextView) findViewById(R.id.et_variety);
        et_variety.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = parent.getItemAtPosition(position);
                CropData itemCropData = ObjectUtil.cast(obj);
                et_variety.setText(itemCropData.getVarietyName());
                Log.e(TAG, "onItemClick: " + itemCropData.getCropCategory1() + "--" + itemCropData.getCropCategory2());
                for (int i = 0; i < arrCommodityVarietyData_1.size(); i++) {
                    if (arrCommodityVarietyData_1.get(i).getVarietyname().trim().equals(itemCropData.getCropCategory1().trim())) {
                        sp_variety_1.setSelection(i);
                        break;
                    }
                }
                for (int i = 0; i < arr_Variety_Data_2.size(); i++) {
                    if (arr_Variety_Data_2.get(i).getVarietyname().trim().equals(itemCropData.getCropCategory2().trim())) {
                        sp_variety_2.setSelection(i);
                        break;
                    }
                }
            }
        });
        sp_variety_1 = (Spinner) findViewById(R.id.sp_variety_1);
        sp_variety_2 = (Spinner) findViewById(R.id.sp_variety_2);
        sp_VarietyAdapter_1 = new Sp_VarietyAdapter(getApplicationContext(), arrCommodityVarietyData_1);
        sp_VarietyAdapter_2 = new Sp_VarietyAdapter(getApplicationContext(), arr_Variety_Data_2);
        sp_variety_1.setAdapter(sp_VarietyAdapter_1);
        sp_variety_2.setAdapter(sp_VarietyAdapter_2);
        sp_variety_1.setOnItemSelectedListener(spTitleListener_1);
        sp_variety_2.setOnItemSelectedListener(spTitleListener_2);


        et_name = (EditText) findViewById(R.id.et_name);
        et_title = (EditText) findViewById(R.id.et_title);
        et_price = (EditText) findViewById(R.id.et_price);
        et_discribe = (EditText) findViewById(R.id.et_discribe);
        et_order = (EditText) findViewById(R.id.et_order);
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        gridAdapter = new GridAdapter();
        resultRecyclerView.setAdapter(gridAdapter);
    }

    private void getVarietyData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "getVariety.php", opt_map, CommodityVarietyDataTmp.class, this);
    }

    private void getData() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", CommodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "GetCommodityById.php", opt_map, CommodityDataTmp.class, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                try {
                    updateCommodity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.del_this:
                iosDialog mIosDialog = new iosDialog.Builder(CommoditySettingActivity.this)
                        .setMessage("确认删除该商品吗！\n删除商品会将该商品所有代理关系同步删除。")
                        .setPositiveButton("我要删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                deleteThis();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("种愿").create();
                mIosDialog.setCancelable(true);
                mIosDialog.show();
                break;
        }
    }

    private void deleteThis() {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", CommodityId);
        HttpUtils hu = new HttpUtils();
        hu.httpPost(Const.BASE_URL + "DeleteCommodityById.php", opt_map, pushFileResultTmp.class, this);
    }

    private void updateCommodity() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    Log.e(TAG, "run: " + images.get(i));
                    if (images.get(i).equals("add")) {
                    } else {
                        if (images.get(i).contains("http")) {
                            Log.e(TAG, "run: isHttp" + images.get(i));
                        } else {
                            arrFile.add(new File(images.get(i)));
                        }
                    }
                }
                if (arrFile.size() == 0) {
                    updateThisData(null);
                } else {
                    Luban.get(getApplicationContext()).setMaxSize(128)
                            .putGear(Luban.CUSTOM_GEAR)
                            .load(arrFile)                     // load all images
                            .asListObservable().doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            start = System.currentTimeMillis();
                            Log.e(TAG, "call: start" + System.currentTimeMillis());
                        }
                    })
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<List<File>>() {
                                @Override
                                public void call(final List<File> fileList) {
                                    for (int i = 0; i < fileList.size(); i++) {
                                        Log.e(TAG, "call: " + fileList.get(i).getName());
                                    }
                                    Log.e(TAG, "call: " + fileList);
                                    updateThisData(fileList);
                                }
                            });
                }

            }
        }).start();
    }

    private void updateThisData(List<File> fileList) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("CommodityId", mCommodityData.getCommodityId());
        opt_map.put("commodity_title", et_title.getText().toString());
        opt_map.put("commodity_name", et_name.getText().toString());
        opt_map.put("commodity_price", et_price.getText().toString());
        opt_map.put("commodity_describe", et_discribe.getText().toString().replace("\n", "\\n"));
        opt_map.put("commodity_variety", et_variety.getText().toString());
        opt_map.put("commodity_class", commodityclass);
        opt_map.put("commodity_order", et_order.getText().toString().trim());
        opt_map.put("userid", String.valueOf(Const.currentUser.user_id));
        opt_map.put("OwnerClass", Const.currentUser.user_role);
        opt_map.put("Variety_1", Variety_1);
        opt_map.put("Variety_2", Variety_2);
        opt_map.put("DelImage", DelImage);
        HttpUtils hu = new HttpUtils();
        hu.httpPostFiles(Const.BASE_URL + "updateCommodity.php", opt_map, fileList, pushFileResultTmp.class, this);
    }

    Runnable resultPushFile = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp.getDetail().getContent().equals("上传成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        if (rspBaseBean.RequestSign.equals("GetCommodityById")) {
            CommodityDataTmp mCommodityDataTmp = ObjectUtil.cast(rspBaseBean);
            mCommodityData = mCommodityDataTmp.getDetail().get(0);
            mHandler.post(updateThis);
        } else if (rspBaseBean.RequestSign.equals("getVariety")) {
            // Log.e(TAG, "onSuccess: " + rspBaseBean);
            CommodityVarietyDataTmp mCommodityVarietyDataTmp = ObjectUtil.cast(rspBaseBean);
            arrCommodityVarietyData = mCommodityVarietyDataTmp.getDetail();

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
            mHandler.post(updateSpinner);
        } else if (rspBaseBean.RequestSign.equals("updateCommodity")) {
            mCommResultTmp = ObjectUtil.cast(rspBaseBean);
            mHandler.post(resultPushFile);
        } else if (rspBaseBean.RequestSign.equals("DeleteCommodityById")) {
            mCommResultTmp2 = ObjectUtil.cast(rspBaseBean);
            mHandler.post(DeleteResult);
        }

    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
    }

    @Override
    public void onFail() {

    }

    Runnable DeleteResult = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp2.getDetail().getContent().equals("删除成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp2.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp2.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };
    Runnable updateThis = new Runnable() {
        @Override
        public void run() {
            et_variety.setText(mCommodityData.getCommodityVariety());
            //sp_variety_1.setSelection();
            //sp_variety_2.setSelection();
            et_name.setText(mCommodityData.getCommodityName());
            et_title.setText(mCommodityData.getCommodityTitle());
            et_price.setText(mCommodityData.getCommodityPrice());
            et_discribe.setText(mCommodityData.getCommodityDescribe());
            et_order.setText(String.valueOf(mCommodityData.getCommodityOrderNo()));
            String[] arrImage = mCommodityData.getCommodityImgs().split(";");
            images.clear();
            images.add("add");
            for (int i = 0; i < arrImage.length; i++) {
                if (arrImage[i].trim().equals("")) {
                } else {
                    images.add(Const.IMG_URL + arrImage[i]);
                }
            }
            Log.e(TAG, "run: images" + images.toString());
            gridAdapter.notifyDataSetChanged();
            getVarietyData();
            if (mCommodityData.getCommodityClass().equals("种子")) {
                commodityclass = "种子";
                rb_seed.setChecked(true);
            } else if (mCommodityData.getCommodityClass().equals("农药")) {
                commodityclass = "农药";
                rb_fertilizer.setChecked(true);
            } else if (mCommodityData.getCommodityClass().equals("化肥")) {
                commodityclass = "化肥";
                rb_pesticide.setChecked(true);
            }
        }
    };
    Runnable updateSpinner = new Runnable() {
        @Override
        public void run() {

            arrCommodityVarietyData_1.clear();
            arrCommodityVarietyData_1.addAll(arrCommodityVarietyData_1_tmp);
            arrCommodityVarietyData_2.clear();
            arrCommodityVarietyData_2.addAll(arrCommodityVarietyData_2_tmp);
            sp_VarietyAdapter_1.notifyDataSetChanged();
            for (int i = 0; i < arrCommodityVarietyData_1.size(); i++) {
                if (arrCommodityVarietyData_1.get(i).getVarietyname().trim().equals(mCommodityData.getCommodityVariety_1().trim())) {
                    isSp2Select = true;
                    sp_variety_1.setSelection(i);
                    break;
                }
            }
        }

    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            if (isAdd == 0) {
                if (images.size() < 10) {
                    images.add(mPicturePath);
                }
            } else {
                images.set(isAdd, mPicturePath);
            }
            gridAdapter.notifyDataSetChanged();
        }
    }

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

        @Override
        public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
            return new GridAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GridAdapter.ViewHolder holder, final int position) {
            if (images.get(position).equals("add")) {
                Glide.with(CommoditySettingActivity.this)
                        .load(R.drawable.add_img).dontAnimate()
                        .centerCrop()
                        .into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, TO_SELECT_PHOTO);
                        isAdd = 0;
                    }
                });
            } else {
                Log.e(TAG, "onBindViewHolder: " + images.get(position));

                Glide.with(CommoditySettingActivity.this)
                        .load(images.get(position)).placeholder(R.drawable.no_have_img).dontAnimate()
                        .centerCrop()
                        .into(holder.imageView);
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, TO_SELECT_PHOTO);
                        isAdd = position;
                    }
                });
                holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        iosDialog mIosDialog = new iosDialog.Builder(CommoditySettingActivity.this)
                                .setMessage("删除确认！")
                                .setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.e(TAG, "onClick: 确认删除");
                                        if (DelImage.equals("")) {
                                            DelImage = images.get(position).replace(Const.IMG_URL, "");
                                        } else {
                                            DelImage = DelImage + "," + images.get(position).replace(Const.IMG_URL, "");
                                        }
                                        images.remove(position);
                                        notifyDataSetChanged();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("暂不删除", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.e(TAG, "onClick: 暂不删除");
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("种愿").create();
                        mIosDialog.show();
                        return true;
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.image);
            }

        }

    }

    AdapterView.OnItemSelectedListener spTitleListener_1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Variety_1 = arrCommodityVarietyData_1.get(i).getVarietyname();
            Log.e(TAG, "onItemSelected: Variety_1onclick" + arrCommodityVarietyData_1.get(i).getVarietyname());
            arr_Variety_Data_2.clear();
            for (int j = 0; j < arrCommodityVarietyData_2.size(); j++) {
                CommodityVarietyData varietyData = new CommodityVarietyData();
                varietyData = arrCommodityVarietyData_2.get(j);
                if (Integer.parseInt(varietyData.getVarietyclassid()) == Integer.parseInt(arrCommodityVarietyData_1.get(i).getVarietyid())) {
                    arr_Variety_Data_2.add(varietyData);
                }
            }
            sp_VarietyAdapter_2.notifyDataSetChanged();
            if (isSp2Select) {
                Log.e(TAG, "onItemSelected: Variety_2onclick" + arrCommodityVarietyData_2.get(i).getVarietyname() + mCommodityData.getCommodityVariety_2());
                for (int j = 0; j < arr_Variety_Data_2.size(); j++) {
                    if (arr_Variety_Data_2.get(j).getVarietyname().trim().equals(mCommodityData.getCommodityVariety_2().trim())) {
                        Log.e(TAG, "run: " + arr_Variety_Data_2.get(j) + mCommodityData.getCommodityVariety_2());
                        sp_variety_2.setSelection(j);
                        break;
                    }
                }
                isSp2Select = false;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener spTitleListener_2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Variety_2 = arr_Variety_Data_2.get(i).getVarietyname();
            varietyid = arr_Variety_Data_2.get(i).getVarietyid();
/*            Log.e(TAG, "onItemSelected: Variety_2" + arrCommodityVarietyData_2.get(i).getVarietyname());*/
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

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

    @Override
    protected void onResume() {
        super.onResume();
        getPermation();
    }

    private void getPermation() {
        MPermissions.requestPermissions(CommoditySettingActivity.this, REQUEST_CODE_FILES, AppPermissions.getFilePermissions());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @PermissionGrant(REQUEST_CODE_FILES)
    public void requestFilesSuccess() {
        //Toast.makeText(this, "GRANT ACCESS LOCATION!", Toast.LENGTH_SHORT).show();

    }

    @PermissionDenied(REQUEST_CODE_FILES)
    public void requestFilesFailed() {
        // Toast.makeText(this, "定位服务已经被禁止!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
