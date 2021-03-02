package com.example.study;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.study.manager.ActivityStackManager;
import com.example.study.manager.LanguageUtil;
import com.example.study.manager.MMKVUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    protected T mBinding = null;
    private ArrayList<Fragment> fragments;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void attachBaseContext(Context newBase) {
        String language = MMKVUtil.getLanguage();
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, language));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0) {
            if (useDataBinding()) {
                mBinding = DataBindingUtil.setContentView(this, getLayoutId());
                Log.d(getTag(), "dataBinding");
            } else {
                setContentView(getLayoutId());
                ButterKnife.bind(this);
            }
        }
        ActivityStackManager.addActivity(this);

        initViews();
    }

    protected CompositeDisposable getCompositeDisposable() {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        return mCompositeDisposable;
    }

    protected void addToCompositeDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    protected void destroyCompositeDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }

    protected void removeFromCompositeDisposable(Disposable disposable) {
        getCompositeDisposable().remove(disposable);
    }

    @Override
    public void finish() {

        ActivityStackManager.removeActivity(this);
        super.finish();
    }

    protected void showFragment(@IdRes int container, Fragment fragment, String tag) {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fragment1 : fragments) {
            transaction.hide(fragment);
        }

        if (fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.add(container, fragment, tag)
                    .show(fragment);
        }
        transaction.commitAllowingStateLoss();
        fragments.add(fragment);


    }

    protected T getBinding() {
        return mBinding;
    }

    protected abstract void initViews();

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract boolean useDataBinding();

    protected abstract String getTag();

    @Override
    protected void onDestroy() {
        destroyCompositeDisposable();
        ActivityStackManager.removeActivity(this);

        super.onDestroy();
    }
}
