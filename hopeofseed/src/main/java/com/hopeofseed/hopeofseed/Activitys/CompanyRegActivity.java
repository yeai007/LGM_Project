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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.DataForHttp.GetPhoneCode;
import com.hopeofseed.hopeofseed.DataForHttp.GetUserClass;
import com.hopeofseed.hopeofseed.Adapter.UserClassAdapter;
import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXData.UserClass;
import com.hopeofseed.hopeofseed.JNXData.UserData;
import com.hopeofseed.hopeofseed.JNXDataTmp.UserDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.GetImagePath;
import com.lgm.utils.ObjectUtil;
import com.lgm.utils.TimeCountUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import citypickerview.widget.CityPicker;
import io.realm.Realm;
import me.shaohui.advancedluban.Luban;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 项目名称：liguangming
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/9/27 12:57
 * 修改人：whisper
 * 修改时间：2016/9/27 12:57
 * 修改备注：
 */
public class CompanyRegActivity extends AppCompatActivity implements View.OnClickListener, NetCallBack {
    private static final String TAG = "CompanyRegActivity";
    Button btn__BusinessLicense, btn_topright, get_phonecode;
    public static final int TO_SELECT_PHOTO = 142;
    private String mPicturePath;
    public static Bitmap mBitmap;
    ImageView img__BusinessLicense;
    static int provincePosition = 3;
    Spinner sp_userclass;
    ArrayList<UserClass> arr_UserClass = new ArrayList<>();
    EditText et_companyname, et_phone, et_password, et_password_refirm, et_truename, et_phone_code, et_address_detail;
    UserClassAdapter userClassAdapter;
    private ArrayList<String> images = new ArrayList<>();
    CityPicker cityPicker;
    private String StrProvince = "", StrCity = "", StrZone = "";


