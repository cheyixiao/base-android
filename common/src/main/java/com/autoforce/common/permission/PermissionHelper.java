package com.autoforce.common.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import com.autoforce.common.R;
import com.autoforce.common.utils.AppMessageUtils;
import com.autoforce.common.utils.SpUtils;
import com.autoforce.common.utils.StringUtils;
import com.autoforce.common.view.dialog.CustomerDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by xlh on 2019/3/5.
 * description: 动态权限申请管理类
 */
public class PermissionHelper {

    private final RxPermissions mRxPermissions;
    private final FragmentActivity mActivity;
    // 申请的权限组
    private final String[] mPermissionArray;
    // 待申请的权限是否是强制性获取
    private final boolean isForceRequest;
    // 权限申请前引导dialog的文本内容  null表示不需要前置引导dialog
    private final String mRequestTips;
    private final PermissionCallback mCallback;
    private final static String SP_NAME = "CYX_PermissionManager";

    private PermissionHelper(Builder builder) {
        this.mRxPermissions = builder.rxPermissions;
        this.mActivity = builder.activity;
        this.mPermissionArray = builder.permissionArray;
        this.isForceRequest = builder.isForceRequest;
        this.mRequestTips = builder.requestTips;
        this.mCallback = builder.callback;
    }

    public void request() {

        if (mPermissionArray == null) {
            throw new RuntimeException("You must set request permissions");
        }

        // 所需申请的权限都已被授予  回调申请成功并返回
        if (isGrant(mPermissionArray)) {
            if (mCallback != null) {
                mCallback.onPermissionGranted();
            }
            return;
        }


        // 所申请权限被拒，并且勾选了不再提示，直接显示前往设置页面对话框
        if (checkShowToSettingDialog(mActivity, mPermissionArray)) {
            showToSettingDialog();
        } else {

            /* **权限可被申请** */

            // 需要显示前置权限引导对话框
            if (mRequestTips != null) {
                showPermissionRequestDialog(mRequestTips);
            } else {
                // 不需引导对话框，直接申请
                requestInternal();
            }
        }
    }

    /**
     * 所申请权限是否已经被用户授予
     *
     * @param permissionArray
     * @return
     */
    private boolean isGrant(String[] permissionArray) {
        for (String permission : permissionArray) {
            if (!mRxPermissions.isGranted(permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查所申请权限被拒，并且勾选了不再提示
     *
     * @param activity
     * @param permissionArray
     * @return
     */
    private boolean checkShowToSettingDialog(Activity activity, String[] permissionArray) {

        boolean isFirstRequest = isFirstRequest(permissionArray);

        if (isFirstRequest) {
            return false;
        }

        for (String permission : permissionArray) {
            if (!mRxPermissions.isGranted(permission) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }

        return false;
    }

    private void requestInternal() {

        mRxPermissions.request(mPermissionArray)
                .subscribe(granted -> {

                    changePermissionRequestNotFirst(mPermissionArray);

                    if (granted) {
                        if (mCallback != null) {
                            mCallback.onPermissionGranted();
                        }
                    } else {

                        // 强制要求获取的权限
//                        mRxPermissions.shouldShowRequestPermissionRationale(mActivity, mPermissionArray)
//                                .subscribe(shouldShow -> {
//                                    if (shouldShow) {
//                                        request();
//                                    } else {
//                                        showToSettingDialog();
//                                    }
//                                });

                        if (mCallback != null) {
                            mCallback.onPermissionDenied(this);
                        }
                    }
                });
    }

    private boolean isFirstRequest(String[] permissions) {

        for (String permission : permissions) {
            boolean result = SpUtils.getInstance(SP_NAME).getBoolean(permission, true);
            if (result) {
                return true;
            }
        }

        return false;

    }

    private void changePermissionRequestNotFirst(String[] permissions) {

        for (String permission : permissions) {
            SpUtils.getInstance(SP_NAME).put(permission, false);
        }

    }

    private void showPermissionRequestDialog(String info) {

        CustomerDialog.Builder builder = new CustomerDialog.Builder(mActivity)
                .setContent(info)
                .setTextSize(16)
                .setPositiveButton(R.string.to_grant, (v -> requestInternal()))
                .setNegativeButton(v -> {
                });

        if (isForceRequest) {
            builder.hideNegativeButton().setCancellable(false);
        }

        builder.build().show();
    }

    private void showToSettingDialog() {

        CustomerDialog.Builder builder = new CustomerDialog.Builder(mActivity)
                .setContent(getPermissionSettingRequestTips(mPermissionArray))
                .setTextSize(14)
                .setPositiveButton(R.string.to_grant, (v -> {
                    goApplicationSetting();
                }));

        if (isForceRequest) {
            builder.hideNegativeButton()
                    .setCancellable(false);
        }
//
        builder.build()
                .show();

    }

    /**
     * 跳转到应用设置页面
     */
    private void goApplicationSetting() {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", AppMessageUtils.getAppPackageName(mActivity), null);
        intent.setData(uri);

        ActivityLauncher.init(mActivity)
                .startActivityForResult(intent, (resultCode, data) -> {
                    if (isForceRequest) {
                        request();
                    }
                });
    }

    /**
     * 获取前往设置页面授予权限提示信息
     *
     * @param permissions
     * @return
     */
    private String getPermissionSettingRequestTips(String[] permissions) {

        Set<String> set = new HashSet<>();
        for (String permission : permissions) {
            switch (permission) {
                case Manifest.permission.READ_PHONE_STATE:
                    set.add(StringUtils.getString(R.string.phone_code));
                    break;
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    set.add(StringUtils.getString(R.string.location));
                    break;
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    set.add(StringUtils.getString(R.string.storage));
                    break;
                case Manifest.permission.CAMERA:
                    set.add(StringUtils.getString(R.string.camera));
                    break;
            }
        }

        StringBuilder sb = new StringBuilder();
        for (String str : set) {
            sb.append(str);
            sb.append("、");
        }

        if (sb.length() <= 0) {
            return "";
        }

        sb.delete(sb.length() - 1, sb.length());
        return String.format(StringUtils.getString(R.string.to_permission_setting_tips), sb.toString());
    }


    public static class Builder {

        private RxPermissions rxPermissions;
        private FragmentActivity activity;
        private String[] permissionArray;
        private boolean isForceRequest;
        private String requestTips;
        private PermissionCallback callback;

        public Builder setRequestPermissions(String... permissions) {
            this.permissionArray = Objects.requireNonNull(permissions);
            return this;
        }

        public Builder isForceRequest(boolean isForce) {
            this.isForceRequest = isForce;
            return this;
        }

        public Builder hasRequestTips(String tips) {
            this.requestTips = tips;
            return this;
        }

        public Builder setCallback(PermissionCallback callback) {
            this.callback = callback;
            return this;
        }

        public PermissionHelper build(FragmentActivity activity) {
            rxPermissions = new RxPermissions(activity);
            this.activity = activity;
            return new PermissionHelper(this);
        }
    }

    public interface PermissionCallback {

        void onPermissionGranted();

        void onPermissionDenied(PermissionHelper permissionManager);
    }

}
