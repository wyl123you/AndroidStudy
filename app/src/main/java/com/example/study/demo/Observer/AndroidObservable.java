package com.example.study.demo.Observer;

import java.util.Observable;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午11:35
 * @Company LotoGram
 */

public class AndroidObservable extends Observable {


    public void update(String msg) {
        setChanged();
        notifyObservers(msg);
    }
}
