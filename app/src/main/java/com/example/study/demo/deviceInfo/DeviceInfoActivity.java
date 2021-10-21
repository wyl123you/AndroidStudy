package com.example.study.demo.deviceInfo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.nfc.NfcAdapter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bun.miitmdid.core.IIdentifierListener;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.supplier.IdSupplier;
import com.example.study.R;
import com.example.study.manager.MacAddressUtil;

import net.vidageek.mirror.dsl.Mirror;

import org.jetbrains.annotations.NotNull;

public class DeviceInfoActivity extends AppCompatActivity {

    private static final String TAG = "DeviceInfoActivity";

    private StringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        builder = new StringBuilder();

        builder.append("品牌: ").append(Build.BRAND).append("\n");
        builder.append("WLAN MAC: ").append(MacAddressUtil.getMac(this)).append("\n");

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            String blueToothMac = adapter.getAddress();
            Log.d(TAG, "blueToothMac: " + blueToothMac);
            builder.append("蓝牙 MAC: ").append(blueToothMac).append("\n");
        } else {
//            Class<? extends BluetoothAdapter> btAdapterClass = adapter.getClass();
//            try {
//                Class<?> btClass = Class.forName("android.bluetooth.IBluetooth");
//                Field bluetooth = btAdapterClass.getDeclaredField("mService");
//                bluetooth.setAccessible(true);
//                Method method = btClass.getMethod("getAddress");
//                Method method1 = bluetooth.get(adapter).getClass().getMethod("getAddress");
//                String address = (String) bluetooth.get(adapter).getClass().getMethod("getAddress").invoke(bluetooth.get(adapter));
//                ((TextView) findViewById(R.id.blue_tooth)).setText(address);
//            } catch (Exception e) {
//                e.printStackTrace();
//                ((TextView) findViewById(R.id.blue_tooth)).setText("获取失败");
//            }
            builder.append("蓝牙 MAC: ").append("暂不支持安卓6以上").append("\n");
        }

        //是否支持红外
        ConsumerIrManager cim = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        builder.append("红外支持: ").append(cim.hasIrEmitter() ? "是" : "否").append("\n");

        //FNC支持
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        builder.append("NFC支持: ").append(nfcAdapter != null ? "是" : "否");
        if (nfcAdapter != null) {
            builder.append("            NFC是否启用: ").append(nfcAdapter.isEnabled() ? "是" : "否");
        }

        builder.append("\n-------------------------------------------------------------------------------\n");

        //电量
        BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        builder.append("电量: ").append(battery).append("%").append("\n");
        if (Build.VERSION.SDK_INT > 23) {
            builder.append("正在充电: ").append(batteryManager.isCharging() ? "是" : "否").append("\n");
        }

        //ime 参数，android10以下imei，10包括10以上 oaid
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            int result = MdidSdkHelper.InitSdk(this, true, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean b, IdSupplier idSupplier) {
                    if (idSupplier != null && idSupplier.isSupported()) {
                        builder.append("OAID: ").append(idSupplier.getOAID()).append("\n");
                        Log.d(TAG, "OAID: " + idSupplier.getOAID());
                    }
                }
            });
            Log.d(TAG, "result: " + result);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.d(TAG, "PhoneCount: " + telephonyManager.getPhoneCount());
                for (int slot = 0; slot < telephonyManager.getPhoneCount(); slot++) {
                    String imei = telephonyManager.getDeviceId(slot);
                    builder.append("IMEI: ").append(imei).append("\n");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((TextView) findViewById(R.id.info)).setText(builder.toString());
    }

    @NotNull
    private String getBlueToothMacAddress() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Object service = new Mirror().on(adapter).get().field("mService");
        if (service == null) {
            return "null";
        }

        Object address = new Mirror().on(service).invoke().method("getAddress").withoutArgs();
        if (address instanceof String) {
            return (String) address;
        } else {
            return "null";
        }
    }
}