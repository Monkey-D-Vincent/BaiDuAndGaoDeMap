package com.xianzhi.baidumap.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * @author LiMing
 * @Demo class BaseActivity
 * @Description TODO
 * @date 2019-09-25 14:03
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        onCreateView(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    protected abstract void onCreateView(@Nullable Bundle savedInstanceState);

    protected abstract void initView();
}
