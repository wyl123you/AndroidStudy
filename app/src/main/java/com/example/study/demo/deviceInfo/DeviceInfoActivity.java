package com.example.study.demo.deviceInfo;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.manager.MacAddressUtil;

public class DeviceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        ((TextView) findViewById(R.id.brand)).setText(Build.BRAND);
        ((TextView) findViewById(R.id.mac)).setText(MacAddressUtil.getMac(this));
    }
}