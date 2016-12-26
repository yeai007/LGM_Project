package com.lgm.utils;

import android.Manifest;

/**
 * 项目名称：LGM_Project
 * 类描述：
 * 创建人：whisper
 * 创建时间：2016/12/26 9:39
 * 修改人：whisper
 * 修改时间：2016/12/26 9:39
 * 修改备注：
 */
public class AppPermissions {
    public static String[] getFilePermissions() {
        String[] Permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };
        return Permissions;
    }

    /**
     * 需要进行检测的权限数组
     */
    public static String[] getLocationPermissions() {
        String[] needPermissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        return needPermissions;
    }
}
