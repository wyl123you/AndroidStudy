package com.example.study.demo.designpatterns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/5/31 下午5:53
 * @Company LotoGram
 */

public class Person implements Cloneable {

    private String name;
    private int age;
    private ArrayList<String> books;

    public Person(String name, int age, ArrayList<String> books) {
        this.name = name;
        this.age = age;
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    public void addBook(String book) {
        books.add(book);
    }

    //    @NonNull
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        return super.clone();
//    }


    @NotNull
    @Override
    public Object clone() {
        Person person;
        try {
            person = (Person) super.clone();
            person.age = this.age;
            person.name = this.name;
            person.books = (ArrayList<String>) this.books.clone();
            return person;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", books=" + books +
                '}';
    }
}
