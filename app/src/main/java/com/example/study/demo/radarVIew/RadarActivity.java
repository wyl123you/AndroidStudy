package com.example.study.demo.radarVIew;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.study.R;
import com.example.study.widget.SweepImageView;

public class RadarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar);
    }

    public void onStart(View view) {
        SweepImageView a = findViewById(R.id.aaaaa);
        SweepImageView b = findViewById(R.id.bbbbb);
        a.startCountDown();
        b.startCountDown();
    }

    public void onStop(View view) {
        SweepImageView a = findViewById(R.id.aaaaa);
        SweepImageView b = findViewById(R.id.bbbbb);
        a.stopCountDown();
        b.stopCountDown();
    }
}