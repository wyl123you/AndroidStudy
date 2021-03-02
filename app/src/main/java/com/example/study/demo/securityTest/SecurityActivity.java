package com.example.study.demo.securityTest;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivitySecurityBinding;

public class SecurityActivity extends BaseActivity<ActivitySecurityBinding> {

    @Override
    protected void initViews() {
        keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        manager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
    }

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

    CancellationSignal signal = new CancellationSignal();

    KeyguardManager keyguardManager;
    FingerprintManager manager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "识别成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "识别失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onSecret(View view) {
        Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("finger", "test");
        startActivityForResult(intent, 1000);
    }

    public void onFace(View view) {

    }

    FingerprintManager.AuthenticationCallback mSelfCancelled = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
            Toast.makeText(SecurityActivity.this, errString, Toast.LENGTH_SHORT).show();
            //showAuthenticationScreen();
            Intent intent = keyguardManager.createConfirmDeviceCredentialIntent("finger", "test");
            startActivityForResult(intent, 1000);
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            Toast.makeText(SecurityActivity.this, helpString, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            Toast.makeText(SecurityActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationFailed() {
            Toast.makeText(SecurityActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
        }
    };

    public void onFinger(View view) {
        Log.d(TAG, "onFinger: ");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, 0);
                return;
            }
            if (!manager.isHardwareDetected()) {
                Toast.makeText(this, "未检测到指纹模块", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "检测到指纹模块", Toast.LENGTH_LONG).show();
            }
            if (!manager.hasEnrolledFingerprints()) {
                Toast.makeText(this, "没有录入指纹", Toast.LENGTH_LONG).show();
                return;
            } else {
                Toast.makeText(this, "有录入指纹,请将手指放到传感器上", Toast.LENGTH_LONG).show();
            }
            manager.authenticate(null, signal, 0, mSelfCancelled, null);
        }
    }
}