package com.hh.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hh.banner.AutoBanner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoBanner banner = findViewById(R.id.banner);

        ArrayList<String> list = new ArrayList<>();
        list.add("http://suo.im/5fq6QB");
        list.add("http://suo.im/4S7j4V");
        list.add("http://suo.im/4D963R");
        banner.setData(list);


    }
}
