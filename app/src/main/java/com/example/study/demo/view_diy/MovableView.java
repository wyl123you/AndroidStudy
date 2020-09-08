package com.example.study.demo.view_diy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

@SuppressLint("ClickableViewAccessibility")
public class MovableView extends View {

    private static final String TAG = "MovableView";

    private int startX;
    private int startY;

    private Scroller mScroller;


    public MovableView(Context context) {
        this(context, null);
    }

    public MovableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MovableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "onTouchEvent: ");
//        int x = (int) event.getX();
//        int y = (int) event.getY();

        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;


                Log.d(TAG, "startX:" + startX + "   startY:" + startY);
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - startX;
                int offsetY = y - startY;
                Log.d(TAG, "offsetX:" + offsetX + "   offsetY:" + offsetY);


//                        layout(
//         1              getLeft() + offsetX,
//                        getTop() + offsetY,
//                        getRight() + offsetX,
//                        getBottom() + offsetY);


//         2      offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);


//                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
//         3      lp.leftMargin = getLeft() + offsetX;
//                lp.topMargin = getTop() + offsetY;


//                scollTo(x,y)表示移动到一个具体的坐标点，
//                而scollBy(dx,dy)则表示移动的增量为dx、dy。
//         4      其中scollBy最终也是要调用scollTo的。
//                scollTo、scollBy移动的是View的内容，
//                如果在ViewGroup中使用则是移动他所有的子View
                // ((View) getParent()).scrollBy(-offsetX, -offsetY);
//
//
//         5       使用Scroller
                //smoothScrollTo(-x, -y);


                break;
            case MotionEvent.ACTION_UP:
                smoothScrollTo(-(x - startX), -(y - startY));
                invalidate();
                break;

        }
        return true;
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll: ");
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            ((View) getParent()).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    public void smoothScrollTo(int dx, int dy) {
        mScroller.startScroll(-startX, -startY, dx, dy, 2000);
        invalidate();
    }
}
