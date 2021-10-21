package com.example.study.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

import androidx.core.view.ViewCompat;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {

    private final String TAG = this.getClass().getSimpleName();

    private final int mHorizontalSpace = 40;
    private final int mVerticalSpace = 40;

    private ArrayList<ArrayList<View>> allLines = new ArrayList<>();
    private ArrayList<Integer> lineHeights = new ArrayList<>();

    //速度追踪器
    private VelocityTracker mVelocityTracker;


    private int mTouchSlop;
    private int mMinFlingVelocity;//最小滑动速度
    private int mMaxFlingVelocity;//最大滑动速度

    private int SCREEN_WIDTH = 0;
    private int SCREEN_HEIGHT = 0;

    private int mLastY;

    private final ViewFlinger mViewFlinger = new ViewFlinger();

    //差值器 f(x) = (x-1)^5 + 1
    private final Interpolator mQuinticInterpolator = t -> {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    };

    //当前操作手指的ID值（滑动时的多指操作）
    private int mScrollPointerId;

    public FlowLayout(Context context) {
        super(context);
        mVelocityTracker = VelocityTracker.obtain();
        Log.d(TAG, "FlowLayout1");
    }

    //反射
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mVelocityTracker = VelocityTracker.obtain();

        final ViewConfiguration vc = ViewConfiguration.get(context);
        mTouchSlop = vc.getScaledTouchSlop();
        mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();

        DisplayMetrics metric = context.getResources().getDisplayMetrics();
        SCREEN_WIDTH = metric.widthPixels;
        SCREEN_HEIGHT = metric.heightPixels;
        Log.d(TAG, "FlowLayout2");
    }

    //主题
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                if (!mScroller.isFinished()) {
//                    mScroller.abortAnimation();
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                return true;
//            case MotionEvent.ACTION_UP:
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }


    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_SETTLING = 2;
    private int mScrollState = SCROLL_STATE_IDLE;

    private void setScrollState(int state) {
        if (state == mScrollState) {
            return;
        }
        mScrollState = state;
        if (state != SCROLL_STATE_SETTLING) {
            mViewFlinger.stop();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        boolean eventAddedToVelocityTracker = false;
        final int action = event.getActionMasked();//多指操作的action
        final int actionIndex = event.getActionIndex();//当前操作手指的index

        final MotionEvent motionEvent = MotionEvent.obtain(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                setScrollState(SCROLL_STATE_IDLE);
                mScrollPointerId = event.getPointerId(0);
                mLastY = (int) (event.getY() + 0.5f);
                Log.d(TAG, "第一个手指按下: " + actionIndex);
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mScrollPointerId = event.getPointerId(actionIndex);//当前操作手指的id
                mLastY = (int) (event.getY(actionIndex) + 0.5f);
                Log.d(TAG, "多指按下: " + actionIndex);
                break;

            case MotionEvent.ACTION_MOVE:
                final int index = event.findPointerIndex(mScrollPointerId);//根据当前操作手指的id获取index
                if (index < 0) {
                    Log.d(TAG, "Error processing scroll; pointer index for id " + mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }

                final int y = (int) (event.getY(index) + 0.5f);
                int dy = mLastY - y;

                if (mScrollState != SCROLL_STATE_DRAGGING) {
                    boolean startScroll = false;

                    if (Math.abs(dy) > mTouchSlop) {
                        if (dy > 0) {
                            dy -= mTouchSlop;
                        } else {
                            dy += mTouchSlop;
                        }
                        startScroll = true;
                    }
                    if (startScroll) {
                        setScrollState(SCROLL_STATE_DRAGGING);
                    }
                }

                if (mScrollState == SCROLL_STATE_DRAGGING) {
                    mLastY = y;
                    constrainScrollBy(0, dy);
                }

                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.d(TAG, "多指松开: " + actionIndex);
                if (event.getPointerId(actionIndex) == mScrollPointerId) {
                    //如果送来的手指是可滚动操作的手指
                    final int newIndex = actionIndex == 0 ? 1 : 0;
                    mScrollPointerId = event.getPointerId(newIndex);
                    mLastY = (int) (event.getY(newIndex) + 0.5f);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "最后一指松开: " + actionIndex);
                mVelocityTracker.addMovement(motionEvent);
                eventAddedToVelocityTracker = true;
                mVelocityTracker.computeCurrentVelocity(1000, mMaxFlingVelocity);
                //float yVelocity = -VelocityTrackerCompat.getYVelocity(, mScrollPointerId);
                float yVelocity = -mVelocityTracker.getYVelocity(mScrollPointerId);
                Log.i("zhufeng", "速度取值：" + yVelocity);
                if (Math.abs(yVelocity) < mMinFlingVelocity) {
                    yVelocity = 0F;
                } else {
                    yVelocity = Math.max(-mMaxFlingVelocity, Math.min(yVelocity, mMaxFlingVelocity));
                }
                if (yVelocity != 0) {
                    mViewFlinger.fling((int) yVelocity);
                } else {
                    setScrollState(SCROLL_STATE_IDLE);
                }
                resetTouch();
                break;
            case MotionEvent.ACTION_CANCEL:
                resetTouch();
                break;
        }
        if (!eventAddedToVelocityTracker) {
            mVelocityTracker.addMovement(motionEvent);
        }
        motionEvent.recycle();
        return true;
    }

    private void resetTouch() {
        if (mVelocityTracker != null) {
            mVelocityTracker.clear();
        }
    }

    public final class ViewFlinger implements Runnable {

        private int mLastFlingY = 0;
        private final OverScroller mOverScroller;
        private boolean mEatRunOnAnimationRequest;
        private boolean mReSchedulePostAnimationCallback;

        public ViewFlinger() {
            Context context = getContext();
            mOverScroller = new OverScroller(context, mQuinticInterpolator);
        }

        @Override
        public void run() {
            disableRunOnAnimationRequest();
            if (mOverScroller.computeScrollOffset()) {
                final int y = mOverScroller.getCurrY();
                int dy = y - mLastFlingY;
                mLastFlingY = y;
                constrainScrollBy(0, dy);
                postOnAnimation();
            }
            enableRunOnAnimationRequest();
        }

        private void fling(int velocityY) {
            mLastFlingY = 0;
            setScrollState(SCROLL_STATE_SETTLING);
            mOverScroller.fling(0, 0, 0, 0, 0, 0, 0, 0);
            postOnAnimation();
        }

        private void stop() {
            removeCallbacks(this);
            mOverScroller.abortAnimation();
        }

        private void disableRunOnAnimationRequest() {
            mReSchedulePostAnimationCallback = false;
            mEatRunOnAnimationRequest = true;
        }

        private void enableRunOnAnimationRequest() {
            mEatRunOnAnimationRequest = false;
            if (mReSchedulePostAnimationCallback) {
                postOnAnimation();
            }
        }


        private void postOnAnimation() {
            if (mEatRunOnAnimationRequest) {
                mReSchedulePostAnimationCallback = true;
            } else {
                removeCallbacks(this);
                ViewCompat.postOnAnimation(FlowLayout.this, this);
            }
        }
    }

    private void constrainScrollBy(int dx, int dy) {
        Rect rect = new Rect();
        getGlobalVisibleRect(rect);
        int height = rect.height();
        int width = rect.width();

        int scrollX = getScrollX();
        int scrollY = getScrollY();

        //右边界
        if (getWidth() - scrollX - dx < width) {
            dx = getWidth() - scrollX - width;
        }
        //左边界
        if (-scrollX - dx > 0) {
            dx = -scrollX;
        }
        //下边界
        if (getHeight() - scrollY - dy < height) {
            dy = getHeight() - scrollY - height;
        }
        //上边界
        if (scrollY + dy < 0) {
            dy = -scrollY;
        }
        scrollBy(dx, dy);
    }
}