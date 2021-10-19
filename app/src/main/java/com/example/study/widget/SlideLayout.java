package com.example.study.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/10/18 下午1:43
 * @Company LotoGram
 */
public class SlideLayout extends FrameLayout {

    private View contentView;
    private View menuView;

    private int contentWidth;
    private int menuWidth;
    private int viewHeight;


    public SlideLayout(@NonNull Context context) {
        super(context);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //布局文件加载完成的时候，会回调这个函数
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        menuView = getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        contentWidth = contentView.getMeasuredWidth();
        menuWidth = menuView.getMeasuredWidth();
        viewHeight = getMeasuredHeight();
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //指定菜单的位置
        menuView.layout(contentWidth, 0, contentWidth + menuWidth, viewHeight);
    }
}
