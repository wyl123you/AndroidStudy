package com.example.study.demo.palettePager;

import android.animation.ArgbEvaluator;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityPaletteBinding;
import com.example.study.demo.palettePager.fragment.FragmentA;
import com.example.study.demo.palettePager.fragment.FragmentB;
import com.example.study.demo.palettePager.fragment.FragmentC;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PalettePagerActivity extends BaseActivity<ActivityPaletteBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_palette;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "PalettePagerActivity";
    }

    @Override
    protected void initViews() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentC());
        PagerAdapter adapter = new PagerAdapter(this, fragments);
        mBinding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mBinding.viewPager.setAdapter(adapter);
        mBinding.viewPager.setOffscreenPageLimit(1);
        mBinding.viewPager.setCurrentItem(999, false);
        mBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.d(TAG, "onPageScrolled: position:" + position);
                Log.d(TAG, "onPageScrolled: positionOffset:" + positionOffset);
                Log.d(TAG, "onPageScrolled: positionOffsetPixels:" + positionOffsetPixels);

                mBinding.viewPager.destroyDrawingCache();
                mBinding.viewPager.setDrawingCacheEnabled(true);
                mBinding.viewPager.buildDrawingCache();
                Bitmap bitmap = mBinding.viewPager.getDrawingCache();
                Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
                mBinding.viewPager.destroyDrawingCache();

                Palette.from(bitmap1).generate(palette -> {
                    if (palette == null) return;
                    Palette.Swatch swatch = palette.getDominantSwatch();
                    if (swatch == null) return;
                    mBinding.palette.setBackgroundColor(swatch.getRgb());
                });

                ArgbEvaluator evaluator = new ArgbEvaluator();//ARGB求值器
                int evaluate;//初始默认颜色透明白）
                switch (position % fragments.size()) {
                    case 0:
                        //根据positionOffset和第0页~第1页的颜色转换范围取颜色值
                        evaluate = (Integer) evaluator.evaluate(positionOffset, 0XFFFF8080, 0XFFFFBC00);
                        break;
                    case 1:
                        //根据positionOffset和第0页~第1页的颜色转换范围取颜色值
                        evaluate = (Integer) evaluator.evaluate(positionOffset, 0XFFFFBC00, 0XFF199AFE);
                        break;
                    case 2:
                        //根据positionOffset和第0页~第1页的颜色转换范围取颜色值
                        evaluate = (Integer) evaluator.evaluate(positionOffset, 0XFF199AFE, 0XFFFF8080);
                        break;
                    default:
                        evaluate = 0XFF00AB96;
                        break;
                }
                mBinding.color.setBackgroundColor(evaluate);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "当前页: " + position);
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        Log.d(TAG, "用户正在滑动");
                        break;
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        Log.d(TAG, "非用户滑动");
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:
                        Log.d(TAG, "切换页面动画结束，或者处于idle闲置状态");
                        break;
                    default:
                        break;
                }
            }
        });

        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Interpolator sInterpolator = new AccelerateDecelerateInterpolator();
            FixedScroller scroller = new FixedScroller(mBinding.viewPager.getContext(), sInterpolator);
            mScroller.set(mBinding.viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int position = mBinding.viewPager.getCurrentItem();
                mBinding.viewPager.setCurrentItem(position + 1, true);
            }
        }).start();
    }
}