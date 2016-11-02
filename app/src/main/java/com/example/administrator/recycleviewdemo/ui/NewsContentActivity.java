package com.example.administrator.recycleviewdemo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.recycleviewdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/10/27.
 */

public class NewsContentActivity extends AppCompatActivity{
    @BindView(R.id.textView)
    TextView mtextview;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_content);
        ButterKnife.bind(this);
        mtextview.setText(getIntent().getStringExtra("docId"));
    }
}
