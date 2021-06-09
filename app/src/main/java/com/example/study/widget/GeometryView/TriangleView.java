package com.example.study.widget.GeometryView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;

import androidx.annotation.Dimension;
import androidx.annotation.Nullable;

import com.example.study.R;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2020/11/12 下午5:40
 * @Company LotoGram
 */

@SuppressLint("CustomViewStyleable")
public class TriangleView extends GeometryView {

    private int triSize = 180;

    private Point pointA;
    private Point pointB;
    private Point pointC;
    private Path path;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
        pointA = new Point();
        pointB = new Point();
        pointC = new Point();
        path = new Path();
    }

    private void initAttrs(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GeometryView);
        triSize = (int) ta.getDimension(R.styleable.GeometryView_triSize, 180);
        lineColor = ta.getColor(R.styleable.GeometryView_lineColor, Color.BLACK);
        lineWidth = (int) ta.getDimension(R.styleable.GeometryView_lineWidth, 30);
        ta.recycle();
    }

    @Dimension
    public int getTriSize() {
        return triSize;
    }

    public void setTriSize(@Dimension int triSize) {
        this.triSize = triSize;
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

        int needWidth = widthMode == MeasureSpec.EXACTLY ? width : triSize * 3 + lineWidth + paddingLeft + paddingRight;
        int needHeight = heightMode == MeasureSpec.EXACTLY ? height : triSize * 3 + lineWidth + paddingTop + paddingBottom;
        setMeasuredDimension(needWidth, needHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        //Log.d(TAG, "width: " + width + "    height: " + height);
        int centerX = width >> 1;
        int centerY = height >> 1;
        paint.setAntiAlias(true);
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        pointA.x = centerX;
        pointA.y = (int) (centerY - (Math.sqrt(3.0) / 3.0 * triSize));

        pointB.x = centerX - (triSize >> 1);
        pointB.y = (int) (centerY + (Math.sqrt(3.0) / 6.0 * triSize));

        pointC.x = centerX + (triSize >> 1);
        pointC.y = (int) (centerY + (Math.sqrt(3.0) / 6.0 * triSize));

        path.moveTo(pointA.x, pointA.y);
        path.moveTo(pointB.x, pointB.y);
        path.moveTo(pointC.x, pointC.y);
        path.close();
        canvas.drawPath(path, paint);
    }
}