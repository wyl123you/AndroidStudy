package com.example.study.demo.moveUnlock;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.study.R;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/2/22 下午3:00
 * @Company LotoGram
 */

public class ImageAuthenticationView1 extends AppCompatImageView {

    private final String TAG = this.getClass().getSimpleName();

    //定义画笔
    private final Paint mPaint;

    //验证滑块的高
    private int mUnitHeight;

    //验证滑块的宽
    private int mUnitWidth;

    //验证滑块宽占用整体图片大小的比例,默认1/12
    private final int mUnitWidthScale;

    //验证滑块高度占用整体图片大小的比例,默认1/10
    private final int mUnitHeightScale;

    //随机生成滑块的X坐标
    private int mUnitRandomX;

    //随机生成滑块的Y坐标
    private int mUnitRandomY;

    //滑块移动的距离
    private float mUnitMoveDistance = 0;

    //验证位置图像(目标位置图片)
    private final Bitmap mTargetBitmap;

    //滑块图像
    private Bitmap mSourceBitmap;

    //滑块背景阴影图像
    private final Bitmap mShadowBitmap;

    //验证的图像
    private Bitmap mBitmap;

    //是否需要旋转
    private final boolean needRotate;

    //旋转的角度
    private int rotate;

    //判断是否完成的偏差量，默认为10
    public int DEFAULT_DEVIATE;

    //判断是否重新绘制图像
    private boolean isReset = true;

    //回调
    private OnPuzzleListener mListener;

    public ImageAuthenticationView1(@NonNull Context context) {
        this(context, null);
    }

    public ImageAuthenticationView1(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageAuthenticationView1(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ImageAuthenticationView1);
        mUnitWidth = ta.getDimensionPixelOffset(R.styleable.ImageAuthenticationView1_unitWidth1, 0);
        mUnitHeight = ta.getDimensionPixelOffset(R.styleable.ImageAuthenticationView1_unitHeight1, 0);
        mUnitHeightScale = ta.getInteger(R.styleable.ImageAuthenticationView1_unitHeightScale1, 10);
        mUnitWidthScale = ta.getInteger(R.styleable.ImageAuthenticationView1_unitWidthScale1, 12);
        Drawable grayBlock = ta.getDrawable(R.styleable.ImageAuthenticationView1_unitShowSrc1);
        //目标未知的灰色块
        mTargetBitmap = drawableToBitmap(grayBlock);
        Drawable shadowBlock = ta.getDrawable(R.styleable.ImageAuthenticationView1_unitShadeSrc1);
        //滑动块的背景
        mShadowBitmap = drawableToBitmap(shadowBlock);
        needRotate = ta.getBoolean(R.styleable.ImageAuthenticationView1_needRotate1, true);
        DEFAULT_DEVIATE = ta.getInteger(R.styleable.ImageAuthenticationView1_deviate1, 10);
        ta.recycle();

        if (DEFAULT_DEVIATE > 20) {
            throw new IllegalArgumentException("DEFAULT_DEVIATE can not be bigger than 20");
        }

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isReset) {
            mBitmap = getBaseBitmap();

            if (mUnitWidth == 0) {
                mUnitWidth = mBitmap.getWidth() / mUnitWidthScale;
            }

            if (mUnitHeight == 0) {
                mUnitHeight = mBitmap.getHeight() / mUnitHeightScale;
            }
            initUnitXY();
            mSourceBitmap = BitmapUtil.create(mBitmap, mUnitRandomX, mUnitRandomY, mUnitWidth, mUnitHeight);
            isReset = false;
        }

