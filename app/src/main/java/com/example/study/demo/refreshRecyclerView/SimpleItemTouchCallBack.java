package com.example.study.demo.refreshRecyclerView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.MyApplication;

public class SimpleItemTouchCallBack extends ItemTouchHelper.Callback {

    private final TouchCallBack mCallBack;

    public SimpleItemTouchCallBack(TouchCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    // 返回可以滑动的方向
    // 一般使用makeMovementFlags(int,int)
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        //允许上下拖拽
        int drag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //允许向左滑动
        int swipe = ItemTouchHelper.LEFT;
        //设置
        return makeMovementFlags(drag, swipe);
    }

    // drag状态下，在canDropOver()返回true时
    // 会调用该方法让我们拖动换位置的逻辑(需要自己处理变换位置的逻辑)
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (mCallBack != null) {
            //通知适配器,两个子条目位置发生改变
            mCallBack.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            Toast.makeText(MyApplication.getInstance(), "onMove", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    // 针对drag状态，当前target对应的item是否允许移动
    // 我们一般用drag来做一些换位置的操作
    // 就是当前对应的target对应的Item可以移动
    @Override
    public boolean canDropOver(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder current, @NonNull RecyclerView.ViewHolder target) {
        int position = target.getAdapterPosition();
        return position % 2 == 0;
    }

    // 针对swipe状态，swipe 到达滑动消失的距离回调函数,一般在这个函数里面处理删除item的逻辑
    // 确切的来讲是swipe item滑出屏幕动画结束的时候调用
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mCallBack != null) {
            Toast.makeText(MyApplication.getInstance(), "onSwiped", Toast.LENGTH_SHORT).show();
            //通知适配器,子条目删除
            mCallBack.onItemDelete(viewHolder.getAdapterPosition());
        }
    }

    // 针对drag状态，当drag ItemView跟底下ItemView重叠时，可以给drag ItemView设置一个Margin值
    // 让重叠不容易发生，相当于增大了drag Item的区域
    @Override
    public int getBoundingBoxMargin() {
        return 200;
    }

    // 针对drag状态，当滑动超过多少就可以出发onMove()方法(这里指onMove()方法的调用，并不是随手指移动的View)
    @Override
    public float getMoveThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
    }

    // 针对swipe和drag状态，当手指离开之后，view回到指定位置动画的持续时间(swipe可能是回到原位，也有可能是swipe掉)
    public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
        return 3000;
    }

    // 针对swipe状态，swipe滑动的位置超过了百分之多少就消失
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return .9f;
    }

    // 针对swipe状态，swipe的逃逸速度
    // 换句话说就算没达到getSwipeThreshold设置的距离
    // 达到了这个逃逸速度item也会被swipe消失掉
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 10000;
    }

    //针对swipe状态，swipe滑动的阻尼系数,设置最大滑动速度
    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 1;
    }

    //支持长按拖动,默认是true
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    //支持滑动,即可以调用到onSwiped()方法,默认是true
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}

