package com.example.study.demo.mvvm;

import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
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

    public Person(String name, int age, String imageUrl) {
        this.name = name;
        this.age = age;
        this.imageUrl = imageUrl;
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

    @BindingAdapter("src")
    public static void setSource(@NotNull ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .into(imageView);
    }

    @BindingAdapter("background")
    public static void setBackground(@NotNull ImageView imageView, @ColorInt int color) {
        imageView.setBackgroundColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @BindingAdapter("background")
    public static void setBackground(@NotNull ImageView imageView, @NotNull Color color) {
        imageView.setBackgroundColor(color.toArgb());
    }

    @BindingAdapter("foreground")
    public static void setForeground(@NotNull ImageView imageView, @DrawableRes int url) {
        imageView.setImageResource(url);
    }

//    @NotNull
//    @Override
//    public String toString() {
//        Log.d("TAG", "toString: 1321313");
//        return "Person{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}';
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void print() {
        Log.d("TAG", "print: ");
    }

    @Override
    public void onChanged(String o) {
        Log.d("Person", "onChanged: ");
    }
}