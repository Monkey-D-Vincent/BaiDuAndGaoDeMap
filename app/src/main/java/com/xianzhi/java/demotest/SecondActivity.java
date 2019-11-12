package com.xianzhi.java.demotest;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

/**
 * @author LiMing
 * @Demo class SecondActivity
 * @Description TODO
 * @date 2019/9/17 上午11:48
 */

public class SecondActivity extends AppCompatActivity implements GoodSelectAdapter.OnTextClickListener {

    private MyListView lvGood;
    private GoodSelectAdapter adapter;
    private Context context;
    private String json;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        lvGood = findViewById(R.id.lv_good);

        json = getResources().getString(R.string.content);
        final DailyWorkValuesBean bean = new Gson().fromJson(json, DailyWorkValuesBean.class);
        adapter = new GoodSelectAdapter(context, bean.getQuestionList(), this);
        lvGood.setAdapter(adapter);
        finish();
    }

    @Override
    public void onClick(String title, String text) {
        Log.i("aaaaaaaaaaaaaaaaaaaaaaaaaaaa", title + "       " + text);
    }
}
