package com.example.tayo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class Intro extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        setContentView(layout);
        layout.setBackgroundResource(R.drawable.intro);
        setContentView(layout);

        new Thread() {
            public void run() {
                try {
                    Thread.sleep(1300);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(Intro.this, MainActivity.class);
                startActivity(i);
                finish(); //현재화면 종료
            }
        }.start();
    }
}
