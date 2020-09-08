package com.example.study.demo.refreshRecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;

import java.util.ArrayList;
import java.util.Collections;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements TouchCallBack, View.OnClickListener, View.OnLongClickListener {

    private Context context;
    private ArrayList<String> data;

    public ListAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String a = data.get(position);

        holder.itemView.setTag(position);

        if (a != null) {
            holder.textView.setText(a + "    " + position);
        }

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        //交换位置
        Collections.swap(data, fromPosition, toPosition);
        //局部刷新(移动)
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDelete(int position) {
        //删除数据
        data.remove(position);
        //局部刷新(移除)
        notifyItemRemoved(position);
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (listener != null) {
            listener.onItemLongClick(v, (int) v.getTag());
        }
        return true;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textView = itemView.findViewById(R.id.text);
        }
    }

    public void addData(ArrayList<String> list) {
        for (String a : list) {
            if (a != null) {
                data.add(a);
            }
        }
        notifyDataSetChanged();
    }

    public void refreshData(ArrayList<String> sss) {
        data.clear();
        for (String a : sss) {
            if (a != null) {
                data.add(a);
            }
        }
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
