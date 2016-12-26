package com.hopeofseed.hopeofseed.Activitys;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hopeofseed.hopeofseed.Data.Const;

import com.hopeofseed.hopeofseed.Http.HttpUtils;


import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.CommResultTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.NewsDataTmp;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.ui.ShowImage;
import com.hopeofseed.hopeofseed.Adapter.PublishImgsAdapter;
import com.hopeofseed.hopeofseed.R;
import com.hopeofseed.hopeofseed.util.GetImagePath;
import com.hopeofseed.hopeofseed.util.UpdateUserAvatar;
import com.lgm.utils.AppPermissions;
import com.lgm.utils.AppUtil;
import com.lgm.utils.ObjectUtil;
import com.lgm.view.ImageSelectorActivity;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.shaohui.advancedluban.Luban;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.hopeofseed.hopeofseed.Activitys.NewsFragment.NEWS_UPDATE_LIST;
import static com.hopeofseed.hopeofseed.Application.REQUEST_CODE_FILES;
import static com.hopeofseed.hopeofseed.Application.REQUEST_CODE_LOCATION;
import static com.hopeofseed.hopeofseed.R.color.text_content_color;


/**
 * @FileName:com.mgkj.smamootwo.Activitys
 * @Desc:
 * @Author:liguangming
 * @Date:2016/5/16
 * @Copyright:2014-2016 Moogeek
 */
public class PubishMainActivity extends AppCompatActivity implements NetCallBack {
    private String mPicturePath;
    public static Bitmap mBitmap;
    ArrayList<File> arrFile = new ArrayList<>();
    Handler mHandler = new Handler();
    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */

    private static final int UPLOAD_IN_PROCESS = 5;
    long start;

    /**
     * 动态添加权限
     */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private ProgressBar progressBar;
    private static String requestURL = Const.BASE_URL + "addNewPic.php";
    private String picPath = null;
    private ProgressDialog progressDialog;
    private TextView uploadImageResult;
    String TAG = "PubishMainActivity";
    GridView gv_photo;
    private ArrayList<Map<String, String>> arrPublishData = new ArrayList<>();
    PublishImgsAdapter mPublishImgsAdapter;
    EditText et_message;
    private RecyclerView resultRecyclerView;
    private ArrayList<String> images = new ArrayList<>();
    GridAdapter gridAdapter;
    pushFileResultTmp mCommResultTmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.publish_main_activity);
        PrepareAppCompat();
        //    createdata();
        initView();

    }
    @Override
    protected void onResume() {
        super.onResume();
        getPermation();
    }

    private void getPermation() {
        MPermissions.requestPermissions(PubishMainActivity.this, REQUEST_CODE_FILES, AppPermissions.getFilePermissions());
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

    private boolean checkInput() {
        boolean ischecked = true;
        if (TextUtils.isEmpty(et_message.getText().toString().trim())) {
            ischecked = false;
            Toast.makeText(getApplicationContext(), "信息不能为空", Toast.LENGTH_SHORT).show();
        } else {
            ischecked = true;
        }
        return ischecked;
    }

    private void initView() {
        uploadImageResult = (TextView) findViewById(R.id.uploadImageResult);
        progressDialog = new ProgressDialog(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        et_message = (EditText) findViewById(R.id.et_message);
        gv_photo = (GridView) findViewById(R.id.gv_photo);
        mPublishImgsAdapter = new PublishImgsAdapter(getApplicationContext(), arrPublishData);
        gv_photo.setAdapter(mPublishImgsAdapter);
        gv_photo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageSelectorActivity.start(PubishMainActivity.this, 6, 1, true, false, false);
            }
        });
        images.add("add");
        resultRecyclerView = (RecyclerView) findViewById(R.id.result_recycler);
        resultRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        gridAdapter = new GridAdapter();
        resultRecyclerView.setAdapter(gridAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TO_SELECT_PHOTO && resultCode == RESULT_OK && null != data) {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            if (images.size() < 10) {
                images.add(mPicturePath);
            }
            gridAdapter.notifyDataSetChanged();
            Log.e(TAG, "onActivityResult: " + images);
        }
    }

    //注册监听事件
    private void PrepareAppCompat() {
        TextView apptitle = (TextView) findViewById(R.id.apptitle);
        apptitle.setText("发布新鲜事");
        Button btn_topleft = (Button) findViewById(R.id.btn_topleft);
        Button btn_topright = (Button) findViewById(R.id.btn_topright);
        btn_topleft.setVisibility(View.VISIBLE);
        btn_topright.setText("发送");
        btn_topright.setVisibility(View.VISIBLE);
        assert btn_topleft != null;
        btn_topleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        assert btn_topright != null;
        btn_topright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInput()) {
                    try {
                        sendWordAndImg();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        });
    }

    private void sendWordAndImg() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < images.size(); i++) {
                    Log.e(TAG, "run: " + images.get(i));
                    if (images.get(i).equals("add")) {
                        if (images.size() == 1) {
                            SendWordAndImage(null);
                        }
                    } else {
                        arrFile.add(new File(images.get(i)));
                    }
                }
                Log.e(TAG, "images: " + images);
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
                                SendWordAndImage(fileList);
                            }
                        });
            }
        }).start();
    }

    private void SendWordAndImage(List<File> fileList) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("UserId", String.valueOf(Const.currentUser.user_id));
        opt_map.put("WordsStr", et_message.getText().toString().trim().replace("\n", "\\n"));
        opt_map.put("LocLat", String.valueOf(Const.LocLat));
        opt_map.put("LocLng", String.valueOf(Const.LocLng));
        opt_map.put("Province", Const.Province);
        opt_map.put("City", Const.City);
        opt_map.put("Zone", Const.Zone);
        HttpUtils hu = new HttpUtils();
        hu.httpPostFiles(Const.BASE_URL + "send_WordAndImg.php", opt_map, fileList, pushFileResultTmp.class, this);
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        mCommResultTmp = ObjectUtil.cast(rspBaseBean);
        mHandler.post(resultPushFile);
    }

    @Override
    public void onError(String error) {
    }

    @Override
    public void onFail() {
    }

    Runnable resultPushFile = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp.getDetail().getContent().equals("上传成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();  //Itent就是我们要发送的内容
                intent.setAction(NEWS_UPDATE_LIST);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                sendBroadcast(intent);   //发送广播
                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
        @Override
        public GridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_result, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GridAdapter.ViewHolder holder, final int position) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (images.get(position).equals("add")) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, TO_SELECT_PHOTO);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), ShowImage.class);
                        intent.putExtra("IMG_URL", images.get(position));
                        startActivity(intent);
                    }
                }

            });
            if (images.get(position).equals("add")) {
                Glide.with(PubishMainActivity.this)
                        .load(R.drawable.add_img)
                        .centerCrop()
                        .into(holder.imageView);
            } else {
                Log.e(TAG, "onBindViewHolder: " + images.get(position));
                Glide.with(PubishMainActivity.this)
                        .load(new File(images.get(position)))
                        .centerCrop()
                        .into(holder.imageView);
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

}
