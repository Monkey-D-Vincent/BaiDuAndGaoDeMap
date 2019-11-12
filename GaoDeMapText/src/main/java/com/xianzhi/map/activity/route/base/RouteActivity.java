package com.xianzhi.map.activity.route.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.xianzhi.map.R;
import com.xianzhi.map.activity.route.FutureRouteActivity;
import com.xianzhi.map.base.BaseActivity;

import butterknife.OnClick;

/**
 * @author LiMing
 * @Demo class RouteActivity
 * @Description TODO
 * @date 2019-11-01 17:07
 */
public class RouteActivity extends BaseActivity {

    @Override
    protected void onCreateView() {
        setContentView(R.layout.activity_route);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.tv_walkRoute, R.id.tv_busRoute, R.id.tv_ridingRoute, R.id.tv_futureRoute, R.id.tv_truceRoute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_walkRoute:
                break;
            case R.id.tv_busRoute:
                break;
            case R.id.tv_ridingRoute:
                break;
            case R.id.tv_futureRoute:
                startActivity(new Intent(context, FutureRouteActivity.class));
                break;
            case R.id.tv_truceRoute:
                Toast.makeText(context, "暂未完成",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
