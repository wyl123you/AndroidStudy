package com.example.study.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/4/26 下午5:35
 * @Company LotoGram
 */

public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {

    private float[] rads;

    private Path path;
    private RectF rectF;

    public RoundImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        path = new Path();
        rectF = new RectF(0, 0, 0, 0);
        rads = new float[]{8, 8, 8, 8, 0.0f, 0.0f, 0.0f, 0.0f};
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {


        int width = this.getWidth();
        int height = this.getHeight();

        rectF.right = width;
        rectF.bottom = height;

        path.addRoundRect(rectF, rads, Path.Direction.CW);

        canvas.clipPath(path);
        super.onDraw(canvas);
    }
}