package com.example.study.demo.moveUnlock;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.study.R;


public class ImageAuthenticationView extends AppCompatImageView {
    /**
     * 定义画笔
     */
    private Paint mPaint;
    /**
     * 验证的图像
     */
    private Bitmap mBitmap;
    /**
     * 验证滑块的高
     */
    private int mUintHeight;
    /**
     * 验证滑块的宽
     */
    private int mUintWidth;
    /**
     * 验证滑块宽占用整体图片大小的比例,默认1/12
     */
    private int mUnitWidthScale;
    /**
     * 验证滑块高度占用整体图片大小的比例,默认1/10
     */
    private int mUnitHeightScale;
    /**
     * 随机生成滑块的X坐标
     */
    private int mUnitRandomX;
    /**
     * 随机生成滑块的Y坐标
     */
    private int mUnitRandomY;
    /***
     * 滑块移动的距离
     */
    private float mUnitMoveDistance = 0;
    /***
     * 滑块图像
     */
    private Bitmap mSourceBitmap;
    /**
     * 验证位置图像
     */
    private Bitmap mTargetBitmap;
    /**
     * 背景阴影图像
     */
    private Bitmap mShadowBitmap;
    /**
     * 是否需要旋转
     **/
    private boolean needRotate;
    /**
     * 旋转的角度
     */
    private int rotate;
    /**
     * 判断是否完成的偏差量，默认为10
     */
    public int DEFAULT_DEVIATE;
    /**
     * 判断是否重新绘制图像
     */
    private boolean isReSet = true;

    /**
     * 拼图成功的回调
     **/
    public interface onPuzzleListener {
        void onSuccess();

        void onFail();
    }

    /**
     * 回调
     */
    private onPuzzleListener mlistener;

    /**
     * 设置回调
     *
     * @param listener 监听
     */
    public void setPuzzleListener(onPuzzleListener listener) {
        this.mlistener = listener;
    }

    public ImageAuthenticationView(Context context) {
        this(context, null);
    }

