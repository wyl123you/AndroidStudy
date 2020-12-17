package com.example.study.demo.mvvm;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.study.R;
import com.example.study.adapter.DatabindingAdapter;
import com.example.study.base.BaseActivity;
import com.example.study.bean.LuckyMoney;
import com.example.study.databinding.ActivityMvvmNewBinding;

import java.util.ArrayList;

public class MVVMnewActivity extends BaseActivity<ActivityMvvmNewBinding> {

    private ViewModel viewModel;

    @Override
    protected void initViews() {
        viewModel = new ListViewModel();
        mBinding.setViewModel((ListViewModel) viewModel);
        ((ListViewModel) viewModel).getNameValue().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(MVVMnewActivity.this, "新的值是：" + s, Toast.LENGTH_LONG).show();
            }
        });

        ((ListViewModel) viewModel).getLuckMoneys().observe(this, new Observer<ArrayList<LuckyMoney>>() {
            @Override
            public void onChanged(ArrayList<LuckyMoney> s) {
//                Toast.makeText(MVVMnewActivity.this, "新的值是：" + , Toast.LENGTH_LONG).show();
                for (LuckyMoney luckyMoney:s){
                    Log.d(TAG, "onChanged: "+luckyMoney.toString());
                }
            }
        });

        DatabindingAdapter adapter = new DatabindingAdapter(((ListViewModel) viewModel).getPerson(), this);
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
