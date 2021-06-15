package com.example.study.demo.screenRecord;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;

import java.io.IOException;

public class RecordSoundActivity extends AppCompatActivity {

    private static final String TAG = "RecordSoundActivity";


    //1) 创建 MediaRecorder对象。
    //2) 调用MediaRecorder对象的setAudioSource()方法设置声音来源，
    //   一般传入 MediaRecorder.AudioSource.MIC参数指定录制来自麦克风的声音。
    //3) 调用MediaRecorder对象的setOutputFormat()设置所录制的音频文件的格式。
    //4) 调用MediaRecorder对象的setAudioEncoder()、setAudioEncodingBitRate(intbitRate)、
    //   setAudioSamplingRate(int samplingRate)设置所录制的声音的编码格式、编码位率、采样率等，
    //   这些参数将可以控制所录制的声音的品质、文件的大小。一般来说，声音品质越好，声音文件越大。
    //5) 调用MediaRecorder的setOutputFile(StringPath)方法设置录制的音频文件的保存位置。
    //6) 调用MediaRecorder的prepare()方法准备录制。
    //7) 调用MediaRecorder对象的start()方法开始录制。
    //8) 录制完成，调用MediaRecorder对象的stop()方法停止录制，并调用release()方法释放资源。


    //提示：
    //1.上面的步骤中第3和第4两个步骤千万不能搞反，否则程序将会抛出IllegalStateException 异常。
    //2.设置声音编码格式要和声音的输出格式相对应，不然录制的音频文件不标准。
    //  如果编码格式和输出格式不对应，录制出的音频文件虽然可以播放，
    //  但是将多个这类音频文件合并之后，会出现只播放合并文件中的部分文件。

    private MediaRecorder mediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sound);
    }

    public void onStartRecordSound(View view) throws IOException {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        } else {
            mediaRecorder.reset();
        }
        //参数指定录制来自麦克风的声音
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置所录制的音频文件的格式(必须在设置声音编码格式之前设置)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        //设置所录制的声音的编码格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        //设置所录制的声音的编码位率
        mediaRecorder.setAudioEncodingBitRate(10000);
        //设置所录制的声音的采样率
        mediaRecorder.setAudioSamplingRate(1000);
        //
        mediaRecorder.setOutputFile(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC) + "/aaa.amr");
        Log.d(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath());

        mediaRecorder.prepare();
        // 开始录音
        mediaRecorder.start();
    }

    public void onStopRecordSound(View view) {
        // 停止录音
        mediaRecorder.stop();
        // 释放资源
        mediaRecorder.release();
        mediaRecorder = null;
    }
}