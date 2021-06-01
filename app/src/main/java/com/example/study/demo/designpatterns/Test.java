package com.example.study.demo.designpatterns;

import java.util.ArrayList;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/6/1 上午9:14
 * @Company LotoGram
 */

public class Test {

    public static void main(String[] args) throws CloneNotSupportedException {

        ArrayList<String> books = new ArrayList<>();
        books.add("aaa");
        books.add("bbb");
        Person person = new Person("邬友亮 ", 24, books);

        Person pp = (Person) person.clone();
        pp.addBook("ccc");
        pp.setName("wwwwwwwww");

        System.out.println(pp.toString());
        System.out.println(person.toString());

        System.out.println(person == pp);
    }
}
