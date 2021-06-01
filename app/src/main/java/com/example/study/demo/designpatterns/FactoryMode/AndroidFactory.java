package com.example.study.demo.designpatterns.FactoryMode;

import org.jetbrains.annotations.NotNull;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午10:40
 * @Company LotoGram
 */

public class AndroidFactory extends Factory {

    @Override
    public <T extends Phone> T createPhone(@NotNull Class<T> clazz) {
        Phone phone = null;
        try {
            phone = (Phone) Class.forName(clazz.getName()).newInstance();
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) phone;
    }
}
