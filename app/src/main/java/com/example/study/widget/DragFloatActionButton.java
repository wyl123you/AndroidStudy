package com.example.study.widget;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

@SuppressLint("ClickableViewAccessibility")
public class DragFloatActionButton extends FloatingActionButton {

    private final String TAG = this.getClass().getSimpleName();
    private int parentHeight;
    private int parentWidth;

    private int lastX;
    private int lastY;
    private boolean isDrag;

    public DragFloatActionButton(Context context) {
        super(context);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DragFloatActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
//                setPressed(true);
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                ViewGroup parent;
                if (getParent() != null) {
                    parent = (ViewGroup) getParent();
                    parentHeight = parent.getHeight();
                    parentWidth = parent.getWidth();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "MOVE");
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance >= 10) isDrag = true;
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > parentWidth - getWidth() ? parentWidth - getWidth() : x;
                y = getY() < 0 ? 0 : getY() + getHeight() > parentHeight ? parentHeight - getHeight() : y;
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "UP: ");
//                setPressed(false);
                if (rawX >= parentWidth / 2) {
                    //靠右吸附
                    animate().setInterpolator(new DecelerateInterpolator())
                            .setDuration(500)
                            .xBy(parentWidth - getWidth() - getX())
                            .start();
                } else {
                    //靠左吸附
                    ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                    oa.setInterpolator(new DecelerateInterpolator());
                    oa.setDuration(500);
                    oa.start();
                }
                break;
        }
        //如果是拖拽(超过10个像素)则消耗事件，否则正常传递即可。
//        return event.getAction() != MotionEvent.ACTION_UP && (!isDrag || super.onTouchEvent(event));
        return isDrag || super.onTouchEvent(event);
    }
}