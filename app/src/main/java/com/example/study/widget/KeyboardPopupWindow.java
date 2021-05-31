package com.example.study.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/5/31 上午11:57
 * @Company LotoGram
 */

public class KeyboardPopupWindow extends PopupWindow {

    private static final String TAG = "MainActivity";

    private Context context;
    private View anchorView;
    private LinearLayout parentView;

    private EditText editText;

    private boolean isRandom = false;
    private ArrayList<Integer> list = new ArrayList<>();

    public KeyboardPopupWindow(Context context) {
        this.context = context;
        initConfig();
        initView();
    }

    public void setAnchor(View anchor) {
        this.anchorView = anchorView;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
        initConfig();
    }


    public KeyboardPopupWindow(Context context, View anchorView, EditText editText, boolean isRandom) {
        this.context = context;
        this.anchorView = anchorView;
        this.editText = editText;
        this.isRandom = isRandom;
        if (context == null || anchorView == null) {
            throw new IllegalArgumentException("context or anchorView can't be null");
        }
        initConfig();
        initView();
    }

    private void initConfig() {
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        forbidDefaultSoftKeyboard();
    }

    //禁止系统默认的软键盘弹出
    private void forbidDefaultSoftKeyboard() {
        if (editText == null) {
            return;
        }
        try {
            Class<EditText> clazz = EditText.class;
            Method method = clazz.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(editText, false);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //刷新自定义的PopupWindow是否outside可触摸反应
    //如果是不可触摸的，则显示该软键盘view


    public void refreshKeyboardOutSideTouchable(boolean touchable) {
        setOutsideTouchable(touchable);
        if (!touchable) {
            show();
        } else {
            dismiss();
        }
    }

    private void initView() {
        parentView = new LinearLayout(context);
        parentView.setOrientation(LinearLayout.VERTICAL);

        LinearLayout line1 = new LinearLayout(context);
        parentView.addView(line1);
        LinearLayout line2 = new LinearLayout(context);
        parentView.addView(line2);
        LinearLayout line3 = new LinearLayout(context);
        parentView.addView(line3);

        Button button7 = new Button(context);
        button7.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button8 = new Button(context);
        button8.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button9 = new Button(context);
        button9.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        line3.addView(button7);
        line3.addView(button8);
        line3.addView(button9);

        Button button4 = new Button(context);
        button4.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button5 = new Button(context);
        button5.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button6 = new Button(context);
        button6.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        line2.addView(button4);
        line2.addView(button5);
        line2.addView(button6);

        Button button1 = new Button(context);
        button1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button2 = new Button(context);
        button2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        Button button3 = new Button(context);
        button3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
        line1.addView(button1);
        line1.addView(button2);
        line1.addView(button3);

        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setContentView(parentView);
    }

    public void show() {
        if (isShowing()) {
            Log.d(TAG, "isShowing");
        }
        if (!isShowing() && anchorView != null) {
            //doRandomSortOp();
            showAtLocation(anchorView, Gravity.BOTTOM, 0, 0);
        }
    }

    public void release() {
        this.dismiss();
        context = null;
        anchorView = null;
        if (list != null) {
            list.clear();
            list = null;
        }
    }
}