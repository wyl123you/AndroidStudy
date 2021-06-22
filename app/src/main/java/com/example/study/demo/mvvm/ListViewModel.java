package com.example.study.demo.mvvm;

import android.util.Log;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.study.demo.retrofit.ApiRequest;
import com.example.study.demo.retrofit.BaseObserver;
import com.example.study.demo.retrofit.LuckyMoney;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

//ViewModel用来处理业务逻辑
public class ListViewModel extends ViewModel implements LifecycleObserver {
    //LiveData是一个抽象类，他的实现子类有MutableLiveData,MediatorLiveData,前者常用
    //常常结合ViewModel一起使用。

    private MutableLiveData<String> name = new MediatorLiveData<>();

    private MutableLiveData<ArrayList<LuckyMoney>> luckMoneys = new MediatorLiveData<>();

    public MutableLiveData<String> getNameValue() {
        return name;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public ListViewModel() {

    }

    public MutableLiveData<ArrayList<LuckyMoney>> getLuckMoneys() {
        return luckMoneys;
    }

    public ArrayList<Person> getPerson() {
        Person person1 = new Person("邬友亮1", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        Person person2 = new Person("邬友亮2", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        Person person3 = new Person("邬友亮3", 23, "https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        person1.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        person2.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");
        person3.setImageUrl("https://pics5.baidu.com/feed/562c11dfa9ec8a132169ba503e89d688a1ecc0a5.jpeg?token=640e1ae26c187b992869c0a1cc2378e8");

        ArrayList<Person> people = new ArrayList<>();
        people.add(person1);
        people.add(person2);
        people.add(person3);
        return people;
    }

    public String getTitle() {
        return name.getValue();
    }

    public void change() {
        name.setValue(String.valueOf(System.currentTimeMillis()));
        Log.d("TAG", "change: " + name.getValue());
        getByHtp();
    }

    public void getByHtp() {
        ApiRequest.create().getAllLuckyMoney()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<ArrayList<LuckyMoney>>() {
                    @Override
                    public void onNext(@NotNull ArrayList<LuckyMoney> luckyMonies) {
                        super.onNext(luckyMonies);
                        luckMoneys.setValue(luckyMonies);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.d("TAG", "onError: ");
                    }
                });
    }
}