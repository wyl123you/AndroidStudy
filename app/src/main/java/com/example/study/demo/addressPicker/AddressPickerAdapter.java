package com.example.study.demo.addressPicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressPickerAdapter extends RecyclerView.Adapter<AddressPickerAdapter.AddressPickerAdapterHolder> {

    private ArrayList<Province> provinces;
    private Context context;

    public AddressPickerAdapter(ArrayList<Province> provinces, Context context) {
        this.provinces = provinces;
        this.context = context;
    }


    @NonNull
    @Override
    public AddressPickerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.text, parent, false);
        return new AddressPickerAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressPickerAdapterHolder holder, int position) {
        Province province = provinces.get(position);

        holder.choice.setText(province.getName());

        ViewGroup.LayoutParams lp = holder.choice.getLayoutParams();
        lp.height = dp2px(context, 30);
    }

    @Override
    public int getItemCount() {
        return provinces == null ? 0 : provinces.size();
    }


    public static class AddressPickerAdapterHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.choice)
        public TextView choice;

        public AddressPickerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
