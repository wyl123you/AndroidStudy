package com.example.study.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.study.R;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/9 上午11:49
 * @Company LotoGram
 */

public class SweepImageView extends androidx.appcompat.widget.AppCompatImageView {

    private final static String TAG = "SweepImageView";
    private final static int DEFAULT_PERIOD = 5000;
    private final static int DEFAULT_START_ANGLE = 0;
    private final static int DEFAULT_AREA_ANGLE = 360;
    private final static int DEFAULT_SWEEP_TYPE = 0;
    private final static int DEFAULT_DIRECTION = 0;
    private final Paint paint;
    private final RectF rectF;

    private int period;
    private int startAngle;
    private float areaAngle;
    private boolean repeat;
    private SweepType sweepType;
    private Direction direction;

    public enum Direction {
        CW(0),
        CWW(1);

        Direction(int direction) {
            //init direction
            this.direction = direction;
        }

        final int direction;

        @Nullable
        public static Direction get(int direction) {
            for (Direction d : Direction.values()) {
                if (direction == d.direction) {
                    return d;
                }
            }
            return null;
        }
    }

    public enum SweepType {
        CIRCLE(0),
        SQUARE(1),
        RADAR(2);

        SweepType(int type) {
            //init sweepType
            this.type = type;
        }

        final int type;

        @Nullable
        public static SweepType get(int type) {
            for (SweepType s : SweepType.values()) {
                if (s.type == type) {
                    return s;
                }
            }
            return null;
        }
    }

    public SweepImageView(@NotNull Context context) {
        this(context, null);
    }

    public SweepImageView(@NotNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SweepImageView(@NotNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.argb(80, 0, 0, 0));
        rectF = new RectF(0, 0, 0, 0);
        initAttrs(context, attrs);
    }

    private void initAttrs(@NotNull Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SweepImageView);
        setPeriod(ta.getInteger(R.styleable.SweepImageView_periodInMillis, DEFAULT_PERIOD));
        setStartAngle(ta.getInteger(R.styleable.SweepImageView_startAngle, DEFAULT_START_ANGLE));
        setAreaAngle(ta.getInteger(R.styleable.SweepImageView_areaAngle, DEFAULT_AREA_ANGLE));
        setRepeat(ta.getBoolean(R.styleable.SweepImageView_repeat, true));
        setDirection(ta.getInteger(R.styleable.SweepImageView_direction, DEFAULT_DIRECTION));
        setSweepType(ta.getInteger(R.styleable.SweepImageView_sweepType, DEFAULT_SWEEP_TYPE));
        ta.recycle();
    }

    public void setStartAngle(int angle) {
        this.startAngle = angle;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setDirection(int direction) {
        this.direction = Direction.get(direction);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public float getPeriod() {
        return period;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setAreaAngle(float areaAngle) {
        this.areaAngle = areaAngle;
    }

    public float getAreaAngle() {
        return areaAngle;
    }

    public void setSweepType(SweepType sweepType) {
        this.sweepType = sweepType;
    }

    public void setSweepType(int type) {
        this.sweepType = SweepType.get(type);
    }

    public SweepType getSweepType() {
        return sweepType;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
//        private int period;
//        private int startAngle;
//        private float areaAngle;
//        private boolean repeat;
//        private Direction direction;

        rectF.left = sweepType == SweepType.SQUARE ? -1000 : 0;
        rectF.top = sweepType == SweepType.SQUARE ? -1000 : 0;
        rectF.right = sweepType == SweepType.SQUARE ? width + 1000 : width;
        rectF.bottom = sweepType == SweepType.SQUARE ? height + 1000 : height;

        if (repeat) {
            if (direction == Direction.CW) {
                areaAngle--;
            } else {
                areaAngle++;
            }
        }

        canvas.drawArc(
                rectF,
                0,
                areaAngle--,
                true,
                paint);
        if (areaAngle == -360) areaAngle = 360;

        postInvalidateDelayed(period / 360);
    }

    private float fwr(int a) {
        return (float) (Math.sqrt(2) * a);
    }
}