package com.example.study.demo.retrofit;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.math.BigDecimal;


public class LuckyMoney extends BaseObservable {

    private Integer id;

    private BigDecimal money;

    private String producer;

    private String consumer;

    public LuckyMoney() {
    }

    public LuckyMoney(Integer id, BigDecimal money, String producer, String consumer) {
        this.id = id;
        this.money = money;
        this.producer = producer;
        this.consumer = consumer;
    }

    @Bindable
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
        //只更新本字段
//        notifyPropertyChanged(BR.luckyMoney);
        //更新所有字段
        notifyChange();
    }

    @Bindable
    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
        //只更新本字段
//        notifyPropertyChanged(BR.luckyMoney);
        //更新所有字段
        notifyChange();
    }

    @Bindable
    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
        //只更新本字段
//        notifyPropertyChanged(BR.luckyMoney);
        //更新所有字段
        notifyChange();
    }

    @Bindable
    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
        //只更新本字段
//        notifyPropertyChanged(BR.luckyMoney);
        //更新所有字段
        notifyChange();
    }

    @NonNull
    @Override
    public String toString() {
        return "LuckyMoney{" +
                "id=" + id +
                ", money=" + money +
                ", producer='" + producer + '\'' +
                ", consumer='" + consumer + '\'' +
                '}';
    }
}
