package com.hopeofseed.hopeofseed.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.Data.Const;
import com.hopeofseed.hopeofseed.Http.HttpUtils;
import com.hopeofseed.hopeofseed.Http.NetCallBack;
import com.hopeofseed.hopeofseed.Http.RspBaseBean;
import com.hopeofseed.hopeofseed.JNXDataTmp.pushFileResultTmp;
import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.AppUtil;
import com.lgm.utils.ObjectUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import me.shaohui.advancedluban.Luban;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static android.content.ContentValues.TAG;
import static com.hopeofseed.hopeofseed.Activitys.NewsFragment.NEWS_UPDATE_LIST;
import static com.hopeofseed.hopeofseed.R.id.img_user_avatar;


/**
 * Created by ${chenyn} on 16/4/8.
 *
 * @desc :更新用户头像
 */
public class UpdateGroupAvatar extends Activity implements View.OnClickListener, NetCallBack {

    private ProgressDialog mProgressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button mBt_localImage;
    private Button mBt_update;
    private String mPicturePath;
    public static Bitmap mBitmap;
    Button btn_topleft;
    ArrayList<File> arrFile = new ArrayList<>();
    String GroupID;
    pushFileResultTmp mCommResultTmp = new pushFileResultTmp();
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        GroupID = intent.getStringExtra("GroupID");
        initView();
        initData();
        getPermation();
    }

    private void getPermation() {
        AppUtil.verifyStoragePermissions(UpdateGroupAvatar.this);
    }

    private void initData() {
        mBt_localImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });

        mBt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(UpdateGroupAvatar.this, "提示：", "正在加载中。。。");
                mProgressDialog.setCanceledOnTouchOutside(true);
                if (mPicturePath != null) {
                    final File file = new File(mPicturePath);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            arrFile.add(file);

                            Luban.get(getApplicationContext()).setMaxSize(128)
                                    .putGear(Luban.CUSTOM_GEAR)
                                    .load(arrFile)                     // load all images
                                    .asListObservable().doOnRequest(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
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
                                            updateAvatar(fileList);
                                        }
                                    });
                        }
                    }).start();
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateGroupAvatar.this, "请选择图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateAvatar(List<File> fileList) {
        HashMap<String, String> opt_map = new HashMap<>();
        opt_map.put("GroupID", GroupID);
        opt_map.put("UserID", String.valueOf(Const.currentUser.user_id));
        HttpUtils hu = new HttpUtils();
        hu.httpPostFiles(Const.BASE_URL + "UpdateGroupAvatar.php", opt_map, fileList, pushFileResultTmp.class, this);
    }

    private void initView() {
        setContentView(R.layout.activity_update_user_avatar);
        mBt_localImage = (Button) findViewById(R.id.bt_local_image);
        mBt_update = (Button) findViewById(R.id.bt_update);
        TextView AppTitle = (TextView) findViewById(R.id.apptitle);
        AppTitle.setText("修改头像");
        btn_topleft = (Button) findViewById(R.id.btn_topleft);
        btn_topleft.setOnClickListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            mPicturePath = GetImagePath.getImageAbsolutePath(this, selectedImage);
            ImageView imageView = (ImageView) findViewById(R.id.iv_show_image);
            mBitmap = BitmapFactory.decodeFile(mPicturePath);
            imageView.setImageBitmap(mBitmap);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_topleft:
                finish();
                break;

        }
    }

    @Override
    public void onSuccess(RspBaseBean rspBaseBean) {
        mCommResultTmp = ObjectUtil.cast(rspBaseBean);
        handler.post(resultPushFile);
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public void onFail() {

    }

    /**
     * uploadimg
     */
    Runnable resultPushFile = new Runnable() {
        @Override
        public void run() {
            if (mCommResultTmp.getDetail().getContent().equals("上传成功")) {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                Intent intent = new Intent();
                setResult(801, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), mCommResultTmp.getDetail().getContent(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                Intent intent = new Intent();
                setResult(801, intent);
                finish();
            }
        }
    };
}
