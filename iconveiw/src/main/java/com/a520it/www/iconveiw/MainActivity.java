package com.a520it.www.iconveiw;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressView progress = (ProgressView) findViewById(R.id.progress);


        //设置进度
        progress.setMax(2000);
        progress.setProgress(1000);
        progress.setTv("50%");

        //设置背景属性, 才会进入onDraw方法
        progress.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}
