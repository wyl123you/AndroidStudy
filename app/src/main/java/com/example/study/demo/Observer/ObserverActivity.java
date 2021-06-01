package com.example.study.demo.Observer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.study.R;

public class ObserverActivity extends AppCompatActivity {

    private static final String TAG = "ObserverActivity";

    private Dog dog;

    private MutableLiveData<Dog> dogMutableLiveData;
    private AndroidObservable androidObservable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observer);
        dog = new Dog();
        //生命周期观察
        getLifecycle().addObserver(dog);

        //使用JDK接口工具(java.util.Observer)类实现观察
        androidObservable = new AndroidObservable();
        androidObservable.addObserver(dog);

        //使用MutableLiveData对对象进行观察，对象变化时，会有相应的回调
        dogMutableLiveData = new MutableLiveData<>();
        dogMutableLiveData.setValue(dog);
        dogMutableLiveData.observe(this, dog -> {
            Log.d(TAG, "onChanged");
            Log.d(TAG, "MutableLiveData:  Dog 发生变化");
            Log.d(TAG, "Dog的name" + dog.getName());
            Toast.makeText(this, "MutableLiveData:  Dog 发生变化", Toast.LENGTH_LONG).show();
        });
    }

    public void update(View view) {
        //回调在实体类的update(Observable o, Object arg)
        androidObservable.update(System.currentTimeMillis() + "");

        //回调在dogMutableLiveData的onChanged方法
        dogMutableLiveData.setValue(new Dog());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}