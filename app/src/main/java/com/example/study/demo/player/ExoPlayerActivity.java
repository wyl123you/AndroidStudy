package com.example.study.demo.player;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseLongArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.manager.ThreadPool;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ExoPlayerActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private StyledPlayerView playerView;
    private MediaItem mediaItem;

    private boolean playWhenReady;
    private int currentWindow;
    private long playbackPosition;

    private ThreadPool.SimpleTask<Long> networkDelayDetectTask = new ThreadPool.SimpleTask<Long>() {
        private int lastPtr = 0;
        private int delayArraySize = 5;
        private SparseLongArray delayArray = new SparseLongArray(delayArraySize);

        @NotNull
        @Override
        public Long doingBackground() {
            long delay = player.getBufferedPosition() - player.getCurrentPosition();
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
        setContentView(R.layout.activity_exo_player);

        playerView = findViewById(R.id.play_view);

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);
        playerView.setUseController(false);

        mediaItem = MediaItem.fromUri("http://pull.zhuagewawa.com/record/w058.flv");

//        mediaItem = MediaItem.fromUri(RawResourceDataSource.buildRawResourceUri(R.raw.aaaaa));
        mediaItem = MediaItem.fromUri("rawresource:///" + R.raw.aaaaa);

        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();

        ThreadPool.executeByIoAtFixedRate(networkDelayDetectTask, 0, 1000, TimeUnit.MILLISECONDS);

//        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                player.setMediaSource(getRawMediaSource());
//            }
//        });
    }

    @NotNull
    @Deprecated
    private MediaSource getRawMediaSource() {
        RawResourceDataSource rawResourceDataSource = new RawResourceDataSource(this);
        Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.aaaaa);

        DataSpec dataSpec = new DataSpec(uri);
        try {
            rawResourceDataSource.open(dataSpec);
        } catch (RawResourceDataSource.RawResourceDataSourceException e) {
            e.printStackTrace();
        }

        DataSource.Factory factory = () -> rawResourceDataSource;

        ExtractorMediaSource mediaSource = new ExtractorMediaSource(rawResourceDataSource.getUri(),
                factory, new DefaultExtractorsFactory(), null, null);
        LoopingMediaSource loopingMediaSource = new LoopingMediaSource(mediaSource, 6);
        return loopingMediaSource;
    }

    @NotNull
    @Deprecated
    private MediaSource getHttpMediaSource() {
        Uri uri = Uri.parse("http://pull.zhuagewawa.com/record/w058.flv");
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "飞星"), new DefaultBandwidthMeter());
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        return new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, new Handler(), new ExtractorMediaSource.EventListener() {
            @Override
            public void onLoadError(IOException error) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFullScreen();
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
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
    protected void onPause() {
        super.onPause();
        player.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        if (networkDelayDetectTask != null) {
            networkDelayDetectTask.cancel();
            networkDelayDetectTask = null;
        }
    }
}