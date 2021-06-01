package com.example.study.demo.designpatterns.FactoryMode;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午10:45
 * @Company LotoGram
 */

public class Test {

    public static void main(String[] args) {

        AndroidFactory factory = new AndroidFactory();

        Xiaomi xiaomi = factory.createPhone(Xiaomi.class);
        Oppo oppo = factory.createPhone(Oppo.class);

        xiaomi.powerOnPhone();
        oppo.powerOnPhone();
    }


}
