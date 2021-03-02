package com.example.study.demo.moveUnlock;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityMoveUnlockBinding;

public class MoveUnlockActivity extends BaseActivity<ActivityMoveUnlockBinding> {

    //滑块
    private SeekBar mSeekBar;
    private SeekBar mSeekBar1;
    //自定义的控件
    private ImageAuthenticationView mDY;
    private ImageAuthenticationView1 mDY1;
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
        mDY1 = findViewById(R.id.aaa);
        mSeekBar = findViewById(R.id.sb_dy);
        mSeekBar1 = findViewById(R.id.sb_dy1);
        btn = findViewById(R.id.btn);

        //滑块监听
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置滑块移动距离
                mDY.setUnitMoveDistance(mDY.getAverageDistance(seekBar.getMax()) * progress);
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
                mDY.reset();
                mSeekBar.setProgress(0);
            }

            @Override
            public void onFail() {
                Toast.makeText(MoveUnlockActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                mSeekBar.setProgress(0);
            }
        });

        //滑块监听
        mSeekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //设置滑块移动距离
                mDY1.setUnitMoveDistance(mDY1.getAverageDistance(seekBar.getMax()) * progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //验证是否拼接成功
                mDY1.testPuzzle();
            }
        });

        //控件监听
        mDY1.setPuzzleListener(new ImageAuthenticationView1.OnPuzzleListener() {
            @Override
            public void onSuccess() {
                //mSeekBar.setEnabled(false);//禁止滑动
                Toast.makeText(MoveUnlockActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                mDY1.reset();
                mSeekBar1.setProgress(0);
            }

            @Override
            public void onFail() {
                Toast.makeText(MoveUnlockActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                mSeekBar1.setProgress(0);
            }
        });

        //重置
        btn.setOnClickListener(v -> {
            //mSeekBar.setEnabled(true);
            mSeekBar.setProgress(0);
            mSeekBar1.setProgress(0);
            mDY.reset();
            mDY1.reset();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mSeekBar.setMax(mDY.getMaxProgress());
//        mSeekBar1.setMax(mDY1.getMaxProgress());
    }
}