package com.example.study.demo.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityQrCodeBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import java.util.Hashtable;

public class QRCodeActivity extends BaseActivity<ActivityQrCodeBinding> {
    //声明常量
    public static final int DEFAULT_VIEW = 0x22;
    private static final int REQUEST_CODE_SCAN = 0X01;

    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding.setClick(new ClickHandler());
    }

    @Override
    protected void initViews() {


       mBinding.qrWithLogo .setImageBitmap(createQRCodeWithLogo("Hello Wo16556841616bkh549+6" +
               "58416894658491651984964knjolbjlhufuvjghjugo870i=9；，；ljniolnlkjbn1651kjhvkhgrld,",300,BitmapFactory.decodeResource(getResources(),R.drawable.android))) ;

    }

    /**
     * 生成带logo的二维码，logo默认为二维码的1/5
     *
     * @param text    需要生成二维码的文字、网址等
     * @param size    需要生成二维码的大小（）
     * @param mBitmap logo文件
     * @return bitmap
     */
    public static Bitmap createQRCodeWithLogo(String text, int size, Bitmap mBitmap) {
        try {
            int IMAGE_HALFWIDTH = size / 5;
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN,0);
            /*
             * 设置容错级别，默认为ErrorCorrectionLevel.L
             * 因为中间加入logo所以建议你把容错级别调至H,否则可能会出现识别不了
             */
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text,
                    BarcodeFormat.QR_CODE, size, size, hints);

            int width = bitMatrix.getWidth();//矩阵高度
            int height = bitMatrix.getHeight();//矩阵宽度
            int halfW = width / 2;
            int halfH = height / 2;

            Matrix m = new Matrix();
            float sx = (float) 2 * IMAGE_HALFWIDTH / mBitmap.getWidth();
            float sy = (float) 2 * IMAGE_HALFWIDTH
                    / mBitmap.getHeight();
            m.setScale(sx, sy);
            //设置缩放信息
            //将logo图片按martix设置的信息缩放
            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0,
                    mBitmap.getWidth(), mBitmap.getHeight(), m, false);

            int[] pixels = new int[size * size];
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH
                            && y > halfH - IMAGE_HALFWIDTH
                            && y < halfH + IMAGE_HALFWIDTH) {
                        //该位置用于存放图片信息
                        //记录图片每个像素信息
                        pixels[y * width + x] = mBitmap.getPixel(x - halfW
                                + IMAGE_HALFWIDTH, y - halfH + IMAGE_HALFWIDTH);
                    } else {
                        if (bitMatrix.get(x, y)) {
                            pixels[y * size + x] = 0xff000000;
                        } else {
                            pixels[y * size + x] = 0xffffffff;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(size, size,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "QRCodeActivity";
    }

    public class ClickHandler {
        public void createQRCode() {
            Log.d(getTag(), "生成二维码");
            String str = mBinding.etQrString.getText().toString();
            bitmap = getBitmap(str, 300, 300);
            mBinding.ivQrImage.setImageBitmap(bitmap);
        }

        public void scanByImage() {
            try {
                String str = scanningImage(bitmap);
                mBinding.tvScanResult.setText(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void scanByCamera() {
            if (ActivityCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QRCodeActivity.this, new String[]{Manifest.permission.CAMERA}, 0);
            }
            if (ActivityCompat.checkSelfPermission(QRCodeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(QRCodeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }
            //申请权限之后，调用DefaultView扫码界面
            ScanUtil.startScan(
                    QRCodeActivity.this,
                    REQUEST_CODE_SCAN,
                    new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.QRCODE_SCAN_TYPE).create());
        }
    }

    public Bitmap getBitmap(String text, int width, int height) {
        try {
            if (TextUtils.isEmpty(text)) {
                Toast.makeText(this, "请输入字符串", Toast.LENGTH_SHORT).show();
                return null;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String scanningImage(Bitmap bitmap) {
        int mWidth, mHeight;
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();
        int[] pixels = new int[mWidth * mHeight];
        bitmap.getPixels(pixels, 0, mWidth, 0, 0, mWidth, mHeight);
        RGBLuminanceSource source = new RGBLuminanceSource(mWidth, mHeight, pixels);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        try {
            Result result = reader.decode(binaryBitmap, hints);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3.重写onActivityResult方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //当扫码页面结束后，处理扫码结果
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        //从onActivityResult返回data中，用 ScanUtil.RESULT作为key值取到HmsScan返回值
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    mBinding.tvScanResult.setText(((HmsScan) obj).getOriginalValue());
                }
            }
        }
    }

}