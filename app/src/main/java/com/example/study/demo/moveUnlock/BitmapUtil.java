package com.example.study.demo.moveUnlock;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/2/22 下午4:17
 * @Company LotoGram
 */

public class BitmapUtil {

    /**
     * 缩放图片
     *
     * @param originBitmap 需要缩放的Bitmap
     * @param targetWidth  目标宽度
     * @param targetHeight 目标高度
     * @return 缩放后的Bitmap
     */
    public static Bitmap scale(@NotNull Bitmap originBitmap, float targetWidth, float targetHeight) {
        int originWidth = originBitmap.getWidth();
        int originHeight = originBitmap.getHeight();
        float widthRatio = targetWidth / originWidth;
        float heightRatio = targetHeight / originHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(widthRatio, heightRatio);
        return Bitmap.createBitmap(originBitmap, 0, 0, originWidth, originHeight, matrix, true);
        //if (!originBitmap.isRecycled()) {
        //    originBitmap.recycle();
        //}
        //return targetBitmap;
    }

    /**
     * 旋转图片
     *
     * @param originBitmap 需要旋转的Bitmap
     * @param degree       需要旋转的角度
     * @return 旋转后的图片
     */
    public static Bitmap rotate(@NotNull Bitmap originBitmap, int degree) {
        int width = originBitmap.getWidth();
        int height = originBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(originBitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap create(@NotNull Bitmap sourceBitmap, int startX, int startY, int width, int height) {
        return Bitmap.createBitmap(sourceBitmap, startX, startY, width, height);
    }
}
