package com.example.study.widget.GeometryView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2020/11/12 下午3:42
 * @Company LotoGram
 */

public class GeometryView extends View {

    protected final String TAG = this.getClass().getSimpleName();

    protected int lineWidth = 30;
    protected int lineColor = Color.BLACK;

    protected Paint paint;

    public GeometryView(Context context) {
        super(context);
    }

    public GeometryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GeometryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GeometryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        paint = new Paint();
    }
}
