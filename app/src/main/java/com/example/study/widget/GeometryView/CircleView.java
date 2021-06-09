package com.example.study.widget.GeometryView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2020/11/12 上午11:43
 * @Company LotoGram
 */

@SuppressLint("CustomViewStyleable")
public class CircleView extends GeometryView {

    private int radius = 90;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GeometryView);

        lineColor = ta.getColor(R.styleable.GeometryView_lineColor, Color.BLACK);
        lineWidth = (int) ta.getDimension(R.styleable.GeometryView_lineWidth, 30);
        radius = (int) ta.getDimension(R.styleable.GeometryView_radius1, 90);

        Log.d(TAG, "lineWidth: " + lineWidth);
        Log.d(TAG, "radius: " + radius);
        Log.d(TAG, "radius: " + radius);
        ta.recycle();
    }

    @ColorInt
    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
    }

    @Dimension
    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(@Dimension int lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Dimension
    public int getRadius() {
        return radius;
    }

    public void setRadius(@Dimension int radius) {
        this.radius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingLeft = getPaddingLeft();

        int needWidth = widthMode == MeasureSpec.EXACTLY ? width : radius * 2 + lineWidth + paddingLeft + paddingRight;
        int needHeight = heightMode == MeasureSpec.EXACTLY ? height : radius * 2 + lineWidth + paddingTop + paddingBottom;
        setMeasuredDimension(needWidth, needHeight);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        Log.d(TAG, "onDraw");
        int width = getWidth();
        int height = getHeight();
        //Log.d(TAG, "width: " + width + "    height: " + height);
        int centerX = width >> 1;
        int centerY = height >> 1;

        //Log.d(TAG, "lineWidth: " + lineWidth);
        //Log.d(TAG, "radius: " + radius);

        paint.setAntiAlias(true);
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }
}