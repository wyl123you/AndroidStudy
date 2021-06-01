package com.example.study.demo.designpatterns;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午9:11
 * @Company LotoGram
 */

public class Master {

    private static Master master;

    private Person person;

    private Master() {
    }

    public static Master getMaster() {
        if (master == null) {
            master = new Master();
        }
        return master;
    }

    protected void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() throws CloneNotSupportedException {
        return (Person) person.clone();
    }
}
