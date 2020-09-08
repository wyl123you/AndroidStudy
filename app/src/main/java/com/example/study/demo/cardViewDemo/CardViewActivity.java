package com.example.study.demo.cardViewDemo;

import android.widget.SeekBar;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityCardViewBinding;

public class CardViewActivity extends BaseActivity<ActivityCardViewBinding> implements SeekBar.OnSeekBarChangeListener {
    @Override
    protected void initViews() {

        mBinding.sb1.setOnSeekBarChangeListener(this);
        mBinding.sb2.setOnSeekBarChangeListener(this);
        mBinding.sb3.setOnSeekBarChangeListener(this);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_card_view;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "CardViewActivity";
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb1:
                mBinding.cardView.setRadius(progress);
                break;
            case R.id.sb2:
                mBinding.cardView.setCardElevation(progress);
                break;
            case R.id.sb3:
                mBinding.cardView.setContentPadding(progress, progress, progress, progress);
                break;
            default:
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}