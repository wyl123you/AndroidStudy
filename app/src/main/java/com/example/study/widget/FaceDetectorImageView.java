package com.example.study.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.study.R;

public class FaceDetectorImageView extends AppCompatImageView {
    // 人脸监测对象
    private FaceDetector faceDetector;
    // 识别到的人脸
    private FaceDetector.Face[] faces;

    // 需要识别的图片
    private Bitmap bitmap;
    // 最多需要识别的人数
    private int maxFaces = 10;
    // 真正识别到的人数
    private int realFaces = 0;
    private Paint paint;

    public FaceDetectorImageView(Context context) {
        this(context, null);
    }

    public FaceDetectorImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceDetectorImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 一定要设置，否则无法识别人脸
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.faces, options);

        // 识别人脸数组定义
        faces = new FaceDetector.Face[maxFaces];
        faceDetector = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), maxFaces);

        // 人脸识别器开始识别，速度还是比较快的，放在主线程也没什么问题
        realFaces = faceDetector.findFaces(bitmap, faces);

        // 初始化画笔工具
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(getResources().getColor(R.color.colorAccent));
        paint.setStrokeWidth(5);
        setImageBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 在识别的每个人的眼睛位置画正方形
        for (int i = 0; i < realFaces; i++) {
            FaceDetector.Face face = faces[i];
            float distance = face.eyesDistance();
            PointF mid = new PointF();
            face.getMidPoint(mid);
            canvas.drawRect(mid.x - distance / 2,
                    mid.y - distance / 2,
                    mid.x + distance / 2,
                    mid.y + distance / 2, paint);
        }
    }
}

