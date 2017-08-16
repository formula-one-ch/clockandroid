package com.example.jiangbingxuan.myclock;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Arrays;

import utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private final String[] PERMISSIONS = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

    // 多个权限请求Code
    private final int REQUEST_CODE_PERMISSIONS=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        setContentView(R.layout.activity_main);
        PermissionUtils.checkMorePermissions(this, PERMISSIONS, new PermissionUtils.PermissionCheckCallBack() {
            @Override
            public void onHasPermission() {
                start();
            }

            @Override
            public void onUserHasAlreadyTurnedDown(String... permission) {
                showExplainDialog(permission, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
                    }
                });
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                PermissionUtils.requestMorePermissions(mContext, PERMISSIONS, REQUEST_CODE_PERMISSIONS);
            }
        });
    }
    private void start(){
        //跳转时钟页面
        Intent intent=new Intent();
        intent.setClass(this,ClockActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 解释权限的dialog
     *
     */
    private void showExplainDialog(String[] permission, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(this)
                .setTitle("申请权限")
                .setMessage("我们需要" + Arrays.toString(permission)+"权限")
                .setPositiveButton("确定", onClickListener)
                .show();
    }
    /**
     * 显示前往应用设置Dialog
     *
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSIONS:
                PermissionUtils.onRequestMorePermissionsResult(mContext, PERMISSIONS, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        start();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要"+Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要"+ Arrays.toString(permission)+"权限", Toast.LENGTH_SHORT).show();
                        showToAppSettingDialog();
                    }
                });


        }
    }
}
