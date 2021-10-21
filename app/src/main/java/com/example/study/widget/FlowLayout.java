package com.example.study.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;
import android.widget.Scroller;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {

    private final String TAG = this.getClass().getSimpleName();

    private final Context context;

    private final int mHorizontalSpace = 40;
    private final int mVerticalSpace = 40;

    private ArrayList<ArrayList<View>> allLines = new ArrayList<>();
    private ArrayList<Integer> lineHeights = new ArrayList<>();

    // 屏幕的高度
    private int mScreenHeight;

    // 手指按下时的getScrollY
    private int mScrollStart;

    // 手指抬起时的getScrollY
    private int mScrollEnd;

    //记录移动时的Y
    private int mLastY;

    //滚动的辅助类
    OverScroller overScroller;
    private Scroller mScroller;

    //是否正在滚动
    private boolean isScrolling;

    //加速度检测
    private VelocityTracker mVelocityTracker;

    public FlowLayout(Context context) {
        super(context);
        this.context = context;
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        Log.d(TAG, "FlowLayout1");
    }

    //反射
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        Log.d(TAG, "FlowLayout2");
    }

    //主题
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mScroller = new Scroller(context);
        mVelocityTracker = VelocityTracker.obtain();
        Log.d(TAG, "FlowLayout3");
    }

    private void init() {
        allLines = new ArrayList<>();
        lineHeights = new ArrayList<>();
    }

    //度量子View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        init();
        int childCount = getChildCount();
        Log.d(TAG, "childCount:" + childCount);
        //获取自身的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //从布局文件获取FlowLayout的宽高
        int selfWidth = MeasureSpec.getSize(widthMeasureSpec);
        int selfHeight = MeasureSpec.getSize(heightMeasureSpec);

        ArrayList<View> lineViews = new ArrayList<>();//保存一行中的所有的View;

        int lineWidthUsed = 0;
        int lineHeight = 0;

        int parentNeededWidth = 0;
        int parentNeededHeight = 0;

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            LayoutParams childLP = childView.getLayoutParams();
            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, paddingLeft + paddingRight, childLP.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, paddingTop + paddingBottom, childLP.height);
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            //子View测量完之后获取宽高
            int childMeasuredWidth = childView.getMeasuredWidth();
            int childMeasuredHeight = childView.getMeasuredHeight();
            Log.d(TAG, "第" + i + "个子View的宽高");
            Log.d(TAG, "w:" + childMeasuredWidth + "  h:" + childMeasuredHeight);

            if (childMeasuredWidth + lineWidthUsed > selfWidth - paddingLeft - paddingRight) {
                allLines.add(lineViews);
                lineHeights.add(lineHeight);
                //一旦换行，我们就可以判断当前行需要的宽高
                parentNeededHeight = parentNeededHeight + lineHeight + mVerticalSpace;
                parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpace);
                lineViews = new ArrayList<>();
                lineWidthUsed = 0;
                lineHeight = 0;
            }

            lineViews.add(childView);
            lineWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpace;
            lineHeight = Math.max(lineHeight, childMeasuredHeight);
        }

        allLines.add(lineViews);
        lineHeights.add(lineHeight);
        //一旦换行，我们就可以判断当前行需要的宽高
        parentNeededHeight = parentNeededHeight + lineHeight;
        parentNeededWidth = Math.max(parentNeededWidth, lineWidthUsed + mHorizontalSpace);

        //度量自己的大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int realWidth = (widthMode == MeasureSpec.EXACTLY) ? selfWidth : parentNeededWidth + paddingLeft + paddingRight;
        int realHeight = (heightMode == MeasureSpec.EXACTLY) ? selfHeight : parentNeededHeight + paddingTop + paddingBottom;
        setMeasuredDimension(realWidth, realHeight);
        scrollTo(0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lineCount = allLines.size();
        Log.d(TAG, "lineCount: " + lineCount);

        int curL = getPaddingLeft();
        int curT = getPaddingTop();

        for (int i = 0; i < lineCount; i++) {
            ArrayList<View> views = allLines.get(i);
            int lineHeight = lineHeights.get(i);
            for (int j = 0; j < views.size(); j++) {
                View view = views.get(j);
                int left = curL;
                int top = curT;

                int right = left + view.getMeasuredWidth();
                int bottom = top + view.getMeasuredHeight();
                view.layout(left, top + (lineHeight - view.getMeasuredHeight()) / 2, right, bottom + (lineHeight - view.getMeasuredHeight()) / 2);
                Log.d(TAG, "第" + (i + 1) + "行，第" + (j + 1) + "个子View的位置:left" + left + "  top:" + top);
                curL = right + mHorizontalSpace;
            }
            curT = curT + lineHeight + mVerticalSpace;
            curL = getPaddingLeft();
        }
    }

//    @Override
//    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                return false;
//            case MotionEvent.ACTION_MOVE:
//                return true;
//            case MotionEvent.ACTION_UP:
//            default:
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果当前正在滚动，调用父类的onTouchEvent
        if (isScrolling) return super.onTouchEvent(event);

        mVelocityTracker.addMovement(event);

        int action = event.getAction();
        int currentY = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mScroller.isFinished()) mScroller.abortAnimation();

                int deltaY = currentY - mLastY;//正数上滑，负数下滑

                if (deltaY < 0) {//上滑
                    Log.d(TAG, "下滑");
                    scrollBy(0, -deltaY);
                } else {//下滑
                    Log.d(TAG, "上滑");
                    if (getScrollY() < 0) {//上滑至顶部

                        int overScroll = -getScrollY();


                        int ratio = 1;

                        scrollBy(0, -deltaY * ratio);
                    } else {
                        scrollBy(0, -deltaY);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000);
                Log.d(TAG, "X: " + mVelocityTracker.getXVelocity());

                int autoScrollDeltaY = -(int) mVelocityTracker.getYVelocity();


                mScroller.startScroll(0, getScrollY(), 0, autoScrollDeltaY, 2000);
                postInvalidate();
                break;
        }
        mLastY = currentY;
        return true;
    }

    @Override
    public void computeScroll() {
        Log.d(TAG, "computeScroll: ");
        if (mScroller.computeScrollOffset()) {//返回false表示滚动结束
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }
}