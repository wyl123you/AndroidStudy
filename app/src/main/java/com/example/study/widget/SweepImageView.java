package com.example.study.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/9 上午11:49
 * @Company LotoGram
 */

public class SweepImageView extends androidx.appcompat.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener {

    private final static String TAG = "SweepImageView";

    public static final int CW = 0x0000;
    public static final int CCW = 0x0001;

    public static final int SHOW = 0x0000;
    public static final int HIDE = 0x0001;

    public static final int RADIAL = 0x0000;
    public static final int CIRCLE = 0x0001;
    public static final int SQUARE = 0x0002;


    public static final int ANGLE000 = 0x0000;
    public static final int ANGLE090 = 0x0020;
    public static final int ANGLE180 = 0x0030;
    public static final int ANGLE270 = 0x0040;

    @IntDef({CW, CCW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Direction {
    }

    @IntDef({SHOW, HIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    @IntDef({CIRCLE, SQUARE, RADIAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sweep {
    }

    @IntDef({ANGLE000, ANGLE090, ANGLE180, ANGLE270})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Angle {
    }

    public interface OnCountDownFinishListener {
        void onCountDownFinish();
    }

    private final Paint paint;
    private final RectF rectF;

    private int originStartAngle;
    private int originAreaAngle;

    private long period;
    private int type;
    private int startAngle;
    private int areaAngle;
    private int sweepType;
    private int direction;
    private boolean repeat;
    private ValueAnimator animator;

    public SweepImageView(@NotNull Context context) {
        this(context, null);
    }

    public SweepImageView(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepImageView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.argb(128, 0, 0, 0));
        rectF = new RectF(0, 0, 0, 0);
        initAttrs(context, attrs);
    }

    private void initAttrs(@NotNull Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SweepImageView);
        setPeriod(ta.getInteger(R.styleable.SweepImageView_periodInMillis, 5000));
        setStartAngle(ta.getInteger(R.styleable.SweepImageView_startAngle, ANGLE000));
        setSweepType(ta.getInteger(R.styleable.SweepImageView_sweep, CIRCLE));
        setDirection(ta.getInteger(R.styleable.SweepImageView_direction, CW));
        setType(ta.getInteger(R.styleable.SweepImageView_type, SHOW));
        setAreaAngle(ta.getInteger(R.styleable.SweepImageView_areaAngle, 180));
        setRepeat(ta.getBoolean(R.styleable.SweepImageView_repeat, true));
        ta.recycle();
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public long getPeriod() {
        return period;
    }

    public void setStartAngle(@Angle int startAngle) {
        this.startAngle = startAngle;
        this.originStartAngle = startAngle;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setSweepType(@Sweep int sweepType) {
        this.sweepType = sweepType;
    }

    @Sweep
    public int getSweepType() {
        return sweepType;
    }

    public void setDirection(@Direction int direction) {
        this.direction = direction;
    }

    @Direction
    public int getDirection() {
        return direction;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    @Type
    public int getType() {
        return type;
    }

    public void setAreaAngle(int areaAngle) {
        if (sweepType == RADIAL) {
            this.areaAngle = areaAngle;
        } else {
            this.areaAngle = type == SHOW ? 360 : 0;
        }
    }

    public int getAreaAngle() {
        return areaAngle;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }


    public void startCountDown() {
        if (animator != null && animator.isRunning()) {
            return;
        } else if (animator != null && animator.isPaused()) {
            animator.start();
            return;
        }

        if (sweepType == RADIAL) {
            int startValue = startAngle;
            int endValue = direction == CW ? startAngle + 360 : startAngle - 360;
            animator = ValueAnimator.ofInt(startValue, endValue);
            animator.setDuration(period);
        } else {
            if (type == SHOW) {
                if (direction == CW) {
                    animator = ValueAnimator.ofInt(-360, repeat ? 360 : 0);
                } else {
                    animator = ValueAnimator.ofInt(360, repeat ? -360 : 0);
                }
            } else {
                if (direction == CW) {
                    animator = ValueAnimator.ofInt(0, repeat ? 720 : 360);
                } else {
                    animator = ValueAnimator.ofInt(0, repeat ? -720 : -360);
                }
            }
            animator.setDuration(repeat ? 2 * period : period);
        }
        animator.addUpdateListener(this);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (repeat) {
                    startCountDown();
                } else {

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void stopCountDown() {
        if (animator != null && animator.isRunning()) {
            animator.pause();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (sweepType == RADIAL) {
            startAngle = (int) animation.getAnimatedValue();
        } else {
            if (type == SHOW) {
                areaAngle = (int) animation.getAnimatedValue();
            } else {
                int value = (int) animation.getAnimatedValue();
                if (value >= -360 && value <= 360) {
                    areaAngle = value;
                } else {
                    startAngle = originStartAngle;
                    areaAngle = -1 * (value / Math.abs(value)) * (720 - Math.abs(value));
                }
            }
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        rectF.left = sweepType == SQUARE ? -width : 0;
        rectF.top = sweepType == SQUARE ? -height : 0;
        rectF.right = sweepType == SQUARE ? 2 * width : width;
        rectF.bottom = sweepType == SQUARE ? 2 * height : height;
        //Log.d(TAG, "startAngle: " + startAngle);
        //Log.d(TAG, "areaAngle: " + areaAngle);
        canvas.drawArc(rectF, startAngle, areaAngle, true, paint);
    }
}