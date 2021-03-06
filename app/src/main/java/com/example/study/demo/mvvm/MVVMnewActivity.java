package com.example.study.demo.mvvm;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;

import com.example.study.BaseActivity;
import com.example.study.R;
import com.example.study.databinding.ActivityMvvmNewBinding;
import com.example.study.demo.retrofit.LuckyMoney;

public class MVVMnewActivity extends BaseActivity<ActivityMvvmNewBinding> {

    private ViewModel viewModel;

    @Override
    protected void initViews() {
        viewModel = new ListViewModel();
        mBinding.setViewModel((ListViewModel) viewModel);
        ((ListViewModel) viewModel).getNameValue().observe(this, s -> Toast.makeText(MVVMnewActivity.this, "新的值是：" + s, Toast.LENGTH_LONG).show());

        ((ListViewModel) viewModel).getLuckMoneys().observe(this, s -> {
//                Toast.makeText(MVVMnewActivity.this, "新的值是：" + , Toast.LENGTH_LONG).show();
            for (LuckyMoney luckyMoney : s) {
                Log.d(TAG, "onChanged: " + luckyMoney.toString());
            }
        });

        DataBindingAdapter adapter = new DataBindingAdapter(((ListViewModel) viewModel).getPerson(), this);
        mBinding.setAdapter(adapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvvm_new;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "MVVMnewActivity";
    }

    public void change() {
        ((ListViewModel) viewModel).getNameValue().setValue(String.valueOf(System.currentTimeMillis()));
    }
}
