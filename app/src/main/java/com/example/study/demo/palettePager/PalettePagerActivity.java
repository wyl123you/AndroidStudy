package com.example.study.demo.palettePager;

import android.animation.ArgbEvaluator;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityPaletteBinding;
import com.example.study.demo.palettePager.fragment.FragmentA;
import com.example.study.demo.palettePager.fragment.FragmentB;
import com.example.study.demo.palettePager.fragment.FragmentC;
import com.example.study.manager.ImageUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.transformer.AlphaPageTransformer;

public class PalettePagerActivity extends BaseActivity<ActivityPaletteBinding> {

    private final ArrayList<String> bannerImgs = new ArrayList<>();
    private final ArrayList<Integer> colorList = new ArrayList<>();

    private boolean isInit = true;

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
                //Log.d(TAG, "onPageScrolled: position:" + position);
                //Log.d(TAG, "onPageScrolled: positionOffset:" + positionOffset);
                //Log.d(TAG, "onPageScrolled: positionOffsetPixels:" + positionOffsetPixels);

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
                //Log.d(TAG, "当前页: " + position);
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                        //Log.d(TAG, "用户正在滑动");
                        break;
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        //Log.d(TAG, "非用户滑动");
                        break;
                    case ViewPager2.SCROLL_STATE_IDLE:
                        //Log.d(TAG, "切换页面动画结束，或者处于idle闲置状态");
                        break;
                    default:
                        break;
                }
            }
        });

        colorList.add(0X999999);
        colorList.add(0X999999);
        colorList.add(0X999999);
        colorList.add(0X999999);
        colorList.add(0X999999);

        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532665664&di=9ead9eb8a9fe2af9a01b0dd39f3e41f4&imgtype=0&src=http%3A%2F%2Fbpic.588ku.com%2Fback_pic%2F05%2F37%2F28%2F475a43591370453.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532613936&di=3769695217e3424f18c3d23966ecd4dc&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fback_pic%2Fqk%2Fback_origin_pic%2F00%2F04%2F19%2F70e2846ebc02ae10161f25bf7f5461a1.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532613934&di=0be1c6bbf0441bd19ef6d4e3ce799263&imgtype=0&src=http%3A%2F%2Fpic96.nipic.com%2Ffile%2F20160430%2F7036970_215739900000_2.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532613936&di=4dd453940f49d9801826e6b820490957&imgtype=0&src=http%3A%2F%2Fpic161.nipic.com%2Ffile%2F20180410%2F26429156_154754410034_2.jpg");
        bannerImgs.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1556532613935&di=39c387012e3d8fa2eef90129eaf83c5c&imgtype=0&src=http%3A%2F%2Fpic25.nipic.com%2F20121211%2F7031681_170238437383_2.jpg");

        mBinding.banner.setAdapter((BGABanner.Adapter<ImageView, String>) (bgaBanner, itemView, banner, position) -> {
            ImageUtil.setRoundRectCorner(itemView, 20);
            if (banner != null && bannerImgs.size() > position) {
                Glide.with(this).asBitmap().load(banner).listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        setColorList(resource, position);

                        if (position == 0) {

                            int vibrantColor = colorList.get(0);
                            mBinding.color1.setBackgroundColor(vibrantColor);

                            if (isInit) {
                                mBinding.palette.setBackgroundColor(vibrantColor);
                                isInit = false;
                            }
                        }

                        return false;
                    }
                }).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(itemView);
            }
        });
        mBinding.banner.setData(bannerImgs, null);
        mBinding.banner.setPageTransformer(new AlphaPageTransformer());


        mBinding.banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: position" + position);
                int vibrantColor = ColorUtils.blendARGB(colorList.get(position % colorList.size()), colorList.get((position + 1) % colorList.size()), positionOffset);
                mBinding.palette.setBackgroundColor(vibrantColor);
                Log.d(TAG, "onPageScrolled: vibrantColor" + vibrantColor);
                Log.d(TAG, "Millis: " + System.currentTimeMillis());
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBinding.color1.setBackgroundColor(colorList.get(position));
                        break;
                    case 1:
                        mBinding.color2.setBackgroundColor(colorList.get(position));
                        break;
                    case 2:
                        mBinding.color3.setBackgroundColor(colorList.get(position));
                        break;
                    case 3:
                        mBinding.color4.setBackgroundColor(colorList.get(position));
                        break;
                    case 4:
                        mBinding.color5.setBackgroundColor(colorList.get(position));
                        break;
                }
                Log.d(TAG, "onPageSelected Millis: " + System.currentTimeMillis());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        try {
            Field duration = BGABanner.class.getDeclaredField("mPageChangeDuration");
            duration.setAccessible(true);
            duration.set(mBinding.banner, 5000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int position = mBinding.viewPager.getCurrentItem();
                runOnUiThread(() -> MyPagerHelper.setCurrentItem(mBinding.viewPager, position + 1, 1000));
//                mBinding.viewPager.setCurrentItem(position + 1, true);
            }
        }).start();
    }

    private void setColorList(Bitmap bitmap, int position) {
        if (colorList == null) {
            return;
        }
        Palette palette = Palette.from(bitmap).generate();
        if (palette.getVibrantSwatch() != null) {
            colorList.set(position, palette.getVibrantSwatch().getRgb());
        } else if (palette.getDarkVibrantSwatch() != null) {
            colorList.set(position, palette.getDarkVibrantSwatch().getRgb());
        } else if (palette.getLightVibrantSwatch() != null) {
            colorList.set(position, palette.getLightVibrantSwatch().getRgb());
        } else if (palette.getMutedSwatch() != null) {
            colorList.set(position, palette.getMutedSwatch().getRgb());
        } else if (palette.getDarkMutedSwatch() != null) {
            colorList.set(position, palette.getDarkMutedSwatch().getRgb());
        } else if (palette.getLightVibrantSwatch() != null) {
            colorList.set(position, palette.getLightVibrantSwatch().getRgb());
        }
    }
}