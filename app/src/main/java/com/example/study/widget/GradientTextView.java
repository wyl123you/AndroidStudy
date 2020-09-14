package com.example.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

public class GradientTextView extends androidx.appcompat.widget.AppCompatTextView {

    private static final String TAG = GradientTextView.class.getSimpleName();

    private int startColor = Color.parseColor("#fff53b");
    private int endColor = Color.parseColor("#d67300");

    private int defStartColor = Color.parseColor("#fff53b");
    private int defEndColor = Color.parseColor("#d67300");

    private Shader shader;

    protected boolean shadowEnable;

    public GradientTextView(@NonNull Context context) {
        super(context);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradientTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NotNull Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView);
        setStartColor(ta.getColor(R.styleable.GradientTextView_startColor, startColor));
        setEndColor(ta.getColor(R.styleable.GradientTextView_endColor, endColor));
        setShadowEnable(ta.getBoolean(R.styleable.GradientTextView_shadowEnable, false));
        Log.d(TAG, "isShadowEnable: " + isShadowEnable());
        ta.recycle();
    }

    public void setStartColor(@ColorInt int startColor) {
        this.startColor = startColor;
    }

    @ColorInt
    public int getStartColor() {
        return startColor;
    }

    public void setEndColor(@ColorInt int endColor) {
        this.endColor = endColor;
    }

    @ColorInt
    public int getEndColor() {
        return endColor;
    }

    protected Shader getShader() {
        if (isShadowEnable())
            shader = new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
        else
            shader = new LinearGradient(0, 0, 0, getHeight(), defStartColor, defEndColor, Shader.TileMode.CLAMP);
        return shader;
    }

    public void setShadowEnable(boolean shadowEnable) {
        this.shadowEnable = shadowEnable;
    }

    public boolean isShadowEnable() {
        return shadowEnable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (shadowEnable) {
            getPaint().setShader(getShader());
        }
        super.onDraw(canvas);
    }
}
