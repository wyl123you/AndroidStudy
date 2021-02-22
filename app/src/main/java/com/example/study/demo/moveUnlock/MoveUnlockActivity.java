package com.example.study.demo.moveUnlock;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityMoveUnlockBinding;

public class MoveUnlockActivity extends BaseActivity<ActivityMoveUnlockBinding> {

    //滑块
    private SeekBar mSeekBar;
    //自定义的控件
    private ImageAuthenticationView mDY;
    private Button btn;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_move_unlock;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "MoveUnlockActivity";
    }

    @Override
    protected void initViews() {

        mDY = findViewById(R.id.dy_v);
        mSeekBar = findViewById(R.id.sb_dy);
        btn = findViewById(R.id.btn);


        //滑块监听
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //设置滑块移动距离
                mDY.setUnitMoveDistance(mDY.getAverageDistance(seekBar.getMax()) * i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //验证是否拼接成功
                mDY.testPuzzle();
            }
        });

        //控件监听
        mDY.setPuzzleListener(new ImageAuthenticationView.onPuzzleListener() {
            @Override
            public void onSuccess() {
                //mSeekBar.setEnabled(false);//禁止滑动
                Toast.makeText(MoveUnlockActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(MoveUnlockActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);
            }
        });

        //重置
        btn.setOnClickListener(v -> {
            //mSeekBar.setEnabled(true);
            mSeekBar.setProgress(0);
            mDY.reSet();
        });
    }
}