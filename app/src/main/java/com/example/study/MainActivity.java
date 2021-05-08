package com.example.study;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.demo.appicon.AppIconActivity;
import com.example.study.demo.breatheLight.BreatheActivity;
import com.example.study.demo.cardViewDemo.CardViewActivity;
import com.example.study.demo.dialogFragmentDemo.DialogFragmentActivity;
import com.example.study.demo.javajs.JavaJsActivity;
import com.example.study.demo.jni.JniActivity;
import com.example.study.demo.language.LanguageActivity;
import com.example.study.demo.loadingview.LoadingViewActivity;
import com.example.study.demo.moveUnlock.MoveUnlockActivity;
import com.example.study.demo.mvvm.MVVMActivity;
import com.example.study.demo.mvvm.MVVMnewActivity;
import com.example.study.demo.notificationDemo.NotificationActivity;
import com.example.study.demo.palettePager.PalettePagerActivity;
import com.example.study.demo.player.ExoPlayerActivity;
import com.example.study.demo.player.NodeMediaPlayerActivity;
import com.example.study.demo.qrcode.QRCodeActivity;
import com.example.study.demo.refreshRecyclerView.RefreshListActivity;
import com.example.study.demo.retrofit.RetrofitActivity;
import com.example.study.demo.roundImageVIew.RoundImageViewActivity;
import com.example.study.demo.securityTest.SecurityActivity;
import com.example.study.demo.shareAnimation.ShareAnimationActivity;
import com.example.study.demo.touchListener.TouchListenerActivity;
import com.example.study.demo.viewPager3D.ViewPager3DActivity;
import com.example.study.demo.view_diy.DivViewMoveActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        findViewById(R.id.floating).setOnClickListener(view -> {
            Toast.makeText(MainActivity.this, "点击了悬浮控件", Toast.LENGTH_LONG).show();
            Log.d("DragFloatActionButton", "onClick");
        });
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

    @OnClick(R.id.javajs)
    public void toJavaJsActivity() {
        startActivity(new Intent(this, JavaJsActivity.class));
    }

    @OnClick(R.id.diy_view)
    public void toDiy_viewActivity() {
        startActivity(new Intent(this, DivViewMoveActivity.class));
    }

    @OnClick(R.id.jni)
    public void toJniActivity() {
        startActivity(new Intent(this, JniActivity.class));
    }

    @OnClick(R.id.mvvm)
    public void toMvvM() {
        startActivity(new Intent(this, MVVMActivity.class));
    }

    @OnClick(R.id.mvvmnew)
    public void TOMVVMnew() {
        startActivity(new Intent(this, MVVMnewActivity.class));
    }

    @OnClick(R.id.LoadingView)
    public void toLoadingView() {
        startActivity(new Intent(this, LoadingViewActivity.class));
    }

    @OnClick(R.id.node_media_player)
    public void toNodeMediaPlayerActivity() {
        startActivity(new Intent(this, NodeMediaPlayerActivity.class));
    }

    @OnClick(R.id.exo_player)
    public void toExoPlayerActivity() {
        startActivity(new Intent(this, ExoPlayerActivity.class));
    }

    @OnClick(R.id.language_setting)
    public void toLanguageActivity() {
        startActivity(new Intent(this, LanguageActivity.class));
    }

    @OnClick(R.id.security)
    public void toSecurityActivity() {
        startActivity(new Intent(this, SecurityActivity.class));
    }

    @OnClick(R.id.palette)
    public void toPaletteActivity() {
        startActivity(new Intent(this, PalettePagerActivity.class));
    }

    @OnClick(R.id.breathe)
    public void toBreatheActivity() {
        startActivity(new Intent(this, BreatheActivity.class));
    }

    @OnClick(R.id.appicon)
    public void toAppiconActivity() {
        startActivity(new Intent(this, AppIconActivity.class));
    }

    @OnClick(R.id.move_unlock)
    public void toMoveUnlockActivity() {
        startActivity(new Intent(this, MoveUnlockActivity.class));
    }

    @OnClick(R.id.share_animation)
    public void toShareAnimationActivity() {
        startActivity(new Intent(this, ShareAnimationActivity.class));
    }

    @OnClick(R.id.view_pager_3d)
    public void to3DViewPagerActivity() {
        startActivity(new Intent(this, ViewPager3DActivity.class));
    }

    @OnClick(R.id.round_image_view)
    public void toRoundImageViewActivity() {
        startActivity(new Intent(this, RoundImageViewActivity.class));
    }
}