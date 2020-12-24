package com.example.study.demo.player;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.manager.ThreadPool;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerView;

public class NodeMediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private NodePlayer player;
    private final String TAG = this.getClass().getSimpleName();


    private ThreadPool.SimpleTask<Long> networkDelayDetectTask = new ThreadPool.SimpleTask<Long>() {
        private int lastPtr = 0;
        private int delayArraySize = 5;
        private SparseLongArray delayArray = new SparseLongArray(delayArraySize);

        @NotNull
        @Override
        public Long doingBackground() {
            long delay = player.getBufferPosition() - player.getCurrentPosition();
            delayArray.put(lastPtr++ % delayArraySize, delay);
            return delay;
        }

        @Override
        public void onSuccess(Long result) {
            super.onSuccess(result);
            ((TextView) findViewById(R.id.delay)).setText(String.format(Locale.CHINA, "%s\n%d", delayArray.toString(), result));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_media_player);
        NodePlayerView playerView = (NodePlayerView) findViewById(R.id.player_view);
        player = new NodePlayer(this);
        player.setPlayerView(playerView);
        player.setInputUrl("http://pull.zhuagewawa.com/record/w057.flv");
//        player.setAudioEnable(true);
        player.setBufferTime(33);
        player.setMaxBufferTime(66);
        player.setHWEnable(true);
//        player.setSubscribe(false);
        player.setNodePlayerDelegate((player, event, msg) -> {
            Log.d(TAG, event + msg);
            switch (event) {
                case 1000://正在连接视频
                    break;
                case 1001://视频连接成功
                    break;
                case 1002://视频连接失败,流地址不存在,或者本地⽹络⽆法和服务端通信,5秒后重连,可停⽌
                    break;
                case 1003://视频开始重连,⾃动重连总开关
                    break;
                case 1004://视频播放结束
                    break;
                case 1005://⽹络异常,播放中断,播放中途⽹络异常,1秒后重连,可停⽌
                    break;
            }
        });
        player.start();
        findViewById(R.id.audio_open).setOnClickListener(this);
        findViewById(R.id.audio_close).setOnClickListener(this);
        findViewById(R.id.audio_increase).setOnClickListener(this);
        findViewById(R.id.audio_decrease).setOnClickListener(this);
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);
        findViewById(R.id.is_playing).setOnClickListener(this);
        findViewById(R.id.is_live).setOnClickListener(this);
        findViewById(R.id.language).setOnClickListener(this);
        ThreadPool.executeByIoAtFixedRate(networkDelayDetectTask, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (player != null) {
//            player.setInputUrl("http://pull.zhuagewawa.com/record/w057.flv");
//            player.start();
//        }
    }

    @Override
    protected void onResume() {
        setFullScreen();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (player != null) {
            player.stop();
        }
        if (networkDelayDetectTask != null) {
            networkDelayDetectTask.cancel();
            networkDelayDetectTask = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    protected void setFullScreen() {
        Window window = getWindow();
        View view = window.getDecorView();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int mSystemUiFlag = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        view.setSystemUiVisibility(mSystemUiFlag);
        window.setNavigationBarColor(Color.TRANSPARENT);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onClick(@NotNull View v) {
        switch (v.getId()) {
            case R.id.audio_open:
                player.setAudioEnable(true);
                break;
            case R.id.audio_close:
                player.setAudioEnable(false);
                break;
            case R.id.audio_increase:
// player.setVolume(100);
                break;
            case R.id.audio_decrease:
// player.setVolume(20);
                break;
            case R.id.start:
                player.start();
                break;
            case R.id.pause:
                player.pause();
                break;
            case R.id.stop:
                player.stop();
                break;
            case R.id.refresh:
                player.stop();
                player.setInputUrl("http://pull.zhuagewawa.com/record/w057.flv");
                player.start();
                break;
            case R.id.is_playing:
                Toast.makeText(this, player.isPlaying() + "", Toast.LENGTH_LONG).show();
                break;
            case R.id.is_live:
                Toast.makeText(this, player.isLive() + "", Toast.LENGTH_LONG).show();
                break;
            case R.id.language:
                Log.d(TAG, Locale.getDefault().getLanguage() + "  " + Locale.getDefault().getCountry());
                Locale locale = new Locale("en", "US");
//                Locale.setDefault(locale);

                Resources resources = getApplicationContext().getResources();
                DisplayMetrics dm = resources.getDisplayMetrics();

                Configuration configuration = resources.getConfiguration();
                configuration.locale = Locale.ENGLISH;

                resources.updateConfiguration(configuration, dm);
                Log.d(TAG, Locale.getDefault().getLanguage() + "  " + Locale.getDefault().getCountry());
                break;
        }
    }
}