    public ImageAuthenticationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageAuthenticationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageAuthenticationView);
        mUintWidth = ta.getDimensionPixelOffset(R.styleable.ImageAuthenticationView_unitHeight, 0);
        mUintHeight = ta.getDimensionPixelOffset(R.styleable.ImageAuthenticationView_unitHeight, 0);
        mUnitHeightScale = ta.getInteger(R.styleable.ImageAuthenticationView_unitHeightScale, 10);
        mUnitWidthScale = ta.getInteger(R.styleable.ImageAuthenticationView_unitWidthScale, 12);
        Drawable showBp = ta.getDrawable(R.styleable.ImageAuthenticationView_unitShowSrc);
        mTargetBitmap = drawableToBitamp(showBp);
        Drawable shadeBp = ta.getDrawable(R.styleable.ImageAuthenticationView_unitShadeSrc);
        mShadowBitmap = drawableToBitamp(shadeBp);
        needRotate = ta.getBoolean(R.styleable.ImageAuthenticationView_needRotate, true);
        DEFAULT_DEVIATE = ta.getInteger(R.styleable.ImageAuthenticationView_deviate, 10);
        ta.recycle();

        //初始化
        mPaint = new Paint();
        //抗锯齿
        mPaint.setAntiAlias(true);
        //是否需要旋转
        if (needRotate) {
            rotate = (int) (Math.random() * 3) * 90;
        } else {
            rotate = 0;
        }
    }


    /**
     * 随机生成生成滑块的XY坐标
     */
    private void initUnitXY() {
        mUnitRandomX = (int) (Math.random() * (mBitmap.getWidth() - mUintWidth));
        mUnitRandomY = (int) (Math.random() * (mBitmap.getHeight() - mUintHeight));
        // 防止生成的位置距离太近
        if (mUnitRandomX <= mBitmap.getWidth() / 2) {
            mUnitRandomX = mUnitRandomX + mBitmap.getWidth() / 4;
        }
        // 防止生成的X坐标截图时导致异常
        if (mUnitRandomX + mUintWidth > getWidth() || mUnitRandomX <= mBitmap.getWidth() / 2) {
            initUnitXY();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReSet) {
            mBitmap = getBaseBitmap();

            if (0 == mUintWidth) {
                mUintWidth = mBitmap.getWidth() / mUnitWidthScale;
            }

            if (0 == mUintHeight) {
                mUintHeight = mBitmap.getHeight() / mUnitHeightScale;
            }

            initUnitXY();
            mSourceBitmap = Bitmap.createBitmap(mBitmap, mUnitRandomX, mUnitRandomY, mUintWidth, mUintHeight);
        }
        isReSet = false;
        canvas.drawBitmap(drawTargetBitmap(), mUnitRandomX, mUnitRandomY, mPaint);
        canvas.drawBitmap(drawResultBitmap(mSourceBitmap), mUnitMoveDistance, mUnitRandomY, mPaint);
    }

    /**
     * 重置
     */
    public void reset() {
        isReSet = true;
        mUnitMoveDistance = 0;
        if (needRotate) {
            rotate = (int) (Math.random() * 3) * 90;
        } else {
            rotate = 0;
        }
        invalidate();
    }

    /**
     * 获取每次滑动的平均偏移值
     *
     * @return xx
     */
    public float getAverageDistance(int max) {
        return (float) (mBitmap.getWidth() - mUintWidth) / max;
    }


    /**
     * 滑块移动距离
     *
     * @param distance ss
     */
    public void setUnitMoveDistance(float distance) {
        mUnitMoveDistance = distance;
        // 防止滑块滑出图片
        if (mUnitMoveDistance > mBitmap.getWidth() - mUintWidth) {
            mUnitMoveDistance = mBitmap.getWidth() - mUintWidth;
        }
        invalidate();

    }

    /**
     * 验证是否拼接成功
     */
    public void testPuzzle() {
        if (Math.abs(mUnitMoveDistance - mUnitRandomX) <= DEFAULT_DEVIATE) {
            if (null != mlistener) {
                mlistener.onSuccess();
            }
        } else {
            if (null != mlistener) {
                mlistener.onFail();
            }
        }
    }

    /**
     * 创建目标图片(阴影部分)
     *
     * @return dd
     */
    private Bitmap drawTargetBitmap() {
        // 绘制图片
        Bitmap showB;
        if (null != mTargetBitmap) {
            showB = BitmapUtil.scale(mTargetBitmap, mUintWidth, mUintHeight);
        } else {
            showB = BitmapUtil.scale(BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_show), mUintWidth, mUintHeight);
        }
        // 如果需要旋转图片,进行旋转，旋转后为了保持和滑块大小一致,需要重新缩放比例
        if (needRotate) {
            showB = BitmapUtil.scale(rotateBitmap(rotate, showB), mUintWidth, mUintHeight);
        }
        return showB;
    }

    /**
     * 创建结滑块图片
     *
     * @param bp vv
     */
    private Bitmap drawResultBitmap(Bitmap bp) {
        // 绘制图片
        Bitmap shadeB;
        if (null != mShadowBitmap) {
            shadeB = BitmapUtil.scale(mShadowBitmap, mUintWidth, mUintHeight);
        } else {
            shadeB = BitmapUtil.scale(BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_shade), mUintWidth, mUintHeight);
        }
        // 如果需要旋转图片,进行旋转,旋转后为了和画布大小保持一致,避免出现图像显示不全,需要重新缩放比例
        if (needRotate) {
            shadeB = BitmapUtil.scale(rotateBitmap(rotate, shadeB), mUintWidth, mUintHeight);
        }

        Bitmap resultBmp = Bitmap.createBitmap(mUintWidth, mUintHeight,
                Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Canvas canvas = new Canvas(resultBmp);
        canvas.drawBitmap(shadeB, new Rect(0, 0, mUintWidth, mUintHeight),
                new Rect(0, 0, mUintWidth, mUintHeight), paint);
        // 选择交集去上层图片
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(bp, new Rect(0, 0, mUintWidth, mUintHeight),
                new Rect(0, 0, mUintWidth, mUintHeight), paint);
        return resultBmp;
    }

    /**
     * 获取实际显示的图片
     *
     * @return dd
     */
    public Bitmap getBaseBitmap() {
        //获取背景图片Bitmap
        Bitmap b = drawableToBitamp(getDrawable());
        float scaleX;
        float scaleY;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值；
        scaleX = getWidth() * 1.0f / b.getWidth();
        scaleY = getHeight() * 1.0f / b.getHeight();
        //按比例缩放
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                matrix, true);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable ss
     * @return ss
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (null == drawable) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();//该方法用于BitmapDrawable转换成Bitmap
        }

        //获取drawable的宽高
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        //创建Bitmap后无法进行修改
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        //创建对应的Bitmap画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 旋转图片
     *
     * @param degree dd
     * @param bitmap dd
     * @return gg
     */
    public Bitmap rotateBitmap(int degree, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    public int getMaxProgress() {
        return getWidth() - mUintWidth;
    }
}