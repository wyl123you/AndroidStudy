package com.example.study.viewPager3D;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.Activity3dViewpagerBinding;
import com.example.study.demo.palettePager.PagerAdapter;
import com.example.study.demo.palettePager.fragment.FragmentA;
import com.example.study.demo.palettePager.fragment.FragmentB;
import com.example.study.demo.palettePager.fragment.FragmentC;
import com.example.study.demo.palettePager.fragment.FragmentD;
import com.example.study.demo.palettePager.fragment.FragmentE;
import com.example.study.demo.palettePager.fragment.FragmentF;

import java.util.ArrayList;

public class ViewPager3DActivity extends BaseActivity<Activity3dViewpagerBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_3d_viewpager;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "ViewPager3DActivity";
    }

    @Override
    protected void initViews() {

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentA());
        fragments.add(new FragmentB());
        fragments.add(new FragmentC());
        fragments.add(new FragmentD());
        fragments.add(new FragmentE());
        fragments.add(new FragmentF());
        PagerAdapter adapter = new PagerAdapter(this, fragments);
        mBinding.viewPager1.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mBinding.viewPager1.setAdapter(adapter);
        mBinding.viewPager1.setOffscreenPageLimit(2);
        mBinding.viewPager1.setCurrentItem(999, false);

        mBinding.viewPager1.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                Log.d(TAG, "position: " + position);
            }
        });

    }
}