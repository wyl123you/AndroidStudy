package com.example.study.demo.designpatterns;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午10:23
 * @Company LotoGram
 */

public class Singleton {

    private final String TAG = "Singleton";

    private volatile static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
