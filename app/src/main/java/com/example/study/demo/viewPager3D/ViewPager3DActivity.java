package com.example.study.demo.viewPager3D;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.study.BaseActivity;
import com.example.study.R;
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

        int pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
//        ViewGroup.LayoutParams lp = mBinding.viewPager1.getLayoutParams();
//        if (lp == null) {
//            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//        } else {
//            lp.width = pagerWidth;
//        }
//        mBinding.viewPager1.setLayoutParams(lp);


        PagerAdapter adapter = new PagerAdapter(this, fragments);
        mBinding.viewPager1.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        mBinding.viewPager1.setAdapter(adapter);
        mBinding.viewPager1.setOffscreenPageLimit(1);
        mBinding.viewPager1.setCurrentItem(999, false);

        mBinding.parent.setOnTouchListener((v, event) -> mBinding.viewPager1.dispatchTouchEvent(event));

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(300));
        transformer.addTransformer(new DepthPageTransformer());
        mBinding.viewPager1.setPageTransformer(new GallyPageTransformer());

    }
}