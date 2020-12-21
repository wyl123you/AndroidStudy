package com.example.study.demo.mvvm;

import android.widget.Toast;

import androidx.lifecycle.LifecycleRegistry;

import com.example.study.R;
import com.example.study.adapter.DatabindingAdapter;
import com.example.study.base.BaseActivity;
import com.example.study.databinding.ActivityMvvmBinding;

import java.util.ArrayList;

public class MVVMActivity extends BaseActivity<ActivityMvvmBinding> {

    private Person person;
    private ArrayList<Person> people;
    private LifecycleRegistry registry;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mvvm;
    }

    @Override
    protected boolean useDataBinding() {
        return true;
    }

    @Override
    protected String getTag() {
        return "MVVMActivity";
    }

    @Override
    protected void initViews() {
        people = new ArrayList<>();
        mBinding.setA(this);
        person = new Person("邬友亮", 23);
        person.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        mBinding.setPerson(person);
        getLifecycle().addObserver(person);

        Person person = new Person("邬友亮", 24);
        person.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        people.add(person);

        Person person1 = new Person("邬友亮", 25);
        person1.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        people.add(person1);

        Person person2 = new Person("邬友亮", 24);
        person2.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        people.add(person2);

        mBinding.setAdapter(new DatabindingAdapter(people, this));

        registry = new LifecycleRegistry(this);
//        registry.markState(Lifecycle.State.CREATED);
//        getLifecycle().
//        registry.addObserver(person2);

        getLifecycle().addObserver(person2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        registry.markState(Lifecycle.State.DESTROYED);
//        registry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
    }

    public void show() {
        Toast.makeText(this, person.toString(), Toast.LENGTH_LONG).show();
    }

    public void confirm() {
        person.setName(mBinding.name.getText().toString());
    }
}
