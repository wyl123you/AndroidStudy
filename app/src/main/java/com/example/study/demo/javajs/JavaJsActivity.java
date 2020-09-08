package com.example.study.demo.javajs;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;

import com.example.study.R;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityJavaJaBinding;

@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class JavaJsActivity extends BaseActivity<ActivityJavaJaBinding> {


    @Override
    protected void initViews() {
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.loadUrl("http://47.102.137.211/wyl/index.html");
        mBinding.webview.addJavascriptInterface(JavaJsActivity.this, "Android");

        mBinding.tvAndroidcalljs.setOnClickListener(v -> mBinding.webview.loadUrl("javascript:javacalljs()"));

        mBinding.tvAndroidcalljsargs.setOnClickListener(v -> mBinding.webview.loadUrl("javascript:javacalljswith(" + "'Android传过来的参数'" + ")"));


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_java_ja;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "JavaJsActivity";
    }

    @JavascriptInterface
    public void jsCallAndroid() {
        mBinding.tvShowmsg.setText("Js调用Android方法");
    }

    @JavascriptInterface
    public void jsCallAndroidArgs(String args) {
        mBinding.tvShowmsg.setText(args);
    }
}