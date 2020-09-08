package com.example.study;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.demo.addressPicker.AddressPickerActivity;
import com.example.study.demo.cardViewDemo.CardViewActivity;
import com.example.study.demo.dialogFragmentDemo.DialogFragmentActivity;
import com.example.study.demo.javajs.JavaJsActivity;
import com.example.study.demo.jni.JniActivity;
import com.example.study.demo.notificationDemo.NotificationActivity;
import com.example.study.demo.qrcode.QRCodeActivity;
import com.example.study.demo.refreshRecyclerView.RefreshListActivity;
import com.example.study.demo.retrofit.RetrofitActivity;
import com.example.study.demo.theme.ThemeActivity;
import com.example.study.demo.touchListener.TouchListenerActivity;
import com.example.study.demo.view_diy.DivViewMoveActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.retrofit)
    public void toRetrofitActivity() {
        startActivity(new Intent(this, RetrofitActivity.class));
    }

    @OnClick(R.id.refresh_recycler_list)
    public void toRefreshRecyclerActivity() {
        startActivity(new Intent(this, RefreshListActivity.class));
    }

    @OnClick(R.id.qr_code)
    public void toQRCodeActivity() {
        startActivity(new Intent(this, QRCodeActivity.class));
    }

    @OnClick(R.id.dialog_fragment)
    public void toDialogFragmentActivity() {
        startActivity(new Intent(this, DialogFragmentActivity.class));
    }

    @OnClick(R.id.cardView)
    public void toCardViewActivity() {
        startActivity(new Intent(this, CardViewActivity.class));
    }

    @OnClick(R.id.notification)
    public void toNotificationActivity() {
        startActivity(new Intent(this, NotificationActivity.class));
    }

    @OnClick(R.id.ThreadPool)
    public void toThreadPoolActivity() {
        startActivity(new Intent(this, TouchListenerActivity.class));
    }

    @OnClick(R.id.address_picker)
    public void toAddressPickerActivity() {
        startActivity(new Intent(this, AddressPickerActivity.class));
    }

    @OnClick(R.id.javajs)
    public void toJavaJsActivity() {
        startActivity(new Intent(this, JavaJsActivity.class));
    }

    @OnClick(R.id.diy_view)
    public void todiy_viewActivity() {
        startActivity(new Intent(this, DivViewMoveActivity.class));
    }

    @OnClick(R.id.theme)
    public void tothemeActivity() {
        startActivity(new Intent(this, ThemeActivity.class));
    }

    @OnClick(R.id.jni)
    public void tojniActivity() {
        startActivity(new Intent(this, JniActivity.class));
    }
}