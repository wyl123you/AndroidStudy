package com.example.study.demo.mvvm;

import android.widget.Toast;

import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.study.R;
import com.example.study.adapter.DatabindingAdapter;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityMvvmNewBinding;

public class MVVMnewActivity extends BaseActivity<ActivityMvvmNewBinding> {

    private ViewModel viewModel;

    @Override
    protected void initViews() {
        viewModel = new ListViewModel();
        mBinding.setViewModel((ListViewModel) viewModel);
        DatabindingAdapter adapter = new DatabindingAdapter(((ListViewModel) viewModel).getPerson(), this);

        mBinding.setAdapter(adapter);

        ((ListViewModel) viewModel).getName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MVVMnewActivity.this, "变化了", Toast.LENGTH_LONG).show();
            }
        });
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
        ((ListViewModel) viewModel).getName().setValue(String.valueOf(System.currentTimeMillis()));
    }
}
