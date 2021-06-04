package com.example.study.demo.diyExtension;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.study.R;
import com.example.study.databinding.ActivityDiyExtensionBinding;
import com.example.study.demo.mvvm.Person;
import com.example.study.manager.LogUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DiyExtensionActivity extends AppCompatActivity {

    private ActivityDiyExtensionBinding binding;

    private String TAG = "DiyExtensionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diy_extension);
        binding.setLifecycleOwner(this);
    }

    public void write(View view) {
        String content = binding.editText1.getText().toString();
        LogUtil.v(TAG, content, content + "1", content + "2");
        LogUtil.v(TAG, new Person("aa", 23, "aa"));
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_LONG).show();
        }

        byte[] bytes = content.getBytes();

        byte temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = bytes[i];
            bytes[i] = (byte) (~temp);
        }

        File file = new File(getFilesDir(), "www.abc");

        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void read(View view) {
        new Thread(() -> {
            File file = new File(getFilesDir(), "www.abc");
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                FileInputStream stream = new FileInputStream(file);

                BufferedInputStream bufferedInputStream = new BufferedInputStream(stream);

                int len;
                byte[] bytes = new byte[1024];

                while ((len = bufferedInputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                }

                byte[] a = outputStream.toByteArray();
                byte temp;
                for (int i = 0; i < a.length; i++) {
                    temp = a[i];
                    a[i] = (byte) (~temp);
                }

                outputStream.flush();
                outputStream.close();
                stream.close();
                runOnUiThread(() -> binding.content.setText(new String(a)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}