    ArrayList<File> arrFile = new ArrayList<>();
    long start;
    pushFileResultTmp mCommResultTmp;
    Handler mHandler = new Handler();
    String phone_code = "";
    String StrError;

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
        et_truename = (EditText) findViewById(R.id.et_truename);
        et_phone_code = (EditText) findViewById(R.id.et_phone_code);
        get_phonecode = (Button) findViewById(R.id.get_phonecode);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        get_phonecode.setOnClickListener(this);

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
                        .province(Const.Province)
                        .city(Const.City)
                        .district(Const.Zone)
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
                try {
                    regCompanyUser();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.get_phonecode:
                Toast.makeText(getApplicationContext(), "获取验证码", Toast.LENGTH_SHORT).show();
                if (et_phone.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "请输入手机号",
                            Toast.LENGTH_SHORT).show();

                } else {
                    TimeCountUtil timeCountUtil = new TimeCountUtil(
                            CompanyRegActivity.this, 60000, 1000, get_phonecode);
                    timeCountUtil.start();
                    getPhoneCode();
                }
                break;
        }
    }

    private String getRandomCode() {
        Random rad = new Random();
        String result = rad.nextInt(1000000) + "";
        if (result.length() != 4) {
            return getRandomCode();
        }
        return result;
    }

    /**
     * 获取手机号
     */
    private void getPhoneCode() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                GetPhoneCode getPhoneCode = new GetPhoneCode();
                getPhoneCode.mobile = et_phone.getText().toString().trim();
                phone_code = getRandomCode();
                getPhoneCode.content = "您的验证码是：" + phone_code + "。请不要把验证码泄露给其他人。如非本人操作，可不用理会！";
                Boolean bRet = getPhoneCode.RunData();
                Message msg = getPhoneCodeUserHandle.obtainMessage();
                if (bRet) {
                    msg.arg1 = 1;
                } else {
                    msg.arg1 = 0;
                }
                msg.obj = getPhoneCode.dataMessage.obj;
                msg.sendToTarget();
            }
        }).start();
    }

    private Handler getPhoneCodeUserHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void regCompanyUser() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    Log.e(TAG, "run: " + images.get(i));
                    if (images.get(i).equals("add")) {
                    } else {
                        arrFile.add(new File(images.get(i)));
                    }
                }
                Log.e(TAG, "images: " + images);
                Luban.get(getApplicationContext()).setMaxSize(2048)
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
                                Log.e(TAG, "call: " + fileList);
                                postCompanyUser(fileList);
                            }
                        });
            }
        }).start();
    }

    private void postCompanyUser(List<File> fileList) {
        String check_result = checkInput();
        if (check_result.equals("IsChecked")) {
            Log.e(TAG, "postCompanyUser: topost");
            HashMap<String, String> opt_map = new HashMap<>();
            opt_map.put("UserName", et_companyname.getText().toString().trim());
            opt_map.put("PassWord", et_password.getText().toString().trim());
            opt_map.put("PhoneCode", et_phone.getText().toString().trim());
            opt_map.put("CompanyName", et_companyname.getText().toString().trim());
            opt_map.put("UserClass", ((UserClass) sp_userclass.getSelectedItem()).getName().trim());
            opt_map.put("Province", StrProvince);
            opt_map.put("City", StrCity);
            opt_map.put("County", StrZone);
            opt_map.put("TrueName", et_truename.getText().toString().trim());
            opt_map.put("GetPhoneCode", et_phone_code.getText().toString().trim());
            opt_map.put("AddressDetail", et_address_detail.getText().toString().trim());
            HttpUtils hu = new HttpUtils();
            hu.httpPostFiles(Const.BASE_URL + "RegCompanyUser.php", opt_map, fileList, UserDataTmp.class, this);
        } else {
            Toast.makeText(getApplicationContext(), check_result, Toast.LENGTH_SHORT).show();
        }
    }

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
        if (et_truename.getText().toString().trim().equals("")) {
            out_msg = "真实姓名不能为空！";
        }
        if (et_phone_code.getText().toString().trim().equals("")) {
            out_msg = "请输入验证码！";
        }
        if (et_address_detail.getText().toString().trim().equals("")) {
            out_msg = "详细地址不能为空！";
        }
        if (TextUtils.isEmpty(((UserClass) sp_userclass.getSelectedItem()).getName().trim())) {
            out_msg = "类别未选择！";
        }
        return out_msg;
    }


    private void getBusinessLicenseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, TO_SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            if (images.size() < 3) {
                images.add(mPicturePath);
            }
            Log.e(TAG, "onActivityResult: " + images.size());
            Glide.with(CompanyRegActivity.this)
                    .load(new File(mPicturePath))
                    .centerCrop()
                    .into(img__BusinessLicense);
        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        Log.e(TAG, "onSuccess: " + rspBaseBean.resultNote);
        if (rspBaseBean.RequestSign.equals("RegCompanyUser")) {
            updateRealmData(rspBaseBean);
        }
    }

    @Override
    public void onError(String error) {
        Log.e(TAG, "onError: " + error);
        StrError = error;
        mHandler.post(toastError);
    }

    @Override
    public void onFail() {

    }

    Runnable toastError = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getApplicationContext(), StrError, Toast.LENGTH_SHORT).show();
        }
    };

    private void updateRealmData(RspBaseBean rspBaseBean) {
        UserDataTmp mUserDataTmp = new UserDataTmp();
        mUserDataTmp = ObjectUtil.cast(rspBaseBean);
        Realm updateRealm = Realm.getDefaultInstance();
        updateRealm.beginTransaction();//开启事务
        UserData updateUserData = updateRealm.where(UserData.class)
                .equalTo("iscurrent", 1)//查询出name为name1的User对象
                .findFirst();//修改查询出的第一个对象的名字
        if (updateUserData != null) {
            updateUserData.setIsCurrent(0);
        }
        updateRealm.commitTransaction();
        /*******************************
         * */
        UserData o = mUserDataTmp.getDetail();
        o.setIsCurrent(1);
        updateRealm.beginTransaction();
        UserData newdata = updateRealm.copyToRealmOrUpdate(o);
        updateRealm.commitTransaction();
        Const.currentUser.user_id = newdata.getUser_id();
        Const.currentUser.user_name = newdata.getUser_name();
        Const.currentUser.password = newdata.getPassword();
        Const.currentUser.nickname = newdata.getNickname();
        Const.currentUser.user_mobile = newdata.getUser_mobile();
        Const.currentUser.user_email = newdata.getUser_email();
        Const.currentUser.createtime = newdata.getCreatetime();
        Const.currentUser.user_permation = newdata.getUser_permation();
        Const.currentUser.user_role = newdata.getUser_role();
        Const.currentUser.user_role_id = newdata.getUser_role_id();
        Const.currentUser.user_field = newdata.getUser_field();
        Const.currentUser.iscurrent = newdata.getIsCurrent();
        Log.e(TAG, "updateRealmData: " + Const.currentUser.toString());
        Intent intent = new Intent(CompanyRegActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
}
