package com.xianzhi.java.demotest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TagFlowLayout mFlowLayout;
    private LinearLayout llContent;
    private Context context;
    private String json;

    private Map<String, String> ids = new HashMap<>();
    private Map<String, String> values = new HashMap<>();
    private Map<String, String> contents = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        initView();
    }

    private void initView() {
        llContent = findViewById(R.id.ll_content);
        initData();
//        findViewById(R.id.tv_click).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("aaaaaaaaaaaaaaaaaaaaaaa", ids.toString());
//                Log.i("aaaaaaaaaaaaaaaaaaaaaaa", values.toString());
//                Log.i("aaaaaaaaaaaaaaaaaaaaaaa", contents.toString());
//            }
//        });
    }

    private void initData() {
        json = getResources().getString(R.string.content);
        final DailyWorkValuesBean bean = new Gson().fromJson(json, DailyWorkValuesBean.class);
        for (int i = 0; i < bean.getQuestionList().size(); i++) {
            final View view = LayoutInflater.from(context).inflate(R.layout.view_content, null);
            mFlowLayout = view.findViewById(R.id.id_flowlayout);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            final EditText etContent = view.findViewById(R.id.et_content);
            tvTitle.setText(bean.getQuestionList().get(i).getDesc());
            mFlowLayout.setAdapter(new TagAdapter<DailyWorkValuesBean.QuestionListBean.AnswerListBean>(bean.getQuestionList().get(i).getAnswerList()) {

                @Override
                public View getView(FlowLayout parent, int position, DailyWorkValuesBean.QuestionListBean.AnswerListBean answerListBean) {
                    TextView tvTitle = (TextView) LayoutInflater.from(context).inflate(R.layout.view_selector, mFlowLayout, false);
                    tvTitle.setText(answerListBean.getDesc());
                    return tvTitle;
                }
            });
            final int index = i;
            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    etContent.setVisibility(bean.getQuestionList().get(index).getAnswerList().get(position).getIs_share() == 1 ? View.VISIBLE : View.GONE);
                    ids.put(bean.getQuestionList().get(index).getId() + "", position + "");
                    values.put(bean.getQuestionList().get(index).getId() + "", bean.getQuestionList().get(index).getAnswerList().get(position).getVal() + "");
                    return true;
                }
            });
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    contents.put(bean.getQuestionList().get(index).getId() + "", etContent.getText().toString());
                }
            });

            llContent.addView(view);
        }
    }
}
