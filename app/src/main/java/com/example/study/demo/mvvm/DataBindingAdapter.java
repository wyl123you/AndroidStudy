package com.example.study.demo.mvvm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.BR;
import com.example.study.R;

import java.util.ArrayList;

public class DataBindingAdapter extends RecyclerView.Adapter<DataBindingAdapter.ViewHolder> {

    private final ArrayList<Person> people;
    private final Context context;

    public DataBindingAdapter(ArrayList<Person> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_dtabinding_recyc, parent, false);

        View view = binding.getRoot();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Person person = people.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (person != null && binding != null) {
            binding.setVariable(BR.person, person);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        return people == null ? 0 : people.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}