package com.example.study.demo.mvvm;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LifecycleRegistry;

import com.example.study.BaseActivity;
import com.example.study.R;
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
        person = new Person("邬友亮", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        mBinding.setPerson(person);

        Person person1 = new Person("邬友亮1", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        Person person2 = new Person("邬友亮2", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d");
        Person person3 = new Person("邬友亮3", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        Person person4 = new Person("邬友亮4", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        Person person5 = new Person("邬友亮5", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8");
        people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person3);
        people.add(person4);
        people.add(person5);
        PersonAdapter adapter = new PersonAdapter(people, this);
        mBinding.setAdapter(adapter);

        getLifecycle().addObserver(person);
    }


    public void show() {
        Toast.makeText(this, person.toString(), Toast.LENGTH_LONG).show();
    }

    public class ClickHandler {

        public void onConfirm() {
            Log.d(TAG, "onConfirm: ");
        }
    }
}
