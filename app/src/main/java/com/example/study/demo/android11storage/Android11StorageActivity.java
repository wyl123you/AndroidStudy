package com.example.study.demo.android11storage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;

import java.io.FileOutputStream;
import java.io.IOException;

public class Android11StorageActivity extends AppCompatActivity {

    String TAG = "Android11StorageActivity";

    Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:");

    //targetSDK<29  则正常使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_android11_storage);


        findViewById(R.id.create_file).setOnClickListener(v -> createFile(uri));
    }

    private void createFile(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "yourFIleName.txt");
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.d(TAG, "创建成功");
            if (uri != null) {
                Log.d(TAG, data.getData().getPath());
                try {
                    FileOutputStream fos = (FileOutputStream) getContentResolver().openOutputStream(data.getData());

                    String aaa = "ssssssssss";

                    fos.write(aaa.getBytes());
                    fos.flush();
                    fos.close();

                    Log.d(TAG, "写入成功");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}