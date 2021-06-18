package com.example.study.demo.securityTest;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivitySecurityBinding;

import org.jetbrains.annotations.NotNull;

public class SecurityActivity extends BaseActivity<ActivitySecurityBinding> {

    private CancellationSignal cancellationSignal = new CancellationSignal();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_security;
    }

    @Override
    protected boolean useDataBinding() {
        return false;
    }

    @Override
    protected String getTag() {
        return null;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "密码正确", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "密码错误", Toast.LENGTH_LONG).show();
            }
        }
    }

    //密码解锁
    public void onSecret(View view) {
        KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        Intent intent = manager.createConfirmDeviceCredentialIntent("finger", "test");
        if (intent == null) {
            Toast.makeText(this, "尚未设置密码解锁", Toast.LENGTH_LONG).show();
        } else {
            startActivityForResult(intent, 1000);
        }
    }

    //指纹解锁
    public void onFinger(View view) {
        Log.d(TAG, "onFinger: ");
        Log.d(TAG, Build.VERSION.SDK_INT + "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            BiometricPrompt prompt = new BiometricPrompt.Builder(this)
                    .setTitle("指纹识别")
                    .setSubtitle("指纹识别副标题")
                    .setDescription("描述")
                    .setNegativeButton("取消", getMainExecutor(), (dialog, which) -> {
                        Toast.makeText(SecurityActivity.this, "quxiao", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "取消");
                    })
                    .build();
            prompt.authenticate(cancellationSignal, getMainExecutor(), new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(SecurityActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//安卓6及以上
            FingerprintManager manager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
            if (manager == null) {
                Toast.makeText(this, "无法获取指纹服务", Toast.LENGTH_LONG).show();
                return;
            }
            if (manager.isHardwareDetected()) {//是否有硬件支持
                Toast.makeText(this, "设备检测到指纹模块", Toast.LENGTH_LONG).show();
                if (manager.hasEnrolledFingerprints()) {//至少录入一个指纹
                    Toast.makeText(this, "有录入指纹,请将手指放到传感器上", Toast.LENGTH_LONG).show();
                    findViewById(R.id.stopFinger).setEnabled(true);
                    if (cancellationSignal.isCanceled())
                        cancellationSignal = new CancellationSignal();
                    //启动指纹识别(第一个参数：指纹识别数据传输加密对象，第四个：指纹识别成功、失败、错误回调接口)
                    manager.authenticate(null, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                        @Override
                        public void onAuthenticationError(int errorCode, CharSequence errString) {
                            Log.d(TAG, "onAuthenticationError: " + errString);
                            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
                            Toast.makeText(SecurityActivity.this, errString, Toast.LENGTH_SHORT).show();
                            KeyguardManager manager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
                            Intent intent = manager.createConfirmDeviceCredentialIntent("finger", "test");
                            startActivityForResult(intent, 1000);
                        }

                        @Override
                        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                            Log.d(TAG, "onAuthenticationHelp: " + helpString);
                            Toast.makeText(SecurityActivity.this, helpString, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onAuthenticationSucceeded(@NotNull FingerprintManager.AuthenticationResult result) {
                            //AuthenticationResult:指纹识别结果封装，从回调接口(AuthenticationCallback)里面传回来
                            Toast.makeText(SecurityActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
                            findViewById(R.id.stopFinger).setEnabled(false);
                        }

                        @Override
                        public void onAuthenticationFailed() {
                            Toast.makeText(SecurityActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
                        }
                    }, null);
                } else {
                    Toast.makeText(this, "没有录入指纹", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "设备未检测到指纹模块", Toast.LENGTH_LONG).show();
            }
        } else {//安卓6以下
            Toast.makeText(this, "安卓6以下暂不支持(因为采用非google官方api)", Toast.LENGTH_LONG).show();
        }
    }

    public void onStopFinger(View view) {
        if (!cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
            cancellationSignal.setOnCancelListener(() -> {
                Log.d(TAG, "取消");
                findViewById(R.id.stopFinger).setEnabled(false);
            });
        }
    }

    //面部解锁
    public void onFace(View view) {
        Toast.makeText(this, "暂未完成该功能", Toast.LENGTH_LONG).show();
    }
}