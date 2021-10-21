package com.example.study.demo.flow;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.widget.FlowLayout;

import java.util.Locale;

public class FlowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        TextView textView = findViewById(R.id.text1);
        FlowLayout flowLayout = findViewById(R.id.flow);


        for (int i = 0; i < 200; i++) {
            TextView textView1 = new TextView(this);
            textView1.setText(String.format(Locale.CHINA, "TextView%d", i));
            flowLayout.addView(textView1, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100));
        }


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FlowActivity.this, "aaaaaa", Toast.LENGTH_LONG).show();
            }
        });
    }
}