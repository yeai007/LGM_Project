package com.hopeofseed.hopeofseed.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hopeofseed.hopeofseed.R;
import com.lgm.utils.AppUtil;

import java.io.File;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

import static com.hopeofseed.hopeofseed.Activitys.UserInfoFragment.UPDATE_USER_INFO;


/**
 * Created by ${chenyn} on 16/4/8.
 *
 * @desc :更新用户头像
 */
public class UpdateUserAvatar extends Activity implements View.OnClickListener {

    private ProgressDialog mProgressDialog;
    private static int RESULT_LOAD_IMAGE = 1;
    private Button mBt_localImage;
    private Button mBt_update;
    private String mPicturePath;
    public static Bitmap mBitmap;
    Button btn_topleft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        getPermation();
    }

    private void getPermation() {
        AppUtil.verifyStoragePermissions(UpdateUserAvatar.this);
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
                mProgressDialog = ProgressDialog.show(UpdateUserAvatar.this, "提示：", "正在加载中。。。");
                mProgressDialog.setCanceledOnTouchOutside(true);
                if (mPicturePath != null) {
                    File file = new File(mPicturePath);
                    try {
                        JMessageClient.updateUserAvatar(file, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                if (i == 0) {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                    //数据是使用Intent返回
                                    Intent intent = new Intent();
                                    //设置返回数据
                                    setResult(RESULT_OK, intent);
                                    Intent intent_update_userinfo = new Intent();  //Itent就是我们要发送的内容
                                    intent_update_userinfo.setAction(UPDATE_USER_INFO);   //设置你这个广播的action，只有和这个action一样的接受者才能接受者才能接收广播
                                    sendBroadcast(intent_update_userinfo);   //发送广播
                                    //关闭Activity
                                    finish();
                                } else {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                    Log.i("UpdateUserAvatar", "JMessageClient.updateUserAvatar" + ", responseCode = " + i + " ; LoginDesc = " + s);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(UpdateUserAvatar.this, "请选择图片", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
/*            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mPicturePath = cursor.getString(columnIndex);
            cursor.close();*/

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
}
