package com.example.study.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {

    private final String TAG = this.getClass().getSimpleName();

    private Context context;

    private int mHorizontalSpace = 10;
    private int mVerticalSpace = 10;

    private ArrayList<ArrayList<View>> allLines = new ArrayList<>();
    private ArrayList<Integer> lineHeights = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
        this.context = context;
    }

    //反射
    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    //主题
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
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
            childView.setTag(i + 1);
            childView.setOnClickListener(view -> Toast.makeText(context, view.getTag() + "", Toast.LENGTH_LONG).show());
            childView.setBackgroundColor(Color.GRAY);
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}