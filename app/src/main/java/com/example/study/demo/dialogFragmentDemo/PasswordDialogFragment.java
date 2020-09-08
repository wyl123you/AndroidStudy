package com.example.study.demo.dialogFragmentDemo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.study.R;

public class PasswordDialogFragment extends DialogFragment {

    private static final String TITLE = "title";
    private static final String TIP = "tip";
    private static final String PASSWORD = "password";
    private static final String CANCELABLE = "cancelable";

    private OnButtonClickListener listener;

    public static PasswordDialogFragment newInstance(Builder builder) {
        PasswordDialogFragment fragment = new PasswordDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, builder.getTitle());
        bundle.putString(TIP, builder.getTip());
        bundle.putString(PASSWORD, builder.getPassword());
        bundle.putBoolean(CANCELABLE, builder.isCancelable());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_password, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.DialogFragment);
    }

    private void initView(View view) {
        TextView title = view.findViewById(R.id.title);
        TextView tip = view.findViewById(R.id.tip);
        TextView password = view.findViewById(R.id.et_password);
        Button cancel = view.findViewById(R.id.cancel);
        Button confirm = view.findViewById(R.id.confirm);

        Bundle bundle = getArguments();
        if (bundle != null) {
            title.setText(bundle.getString(TITLE, ""));
            tip.setText(bundle.getString(TIP, ""));
            setCancelable(bundle.getBoolean(CANCELABLE, false));
        }

        cancel.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onCancel();
            }
        });

        confirm.setOnClickListener(v -> {
            dismiss();
            if (listener != null) {
                listener.onConfirm(password.getText().toString());
            }
        });
    }

    public static class Builder {
        private String title;
        private String tip;
        private String password;
        private boolean cancelable;

        private String getTitle() {
            return title;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        private String getTip() {
            return tip;
        }

        public Builder setTip(String tip) {
            this.tip = tip;
            return this;
        }

        private String getPassword() {
            return password;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        private boolean isCancelable() {
            return cancelable;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public PasswordDialogFragment build() {
            return PasswordDialogFragment.newInstance(this);
        }
    }

    public interface OnButtonClickListener {
        void onConfirm(String password);

        void onCancel();
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }
}
