package com.example.study.demo.jni;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityJniBinding;

public class JniActivity extends BaseActivity<ActivityJniBinding> {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_jni;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "JniActivity";
    }

    @Override
    protected void initViews() {
        mBinding.image.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 50);
            }
        });
        mBinding.image.setClipToOutline(true);
    }

    public void onClick(View view) {

        try {
            int a = Integer.parseInt(mBinding.text.getText().toString().trim());
            int b = Integer.parseInt(mBinding.text1.getText().toString().trim());
            //Toast.makeText(JniActivity.this, "" + NDKTools.add(a, b), Toast.LENGTH_LONG).show();
            Toast.makeText(JniActivity.this, NDKTools.getStringFromNDK("1111"), Toast.LENGTH_LONG).show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(JniActivity.this, "null", Toast.LENGTH_LONG).show();

        }

    }
}
