package com.hopeofseed.hopeofseed.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.DataForHttp.GetUserClass;
import com.hopeofseed.hopeofseed.Adapter.UserClassAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.DataForHttp.CompanyRegister;
import com.hopeofseed.hopeofseed.JNXData.UserClass;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.GetImagePath;
import com.lgm.utils.ObjectUtil;

import java.io.File;
import java.util.ArrayList;

import citypickerview.widget.CityPicker;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 12:57
 * 修改人：whisper
 * 修改时间：2016/9/27 12:57
 * 修改备注：
 */
public class CompanyRegActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CompanyRegActivity";
    Button btn__BusinessLicense, btn_topright;
    public static final int TO_SELECT_PHOTO = 142;
    private String mPicturePath;
    public static Bitmap mBitmap;
    ImageView img__BusinessLicense;
    static int provincePosition = 3;
    Spinner sp_userclass;
    ArrayList<UserClass> arr_UserClass = new ArrayList<>();
    EditText et_companyname, et_phone, et_password, et_password_refirm;
    UserClassAdapter userClassAdapter;
    private ArrayList<String> images = new ArrayList<>();
    CityPicker cityPicker;
    private String StrProvince = "", StrCity = "", StrZone = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_reg_activity);
        initView();
    }

    private void initView() {
        TextView appTitle = (TextView) findViewById(R.id.apptitle);
        appTitle.setText("企业用户注册");
        btn__BusinessLicense = (Button) findViewById(R.id.btn__BusinessLicense);
        btn__BusinessLicense.setOnClickListener(this);
        img__BusinessLicense = (ImageView) findViewById(R.id.img__BusinessLicense);
        (findViewById(R.id.btn_topleft)).setOnClickListener(this);
        btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topright.setOnClickListener(this);
        btn_topright.setText("申请");
        btn_topright.setVisibility(View.VISIBLE);
        sp_userclass = (Spinner) findViewById(R.id.sp_userclass);
        et_companyname = (EditText) findViewById(R.id.et_companyname);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_refirm = (EditText) findViewById(R.id.et_password_refirm);
        initSpTitle();
        final Button go = (Button) findViewById(R.id.go);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityPicker = new CityPicker.Builder(CompanyRegActivity.this).textSize(20)
                        .title("地址选择")
                        .titleBackgroundColor("#5F9EA0")
                        .onlyShowProvinceAndCity(false)
                        .confirTextColor("#000000")
                        .cancelTextColor("#000000")
                        .province("山东省")
                        .city("济南市")
                        .district("全部")
                        .textColor(Color.parseColor("#000000"))
                        .provinceCyclic(false)
                        .cityCyclic(false)
                        .districtCyclic(false)
                        .visibleItemsCount(7)
                        .itemPadding(10)
                        .build();

                cityPicker.show();
                cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
                    @Override
                    public void onSelected(String... citySelected) {
                        go.setText("" + citySelected[0] + "  " + citySelected[1] + "  "
                                + citySelected[2]);
                        StrProvince = citySelected[0];
                        StrCity = citySelected[1];
                        StrZone = citySelected[2];
                    }
                });
            }
        });
    }

    private void initSpTitle() {
        userClassAdapter = new UserClassAdapter(this, arr_UserClass);
        sp_userclass.setAdapter(userClassAdapter);
        getUserClassData();

    }


    private void getUserClassData() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetUserClass getUserClass = new GetUserClass();
                getUserClass.UserId = String.valueOf(Const.currentUser.user_id);
                Boolean bRet = getUserClass.RunData();
                Message msg = getUserClassDataHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = getUserClass.retRows;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getUserClassDataHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:

                    break;
                case 1:
                    ArrayList<UserClass> arr_TopClass_tmp = new ArrayList<>();
                    ArrayList<UserClass> arr_TopClass_tmp2 = new ArrayList<>();
                    arr_TopClass_tmp = ObjectUtil.cast(msg.obj);
                    for (int i = 0; i < arr_TopClass_tmp.size(); i++) {
                        UserClass userClass = new UserClass();
                        userClass = arr_TopClass_tmp.get(i);
                        if (userClass.getName().equals("农友") || userClass.getName().equals("专家")) {
                        } else {
                            arr_TopClass_tmp2.add(userClass);
                        }
                    }
                    arr_UserClass.clear();

                    arr_UserClass.addAll(arr_TopClass_tmp2);
                    userClassAdapter.notifyDataSetChanged();
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn__BusinessLicense:
                getBusinessLicenseImage();
                break;
            case R.id.btn_topleft:
                finish();
                break;
            case R.id.btn_topright:
                regCompanyUser();
                break;
        }
    }

    private void regCompanyUser() {
        String check_result = checkInput();
        if (check_result.equals("IsChecked")) {
            // TODO Auto-generated method stub
            new Thread(new Runnable() {
                @Override
                public void run() {
                    CompanyRegister userRegister = new CompanyRegister();
                    userRegister.UserName = et_companyname.getText().toString().trim();
                    userRegister.PassWord = et_password.getText().toString().trim();
                    userRegister.PhoneCode = et_phone.getText().toString().trim();
                    userRegister.CompanyName = et_companyname.getText().toString().trim();
                    userRegister.UserClass = ((UserClass) sp_userclass.getSelectedItem()).getName().trim();
                    userRegister.Province = StrProvince;
                    userRegister.City = StrCity;
                    userRegister.County = StrZone;
                    Log.e(TAG, "run: " + images.size());
                    userRegister.images = images;
                    Boolean bRet = userRegister.UploadImg();
                    Message msg = loginUserHandle.obtainMessage();
                    if (bRet) {
                        msg.arg1 = userRegister.dataMessage.arg1;
                    } else {
                        msg.arg1 = 0;
                    }
                    msg.sendToTarget();
                }
            }).start();
        } else {
            Toast.makeText(getApplicationContext(), check_result, Toast.LENGTH_SHORT).show();
        }


    }

    private Handler loginUserHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    break;
                case 1:

//                    initJpushUser();

                    Log.e(TAG, "handleMessage: " + Const.currentUser.user_name + Const.currentUser.password);
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "该帐号不存在", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "handleMessage: " + Const.currentUser.user_name + Const.currentUser.password);
                    break;
            }
        }
    };

    private String checkInput() {
        String out_msg = "IsChecked";
        if (et_companyname.getText().toString().trim().equals("")) {
            out_msg = "公司名不能为空！";
        }
        if (et_password.getText().toString().trim().equals("")) {
            out_msg = "密码不能为空！";
        }
        if (et_password_refirm.getText().toString().trim().equals("")) {
            out_msg = "密码不能为空！";
        }
        if (et_phone.getText().toString().trim().equals("")) {
            out_msg = "电话不能为空！";
        }
        if (StrProvince.trim().equals("")) {
            out_msg = "地址不全！";
        }
        return out_msg;
    }


    private void getBusinessLicenseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, TO_SELECT_PHOTO);
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, TO_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            if (images.size() < 10) {
                images.add(mPicturePath);
            }
            Log.e(TAG, "onActivityResult: " + images.size());
            Glide.with(CompanyRegActivity.this)
                    .load(new File(mPicturePath))
                    .centerCrop()
                    .into(img__BusinessLicense);
        }
    }
}
