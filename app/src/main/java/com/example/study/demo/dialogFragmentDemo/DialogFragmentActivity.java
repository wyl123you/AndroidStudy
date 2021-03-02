package com.example.study.demo.dialogFragmentDemo;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityDialogFragmentBinding;

public class DialogFragmentActivity extends BaseActivity<ActivityDialogFragmentBinding> {

    @Override
    protected void initViews() {
        mBinding.setClick(new ClickHandler());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dialog_fragment;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "DialogFragmentActivity";
    }

    public class ClickHandler {
        public void onOpenFragment() {
            PasswordDialogFragment fragment = new PasswordDialogFragment.Builder()
                    .setTitle("输入密码")
                    .setTip("若忘记密码，请重新设置!")
                    .setCancelable(false)
                    .build();
            fragment.show(getSupportFragmentManager(), "PasswordDialogFragment");
        }
    }
}