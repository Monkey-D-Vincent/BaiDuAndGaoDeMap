package com.xianzhi.map.activity.track;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.xianzhi.map.R;
import com.xianzhi.map.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class TrackActivity
 * @Description TODO
 * @date 2019-11-06 11:13
 */
public class TrackActivity extends BaseActivity {

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_track);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_running, R.id.tv_history})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_running:
                startActivity(new Intent(context, TrackRunningActivity.class));
                break;
            case R.id.tv_history:
                startActivity(new Intent(context, TrackHistoryActivity.class));
                break;
        }
    }
}
