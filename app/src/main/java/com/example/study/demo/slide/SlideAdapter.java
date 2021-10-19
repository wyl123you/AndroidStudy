package com.example.study.demo.slide;

import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.MyApplication;
import com.example.study.R;
import com.example.study.demo.refreshRecyclerView.TouchCallBack;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: Wu Youliang
 * @CreateDate: 2021/10/18 下午2:24
 * @Company LotoGram
 */
public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> implements TouchCallBack {

    private ArrayList<String> str;

    public SlideAdapter(ArrayList<String> str) {
        this.str = str;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        String p = str.get(position);

        holder.content.setText(p);
    }

    @Override
    public int getItemCount() {
        return str == null ? 0 : str.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(str, fromPosition, toPosition);
        //局部刷新(移动)
        notifyItemMoved(fromPosition, toPosition);

        // vibrator.vibrate(2000);//振动两秒
        // 下边是可以使震动有规律的震动  -1：表示不重复 0：循环的震动
        //long[] pattern = { 200, 2000, 2000, 200, 200, 200 };
        //vibrator.vibrate(pattern, -1);
        Vibrator vibrator = (Vibrator) MyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);//振动两秒
    }

    @Override
    public void onItemDelete(int position) {
        //删除数据
        str.remove(position);
        //局部刷新(移除)
        notifyItemRemoved(position);
    }

    protected static class SlideViewHolder extends RecyclerView.ViewHolder {

        private TextView content;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
        }
    }
}
