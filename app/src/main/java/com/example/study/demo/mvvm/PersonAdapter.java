package com.example.study.demo.mvvm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.BR;
import com.example.study.R;
import com.example.study.databinding.ItemDtabindingRecycBinding;

import java.util.ArrayList;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

    private final String TAG = this.getClass().getSimpleName();


    private final ArrayList<Person> people;
    private final Context context;

    public PersonAdapter(ArrayList<Person> people, Context context) {
        this.people = people;
        this.context = context;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemDtabindingRecycBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_dtabinding_recyc, parent, false);

        return new PersonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = people.get(position);
        ViewDataBinding binding = DataBindingUtil.getBinding(holder.itemView);
        if (person != null && binding != null) {
            binding.setVariable(BR.person, person);
            binding.executePendingBindings();
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: " + (people == null ? 0 : people.size()));
        return people == null ? 0 : people.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        private final ItemDtabindingRecycBinding binding;

        public PersonViewHolder(@NonNull ItemDtabindingRecycBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemDtabindingRecycBinding getBinding() {
            return binding;
        }
    }
}
