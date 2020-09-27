package com.example.study.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

public class StrokeGradientTextView extends GradientTextView {

    private static final String TAG = StrokeGradientTextView.class.getSimpleName();

    private int strokeColor = Color.parseColor("#ff0000");
    private float strokeWidth = 0;

    private boolean strokeEnable;

    public StrokeGradientTextView(@NonNull Context context) {
        super(context);
    }

    public StrokeGradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StrokeGradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeGradientTextView);
        setStrokeColor(ta.getColor(R.styleable.StrokeGradientTextView_strokeColor, strokeColor));
        setStrokeWidth(ta.getDimension(R.styleable.StrokeGradientTextView_strokeWidth, strokeWidth));
        setStrokeEnable(ta.getBoolean(R.styleable.StrokeGradientTextView_strokeEnable, false));
        ta.recycle();
    }

    public void setStrokeWidth(@Dimension float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Dimension
    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
    }

    @ColorInt
    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeEnable(boolean strokeEnable) {
        this.strokeEnable = strokeEnable;
    }

    public boolean isStrokeEnable() {
        return strokeEnable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: " + count++);
        //文字
        ColorStateList list = getTextColors();
        TextPaint paint = getPaint();

        paint.setStyle(Paint.Style.FILL);
        setTextColor(list);
        paint.setShader(getShader());
        super.onDraw(canvas);

        if (isStrokeEnable()) {//描边
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setShader(null);
            setShadowEnable(false);
            setTextColor(strokeColor);
        }
        super.onDraw(canvas);

    }
}
