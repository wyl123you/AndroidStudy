package com.example.study.widget.GeometryView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2020/11/12 下午2:54
 * @Company LotoGram
 */

@SuppressLint("CustomViewStyleable")
public class RectangleView extends GeometryView {

    private int recWidth = 180;
    private int recHeight = 180;

    public RectangleView(Context context) {
        this(context, null);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RectangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GeometryView);
        lineColor = ta.getColor(R.styleable.GeometryView_lineColor, Color.BLACK);
        lineWidth = (int) ta.getDimension(R.styleable.GeometryView_lineWidth, 30);
        recWidth = (int) ta.getDimension(R.styleable.GeometryView_recWidth, 180);
        recHeight = (int) ta.getDimension(R.styleable.GeometryView_recHeight, 180);
        ta.recycle();
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

        int needWidth = widthMode == MeasureSpec.EXACTLY ? width : recWidth + lineWidth + paddingLeft + paddingRight;
        int needHeight = heightMode == MeasureSpec.EXACTLY ? height : recHeight + lineWidth + paddingTop + paddingBottom;
        setMeasuredDimension(needWidth, needHeight);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //Log.d(TAG, "width: " + width + "    height: " + height);
        int centerX = width >> 1;
        int centerY = height >> 1;

        paint.setAntiAlias(true);
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        canvas.drawRect(
                centerX - (recWidth >> 1),
                centerY - (recHeight >> 1),
                centerX + (recWidth >> 1),
                centerY + (recHeight >> 1),
                paint);
    }

    @ColorInt
    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(@ColorInt int lineColor) {
        this.lineColor = lineColor;
    }

    @Dimension
    public int getRecWidth() {
        return recWidth;
    }

    public void setRecWidth(@Dimension int recWidth) {
        this.recWidth = recWidth;
    }

    @Dimension
    public int getRecHeight() {
        return recHeight;
    }

    public void setRecHeight(@Dimension int recHeight) {
        this.recHeight = recHeight;
    }

    @Dimension
    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(@Dimension int lineWidth) {
        this.lineWidth = lineWidth;
    }
}