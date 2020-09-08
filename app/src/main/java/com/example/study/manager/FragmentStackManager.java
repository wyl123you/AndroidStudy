package com.example.study.manager;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentStackManager {

    private static ArrayList<Fragment> fragments = new ArrayList<>();

    public static ArrayList<Fragment> getAllFragment() {
        return fragments;
    }

    public static void destroyAllFragment() {
        for (Fragment fragment : getAllFragment()) {
            fragment.onDestroy();
        }
    }
}
