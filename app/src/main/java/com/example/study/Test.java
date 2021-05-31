package com.example.study;

import com.example.study.demo.designpatterns.Person;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws CloneNotSupportedException {

        ArrayList<String> books = new ArrayList<>();
        books.add("a");
        books.add("b");


        Person person = new Person("wuyoul ", 34, books);

        Person pp = (Person) person.clone();
        pp.addBook("cccccc");

        pp.setName("wwwwwwwww");

        System.out.println(pp.toString());
        System.out.println(person.toString());

        System.out.println(person == pp);

    }

}
