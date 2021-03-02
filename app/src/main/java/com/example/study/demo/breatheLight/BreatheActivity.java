package com.example.study.demo.breatheLight;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.View;
import android.view.Window;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityBreatheBinding;

import java.util.Random;

public class BreatheActivity extends BaseActivity<ActivityBreatheBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_breathe;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "BreatheActivity";
    }

    @Override
    protected void initViews() {
        trans(mBinding.background, getColor(), getColor());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Window window = getWindow();
//        window.setNavigationBarColor(Color.TRANSPARENT);
//        window.setStatusBarColor(Color.TRANSPARENT);
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View view = window.getDecorView();
        int mSystemUiFlag =
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
        view.setSystemUiVisibility(mSystemUiFlag);
    }

    private void trans(View view, int startColor, int endColor) {
        ValueAnimator animator = new ValueAnimator();
        animator.setIntValues(0, 100);
        animator.setDuration(4000);
        animator.addUpdateListener(valueAnimator -> {
            int currentValue = (int) valueAnimator.getAnimatedValue();


            int r1 = Color.red(startColor);
            int g1 = Color.green(startColor);
            int b1 = Color.blue(startColor);


            int r2 = Color.red(endColor);
            int g2 = Color.green(endColor);
            int b2 = Color.blue(endColor);

            int r3 = r1 + ((r2 - r1) * currentValue / 100);
            int g3 = g1 + ((g2 - g1) * currentValue / 100);
            int b3 = b1 + ((b2 - b1) * currentValue / 100);

            int color1 = Color.rgb(r3, g3, b3);

//            if (currentValue != this.currentValue) {
////                Log.d(TAG, "onAnimationUpdate: " + currentValue);
////                Log.d(TAG, "起始值： " + r1 + "  " + g1 + "  " + b1);
////                Log.d(TAG, "中间值： " + r3 + "  " + g3 + "  " + b3);
////                Log.d(TAG, "结束值： " + r2 + "  " + g2 + "  " + b2);
//                this.currentValue = currentValue;
//            }

            view.setBackgroundColor(color1);
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                trans(view, endColor, getColor());
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.start();
    }

    private int getColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        return Color.rgb(r, g, b);
    }
}