package com.example.study.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class StrokeGradientText extends androidx.appcompat.widget.AppCompatTextView {

    private final String TAG = this.getClass().getSimpleName();
    private final Context context;

    private int startColor = Color.parseColor("#ffffff");
    private int endColor = Color.parseColor("#000000");
    private int strokeColor = Color.parseColor("#ff0000");
    private float strokeWidth = 2;
    private boolean shadowEnable = true;
    private boolean strokeEnable = true;

    private ShadowType shadowType = ShadowType.LINEAR;

    private Shader shader;

    public enum ShadowType {
        LINEAR,
        SWEEP,
        RADIAL
    }

    public StrokeGradientText(@NonNull Context context) {
        this(context, null);
    }

    public StrokeGradientText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeGradientText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttrs(context, attrs);
    }

    private void initAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) return;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.StrokeGradientText);
        startColor = ta.getColor(R.styleable.StrokeGradientText_startColor, Color.WHITE);
        endColor = ta.getColor(R.styleable.StrokeGradientText_endColor, Color.BLACK);
        strokeEnable = ta.getBoolean(R.styleable.StrokeGradientText_strokeEnable, true);
        strokeWidth = ta.getDimension(R.styleable.StrokeGradientText_strokeWidth, 1f);
        strokeColor = ta.getColor(R.styleable.StrokeGradientText_strokeColor, Color.RED);
        Log.d(TAG, "strokeWidth: " + strokeWidth);
        shadowType = getShadowTypeByIndex(ta.getInteger(R.styleable.StrokeGradientText_shadowType, 0));
        shadowEnable = ta.getBoolean(R.styleable.StrokeGradientText_shadowEnable, true);
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

    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
    }

    @ColorInt
    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeWidth(@Dimension float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Dimension
    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setShadowEnable(boolean shadowEnable) {
        this.shadowEnable = shadowEnable;
    }

    public boolean isShadowEnable() {
        return shadowEnable;
    }

    public void setStrokeEnable(boolean strokeEnable) {
        this.strokeEnable = strokeEnable;
    }

    public boolean isStrokeEnable() {
        return strokeEnable;
    }

    public void setShadowType(ShadowType shadowType) {
        this.shadowType = shadowType;
    }

    public ShadowType getShadowType() {
        return shadowType;
    }

    private ShadowType getShadowTypeByIndex(int index) {
        switch (index) {
            case 1:
                return ShadowType.SWEEP;
            case 2:
                return ShadowType.RADIAL;
            default:
                return ShadowType.LINEAR;
        }
    }

    private Shader getShader() {
        if (shadowEnable) {
            switch (shadowType) {
                case SWEEP:
                    return shader = new SweepGradient(
                            getWidth() >> 1,
                            getHeight() >> 1,
                            getSweepColorArray(),
                            getSweepPositionArray());
                case RADIAL:
                    return shader = new RadialGradient(
                            getWidth() >> 1,
                            getHeight() >> 1,
                            getHeight() >> 1,
                            startColor,
                            endColor,
                            Shader.TileMode.CLAMP);
                default:
                    return shader = new LinearGradient(0, 0, 0, getHeight(), startColor, endColor, Shader.TileMode.CLAMP);
            }
        }
        return shader;
    }

    @NotNull
    private int[] getSweepColorArray() {
        int[] sweepColorArray = new int[360];
        for (int i = 0; i < sweepColorArray.length; i++) {
            sweepColorArray[i] = getMiddleColor(getSweepPositionArray()[i]);
        }
        return sweepColorArray;
    }

    @NotNull
    @Contract(value = " -> new", pure = true)
    private float[] getSweepPositionArray() {
        float[] sweepPositionArray = new float[360];
        for (int i = 0; i < sweepPositionArray.length; i++) {
            sweepPositionArray[i] = (float) (i / 360.0);
        }
        return sweepPositionArray;
    }

    /**
     * 获取两个色值的中间色
     *
     * @param fraction 百分比
     * @return 颜色
     */
    private int getMiddleColor(float fraction) {
        int redCurrent;
        int blueCurrent;
        int greenCurrent;
        int alphaCurrent;

        int redStart = Color.red(startColor);
        int blueStart = Color.blue(startColor);
        int greenStart = Color.green(startColor);
        int alphaStart = Color.alpha(startColor);

        int redEnd = Color.red(endColor);
        int blueEnd = Color.blue(endColor);
        int greenEnd = Color.green(endColor);
        int alphaEnd = Color.alpha(endColor);

        int redDifference = redEnd - redStart;
        int blueDifference = blueEnd - blueStart;
        int greenDifference = greenEnd - greenStart;
        int alphaDifference = alphaEnd - alphaStart;

        if (fraction > 0.50) {
            fraction = 1.00f - fraction;
        }

        redCurrent = (int) (redStart + fraction * redDifference);
        blueCurrent = (int) (blueStart + fraction * blueDifference);
        greenCurrent = (int) (greenStart + fraction * greenDifference);
        alphaCurrent = (int) (alphaStart + fraction * alphaDifference);
        return Color.argb(alphaCurrent, redCurrent, greenCurrent, blueCurrent);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ColorStateList list = getTextColors();
        TextPaint paint = getPaint();
        if (strokeEnable) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(strokeWidth);
            paint.setAntiAlias(true);
            paint.setShader(null);
            setTextColor(strokeColor);
            super.onDraw(canvas);
        }
        if (shadowEnable) {
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(getShader());
            super.onDraw(canvas);
        } else {
            paint.setStyle(Paint.Style.FILL);
            setTextColor(list);
            super.onDraw(canvas);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    private int px2dip(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}