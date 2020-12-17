package com.example.study.demo.mvvm;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;

import com.bumptech.glide.Glide;
import com.example.study.BR;

import org.jetbrains.annotations.NotNull;

public class Person extends BaseObservable implements LifecycleObserver, Observer<String> {

    private String name;
    private int age;
    private String imageUrl;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName() {
        return name;
    }

    @Bindable
    public String getImageUrl() {
        return imageUrl;
    }

    @Bindable
    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        notifyPropertyChanged(BR.imageUrl);
    }

    public void setAge(int age) {
        this.age = age;
        notifyPropertyChanged(BR.age);
    }

    public void setAge(String age) {
        if (TextUtils.isEmpty(age)) {
            this.age = 0;
        } else {
            this.age = Integer.parseInt(age);
        }
        notifyPropertyChanged(BR.age);
    }

    @BindingAdapter("imageUrl")
    public static void imageLoader(@NotNull ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @NotNull
    @Override
    public String toString() {
        Log.d("TAG", "toString: 1321313");
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void print() {
        Log.d("TAG", "print: ");
    }

    @Override
    public void onChanged(String o) {
        Log.d("Person", "onChanged: ");
    }
}
