package com.example.study.demo.designpatterns.FactoryMode;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午10:39
 * @Company LotoGram
 */

public abstract class Factory {

    public abstract <T extends Phone> T createPhone(Class<T> clazz);

}