        canvas.drawBitmap(drawTargetBitmap(), mUnitRandomX, mUnitRandomY, mPaint);
        canvas.drawBitmap(drawSourceBitmap(), mUnitMoveDistance, mUnitRandomY, mPaint);
    }

    /**
     * 随机生成生成滑块的XY坐标
     */
    private void initUnitXY() {
        mUnitRandomX = (int) (Math.random() * (mBitmap.getWidth() - mUnitWidth));
        mUnitRandomY = (int) (Math.random() * (mBitmap.getHeight() - mUnitHeight));
        //防止生成的位置距离太近
        if (mUnitRandomX <= mBitmap.getWidth() / 2) {
            mUnitRandomX = mUnitRandomX + mBitmap.getWidth() / 4;
        }
        //防止生成的X坐标截图时导致异常
        if (mUnitRandomX + mUnitWidth > getWidth()) {
            initUnitXY();
        }
    }

    private Bitmap getBaseBitmap() {
        Bitmap bitmap = drawableToBitmap(getDrawable());
        return BitmapUtil.scale(bitmap, getWidth(), getHeight());
    }

    /**
     * 创建结滑块图片
     *
     * @return 滑块图片
     */
    private Bitmap drawSourceBitmap() {
        //绘制滑块
        Bitmap shadowBitmap;
        if (mShadowBitmap != null) {
            shadowBitmap = BitmapUtil.scale(mShadowBitmap, mUnitWidth, mUnitHeight);
        } else {
            shadowBitmap = BitmapUtil.scale(BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_shade), mUnitWidth, mUnitHeight);
        }

        // 如果需要旋转图片,进行旋转,旋转后为了和画布大小保持一致,避免出现图像显示不全,需要重新缩放比例
        if (needRotate) {
            shadowBitmap = BitmapUtil.scale(BitmapUtil.rotate(shadowBitmap, rotate), mUnitWidth, mUnitHeight);
        }

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        Bitmap resultBmp = Bitmap.createBitmap(mUnitWidth, mUnitHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBmp);
        canvas.drawBitmap(shadowBitmap, new Rect(0, 0, mUnitWidth, mUnitHeight), new Rect(0, 0, mUnitWidth, mUnitHeight), paint);
        //选择交集去上层图片
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawBitmap(mSourceBitmap, new Rect(0, 0, mUnitWidth, mUnitHeight), new Rect(0, 0, mUnitWidth, mUnitHeight), paint);
        return resultBmp;
    }

    /**
     * 创建目标图片(阴影部分)
     *
     * @return 目标图片
     */
    private Bitmap drawTargetBitmap() {
        Bitmap targetBitmap;
        if (mTargetBitmap != null) {
            targetBitmap = BitmapUtil.scale(mTargetBitmap, mUnitWidth, mUnitHeight);
        } else {
            targetBitmap = BitmapUtil.scale(BitmapFactory.decodeResource(getResources(), R.drawable.puzzle_show), mUnitWidth, mUnitHeight);
        }
        //如果需要旋转图片,进行旋转，旋转后为了保持和滑块大小一致,需要重新缩放比例
        if (needRotate) {
            targetBitmap = BitmapUtil.scale(BitmapUtil.rotate(targetBitmap, rotate), mUnitWidth, mUnitHeight);
        }
        return targetBitmap;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            return bitmapDrawable.getBitmap();
        }

        //获取drawable的宽高
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        //创建Bitmap后无法进行修改
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //创建对应的Bitmap画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 滑块移动距离
     *
     * @param distance 滑块移动的距离
     */
    public void setUnitMoveDistance(float distance) {
        mUnitMoveDistance = distance;
        //防止滑块滑出图片
        if (mUnitMoveDistance > mBitmap.getWidth() - mUnitWidth) {
            mUnitMoveDistance = mBitmap.getWidth() - mUnitWidth;
        }
        invalidate();
    }

    /**
     * 获取每次滑动的平均偏移值
     *
     * @return 每次滑动的平均偏移值
     */
    public float getAverageDistance(int max) {
        return (float) (mBitmap.getWidth() - mUnitWidth) / max;
    }

    /**
     * 验证是否拼接成功
     */
    public void testPuzzle() {
        if (Math.abs(mUnitMoveDistance - mUnitRandomX) <= DEFAULT_DEVIATE) {
            if (mListener != null) {
                mListener.onSuccess();
            }
        } else {
            if (mListener != null) {
                mListener.onFail();
            }
        }
    }

    /**
     * 重置
     */
    public void reset() {
        isReset = true;
        mUnitMoveDistance = 0;
        if (needRotate) {
            rotate = (int) (Math.random() * 3) * 90;
        } else {
            rotate = 0;
        }
        invalidate();
    }

    /**
     * 拼图成功的回调
     **/
    public interface OnPuzzleListener {
        void onSuccess();

        void onFail();
    }

    /**
     * 设置回调
     *
     * @param listener 监听器
     */
    public void setPuzzleListener(OnPuzzleListener listener) {
        this.mListener = listener;
    }
}