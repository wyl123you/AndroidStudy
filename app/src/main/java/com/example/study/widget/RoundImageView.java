package com.example.study.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/4/26 下午5:35
 * @Company LotoGram
 */

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView implements Runnable {

    private final String TAG = this.getClass().getSimpleName();

    private final Path path = new Path();
    private final Paint paint = new Paint();
    private final float[] rads = new float[]{28, 28, 28, 28, 28, 28, 28, 28};
    private final RectF rectF = new RectF(0, 0, 0, 0);

    private static volatile float startAngle;

    private boolean isCircle;
    private float radius;
    private boolean borderEnable;
    private int borderColor;
    private float borderWidth;

    private float topLeftRadius;
    private float topRightRadius;
    private float bottomLeftRadius;
    private float bottomRightRadius;

    public RoundImageView(@NonNull Context context) {
        super(context);
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        isCircle = ta.getBoolean(R.styleable.RoundImageView_circle, false);
        radius = ta.getDimension(R.styleable.RoundImageView_radius, 0);
        topLeftRadius = ta.getDimension(R.styleable.RoundImageView_topLeftRadius, radius);
        topRightRadius = ta.getDimension(R.styleable.RoundImageView_topRightRadius, radius);
        bottomLeftRadius = ta.getDimension(R.styleable.RoundImageView_bottomLeftRadius, radius);
        bottomRightRadius = ta.getDimension(R.styleable.RoundImageView_bottomRightRadius, radius);
        borderEnable = ta.getBoolean(R.styleable.RoundImageView_borderEnable, false);
        borderColor = ta.getColor(R.styleable.RoundImageView_borderColor, Color.RED);
        borderWidth = ta.getDimension(R.styleable.RoundImageView_borderWidth, 5f);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setScaleType(ScaleType.CENTER_CROP);

        //当在xml或者代码中给子View设置了具体的dimen值，那么MeasureSpec.getSiz()获得对应的像素值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        Log.d(TAG, "width: " + width);
        Log.d(TAG, "height: " + height);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int finalWidth = width;
        int finalHeight = height;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "MeasureSpec Mode: EXACTLY");
                Log.d(TAG, "子视图为match_parent或具体数值");
                finalWidth = width;
                break;
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "MeasureSpec Mode: AT_MOST");
                Log.d(TAG, "子视图为wrap_content");
                Log.d(TAG, "子视图当前可以使用的最大空间大小: " + width);
                finalWidth = ((BitmapDrawable) getDrawable()).getIntrinsicWidth();
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "MeasureSpec Mode: UNSPECIFIED");
                Log.d(TAG, "子视图没有大小限制");
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                Log.d(TAG, "MeasureSpec Mode: EXACTLY");
                Log.d(TAG, "子视图为match_parent或具体数值");
                finalHeight = height;
                break;
            case MeasureSpec.AT_MOST:
                Log.d(TAG, "MeasureSpec Mode: AT_MOST");
                Log.d(TAG, "子视图为wrap_content");
                Log.d(TAG, "子视图当前可以使用的最大空间大小: " + width);
                finalHeight = ((BitmapDrawable) getDrawable()).getIntrinsicHeight();
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.d(TAG, "MeasureSpec Mode: UNSPECIFIED");
                Log.d(TAG, "子视图没有大小限制");

                break;
        }

        if (isCircle) {
            setMeasuredDimension(Math.min(finalHeight, finalWidth), Math.min(finalHeight, finalWidth));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();
        rectF.left = getPaddingLeft();
        rectF.top = getPaddingTop();
        rectF.right = width - getPaddingRight();
        rectF.bottom = height - getPaddingBottom();
        if (isCircle) {
            Arrays.fill(rads, width >> 1);
        } else {
            Arrays.fill(rads, radius);
            rads[0] = topLeftRadius;
            rads[1] = topLeftRadius;
            rads[2] = topRightRadius;
            rads[3] = topRightRadius;
            rads[4] = bottomRightRadius;
            rads[5] = bottomRightRadius;
            rads[6] = bottomLeftRadius;
            rads[7] = bottomLeftRadius;
        }
        path.addRoundRect(rectF, rads, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);

        if (isCircle && borderEnable) {
            paint.setAntiAlias(true);
            paint.setColor(borderColor);
            //第二个参数是起点缩进
            //PathEffect effects = new DashPathEffect(new float[]{50, 50}, 40);
            //paint.setPathEffect(effects);
            paint.setStrokeWidth(borderWidth);
            paint.setStyle(Paint.Style.STROKE);
            paint.setDither(true);
            canvas.drawArc(rectF, startAngle, 180f, false, paint);
            Log.d(TAG, "起始角:" + startAngle);
            post(this);
        }
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startAngle += 10;
            invalidate();
        }
    };

    @Override
    public void run() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 360);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(10000);
        anim.addUpdateListener(animation -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        anim.start();
    }
}